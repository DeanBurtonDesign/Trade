package com.cleartraders.autotrader;
import com.cleartraders.autotrader.gainwsapi.ServiceCallbackHandler;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;


public class DealRequestAtBestCallbackHandler extends ServiceCallbackHandler
{
    public void receiveResultDealRequestAtBest(com.cleartraders.autotrader.gainwsapi.ServiceStub.DealRequestAtBestResponse result) 
    {
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Result is: "+result.getDealRequestAtBestResult().getConfirmation());
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Result is: "+result.getDealRequestAtBestResult().getConfirmation());
    }
}
