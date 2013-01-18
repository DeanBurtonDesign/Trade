package com.cleartraders.webapp.model.notification;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public abstract class NotificationScheduler
{
    private long runTime;
    private Timer schedulerTimer = new Timer();
    
    public NotificationScheduler(long runTime)
    {
        this.runTime = runTime;
        this.schedulerTimer.schedule(new SchedulerTask(), new Date(this.runTime));
    }
    
    public abstract void executeScheduler();
    
    public long getRunTime()
    {
        return runTime;
    }

    public void setRunTime(long runTime)
    {
        this.runTime = runTime;
    }
    
    private class SchedulerTask extends TimerTask
    {
        public void run()
        {
            executeScheduler();
        }
    }
}
