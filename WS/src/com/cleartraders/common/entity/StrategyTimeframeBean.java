package com.cleartraders.common.entity;

public class StrategyTimeframeBean
{
    private long strategy_id=0;
    private long period_id=0;
    
    public long getPeriod_id()
    {
        return period_id;
    }
    public void setPeriod_id(long period_id)
    {
        this.period_id = period_id;
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
