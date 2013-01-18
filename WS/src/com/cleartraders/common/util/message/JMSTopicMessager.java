package com.cleartraders.common.util.message;

import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.cleartraders.common.util.config.CommonResManager;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;

public class JMSTopicMessager implements IMessager
{
    private TopicConnection connection=null;
    private TopicSession session=null;    
    private TopicPublisher topicPublisher=null;

    private String user = "";
    private String password = "";
    private String url = "";    
    private String topicName = "";
    
    public JMSTopicMessager(String topicName)
    {
        this.url = CommonResManager.getInstance().getJms_server_url();
        this.user = CommonResManager.getInstance().getJms_username();
        this.password = CommonResManager.getInstance().getJms_password();
        this.topicName = topicName;
        
        init();
    }
    
    public JMSTopicMessager(String serverURL, String userName, String password, String topicName)
    {
        this.url = serverURL;
        this.user = userName;
        this.password = password;
        this.topicName = topicName;
        
        init();
    }
    
    public boolean destory()
    {
        try
        {
            if (topicPublisher != null)
            {
                topicPublisher.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        topicPublisher = null;
        
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
        }
        finally
        {
            try
            {
                if (connection != null)
                {
                    connection.close();
                }
            }
            catch(Exception ex)
            {                
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

            connection.start();

            /**
             * creating JMS topic
             */
            Topic topic = session.createTopic(topicName);

            /**
             * creating subscriber instance
             */
            topicPublisher = session.createPublisher(topic);
            
            result = true;
        }
        catch (Exception e)
        {
            result=false;
            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,"JMSTopicMessager:init() JMSException Happened, it will be inited again! Exception details=> "+ CommonTools.getExceptionDescribe(e));
                
            destory();
        }  
        
        return result;
    }

    public boolean sendTextMessage(String textMsg)
    {
        boolean result=false;
        
        try
        {
            TextMessage message = session.createTextMessage();
            message.setText(textMsg);
            
            topicPublisher.publish(message);
            
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,"JMSTopicMessager:sendTextMessage() JMSException Happened, it will be inited again! Exception details=> "+ CommonTools.getExceptionDescribe(e));
            
            result=false;
            
            init();
        }

        return result;
    }

    public TopicSession getSession()
    {
        return session;
    }

    public void setSession(TopicSession session)
    {
        this.session = session;
    }

    public TopicPublisher getTopicPublisher()
    {
        return topicPublisher;
    }

    public void setTopicPublisher(TopicPublisher topicPublisher)
    {
        this.topicPublisher = topicPublisher;
    }

//    private void sleepSpecificMillSeconds(long millSeconds)
//    {
//        try
//        {
//            Thread.sleep(millSeconds);
//        }
//        catch(Exception e)
//        {            
//        }
//    }
}
