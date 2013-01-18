package com.cleartraders.webapp.model.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public abstract class NotificationHandler
{    
    private HashMap<Long, List<NotificationScheduler>> m_oAllNotificationSchedulers = new HashMap<Long, List<NotificationScheduler>>();
    
    public NotificationHandler()
    {    
    }
    
    public abstract void createAllSchedular();
    
    public abstract void loadAllNotificationSchedulers();
        
    public List getNotificationScheduler(long targetID)
    {
        return m_oAllNotificationSchedulers.get(Long.valueOf(targetID));
    }
    
    void addNotificationSchedulear(long targetID, NotificationScheduler sheduler)
    {
        if(m_oAllNotificationSchedulers.get(Long.valueOf(targetID)) == null)
        {
            List<NotificationScheduler> schedulers = new ArrayList<NotificationScheduler>();
            schedulers.add(sheduler);
            
            m_oAllNotificationSchedulers.put(Long.valueOf(targetID), schedulers);
        }
        else
        {
            List<NotificationScheduler> schedulers = m_oAllNotificationSchedulers.get(Long.valueOf(targetID));
            
            schedulers.add(sheduler);
        }
    }
}
