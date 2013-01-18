package com.cleartraders.webapp.model.bean;

/**
 * 
 
create table payment_history_table
(
    id              int unsigned not null primary key,
    user_id             int unsigned not null,
    pay_item_name   varchar(100),
    pay_amount      double not null,  #USD
    date                    double not null,  #transfer end
    txn_id          varchar(100),  #txn_id is unique transaction ID
    completed       int unsigned not null # 0: not completed  1: completed
);


 * @author Administrator
 *
 */

public class PaymentHistoryBean
{
    private long id;
    private long user_id;
    private String pay_item_name="";
    private double pay_amount;
    private long date;
    private String txn_id;
    private int completed;
    
    public String getTxn_id()
    {
        return txn_id;
    }
    public void setTxn_id(String txn_id)
    {
        this.txn_id = txn_id;
    }
    public long getId()
    {
        return id;
    }
    public void setId(long id)
    {
        this.id = id;
    }
    public long getDate()
    {
        return date;
    }
    public void setDate(long date)
    {
        this.date = date;
    }
    public int getCompleted()
    {
        return completed;
    }
    public void setCompleted(int completed)
    {
        this.completed = completed;
    }
    public double getPay_amount()
    {
        return pay_amount;
    }
    public void setPay_amount(double pay_amount)
    {
        this.pay_amount = pay_amount;
    }
    public String getPay_item_name()
    {
        return pay_item_name;
    }
    public void setPay_item_name(String pay_item_name)
    {
        this.pay_item_name = pay_item_name;
    }
    public long getUser_id()
    {
        return user_id;
    }
    public void setUser_id(long user_id)
    {
        this.user_id = user_id;
    }
    
    
}
