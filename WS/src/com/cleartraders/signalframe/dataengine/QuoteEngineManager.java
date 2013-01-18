package com.cleartraders.signalframe.dataengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.HLOCDataBean;
import com.cleartraders.common.entity.MarketPeriodBean;
import com.cleartraders.common.entity.MarketTypeBean;
import com.cleartraders.common.util.feeder.iqfeed.IQDataFeedHistoryDataHandler;
import com.cleartraders.common.util.tools.IRunningInstanceMgr;

/**
 * Manage all realtime engine
 * @author Administrator
 *
 */
public class QuoteEngineManager implements IRunningInstanceMgr
{
    private Map<String,IQuoteEngine> _allQuoteEngine = null;
    private static QuoteEngineManager m_oInstance = new QuoteEngineManager();
    
    private QuoteEngineManager()
    {
        _allQuoteEngine = new HashMap<String, IQuoteEngine>();
    }
    
    public synchronized static QuoteEngineManager getInstance()
    {               
        return m_oInstance;
    }
    
    public void init()
    {        
        List<MarketTypeBean> oAllMarketBean = DataCache.getInstance().getAllMarketType();
        List<MarketPeriodBean> oAllMarketPeriod = DataCache.getInstance().getAllMarketPeriod();
        
        if(null == oAllMarketBean || null == oAllMarketPeriod)
            return;
        
        for(int i=0; i<oAllMarketBean.size(); i++)
        {
            MarketTypeBean oMarketBean = oAllMarketBean.get(i);
            
            for(int j=0; j<oAllMarketPeriod.size(); j++)
            {
                MarketPeriodBean oMarketPeriodBean = oAllMarketPeriod.get(j);
                
                initHLOCData(oMarketBean, oMarketPeriodBean);
                
                IQuoteEngine oQuoteEngine = new RealtimeQuoteFeederEngine(oMarketBean.getMarket_name(),oMarketBean.getId(), oMarketPeriodBean.getId());
                
                _allQuoteEngine.put(oMarketBean.getId()+":"+oMarketPeriodBean.getId(), oQuoteEngine);
            }
        }
    }
    
    private void initHLOCData(MarketTypeBean oMarketBean, MarketPeriodBean oMarketPeriodBean)
    {
        ArrayList<String> oResult = null;
        if( CommonDefine.MINUTE_PERIOD_TYPE == oMarketPeriodBean.getPeriod_type())
        {
            //minutely data
            oResult = IQDataFeedHistoryDataHandler.getInstance().getMinuteFinanceData(oMarketBean.getMarket_name(), oMarketPeriodBean.getValue(), CommonDefine.MAX_CATCHE_BARS);
        }
        else if( CommonDefine.HOUR_PERIOD_TYPE == oMarketPeriodBean.getPeriod_type())
        {
            //hourly data
            oResult = IQDataFeedHistoryDataHandler.getInstance().getMinuteFinanceData(oMarketBean.getMarket_name(), oMarketPeriodBean.getValue()*60, CommonDefine.MAX_CATCHE_BARS);
        }
        else if( CommonDefine.DAILY_PERIOD_TYPE == oMarketPeriodBean.getPeriod_type())
        {
            //daily data
            oResult = IQDataFeedHistoryDataHandler.getInstance().getDayFinanceData(oMarketBean.getMarket_name(), String.valueOf(CommonDefine.MAX_CATCHE_BARS));
        }
        
        if(null != oResult)
        {
            //parse result
            List<HLOCDataBean> hlocBars = parseHLOCData(oResult);
            
            for(int i=0; i<hlocBars.size(); i++)
            {
                HLOCDataBean hlocData = hlocBars.get(i);
                
                DataCache.getInstance().addHLOCData(oMarketBean.getId()+":"+oMarketPeriodBean.getId(), hlocData);
            }
        }  
    }
    
    private List<HLOCDataBean> parseHLOCData(ArrayList<String> oStringData)
    {
        return IQDataFeedHistoryDataHandler.getInstance().convertStringDataToHLOC(oStringData);
    }
    
    public IQuoteEngine getQuoteEngine(long marketID, long periodID)
    {
        if(null == _allQuoteEngine)
            return null;
        
        return _allQuoteEngine.get(marketID+":"+periodID);
    }
    
    public void startAll()
    {
        if(null == _allQuoteEngine)
            return;
        
        Iterator<IQuoteEngine> oIt = _allQuoteEngine.values().iterator();
        while(oIt.hasNext())
        {
            IQuoteEngine oQuoteEngine = (IQuoteEngine)oIt.next();
            oQuoteEngine.startEngine();
        }
    }    
}
