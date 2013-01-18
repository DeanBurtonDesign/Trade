package com.cleartraders.common.entity;

/**
 * Signal extend Class
 * 
 * @author Administrator
 *
 */
public class StrategySignal
{
    private Signal includedSignal = null;
    private double relatedHighestPrice = 0.0;
    private double relatedLowestPrice = 0.0;
    
    public StrategySignal()
    {
        this.includedSignal = new Signal();
    }
    
    public StrategySignal(Signal includedSignal)
    {
        this.includedSignal = new Signal(includedSignal);
    }
    
    public Signal getIncludedSignal()
    {
        return includedSignal;
    }
    public void setIncludedSignal(Signal includedSignal)
    {
        this.includedSignal = includedSignal;
    }
    public double getRelatedHighestPrice()
    {
        return relatedHighestPrice;
    }
    public void setRelatedHighestPrice(double relatedHighestPrice)
    {
        this.relatedHighestPrice = relatedHighestPrice;
    }
    public double getRelatedLowestPrice()
    {
        return relatedLowestPrice;
    }
    public void setRelatedLowestPrice(double relatedLowestPrice)
    {
        this.relatedLowestPrice = relatedLowestPrice;
    }
}
