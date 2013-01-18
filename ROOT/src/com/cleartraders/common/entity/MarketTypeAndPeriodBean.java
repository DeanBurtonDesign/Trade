package com.cleartraders.common.entity;

public class MarketTypeAndPeriodBean
{
    private long marketID;
    private long marketPeriodID;
    private long strategyID;
    private boolean active=false;
    
    public boolean isActive()
    {
        return active;
    }
    public void setActive(boolean active)
    {
        this.active = active;
    }
    public long getStrategyID()
    {
        return strategyID;
    }
    public void setStrategyID(long strategyID)
    {
        this.strategyID = strategyID;
    }
    public long getMarketID()
    {
        return marketID;
    }
    public void setMarketID(long marketID)
    {
        this.marketID = marketID;
    }
    public long getMarketPeriodID()
    {
        return marketPeriodID;
    }
    public void setMarketPeriodID(long marketPeriodID)
    {
        this.marketPeriodID = marketPeriodID;
    }
    
    
}
