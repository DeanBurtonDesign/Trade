package com.cleartraders.webapp.model.bean;

/*
 create table user_analysis_market_table
(
      id                    int unsigned not null primary key,
      user_id               int unsigned not null,
      market_type_id        int unsigned not null, 
      strategy_id               int unsigned not null,
      market_period_id      int unsigned not null,
      analysis_from         double not null,
      analysis_to           double not null,
      active                int default 2           # 1: enable 2: disable
);
 */
public class UserAnalysisMarketBean
{
    private long id=0;
    private long user_id=0;
    private long market_type_id=0;
    private long strategy_id=0;
    private String marketName="";
    private long market_period_id=0;
    private String timePeriodName="";
    private long periodValue=0;
    private long analysis_from=0;
    private long analysis_to=0;
    private boolean active=false;
    
    public long getPeriodValue()
    {
        return periodValue;
    }
    public void setPeriodValue(long periodValue)
    {
        this.periodValue = periodValue;
    }
    public String getMarketName()
    {
        return marketName;
    }
    public void setMarketName(String marketName)
    {
        this.marketName = marketName;
    }
    public String getTimePeriodName()
    {
        return timePeriodName;
    }
    public void setTimePeriodName(String timePeriodName)
    {
        this.timePeriodName = timePeriodName;
    }
    public boolean isActive()
    {
        return active;
    }
    public void setActive(boolean active)
    {
        this.active = active;
    }
    public long getAnalysis_from()
    {
        return analysis_from;
    }
    public void setAnalysis_from(long analysis_from)
    {
        this.analysis_from = analysis_from;
    }
    public long getAnalysis_to()
    {
        return analysis_to;
    }
    public void setAnalysis_to(long analysis_to)
    {
        this.analysis_to = analysis_to;
    }
    public long getId()
    {
        return id;
    }
    public void setId(long id)
    {
        this.id = id;
    }
    public long getMarket_period_id()
    {
        return market_period_id;
    }
    public void setMarket_period_id(long market_period_id)
    {
        this.market_period_id = market_period_id;
    }
    public long getMarket_type_id()
    {
        return market_type_id;
    }
    public void setMarket_type_id(long market_type_id)
    {
        this.market_type_id = market_type_id;
    }
    public long getUser_id()
    {
        return user_id;
    }
    public void setUser_id(long user_id)
    {
        this.user_id = user_id;
    }
    public long getStrategy_id()
    {
        return strategy_id;
    }
    public void setStrategy_id(long strategy_id)
    {
        this.strategy_id = strategy_id;
    }
    
    
}
