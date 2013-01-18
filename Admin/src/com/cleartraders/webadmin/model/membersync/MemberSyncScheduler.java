package com.cleartraders.webadmin.model.membersync;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;

public class MemberSyncScheduler
{
    private Timer schedulerTimer = new Timer();
    private IMemberSync memberSyncMgr;
    
    public MemberSyncScheduler(IMemberSync memberSyncMgr, long runTime, long period)
    {
        this.memberSyncMgr = memberSyncMgr;
        this.schedulerTimer.schedule(new SchedulerTask(), new Date(runTime), period);
    }
    
    private void executeScheduler()
    {
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Start to sync member to MailChimp...");
        
        try
        {
            this.memberSyncMgr.syncMembers();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,e.toString());
        }
    }
    
    private class SchedulerTask extends TimerTask
    {
        public void run()
        {
            executeScheduler();
        }
    }
}
