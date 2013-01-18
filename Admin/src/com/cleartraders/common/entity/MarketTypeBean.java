package com.cleartraders.common.entity;

/**
 * Ref:
 * 
create table market_type_table
(
    id                      int unsigned not null primary key,  
    market_type         int not null,   # 1: Forex 2: Stock
    market_type_name    varchar(100) not null, # for example, Forex
    display_name        varchar(100) not null, # for example, USD/JPYT
    market_name         varchar(100) not null, # for example, TUSDJPY
    active              int default 2, # 1: enable 2: disable   
    quote_decimal       int unsigned not null  # decimal place of price
);
 * @author Peter
 *
 */
public class MarketTypeBean
{
    private long id=0;
    private int market_type=0;
    private String market_type_name="";
    private String display_name="";
    private String market_name="";
    private int active=0;
    private int quote_decimal=0;
    
    public MarketTypeBean(){};
    
    public MarketTypeBean(MarketTypeBean cloneBean)
    {
        id=cloneBean.getId();
        market_type=cloneBean.getMarket_type();
        market_type_name=cloneBean.getMarket_type_name();
        display_name=cloneBean.getDisplay_name();
        market_name=cloneBean.getMarket_name();
        active=cloneBean.getActive();
        quote_decimal=cloneBean.getQuote_decimal();
    }
    
    public int getQuote_decimal()
    {
        return quote_decimal;
    }

    public void setQuote_decimal(int quote_decimal)
    {
        this.quote_decimal = quote_decimal;
    }
    public String getMarket_type_name()
    {
        return market_type_name;
    }
    public void setMarket_type_name(String market_type_name)
    {
        this.market_type_name = market_type_name;
    }
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
    public int getMarket_type()
    {
        return market_type;
    }
    public void setMarket_type(int market_type)
    {
        this.market_type = market_type;
    }
    
}
