package com.cleartraders.common.entity;

import java.io.Serializable;

public class IndicatorStringValue implements Serializable 
{
    private static final long serialVersionUID = -5184793348984109622L;
    
    private String indicatorStringValue = "";

    public IndicatorStringValue(String indicatorStringValue)
    {
        this.indicatorStringValue = indicatorStringValue;
    }
    
    public String getIndicatorStringValue()
    {
        return this.indicatorStringValue;
    }

    public void setIndicatorStringValue(String indicatorStringValue)
    {
        this.indicatorStringValue = indicatorStringValue;
    }
    
    
}
