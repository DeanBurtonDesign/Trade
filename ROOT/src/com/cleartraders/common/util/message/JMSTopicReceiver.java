package com.cleartraders.common.util.message;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.cleartraders.common.util.config.CommonResManager;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;


public abstract class JMSTopicReceiver implements IMessager, MessageListener, ExceptionListener
{
    private TopicConnection connection=null;
    private TopicSession session=null;    
    private TopicSubscriber topicSubscriber=null;

    private String user = "";
    private String password = "";
    private String url = "";    
    private String topicName = "";
    
    public JMSTopicReceiver(String topicName)
    {
        this.url = CommonResManager.getInstance().getJms_server_url();
        this.user = CommonResManager.getInstance().getJms_username();
        this.password = CommonResManager.getInstance().getJms_password();
        this.topicName = topicName;
        
        init();
    }
    public boolean destory()
    {
        try
        {
            if (topicSubscriber != null)
            {
                topicSubscriber.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        topicSubscriber = null;
        
        try
        {
            if (session != null)
            {
                session.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        session = null;
        
        try
        {
            if (connection != null)
            {
                connection.stop();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (connection != null)
            {
                try
                {
                connection.close();
                }
                catch(Exception ex){}
            }
        }        
        
        connection = null;
                
        return true;
    }

    public boolean init()
    {
        boolean result=false;
        
        try
        {            
            //destory first
            destory();
            
            /**
             * JMS TopicConnectionFactory initialization
             */
            TopicConnectionFactory topicConnectionFactory = new ActiveMQConnectionFactory(user, password, url);

            /**
             * JMS TopicConnection initialization
             */
            connection = topicConnectionFactory.createTopicConnection();
            /**
             * creating JMS session
             */
            session = connection.createTopicSession(false,Session.DUPS_OK_ACKNOWLEDGE);

            /**
             * creating JMS topic
             */
            Topic topic = session.createTopic(topicName);

            /**
             * creating subscriber instance
             */
            topicSubscriber = session.createSubscriber(topic, null, true);            
            topicSubscriber.setMessageListener(this);

            connection.setExceptionListener(this);
            connection.start();
            
            result = true;
        }
        catch (Exception e)
        {
            result=false;
            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,"Exception Happened in JMSTopicReceiver.init() while init it, it will be inited again! Exception details=> "+ CommonTools.getExceptionDescribe(e));
            
            destory();
            
            //here system must init again, otherwise this Topic receiver will never receive message
            sleepSpecificMillSeconds(5000);
            init();
        }
        
        return result;
    }
    
    public boolean sendTextMessage(String textMsg)
    {
        return false;
    }
    
    public void onException(JMSException e)
    {
        e.printStackTrace();
        LogTools.getInstance().insertLog(DebugLevel.ERROR,"Exception Happened in JMSTopicReceiver.onException(), it will be inited again! Exception details=> "+ CommonTools.getExceptionDescribe(e));
        
        sleepSpecificMillSeconds(1000);
        init();
    }
    
    private void sleepSpecificMillSeconds(long millSeconds)
    {
        try
        {
            Thread.sleep(millSeconds);
        }
        catch(Exception e)
        {            
        }
    }
}
