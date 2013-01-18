package com.cleartraders.common.entity;


/**
 * 
    create table products_table
    (
        id                  int unsigned not null primary key,
        name            varchar(100), #product name
        paid            int unsigned not null, #0: paid   1: free trial
        period          int unsigned not null, #days
        total_markets   int unsigned not null, #market numbers
        price           double not null,# product price US$
        include_sms     int unsigned not null,
        active          int unsigned not null default 0 #0: inactive  1: active 
    );
    
 * @author Administrator
 *
 */
public class ProductBean
{
    private long id;
    private String name;
    private int paid;
    private int period;
    private int totalMarkets;
    private double price;
    private int includeSMS;
    private int active;
    private String paypal_btn_id="";
    
    public String getPaypal_btn_id()
    {
        return paypal_btn_id;
    }


    public void setPaypal_btn_id(String paypal_btn_id)
    {
        this.paypal_btn_id = paypal_btn_id;
    }

    public String getDescrption()
    {
        String descrption="";
        
        descrption += "id="+this.id;
        descrption += ";";
        
        descrption += "name="+this.name;
        descrption += ";";
        
        descrption += "paid="+this.paid;
        descrption += ";";
        
        descrption += "period="+this.period;
        descrption += ";";
        
        descrption += "totalMarkets="+this.totalMarkets;
        descrption += ";";
        
        descrption += "price="+this.price;
        descrption += ";";
        
        descrption += "includeSMS="+this.includeSMS;
        descrption += ";";
        
        descrption += "paypalBtnID="+this.paypal_btn_id;
        descrption += ";";
        
        descrption += "active="+this.active;
        
        return descrption;
    }
    
    
    public int getActive()
    {
        return active;
    }
    public void setActive(int active)
    {
        this.active = active;
    }
    public long getId()
    {
        return id;
    }
    public void setId(long id)
    {
        this.id = id;
    }
    public int getIncludeSMS()
    {
        return includeSMS;
    }
    public void setIncludeSMS(int includeSMS)
    {
        this.includeSMS = includeSMS;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public int getPaid()
    {
        return paid;
    }
    public void setPaid(int paid)
    {
        this.paid = paid;
    }
    public int getPeriod()
    {
        return period;
    }
    public void setPeriod(int period)
    {
        this.period = period;
    }
    public double getPrice()
    {
        return price;
    }
    public void setPrice(double price)
    {
        this.price = price;
    }
    public int getTotalMarkets()
    {
        return totalMarkets;
    }
    public void setTotalMarkets(int totalMarkets)
    {
        this.totalMarkets = totalMarkets;
    }
    
    
}
