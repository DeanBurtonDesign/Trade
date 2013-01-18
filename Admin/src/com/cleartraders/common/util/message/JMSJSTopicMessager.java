package com.cleartraders.common.util.message;

import javax.jms.TextMessage;

import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;

public class JMSJSTopicMessager extends JMSTopicMessager
{
    private String _market_topic_name = "";
    
    public JMSJSTopicMessager(String market_topic_name, String message_topic_Name)
    {
        super(message_topic_Name);
        _market_topic_name = market_topic_name;
    }
    
    public boolean sendTextMessage(String textMsg)
    {
        boolean result=false;
        
        try
        {
            if(textMsg.split(",").length < 5)
            {
                textMsg += "0";
            }
            
            TextMessage message = super.getSession().createTextMessage();
            message.setText(_market_topic_name+","+textMsg);
            
            super.getTopicPublisher().publish(message);
            
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,"Exception Happened in JMSJSTopicMessager.sendTextMessage()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
                        
            result=false;
            
            super.init();
        }

        return result;
    }
}
