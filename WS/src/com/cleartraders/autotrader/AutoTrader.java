package com.cleartraders.autotrader;

import java.util.List;

import com.cleartraders.autotrader.gainwsapi.ServiceCallbackHandler;
import com.cleartraders.autotrader.gainwsapi.ServiceStub;
import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.Signal;
import com.cleartraders.common.entity.UserSignalPreferenceBaseBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.signalframe.conf.SFResManager;
import com.cleartraders.ws.WSConstants;
import com.cleartraders.ws.db.WSDBAccessor;
import com.cleartraders.ws.model.bean.AutoTradeAccountBean;

public class AutoTrader
{
    private static AutoTrader m_oInstance=new AutoTrader();
    
    private boolean _enablTrade = false;
    
    public AutoTrader()
    {
        init();
    }
    
    public synchronized static AutoTrader getInstance()
    {
        return m_oInstance;
    }
    
    private void init()
    {        
        try
        {
            int iFlag = WSDBAccessor.getInstance().getAutoTradeStatus();
            if(WSConstants.OPEN_AUTO_TRADE == iFlag)
            {
                _enablTrade = true;
            }
            else
            {
                _enablTrade = false;
            }            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public synchronized boolean tradeBySignal(Signal oSignal)
    {
        if(null == oSignal || false == _enablTrade)
        {
            return false;
        }
        
        if( CommonDefine.CLOSE_SIGNAL != oSignal.getSignal_type() && 
            CommonDefine.LONG_SIGNAL != oSignal.getSignal_type() && 
            CommonDefine.SHORT_SIGNAL != oSignal.getSignal_type() )
        {
            InfoTrace.getInstance().printInfo(DebugLevel.WARNING,"AutoTrader receive signal type is not Close, Long or Short");
            LogTools.getInstance().insertLog(DebugLevel.WARNING,"AutoTrader receive signal type is not Close, Long or Short");
            
            return false;
        }
            
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"AutoTrader receive signal, type="+oSignal.getSignal_type()+", strategy id="+oSignal.getStrategy_id()+", period id="+oSignal.getSignal_period()+", market name="+oSignal.getMarket_name()+", value="+oSignal.getSignalValue());
        LogTools.getInstance().insertLog(DebugLevel.INFO,"AutoTrader receive signal, type="+oSignal.getSignal_type()+", strategy id="+oSignal.getStrategy_id()+", period id="+oSignal.getSignal_period()+", market name="+oSignal.getMarket_name()+", value="+oSignal.getSignalValue());
                
        //check this signal is the My Signal subscription for market id and time period
        List<UserSignalPreferenceBaseBean> allSignalPreferenceOfAutoTrader =  WSDBAccessor.getInstance().getSignalPreferenceOfAutoTrader(WSConstants.AUTO_TRADER_USER_ID);
        if(allSignalPreferenceOfAutoTrader == null)
        {
            return false;
        }
        else
        {
            boolean subscribedThisSignal = false;
            
            for(int i=0; i<allSignalPreferenceOfAutoTrader.size(); i++)
            {
                UserSignalPreferenceBaseBean oPreferenceBean = allSignalPreferenceOfAutoTrader.get(i);   
                
                //if market is not active, then, ignore it
                if(!oPreferenceBean.getActive())
                {
                    InfoTrace.getInstance().printInfo(DebugLevel.INFO,"AutoTrader receive signal, market id,"+oPreferenceBean.getMarket_type_id()+" doesn't active!");
                    LogTools.getInstance().insertLog(DebugLevel.INFO,"AutoTrader receive signal, market id,"+oPreferenceBean.getMarket_type_id()+" doesn't active!");
                    continue;
                }
                
                if(oPreferenceBean.getMarket_type_id() == oSignal.getMarket_type_id() && 
                   oPreferenceBean.getMarket_period_id() == oSignal.getSignal_period() && 
                   oPreferenceBean.getStrategy_id() == oSignal.getStrategy_id())
                {
                    subscribedThisSignal = true;
                    break;
                }
            }
            
            if(!subscribedThisSignal)
            {
                InfoTrace.getInstance().printInfo(DebugLevel.INFO,"AutoTrader receive signal, but not be subscribed.");
                LogTools.getInstance().insertLog(DebugLevel.INFO,"AutoTrader receive signal, but not be subscribed.");
                
                return false;
            }
        }
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"AutoTrader prepare to trade for signal, type="+oSignal.getSignal_type()+", market name="+oSignal.getMarket_name()+", value="+oSignal.getSignalValue());
        LogTools.getInstance().insertLog(DebugLevel.INFO,"AutoTrader prepare to trade for signal, type="+oSignal.getSignal_type()+", market name="+oSignal.getMarket_name()+", value="+oSignal.getSignalValue());
        
        AutoTradeAccountBean oCurrentAutoTradeAccountInfo = WSDBAccessor.getInstance().getCurrentAutoTradeAccountInfoByMarket(oSignal);
                
        int currentPosition = 0;
        
        if(null != oCurrentAutoTradeAccountInfo)
        {
            // this market was already in DB            
            currentPosition = oCurrentAutoTradeAccountInfo.getPositon();
            
            InfoTrace.getInstance().printInfo(DebugLevel.INFO,"AutoTrader: Market was already in DB, current position is "+currentPosition);
            LogTools.getInstance().insertLog(DebugLevel.INFO,"AutoTrader: Market was already in DB, current position is "+currentPosition);
            
            //if there are some position traded already,
            //system should close postion is this is close signal OR this is opposite Signal
            if(oCurrentAutoTradeAccountInfo.getPositon() != 0 && 
               (CommonDefine.CLOSE_SIGNAL == oSignal.getSignal_type() || oCurrentAutoTradeAccountInfo.getPosition_type() != oSignal.getSignal_type()))
            {
                //close current positions
                long tradeSignalType = oSignal.getSignal_type();
                
                if(CommonDefine.CLOSE_SIGNAL == oSignal.getSignal_type())
                {
                    //this is Close signal, if Position is Long, sell them. If position is Short, buy them.
                    if( CommonDefine.LONG_SIGNAL == oCurrentAutoTradeAccountInfo.getPosition_type() )
                    {
                        tradeSignalType = CommonDefine.SHORT_SIGNAL;
                    }
                    else
                    {
                        tradeSignalType = CommonDefine.LONG_SIGNAL;
                    }
                }
                else if(oCurrentAutoTradeAccountInfo.getPosition_type() != oSignal.getSignal_type())
                {
                    //this is Long or Short signal, just trade it
                    tradeSignalType = oSignal.getSignal_type();
                }

                //trade this signal                
                if(tradeAndRecordSignal(oSignal,tradeSignalType,oCurrentAutoTradeAccountInfo.getPositon()))
                {
                    currentPosition = 0;
                    
                    //set potion = 0
                    WSDBAccessor.getInstance().updateAutoTraderAccountPosition(oSignal, 0);
                }
            }
            
            //above is to Close opposite position, now if signal is not Close signal, system should trade it as new position
            if(CommonDefine.CLOSE_SIGNAL != oSignal.getSignal_type())
            {
                //trade this signal
                if(tradeAndRecordSignal(oSignal,oSignal.getSignal_type(),SFResManager.getInstance().getAmount_for_each_signal()))
                {
                    //update record into DB for this market
                    WSDBAccessor.getInstance().updateAutoTraderAccountPosition(oSignal, currentPosition + SFResManager.getInstance().getAmount_for_each_signal());
                }
            }
        }
        else
        {
            //this market was NOT in DB yet  
            if(CommonDefine.CLOSE_SIGNAL != oSignal.getSignal_type())
            {
                InfoTrace.getInstance().printInfo(DebugLevel.INFO,"AutoTrader: Market was NOT in DB, current position is 0");
                LogTools.getInstance().insertLog(DebugLevel.INFO,"AutoTrader: Market was NOT in DB, current position is 0");

                //trade this signal
                if(tradeAndRecordSignal(oSignal,oSignal.getSignal_type(),SFResManager.getInstance().getAmount_for_each_signal()))
                {
                    //add new record into DB for this market
                    WSDBAccessor.getInstance().addAutoTraderAccountPosition(oSignal, SFResManager.getInstance().getAmount_for_each_signal());
                }
            }
        }
        
        return true;
    }
    
    private boolean tradeAndRecordSignal(Signal oSignal,long tradeSignalType, int amount)
    {          
        boolean tradeResult = tradeBySignal(oSignal,tradeSignalType,amount);
        
        if(tradeResult)
        {
            InfoTrace.getInstance().printInfo(DebugLevel.INFO,"AutoTrader completed trade, type="+oSignal.getSignal_type()+", market name="+oSignal.getMarket_name()+", amount="+amount);
            LogTools.getInstance().insertLog(DebugLevel.INFO,"AutoTrader completed trade, type="+oSignal.getSignal_type()+", market name="+oSignal.getMarket_name()+", amount="+amount);
        }
        else
        {
            InfoTrace.getInstance().printInfo(DebugLevel.WARNING,"AutoTrader didn't complete trade, type="+oSignal.getSignal_type()+", market name="+oSignal.getMarket_name()+", amount="+SFResManager.getInstance().getAmount_for_each_signal());
            LogTools.getInstance().insertLog(DebugLevel.WARNING,"AutoTrader didn't complete trade, type="+oSignal.getSignal_type()+", market name="+oSignal.getMarket_name()+", amount="+SFResManager.getInstance().getAmount_for_each_signal());
        }
        
        return tradeResult;
    }
    
    /**
     * 
     * @param oSignal          --Signal information
     * @param tradeSignalType  --trader type, if no previous signal or direction is same, then trade as signal type
     *                           Otherwise, system should close previous opposite positions, then trade new signal
     * @param amount           --trade amount
     * @return
     */
    private boolean tradeBySignal(Signal oSignal,long tradeSignalType, int amount)
    {
        try
        {
            InfoTrace.getInstance().printInfo(DebugLevel.INFO,"AutoTrader: prepare to send order to GainAPI");
            LogTools.getInstance().insertLog(DebugLevel.INFO,"AutoTrader: prepare to send order to GainAPI");
            
            ServiceStub stub = new ServiceStub();
            //System.out.println(System.currentTimeMillis());

            ServiceStub.DealRequestAtBest oPara = new ServiceStub.DealRequestAtBest();
            oPara.setAmount(String.valueOf(amount));
            if(CommonDefine.LONG_SIGNAL == tradeSignalType)
            {
                oPara.setBuySell("B");
            }
            else if(CommonDefine.SHORT_SIGNAL == tradeSignalType)
            {
                oPara.setBuySell("S");
            }
            else if(CommonDefine.CLOSE_SIGNAL == tradeSignalType)
            {
                //since Close signal doesn't add more signals
                return true;
            }
            else
            {
                return false;
            }
            
            oPara.setPair(oSignal.getMarket_name());
            oPara.setUserID(SFResManager.getInstance().getAuto_trader_api_user_id());
            oPara.setPWD(SFResManager.getInstance().getAuto_trader_api_pwd());
            
            ServiceCallbackHandler oHandler = new DealRequestAtBestCallbackHandler();
            
            stub.startDealRequestAtBest(oPara, oHandler);

            //System.out.println("Completed sending request: "+System.currentTimeMillis());
            InfoTrace.getInstance().printInfo(DebugLevel.INFO,"AutoTrader: complete sending order to GainAPI");
            LogTools.getInstance().insertLog(DebugLevel.INFO,"AutoTrader: complete sending order to GainAPI");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,
            "Exception happened in AutoTrader.tradeBySignal()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
            
            return false;
        }
        
        return true;
    }
        
    public boolean closeAllPosition()
    {
        return WSDBAccessor.getInstance().clearAutoTradeAccountInfo(WSConstants.AUTO_TRADER_USER_ID);
    }
    
    public boolean enablTrade()
    {
        return _enablTrade;
    }

    public boolean setEnablTrade(boolean trade)
    {
        if(trade)
        {
            if(WSDBAccessor.getInstance().updateAutoTradeStatus(WSConstants.OPEN_AUTO_TRADE))
            {
                _enablTrade = trade;
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            if(WSDBAccessor.getInstance().updateAutoTradeStatus(WSConstants.CLOSE_AUTO_TRADE))
            {
                _enablTrade = trade;
                return true;
            }
            else
            {
                return false;
            }
        }
    }
    
    public static void main(String args[])
    {
        //System.out.println("java.class.path:"+System.getProperties().getProperty("java.class.path"));
        
        Signal oSignal = new Signal();
        oSignal.setMarket_name("EUR/USD");
        oSignal.setSignal_period(2);
        oSignal.setSignal_type(CommonDefine.LONG_SIGNAL);        
        AutoTrader.getInstance().tradeBySignal(oSignal);
        
        Signal oSignal2 = new Signal();
        oSignal2.setMarket_name("EUR/USD");
        oSignal2.setSignal_period(1);
        oSignal2.setSignal_type(CommonDefine.SHORT_SIGNAL);        
        AutoTrader.getInstance().tradeBySignal(oSignal);
    }
}
