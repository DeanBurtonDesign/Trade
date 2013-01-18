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
import com.cleartraders.signalframe.indicator.SMA;
import com.cleartraders.ws.db.WSDBAccessor;

public class StrategyMACrossover extends StrategyBase
{
    private Map<String, SMA> _firstSMAIndicators = new HashMap<String, SMA>(); //period is 7
    private Map<String, SMA> _secondSMAIndicators = new HashMap<String, SMA>();//period is 14
    private Map<String, SMA> _thirdSMAIndicators = new HashMap<String, SMA>();//period is 21
    private Map<String, Signal> _latestSignal = new HashMap<String, Signal>();
    
    public StrategyMACrossover(long strategyID, String strategySystemName, boolean isRealtime)
    {
        super(strategyID, strategySystemName,isRealtime);
                
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
        
        long firstIndicatorID = DataCache.getInstance().getIndicatorByNameAndPeriod(SMA.SMA_INDICATOR_NAME, 7).getId();
        long secondIndicatorID = DataCache.getInstance().getIndicatorByNameAndPeriod(SMA.SMA_INDICATOR_NAME, 14).getId();
        long thirdIndicatorID = DataCache.getInstance().getIndicatorByNameAndPeriod(SMA.SMA_INDICATOR_NAME, 21).getId();
        
        for(int i=0; i<oAllMarketBean.size(); i++)
        {
            MarketTypeBean oMarketBean = oAllMarketBean.get(i);
            
            //for(int j=0; j<1; j++)
            for(int j=0; j<oAllMarketPeriod.size(); j++)
            {
                MarketPeriodBean oMarketPeriodBean = oAllMarketPeriod.get(j);
                
                Signal oLatestSignal = SFDBAccessor.getInstance().getLatestSignal(oMarketBean.getId(), this.getStrategyID(), oMarketPeriodBean.getId());
                String signalKey = oMarketBean.getId()+":"+oMarketPeriodBean.getId();
                if(null != oLatestSignal) 
                    _latestSignal.put(signalKey, oLatestSignal);
                
                //Note: Indicator Name must be composed by Name:MarketID:PeriodID
                //SMS 7
                String firstIndicatorName = Utils.generateIndicatorName(SMA.SMA_INDICATOR_NAME+7, oMarketBean.getId(), oMarketPeriodBean.getId());
                SMA oFirstSMA = new SMA(DataCache.getInstance().getHLOCData(oMarketBean.getId(), oMarketPeriodBean.getId()),firstIndicatorID,firstIndicatorName,7,0,true);
                _firstSMAIndicators.put(firstIndicatorName, oFirstSMA);
                
                //SMS 14
                String secondIndicatorName = Utils.generateIndicatorName(SMA.SMA_INDICATOR_NAME+14, oMarketBean.getId(), oMarketPeriodBean.getId());
                SMA oSecondSMA = new SMA(DataCache.getInstance().getHLOCData(oMarketBean.getId(), oMarketPeriodBean.getId()),secondIndicatorID,secondIndicatorName,14,0,true);
                _secondSMAIndicators.put(secondIndicatorName, oSecondSMA);
                
                //SMS 21
                String thirdIndicatorName = Utils.generateIndicatorName(SMA.SMA_INDICATOR_NAME+21, oMarketBean.getId(), oMarketPeriodBean.getId());
                SMA oThirdSMA = new SMA(DataCache.getInstance().getHLOCData(oMarketBean.getId(), oMarketPeriodBean.getId()),thirdIndicatorID,thirdIndicatorName,21,0,true);
                _thirdSMAIndicators.put(thirdIndicatorName, oThirdSMA);
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
        
        if(null == _firstSMAIndicators || null == _secondSMAIndicators || null == _thirdSMAIndicators)
            return;
        
        long marketID = Long.parseLong(quoteEngineID.split(":")[0]);
        long periodID = Long.parseLong(quoteEngineID.split(":")[1]);
        
        String firstIndicatorName = Utils.generateIndicatorName(SMA.SMA_INDICATOR_NAME+7, marketID, periodID);
        SMA oSMA7 = _firstSMAIndicators.get(firstIndicatorName);
        
        if(null == oSMA7)
            return;
        
        IndicatorValueBean firstIndicatorValue = oSMA7.getIndicatorValue("", quote.getDate());
        
        String secondIndicatorName = Utils.generateIndicatorName(SMA.SMA_INDICATOR_NAME+14, marketID, periodID);
        SMA oSMA14 = _secondSMAIndicators.get(secondIndicatorName);
        
        if(null == oSMA14)
            return;
        
        IndicatorValueBean secondIndicatorValue = oSMA14.getIndicatorValue("", quote.getDate());
        
        String thirdIndicatorName = Utils.generateIndicatorName(SMA.SMA_INDICATOR_NAME+21, marketID, periodID);
        SMA oSMA21 = _thirdSMAIndicators.get(thirdIndicatorName);
        
        if(null == oSMA21)
            return;
        
        IndicatorValueBean thirdIndicatorValue = oSMA21.getIndicatorValue("", quote.getDate());
        
        String signalKey = marketID+":"+periodID;
        
        if(null == _latestSignal.get(signalKey))
        {
            Signal latestLongShortCloseSignal = WSDBAccessor.getInstance().getLatestSignal(this.getStrategyID(), marketID, (int)periodID);
            if(null != latestLongShortCloseSignal && latestLongShortCloseSignal.getId() > 0)
            {            
                _latestSignal.put(signalKey, latestLongShortCloseSignal);
            }
            else
            {
                _latestSignal.put(signalKey, new Signal());
            }
        }
        
        //Long signal:   SMA(7) > SMA(14) and SMA(7) > SMA(21)        
        if(firstIndicatorValue.getYValue() > secondIndicatorValue.getYValue() && firstIndicatorValue.getYValue() > thirdIndicatorValue.getYValue())
        {
            //if latest signal is NULL or ( signal isn't null and it is not Long_signal)
            if( null == _latestSignal.get(signalKey) || 
                ( null != _latestSignal.get(signalKey) 
                  && _latestSignal.get(signalKey).getSignal_type() != CommonDefine.LONG_SIGNAL)
              )
            {
                InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Strategy "+this.getStrategySystemName()+": generate Long signal!");
                LogTools.getInstance().insertLog(DebugLevel.INFO,"Strategy "+this.getStrategySystemName()+": generate Long signal!");
                
                Signal oLongSignal = createSignal(quote.getDate(), CommonDefine.LONG_SIGNAL, marketID, periodID,quote.getClose());
                
                _latestSignal.put(signalKey, oLongSignal);
                
                handleSignal(oLongSignal);
            }
            
            return ;
        }
        
        //Short signal:  SMA(7) < SMA(14) and SMA(7) < SMA(21)
        if(firstIndicatorValue.getYValue() < secondIndicatorValue.getYValue() && firstIndicatorValue.getYValue() < thirdIndicatorValue.getYValue())
        {
            //if latest signal is NULL or ( signal isn't null and it is not Short Signal)
            if( null == _latestSignal.get(signalKey) || 
                ( null != _latestSignal.get(signalKey) 
                  && _latestSignal.get(signalKey).getSignal_type() != CommonDefine.SHORT_SIGNAL)
              )
            {
                InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Strategy "+this.getStrategySystemName()+": generate Short signal!");
                LogTools.getInstance().insertLog(DebugLevel.INFO,"Strategy "+this.getStrategySystemName()+": generate Short signal!");
                
                Signal oShortSignal = createSignal(quote.getDate(), CommonDefine.SHORT_SIGNAL, marketID, periodID,quote.getClose());
                
                _latestSignal.put(signalKey, oShortSignal);
                
                handleSignal(oShortSignal);
            }
            
            return;
        }
        
        //Close signal:  if previous signal is Long,  SMA(7) < SMA(14), 
        //               if previous signal is Short, SMA(7) > SMA(14)
        //Signal latestLongShortCloseSignal = WSDBAccessor.getInstance().getLatestSignal(this.getStrategyID(), marketID, (int)periodID);
        Signal latestLongShortCloseSignal =  _latestSignal.get(signalKey);       
        if(null == latestLongShortCloseSignal || latestLongShortCloseSignal.getId() < 1)
            return;
        
        //remove close signal 2010/5/28
//        if(CommonDefine.LONG_SIGNAL == latestLongShortCloseSignal.getSignal_type())
//        {
//            if(firstIndicatorValue.getYValue() < secondIndicatorValue.getYValue())
//            {
//                //if latest signal is NULL or ( signal isn't null and it is not Short Signal)
//                if( null == _latestSignal.get(signalKey) || 
//                    ( null != _latestSignal.get(signalKey) 
//                      && _latestSignal.get(signalKey).getSignal_type() != CommonDefine.CLOSE_SIGNAL)
//                  )
//                {
//                    //this is Close signal
//                    InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Strategy "+this.getStrategySystemName()+": generate Close signal!");
//                    LogTools.getInstance().insertLog(DebugLevel.INFO,"Strategy "+this.getStrategySystemName()+": generate Close signal!");
//                    
//                    Signal oCloseSignal = createSignal(quote.getDate(), CommonDefine.CLOSE_SIGNAL, marketID, periodID,quote.getClose());
//                    
//                    _latestSignal.put(signalKey, oCloseSignal);
//                    
//                    handleSignal(oCloseSignal);
//                }
//                
//                return;
//            }
//        }
//        else if(CommonDefine.SHORT_SIGNAL == latestLongShortCloseSignal.getSignal_type())
//        {
//            if(firstIndicatorValue.getYValue() > secondIndicatorValue.getYValue())
//            {
//                //if latest signal is NULL or ( signal isn't null and it is not Short Signal)
//                if( null == _latestSignal.get(signalKey) || 
//                    ( null != _latestSignal.get(signalKey) 
//                      && _latestSignal.get(signalKey).getSignal_type() != CommonDefine.CLOSE_SIGNAL)
//                  )
//                {                    
//                    //this is Close signal
//                    InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Strategy "+this.getStrategySystemName()+": generate Close signal!");
//                    LogTools.getInstance().insertLog(DebugLevel.INFO,"Strategy "+this.getStrategySystemName()+": generate Close signal!");
//                    
//                    Signal oCloseSignal = createSignal(quote.getDate(), CommonDefine.CLOSE_SIGNAL, marketID, periodID,quote.getClose());
//                    
//                    _latestSignal.put(signalKey, oCloseSignal);
//                    
//                    handleSignal(oCloseSignal);
//                }
//                
//                return;
//            }
//        }
    }
}
