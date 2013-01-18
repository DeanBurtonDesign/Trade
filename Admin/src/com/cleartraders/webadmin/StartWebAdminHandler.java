package com.cleartraders.webadmin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.util.config.CommonResManager;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.webadmin.model.membersync.MemberSyncScheduler;
import com.cleartraders.webadmin.model.membersync.mailchimp.MailChimpMemberSync;

public class StartWebAdminHandler extends HttpServlet
{
    private static final long serialVersionUID = -5248152430424562470L;
    private static final long PERIOD_SYNC_TO_MAIL_CHIMP = 24*60*60*1000; //period is 24 hours
    
    public void init() throws ServletException
    {
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Call StartWebAdminHandler.init()!");

        // init base resource file first
        CommonResManager.getInstance();
        DataCache.getInstance();
        
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Call MemberSyncHandler.init()!");
        new MemberSyncScheduler(new MailChimpMemberSync(),System.currentTimeMillis(), PERIOD_SYNC_TO_MAIL_CHIMP);
                
        LogTools.getInstance().insertLog(DebugLevel.INFO,"StartWebAdminHandler init() Resource and DataCache successfully!");
    }
}
