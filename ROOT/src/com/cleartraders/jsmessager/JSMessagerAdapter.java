package com.cleartraders.jsmessager;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.cleartraders.jsmessager.common.ConnectionLoop;
import com.cleartraders.jsmessager.common.ExtendedMessageListener;
import com.cleartraders.jsmessager.common.FeedMessage;
import com.cleartraders.jsmessager.common.JMSHandler;
import com.cleartraders.jsmessager.common.SubscribedItemAttributes;
import com.lightstreamer.interfaces.data.DataProviderException;
import com.lightstreamer.interfaces.data.FailureException;
import com.lightstreamer.interfaces.data.ItemEventListener;
import com.lightstreamer.interfaces.data.SmartDataProvider;
import com.lightstreamer.interfaces.data.SubscriptionException;

/**
 * This file is a lightstreamer Adapater used for "JS Messager"
 * @author  Peter at PowerTeam
 *
 */
public class JSMessagerAdapter implements SmartDataProvider, ExtendedMessageListener
{

    private String loggerName;

    private Logger logger;

    // JMS messager Handler
    private JMSHandler jmsHandler;

    // Item event listener
    private ItemEventListener listener;

    // All subscribe Item
    private ConcurrentHashMap<String, SubscribedItemAttributes> subscribedItems = new ConcurrentHashMap<String, SubscribedItemAttributes>();

    // Unique Handle ID
    private volatile int nextHandleId = 1;

    // Handle Map
    private ConcurrentHashMap<String, Object> handles = new ConcurrentHashMap<String, Object>();

    // Message Pool size
    private int msgPoolSize;

    private int recoveryPause;

    // a read/write lock is used
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(false);

    // this is the queue of pending requests for the Generator
    private ConcurrentLinkedQueue<String> toSendRequests = new ConcurrentLinkedQueue<String>();

    // this map will update every subscribed item setting the item_status field
    // to inactive
    private static HashMap<String, String> inactiveMap = new HashMap<String, String>();

    // this map will update every subscribed item setting the item_status field
    // to inactive. This map
    // is used in case of snapshot updates
    private static HashMap<String, String> completeInactiveMap = new HashMap<String, String>();

    /*
    SIGNAL
    signal_type     int unsigned not null, #0:BUY 1:SELL 2:Scalper
    market_type_id  int unsigned not null, 
    signal_period   int unsigned not null,
    generate_date   double not null,
    signal_value    double not null,
    expire_date     double not null,
    signal_rate     int unsigned not null,
    direction       int unsigned not null, #0:BUY 1:SELL
    profit          double not null, 
    checked         int  (This is not DB field, but background notified this field)
    system_name     varchar(100)
    */
    
    static
    {
        inactiveMap.put("item_status", "inactive");

        // Only for snapshot
        completeInactiveMap.put("game_name", "-");
        completeInactiveMap.put("time", "0");
        completeInactiveMap.put("user_name", "-");
        completeInactiveMap.put("user_ip", "-");
        completeInactiveMap.put("message_text", "-");
        completeInactiveMap.put("message_type", "-");
        completeInactiveMap.put("item_status", "inactive");
    }

    // ////////////////DataProvider
    /*
     * called by Lightstreamer Kernel on start
     */
    public void init(Map params, File arg1) throws DataProviderException
    {
        // load configuration

        // load logger category name
        loggerName = getParam(params, "loggerName", false,"LightstreamerLogger.StockQuotesJMSAdapter");
        
        // get the logger
        logger = Logger.getLogger(loggerName);

        // load JMS connections parameters
        String providerURL = getParam(params, "jmsUrl", true, null);
        String initialContextFactory = getParam(params, "initialContextFactory", true, null);
        String topicConnectionFactory = getParam(params, "topicConnectionFactory", true, null);
        String queueConnectionFactory = getParam(params, "queueConnectionFactory", true, null);
        String topic = getParam(params, "topicName", true, null);
        String queue = getParam(params, "queueName", true, null);
        String jmsUserName = getParam(params, "jmsUserName", false, "");
        String jmsUserPassword = getParam(params, "jmsUserPassword", false, "");

        // the size of the message pool
        this.msgPoolSize = getParam(params, "msgPoolSize", false, 15);

        // in case of disconnection/failed_connection from/to JMS this is
        // the pause between each reconnection attempt
        this.recoveryPause = getParam(params, "recoveryPauseMillis", false, 2000);

        logger.debug("Configuration read.");

        // create the JMS handler. The object will handle the instantiation of
        // JMS-related objects
        jmsHandler = new JMSHandler(logger, initialContextFactory, providerURL,
                queueConnectionFactory, queue, topicConnectionFactory, topic,
                jmsUserName, jmsUserPassword);
        // the message listener that will receive JMS messages will be the
        // StockQuotesJMSAdapter instance (this)
        jmsHandler.setListener(this);

        // this thread keeps on trying to connect to JMS until succedes. When
        // connected
        // calls the onConnection method
        new ConnectionLoopTSQS(jmsHandler, recoveryPause, logger).start();

        logger.info("StockQuotesJMSAdapter ready.");

    }

    public void setListener(ItemEventListener listener)
    {
        // this listener will pass updates to Lightstreamer Kernel
        this.listener = listener;
    }

    /*
     * check for valid item names (item1 item2 item3....item30)
     */
    private boolean isValidItem(String itemName)
    {
        if (null == itemName || "".equals(itemName))
        {
            return false;
        }

        return true;
    }

    /*
     * called by Lightstreamer Kernel on item subscription
     */
    public void subscribe(String itemName, Object itemHandle,
            boolean needsIterator) throws SubscriptionException,
            FailureException
    {
        logger.info("Subscribing to " + itemName);

        // make some check to be sure we are subscribing to a valid item
        if (!isValidItem(itemName))
        {
            // not a valid item
            throw new SubscriptionException("(Subscribing) Unexpected item: " + itemName);
        }

        logger.info("(Subscribing) Valid item: " + itemName);

        // Generate an unique ID to represent the itemHandle object. This ID
        // will be then
        // sent to the Generator which in turn will return it on each item
        // update so that
        // the correct handle can be passed to the smartUpdate method.
        String uniqueId = String.valueOf(nextHandleId++);

        // create an object to contain some basic attributes for the item
        // this item will be useful for snapshot handling and unsubscription
        // calls
        SubscribedItemAttributes itemAttrs = new SubscribedItemAttributes(itemName, uniqueId);

        // get the writelock to write inside the maps
        rwLock.writeLock().lock();
        logger.info("------------------>Write LOCK 1");
        // insert item in the list of subscribed items
        // the item name will be the key, the attributes-object will be the
        // value,
        subscribedItems.put(itemName, itemAttrs);
        logger.info("Put into subscribedItems, and subscribedItems has "+subscribedItems.size()+ "items!");
        // insert the handle in a map with the generated unique id as the key
        handles.put(uniqueId, itemHandle);

        boolean dispatchThread = false;
//        if (lastHeartbeatRandom == -1)
//        {
            // JMS is not available now, send the inactive flag to the clients
            // since this call is non-blocking we can issue it here
            dispatchInactiveFlag(itemAttrs);
//        }
//        else
//        {
//            // insert the subscription request to be dispatched to the Generator
//            // via JMS.
//            // This request asks the Simulator to start dispatching the data
//            // flow
//            // for this item. Note that it "enables" Generator to send data for
//            // this item
//            // and not to "generate" values for this item. In fact, Generator
//            // begins the
//            // production of values for all the items on startup.
//            toSendRequests.offer("subscribe" + itemName + "_" + uniqueId);
//            dispatchThread = true;
//        }

        // release the lock
        logger.info("------------------>Write UNLOCK 1");
        rwLock.writeLock().unlock();

        logger.debug("(Subscribing) Inserted in subscribed items list: "
                + itemName + " (" + uniqueId + ")");

        if (dispatchThread)
        {
            // Start a thread to send the subscribe request to the Generator.
            // We should use something better like a pool of threads here, but
            // for simplicity we start a new
            // thread each time we place some new request to be sent to the
            // Generator inside the queue
            new SenderThread().start();
        }
    }

    /*
     * called by Lightstreamer Kernel on item subscription if implementing a
     * DataProvider (this is a SmartDataProvider so it is never called)
     */
    public void subscribe(String itemName, boolean needsIterator)
            throws SubscriptionException, FailureException
    {
        // NEVER CALLED
    }

    /*
     * called by Lightstreamer Kernel on item unsubscription
     */
    public void unsubscribe(String itemName) throws SubscriptionException,
            FailureException
    {
        logger.info("Unsubscribing from " + itemName);

        // get the writelock to check if the item is subscribed
        // and to eventually delete it
        rwLock.writeLock().lock();
        logger.debug("------------------>Write LOCK 2");
        // check if this is a subscribed item.
        if (!subscribedItems.containsKey(itemName))
        {
            // before throw an exception must release the lock
            logger.debug("------------------>Write UNLOCK 2");
            rwLock.writeLock().unlock();
            
            // not subscribed item, throw an exception
            throw new SubscriptionException("(Unsubscribing) Unexpected item: " + itemName);
        }
        
        // get the object representing the unsubscribing item
        SubscribedItemAttributes item = subscribedItems.get(itemName);
        // remove the item from the subscribed items map
        subscribedItems.remove(itemName);
        // remove the handle from the handles map
        handles.remove(item.handleId);

        boolean dispatchThread = false;

        // release the lock
        logger.debug("------------------>Write UNLOCK 2");
        rwLock.writeLock().unlock();

        logger.debug("(Unsubscribing) removed from subscribed items list:"
                + itemName + " (" + item.handleId + ")");

        if (dispatchThread)
        {
            // Start a thread to send the unsubscribe request to the Generator.
            // We should use something better like a pool of threads here, but
            // for simplicity we start a new
            // thread each time we place some new request to be sent to the
            // Generator inside the queue
            new SenderThread().start();
        }
    }

    /*
     * called by Lightstreamer Kernel to know if the snapshot is available for
     * an item
     */
    public boolean isSnapshotAvailable(String itemName)
            throws SubscriptionException
    {
        // we generate the updates so we can make snapshots always available
        // (even if JMS is down
        // we generate locally an "inactive" update)
        return true;
    }

    /*
     * called to dispatch the inactive update to a subscribed item. Two
     * consideration are needed: 1-This method should be better implemented in
     * an asyncronous manner (i.e. put the updates in a queue and wait for a
     * thread that dequeues it). In that case, also the updates received from
     * the onMessage event should be queued in the same way. 2-As this method is
     * alway called by a method that already owns the write lock, we don't get
     * any lock here, (it would be better if there was a test here that gets a
     * lock if the running thread doesn't own one).
     */
    private void dispatchInactiveFlag(SubscribedItemAttributes item)
    {
        // get the information about the snapshot status of the item (i.e.
        // whether was already sent or not)
        boolean isSnapshot = !item.isSnapshotSent;
        if (isSnapshot)
        {
            item.isSnapshotSent = true;
        }

        Object handle = handles.get(item.handleId);
        if (handle != null)
        {
            if (isSnapshot)
            {
                logger.info("Prepare to send Snapshot message!");

                // send a complete snapshot with empty fields (apart from the
                // item_status event set to "inactive")
                listener.smartUpdate(handle, completeInactiveMap, isSnapshot);
                // Note that if the adapter does not know the schema for the
                // items, here we should send an incomplete
                // update with the isSnapshot flag set to false (look at
                // isSnapshotAvailable method's comments)
            }
            else
            {
                // send an update containing only the item_status field set to
                // "inactive"
                listener.smartUpdate(handle, inactiveMap, isSnapshot);
            }
        }
        logger.debug("Inactive flag dispatched: " + item.itemName + " ("+ item.handleId + ")");

    }

    // /////////MessageListener

    private static final String noCompMex = "Message received was not compatible with this adapter. Maybe someone else sending messages?";

    /*
     * called by ConnectionLoop on connection with JMS
     */
    public void onConnection()
    {
        // get the write lock to set the jmsOk flag to true
        rwLock.writeLock().lock();
        logger.debug("------------------>Write LOCK 3");
        logger.info("JMS is now up");
        // release the lock
        logger.debug("------------------>Write UNLOCK 3");
        rwLock.writeLock().unlock();
    }

    /*
     * As this method is alway called by a method that already owns the write
     * lock, we don't get any lock here (it would be better if there was a test
     * here that gets a lock if the running thread doesn't own one).
     */
    public void subscribeAll()
    {
        // we have to request the Generator all the currently subscribed items.
        // Just before we send a reset message
        // to stop the Generator to send old items (since the Generator has no
        // logic to know if Lightstreamer is
        // up or down, if Lightstreamer falls and then gets back alive the
        // Generator could have items subscribed by the
        // old life of this adapter)

        logger.debug("Subscribing all to the Generator");
        // Any previous request will be reissued so we have to clear the queue
        // in order to avoid
        // duplicate requests.
        toSendRequests.clear();
        // send a reset message to shut down all possible old subscription
        toSendRequests.offer("reset");
        // iterate through the subscribedItem to issue one subscription request
        // per each subscribed item
        Enumeration<SubscribedItemAttributes> subItems = subscribedItems
                .elements();
        while (subItems.hasMoreElements())
        {
            SubscribedItemAttributes sia = subItems.nextElement();
            // put the subscription request inside the queue
            toSendRequests.offer("subscribe" + sia.itemName + "_"
                    + sia.handleId);
        }

        // Start a thread to send the subscribe request to the Generator.
        // We should use something better like a pool of threads here, but for
        // simplicity we start a new
        // thread each time we place some new request to be sent to the
        // Generator inside the queue
        new SenderThread().start();
    }

    /*
     * called whenever the connection to JMS is lost
     */
    public void onException(JMSException je)
    {
        logger.error("onException: JMSException -> " + je.getMessage());

        // get the write lock in order to set the jmsOk flag and to call the
        // onFeedDisconnection method
        rwLock.writeLock().lock();
        logger.debug("------------------>Write LOCK 4");
        logger.info("JMS is now down");
        logger.debug("------------------>Write UNLOCK 4");
        // release the lock
        rwLock.writeLock().unlock();

        // start loop to try to reconnect
        new ConnectionLoopTSQS(jmsHandler, recoveryPause, logger).start();

    }

    /*
     * called in case the feed is lost (i.e. Generator is down or JMS connection
     * is down) As this method is alway called by a method that already owns the
     * write lock, we don't get any lock here, (it would be better if there was
     * a test here that gets a lock if the running thread doesn't own one).
     */
    public void onFeedDisconnection()
    {
        logger.info("Feed no more available");
        // we iterates through the subscribedItem to send the "incative" field
        // per each subscribed item
        Enumeration<SubscribedItemAttributes> subItems = subscribedItems.elements();
        while (subItems.hasMoreElements())
        {
            SubscribedItemAttributes sia = subItems.nextElement();
            this.dispatchInactiveFlag(sia);
        }
    }

    /**
     * Parse the text message follow specify format
     *
     SIGNAL Message Format
     "item name (this is topic name,too)","signal",signal_type,market_type_id,signal_period,generate_date,
     signal_value,expire_date,signal_rate,direction,profit,checked,system_name,strategy_id
     
     Table Define:
     {
         signal_type     int unsigned not null, #0:BUY 1:SELL 2:Scalper
         market_type_id  int unsigned not null, 
         signal_period   int unsigned not null,
         generate_date   double not null,
         signal_value    double not null,
         expire_date     double not null,
         signal_rate     int unsigned not null,
         direction       int unsigned not null, #0:BUY 1:SELL
         profit          double not null, 
         checked         int  (This is not DB field, but background notified this field)
         system_name     varchar(100)
         strategy_id     int unsigned not null,
     }

     * @param strMsg
     * @return
     */
    private FeedMessage parseTextToFeedMsg(String strMsg)
    {
        if(null == strMsg)
        {
            return null;
        }
        
        FeedMessage feedMsg = null;
        
        String[] textList = strMsg.split(",");        

        if (textList.length == 20)
        {
            logger.info("Receive signal message!");
            
            feedMsg = createSignalFeedMessage(textList);
        }
        else if(textList.length == 6)
        {
            feedMsg = createQuoteFeedMessage(textList);
        }
        else
        {
            logger.warn("Received a text message which doesn't has right format. Message is: "+strMsg);
            
            return null;
        }

        return feedMsg;
    }
    
    private FeedMessage createQuoteFeedMessage(String[] textList)
    {
        /*
        Ticker Quote Format as below:
        
        Field                      Format
        
        1) market_topic            String  //Item Name and topic name
        2) mark                    String // ignore this one
        3) quote_date              long  //Time Stamp  MM/DD/YYYY HH:MM:SS,09/18/2008 12:42:38
        4) price_value             double
        5) volume_value            double
        6) other_value             double
        
        */
        FeedMessage feedMsg = null;
        
        String itemName = textList[0];   //market_topic     
        String quote_date = textList[2];
        String price_value = textList[3];
        String volume_value = textList[4];
        String other_value  = textList[5];
        
        final HashMap<String, String> currentValues = new HashMap<String, String>();
        currentValues.put("market_name", itemName);
        currentValues.put("quote_date", quote_date);
        currentValues.put("price_value", price_value);
        currentValues.put("volume_value", volume_value);
        currentValues.put("other_value", other_value);
        
        feedMsg = createFeedMessage(itemName,currentValues);
        
        if(null == feedMsg)
        {
            logger.info("Request item is " + itemName+", subscribedItems has "+subscribedItems.size()+ "items!");
            logger.warn("Received a text message which isn't subscribed. The market topic is " + itemName);
        }
        else
        {
            logger.info("Received a text message which isn't subscribed. The market topic is " + itemName);
        }
        
        return feedMsg;
    }
    
    private FeedMessage createSignalFeedMessage(String[] textList)
    {
        /*
        Signal Message Formatter:
        
        itemname
        id 
        signal_type 
        market_type_id 
        market_name 
        market_topic_name 
        signal_period 
        generate_date 
        generate_date_string 
        signalValue 
        expire_date 
        expire_date_string 
        signal_rate 
        direction 
        profit 
        profitString 
        checked 
        system_name 
        Strategy_id
        */
        FeedMessage feedMsg = null;
        String itemName = textList[0];
        
        String signal_id = textList[1];
        String signal_type = textList[2];
        String market_type_id = textList[3];
        String market_name  = textList[4];
        String market_topic_name = textList[5];
        String signal_period = textList[6];
        String generate_date = textList[7];
        String generate_date_string = textList[8];
        String signal_value = textList[9];
        String expire_date = textList[10];
        String expire_date_string = textList[11];
        String signal_rate = textList[12];
        String direction = textList[13];
        String profit = textList[14];
        String profitString = textList[15];
        String checked = textList[16];
        String system_name = textList[17];
        String signal_period_minutes = textList[18];
        String strategy_id = textList[19];
        
        final HashMap<String, String> currentValues = new HashMap<String, String>();
        currentValues.put("signal_id", signal_id);
        currentValues.put("signal_type", signal_type);
        currentValues.put("market_type_id", market_type_id);
        currentValues.put("signal_period", signal_period);
        currentValues.put("generate_date", generate_date);
        currentValues.put("signal_value", signal_value);
        currentValues.put("expire_date", expire_date);
        currentValues.put("signal_rate", signal_rate);            
        currentValues.put("direction", direction);
        currentValues.put("profit", profit);
        currentValues.put("checked", checked);
        currentValues.put("system_name", system_name);
        
        currentValues.put("market_name", market_name);            
        currentValues.put("market_topic_name", market_topic_name);
        currentValues.put("generate_date_string", generate_date_string);
        currentValues.put("expire_date_string", expire_date_string);
        currentValues.put("profitString", profitString);
        
        currentValues.put("signal_period_minutes", signal_period_minutes);
        currentValues.put("strategy_id", strategy_id);

        feedMsg = createFeedMessage(itemName,currentValues);
        
        if(null == feedMsg)
        {
            logger.info("Request item is " + itemName+", subscribedItems has "+subscribedItems.size()+ "items!");
            logger.warn("Received a text message which isn't subscribed. The signal type is " + signal_type + ", signal_value is " + signal_value);
        }
        else
        {
            logger.info("Parsed a text message. The signal type is " + signal_type + ", signal_value is " + signal_value);
        }
        
        return feedMsg;
    }
    
    private FeedMessage createFeedMessage(String itemName,HashMap<String, String> currentValues)
    {
        FeedMessage feedMsg = null;
        SubscribedItemAttributes item = null;
        
        if (subscribedItems.get(itemName) != null)
        {
            item = subscribedItems.get(itemName);

            feedMsg = new FeedMessage(itemName, currentValues, true,item.handleId, new Random().nextInt(1000));
        }
        
        return feedMsg;
    }

    /*
     * receive messages from JMS
     */
    public void onMessage(Message message)
    {
        if (message == null)
        {
            logger.warn(noCompMex + " (null)");
            return;
        }

        logger.info("Received message");
        
        // we have to extract data from the Message object
        FeedMessage feedMsg = null;

        SubscribedItemAttributes item = null;

        try
        {
            // add by Peter
            // Parse the message from topic if the message is a TextMessage
            if (message instanceof TextMessage)
            {
                TextMessage textMessage = (TextMessage) message;
                feedMsg = parseTextToFeedMsg(textMessage.getText());
                logger.info("Received Text Message");
            }
            else
            {
                // cast Message to ObjectMessage.
                ObjectMessage objectMessage = (ObjectMessage) message;
                // Obtain the contained Serializable object
                // try to cast it to HeartbeatMessage
                try
                {
                    feedMsg = (FeedMessage) objectMessage.getObject();
                    logger.info("Received Ojbect Message");
                }
                catch (ClassCastException jmse)
                {
                    
                    jmse.printStackTrace();
                }
            }

        }
        catch (ClassCastException jmse)
        {
            // if message isn't an ObjectMessage or message.getObject() isn't a
            // FeedMessage
            // then this update is not "correct"
            logger.warn(noCompMex + "(no FeedMessage instance)");
            return;
        }
        catch (JMSException jmse)
        {
            logger.error("StockQuotesJMSAdapter.onMessage - JMSException: "+ jmse.getMessage(), jmse);
            return;
        }

        // handle the update
        // get the read lock to access the subscribedItems map and the handles
        // map
        rwLock.readLock().lock();
        logger.info("------------------>Read LOCK 5");
        // It discards updates about items that are no more subscribed.
        if (null == feedMsg || !subscribedItems.containsKey(feedMsg.itemName))
        {
            // maybe the unsubscription message was lost?
            // or someone else is publishing updates?
            // or just a timing problem with the network?

            // release the lock and exit
            logger.info("------------------>Read UNLOCK 5");
            rwLock.readLock().unlock();
            logger.debug("Received update for not subscribed item: "
                    + feedMsg.itemName);
            return;
        }

        Object handle = null;
        boolean isSnapshot = false;

        // get the object that represents the item
        item = subscribedItems.get(feedMsg.itemName);
        if (item != null)
        {
            // get the handle that represents the item subscription
            handle = handles.get(item.handleId);
            if (handle == null)
            {
                // if the handle is not available it means that an
                // unsubscription and a
                // subsequent new subscription were issued by Lightstreamer
                // Kernel and
                // that this update is related to the old subscription, so even
                // if the
                // update could be valid, we choose to discard it

                // release the lock and exit
                logger.info("------------------>Read UNLOCK 5");
                rwLock.readLock().unlock();
                logger.debug("Received update for unsubscribed handle: "
                        + feedMsg.itemName + "(" + feedMsg.handleId + ")");
                return;
            }

            // Since the generator always sends a complete update (i.e. it does
            // not filter unchanged values)
            // we can handle the snapshot flag on the adapter side:
            // the feedMessage carries a isSnapshot flag but we ignore that flag
            // and set the isSnapshot flag
            // from the point of view of Lightstreamer Kernel
            if (!item.isSnapshotSent)
            {
                item.isSnapshotSent = true;
                isSnapshot = true;
            }
        }
        logger.debug("Received update for item " + feedMsg.itemName);
        logger.info("Received update for item " + feedMsg.itemName);
        // forward the update to Lightstreamer kernel
        listener.smartUpdate(handle, feedMsg.currentValues, isSnapshot);
        // release the lock
        logger.debug("------------------>Read UNLOCK 5");
        rwLock.readLock().unlock();

    }

    // ///////////////Utils
    private static String noParam = " is missing.\nProcess exits";

    private static String useDefault = " is missing. Using default.";

    private static String isNaN = " must be a number but it isn't. Using default.";

    private int getParam(Map params, String toGet, boolean required, int def)
            throws DataProviderException
    {
        int resInt;
        String res = (String) params.get(toGet);
        if (res == null)
        {
            if (required)
            {
                throw new DataProviderException(toGet + noParam);
            }
            else
            {
                if (logger != null)
                {
                    logger.warn(toGet + useDefault);
                }
                resInt = def;
            }
        }
        else
        {
            try
            {
                resInt = Integer.parseInt(res);
            }
            catch (NumberFormatException nfe)
            {
                if (logger != null)
                {
                    logger.error(toGet + isNaN);
                }
                resInt = def;
            }
        }

        if (logger != null)
        {
            logger.debug(toGet + ": " + resInt);
        }
        return resInt;
    }

    private String getParam(Map params, String toGet, boolean required,
            String def) throws DataProviderException
    {
        String res = (String) params.get(toGet);

        if (res == null)
        {
            if (required)
            {
                throw new DataProviderException(toGet + noParam);
            }
            else
            {
                if (logger != null)
                {
                    logger.warn(toGet + useDefault);
                }
                res = def;
            }
        }
        if (logger != null)
        {
            logger.debug(toGet + ": " + res);
        }
        return res;
    }

    // //////////////////ConnectionLoop

    private class ConnectionLoopTSQS extends ConnectionLoop
    {

        public ConnectionLoopTSQS(JMSHandler jmsHandler, int recoveryPause,
                Logger logger)
        {
            super(jmsHandler, recoveryPause, logger);
        }

        protected void onConnectionCall()
        {
            // call the connection handler
            onConnection();
        }

        protected void connectionCall() throws JMSException, NamingException
        {
            // initialize TopicSubscriber and QueueSender
            jmsHandler.initTopicSubscriber();
            jmsHandler.initQueueSender(msgPoolSize);
        }

    }

    public class SenderThread extends Thread
    {

        public void run()
        {
            String nextRequest = "";

            logger.debug("Dispatch thread started");
            // get the read lock to read the toSendRequests queue
            rwLock.readLock().lock();
            logger.debug("------------------>Read LOCK 8");
            // go on until there are requests in the queue
            while ((nextRequest = toSendRequests.poll()) != null)
            {
                try
                {
                    // send message to the feed through JMS
                    jmsHandler.sendMessage(nextRequest);
                    logger.debug("Message dispatched to JMS: " + nextRequest);
                }
                catch (JMSException je)
                {
                    logger.error("Can't actually dispatch request "
                            + nextRequest + ": JMSException -> "
                            + je.getMessage());
                }
            }
            // release the lock
            logger.debug("------------------>Read UNLOCK 8");
            rwLock.readLock().unlock();
            logger.debug("Dispatch thread ends");
        }

    }

}
