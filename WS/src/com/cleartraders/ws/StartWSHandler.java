package com.cleartraders.ws;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.ws.config.WSResManager;

public class StartWSHandler extends HttpServlet
{
    private static final long serialVersionUID = 3393980900031762868L;

    public void init() throws ServletException
    {
        LogTools.getInstance().insertLog(DebugLevel.INFO, "Call StartWSHandler.init()!");

        WSResManager.getInstance();

        DataCache.getInstance();
        
        LogTools.getInstance().insertLog(DebugLevel.INFO, "Init Resource and Data cache successfully!");
        
        //testing JS signal
//        Thread test = new Thread()
//        {            
//            public void run()
//            {
//                int signalID=1;
//                boolean isBuy=true;
//                
//                try
//                {
//                    Thread.sleep(70000);
//                }
//                catch (InterruptedException e1)
//                {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//                
//                while(true)
//                {
//                    try
//                    {
//                        Thread.sleep(240000);
//                        
//                        int signalType = CommonDefine.BUY_SINGAL;
//                        
//                        isBuy = !isBuy;
//                        if(isBuy)
//                        {
//                            signalType = CommonDefine.BUY_SINGAL;
//                        }
//                        else
//                        {
//                            signalType = CommonDefine.SELL_SINGAL;
//                        }
//                        
//                        HLOCDataBean latestHLOC = DataCache.getInstance().getLatestHLOCData("4:1");
//                        
//                        DataServiceImpl oMockService = new DataServiceImpl();
//                        
//                        Signal signalBean = new Signal();
//                        signalBean.setChecked(1);
//                        signalBean.setDirection(0);
//                        signalBean.setGenerate_date(latestHLOC.getDate());
//                        signalBean.setExpire_date(60000);
//                        signalBean.setExpire_date_string(""+60000);
//                        signalBean.setGenerate_date_string(""+latestHLOC.getDate());
//                        signalBean.setId(signalID++);
//                        signalBean.setMarket_name("AUD/USD");
//                        signalBean.setMarket_topic_name("BAUDUSD");
//                        signalBean.setMarket_type_id(4);
//                        signalBean.setProfit(0.0);
//                        signalBean.setProfitString("0.0");
//                        signalBean.setSignal_period(1);
//                        signalBean.setSignal_rate(1);
//                        signalBean.setSignal_type(signalType);
//                        signalBean.setSignalValue(latestHLOC.getClose());                       
//                        signalBean.setSystem_name("cleartraders");
//                                                
//                        oMockService.addAndNotifySignals("cleartraders_admin_clients", "0BAED9D51560BC78E7698B52D0E8DF2C03E9566E", signalBean);
//                    }
//                    catch(Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        
//        test.start();
        //end test
    }
}
