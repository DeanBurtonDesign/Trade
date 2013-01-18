package com.cleartraders.common.entity;

/**
 * 
 * Ref:
create table market_period_table
(
    id                   int unsigned not null primary key,  
    period_type          int unsigned not null,  # 1: Minute  2: Hour  3: Day  4: Week 5: Month 6: Year
    period_name              varchar(50) not null,
    value                                int unsigned not null
);
 * @author Peter
 *
 */
public class MarketPeriodBean
{
    private long id=0;
    private int period_type=0;//# 1: Minute  2: Hour  3: Day  4: Week 5: Month 6: Year
    private String period_name="";
    private int value=0;
    
    public MarketPeriodBean(){};
    
    public MarketPeriodBean(MarketPeriodBean cloneBean)
    {
        id = cloneBean.getId();
        period_type = cloneBean.getPeriod_type();
        period_name = cloneBean.getPeriod_name();
        value = cloneBean.getValue();
    }
    
    public long getId()
    {
        return id;
    }
    public void setId(long id)
    {
        this.id = id;
    }
    public String getPeriod_name()
    {
        return period_name;
    }
    public void setPeriod_name(String period_name)
    {
        this.period_name = period_name;
    }
    public int getPeriod_type()
    {
        return period_type;
    }
    public void setPeriod_type(int period_type)
    {
        this.period_type = period_type;
    }
    public int getValue()
    {
        return value;
    }
    public void setValue(int value)
    {
        this.value = value;
    }
    
    public int getPeriodMinutes()
    {
        int iResult = 0;
        switch(period_type)
        {
            case 1:
                iResult = value;
                break;
            case 2:
                iResult = value*60;
                break;
            case 3:
                iResult = value*60*24;
                break;
            case 4:
                iResult = value*60*24*7;
                break;
            case 5:
                iResult = value*60*24*30;
                break;
            case 6:
                iResult = value*60*24*365;
                break;
            default:
                iResult = value;
                break;
        }
        
        return iResult;
    }
}
