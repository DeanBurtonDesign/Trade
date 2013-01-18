package com.cleartraders.webapp.model.bean;

public class TradeBean
{
    private long points = 0;
    private int trade_direction = 0;
    private long market_type_id=0;
    private String market_type_name="";
    private long strategy_id=0;
    private String strategy_name="";
    private long time_frame_id=0;
    private String time_frame_name="";
    
    public String toTextMessage()
    {
        String textMessage = "";
        
        textMessage += this.points;
        textMessage += ",";
        
        textMessage += this.trade_direction;
        textMessage += ",";
        
        textMessage += this.market_type_name;
        textMessage += ",";
        
        textMessage += this.strategy_name;
        textMessage += ",";
        
        textMessage += this.time_frame_name;
        
        return textMessage;
    }
    
    public long getMarket_type_id()
    {
        return market_type_id;
    }
    public void setMarket_type_id(long market_type_id)
    {
        this.market_type_id = market_type_id;
    }
    public String getMarket_type_name()
    {
        return market_type_name;
    }
    public void setMarket_type_name(String market_type_name)
    {
        this.market_type_name = market_type_name;
    }
    public long getPoints()
    {
        return points;
    }
    public void setPoints(long points)
    {
        this.points = points;
    }
    public long getStrategy_id()
    {
        return strategy_id;
    }
    public void setStrategy_id(long strategy_id)
    {
        this.strategy_id = strategy_id;
    }
    public String getStrategy_name()
    {
        return strategy_name;
    }
    public void setStrategy_name(String strategy_name)
    {
        this.strategy_name = strategy_name;
    }
    public long getTime_frame_id()
    {
        return time_frame_id;
    }
    public void setTime_frame_id(long time_frame_id)
    {
        this.time_frame_id = time_frame_id;
    }
    public String getTime_frame_name()
    {
        return time_frame_name;
    }
    public void setTime_frame_name(String time_frame_name)
    {
        this.time_frame_name = time_frame_name;
    }
    public int getTrade_direction()
    {
        return trade_direction;
    }
    public void setTrade_direction(int trade_direction)
    {
        this.trade_direction = trade_direction;
    }
    
    
    
}
