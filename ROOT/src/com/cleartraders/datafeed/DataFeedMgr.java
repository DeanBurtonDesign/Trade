package com.cleartraders.datafeed;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cleartraders.common.util.config.CommonResManager;
import com.cleartraders.common.util.feeder.IDataFeeder;
import com.cleartraders.common.util.feeder.iqfeed.IQDataFeedRealTimeDataHandler;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.message.JMSJSTopicMessager;
import com.cleartraders.common.util.message.JMSTopicMessager;
import com.cleartraders.datafeed.db.FeedDBAccessor;
import com.cleartraders.datafeed.model.bean.MarketBean;

/**
 * This class is used to manager all feeders
 * @author Peter
 *
 */
public class DataFeedMgr
{
    private List<IDataFeeder> _feederList = null;
    private static DataFeedMgr m_oInstance = new DataFeedMgr();
    
    private DataFeedMgr()
    {
        _feederList = new ArrayList<IDataFeeder>();
    }
    
    public synchronized static DataFeedMgr getInstance()
    {               
        return m_oInstance;
    }
    
    public void init()
    {
        //load market list
        List<MarketBean> marketList = FeedDBAccessor.getInstance().getAllEnableMarket();
        
        for(int i=0; i<marketList.size(); i++)
        {
            MarketBean oMarketBean = marketList.get(i);
            IDataFeeder oFeeder = new IQDataFeedRealTimeDataHandler(oMarketBean.getMarket_name());
            oFeeder.setID(oMarketBean.getId());
            oFeeder.addMessager(new JMSTopicMessager(oMarketBean.getMarket_name()));
            
            //Add another messager for price update
            oFeeder.addMessager(new JMSJSTopicMessager(oMarketBean.getMarket_name(),CommonResManager.getInstance().getSignaltojs_topic()));
            
            _feederList.add(oFeeder);
        }
    }
    
    public void startAllFeeder()
    {
        if(_feederList != null)
        {
            try
            {
                for(int i=0; i<_feederList.size(); i++)
                {
                    IDataFeeder oFeeder = _feederList.get(i);
                    oFeeder.startFeeder();
                    
                    Thread.sleep(5000);
                }
                
                LogTools.getInstance().insertLog(DebugLevel.INFO,"DataFeedMgr.startAllFeeder() Success!");
                
                LogTools.getInstance().insertLog(DebugLevel.INFO,"DataFeedMgr start to monitor data feeder!");
                while(true)
                {
                    try
                    {
                        for(int i=0; i<_feederList.size(); i++)
                        {
                            IDataFeeder oFeeder = _feederList.get(i);
                            
                            if(!oFeeder.getStatus())
                            {
                                LogTools.getInstance().insertLog(DebugLevel.WARNING,"DataFeedMgr found feeder,"+oFeeder.getID()+" was not live, system will restart it!");
                                
                                oFeeder.stopFeeder();
                                oFeeder.startFeeder();
                            }
                            
                            Thread.sleep(5000);
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void stopAllFeeder()
    {
        if(_feederList != null)
        {
            for(int i=0; i<_feederList.size(); i++)
            {
                IDataFeeder oFeeder = _feederList.get(i);
                oFeeder.stopFeeder();
            }
        }
    }
    
    public static void main(String[] agrs)
    {
        //DataFeedMgr.getInstance().init();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            long time =  1225735860000L;
//            System.out.println(dateTimeFormat.parse("2008-10-26 12:00:00").getTime());
//            System.out.println(dateTimeFormat.parse("2008-10-27 12:00:00").getTime());
            System.out.println(dateTimeFormat.format(new Date(time)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
