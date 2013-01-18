package com.cleartraders.ws;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.IndicatorBean;
import com.cleartraders.common.entity.IndicatorsSimpleValue;
import com.cleartraders.common.entity.Signal;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.feeder.iqfeed.IQDataFeedHistoryDataHandler;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.signalframe.strategy.StrategyManager;
import com.cleartraders.ws.db.WSDBAccessor;


public class DataServiceImpl implements IDataService
{    
    private final String _signal_sender_name="cleartraders_admin_clients";
    private final String _signal_sender_pwd="0BAED9D51560BC78E7698B52D0E8DF2C03E9566E";
    
    //get indicator value by id
    public ArrayList<IndicatorsSimpleValue> getIndicatorValue(String userName, String password,int market_type_id, int signal_period,int strategy_id)
    {
        //check user rights
        if(!passCheck(userName, password))
        {
            return null;
        }
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Call  webservice function, getIndicatorValue. Request from user:"+userName+", at " + System.currentTimeMillis());
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Call  webservice function, getIndicatorValue. Request from user:"+userName+", at " + System.currentTimeMillis());
        
        ArrayList<IndicatorsSimpleValue> oAllRelatedIndicatorsValue = new ArrayList<IndicatorsSimpleValue>();
        
        ArrayList<IndicatorBean> oRelatedIndicators = WSDBAccessor.getInstance().getIndicatorsByStrategyID(strategy_id);
        for(int i=0; i<oRelatedIndicators.size(); i++)
        {
            IndicatorBean oIndicatorBean = oRelatedIndicators.get(i);
            
            ArrayList<IndicatorsSimpleValue> oIndicatorValue = StrategyManager.getInstance().getIndicatorValue(market_type_id,signal_period,strategy_id,oIndicatorBean);
            
            oAllRelatedIndicatorsValue.addAll(oIndicatorValue);
        }
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Compeleted Call webservice function, getIndicatorValue, at " + System.currentTimeMillis());
        
        return oAllRelatedIndicatorsValue;
    }
    
    public ArrayList<Signal> getSignals(String userName, String password, int signalType,long market_type_id, int signal_period, int strategy_id, int signalsCount)
    {
        //check user rights
        if(!passCheck(userName, password))
        {
            return null;
        }
        
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Call web service function, getSignals at "+dateTimeFormat.format(System.currentTimeMillis())+"Request from user:"+userName);
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Call web service function, getSignals at "+dateTimeFormat.format(System.currentTimeMillis())+"Request from user:"+userName);
        
        return WSDBAccessor.getInstance().getSpecificSignals(signalType, market_type_id, signal_period, strategy_id, signalsCount);
    }

    public boolean addAndNotifySignals(String userName, String password, Signal oBasicSignal)
    {        
        //check user rights and verify whehte        
        if(!passCheck(userName, password) && !isFromRightSignalSender(userName))
        {
            return false;
        }
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"...............................................................");
        LogTools.getInstance().insertLog(DebugLevel.INFO,"...............................................................");

        if (null == oBasicSignal)
        {
            InfoTrace.getInstance().printInfo(DebugLevel.WARNING,"Call web service function, addAndNotifySignals! But oSignal is NULL");
            LogTools.getInstance().insertLog(DebugLevel.WARNING,"Call web service function, addAndNotifySignals! But oSignal is NULL");
            
            return false;
        }
        
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        //remove Hour, Minute and Second from Daily date String
        if( oBasicSignal.getSignal_period() == CommonDefine.ONE_DAY || 
            oBasicSignal.getSignal_period() == CommonDefine.ONE_WEEK || 
            oBasicSignal.getSignal_period() == CommonDefine.ONE_MONTH || 
            oBasicSignal.getSignal_period() == CommonDefine.ONE_YEAR )
        {
            dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateStringBeforeUpdate = dateTimeFormat.format(new Date(oBasicSignal.getGenerate_date()));
            
            long updateDate = oBasicSignal.getGenerate_date();
            
            try
            {
                updateDate = dateTimeFormat.parse(dateStringBeforeUpdate).getTime();
            }
            catch(ParseException e)
            {
                e.printStackTrace();
                
                LogTools.getInstance().insertLog(DebugLevel.ERROR,
                        "Exception happened in DataServiceImpl.addAndNotifySignals()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
            }
            
            oBasicSignal.setGenerate_date(updateDate);
        }
        

        //update signal expire time
        //current signal time only contains Time Out Offset
        oBasicSignal.setExpire_date(System.currentTimeMillis()+oBasicSignal.getExpire_date());
        
        //for test
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Initial Expired Date:"+oBasicSignal.getExpire_date());
        //end for test
        
        String signalDate = dateTimeFormat.format(new Date((long)oBasicSignal.getGenerate_date()));
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Call web service function, addAndNotifySignals! Signal_Type ="+oBasicSignal.getSignal_type()+" Signal Date ="+signalDate+"Request from user:"+userName);
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Call web service function, addAndNotifySignals! Signal_Type ="+oBasicSignal.getSignal_type()+" Signal Date ="+signalDate+"Request from user:"+userName);
                             
        //store basic signals
        boolean result = false;
        
        //Close signal don't need be stored, since it will be store in the strategy handler
        if(CommonDefine.CLOSE_SIGNAL == oBasicSignal.getSignal_type())
        {
            result = true;
        }
        else
        {
            result = WSDBAccessor.getInstance().storeSignalIntoDB(oBasicSignal);
        }
        
        // Note: three notify methods are independent
        // That means if one of them failed, others should still notify
        // Note: only notify BUY and Sell signal. Scalper doesn't need notify
        if (result && ( CommonDefine.BUY_SINGAL == oBasicSignal.getSignal_type() || CommonDefine.SELL_SINGAL == oBasicSignal.getSignal_type() || CommonDefine.CLOSE_SIGNAL == oBasicSignal.getSignal_type() ))
        {            
            InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Preparse to handle signal by strategy! Signal_date="+oBasicSignal.getGenerate_date()+" Date String="+signalDate);
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Preparse to handle signal by strategy! Signal_date="+oBasicSignal.getGenerate_date()+" Date String="+signalDate);
                        
            StrategyManager.getInstance().handleSignalByRelatedStrategy(oBasicSignal);
        }

        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"...............................................................");
        LogTools.getInstance().insertLog(DebugLevel.INFO,"...............................................................");
        
        return result;
    }
    
    public ArrayList<String> getMinuteFinanceData(String userName, String password,String dataType, String symbol, long maxDatapoints, int intervalMin)
    {
        //check user rights
        if(!passCheck(userName, password))
        {
            return null;
        }
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Call  webservice function, getMinuteFinanceData. Request from user:"+userName);
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Call  webservice function, getMinuteFinanceData. Request from user:"+userName);

        ArrayList<String> oResult = new ArrayList<String>();
        try
        {
            // call IQFeed handle and get back data
            oResult = IQDataFeedHistoryDataHandler.getInstance().getMinuteFinanceData(symbol, intervalMin, maxDatapoints);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,
                    "Exception happened in DataServiceImpl.getMinuteFinanceData()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
        }

        return oResult;
    }

    public ArrayList<String> getDayFinanceData(String userName, String password, String dataType, String symbol, int days)
    {
        //check user rights
        if(!passCheck(userName, password))
        {
            return null;
        }
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Call  webservice function, getDayFinanceData. Request from user:"+userName);
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Call  webservice function, getDayFinanceData. Request from user:"+userName);

        ArrayList<String> oResult = new ArrayList<String>();
        try
        {
            // call IQFeed handle and get back data
            oResult = IQDataFeedHistoryDataHandler.getInstance().getDayFinanceData(symbol, String.valueOf(days));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,
                    "Exception happened in DataServiceImpl.getDayFinanceData()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
        }

        return oResult;
    }

    public ArrayList<String> getWeekFinanceData(String userName, String password, String dataType, String symbol, int weeks)
    {
        //check user rights
        if(!passCheck(userName, password))
        {
            return null;
        }
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Call  webservice function, getWeekFinanceData. Request from user:"+userName);
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Call  webservice function, getWeekFinanceData. Request from user:"+userName);

        ArrayList<String> oResult = new ArrayList<String>();
        try
        {
            // call IQFeed handle and get back data
            oResult = IQDataFeedHistoryDataHandler.getInstance().getWeekFinanceData(symbol, String.valueOf(weeks));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,
                    "Exception happened in DataServiceImpl.getWeekFinanceData()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
        }

        return oResult;
    }

    public ArrayList<String> getMonthFinanceData(String userName, String password, String dataType, String symbol, int months)
    {
        //check user rights
        if(!passCheck(userName, password))
        {
            return null;
        }
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Call  webservice function, getMonthFinanceData. Request from user:"+userName);
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Call  webservice function, getMonthFinanceData. Request from user:"+userName);

        ArrayList<String> oResult = new ArrayList<String>();
        try
        {
            // call IQFeed handle and get back data
            oResult = IQDataFeedHistoryDataHandler.getInstance().getMonthFinanceData(symbol, String.valueOf(months));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,
                    "Exception happened in DataServiceImpl.getMonthFinanceData()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
        }

        return oResult;
    }
        
    private boolean isFromRightSignalSender(String userName)
    {
        return _signal_sender_name.equals(userName);
    }
    
    private boolean passCheck(String userName, String password)
    {
        if(null == userName || null == password || "".equals(userName) || "".equals(password))
        {
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,"Call  webservice function, passCheck. Request parameter is not valid.");
            LogTools.getInstance().insertLog(DebugLevel.ERROR,"Call  webservice function, passCheck. Request parameter is not valid.");
            
            return false;
        }
        
        //for admin clients
        if(_signal_sender_name.equals(userName) && _signal_sender_pwd.equals(password))
        {
            return true;
        }
        
        //check user and password
        UserBean oUser = WSDBAccessor.getInstance().getUserInfoByLoginName(userName);
        if(null == oUser || !password.equals(oUser.getPwd()) || oUser.getExpired_date() < System.currentTimeMillis() || oUser.getEnable() != CommonDefine.USER_ENABLED)
        {
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,"Call  webservice function, passCheck. Request is invalid from user:"+userName);
            LogTools.getInstance().insertLog(DebugLevel.ERROR,"Call  webservice function, passCheck. Request is invalid from user:"+userName);
                        
            //invalid user request WS
            return false;
        }
        
        return true;
    }
        

    public static void main(String args[])
    {
        DataServiceImpl me = new DataServiceImpl();

        ArrayList<String> testResult = new ArrayList<String>();
        testResult = me.getMonthFinanceData("","","", "TEURUSD", 10);

        for (int i = 0; i < testResult.size(); i++)
        {
            InfoTrace.getInstance().printInfo(DebugLevel.INFO,testResult.get(i));
        }
    }
}
