package com.cleartraders.datafeed.model.bean;

public class MarketBean
{
    /*
    create table market_type_table
    (
        id              int unsigned not null primary key,
        display_name    varchar(100),
        market_name     varchar(100),
        active          int default 2 # 1: enable 2: disable    
    );
     */
    
    private long id=0;
    private String display_name = "";
    private String market_name = "";
    private int active = -1;
    
    public int getActive()
    {
        return active;
    }
    public void setActive(int active)
    {
        this.active = active;
    }
    public String getDisplay_name()
    {
        return display_name;
    }
    public void setDisplay_name(String display_name)
    {
        this.display_name = display_name;
    }
    public long getId()
    {
        return id;
    }
    public void setId(long id)
    {
        this.id = id;
    }
    public String getMarket_name()
    {
        return market_name;
    }
    public void setMarket_name(String market_name)
    {
        this.market_name = market_name;
    }
    
    
}
