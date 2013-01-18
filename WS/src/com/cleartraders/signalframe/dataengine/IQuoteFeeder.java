package com.cleartraders.signalframe.dataengine;

import com.cleartraders.common.entity.HLOCDataBean;

public interface IQuoteFeeder
{
    public void onQuoteUpdate(boolean isNewQuote, String quoteEngineID, HLOCDataBean quote);
}
