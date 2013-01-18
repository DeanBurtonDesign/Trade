package com.cleartraders.webapp.model.activesignals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.entity.MarketPeriodBean;
import com.cleartraders.common.entity.Signal;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.entity.UserSignalPreferenceBaseBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.config.WebappResManager;
import com.cleartraders.webapp.db.DBAccessor;
import com.cleartraders.webapp.model.myaccount.AccountController;

public class ActiveSignalsList
{
    public List<Signal> getPastedSignals(UserBean oUserBean, long userTimeOffset)
    {
        List<Signal> oFinalPastedSignals = new ArrayList<Signal>();
        
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return oFinalPastedSignals;
        }
        
        
        int totalSignalNum = Integer.parseInt(WebappResManager.getInstance().getNumber_of_pasted_signals());
        
        //since this number is the all markets, so, we should mutilply 30 to assure all pasted signal are fetched
        List<Signal> oPastedSignals = DBAccessor.getInstance().getPastedSignals(oUserBean, totalSignalNum*30);
        
        List<UserSignalPreferenceBaseBean> oActiveSignalsPreference = (new AccountController()).getActiveUserSignalPreferenceBean(oUserBean);
        
        for(int i=0; i<oPastedSignals.size(); i++)
        {
            Signal oSignalBean = oPastedSignals.get(i);
            
            if(!isActiveMarket(oActiveSignalsPreference,oSignalBean))
            {
                continue;
            }
            
            if(oFinalPastedSignals.size() >= totalSignalNum)
            {
                break;
            }
            
            SimpleDateFormat oDateFormatter = new SimpleDateFormat("HH:mm");
            oSignalBean.setGenerate_date_string(oDateFormatter.format(new Date(oSignalBean.getGenerate_date()+userTimeOffset)));
            
            MarketPeriodBean oMarketBean = DataCache.getInstance().getMarketPeriodByID((long)oSignalBean.getSignal_period());
            String signalPeriodName = oMarketBean.getValue()+" "+oMarketBean.getPeriod_name();
            oSignalBean.setSignal_period_name(signalPeriodName);
                       
            oFinalPastedSignals.add(oSignalBean);
        }
        
        return oFinalPastedSignals;
    }
    
    public String getLastestTimeframeSignals(long strategyID, long marketID, long userTimeOffset)
    {
        SimpleDateFormat oDateFormatter = new SimpleDateFormat("HH:mm");
        
        String result = "";
        List<MarketPeriodBean> allTimeFrames = DataCache.getInstance().getAllMarketPeriod();
        for(int i=0; i<allTimeFrames.size(); i++)
        {
            MarketPeriodBean oMarketPeriod = allTimeFrames.get(i);
            
            Signal oSignal = DBAccessor.getInstance().getLatestSignal(marketID, strategyID, oMarketPeriod.getId());
            if(null != oSignal && oSignal.getId() > 0)
            {                
                long signalAjustedTime = oSignal.getGenerate_date()+userTimeOffset;
                oSignal.setGenerate_date_string(oDateFormatter.format(new Date(signalAjustedTime)));
                
                long signalExpireTime = oSignal.getExpire_date();
                if(signalExpireTime > System.currentTimeMillis())
                {
                    oSignal.setExpire_date((long)((signalExpireTime-System.currentTimeMillis())/1000));
                    String expire_date = String.valueOf(oSignal.getExpire_date()) + " sec";
                    oSignal.setExpire_date_string(expire_date);
                }
                else
                {
                    oSignal.setExpire_date(0);
                    oSignal.setExpire_date_string("0 sec");
                }
                
                result+=oSignal.toCompletedTextMessage();
            }
            
            if( i < allTimeFrames.size() -1 )
            {
                result += ";";
            }
        }
        
        return result;
    }
    
    public int getTotalNumberOfActiveSingalByUSer(UserBean oUser)
    {
        if(null == oUser)
        {
            return 0;
        }
        
        try
        {
            return DBAccessor.getInstance().getTotalNumberOfActiveSingalByUSer(oUser);
        }
        catch(Exception e)
        {
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        
        return 0;
    }
    
    private boolean isActiveMarket(List<UserSignalPreferenceBaseBean> allActiveMarkets, Signal oSignal)
    {
        if(null == allActiveMarkets || allActiveMarkets.size() == 0)
        {
            return false;
        }
        
        for(int i=0; i<allActiveMarkets.size(); i++)
        {
            UserSignalPreferenceBaseBean oTempMarketTypeBean = allActiveMarkets.get(i);
            
            if(oTempMarketTypeBean.getMarket_type_id() == oSignal.getMarket_type_id())
            {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean isActiveSignal(List<UserSignalPreferenceBaseBean> allActiveMarkets, Signal oSignal)
    {
        if(null == allActiveMarkets || allActiveMarkets.size() == 0)
        {
            return false;
        }
        
        for(int i=0; i<allActiveMarkets.size(); i++)
        {
            UserSignalPreferenceBaseBean oTempMarketTypeBean = allActiveMarkets.get(i);
            
            if(   oTempMarketTypeBean.getMarket_type_id() == oSignal.getMarket_type_id() 
               && oTempMarketTypeBean.getMarket_period_id() == oSignal.getSignal_period()
               && oTempMarketTypeBean.getStrategy_id() == oSignal.getStrategy_id())
            {
                return true;
            }
        }
        
        return false;
    }
        
    public List<Signal> getSpecificPageOfActiveSignals(UserBean oUser,int start, int range)
    {
        List<Signal> oFinalAllActiveSignals = new ArrayList<Signal>();
        
        if(null == oUser || oUser.getId() < 0)
        {
            return oFinalAllActiveSignals;
        }
        
        try
        {            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Prepare to getActiveSignalsByUser at "+System.currentTimeMillis());
            
            List<Signal> oAllSignals = DBAccessor.getInstance().getActiveSignalsByUser(oUser,start,range);   
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Finish to getActiveSignalsByUser at "+System.currentTimeMillis()+". Size="+oAllSignals.size());
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Prepare to getActiveUserSignalPreferenceBean at "+System.currentTimeMillis());
            
            List<UserSignalPreferenceBaseBean> oActiveSignalsPreference = (new AccountController()).getActiveUserSignalPreferenceBean(oUser);
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Finish to getActiveUserSignalPreferenceBean at "+System.currentTimeMillis()+". Size="+oActiveSignalsPreference.size());
            
            //calculate signal expire minutes
            for(int i=0; i<oAllSignals.size(); i++)
            {
                Signal oSignal = oAllSignals.get(i);
                
                if(!isActiveSignal(oActiveSignalsPreference,oSignal))
                {
                    continue;
                }
                
                //the signal period is period ID, here should convert into Minutes                
                MarketPeriodBean oMarketPeriodBean = DataCache.getInstance().getMarketPeriodByID((long)oSignal.getSignal_period());
                oSignal.setSignal_period_minutes(oMarketPeriodBean.getPeriodMinutes());
                
                long signalExpireTime = oSignal.getExpire_date();
                double currentTime = System.currentTimeMillis();
                
                oSignal.setExpire_date((long)((signalExpireTime-currentTime)/1000));
                String expire_date = String.valueOf(oSignal.getExpire_date()) + " sec";
                oSignal.setExpire_date_string(expire_date);      
            
                if(oSignal.getProfit() > 0)
                {
                    oSignal.setProfitString("+"+oSignal.getProfit());
                }
                else if(oSignal.getProfit() < 0)
                {
                    oSignal.setProfitString("-"+oSignal.getProfit()*-1);
                }
                else
                {
                    oSignal.setProfitString("0");
                }
                
                MarketPeriodBean oMarketBean = DataCache.getInstance().getMarketPeriodByID((long)oSignal.getSignal_period());
                String signalPeriodName = oMarketBean.getValue()+" "+oMarketBean.getPeriod_name();
                oSignal.setSignal_period_name(signalPeriodName);
                
                oFinalAllActiveSignals.add(oSignal);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        
        return oFinalAllActiveSignals;
    }
}
