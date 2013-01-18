package com.cleartraders.webapp.model.bean;

/**
 *  

    create table scheduler_notifications_table
    (
        id                  int unsigned not null primary key,
        user_id         int unsigned not null,
        time            int unsigned not null, # execute time
        template_type   int unsigned not null  #notification template type 0: 2 before expired, 1: expired, 2: 2 days after expired 3: 7 days after expired
    );
    
 * @author Administrator
 *
 */
public class SchedulerNotificationsBean
{
    private long id;
    private long user_id;
    private long time;
    private int template_type;
    
    public long getId()
    {
        return id;
    }
    public void setId(long id)
    {
        this.id = id;
    }
    public int getTemplate_type()
    {
        return template_type;
    }
    public void setTemplate_type(int template_type)
    {
        this.template_type = template_type;
    }
    public long getTime()
    {
        return time;
    }
    public void setTime(long time)
    {
        this.time = time;
    }
    public long getUser_id()
    {
        return user_id;
    }
    public void setUser_id(long user_id)
    {
        this.user_id = user_id;
    }
}
