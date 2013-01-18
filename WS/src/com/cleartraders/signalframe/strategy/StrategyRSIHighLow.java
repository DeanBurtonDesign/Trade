package com.cleartraders.signalframe.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.HLOCDataBean;
import com.cleartraders.common.entity.IndicatorValueBean;
import com.cleartraders.common.entity.MarketPeriodBean;
import com.cleartraders.common.entity.MarketTypeBean;
import com.cleartraders.common.entity.Signal;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.signalframe.Utils;
import com.cleartraders.signalframe.db.SFDBAccessor;
import com.cleartraders.signalframe.indicator.IndicatorBase;
import com.cleartraders.signalframe.indicator.RSI;
import com.cleartraders.ws.db.WSDBAccessor;

public class StrategyRSIHighLow extends StrategyBase
{
    private Map<String, RSI> _rsiIndicators = new HashMap<String, RSI>(); //period is 14
    private Map<String, Signal> _latestSignal = new HashMap<String, Signal>();
    
    public StrategyRSIHighLow(long strategyID, String strategySystemName, boolean isRealtime)
    {
        super(strategyID, strategySystemName, isRealtime);
       
        initIndicators();
        
        registerQuoteEngine();
    }

    @Override
    public void initIndicators()
    {
        List<MarketTypeBean> oAllMarketBean = DataCache.getInstance().getAllMarketType();
        List<MarketPeriodBean> oAllMarketPeriod = DataCache.getInstance().getAllMarketPeriod();
        
        if(null == oAllMarketBean || null == oAllMarketPeriod)
            return;
        
        long rsiIndicatorID = DataCache.getInstance().getIndicatorByNameAndPeriod(RSI.RSI_INDICATOR_NAME, 14).getId();
        long rsiHighIndicatorID = DataCache.getInstance().getIndicatorByNameAndPeriod(RSI.RSI_HIGH, 1).getId();
        long rsiLowIndicatorID = DataCache.getInstance().getIndicatorByNameAndPeriod(RSI.RSI_LOW, 1).getId();
        
        for(int i=0; i<oAllMarketBean.size(); i++)
        {
            MarketTypeBean oMarketBean = oAllMarketBean.get(i);
            
            for(int j=0; j<oAllMarketPeriod.size(); j++)
            {
                MarketPeriodBean oMarketPeriodBean = oAllMarketPeriod.get(j);
                
                Signal oLatestSignal = SFDBAccessor.getInstance().getLatestSignal(oMarketBean.getId(), this.getStrategyID(), oMarketPeriodBean.getId());
                String signalKey = oMarketBean.getId()+":"+oMarketPeriodBean.getId();
                if(null != oLatestSignal) 
                    _latestSignal.put(signalKey, oLatestSignal);
                
                //Note: Indicator Name must be composed by Name:MarketID:PeriodID
                //RSI 14
                String rsiIndicatorName = Utils.generateIndicatorName(RSI.RSI_INDICATOR_NAME+14, oMarketBean.getId(), oMarketPeriodBean.getId());
                RSI rsiIndicator = new RSI(DataCache.getInstance().getHLOCData(oMarketBean.getId(), oMarketPeriodBean.getId()),rsiIndicatorID,rsiIndicatorName,14,0,true);
                rsiIndicator.setHighIndicatorID(rsiHighIndicatorID);
                rsiIndicator.setLowIndicatorID(rsiLowIndicatorID);
                
                _rsiIndicators.put(rsiIndicatorName, rsiIndicator);
                
            }
        }
    }

    @Override
    public void notifyIndicatorValue(Map<String, Double> currentIndicatorValue, IndicatorBase oIndicator)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onQuoteUpdate(boolean isNewQuote, String quoteEngineID, HLOCDataBean quote)
    {
        if(null == quoteEngineID || null == quote)
            return;
        
        if(quoteEngineID.split(":").length != 2)
            return;
        
        if(null == _rsiIndicators)
            return;
        
        long marketID = Long.parseLong(quoteEngineID.split(":")[0]);
        long periodID = Long.parseLong(quoteEngineID.split(":")[1]);
        
        String signalKey = marketID+":"+periodID;
        
        if(null == _latestSignal.get(signalKey))
        {
            Signal latestLongShortCloseSignal = WSDBAccessor.getInstance().getLatestSignal(this.getStrategyID(), marketID, (int)periodID);
            if(null != latestLongShortCloseSignal && latestLongShortCloseSignal.getId() > 0)
            {            
                _latestSignal.put(signalKey, latestLongShortCloseSignal);
            }
        }
        
        String rsiIndicatorName = Utils.generateIndicatorName(RSI.RSI_INDICATOR_NAME+14, marketID, periodID);
        
        RSI oRSI = _rsiIndicators.get(rsiIndicatorName);
        
        if(null == oRSI)
            return;
        
        IndicatorValueBean currentRSIIndicatorValue = oRSI.getIndicatorValue(rsiIndicatorName, quote.getDate());
        IndicatorValueBean previousRSIIndicatorValue = oRSI.getPreviousIndicatorValue(rsiIndicatorName, quote.getDate());
        
        Signal latestLongShortCloseSignal =  _latestSignal.get(signalKey);       
        if(null == latestLongShortCloseSignal)
        {
            _latestSignal.put(signalKey, new Signal());
        }
        
        //remove close signal 2010/5/28
        
//        //Close Signal Logic:
//        //1) Long Close
//        //a) Latest signal is Long, and previous RSI > 30, when current RSI < 30, Close signal occur.
//        //b) Latest signal is Long, and current RSI > 70
//        if(CommonDefine.LONG_SIGNAL == latestLongShortCloseSignal.getSignal_type())
//        {
//            if( (currentRSIIndicatorValue.getYValue() > 70 ) ||
//                (previousRSIIndicatorValue.getYValue() > 30 && currentRSIIndicatorValue.getYValue() < 30))
//            {
//                //Close signal
//                InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Strategy "+this.getStrategySystemName()+": generate Close signal!");
//                LogTools.getInstance().insertLog(DebugLevel.INFO,"Strategy "+this.getStrategySystemName()+": generate Close signal!");
//                
//                Signal oCloseSignal = createSignal(quote.getDate(), CommonDefine.CLOSE_SIGNAL, marketID, periodID,quote.getClose());
//                
//                _latestSignal.put(signalKey, oCloseSignal);
//                
//                handleSignal(oCloseSignal);
//                
//                return;
//            }
//        }        
//        //2) Short Close
//        //a) Latest signal is Short, and previous RSI < 70, when current RSI > 70, Close signal occur.
//        //b) Latest signal is Short, and current RSI < 30
//        else if(CommonDefine.SHORT_SIGNAL == latestLongShortCloseSignal.getSignal_type())
//        {
//            if( (currentRSIIndicatorValue.getYValue() < 30) ||
//                (previousRSIIndicatorValue.getYValue() < 70 && currentRSIIndicatorValue.getYValue() > 70))
//            {
//                //Close signal
//                InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Strategy "+this.getStrategySystemName()+": generate Close signal!");
//                LogTools.getInstance().insertLog(DebugLevel.INFO,"Strategy "+this.getStrategySystemName()+": generate Close signal!");
//                
//                Signal oCloseSignal = createSignal(quote.getDate(), CommonDefine.CLOSE_SIGNAL, marketID, periodID,quote.getClose());
//                
//                _latestSignal.put(signalKey, oCloseSignal);
//                
//                handleSignal(oCloseSignal);
//                
//                return;
//            }
//        }
        
        //Long Signal Logic:
        //change logic 2010-8-04
        //Create the Long and Short signal without to check previous signal  
        if( previousRSIIndicatorValue.getYValue() < 30 && 
            currentRSIIndicatorValue.getYValue() > 30 /* &&
            CommonDefine.LONG_SIGNAL != latestLongShortCloseSignal.getSignal_type()*/ )
        {
            //Long signal
            InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Strategy "+this.getStrategySystemName()+": generate Long signal!");
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Strategy "+this.getStrategySystemName()+": generate Long signal!");
            
            Signal oLongSignal = createSignal(quote.getDate(), CommonDefine.LONG_SIGNAL, marketID, periodID,quote.getClose());
            
            _latestSignal.put(signalKey, oLongSignal);
            
            handleSignal(oLongSignal);
            
            return;
        }
        
        //Short Signal Logic:
        //change logic 2010-8-04
        //Create the Long and Short signal without to check previous signal
        if( previousRSIIndicatorValue.getYValue() > 70 && 
            currentRSIIndicatorValue.getYValue() < 70 /*&& 
            CommonDefine.SHORT_SIGNAL != latestLongShortCloseSignal.getSignal_type()*/ )
        {
            //Short signal
            InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Strategy "+this.getStrategySystemName()+": generate Short signal!");
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Strategy "+this.getStrategySystemName()+": generate Short signal!");
            
            Signal oShortSignal = createSignal(quote.getDate(), CommonDefine.SHORT_SIGNAL, marketID, periodID,quote.getClose());
            
            _latestSignal.put(signalKey, oShortSignal);
            
            handleSignal(oShortSignal);
            
            return;
        }        
    }

}
