package com.cleartraders.ws.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cleartraders.common.db.CommonDBAccessor;
import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.IndicatorBean;
import com.cleartraders.common.entity.Signal;
import com.cleartraders.common.entity.UserSignalPreferenceBaseBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.ws.WSConstants;
import com.cleartraders.ws.model.bean.AutoTradeAccountBean;

public class WSDBAccessor extends CommonDBAccessor
{
    private static WSDBAccessor m_oInstance = new WSDBAccessor();

    public WSDBAccessor()
    {
    }

    public synchronized static WSDBAccessor getInstance()
    {
        return m_oInstance;
    }

    public boolean addAutoTraderAccountPosition(Signal oSignal, int position)
    {
        if (oSignal == null)
        {
            return false;
        }

        boolean result = false;
        Connection conn = null;
        Statement stat = null;

        try
        {
            conn = getConnection();
            stat = conn.createStatement();

            String querySql = "insert into auto_trade_account_table values("
                    + WSConstants.AUTO_TRADER_USER_ID + ","
                    + oSignal.getMarket_type_id() + ","
                    + oSignal.getSignal_type() + "," + position + ")";
            stat.execute(querySql);

            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();

            LogTools.getInstance().insertLog(DebugLevel.ERROR,
            "Exception happened in WSDBAccessor.addAutoTraderAccountPosition()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            try
            {
                if (null != stat)
                {
                    stat.close();
                }

                if (null != conn)
                {
                    conn.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();

                LogTools.getInstance().insertLog(DebugLevel.ERROR,
                "Exception happened in WSDBAccessor.addAutoTraderAccountPosition()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
            }
        }

        return result;
    }

    public boolean updateAutoTraderAccountPosition(Signal oSignal, int position)
    {
        if (oSignal == null)
        {
            return false;
        }

        boolean result = false;
        Connection conn = null;
        Statement stat = null;

        try
        {
            conn = getConnection();
            stat = conn.createStatement();

            String querySql = "update auto_trade_account_table set positon="
                    + position + ", position_type=" + oSignal.getSignal_type()
                    + " where market_type_id=" + oSignal.getMarket_type_id();
            stat.execute(querySql);

            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();

            LogTools.getInstance().insertLog(DebugLevel.ERROR,
            "Exception happened in WSDBAccessor.updateAutoTraderAccountPosition()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            try
            {
                if (null != stat)
                {
                    stat.close();
                }

                if (null != conn)
                {
                    conn.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();

                LogTools.getInstance().insertLog(DebugLevel.ERROR,
                "Exception happened in WSDBAccessor.updateAutoTraderAccountPosition()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
            }
        }

        return result;
    }
    
    public ArrayList<IndicatorBean> getIndicatorsByStrategyID(long strategyID)
    {
        ArrayList<IndicatorBean> oResult = new ArrayList<IndicatorBean>();
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;

        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select A.id,A.name,A.period,A.color,A.description,B.strategy_id,B.visable from indicator_table A, strategy_indicator_table B where B.strategy_id = "+ strategyID+" and A.id=B.indicator_id");
            
            while (result.next())
            {
                IndicatorBean oIndicator = new IndicatorBean();
                
                oIndicator.setId(result.getLong("id"));
                oIndicator.setStrategy_id(result.getLong("strategy_id"));
                oIndicator.setName(result.getString("name"));
                oIndicator.setPeriod(result.getInt("period"));
                oIndicator.setColor(result.getString("color"));
                oIndicator.setDescription(result.getString("description"));
                oIndicator.setVisable(result.getInt("visable"));

                oResult.add(oIndicator);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,
            "Exception happened in WSDBAccessor.getIndicatorsByStrategyID()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            try
            {
                if (null != result)
                {
                    result.close();
                }

                if (null != stat)
                {
                    stat.close();
                }

                if (null != conn)
                {
                    conn.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                
                LogTools.getInstance().insertLog(DebugLevel.ERROR,
                        "Exception happened in WSDBAccessor.getIndicatorsByStrategyID()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
            }
        }

        return oResult;
    }

    public List<UserSignalPreferenceBaseBean> getSignalPreferenceOfAutoTrader(long userID)
    {
        List<UserSignalPreferenceBaseBean> oResult = new ArrayList<UserSignalPreferenceBaseBean>();

        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;

        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select * from user_signal_preference_table where user_id = "+ userID);
            
            while (result.next())
            {
                UserSignalPreferenceBaseBean oSignalPreference = new UserSignalPreferenceBaseBean();
                oSignalPreference.setUser_id(result.getInt("user_id"));
                oSignalPreference.setSignal_type(result.getInt("signal_type"));
                oSignalPreference.setStrategy_id(result.getLong("strategy_id"));
                oSignalPreference.setMarket_type_id(result.getInt("market_type_id"));
                oSignalPreference.setMarket_period_id(result.getInt("market_period_id"));
                oSignalPreference.setNotify_email(result.getString("notify_email"));
                oSignalPreference.setNotify_sms(result.getString("notify_sms"));

                if (1 == result.getInt("active"))
                {
                    oSignalPreference.setActive(true);
                }
                else
                {
                    oSignalPreference.setActive(false);
                }

                if (1 == result.getInt("enable_email"))
                {
                    oSignalPreference.setEnable_email(true);
                }
                else
                {
                    oSignalPreference.setEnable_email(false);
                }

                if (1 == result.getInt("enable_sms"))
                {
                    oSignalPreference.setEnable_sms(true);
                }
                else
                {
                    oSignalPreference.setEnable_sms(false);
                }

                oResult.add(oSignalPreference);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,
            "Exception happened in WSDBAccessor.getSignalPreferenceOfAutoTrader()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            try
            {
                if (null != result)
                {
                    result.close();
                }

                if (null != stat)
                {
                    stat.close();
                }

                if (null != conn)
                {
                    conn.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                
                LogTools.getInstance().insertLog(DebugLevel.ERROR,
                        "Exception happened in WSDBAccessor.getSignalPreferenceOfAutoTrader()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
            }
        }

        return oResult;
    }

    public AutoTradeAccountBean getCurrentAutoTradeAccountInfoByMarket(Signal oSignal)
    {
        if (oSignal == null)
        {
            return null;
        }

        AutoTradeAccountBean oResult = null;

        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;

        try
        {
            conn = getConnection();
            stat = conn.createStatement();

            String querySql = "select * from auto_trade_account_table where market_type_id="
                    + oSignal.getMarket_type_id();
            result = stat.executeQuery(querySql);

            if (result.next())
            {
                oResult = new AutoTradeAccountBean();

                oResult.setUser_id(result.getLong("user_id"));
                oResult.setMarket_type_id(result.getLong("market_type_id"));
                oResult.setPosition_type(result.getInt("position_type"));
                oResult.setPositon(result.getInt("positon"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,
                    "Exception happened in WSDBAccessor.getCurrentAutoTradeAccountInfoByMarket()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            try
            {
                if (null != result)
                {
                    result.close();
                }

                if (null != stat)
                {
                    stat.close();
                }

                if (null != conn)
                {
                    conn.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                
                LogTools.getInstance().insertLog(DebugLevel.ERROR,
                        "Exception happened in WSDBAccessor.getCurrentAutoTradeAccountInfoByMarket()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
            }
        }

        return oResult;
    }

    public boolean clearAutoTradeAccountInfo(long userID)
    {
        boolean result = false;
        Connection conn = null;
        Statement stat = null;

        try
        {
            conn = getConnection();
            stat = conn.createStatement();

            String querySql = "delete from auto_trade_account_table where user_id="
                    + userID;
            stat.execute(querySql);

            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,
            "Exception happened in WSDBAccessor.clearAutoTradeAccountInfo()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            try
            {
                if (null != stat)
                {
                    stat.close();
                }

                if (null != conn)
                {
                    conn.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                
                LogTools.getInstance().insertLog(DebugLevel.ERROR,
                "Exception happened in WSDBAccessor.clearAutoTradeAccountInfo()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
            }
        }

        return result;
    }

    public boolean updateAutoTradeStatus(int iStatus)
    {
        boolean result = false;
        Connection conn = null;
        Statement stat = null;

        try
        {
            conn = getConnection();
            stat = conn.createStatement();

            String querySql = "update auto_trade_flag_table set enable_trade="
                    + iStatus;
            stat.execute(querySql);

            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,
            "Exception happened in WSDBAccessor.updateAutoTradeStatus()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            try
            {
                if (null != stat)
                {
                    stat.close();
                }

                if (null != conn)
                {
                    conn.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                
                LogTools.getInstance().insertLog(DebugLevel.ERROR,
                "Exception happened in WSDBAccessor.updateAutoTradeStatus()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
            }
        }

        return result;
    }

    public int getAutoTradeStatus()
    {
        int autoTraderFlag = WSConstants.CLOSE_AUTO_TRADE;

        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;

        try
        {
            conn = getConnection();
            stat = conn.createStatement();

            String querySql = "select enable_trade from auto_trade_flag_table";
            result = stat.executeQuery(querySql);

            if (result.next())
            {
                autoTraderFlag = result.getInt("enable_trade");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,
            "Exception happened in WSDBAccessor.getAutoTradeStatus()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            try
            {
                if (null != result)
                {
                    result.close();
                }

                if (null != stat)
                {
                    stat.close();
                }

                if (null != conn)
                {
                    conn.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                
                LogTools.getInstance().insertLog(DebugLevel.ERROR,
                "Exception happened in WSDBAccessor.getAutoTradeStatus()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
            }
        }

        return autoTraderFlag;
    }
    
    public List<Signal> getAllPreviousConversedSignals(Signal oNewSignal, long startDate)
    {
        String querySql = "";
        
        int signal_type = CommonDefine.CLOSE_SIGNAL;
        if (CommonDefine.LONG_SIGNAL == oNewSignal.getSignal_type())
        {
            signal_type = CommonDefine.SHORT_SIGNAL;
            
            querySql = "select * from signals_table where "
                + " signal_type="+ signal_type 
                + " and market_type_id="+ oNewSignal.getMarket_type_id() 
                + " and signal_period="+ oNewSignal.getSignal_period() 
                + " and strategy_id="+ oNewSignal.getStrategy_id()
                + " and generate_date > "+ startDate 
                + " and generate_date <= "+ oNewSignal.getGenerate_date()
                + " order by generate_date DESC";
        }
        else if(CommonDefine.SHORT_SIGNAL == oNewSignal.getSignal_type())
        {
            signal_type = CommonDefine.LONG_SIGNAL;
            
            querySql = "select * from signals_table where "
                + " signal_type="+ signal_type 
                + " and market_type_id="+ oNewSignal.getMarket_type_id() 
                + " and signal_period="+ oNewSignal.getSignal_period() 
                + " and strategy_id="+ oNewSignal.getStrategy_id()
                + " and generate_date > "+ startDate 
                + " and generate_date <= "+ oNewSignal.getGenerate_date()
                + " order by generate_date DESC";
        }
        else if(CommonDefine.CLOSE_SIGNAL == oNewSignal.getSignal_type())
        {
            //be careful, if new signal is Close, the signals condition should be  ( "current Signal" > "right signals" >= "start date" )
            
            querySql = "select * from signals_table where "
                + " ( signal_type="+ CommonDefine.LONG_SIGNAL + " or signal_type="+ CommonDefine.SHORT_SIGNAL + " )"
                + " and market_type_id="+ oNewSignal.getMarket_type_id() 
                + " and signal_period="+ oNewSignal.getSignal_period() 
                + " and strategy_id="+ oNewSignal.getStrategy_id()
                + " and generate_date >= "+ startDate 
                + " and generate_date <= "+ oNewSignal.getGenerate_date()
                + " order by generate_date DESC";
        }
                
        return getSignalListBySql(querySql);
    }

//    public List<Signal> getAllPreviousConversedBasicSignals(Signal oNewSignal, long startDate)
//    {
//        int signal_type = CommonDefine.BUY_SINGAL;
//        if (CommonDefine.BUY_SINGAL == oNewSignal.getSignal_type())
//        {
//            signal_type = CommonDefine.SELL_SINGAL;
//        }
//
//        String querySql = "select * from signals_table where "
//                +" signal_type="+ signal_type 
//                + " and market_type_id="+ oNewSignal.getMarket_type_id() 
//                + " and signal_period="+ oNewSignal.getSignal_period() 
//                + " and generate_date > "+ startDate 
//                + " and generate_date < "+ oNewSignal.getGenerate_date()
//                + " order by generate_date DESC";
//
//        return getSignalListBySql(querySql);
//    }
    
    public List<Signal> getLatestBasiclSignal(long marketID, int periodID, int amount)
    {
        String querySql = "select * from signals_table where "
                + " ( signal_type="+ CommonDefine.BUY_SINGAL + " or signal_type = "+CommonDefine.SELL_SINGAL+")"
                + " and market_type_id="+ marketID 
                + " and signal_period="+ periodID 
                + " order by generate_date DESC limit 0,"+amount;

        return getSignalListBySql(querySql);
    }

    public List<Signal> getSignalListBySql(String querySql)
    {
        List<Signal> finalResult = new ArrayList<Signal>();

        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;

        try
        {
            conn = getConnection();
            stat = conn.createStatement();

            result = stat.executeQuery(querySql);

            while (result.next())
            {
                Signal oSignal = new Signal();

                oSignal.setId(result.getLong("id"));
                oSignal.setDirection(result.getInt("direction"));
                oSignal.setSignal_type(result.getInt("signal_type"));
                oSignal.setStrategy_id(result.getLong("strategy_id"));
                oSignal.setMarket_type_id(result.getInt("market_type_id"));
                oSignal.setSignal_period(result.getInt("signal_period"));
                oSignal.setGenerate_date(result.getLong("generate_date"));
                oSignal.setExpire_date(result.getLong("expire_date"));
                oSignal.setSignalValue(result.getDouble("signal_value"));
                oSignal.setSignal_rate(result.getInt("signal_rate"));
                oSignal.setProfit(result.getDouble("profit"));
                oSignal.setSystem_name(result.getString("system_name"));
                
                finalResult.add(oSignal);
            }
        }
        catch (Exception e)
        {
            finalResult = null;

            e.printStackTrace();

            LogTools.getInstance().insertLog(DebugLevel.ERROR,
            "Exception happened in WSDBAccessor.getSignalBySql()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            try
            {
                if (null != result)
                {
                    result.close();
                }

                if (null != stat)
                {
                    stat.close();
                }

                if (null != conn)
                {
                    conn.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                
                LogTools.getInstance().insertLog(DebugLevel.ERROR,
                "Exception happened in WSDBAccessor.getSignalBySql()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
            }
        }

        return finalResult;
    }
    
    /**
     * get previous signal under same Signal type(Buy or Sell or Long or Short or Close), Market (e.g,
     * EUR/USD), Period ID (e.g, 1min)
     * 
     * @param oNewSignal
     * @return
     */
    public Signal getPreviousSameDirectionOrCloseSignal(Signal oNewSignal)
    {
        String querySql = "select * from signals_table where "
                + " ( signal_type=" + oNewSignal.getSignal_type() + " or signal_type="+CommonDefine.CLOSE_SIGNAL+" ) "
                + " and market_type_id="+ oNewSignal.getMarket_type_id() 
                + " and signal_period="+ oNewSignal.getSignal_period() 
                + " and strategy_id="+ oNewSignal.getStrategy_id()
                + " and generate_date <"+ oNewSignal.getGenerate_date()
                + " order by generate_date DESC limit 0,1";

        return getSignalBySql(querySql);
    }
    
    
    public Signal getPreviousSameDirectionOrLongShortSignal(Signal oNewSignal)
    {
        String querySql = "select * from signals_table where "
                + "( signal_type=" + oNewSignal.getSignal_type() + " or signal_type="+CommonDefine.LONG_SIGNAL+" or signal_type="+CommonDefine.SHORT_SIGNAL+" )"
                +" and market_type_id="+ oNewSignal.getMarket_type_id() 
                + " and signal_period="+ oNewSignal.getSignal_period() 
                + " and strategy_id="+ oNewSignal.getStrategy_id()
                + " and generate_date<"+ oNewSignal.getGenerate_date()
                + " order by generate_date DESC limit 0,1";
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,querySql);

        return getSignalBySql(querySql);
    }
    
    /**
     * get previous signal under same Signal type(Buy or Sell or Long or Short or Close), Market (e.g,
     * EUR/USD), Period ID (e.g, 1min)
     * 
     * @param oNewSignal
     * @return
     */
    public Signal getPreviousSameDirectionSignal(Signal oNewSignal)
    {
        String querySql = "select * from signals_table where "
                +" signal_type="+ oNewSignal.getSignal_type() 
                + " and market_type_id="+ oNewSignal.getMarket_type_id() 
                + " and signal_period="+ oNewSignal.getSignal_period() 
                + " and strategy_id="+ oNewSignal.getStrategy_id()
                + " and generate_date<"+ oNewSignal.getGenerate_date()
                + " order by generate_date DESC limit 0,1";

        return getSignalBySql(querySql);
    }

    /**
     * get previous signal(long or short or close) under same Signal Market (e.g, EUR/USD),
     * Period ID (e.g, 1min)
     * 
     * @param oNewSignal
     * @return
     */
    public Signal getPreviousSignal(Signal oNewSignal)
    {
        String querySql = "select * from signals_table where "
                + "( signal_type="+CommonDefine.LONG_SIGNAL + " or signal_type=" + CommonDefine.SHORT_SIGNAL + " or signal_type = "+CommonDefine.CLOSE_SIGNAL+")"
                + " and market_type_id="+ oNewSignal.getMarket_type_id() 
                + " and signal_period="+ oNewSignal.getSignal_period() 
                + " and strategy_id="+ oNewSignal.getStrategy_id()
                + " and generate_date<"+ oNewSignal.getGenerate_date()
                + " order by generate_date DESC limit 0,1";

        return getSignalBySql(querySql);
    }
    
    /**
     * get previous basic signal(Buy or Sell) under same Signal Market (e.g, EUR/USD),
     * Period ID (e.g, 1min)
     * 
     * @param oNewSignal
     * @return
     */
    public Signal getLatestSignal(long strategyID, long marketID, int periodID)
    {
        String querySql = "select * from signals_table where "
                +" (signal_type=" + CommonDefine.LONG_SIGNAL + " or signal_type="+ CommonDefine.SHORT_SIGNAL + " or signal_type="+ CommonDefine.CLOSE_SIGNAL +" ) "
                +" and strategy_id="+ strategyID
                +" and market_type_id="+ marketID 
                +" and signal_period="+ periodID 
                +" order by generate_date DESC limit 0,1";

        return getSignalBySql(querySql);
    }
    
//    public Signal getPreviousBasiceSignal(Signal oNewSignal)
//    {
//        String querySql = "select * from signals_table where "
//                + "(signal_type="+ CommonDefine.BUY_SINGAL + " or signal_type="+ CommonDefine.SELL_SINGAL + ")"
//                + " and market_type_id="+ oNewSignal.getMarket_type_id() 
//                + " and signal_period="+ oNewSignal.getSignal_period() 
//                + " and generate_date<"+ oNewSignal.getGenerate_date()
//                + " order by generate_date DESC limit 0,1";
//
//        return getSignalBySql(querySql);
//    }
    
    

    public Signal getSignalBySql(String querySql)
    {
        Signal finalResult = null;

        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;

        try
        {
            conn = getConnection();
            stat = conn.createStatement();

            result = stat.executeQuery(querySql);

            if (result.next())
            {
                finalResult = new Signal();

                finalResult.setId(result.getLong("id"));
                finalResult.setDirection(result.getInt("direction"));
                finalResult.setSignal_type(result.getInt("signal_type"));
                finalResult.setStrategy_id(result.getLong("strategy_id"));
                finalResult.setMarket_type_id(result.getInt("market_type_id"));
                finalResult.setSignal_period(result.getInt("signal_period"));
                finalResult.setGenerate_date(result.getLong("generate_date"));
                finalResult.setExpire_date(result.getLong("expire_date"));
                finalResult.setSignalValue(result.getDouble("signal_value"));
                finalResult.setSignal_rate(result.getInt("signal_rate"));
                finalResult.setProfit(result.getDouble("profit"));
                finalResult.setSystem_name(result.getString("system_name"));
            }
        }
        catch (Exception e)
        {
            finalResult = null;

            e.printStackTrace();

            LogTools.getInstance().insertLog(DebugLevel.ERROR,
            "Exception happened in WSDBAccessor.getSignalBySql()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            try
            {
                if (null != result)
                {
                    result.close();
                }

                if (null != stat)
                {
                    stat.close();
                }

                if (null != conn)
                {
                    conn.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                
                LogTools.getInstance().insertLog(DebugLevel.ERROR,
                "Exception happened in WSDBAccessor.getSignalBySql()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
            }
        }

        return finalResult;
    }

    public boolean reduceUserSMSCredits(List<Long> userIdList, int credits)
    {
        if (null == userIdList)
        {
            return false;
        }

        if (userIdList.size() == 0)
        {
            LogTools.getInstance().insertLog(DebugLevel.WARNING,
                    "Reduce users' SMS credits, but user id list is empty!");
            return true;
        }

        boolean result = false;
        Connection conn = null;
        Statement stat = null;

        try
        {
            conn = getConnection();
            stat = conn.createStatement();

            String updateSql = "update users_table set sms_credit = sms_credit - "+credits+" where id in (";

            for (int i = 0; i < userIdList.size(); i++)
            {
                updateSql += userIdList.get(i).longValue();

                if (i < userIdList.size() - 1)
                {
                    updateSql += ",";
                }
                else if (i == userIdList.size() - 1)
                {
                    updateSql += ")";
                }
            }

            stat.execute(updateSql);

            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,
            "Exception happened in WSDBAccessor.reduceUserSMSCredits()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            try
            {
                if (null != stat)
                {
                    stat.close();
                }

                if (null != conn)
                {
                    conn.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                
                LogTools.getInstance().insertLog(DebugLevel.ERROR,
                "Exception happened in WSDBAccessor.reduceUserSMSCredits()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
            }
        }

        return result;
    }

    public List<UserSignalPreferenceBaseBean> getAllUserCustomSpecificSignal(
            Signal signalBean, boolean isForSMS)
    {
        List<UserSignalPreferenceBaseBean> oResult = new ArrayList<UserSignalPreferenceBaseBean>();

        if (null == signalBean)
        {
            return oResult;
        }

        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        try
        {
            conn = getConnection();
            stat = conn.createStatement();

            String searchSql = "";
            if (isForSMS)
            {
                // must check sms credits
                searchSql = "select A.id,A.user_id,A.signal_type,A.market_type_id,A.market_period_id,A.notify_email,A.notify_sms,A.enable_email,A.enable_sms,A.active,B.time_zone_id, B.sms_credit "
                        + " from user_signal_preference_table A, users_table B where "+ " A.market_type_id="+ signalBean.getMarket_type_id()
                        + " and A.strategy_id="+ signalBean.getStrategy_id()
                        + " and A.user_id = B.id and B.sms_credit > 0 and B.enable="+CommonDefine.USER_ENABLED
                        + " and A.market_period_id="+ signalBean.getSignal_period()
                        + " and B.expired_date > " + System.currentTimeMillis();
            }
            else
            {
                // don't need check sms credits
                searchSql = "select A.id,A.user_id,A.signal_type,A.market_type_id,A.market_period_id,A.notify_email,A.notify_sms,A.enable_email,A.enable_sms,A.active,B.time_zone_id, B.sms_credit "
                        + " from user_signal_preference_table A, users_table B where " + " A.market_type_id="+ signalBean.getMarket_type_id()
                        + " and A.strategy_id="+ signalBean.getStrategy_id()
                        + " and A.user_id = B.id and B.enable="+CommonDefine.USER_ENABLED+" and A.market_period_id="+ signalBean.getSignal_period()
                        + " and B.expired_date > " + System.currentTimeMillis();
            }

            result = stat.executeQuery(searchSql);
            while (result.next())
            {
                UserSignalPreferenceBaseBean oSignalPreference = new UserSignalPreferenceBaseBean();
                oSignalPreference.setUser_id(result.getInt("user_id"));
                oSignalPreference.setSignal_type(result.getInt("signal_type"));
                oSignalPreference.setMarket_type_id(result
                        .getInt("market_type_id"));
                oSignalPreference.setMarket_period_id(result
                        .getInt("market_period_id"));
                oSignalPreference.setNotify_email(result
                        .getString("notify_email"));
                oSignalPreference.setNotify_sms(result.getString("notify_sms"));
                oSignalPreference.setTime_zone_id(result
                        .getLong("time_zone_id"));
                oSignalPreference.setUser_sms_credits(result
                        .getLong("sms_credit"));

                if (1 == result.getInt("active"))
                {
                    oSignalPreference.setActive(true);
                }
                else
                {
                    oSignalPreference.setActive(false);
                }

                if (1 == result.getInt("enable_email"))
                {
                    oSignalPreference.setEnable_email(true);
                }
                else
                {
                    oSignalPreference.setEnable_email(false);
                }

                if (1 == result.getInt("enable_sms"))
                {
                    oSignalPreference.setEnable_sms(true);
                }
                else
                {
                    oSignalPreference.setEnable_sms(false);
                }

                oResult.add(oSignalPreference);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,
            "Exception happened in WSDBAccessor.getAllUserCustomSpecificSignal()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            try
            {
                if (null != result)
                {
                    result.close();
                }

                if (null != stat)
                {
                    stat.close();
                }

                if (null != conn)
                {
                    conn.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                
                LogTools.getInstance().insertLog(DebugLevel.ERROR,
                "Exception happened in WSDBAccessor.getAllUserCustomSpecificSignal()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
            }
        }

        return oResult;
    }

    public ArrayList<Signal> getSpecificSignals(int signalType,
            long market_type_id, int signal_period, int strategy_id, int signalsCount)
    {
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Call function, getAllSignals(int signalType,long market_type_id, int signal_period, int signalsCount);");
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Call function, getAllSignals(int signalType,long market_type_id, int signal_period, int signalsCount);");

        if (-1 == signalType || -1 == market_type_id)
        {
            InfoTrace
                    .getInstance()
                    .printInfo(DebugLevel.WARNING,
                            "Call function, getAllSignals, but the signal_type or market_type_id is -1");
            LogTools
                    .getInstance()
                    .insertLog(DebugLevel.WARNING,
                            "Call function, getAllSignals, but the signal_type or market_type_id is -1");

            return new ArrayList<Signal>();
        }

        ArrayList<Signal> oResult = new ArrayList<Signal>();

        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        try
        {
            conn = getConnection();
            stat = conn.createStatement();

            result = stat
                    .executeQuery("select * from signals_table where signal_type="
                            + signalType
                            + " and market_type_id="
                            + market_type_id
                            + " and strategy_id="
                            + strategy_id
                            + " and signal_period="
                            + signal_period
                            + " order by generate_date DESC limit 0,"
                            + signalsCount);

            while (result.next())
            {
                Signal oSignal = new Signal();
                oSignal.setSignal_type(result.getInt("signal_type"));
                oSignal.setStrategy_id(result.getLong("strategy_id"));
                oSignal.setMarket_type_id(result.getLong("market_type_id"));
                oSignal.setSignal_period(result.getInt("signal_period"));
                oSignal.setGenerate_date(result.getLong("generate_date"));
                oSignal.setSignalValue(result.getDouble("signal_value"));
                oSignal.setExpire_date(result.getLong("expire_date"));
                oSignal.setSignal_rate(result.getInt("signal_rate"));
                oSignal.setDirection(result.getInt("direction"));
                oSignal.setProfit(result.getDouble("profit"));
                oSignal.setSystem_name(result.getString("system_name"));

                oResult.add(oSignal);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

            LogTools.getInstance().insertLog(DebugLevel.ERROR,"Exception happened in WSDBAccessor.getAllSignals()! Exception details=> "
                            + CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            try
            {
                if (null != result)
                {
                    result.close();
                }

                if (null != stat)
                {
                    stat.close();
                }

                if (null != conn)
                {
                    conn.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();

                LogTools.getInstance().insertLog(DebugLevel.ERROR,"Exception happened in WSDBAccessor.getAllSignals()! Exception details=> "
                                + CommonTools.getExceptionDescribe(e));
            }
        }

        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"................Get "
                                + oResult.size()+ " Recorders of Signal...............................................");
        LogTools.getInstance().insertLog(DebugLevel.INFO,"................Get "
                                + oResult.size()+ " Recorders of Signal...............................................");

        return oResult;
    }

    public boolean updateSignalProfitIntoDB(long signalID, double profit)
    {
        if (profit == 0)
        {
            return true;
        }

        boolean result = false;
        Connection conn = null;
        Statement stat = null;

        try
        {
            conn = getConnection();
            stat = conn.createStatement();

            stat.execute("update signals_table set profit=" + profit
                    + " where id=" + signalID);

            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,"Exception happened in WSDBAccessor.updateSignalProfitIntoDB()! Exception details=> "
                    + CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            try
            {
                if (null != stat)
                {
                    stat.close();
                }

                if (null != conn)
                {
                    conn.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                
                LogTools.getInstance().insertLog(DebugLevel.ERROR,"Exception happened in WSDBAccessor.updateSignalProfitIntoDB()! Exception details=> "
                        + CommonTools.getExceptionDescribe(e));
            }
        }

        return result;
    }

    public synchronized boolean storeSignalIntoDB(Signal signalBean)
    {
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Call function, storeSignalIntoDB");
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Call function, storeSignalIntoDB");
                
        if (null == signalBean)
        {
            return false;
        }

        boolean result = false;
        Connection conn = null;
        Statement stat = null;
        ResultSet oQueryResult = null;

        try
        {
            conn = getConnection();
            stat = conn.createStatement();

            // System.out.println("Time of call check:
            // "+System.currentTimeMillis()+" for signal
            // value:"+signalBean.getSignalValue());

            String sqlString = "select * from signals_table where signal_type="+ signalBean.getSignal_type() 
                    + " and market_type_id ="+ signalBean.getMarket_type_id() 
                    + " and signal_period ="+ signalBean.getSignal_period() 
                    + " and strategy_id ="+ signalBean.getStrategy_id() 
                    + " and generate_date ="+ signalBean.getGenerate_date();

            // check whether it is already exist
            oQueryResult = stat.executeQuery(sqlString);

            if (oQueryResult.next())
            {
                InfoTrace.getInstance().printInfo(DebugLevel.WARNING,"Duplicated Siganl, don't need store !!! signal_type="
                                + signalBean.getSignal_type()
                                + ", market_type_id="+ signalBean.getMarket_type_id()
                                + ",signal_period =" + signalBean.getSignal_period()
                                + ",signal_strategy_id =" + signalBean.getStrategy_id()
                                + ",generate_date ="+ signalBean.getGenerate_date());
                
                LogTools.getInstance().insertLog(DebugLevel.WARNING,"Duplicated Siganl, don't need store !!! signal_type="
                                + signalBean.getSignal_type()
                                + ", market_type_id="+ signalBean.getMarket_type_id()
                                + ",signal_period ="+ signalBean.getSignal_period()
                                + ",signal_strategy_id =" + signalBean.getStrategy_id()
                                + ",generate_date ="+ signalBean.getGenerate_date());

                result = false;
            }
            else
            {
                // System.out.println("Time of call insert:
                // "+System.currentTimeMillis()+" for signal
                // value:"+signalBean.getSignalValue());

                stat.execute("insert into signals_table(signal_type,strategy_id,market_type_id,signal_period,generate_date,signal_value,expire_date,signal_rate,direction,profit,system_name) values("
                                + signalBean.getSignal_type()
                                + ","
                                + signalBean.getStrategy_id()
                                + ","
                                + signalBean.getMarket_type_id()
                                + ","
                                + signalBean.getSignal_period()
                                + ","
                                + signalBean.getGenerate_date()
                                + ","
                                + signalBean.getSignalValue()
                                + ","
                                + signalBean.getExpire_date()
                                + ","
                                + signalBean.getSignal_rate()
                                + ","
                                + signalBean.getDirection()
                                + ","
                                + signalBean.getProfit()
                                + ",'"
                                + signalBean.getSystem_name() + "')");

                InfoTrace.getInstance().printInfo(
                        DebugLevel.INFO,
                        "Store signal successfully!!! signal_type="
                                + signalBean.getSignal_type()
                                + ", market_type_id="
                                + signalBean.getMarket_type_id()
                                + ",signal_period ="
                                + signalBean.getSignal_period()
                                + ",generate_date ="
                                + signalBean.getGenerate_date());

                LogTools.getInstance().insertLog(
                        DebugLevel.INFO,
                        "Store signal successfully!!! signal_type="
                                + signalBean.getSignal_type()
                                + ", market_type_id="
                                + signalBean.getMarket_type_id()
                                + ",signal_period ="
                                + signalBean.getSignal_period()
                                + ",generate_date ="
                                + signalBean.getGenerate_date()
                                + ",signal_value ="
                                + signalBean.getSignalValue());

                result = true;

                //System.out.println("Complate signal: "+System.currentTimeMillis()+" for signal value:"+signalBean.getSignalValue());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,"Exception happened in WSDBAccessor.storeSignalIntoDB()! Exception details=> "
                    + CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            try
            {
                if (null != oQueryResult)
                {
                    oQueryResult.close();
                }

                if (null != stat)
                {
                    stat.close();
                }

                if (null != conn)
                {
                    conn.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                
                LogTools.getInstance().insertLog(DebugLevel.ERROR,"Exception happened in WSDBAccessor.storeSignalIntoDB()! Exception details=> "
                        + CommonTools.getExceptionDescribe(e));
            }
        }

        return result;
    }
}
