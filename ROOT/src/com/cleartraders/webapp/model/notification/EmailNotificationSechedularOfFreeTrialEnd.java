package com.cleartraders.webapp.model.notification;

import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.util.email.EmailBean;
import com.cleartraders.common.util.email.EmailHandler;
import com.cleartraders.webapp.db.DBAccessor;

public class EmailNotificationSechedularOfFreeTrialEnd extends NotificationScheduler
{
    
    private EmailBean emailBean = null;
    private EmailHandler emailHandler = null;
    
    public EmailNotificationSechedularOfFreeTrialEnd(long runTime, EmailBean email, EmailHandler emailHandler)
    {
        super(runTime);
        
        this.emailBean = email;
        this.emailHandler = emailHandler;
    }
    
    @Override
    public void executeScheduler()
    {
        this.emailHandler.appendEmailToSend(this.emailBean);
        
        //remove all expired schdulers
        try
        {
            DBAccessor.getInstance().removeAllExpiredSchedulers(System.currentTimeMillis());
            
            //whenever user free trial end, system should update their status to "Free Trial Ended"
            int freeTrialMemberLevel = 1;
            DBAccessor.getInstance().setUserStatusForFreeTrialEndUser(CommonDefine.FREE_TRIAL_END_USER, freeTrialMemberLevel, System.currentTimeMillis());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

}
