package com.cleartraders.common.util.email;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import com.cleartraders.common.util.config.CommonResManager;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;


public class EmailHandler
{
    private static EmailHandler m_oInstance=new EmailHandler();
    
    //store all emails which should be sent 
    //after sending, it should remove from this list
    private Vector<EmailBean> m_oMailList = new Vector<EmailBean>();
    
    private Timer m_sendEmailTimer = new Timer();
    
    private EmailHandler()
    {
        m_sendEmailTimer.schedule(new EmailTask(),new Date(System.currentTimeMillis()), CommonResManager.getInstance().getSendEmailDelay());
    }
    
    public static synchronized EmailHandler getInstance()
    {
        return m_oInstance;
    }
    
    public synchronized void appendEmailToSend(EmailBean oEmail)
    {
        if(oEmail != null)
        {
            m_oMailList.add(oEmail);
        }
    }
    
    public synchronized void appendEmailToSend(List<EmailBean> oEmailList)
    {
        if(oEmailList != null && oEmailList.size() > 0)
        {
            m_oMailList.addAll(oEmailList);
        }
    }
    
    private synchronized EmailBean getEmailToSend()
    {
        if(m_oMailList != null && m_oMailList.size() > 0)
        {
            EmailBean oEmail = new EmailBean(m_oMailList.get(m_oMailList.size() -1));
            m_oMailList.remove(m_oMailList.get(m_oMailList.size() -1));
            
            return oEmail;
        }
        else
        {
            return null;
        }
    }
    
    private boolean sendEmail(EmailBean oEmail)
    {
        if(oEmail == null)
        {
            return false;
        }
        
        
        boolean result=false;
        String to = oEmail.getRecipients();
        String subject = oEmail.getEmailSubject();
        String body = oEmail.getEmailBody();
        
        String SMTPServerURL = CommonResManager.getInstance().getSMTPServerURL();
        
        try
        {
            SendMail sm = new SendMail(SMTPServerURL,to, CommonResManager.getInstance().getEmail_sent_from(), CommonResManager.getInstance().getEmail_sent_from_name(), subject,body, true);
            sm.setUserName(CommonResManager.getInstance().getSMTPUser());
            sm.setPassword(CommonResManager.getInstance().getSMTPPWD());
            // sm.setAttachFile("d:\\DBExtractor.java");
            sm.send();
            
            result = true;
            
            InfoTrace.getInstance().printInfo(DebugLevel.INFO," Send Email to: "+to+"; Subject is: "+subject);
            LogTools.getInstance().insertLog(DebugLevel.INFO," Send Email to: "+to+"; Subject is: "+subject);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        return result;
    }
    
    private class EmailTask extends TimerTask
    {
        public void run()
        {
            // TODO Auto-generated method stub            
            sendEmail(getEmailToSend());
        }
    }
    
    public static void main(String args[])
    {
        try
        {
            ArrayList<EmailBean> testEmail = new ArrayList<EmailBean>();
            for(int i=0; i<200; i++)
            {
                String emailSubject="Signal Notify";
                String emailBody="Dear Mr. Huang, \n BUY SIGNAL Occured at 1.2778";
                String recipients="bjyuzhen@gmail.com";
                
                EmailBean oEmail = new EmailBean();
                oEmail.setEmailSubject(emailSubject);
                oEmail.setEmailBody(emailBody);
                oEmail.setRecipients(recipients);
                
                testEmail.add(oEmail);
            }
            
            EmailHandler.getInstance().appendEmailToSend(testEmail);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
