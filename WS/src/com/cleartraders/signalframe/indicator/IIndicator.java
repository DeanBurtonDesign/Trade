package com.cleartraders.signalframe.indicator;

import java.util.ArrayList;
import java.util.Map;

import com.cleartraders.common.entity.IndicatorValueBean;
import com.cleartraders.common.entity.IndicatorsSimpleValue;


public interface IIndicator
{
    public void initIndicator();
    public void calculateIndicator();
    
    public ArrayList<IndicatorsSimpleValue> getIndicatorsValue(long strategyID);
    public IndicatorValueBean getIndicatorValue(String indicatorName, long date);
    
    public void addListener(IIndicatorListener indicatorListener);
    public void notifyIndicator(Map<String, Double> indicatorsValue);
}
