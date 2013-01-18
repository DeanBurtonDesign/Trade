package com.cleartraders.webapp.model.notification;

import java.util.ArrayList;
import java.util.List;

import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.email.EmailBean;
import com.cleartraders.common.util.email.EmailHandler;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.db.DBAccessor;
import com.cleartraders.webapp.model.bean.SchedulerNotificationsBean;

public class EmailNotificationHandler extends NotificationHandler
{
    private static EmailNotificationHandler m_oInstance = new EmailNotificationHandler();
    
    private List<UserNotificationUnit> m_oAllUserNotificationUnit = new ArrayList<UserNotificationUnit>();
    
    public static synchronized EmailNotificationHandler getInstance()
    {
        return m_oInstance;
    }    
    
    private EmailNotificationHandler()
    {
    	//2010-06-07 remove all schedular notifications for ClearTraders 1.0
        //createAllSchedular();        
        //loadAllNotificationSchedulers();
    	
    }
    
    public void createAllSchedular()
    {        
        //For Free Trial Schedular Email, 4x Trial Signup (start, 3 days remaining, expired, 3 days after)
        //create 3 days before the free trial finishes
        addSchedulerTimeBasedOnExpiredTime(-3*WebConstants.MINI_SECONDS_EACH_DAY, EmailNotificationFactory.getNotificationTemplate(WebConstants.THREE_DAYS_BEFORE_EXPIRED_TEMPLATE));
        
        //create free trial finish
        addSchedulerTimeBasedOnExpiredTime(0, EmailNotificationFactory.getNotificationTemplate(WebConstants.FREE_TRIAL_EXPIRED_TEMPLATE));
        
        //create 3 day after the free trial finishes
        addSchedulerTimeBasedOnExpiredTime(3*WebConstants.MINI_SECONDS_EACH_DAY, EmailNotificationFactory.getNotificationTemplate(WebConstants.THREE_DAYS_AFTER_EXPIRED_TEMPLATE));
        
        //create 7 days after the free trial finishes if they have not become a member still
        //addSchedulerTimeBasedOnExpiredTime(7*WebConstants.MINI_SECONDS_EACH_DAY, EmailNotificationFactory.getNotificationTemplate(WebConstants.SEVEN_DAYS_AFTER_EXPRED_TEMPLATE));
    }
    
    /**
     * load all scheduler notifications while init this Handler
     */
    public void loadAllNotificationSchedulers()
    {
        List<SchedulerNotificationsBean> pendingNotifications = DBAccessor.getInstance().getAllSchedulerNotification();
        
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Load all notification schedulers, size:"+pendingNotifications.size());
        
        for(int i=0; i<pendingNotifications.size(); i++)
        {
            SchedulerNotificationsBean notification = pendingNotifications.get(i);
            
            NotificationTemplate emailTemplate = EmailNotificationFactory.getNotificationTemplate(notification.getTemplate_type());
            
            //get user info
            UserBean userBean = DBAccessor.getInstance().getUserInfoByID(notification.getUser_id());
            if(null == userBean)
            {
                //this user is not exist
                DBAccessor.getInstance().removeAllExpiredSchedulersForUser(notification.getUser_id());
            }
            else
            {
                EmailBean notificatonEmail = createEmailByTemplate(userBean, emailTemplate);
                NotificationScheduler scheduler = null;
                
                if(notification.getTemplate_type() == WebConstants.FREE_TRIAL_EXPIRED_TEMPLATE)
                {
                    scheduler = new EmailNotificationSechedularOfFreeTrialEnd(notification.getTime(), notificatonEmail, EmailHandler.getInstance());
                }
                else
                {
                    scheduler = new EmailNotificationScheduler(notification.getTime(), notificatonEmail, EmailHandler.getInstance());
                }
                
                addNotificationSchedulear(notification.getUser_id(), scheduler);
            }
        }
    }
    
    private void addSchedulerTimeBasedOnExpiredTime(long timeBeforeExpired, NotificationTemplate template)
    {
        m_oAllUserNotificationUnit.add(new UserNotificationUnit(timeBeforeExpired, template));
    }
    
    public void createNotificationSchedulersForUser(UserBean userBean)
    {
        for(int i=0; i<m_oAllUserNotificationUnit.size(); i++)
        {
            UserNotificationUnit notficationUnit = m_oAllUserNotificationUnit.get(i);
            
            EmailBean notificatonEmail = createEmailByTemplate(userBean, notficationUnit.getTemplate());
            
            NotificationScheduler scheduler = null;
            if(notficationUnit.getTemplate().getTemplateType() == WebConstants.FREE_TRIAL_EXPIRED_TEMPLATE)
            {
                scheduler = new EmailNotificationSechedularOfFreeTrialEnd(userBean.getExpired_date()+notficationUnit.getTime(), notificatonEmail, EmailHandler.getInstance());
            }
            else
            {
                scheduler = new EmailNotificationScheduler(userBean.getExpired_date()+notficationUnit.getTime(), notificatonEmail, EmailHandler.getInstance());
            }
            
            addNotificationSchedulear(userBean.getId(), scheduler);
            
            //store into DB
            SchedulerNotificationsBean notification = new SchedulerNotificationsBean();
            notification.setId(0);
            notification.setTemplate_type(notficationUnit.getTemplate().getTemplateType());
            notification.setTime(userBean.getExpired_date()+notficationUnit.getTime());
            notification.setUser_id(userBean.getId());
            
            DBAccessor.getInstance().storeSchedulerNotification(notification);
        }
    }
    
    private EmailBean createEmailByTemplate(UserBean userBean, NotificationTemplate template)
    {
        return EmailNotificationFactory.getNotificationEmail(userBean, template.getTemplateType());
    }
    
    private class UserNotificationUnit
    {
        private long time;
        private NotificationTemplate template;
        
        public UserNotificationUnit(long time, NotificationTemplate template)
        {
            this.time = time;
            this.template = template;
        }
        
        public NotificationTemplate getTemplate()
        {
            return template;
        }

        public void setTemplate(NotificationTemplate template)
        {
            this.template = template;
        }

        public long getTime()
        {
            return time;
        }

        public void setTime(long time)
        {
            this.time = time;
        }
    }
}
