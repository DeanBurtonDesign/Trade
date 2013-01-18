package com.cleartraders.jsmessager.common;

import java.io.Serializable;
import java.util.ArrayList;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

public class JMSHandler {
	
	private Logger logger;
	
    private QueueSender queueSender;
    private QueueSession queueSession;
    private QueueConnection queueConnection;
    private Queue queue;
    private String queueName;
    private boolean queueSessionReady = false;
    
    private TopicPublisher topicPublisher;
    private TopicSession topicSession;
    private TopicConnection topicConnection;
    private Topic topic;
    private String topicName;
    private boolean topicSessionReady = false;
    
    private boolean JMSReady = false;
    
    private ExtendedMessageListener messageListener;
    
	private TextMessagePool textMessagePool;
	private ObjectMessagePool objectMessagePool;
	
	private String providerURL;
    
    //add by Peter
    private String jmsUserName=ActiveMQConnection.DEFAULT_USER;
    private String jmsUserPassword=ActiveMQConnection.DEFAULT_PASSWORD;
    //end by peter
	
	/*
	 * This object can handle:
	 * 1 TopicSubscriber and 1 TopicPublisher
	 * related to the same TopicConnectionFactory
	 * 1 QueueReceiver and 1 QueueSender
	 * related to the same QueueConnectionFactory
	 * All related to the same InitialContextFactory
	 */
	public JMSHandler(Logger logger, String initialContextFactory, String providerURL, String queueConnectionFactoryName, String queueName, String topicConnectionFactoryName, String topicName,String userName,String password) {
	
		
		this.logger = logger;
		
		this.providerURL = providerURL;
		
		this.topicName = topicName;
		
		this.queueName = queueName;
        
		//add by Peter            
        //get JMS user name
        if("".equals(userName))
        {
            jmsUserName = ActiveMQConnection.DEFAULT_USER;
        }
        else
        {
            jmsUserName = userName;
        }

        //get JMS user password
        if("".equals(password))
        {
            jmsUserPassword = ActiveMQConnection.DEFAULT_PASSWORD;
        }
        else
        {
            jmsUserPassword = password;
        }
		//end by Peter
        
		logger.debug("JMSHandler Ready");
			
	}
	
	public void setListener(ExtendedMessageListener messageListener) 
    {
		this.messageListener = messageListener;
	}
	
	/*
	 * initiate the InitialContext
	 */
	private synchronized void initJMS() throws JMSException, NamingException{
		if (JMSReady) {
			//InitialContext is already OK, exit
			return;
		}
        		
		//InitialContext is now ready
		JMSReady = true;
	}
	
	/*
	 * close all open Sessions/Connections and unset ready flags
	 */
	public synchronized void reset() {
        
        if (topicPublisher != null ){
            try {
                topicPublisher.close();
            } catch (JMSException e) {
            }
        }
        
		if (topicSession != null) {
			try {
				topicSession.close();
			} catch (JMSException e) {
			}
		}
        
        if (topicConnection != null) 
        {
            try {
                topicConnection.stop();
            } 
            catch (Exception e){
            }
            finally{
                try {
                    topicConnection.close();
                } catch (JMSException e) {
                }
            }
        }
                
        if (queueSender != null ){
            try {
                queueSender.close();
            } catch (JMSException e) {
            }
        }
                
		if (queueSession != null) {
			try {
				queueSession.close();
			} catch (JMSException e) {
			}
		}
        
        if (queueConnection != null) 
        {
            try {
                queueConnection.stop();
            } 
            catch (Exception e){
            }
            finally{
                try {
                    queueConnection.close();
                } catch (JMSException e) {
                }
            }
        }
                 
        topicPublisher = null;
		topicSession = null;
		topicConnection = null;
        
        queueSender = null;
		queueSession = null;
		queueConnection = null;
        
		JMSReady = false;
		queueSessionReady = false;
		topicSessionReady = false;
	}
	
	/*
	 * prepare the QueueSession
	 */
	private synchronized void initQueueSession() throws JMSException, NamingException {
		if (queueSessionReady) {
			//QueueSession is already OK, exit
			return;
		}

		initJMS();
		
        //add by Peter 2007/12/23
        QueueConnectionFactory queueConnectionFactory = new ActiveMQConnectionFactory(jmsUserName, jmsUserPassword, providerURL);;
        //end add by Peter 2007/12/23
        
		//get the QueueConnection from our QueueConnectionFactory
		queueConnection = queueConnectionFactory.createQueueConnection();
		logger.debug("Queue connection created");
		
		//if set we pass our ExtendedMessageListener to the QueueConnection as ExceptionListener
		if (messageListener != null) 
        {
			queueConnection.setExceptionListener(messageListener);
		}
		
		//get the QueueSession from our QueueConnectionFactory
		queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        logger.debug("Queue session created");
        
        this.queue = queueSession.createQueue(queueName);
        logger.debug("Queue created:"+queueName);
        
        //QueueSession is now ready
        queueSessionReady = true;
	}
	
	/*
	 * prepare the TopicSession
	 */
	private synchronized void initTopicSession() throws JMSException, NamingException {
		if (topicSessionReady) {
			//TopicSession is already OK, exit
			return;
		}
		//first of all we have to inititiate the InitialContext
		//(without this we can't instantiate a TopicSession)
		initJMS();
        
        //add by Peter 2007/12/23
        TopicConnectionFactory topicConnectionFactory = new ActiveMQConnectionFactory(jmsUserName, jmsUserPassword, providerURL);
        //end add by Peter 2007/12/23
        
		//get the TopicConnection from our TopicConnectionFactory
		topicConnection = topicConnectionFactory.createTopicConnection();
		logger.debug("Topic connection created");
		
		//if set we pass our ExtendedMessageListener to the TopicConnection as ExceptionListener
		if (messageListener != null) {
			topicConnection.setExceptionListener(messageListener);
		}
		
		//get the TopicSession from our TopicConnectionFactory
		topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        logger.debug("Topic session created");

        this.topic = topicSession.createTopic(topicName);
        logger.debug("Topic created:"+topicName);
        
        //TopicSession is now ready
        topicSessionReady = true;
	}
	
	/*
	 * prepare the QueueReceiver 
	 */
	public  synchronized void initQueueReceiver() throws JMSException, NamingException {
		//first of all we have to inititiate the QueueSession
		//(without this we can't instantiate a QueueReceiver)
		initQueueSession();
		
		//get the QueueReceiver from our QueueSession
		QueueReceiver queueReceiver = queueSession.createReceiver(queue);
		logger.debug("Queue receiver created");
		
		//if set we pass our ExtendedMessageListener to the QueueReceiver as MessageListener
		if (messageListener != null) {
			queueReceiver.setMessageListener(messageListener);
		}
		
		//start listening to JMS
		queueConnection.start();
		logger.debug("Queue connection started");
	}
	
	public synchronized void initQueueSender(int msgPoolSize) throws JMSException, NamingException {
		//first of all we have to inititiate the QueueSession
		//(without this we can't instantiate a QueueSender)
		initQueueSession();
		
		//get the QueueSender from our QueueSession
		queueSender = queueSession.createSender(queue);
		logger.debug("Queue sender created");
		
		//create the message pool for text messages
		textMessagePool = new TextMessagePool(queueSession, msgPoolSize);
		logger.debug("Text message pool created");
	}
	
	public synchronized void sendMessage(String text) throws JMSException{
		//check if QueueSession is ready
		if (!queueSessionReady) {
			//QueueSession is not ready, we can't send messages
			throw new JMSException("Queue sender not ready");
		}
		
		//get a TextMessage from the pool
		TextMessage textMessage = (TextMessage) this.textMessagePool.getMessage();
		//fill it with text (our message to be sent)
    	textMessage.setText(text);
		logger.debug("Sending message: " + text);
		//send to JMS
		queueSender.send(textMessage);
		//release the TextMessage to the pool
		textMessagePool.release(textMessage);
	}
	
	public synchronized void initTopicSubscriber() throws JMSException, NamingException {
		//first of all we have to inititiate the TopicSession
		//(without this we can't instantiate a TopicSubscriber)
		initTopicSession();
		
		//get the TopicSubscriber from our TopicSession
		TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic, null, true);
		logger.debug("Topic subscriber created");
		
		//if set we pass our ExtendedMessageListener to the TopicSubscriber as MessageListener
		if (messageListener != null) {
			topicSubscriber.setMessageListener(messageListener);
		}
		
		//start listening to JMS
		topicConnection.start();
		logger.info("Topic connection started");
	}
	
	public synchronized void initTopicPublisher(int msgPoolSize) throws JMSException, NamingException {
		//first of all we have to inititiate the TopicSession
		//(without this we can't instantiate a TopicPublisher)
		initTopicSession();
		
		//get the TopicPublisher from our TopicSession
        this.topicPublisher = topicSession.createPublisher(topic);
        logger.debug("Topic publisher created");
        
        //create the message pool for FeedMessage messages
        this.objectMessagePool = new ObjectMessagePool(topicSession, msgPoolSize);
        logger.debug("Object message pool created");
	}
	
	
	public synchronized void publishMessage(Serializable obj) throws JMSException{
		//check if TopicSession is ready
		if (!topicSessionReady) {
			//TopicSession is not ready, we can't publish messages
			throw new JMSException("Topic publisher not ready");
		}
		
		//get an ObjectMessage from the pool
		ObjectMessage objectMessage = (ObjectMessage) this.objectMessagePool.getMessage();
    	//fill it with obj (our message to be sent)
		objectMessage.setObject(obj);
		logger.debug("Publishing message object " + obj);
		//publish to JMS
		this.topicPublisher.publish(objectMessage);
		//release the ObjectMessage to the pool
		this.objectMessagePool.release(objectMessage);
	}
	
	
	
//////////////////////MessagePool
    
    /*
     * Implement a pool of JMS messages used in subscribe/unsubscribe operations.
     */
    private abstract class MessagePool {
    	
    	protected Session session = null;
    	private int lenSegmentMsgPool = 0;
    	private ArrayList<Message> freeMessagePool = null;
    	
    	/*
    	 * create the pool
    	 */
    	public MessagePool(Session session, int lenSegmentMsgPool) throws JMSException {
    		this.session = session;
    		this.lenSegmentMsgPool = lenSegmentMsgPool;
    		this.freeMessagePool = new ArrayList<Message>(lenSegmentMsgPool);

    		//fill the pool with lenSegmentMsgPool empty messages
    		this.createNewMessages(this.lenSegmentMsgPool);
    	}
    	
    	/*
    	 * add new messages to the pool
    	 */
    	private void createNewMessages(int lenSegmentMsgPool) throws JMSException {
    		for (int i=0; i<lenSegmentMsgPool; i++) {
    			this.freeMessagePool.add(this.createMessage());
    		}
    	}
    	
    	/* 
    	 * this method will be implemented by subclasses. Each subclass
    	 * can create its own type of message
    	 */
    	protected abstract Message createMessage() throws JMSException;
    	
    	/*
    	 * get a message from the pool 
    	 */
    	public synchronized Message getMessage() throws JMSException {
    		if (this.freeMessagePool.size() == 0) {
    			//if there aren't free messages in the pool, fill
    			//the pool with new messages
    			this.createNewMessages(this.lenSegmentMsgPool);
    		}
    		//remove a message from the free messages list and return to caller 
    		return (Message) this.freeMessagePool.remove(this.freeMessagePool.size()-1);
    	}
    	
    	public synchronized void release(Message message) throws JMSException {
    		if (message == null) {
    			//message is null, can't add to the pool
    			logger.error("Can't realese a null message in free message pool");
                throw new JMSException("Message pool error");
    		}

    		//put the released message in the free messages list
    		this.freeMessagePool.add(message);
    	}
    	
    }
    
    private class TextMessagePool extends MessagePool {
    	
    	public TextMessagePool(Session session, int lenSegmentMsgPool) throws JMSException {
    		super(session, lenSegmentMsgPool);
    	}
    	
    	protected Message createMessage() throws JMSException {
    		//use a JMS session to create a new TextMessage
    		return this.session.createTextMessage();
    	}
    	
    }
    
    private class ObjectMessagePool extends MessagePool {
    	
    	public ObjectMessagePool(Session session, int lenSegmentMsgPool) throws JMSException {
    		super(session, lenSegmentMsgPool);
    	}
    	
    	protected Message createMessage() throws JMSException {
    		//uses a JMS session to create a new ObjectMessage
    		return this.session.createObjectMessage();
    	}
    	
    }
	
}
