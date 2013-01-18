package com.cleartraders.common.util.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.util.config.CommonResManager;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;


public class SMSHandler
{
    private static SMSHandler m_oInstance = new SMSHandler();
    
    private Vector<SMSBean> m_oSMSList = new Vector<SMSBean>();
    
    private Timer m_sendSMSTimer = new Timer();
    
    private SMSHandler()
    {
        m_sendSMSTimer.schedule(new SMSTask(),new Date(System.currentTimeMillis()), CommonResManager.getInstance().getSendSMSDelay());
    }
    
    public static synchronized SMSHandler getInstance()
    {
        return m_oInstance;
    }
    
    public synchronized void appendSMSToSend(SMSBean oSMS)
    {
        if(oSMS != null)
        {
            m_oSMSList.add(oSMS);
        }
    }
    
    public synchronized void appendSMSToSend(List<SMSBean> oSMSList)
    {
        if(oSMSList != null && oSMSList.size() > 0)
        {
            m_oSMSList.addAll(oSMSList);
        }
    }
    
    private synchronized SMSBean getSMSToSend()
    {
        if(m_oSMSList != null && m_oSMSList.size() > 0)
        {
            SMSBean oSMS = new SMSBean(m_oSMSList.get(m_oSMSList.size() -1));
            m_oSMSList.remove(m_oSMSList.get(m_oSMSList.size() -1));
            
            return oSMS;
        }
        else
        {
            return null;
        }
    }
    
    private boolean sendSMS(SMSBean oSMS)
    {
        if(oSMS == null)
        {
            return false;
        }       
        
        boolean result=false;
        
        try
        {
            String SMSMessageURL = createSMSMessageURL(oSMS);
            
            result = sendSMS(SMSMessageURL);  
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            result = false;
        }        
        
        if(result)
        {
            InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Send SMS to "+oSMS.getReceiverMobile()+" successfully!");
        }
        else
        {
            InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Send SMS to "+oSMS.getReceiverMobile()+" failed!");
        }
        
        return result;
    }
    
    private boolean sendSMS(String strSMSMessageURL) throws Exception
    {
        if(strSMSMessageURL == null || strSMSMessageURL.length() < 1)
        {
            throw new Exception("Argument is NULL at createSMSMessageURL()!");
        }
        
        URL url = new URL(strSMSMessageURL);
        
        if(operateServerByURL(url) != CommonDefine.SUCCESS_RESULT)
        {
            InfoTrace.getInstance().printInfo(DebugLevel.WARNING," Sending SMS failed!");
            LogTools.getInstance().insertLog(DebugLevel.WARNING," Sending SMS failed! SMS message URL is "+strSMSMessageURL);
            
            return false;
        }
        else
        {
            InfoTrace.getInstance().printInfo(DebugLevel.INFO," Sending SMS successfully!");
            LogTools.getInstance().insertLog(DebugLevel.INFO," Sending SMS successfully!");
            
            return true;
        }
    }
    
    public int operateServerByURL(URL url)
    {
        int result=CommonDefine.SUCCESS_RESULT;
        
        try
        {
            URLConnection urlconn = url.openConnection();
            BufferedReader dataStream = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
            String currentLine=null;
            if(null!=(currentLine=dataStream.readLine()))
            {
                currentLine = currentLine.trim();
                
                if(currentLine.indexOf("ERR:") != -1)
                {
                    InfoTrace.getInstance().printInfo(DebugLevel.WARNING," operate Server By URL failed!");
                    LogTools.getInstance().insertLog(DebugLevel.WARNING," operate Server By URL failed! responce message is "+currentLine);
                    
                    result=CommonDefine.ERROR_RESULT;
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR," Exception happened in SMSHandler.operateServerByURL()! "+ CommonTools.getExceptionDescribe(e));
            
            result = CommonDefine.ERROR_RESULT;
        }
        
        return result;
    }
    
    private String createSMSMessageURL(SMSBean oSMS) throws Exception
    {
        if(oSMS == null)
        {
            throw new Exception("Argument is NULL at createSMSMessageURL()!");
        }
        
        //For example:
        //URL should be https://api.clickatell.com/http/sendmsg?user=bjyu&password=123456a&api_id=3116893&to=8613111111111&text=Meet+me+at+home
        String SMSMessageURL= "";
        
        String toMobile = oSMS.getReceiverMobile();
        String content = oSMS.getSmsContent();
        
        SMSMessageURL += CommonResManager.getInstance().getSMSHTTPServerURL();
        SMSMessageURL += "user=" + CommonResManager.getInstance().getSMSAccountUserName();
        SMSMessageURL += "&password=" + CommonResManager.getInstance().getSMSAccountPassword();
        SMSMessageURL += "&api_id=" + CommonResManager.getInstance().getSMSAPIID();
        SMSMessageURL += "&to=" + toMobile;    
        SMSMessageURL += "&text=" + URLEncoder.encode(content,"UTF-8");  
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO," Prepare to Send SMS to"+toMobile+". Content is: "+content);
        LogTools.getInstance().insertLog(DebugLevel.INFO," Prepare to Send SMS to"+toMobile+". Content is: "+content);
        
        return SMSMessageURL;
    }
    
    private class SMSTask extends TimerTask
    {
        public void run()
        {
            // TODO Auto-generated method stub            
            sendSMS(getSMSToSend());
        }
    }
    
    public static void main(String args[])
    {
        try
        {
            ArrayList<SMSBean> testSMS = new ArrayList<SMSBean>();
            for(int i=0; i<1; i++)
            {
                SMSBean oSMS = new SMSBean();
                oSMS.setReceiverMobile("8613121115631");
                //oSMS.setSmsContent("Short Signal\nEUR/USD\n5 MIN\n1.42304\nLog into Clear Traders to see more information on this signal.");
                //oSMS.setSmsContent("Thanks for registering with Clear Traders, don't forget you can get instant trade signals via SMS! Buy cheap SMS credits in your My Account page now.");
                oSMS.setSmsContent("Thanks for registering with Clear Traders, don't forget you get instant trade signals via SMS! Just select SMS alerts in your My Account page to activate!");
                
                testSMS.add(oSMS);
            }
            
            SMSHandler.getInstance().appendSMSToSend(testSMS);
            
            Thread.sleep(5000);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
