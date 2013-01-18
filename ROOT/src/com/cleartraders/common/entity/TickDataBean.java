package com.cleartraders.common.entity;

public class TickDataBean
{
    private String dataType="";
    private long tickTime=0;
    private double priceValue=0;
    private double volumeValue=0;
    private double otherValue=0;
    
    public TickDataBean(){}
    
    public TickDataBean(TickDataBean cloneBean)
    {
        this.dataType=cloneBean.getDataType();
        this.tickTime=cloneBean.getTickTime();
        this.priceValue=cloneBean.getPriceValue();
        this.volumeValue=cloneBean.getVolumeValue();
        this.otherValue=cloneBean.getOtherValue();
    }
    
    public TickDataBean(String dataDescription)
    {
        if(null == dataDescription || dataDescription.split(",").length < 4)
        {
            return;
        }
        
        String[] dataArray = dataDescription.split(",");
        
        if(!"Q".equalsIgnoreCase(dataArray[0]))
        {
            return;
        }
        
        this.dataType=dataArray[0];
        this.tickTime=Long.parseLong(dataArray[1]);
        this.priceValue=Double.parseDouble(dataArray[2]);
        this.volumeValue=Double.parseDouble(dataArray[3]);
    }

    public String getDataType()
    {
        return dataType;
    }

    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }

    public double getOtherValue()
    {
        return otherValue;
    }

    public void setOtherValue(double otherValue)
    {
        this.otherValue = otherValue;
    }

    public double getPriceValue()
    {
        return priceValue;
    }

    public void setPriceValue(double priceValue)
    {
        this.priceValue = priceValue;
    }

    public long getTickTime()
    {
        return tickTime;
    }

    public void setTickTime(long tickTime)
    {
        this.tickTime = tickTime;
    }

    public double getVolumeValue()
    {
        return volumeValue;
    }

    public void setVolumeValue(double volumeValue)
    {
        this.volumeValue = volumeValue;
    }
}
