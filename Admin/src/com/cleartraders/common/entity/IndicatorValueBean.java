package com.cleartraders.common.entity;

import java.io.Serializable;

public class IndicatorValueBean implements Serializable 
{
    private static final long serialVersionUID = 8579986916964273801L;

    private long marketID = 0;
    private long periodID = 0;
    private long indicatorID = 0;
    private long dateValue = 0;
    private double yValue = 0.0;
    
    public IndicatorValueBean(long dateValue, double yValue)
    {
        this.dateValue = dateValue;
        this.yValue = yValue;
    }
    
    public long getIndicatorID()
    {
        return indicatorID;
    }

    public void setIndicatorID(long indicatorID)
    {
        this.indicatorID = indicatorID;
    }

    public long getMarketID()
    {
        return marketID;
    }

    public void setMarketID(long marketID)
    {
        this.marketID = marketID;
    }

    public long getPeriodID()
    {
        return periodID;
    }

    public void setPeriodID(long periodID)
    {
        this.periodID = periodID;
    }
    
    public String toSimpleValueString()
    {
        String result = "";
        
        result += this.getDateValue();
        result += ",";
        
        result += this.getYValue();
        
        return result;
        
    }
    
    /**
     * @return
     */
    public String toTextMessage()
    {
        /**
         * INDICATOR
         * 
         * date
         * yValue
         * Market ID
         * Period ID
         * Indicator ID
         */
        
        String indicatorMessage = "INDICATOR,";
        
        indicatorMessage += this.getDateValue();
        indicatorMessage += ",";
        
        indicatorMessage += this.getYValue();
        indicatorMessage += ",";
        
        indicatorMessage += this.getMarketID();
        indicatorMessage += ",";
        
        indicatorMessage += this.getPeriodID();
        indicatorMessage += ",";
        
        indicatorMessage += this.getIndicatorID();
        
        return indicatorMessage;
    }
    
    public long getDateValue()
    {
        return dateValue;
    }
    public void setDateValue(long dateValue)
    {
        this.dateValue = dateValue;
    }
    public double getYValue()
    {
        return yValue;
    }
    public void setYValue(double value)
    {
        yValue = value;
    }
}
