package com.cleartraders.signalframe.strategy;

import java.util.Map;

import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.HLOCDataBean;
import com.cleartraders.common.entity.Signal;
import com.cleartraders.signalframe.indicator.IndicatorBase;

public class StrategySupportResistance extends StrategyBase
{
    public StrategySupportResistance(long strategyID, String strategySystemName, boolean isRealtime)
    {
        super(strategyID, strategySystemName,isRealtime);
        
        initIndicators();
        
        registerQuoteEngine();
    }
    
    public boolean calculateSignal(Signal currentSignal)
    {
        if(null == currentSignal)
        {
            return false;
        }
        
        if( CommonDefine.BUY_SINGAL != currentSignal.getSignal_type() && 
            CommonDefine.SELL_SINGAL != currentSignal.getSignal_type() &&
            CommonDefine.CLOSE_SIGNAL != currentSignal.getSignal_type())
        {
            return false;
        }
        
        //calculate the profit of basic signals
        if( CommonDefine.BUY_SINGAL == currentSignal.getSignal_type())
        {
            currentSignal.setSignal_type(CommonDefine.LONG_SIGNAL);
        }
        else if(CommonDefine.SELL_SINGAL == currentSignal.getSignal_type())
        {
            currentSignal.setSignal_type(CommonDefine.SHORT_SIGNAL);
        }
        else
        {
            currentSignal.setSignal_type(CommonDefine.CLOSE_SIGNAL);
        }
        
        return handleSignal(currentSignal);
    }
    
    /**
     *  This strategy doesn't contain any sub-indicators
     */
    public void initIndicators()
    {        
    }
    
    /**
     * This strategy doesn't contain any sub-indicators
     */
    public void notifyIndicatorValue(Map<String, Double> currentIndicatorValue, IndicatorBase oIndicator)
    {
    }

    /**
     *  This strategy doesn't contain any sub-indicators
     */
    public void onQuoteUpdate(boolean isNewQuote, String quoteEngineID, HLOCDataBean quote)
    {        
    }
}
