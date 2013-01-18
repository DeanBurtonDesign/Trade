package com.cleartraders.webapp.model.bean;

/**
 * 
 * @author Administrator
 *
 * JavaBean for user_broker_fee_table
    create table user_broker_fee_table
    (
        id                            int unsigned not null primary key,
        user_id               int unsigned not null,
        broker_fee            double not null       # Points
    );
 */
public class UserBrokerInfo
{
    private long id=0;
    private long user_id=0;
    private double broker_fee=0.0;
    
    public double getBroker_fee()
    {
        return broker_fee;
    }
    public void setBroker_fee(double broker_fee)
    {
        this.broker_fee = broker_fee;
    }
    public long getId()
    {
        return id;
    }
    public void setId(long id)
    {
        this.id = id;
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
