package com.cleartraders.webapp.model.notification;

import com.cleartraders.common.util.email.EmailBean;
import com.cleartraders.common.util.email.EmailHandler;
import com.cleartraders.webapp.db.DBAccessor;

public class EmailNotificationScheduler extends NotificationScheduler
{
    private EmailBean emailBean = null;
    private EmailHandler emailHandler = null;
    
    public EmailNotificationScheduler(long runTime, EmailBean email, EmailHandler emailHandler)
    {
        super(runTime);
        
        this.emailBean = email;
        this.emailHandler = emailHandler;
    }
    
    public void executeScheduler()
    {
        this.emailHandler.appendEmailToSend(this.emailBean);
        
        //remove all expired schdulers
        try
        {
            DBAccessor.getInstance().removeAllExpiredSchedulers(System.currentTimeMillis());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
