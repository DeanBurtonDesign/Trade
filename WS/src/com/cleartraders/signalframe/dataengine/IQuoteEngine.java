package com.cleartraders.signalframe.dataengine;

import com.cleartraders.common.entity.HLOCDataBean;


public interface IQuoteEngine
{
    public boolean addIndicatorQuoteFeeder(String feederName, IQuoteFeeder quoteFeeder);
    public boolean removeIndicatorQuoteFeeder(String feederName);
    
    public boolean addStrategyQuoteFeeder(String feederName, IQuoteFeeder quoteFeeder);
    public boolean removeStrategyQuoteFeeder(String feederName);
    
    public void notifyQuote(boolean isNewQuote, HLOCDataBean oHLOCDataItem);
    public void notifyToIndicator(boolean isNewQuote, HLOCDataBean oHLOCDataItem);
    public void notifyToStrategy(boolean isNewQuote, HLOCDataBean oHLOCDataItem);
    
    public boolean sendMessage(String message);
    
    public boolean startEngine();
    public void stopEngine();
    
    public boolean getStatus();
    public void setStatus(boolean live);
}
