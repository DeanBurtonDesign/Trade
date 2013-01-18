package com.cleartraders.signalframe.strategy;

import com.cleartraders.common.entity.Signal;

public interface IStrategy
{
    public boolean isActive();
    public void active(boolean status);
    
    public long getStrategyID();
    public void setStrategyID(long strategyID);
    
    public boolean handleSignal(Signal oSignal);
}
