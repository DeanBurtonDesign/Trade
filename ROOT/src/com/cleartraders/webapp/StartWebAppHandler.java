package com.cleartraders.webapp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.util.config.CommonResManager;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.webapp.model.notification.EmailNotificationHandler;

public class StartWebAppHandler extends HttpServlet
{
    private static final long serialVersionUID = -4225752731963741169L;

    public void init() throws ServletException
    {
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Call StartWebAppHandler.init()!");
        
        //init base resource file first
        CommonResManager.getInstance();
        
        //init notification handler
        EmailNotificationHandler.getInstance();
        
        DataCache.getInstance();
    }
}
