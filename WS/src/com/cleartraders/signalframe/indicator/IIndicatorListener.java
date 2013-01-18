package com.cleartraders.signalframe.indicator;

import java.util.Map;

/**
 * This listener is for the front end, so, just notify the latest value
 * @author Administrator
 *
 */
public interface IIndicatorListener
{
    public void notifyIndicatorValue(Map<String, Double> currentIndicatorValue, IndicatorBase oIndicator);
}
