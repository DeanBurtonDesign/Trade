package com.cleartraders.signalframe.strategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.cleartraders.signalframe.dataengine.QuoteEngineManager;

public class StartStrategyMgrHandler extends HttpServlet
{
    private static final long serialVersionUID = -5475803965144913494L;

    public void init() throws ServletException
    {
        Thread oTempThread = new Thread()
        {
            public void run()
            {
                try
                {
                    //sleep long time to wait for other preparation work
                    sleep(30000);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                
                QuoteEngineManager.getInstance().init();
                QuoteEngineManager.getInstance().startAll();
                
                StrategyManager.getInstance();
            }
        };
        
        oTempThread.start();
    }
}
