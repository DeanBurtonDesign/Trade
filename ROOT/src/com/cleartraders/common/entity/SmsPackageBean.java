package com.cleartraders.common.entity;

/**
 * 
 
create table sms_package_table
(
    id                  int unsigned not null primary key,
    product_id      int unsigned not null, # each package is under specific product
    name            varchar(100),                # package name
    sms_included    int unsigned not null, # sms credits
    sms_cost        double not null              # sms package price US$
);

 * @author Administrator
 *
 */
public class SmsPackageBean
{
    private long id=0;
    private long product_id=0;
    private String name="";
    private int sms_included=0;
    private double sms_cost=0;
    private String paypal_button_id="";
    
    public String getPaypal_button_id()
    {
        return paypal_button_id;
    }
    public void setPaypal_button_id(String paypal_button_id)
    {
        this.paypal_button_id = paypal_button_id;
    }
    public long getId()
    {
        return id;
    }
    public void setId(long id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public long getProduct_id()
    {
        return product_id;
    }
    public void setProduct_id(long product_id)
    {
        this.product_id = product_id;
    }
    public double getSms_cost()
    {
        return sms_cost;
    }
    public void setSms_cost(double sms_cost)
    {
        this.sms_cost = sms_cost;
    }
    public int getSms_included()
    {
        return sms_included;
    }
    public void setSms_included(int sms_included)
    {
        this.sms_included = sms_included;
    }
}
