package com.cleartraders.ws.model.bean;

/**
 *  create table auto_trade_account_table
    (
      user_id               int unsigned not null primary key,
      market_type_id    int unsigned not null,
      position_type     int unsigned not null, #0:BUY 1:SELL
      positon                       int unsigned not null
    );
 *
 */
public class AutoTradeAccountBean
{
    private long user_id=0;
    private long market_type_id=0;
    private int position_type=-1;
    private int positon = 0;
    
    public long getMarket_type_id()
    {
        return market_type_id;
    }
    public void setMarket_type_id(long market_type_id)
    {
        this.market_type_id = market_type_id;
    }
    public int getPosition_type()
    {
        return position_type;
    }
    public void setPosition_type(int position_type)
    {
        this.position_type = position_type;
    }
    public int getPositon()
    {
        return positon;
    }
    public void setPositon(int positon)
    {
        this.positon = positon;
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
