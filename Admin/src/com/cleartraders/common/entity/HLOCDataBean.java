package com.cleartraders.common.entity;

import java.io.Serializable;

public class HLOCDataBean  implements Serializable
{
    private static final long serialVersionUID = -6249947731357481552L;
    private String dataType="";
    private long marketID=0;
    private long date=0;
    private double open=0.0;
    private double close=0.0;
    private double high=0.0;
    private double low=0.0;
    private double volume=0.0;
    
    public double getClose()
    {
        return close;
    }
    public void setClose(double close)
    {
        this.close = close;
    }
    public String getDataType()
    {
        return dataType;
    }
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }
    public long getDate()
    {
        return date;
    }
    public void setDate(long date)
    {
        this.date = date;
    }
    public double getHigh()
    {
        return high;
    }
    public void setHigh(double high)
    {
        this.high = high;
    }
    public double getLow()
    {
        return low;
    }
    public void setLow(double low)
    {
        this.low = low;
    }
    public long getMarketID()
    {
        return marketID;
    }
    public void setMarketID(long marketID)
    {
        this.marketID = marketID;
    }
    public double getOpen()
    {
        return open;
    }
    public void setOpen(double open)
    {
        this.open = open;
    }
    public double getVolume()
    {
        return volume;
    }
    public void setVolume(double volume)
    {
        this.volume = volume;
    }
    
    public String quoteToString()
    {
        String quoteString = "";
        quoteString += "market id="+marketID;
        quoteString += ", Date ="+date;
        quoteString += ", Open ="+open;
        quoteString += ", Close="+close;
        quoteString += ", High="+high;
        quoteString += ", Low="+low;
        quoteString += ", Volume="+volume;
        
        return quoteString;
    }
}
