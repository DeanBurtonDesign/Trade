package com.cleartraders.signalframe.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.HLOCDataBean;
import com.cleartraders.common.entity.MarketPeriodBean;
import com.cleartraders.common.entity.MarketTypeBean;
import com.cleartraders.common.entity.Signal;
import com.cleartraders.common.entity.StrategySignal;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.signalframe.Utils;
import com.cleartraders.signalframe.conf.SFResManager;
import com.cleartraders.signalframe.indicator.IndicatorBase;
import com.cleartraders.signalframe.indicator.TopBottomTurnIndicator;
import com.cleartraders.ws.db.WSDBAccessor;

public class StrategyTopBottomTurn extends StrategyBase 
{
    private Map<String, TopBottomTurnIndicator> _topBottomTurnIndicators = null;
    private Map<String, StrategySignal> _latestSignal = new HashMap<String, StrategySignal>();
    
    public StrategyTopBottomTurn(long strategyID, String strategySystemName, boolean isRealtime)
    {
        super(strategyID,strategySystemName,isRealtime);
        
        initIndicators();
        
        registerQuoteEngine();
    }
    
    public void initIndicators()
    {
        _topBottomTurnIndicators = new HashMap<String, TopBottomTurnIndicator>();
        
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
                
                String indicatorName = Utils.generateIndicatorName("topbottomturn", oMarketBean.getId(), oMarketPeriodBean.getId());
                
                TopBottomTurnIndicator topBottomTurnIndicator = new TopBottomTurnIndicator(DataCache.getInstance().getHLOCData(oMarketBean.getId(), oMarketPeriodBean.getId()),0,indicatorName,1,0,true);
                topBottomTurnIndicator.addListener(this);
                
                _topBottomTurnIndicators.put(indicatorName, topBottomTurnIndicator);
            }
        }
    }
    
    /**
     * Because there is only one indicator for this strategy, so, system only need to handle notifyIndicatorValue
     */
    public void onQuoteUpdate(boolean isNewQuote, String quoteEngineID, HLOCDataBean quote)
    {                
    }
    
    public void notifyIndicatorValue(Map<String, Double> currentIndicatorValues, IndicatorBase oIndicator)
    {
        //InfoTrace.getInstance().printInfo(DebugLevel.INFO," Strategy: "+this.getStrategySystemName()+" got Indicator "+oIndicator.getIndicatorName()+" value=> "+currentIndicatorValues.get(oIndicator.getIndicatorName()).doubleValue());
        
        long marketID = Utils.getMarketIDFromIndicatorName(oIndicator.getIndicatorName());
        long periodID = Utils.getPeriodIDFromIndicatorName(oIndicator.getIndicatorName());
        
        double currentIndicatorValue = currentIndicatorValues.get(oIndicator.getIndicatorName()).doubleValue();
        long priceDate = currentIndicatorValues.get("date").longValue();
        
        String signalKey = marketID+":"+periodID;
        
        //If price goes below(buy signal) or above (sell signal) the highest (buy) or lowest (sell) level between the three signals close trade.        
        // (1) get the latest signal 
        if(null == _latestSignal.get(signalKey))
        {
            Signal latestLongShortCloseSignal = WSDBAccessor.getInstance().getLatestSignal(this.getStrategyID(), marketID, (int)periodID);
            if(null == latestLongShortCloseSignal || latestLongShortCloseSignal.getId() < 1)
                return;
            
            int numberOfPreviousSignals = 3;
            //get the previous Three signals
            List<Signal> historySignals = WSDBAccessor.getInstance().getLatestBasiclSignal(marketID, (int)periodID, numberOfPreviousSignals);
            if(null == historySignals || historySignals.size() != numberOfPreviousSignals)
            {
                _latestSignal.put(signalKey, new StrategySignal());
                
                return;
            }
            
            StrategySignal strategySignal = new StrategySignal(latestLongShortCloseSignal);
            strategySignal.setRelatedLowestPrice(getLowestPrice(historySignals.get(2).getGenerate_date(), marketID, periodID, latestLongShortCloseSignal.getSignalValue()));
            strategySignal.setRelatedHighestPrice(getHighestPrice(historySignals.get(2).getGenerate_date(), marketID, periodID, latestLongShortCloseSignal.getSignalValue()));
                        
            _latestSignal.put(signalKey, strategySignal);
        }
        
        StrategySignal strategySignal = _latestSignal.get(signalKey);
        
        if(CommonDefine.CLOSE_SIGNAL == strategySignal.getIncludedSignal().getSignal_type())
        {
            //2.1) If the latest signal is Close, then ignore it.              
            return;
        }
        else if(CommonDefine.LONG_SIGNAL == strategySignal.getIncludedSignal().getSignal_type())
        {
            //2.2) If the latest signal is Long, then, check the current price is lower the lowest or not, if does, then Close signal generated             
            if( currentIndicatorValue < strategySignal.getRelatedLowestPrice() )
            {
                InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Generate Close signal when Quote " +currentIndicatorValue+ " lower than lowest price "+strategySignal.getRelatedLowestPrice()+" by Indicator....");
                
                int numberOfPreviousSignals = 3;
                
                //get the previous three signals
                List<Signal> historySignals = WSDBAccessor.getInstance().getLatestBasiclSignal(marketID, (int)periodID, numberOfPreviousSignals);
                if(null == historySignals || historySignals.size() != numberOfPreviousSignals)
                {
                    return;
                }
                
                Signal oCloseSignal = createSignal(priceDate, CommonDefine.CLOSE_SIGNAL, marketID, periodID,currentIndicatorValue);
                
                StrategySignal newStrategySignal = new StrategySignal(oCloseSignal);
                
                strategySignal.setRelatedLowestPrice(getLowestPrice(historySignals.get(2).getGenerate_date(), marketID, periodID, oCloseSignal.getSignalValue()));
                strategySignal.setRelatedHighestPrice(getHighestPrice(historySignals.get(2).getGenerate_date(), marketID, periodID, oCloseSignal.getSignalValue()));
                
                _latestSignal.put(marketID+":"+periodID, newStrategySignal);
                
                //for test
                if(oCloseSignal.getExpire_date() == 59)
                {
                    InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Expire date error!");
                }
                //end for test
                
                handleSignal(oCloseSignal);
            }
        }
        else if(CommonDefine.SHORT_SIGNAL == strategySignal.getIncludedSignal().getSignal_type())
        {
            //2.3) If the latest signal is Short, then,check the current price is higher than the highest or not, if does, then Close signal generated
            
            if( currentIndicatorValue > strategySignal.getRelatedHighestPrice() )
            {
                InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Generate Close signal when Quote " +currentIndicatorValue+ " higher than highest price "+strategySignal.getRelatedHighestPrice()+" by Indicator....");
                
                int numberOfPreviousSignals = 3;
                
                //get the previous three signals
                List<Signal> historySignals = WSDBAccessor.getInstance().getLatestBasiclSignal(marketID, (int)periodID, numberOfPreviousSignals);
                if(null == historySignals || historySignals.size() != numberOfPreviousSignals)
                {
                    return;
                }
                
                Signal oCloseSignal = createSignal(priceDate, CommonDefine.CLOSE_SIGNAL, marketID, periodID, currentIndicatorValue);
                
                StrategySignal newStrategySignal = new StrategySignal(oCloseSignal);
                
                strategySignal.setRelatedLowestPrice(getLowestPrice(historySignals.get(2).getGenerate_date(), marketID, periodID, oCloseSignal.getSignalValue()));
                strategySignal.setRelatedHighestPrice(getHighestPrice(historySignals.get(2).getGenerate_date(), marketID, periodID, oCloseSignal.getSignalValue()));
                
                _latestSignal.put(marketID+":"+periodID, newStrategySignal);
                
                //for test
                if(oCloseSignal.getExpire_date() == 59)
                {
                    InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Expire date error!");
                }
                //end for test
                
                handleSignal(oCloseSignal);
            }
        }
        else
        {
            return;
        }
    }
        
    private double getLowestPrice(long fromDate, long marketID, long periodID, double currentLow)
    {
        double lowestPrice = currentLow;
        
        List<HLOCDataBean> previousHLOCDataList = DataCache.getInstance().getHLOCData(marketID, periodID);
        
        if(null == previousHLOCDataList)
            return 0.0;
        
        for(int i=0; i<previousHLOCDataList.size(); i++)
        {
            HLOCDataBean oTempBean = previousHLOCDataList.get(i);
            
            if(oTempBean.getDate() > fromDate && oTempBean.getLow() < lowestPrice)
            {
                lowestPrice = oTempBean.getLow();
            }
        }
        
        return lowestPrice;
    }
    
    private double getHighestPrice(long fromDate, long marketID, long periodID, double currentHigh)
    {
        double highestPrice = currentHigh;
        
        List<HLOCDataBean> previousHLOCDataList = DataCache.getInstance().getHLOCData(marketID, periodID);
        
        if(null == previousHLOCDataList)
            return 0.0;
        
        for(int i=0; i<previousHLOCDataList.size(); i++)
        {
            HLOCDataBean oTempBean = previousHLOCDataList.get(i);
            
            if(oTempBean.getDate() > fromDate && oTempBean.getHigh() > highestPrice)
            {
                highestPrice = oTempBean.getHigh();
            }
        }
        
        return highestPrice;
    }
    
    public boolean calculateSignal(Signal currentSignal)
    {
        if(null == currentSignal)
            return false;
                
        int numberOfPreviousSignals = 3;
        
        //get the previous two signals
        List<Signal> historySignals = WSDBAccessor.getInstance().getLatestBasiclSignal(currentSignal.getMarket_type_id(), currentSignal.getSignal_period(), numberOfPreviousSignals);
        if(null == historySignals || historySignals.size() != numberOfPreviousSignals)
        {
            return true;
        }
        
        Signal firstSignal = historySignals.get(2);
        Signal secondSignal = historySignals.get(1);
        Signal thirdSignal = historySignals.get(0);// the third signal is currentSignal since it has been stored into DB before call this function
        
        //check if this signal is Long or Short or Close
        
        //For Long signal: 
        //Logic as below,
        //BUY, SELL, BUY (SELL must lower than other two BUY, bars should be no more than Max Allowed)
        if( CommonDefine.BUY_SINGAL == firstSignal.getSignal_type() && 
            CommonDefine.SELL_SINGAL == secondSignal.getSignal_type() &&
            CommonDefine.BUY_SINGAL == thirdSignal.getSignal_type() )
        {
            InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Match Long signal logic..... Buy, Sell, Buy....");
            
            if( secondSignal.getSignalValue() < firstSignal.getSignalValue() && 
                secondSignal.getSignalValue() < thirdSignal.getSignalValue() )
            {
                InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Match Long signal logic..... Buy high, Sell low, Buy high....");
                
                //check max allowed condition
                if(checkMaxAllowedCondition(firstSignal, secondSignal, thirdSignal))
                {
                    InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Match Long signal max allowed, long signal geneated....");
                    
                    //this is one Long signal
                    Signal oLongSignal = new Signal(currentSignal);
                    oLongSignal.setSignal_type(CommonDefine.LONG_SIGNAL);
                    oLongSignal.setStrategy_id(getStrategyID());   
                                        
                    StrategySignal strategySignal = new StrategySignal(oLongSignal);
                    strategySignal.setRelatedLowestPrice(getLowestPrice(firstSignal.getGenerate_date(), oLongSignal.getMarket_type_id(), oLongSignal.getSignal_period(), oLongSignal.getSignalValue()));
                    strategySignal.setRelatedHighestPrice(getHighestPrice(firstSignal.getGenerate_date(), oLongSignal.getMarket_type_id(), oLongSignal.getSignal_period(), oLongSignal.getSignalValue()));
                    
                    _latestSignal.put(oLongSignal.getMarket_type_id()+":"+oLongSignal.getSignal_period(), strategySignal);
                    
                    //for test
                    if(oLongSignal.getExpire_date() == 59)
                    {
                        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Expire date error!");
                    }
                    //end for test
                    
                    return handleSignal(oLongSignal);
                }
            }
        }        
        //For Short signal: 
        //Logic as below,
        //SELL, BUY, SELL (BUY must more than other two SELL, bars should be no more than Max Allowed)
        else if( CommonDefine.SELL_SINGAL == firstSignal.getSignal_type() && 
                 CommonDefine.BUY_SINGAL == secondSignal.getSignal_type() &&
                 CommonDefine.SELL_SINGAL == thirdSignal.getSignal_type() )
        {
            InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Match Long signal logic..... Sell, Buy, Sell ....");
            
            if( secondSignal.getSignalValue() > firstSignal.getSignalValue() && 
                secondSignal.getSignalValue() > thirdSignal.getSignalValue() )
            {
                InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Match Long signal logic..... Sell low, Buy high, Sell low....");
                
                //check max allowed condition
                if(checkMaxAllowedCondition(firstSignal, secondSignal, thirdSignal))
                {
                    InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Match Long signal max allowed, short signal geneated....");
                    
                    //this is one Short signal
                    Signal oShortSignal = new Signal(currentSignal);
                    oShortSignal.setSignal_type(CommonDefine.SHORT_SIGNAL);
                    oShortSignal.setStrategy_id(getStrategyID());
                    
                    StrategySignal strategySignal = new StrategySignal(oShortSignal);
                    strategySignal.setRelatedLowestPrice(getLowestPrice(firstSignal.getGenerate_date(), oShortSignal.getMarket_type_id(), oShortSignal.getSignal_period(), oShortSignal.getSignalValue()));
                    strategySignal.setRelatedHighestPrice(getHighestPrice(firstSignal.getGenerate_date(), oShortSignal.getMarket_type_id(), oShortSignal.getSignal_period(), oShortSignal.getSignalValue()));
                    
                    _latestSignal.put(oShortSignal.getMarket_type_id()+":"+oShortSignal.getSignal_period(), strategySignal);
                    
                    //for test
                    if(oShortSignal.getExpire_date() == 59)
                    {
                        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Expire date error!");
                    }
                    //end for test
                    
                    return handleSignal(oShortSignal);
                }
            }
        }
        
        //For Close signal
        /*
         * Logic as below:
         * 
         * when generate a new basic signal(buy or sell), then check previous two signal, if they(three) could 
         * form new logic signal, then generate Buy(Long) or Sell(Short) signal, otherwise, this is Close signal.         * 
         * Moreover, if previous is Close signal, and the new basic signal seems a Close signal, then ignore it
         */
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"This is CLose signal ....");
        
        //check the latest signal is Close or not
        Signal latestSignal = WSDBAccessor.getInstance().getLatestSignal(getStrategyID(), currentSignal.getMarket_type_id(), currentSignal.getSignal_period());
        if(null != latestSignal && CommonDefine.CLOSE_SIGNAL == latestSignal.getSignal_type())
        {
            InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Previous one is CLose signal, so ignore it ....");
            
            //ignore this signal 
            return true;
        }
        else
        {
            InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Generate CLose signal....");
            
            Signal oCloseSignal = new Signal(currentSignal);
            oCloseSignal.setSignal_type(CommonDefine.CLOSE_SIGNAL);
            oCloseSignal.setStrategy_id(getStrategyID());
            
            StrategySignal strategySignal = new StrategySignal(oCloseSignal);
            strategySignal.setRelatedLowestPrice(getLowestPrice(firstSignal.getGenerate_date(), oCloseSignal.getMarket_type_id(), oCloseSignal.getSignal_period(), oCloseSignal.getSignalValue()));
            strategySignal.setRelatedHighestPrice(getHighestPrice(firstSignal.getGenerate_date(), oCloseSignal.getMarket_type_id(), oCloseSignal.getSignal_period(), oCloseSignal.getSignalValue()));
            
            _latestSignal.put(oCloseSignal.getMarket_type_id()+":"+oCloseSignal.getSignal_period(), strategySignal);
            
            //for test
            if(oCloseSignal.getExpire_date() == 59)
            {
                InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Expire date error!");
            }
            //end for test
            
            return handleSignal(oCloseSignal);
        }
    }
    
    private boolean checkMaxAllowedCondition(Signal firstSignal, Signal secondSignal, Signal currentSignal)
    {
        //get those candle data from first signal to current
        List<HLOCDataBean> oHLOCDataList = DataCache.getInstance().getHLOCData(currentSignal.getMarket_type_id(),currentSignal.getSignal_period());
        List<HLOCDataBean> firstHLOCDataList = new ArrayList<HLOCDataBean>();
        List<HLOCDataBean> secondHLOCDataList = new ArrayList<HLOCDataBean>();
        
        for(int i=0; i<oHLOCDataList.size(); i++)
        {
            HLOCDataBean oHLOCDataBean = oHLOCDataList.get(i);
            
            if(oHLOCDataBean.getDate() > firstSignal.getGenerate_date() && oHLOCDataBean.getDate() < secondSignal.getGenerate_date())
            {
                firstHLOCDataList.add(oHLOCDataBean);
            }
            else if(oHLOCDataBean.getDate() > secondSignal.getGenerate_date() && oHLOCDataBean.getDate() < currentSignal.getGenerate_date())
            {
                secondHLOCDataList.add(oHLOCDataBean);
            }
        }
        
        //check the candles match the condition or not
        int maxAllowedCandleBarBetweenSignal = SFResManager.getInstance().getMax_allowed_candle_bar_between_signal();
        
        if(firstHLOCDataList.size() > maxAllowedCandleBarBetweenSignal || secondHLOCDataList.size() > maxAllowedCandleBarBetweenSignal)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
