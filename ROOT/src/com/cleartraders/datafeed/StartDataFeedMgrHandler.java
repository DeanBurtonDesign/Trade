package com.cleartraders.datafeed;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.cleartraders.common.util.config.CommonResManager;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.datafeed.config.DataFeedResManager;

public class StartDataFeedMgrHandler extends HttpServlet
{
    private static final long serialVersionUID = -8843914415490236941L;

    public void init() throws ServletException
    {       
        //init base resource file first
        CommonResManager.getInstance();
        
        //init resource file
        DataFeedResManager.getInstance();
        
        LogTools.getInstance().insertLog(DebugLevel.INFO,"StartDataFeedMgrHandler.init() init DataFeed Resource Success!");
        
        Thread oTempThread = new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(10000);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                
                DataFeedMgr.getInstance().init();
                DataFeedMgr.getInstance().startAllFeeder();
            }
        };
        
        oTempThread.start();
    }
}
