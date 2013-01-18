package com.cleartraders.webapp.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cleartraders.common.db.CommonDBAccessor;
import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.MarketTypeAndPeriodBean;
import com.cleartraders.common.entity.MarketTypeBean;
import com.cleartraders.common.entity.QuickLinkBean;
import com.cleartraders.common.entity.Signal;
import com.cleartraders.common.entity.TimeZoneBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.entity.UserSignalPreferenceBaseBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.model.bean.AutoTradeInfoBean;
import com.cleartraders.webapp.model.bean.CumulativeProftDataBean;
import com.cleartraders.webapp.model.bean.PaymentHistoryBean;
import com.cleartraders.webapp.model.bean.SchedulerNotificationsBean;
import com.cleartraders.webapp.model.bean.SignalNotificationBean;
import com.cleartraders.webapp.model.bean.UserAnalysisMarketBean;
import com.cleartraders.webapp.model.bean.UserBrokerInfo;

public class DBAccessor extends CommonDBAccessor
{
    private static DBAccessor m_oInstance = new DBAccessor();

    public DBAccessor()
    {
    }

    public synchronized static DBAccessor getInstance()
    {
        return m_oInstance;
    }
        
    public boolean storePaymentIntoDB(PaymentHistoryBean paymentBean)
    {
        if(null == paymentBean)
        {
            return false;
        }
        
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("insert into payment_history_table(user_id,pay_item_name,pay_amount,date,txn_id,completed) values("+
                         paymentBean.getUser_id()+",'"+paymentBean.getPay_item_name()+"',"+paymentBean.getPay_amount()+","+paymentBean.getDate()+",'"+paymentBean.getTxn_id()+"',"+paymentBean.getCompleted()+")");
            
            result = true;
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public boolean updatePaymentTxnID(long paymentID, String txnID)
    {
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("update payment_history_table set txn_id='"+txnID+"' where id="+paymentID);
            
            result = true;
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public boolean updatePaymentStatus(long paymentID, int completed)
    {
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        long currentTime = System.currentTimeMillis();
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("update payment_history_table set completed="+completed+",complete_date=" + currentTime + " where id="+paymentID);
            
            result = true;
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public List<PaymentHistoryBean> getPaymentListByTxnID(String transactionID)
    {
        List<PaymentHistoryBean> oPaymentList = new ArrayList<PaymentHistoryBean>();
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select * from payment_history_table where txn_id='"+transactionID+"'");
                        
            while (result.next())
            {
                PaymentHistoryBean paymentBean = new PaymentHistoryBean();
                
                paymentBean.setId(result.getLong("id"));
                paymentBean.setUser_id(result.getLong("user_id")); 
                paymentBean.setPay_item_name(result.getString("pay_item_name"));
                paymentBean.setPay_amount(result.getDouble("pay_amount"));
                paymentBean.setDate(result.getLong("date"));
                paymentBean.setTxn_id(result.getString("txn_id"));
                paymentBean.setCompleted(result.getInt("completed"));
                
                oPaymentList.add(paymentBean);
            }
        }
        catch (Exception e)
        {
            oPaymentList = null;
            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return oPaymentList;
    }
    
    public List<PaymentHistoryBean> getPaymentList(long userID, int completed)
    {
        List<PaymentHistoryBean> notCompletedPayment = null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select * from payment_history_table where completed="+completed);
            
            notCompletedPayment = new ArrayList<PaymentHistoryBean>();
            
            while (result.next())
            {
                PaymentHistoryBean paymentBean = new PaymentHistoryBean();
                
                paymentBean.setId(result.getLong("id"));
                paymentBean.setUser_id(result.getLong("user_id")); 
                paymentBean.setPay_item_name(result.getString("pay_item_name"));
                paymentBean.setPay_amount(result.getDouble("pay_amount"));
                paymentBean.setDate(result.getLong("date"));
                paymentBean.setTxn_id(result.getString("txn_id"));
                paymentBean.setCompleted(result.getInt("completed"));
                
                notCompletedPayment.add(paymentBean);
            }
        }
        catch (Exception e)
        {
            notCompletedPayment = null;
            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return notCompletedPayment;
    }
    
    public AutoTradeInfoBean getAutoTradeInfo()
    {
        AutoTradeInfoBean oResult = null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select enable_trade from auto_trade_flag_table");
            
            if (result.next())
            {
                oResult = new AutoTradeInfoBean();
                int fromDate = result.getInt("enable_trade");
                
                if(WebConstants.OPEN_AUTO_TRADE == fromDate)
                {
                    oResult.setStarted(true);
                }
                else
                {
                    oResult.setStarted(false);
                }
                           
            }
        }
        catch (Exception e)
        {
            oResult = null;
            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return oResult;
    }
    
    public boolean updateMyAnalysisConditionActive(UserBean oUserBean,long conditionId,int activeFlag)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        }
        
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("update user_analysis_market_table set active="+activeFlag+" where user_id="+oUserBean.getId()+" and id="+conditionId);
            result = true;
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public boolean updateMyAnalysisConditionDate(UserBean oUserBean,long fromDate,long toDate)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        }
        
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("update user_analysis_market_table set analysis_from="+fromDate+", analysis_to="+toDate+" where user_id="+oUserBean.getId());
            result = true;
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public boolean changeAnalysisMarketCondition(UserBean oUserBean,long previousMarketID,long newMarketID,long newStrategyID, long newSignalPeriodID)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        }
        
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("update user_analysis_market_table set market_period_id="+newSignalPeriodID+", strategy_id="+newStrategyID+",market_type_id="+newMarketID+" where user_id="+oUserBean.getId()+" and market_type_id="+previousMarketID);
            result = true;
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public boolean removeMyAnalysisCondition(UserBean oUserBean, long conditionID)
    {
        if(null == oUserBean)
        {
            return false;
        }
        
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("delete from user_analysis_market_table where user_id ="+oUserBean.getId()+" and id="+conditionID);
            result = true;
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public boolean addAnalysisMarketCondition(UserBean oUserBean,long newMarketID,long newStrategyID, long newSignalPeriodID, long fromDate, long toDate)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        }
        
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("insert into user_analysis_market_table(user_id,market_type_id,strategy_id,market_period_id,analysis_from,analysis_to,active) values("+oUserBean.getId()+","+newMarketID+","+newStrategyID+","+newSignalPeriodID+","+fromDate+","+toDate+",1)");
            result = true;
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public long[] getAnalysisMarketConditionDate(UserBean oUserBean)
    {
        if(null == oUserBean)
        {
            return null;
        }
        
        long[] finalResult = null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select analysis_from,analysis_to from user_analysis_market_table where user_id = "+oUserBean.getId());
            
            finalResult = new long[2];
            finalResult[0] = 0;
            finalResult[1] = 0;
            
            while (result.next())
            {
                long fromDate = result.getLong("analysis_from");
                long toDate = result.getLong("analysis_to");
                
                if(fromDate > 0 || toDate > 0)
                {
                    finalResult[0] = fromDate;
                    finalResult[1] = toDate;
                    
                    break;
                }                
            }
        }
        catch (Exception e)
        {
            finalResult = null;
            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return finalResult;
    }
    
    public List<UserAnalysisMarketBean> getUserAnalysisMarketList(UserBean oUserBean)
    {
        if(null == oUserBean)
        {
            return new ArrayList<UserAnalysisMarketBean>();
        }
        
        List<UserAnalysisMarketBean> finalResult = null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select * from user_analysis_market_table where user_id = "+oUserBean.getId());
            
            finalResult = new ArrayList<UserAnalysisMarketBean>();
            
            while (result.next())
            {
                UserAnalysisMarketBean oBean = new UserAnalysisMarketBean();
                oBean.setId(result.getLong("id"));
                oBean.setMarket_type_id(result.getLong("market_type_id"));
                oBean.setStrategy_id(result.getLong("strategy_id"));
                oBean.setUser_id(oUserBean.getId());
                oBean.setMarket_period_id(result.getLong("market_period_id"));
                oBean.setAnalysis_from((long)result.getDouble("analysis_from"));
                oBean.setAnalysis_to((long)result.getDouble("analysis_to"));
                
                if(WebConstants.ENABLE == result.getInt("active"))
                {
                    oBean.setActive(true);
                }
                else
                {
                    oBean.setActive(false);
                }
                
                finalResult.add(oBean);
            }
        }
        catch (Exception e)
        {
            if(finalResult != null)
            {
                finalResult.clear();
            }
            finalResult = null;
            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return finalResult;
    }
    
    public int getSignalsAmount(int signalType, long strategyID, long marketID,long periodId, long from, long to)
    {
       int signalAmount = 0;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
                        
            if(from > 0 && to > 0)
            {
                result = stat.executeQuery("select count(*) all_signals from signals_table where signal_type="+signalType+" and strategy_id="+strategyID+" and market_type_id="+marketID+" and signal_period="+periodId+" and generate_date >="+from+" and generate_date <="+to+" order by generate_date ASC;");
            }
            else if(from == 0 && to == 0)
            {
                result = stat.executeQuery("select count(*) all_signals from signals_table where signal_type="+signalType+" and strategy_id="+strategyID+" and market_type_id="+marketID+" and signal_period="+periodId+" order by generate_date ASC;");
            }
            else if(from ==0)
            {
                result = stat.executeQuery("select count(*) all_signals from signals_table where signal_type="+signalType+" and strategy_id="+strategyID+" and market_type_id="+marketID+" and signal_period="+periodId+" and generate_date <="+to+" order by generate_date ASC;");
            }
            else if(to == 0)
            {
                result = stat.executeQuery("select count(*) all_signals from signals_table where signal_type="+signalType+" and strategy_id="+strategyID+" and market_type_id="+marketID+" and signal_period="+periodId+" and generate_date >="+from+" order by generate_date ASC;");
            }
                        
            if (result.next())
            {
                signalAmount = result.getInt("all_signals");
            }
        }
        catch (Exception e)
        {
            signalAmount=0;
            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return signalAmount;
    }
            
    public List<Signal> getTopProfitSignal(int topProfitSignal, long startDate)
    {
        List<Signal> finalResult = null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
                        
            result = stat.executeQuery("select * from signals_table where generate_date > "+startDate+" and (signal_type="+CommonDefine.LONG_SIGNAL+" or signal_type="+CommonDefine.SHORT_SIGNAL+" or signal_type="+CommonDefine.CLOSE_SIGNAL+") order by profit DESC limit 0,"+topProfitSignal);
                    
            finalResult = new ArrayList<Signal>();
            
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
            if(finalResult != null)
            {
                finalResult.clear();
            }
            finalResult = null;
            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return finalResult;
    }
    
    public List<CumulativeProftDataBean> getSignalsProftData(long strategyID, long marketID,long periodId, long from, long to)
    {
        List<CumulativeProftDataBean> finalResult = null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
                        
            if(from > 0 && to > 0)
            {
                result = stat.executeQuery("select * from signals_table where strategy_id="+strategyID+" and market_type_id="+marketID+" and signal_period="+periodId+" and (signal_type="+CommonDefine.LONG_SIGNAL+" or signal_type="+CommonDefine.SHORT_SIGNAL+" or signal_type="+CommonDefine.CLOSE_SIGNAL+") and generate_date >="+from+" and generate_date <="+to+" order by generate_date ASC;");
            }
            else if(from == 0 && to == 0)
            {
                result = stat.executeQuery("select * from signals_table where strategy_id="+strategyID+" and market_type_id="+marketID+" and signal_period="+periodId+" and (signal_type="+CommonDefine.LONG_SIGNAL+" or signal_type="+CommonDefine.SHORT_SIGNAL+" or signal_type="+CommonDefine.CLOSE_SIGNAL+") order by generate_date ASC;");
            }
            else if(from ==0)
            {
                result = stat.executeQuery("select * from signals_table where strategy_id="+strategyID+" and market_type_id="+marketID+" and signal_period="+periodId+" and (signal_type="+CommonDefine.LONG_SIGNAL+" or signal_type="+CommonDefine.SHORT_SIGNAL+" or signal_type="+CommonDefine.CLOSE_SIGNAL+") and generate_date <="+to+" order by generate_date ASC;");
            }
            else if(to == 0)
            {
                result = stat.executeQuery("select * from signals_table where strategy_id="+strategyID+" and market_type_id="+marketID+" and signal_period="+periodId+" and (signal_type="+CommonDefine.LONG_SIGNAL+" or signal_type="+CommonDefine.SHORT_SIGNAL+" or signal_type="+CommonDefine.CLOSE_SIGNAL+") and generate_date >="+from+" order by generate_date ASC;");
            }
                        
            finalResult = new ArrayList<CumulativeProftDataBean>();
            
            while (result.next())
            {
                CumulativeProftDataBean oBean = new CumulativeProftDataBean();
                
                oBean.set_date((long)result.getDouble("generate_date"));
                oBean.set_value(result.getDouble("profit"));
                
                finalResult.add(oBean);
            }
        }
        catch (Exception e)
        {
            if(finalResult != null)
            {
                finalResult.clear();
            }
            finalResult = null;
            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return finalResult;
    }
              
    
    
    public UserBrokerInfo getUserBrokerFee(UserBean oUserBean)
    {
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        UserBrokerInfo oResult = new UserBrokerInfo();
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            result = stat.executeQuery("select * from user_broker_fee_table where user_id="+oUserBean.getId()+"");
            
            if (result.next())
            {
                oResult.setId(result.getLong("id"));
                oResult.setUser_id(result.getLong("user_id"));
                oResult.setBroker_fee(result.getDouble("broker_fee"));
            }
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return oResult;
    }
    
    public boolean updateUserExpiredDate(UserBean userBean)
    {
        boolean result = false;
        
        Connection conn = null;
        Statement stat = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            stat.execute("update users_table set expired_date="+userBean.getExpired_date()+" where id="+userBean.getId());
            
            result = true;
        }
        catch (Exception e)
        {      
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public boolean setUserStatusForFreeTrialEndUser(int newStatus, int member_level, long expiredTime)
    {
        boolean result = false;
        
        Connection conn = null;
        Statement stat = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            String updateSql = "update users_table set status="+newStatus+" where member_level="+member_level+" and expired_date<="+expiredTime;
            InfoTrace.getInstance().printInfo(DebugLevel.INFO, "Update user free trial ended status SQL: " + updateSql);
            
            stat.execute(updateSql);
            
            result = true;
        }
        catch (Exception e)
        {      
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
        
    public boolean updateUserStatusInfo(UserBean userBean)
    {
        boolean result = false;
        
        Connection conn = null;
        Statement stat = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            stat.execute("update users_table set enable="+userBean.getEnable()+", status="+userBean.getStatus()+", reg_date="+userBean.getReg_date()+", expired_date="+userBean.getExpired_date()+" where id="+userBean.getId());
            
            result = true;
        }
        catch (Exception e)
        {      
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public synchronized boolean removeUserSignalPreferencesAndMarketAnalysis(UserBean oUserBean)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        }   
        
        boolean result = false;
        
        Connection conn = null;
        Statement stat = null;
        boolean startTran = false;
        boolean isCommit=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            conn.setAutoCommit(false);
            startTran = true;
            
            stat.execute("delete from user_signal_preference_table where user_id="+oUserBean.getId());
            
            stat.execute("delete from user_analysis_market_table where user_id="+oUserBean.getId());
                        
            conn.setAutoCommit(true);
            startTran = false;
            
            isCommit=true;   
            result = true;
        }
        catch (Exception e)
        {            
            if(!isCommit && startTran && conn != null)
            {
                try
                {
                    conn.rollback();
                }
                catch(Exception e2)
                {
                    e2.printStackTrace();
                }
            }
            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
        
        
    public synchronized boolean storeUserSignalPreferenceAndMarkets(UserBean userBean, List<MarketTypeAndPeriodBean> marketList)
    {
        if(userBean == null || marketList == null || marketList.size() == 0)
        {
            return false;
        }
        
        boolean storeResult = false;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        boolean startTran = false;
        boolean isCommit=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            conn.setAutoCommit(false);
            startTran = true;
            
            //insert custom signal market and analysis markets
            for(int i=0; i<marketList.size(); i++)
            {
                MarketTypeAndPeriodBean marketTypeAndPeriodBean = marketList.get(i);
                
                long strategyID = 0;
                //get one active strategy id for market id
                result = stat.executeQuery("select A.strategy_id from strategy_market_table A, strategy_table B where A.market_id="+marketTypeAndPeriodBean.getMarketID()+" and A.strategy_id=B.id and B.active="+CommonDefine.ACTIVE_STRATEGY);
                if (result.next())
                {
                    strategyID = result.getLong("strategy_id");
                }
                
                if(result != null)
                {
                    result.close();
                }
                
                String insertUserSignalPreferenceSQL = "insert into user_signal_preference_table (user_id,signal_type,strategy_id,market_type_id,market_period_id,notify_email,notify_sms,enable_email,enable_sms,active) "+
                                                       "values("+userBean.getId()+",0,"+strategyID+","+marketTypeAndPeriodBean.getMarketID()+","+marketTypeAndPeriodBean.getMarketPeriodID()+",'"+userBean.getEmail()+"','"+userBean.getMobile()+"',1,2,1)";
                
                stat.execute(insertUserSignalPreferenceSQL);
                
                String insertUserAnalysisMarketSQL = "insert into user_analysis_market_table (user_id,market_type_id,strategy_id,market_period_id,analysis_from,analysis_to,active) " +
                                                     "values("+userBean.getId()+","+marketTypeAndPeriodBean.getMarketID()+","+marketTypeAndPeriodBean.getStrategyID()+","+marketTypeAndPeriodBean.getMarketPeriodID()+",0,0,1);";
                
                stat.execute(insertUserAnalysisMarketSQL);

            }
            
            conn.setAutoCommit(true);
            startTran = false;
            isCommit=true;                
            storeResult = true;
            
        }
        catch (Exception e)
        {            
            if(!isCommit && startTran && conn != null)
            {
                try
                {
                    conn.rollback();
                }
                catch(Exception e2)
                {
                    e2.printStackTrace();
                }
            }
            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            try
            {
                if(result != null)
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return storeResult;
    }
    
    public UserBean getUserBeanByMobileNumber(String mobileNumber)
    {
        if(null == mobileNumber || "".equals(mobileNumber))
        {
            return null;
        }
        
        String querySql = "select * from users_table where mobile='"+mobileNumber+"' ";
        
        return getUserBySql(querySql);
    }
    
    public UserBean getUserBeanByEmailOrLoginName(String emailAddress)
    {
        if(null == emailAddress || "".equals(emailAddress))
        {
            return null;
        }
        
        String querySql = "select * from users_table where login_name='"+emailAddress+"' or email='"+emailAddress+"' ";
        
        return getUserBySql(querySql);
    }
    
    public UserBean getUserBeanByEmail(String emailAddress)
    {
        if(null == emailAddress || "".equals(emailAddress))
        {
            return null;
        }
        
        String querySql = "select * from users_table where email='"+emailAddress+"' ";
        
        return getUserBySql(querySql);
    }
    
    public boolean storeSchedulerNotification(SchedulerNotificationsBean notification)
    {
        boolean storeResult = false;
        
        Connection conn = null;
        Statement stat = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
                        
            //insert User SQL
            String insertUserSQL = "insert into scheduler_notifications_table (user_id, exe_time, template_type) values("+notification.getUser_id()+","+notification.getTime()+","+notification.getTemplate_type()+")";
            
            stat.execute(insertUserSQL);
            
            storeResult = true;
            
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return storeResult;
    }
    
    public boolean removeAllExpiredSchedulersForUser(long userID)
    {
        boolean removeResult = false;
        
        Connection conn = null;
        Statement stat = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
                        
            //insert User SQL
            String removeUserSQL = "delete from scheduler_notifications_table where user_id = "+userID;
            
            stat.execute(removeUserSQL);
            
            removeResult = true;
            
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return removeResult;
    }
    
    public boolean removeAllExpiredSchedulers(long expiredTime)
    {
        boolean removeResult = false;
        
        Connection conn = null;
        Statement stat = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
                        
            //insert User SQL
            String removeUserSQL = "delete from scheduler_notifications_table where exe_time <= "+expiredTime;
            
            stat.execute(removeUserSQL);
            
            removeResult = true;
            
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return removeResult;
    }
    
    public List<SchedulerNotificationsBean> getAllSchedulerNotification()
    {
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        List<SchedulerNotificationsBean> schedulerNotifications = new ArrayList<SchedulerNotificationsBean>();
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            result = stat.executeQuery("select * from scheduler_notifications_table");
            
            while (result.next())
            {
                SchedulerNotificationsBean schedulerNotification = new SchedulerNotificationsBean();
                
                schedulerNotification.setId(result.getLong("id"));
                schedulerNotification.setUser_id(result.getLong("user_id"));
                schedulerNotification.setTime(result.getLong("exe_time"));
                schedulerNotification.setTemplate_type(result.getInt("template_type"));
                                
                schedulerNotifications.add(schedulerNotification);
            }
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return schedulerNotifications;
    }
    
    public List<UserBean> getUserByMemberLevel(int memberLevel)
    {
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        List<UserBean> userBeanList = new ArrayList<UserBean>();
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            result = stat.executeQuery("select * from users_table where member_level="+memberLevel);
            
            while (result.next())
            {
                UserBean userBean = new UserBean();
                
                userBean.setId(result.getLong("id"));
                userBean.setLogin_name(result.getString("login_name"));
                userBean.setFirst_name(result.getString("first_name"));
                userBean.setLast_name(result.getString("last_name"));
                
                userBean.setMemberType(result.getLong("member_type"));
                userBean.setMemberLevel(result.getLong("member_level"));
                userBean.setPwd(result.getString("pwd"));
                userBean.setEmail(result.getString("email"));
                userBean.setMobile(result.getString("mobile"));
                
                userBean.setCountry_id(result.getLong("country_id"));
                userBean.setSuburb_city(result.getString("suburb_city"));
                userBean.setStreet_address(result.getString("street_addr"));
                userBean.setState(result.getString("state"));
                userBean.setPostCode(result.getString("post_code"));
                userBean.setTrading_experience(result.getLong("trading_exper"));
                userBean.setAge(result.getLong("ages"));
                userBean.setGender(result.getInt("gender"));
                
                userBean.setTime_zone_id(result.getLong("time_zone_id"));
                userBean.setReg_date(result.getLong("reg_date"));
                userBean.setExpired_date(result.getLong("expired_date"));
                userBean.setLast_login(result.getLong("last_login"));
                userBean.setEnable(result.getInt("enable"));
                userBean.setConfirm_code(result.getString("confirm_code"));
                userBean.setSms_credits(result.getLong("sms_credit"));
                userBean.setStatus(result.getInt("status"));
                userBean.setLocked(result.getInt("locked"));
                
                userBeanList.add(userBean);
            }
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return userBeanList;
    }
    
    
        
    
    public TimeZoneBean getUserTimeZone(UserBean oUserBean)
    {
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        TimeZoneBean oResult = new TimeZoneBean();
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            result = stat.executeQuery("select A.zone_id, B.name from trading_time_zones_table A, time_zone_table B  where A.user_id="+oUserBean.getId()+" and A.zone_id=B.id");
            
            if (result.next())
            {
                oResult.setId(result.getLong("zone_id"));
                oResult.setName(result.getString("name"));
            }
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return oResult;
    }
    
    public List<MarketTypeBean> getAllActiveMarkets(UserBean oUserBean)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return new ArrayList<MarketTypeBean>();
        } 
        
        List<MarketTypeBean> finalResult = null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select market_type_id from user_signal_preference_table where user_id="+oUserBean.getId()+" and active="+WebConstants.ENABLE);
            
            finalResult = new ArrayList<MarketTypeBean>();
            
            while (result.next())
            {
                long market_type_id = result.getLong("market_type_id");
                MarketTypeBean oBean = DataCache.getInstance().getMarketTypeByID(new Long(market_type_id));
                
                finalResult.add(oBean);
            }
        }
        catch (Exception e)
        {
            if(finalResult != null)
            {
                finalResult.clear();
            }
            finalResult = null;
            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return finalResult;
    }
    
    public List<QuickLinkBean> getAllQuickLinks(UserBean oUserBean)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return new ArrayList<QuickLinkBean>();
        } 
        
        List<QuickLinkBean> finalResult = null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select * from quick_link_info_table where user_id="+oUserBean.getId());
            
            finalResult = new ArrayList<QuickLinkBean>();
            
            while (result.next())
            {
                QuickLinkBean oBean = new QuickLinkBean();
                
                oBean.setType(result.getInt("link_type"));
                oBean.setIndex(result.getInt("link_index"));
                oBean.setName(result.getString("name"));
                oBean.setUrl(result.getString("url"));
                
                finalResult.add(oBean);
            }
        }
        catch (Exception e)
        {
            if(finalResult != null)
            {
                finalResult.clear();
            }
            finalResult = null;
            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return finalResult;
    }
    
    public List<SignalNotificationBean> getAllSignalNotification(UserBean oUserBean)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return new ArrayList<SignalNotificationBean>();
        } 
        
        //get Data from signal_notification_table tables
        List<SignalNotificationBean> finalResult = null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select * from signal_notification_table where user_id="+oUserBean.getId());
            
            finalResult = new ArrayList<SignalNotificationBean>();
            
            while (result.next())
            {
                SignalNotificationBean oBean = new SignalNotificationBean();
                
                oBean.setEmail(result.getString("email"));
                oBean.setMobile(result.getString("mobile"));
                oBean.setUse_contact(result.getInt("use_contact"));
                
                finalResult.add(oBean);
            }
        }
        catch (Exception e)
        {
            if(finalResult != null)
            {
                finalResult.clear();
            }
            finalResult = null;
            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return finalResult;
    }
    
    
    
    public List<UserSignalPreferenceBaseBean> getUserSignalPreferenceBean(UserBean oUserBean, boolean checkActive)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return new ArrayList<UserSignalPreferenceBaseBean>();
        }        
        
        //get Data from user_signal_preference_table tables
        List<UserSignalPreferenceBaseBean> finalResult = null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            if(checkActive)
            {
                result = stat.executeQuery("select * from user_signal_preference_table where user_id="+oUserBean.getId()+" and active="+WebConstants.ENABLE);
            }
            else
            {
                result = stat.executeQuery("select * from user_signal_preference_table where user_id="+oUserBean.getId());
            }
            
            finalResult = new ArrayList<UserSignalPreferenceBaseBean>();
            
            while (result.next())
            {
                UserSignalPreferenceBaseBean oBean = new UserSignalPreferenceBaseBean();
                
                oBean.setId(result.getLong("id"));
                oBean.setUser_id(oUserBean.getId());
                
                if(WebConstants.ENABLE== result.getInt("active"))
                {
                    oBean.setActive(true);
                }
                else
                {
                    oBean.setActive(false);
                }
                
                if(WebConstants.ENABLE == result.getInt("enable_email"))
                {
                    oBean.setEnable_email(true);
                }
                else
                {
                    oBean.setEnable_email(false);
                }
                
                if(WebConstants.ENABLE == result.getInt("enable_sms"))
                {
                    oBean.setEnable_sms(true);
                }
                else
                {
                    oBean.setEnable_sms(false);
                }
                
                oBean.setStrategy_id(result.getInt("strategy_id"));
                oBean.setMarket_period_id(result.getInt("market_period_id"));
                oBean.setMarket_type_id(result.getInt("market_type_id"));
                oBean.setNotify_email(result.getString("notify_email"));
                oBean.setNotify_sms(result.getString("notify_sms"));
                oBean.setSignal_type(result.getInt("signal_type"));
                
                finalResult.add(oBean);
            }
        }
        catch (Exception e)
        {
            if(finalResult != null)
            {
                finalResult.clear();
            }
            finalResult = null;
            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return finalResult;
        
    }
        
    
    
    
    
    
    
    public boolean updateAnalysisMarketTimeFrame(UserBean oUserBean,String preferenceID,String timePeriodType,String strategyID)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        }
        
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("update user_analysis_market_table set market_period_id="+timePeriodType+",strategy_id="+strategyID+" where user_id="+oUserBean.getId()+" and id="+preferenceID+"");
            result = true;
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public boolean updateMarketStrategy(UserBean oUserBean,String preferenceID,String strategyID)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        }
        
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("update user_signal_preference_table set strategy_id="+strategyID+" where user_id="+oUserBean.getId()+" and id="+preferenceID+"");
            result = true;
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public boolean updateMarketTimeFrame(UserBean oUserBean,String preferenceID,String timePeriodType)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        }
        
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("update user_signal_preference_table set market_period_id="+timePeriodType+" where user_id="+oUserBean.getId()+" and id="+preferenceID+"");
            result = true;
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public boolean updateUserBrokerFee(UserBean oUserBean, double brokerFee)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        }
        
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("update user_broker_fee_table set broker_fee="+brokerFee+" where user_id="+oUserBean.getId());
            result = true;
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    
    
    public boolean updateUserTimeZone(UserBean oUserBean, long timezoneid)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        }
        
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("update trading_time_zones_table set zone_id="+timezoneid+" where user_id="+oUserBean.getId());
            stat.execute("update users_table set time_zone_id="+timezoneid+" where id="+oUserBean.getId());
            result = true;
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public boolean updateUserQuickLinks(UserBean oUserBean, List<QuickLinkBean> oLinksList)
    {
        if(null == oUserBean || oUserBean.getId() < 0 || null == oLinksList)
        {
            return false;
        } 
        
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        boolean isCommit=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            conn.setAutoCommit(false);
            
            //delete all first
            stat.execute("delete from quick_link_info_table where user_id="+oUserBean.getId());
            
            //insert all second
            for(int i=0; i<oLinksList.size(); i++)
            {
                QuickLinkBean oTempLink = (QuickLinkBean)oLinksList.get(i);
                String insertSQL = "insert into quick_link_info_table values("+oUserBean.getId()+","+oTempLink.getType()+","+oTempLink.getIndex()+",'"+oTempLink.getName()+"','"+oTempLink.getUrl()+"')";
                stat.execute(insertSQL);
            }
            conn.setAutoCommit(true);
            isCommit=true;
            result = true;
        }
        catch (Exception e)
        {            
            if(!isCommit && conn != null)
            {
                try
                {
                    conn.rollback();
                }
                catch(Exception e2)
                {
                    e2.printStackTrace();
                }
            }
            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public boolean updateAllSignalNotification(UserBean oUserBean, SignalNotificationBean oSignalAlert)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        }
        
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        boolean isCommit=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            conn.setAutoCommit(false);
            stat.execute("update signal_notification_table set email='"+oSignalAlert.getEmail()+"', mobile='"+oSignalAlert.getMobile()+"', use_contact="+oSignalAlert.getUse_contact()+" where user_id="+oUserBean.getId());
            stat.execute("update user_signal_preference_table set notify_email='"+oSignalAlert.getEmail()+"', notify_sms='"+oSignalAlert.getMobile()+"' where user_id="+oUserBean.getId());
            conn.setAutoCommit(true);
            isCommit=true;
            result = true;
        }
        catch (Exception e)
        {            
            if(!isCommit && conn != null)
            {
                try
                {
                    conn.rollback();
                }
                catch(Exception e2)
                {
                    e2.printStackTrace();
                }
            }
            
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
        
    public boolean removeMarketSubscription(UserSignalPreferenceBaseBean oUserSignalPreference)
    {
        if(oUserSignalPreference == null)
        {
            return false;
        }
        
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("delete from user_signal_preference_table where market_type_id="+oUserSignalPreference.getMarket_type_id()+" and user_id="+oUserSignalPreference.getUser_id()+" and strategy_id="+oUserSignalPreference.getStrategy_id()+" and market_period_id="+oUserSignalPreference.getMarket_period_id());
            
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public boolean addMarketSubscription(UserSignalPreferenceBaseBean oUserSignalPreference)
    {
        if(oUserSignalPreference == null)
        {
            return false;
        }
        
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        /**
            create table user_signal_preference_table
            (
              id                    int unsigned not null primary key,
              user_id               int unsigned not null,
              signal_type           int unsigned not null,  #0:BUY 1:SELL 2:Scalper
              strategy_id           int unsigned not null,
              market_type_id        int unsigned not null, 
              market_period_id      int unsigned not null,
              notify_email          varchar(100) not null,  #default is user preference, but user may custom for different type, such as EUR/USD notify, but others don't
              notify_sms            varchar(100) not null,  #default is user preference, but user may custom for different type   
              enable_email          int unsigned not null,  # 1: enable 2: disable 
              enable_sms            int unsigned not null,  # 1: enable 2: disable 
              active                int default 2           # 1: enable 2: disable  
            );
         */
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            int enable_email = WebConstants.DISABLE;
            if(oUserSignalPreference.getEnable_email())
            {
                enable_email = WebConstants.ENABLE;
            }
            
            int enable_sms = WebConstants.DISABLE;
            if(oUserSignalPreference.getEnable_sms())
            {
                enable_sms = WebConstants.ENABLE;
            }
            
            int active = WebConstants.DISABLE;
            if(oUserSignalPreference.getActive())
            {
                active = WebConstants.ENABLE;
            }
            
            String sqlString = "insert into user_signal_preference_table(user_id,signal_type,strategy_id,market_type_id,market_period_id,notify_email,notify_sms,enable_email,enable_sms,active) values("+
                               oUserSignalPreference.getUser_id()+","+oUserSignalPreference.getSignal_type()+","+oUserSignalPreference.getStrategy_id()+","+oUserSignalPreference.getMarket_type_id()+","+
                               oUserSignalPreference.getMarket_period_id()+",'"+oUserSignalPreference.getNotify_email()+"','"+oUserSignalPreference.getNotify_sms()+"',"+
                               enable_email+","+enable_sms+","+active+")";
            
            //System.out.println(sqlString);
            
            stat.execute(sqlString);
            
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public boolean updateMarketSubscription(UserBean oUserBean,long previousMarketID,long newMarketID,long newSignalPeriodID, long newStrategyID)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        } 
        
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("update user_signal_preference_table set market_type_id="+newMarketID+", strategy_id="+newStrategyID+", market_period_id="+newSignalPeriodID+" where market_type_id="+previousMarketID+" and user_id="+oUserBean.getId());
            
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public boolean removeMySignalSetting(UserBean oUserBean,long mysignalID)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        }
        
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("delete from user_signal_preference_table where user_id="+oUserBean.getId()+" and id="+mysignalID+"");
            
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public boolean updateMySignalEnableSetting(UserBean oUserBean,long mysignalID,int iActiveFlag,int iSmsFlag,int iEmailFlag)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        }   
        
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("update user_signal_preference_table set active="+iActiveFlag+", enable_sms="+iSmsFlag+", enable_email="+iEmailFlag+" where user_id="+oUserBean.getId()+" and id="+mysignalID+"");
            
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public boolean updateUserSignupInfo(UserBean oUserBean)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        }        
        
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("update users_table set first_name='"+oUserBean.getFirst_name()+"', last_name='"+oUserBean.getLast_name()+"', email='"+oUserBean.getEmail()+"', mobile='"+oUserBean.getMobile()+"', country_id="+oUserBean.getCountry_id()+", pwd='"+oUserBean.getPwd()+"', time_zone_id="+oUserBean.getTime_zone_id()+", last_login="+oUserBean.getLast_login()+" where id="+oUserBean.getId()+"");
            
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public boolean updateUserContacts(UserBean oUserBean)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        }        
        
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("update users_table set first_name='"+oUserBean.getFirst_name()+"', last_name='"+oUserBean.getLast_name()+"', email='"+oUserBean.getEmail()+"', suburb_city='"+oUserBean.getSuburb_city()+"', country_id="+oUserBean.getCountry_id()+", mobile='"+oUserBean.getMobile()+"' where id="+oUserBean.getId()+"");
            
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
        
    public boolean updateUserMembershipByUserID(long userID, long memberLevel)
    {
        if(userID < 0)
        {
            return false;
        }
        
        Connection conn = null;
        Statement stat = null;
        boolean result = false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("update users_table set member_level="+memberLevel+" where id="+userID);
            
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }

        return result;
    }
    
    public boolean updateUserResidentialInfo(long userID, String address_city, String address_zip, String address_street, String address_state)
    {
        if(userID < 0)
        {
            return false;
        }
        
        Connection conn = null;
        Statement stat = null;
        boolean result = false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            String updateResidentialInfoSql = "update users_table set suburb_city='"+address_city+"',street_addr='"+address_street+"',state='"+address_state+"',post_code='"+address_zip+"' where id="+userID+"";
            InfoTrace.getInstance().printInfo(DebugLevel.INFO, "Update residential info sql = "+updateResidentialInfoSql);
            
            stat.execute(updateResidentialInfoSql);
            
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }

        return result;
    }
    
    public Signal getLatestSignal(long marketID, long strategyID, long periodID)
    {
        Signal oSignal=new Signal();
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            String sqlString = "select * from signals_table where market_type_id="+marketID+" and strategy_id="+strategyID+" and signal_period="+periodID+" and (signal_type="+CommonDefine.LONG_SIGNAL+" or signal_type="+CommonDefine.SHORT_SIGNAL+" or signal_type="+CommonDefine.CLOSE_SIGNAL+") ORDER BY generate_date desc limit 0,1 ";

            result = stat.executeQuery(sqlString);
            
            if (result.next())
            {
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
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }

        return oSignal;
    }
    
    
    public int getTotalNumberOfActiveSingalByUSer(UserBean oUser)
    {
        if(null == oUser || oUser.getId() < 0)
        {
            return 0;
        }
        
        long currentTime = System.currentTimeMillis();
        int iSum=0;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery(" select count(*) sum "+
                                       " from signals_table "+
                                       " where (signal_type="+CommonDefine.LONG_SIGNAL+" or signal_type="+CommonDefine.SHORT_SIGNAL+" or signal_type="+CommonDefine.CLOSE_SIGNAL+") and expire_date > "+currentTime+"");

            
            if (result.next())
            {
                iSum = result.getInt("sum");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }

        return iSum;
    }
    
    public List<Signal> getPastedSignals(UserBean oUserBean, int totalSignalNum)
    {
        List<Signal> oPastedSignals = new ArrayList<Signal>();
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            String sqlString = " select id, signal_type, market_type_id, strategy_id, signal_period, generate_date, signal_value, expire_date, signal_rate, direction, profit, system_name "+
                               " from signals_table "+
                               " where (signal_type="+CommonDefine.LONG_SIGNAL+" or signal_type="+CommonDefine.SHORT_SIGNAL+" or signal_type="+CommonDefine.CLOSE_SIGNAL+") ORDER BY generate_date desc limit "+0+","+totalSignalNum;
                        
            result = stat.executeQuery(sqlString);
                        
            while (result.next())
            {
                Signal oSignal = new Signal();
                
                oSignal.setId(result.getLong("id"));
                oSignal.setDirection(result.getInt("direction"));
                
                long marketTypeId = result.getLong("market_type_id");  
                //get data from Data cache
                oSignal.setMarket_name(DataCache.getInstance().getMarketTypeByID(marketTypeId).getDisplay_name());
                oSignal.setMarket_topic_name(DataCache.getInstance().getMarketTypeByID(marketTypeId).getMarket_name()); 
                
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
                
                oPastedSignals.add(oSignal);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }

        return oPastedSignals;
    }
    
    public List<Signal> getActiveSignalsByUser(UserBean oUser, int start, int range)
    {
        List<Signal> oAllActiveSignals = new ArrayList<Signal>();
        
        if(null == oUser || oUser.getId() < 0)
        {
            return oAllActiveSignals;
        }
                
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            //to prevent lots of un-used signal data in tempory table in sub-select 
            //limit the latest signals to 40,000 first
            int amountSignalToCheck = 40000;
            
            String sqlQueryString = " select id, signal_type, market_type_id, strategy_id, signal_period, generate_date, signal_value, expire_date, signal_rate, direction, profit, system_name "+
                                    " from (select * from signals_table " + 
                                    " where (signal_type=" + CommonDefine.LONG_SIGNAL+" or signal_type="+CommonDefine.SHORT_SIGNAL+") order by generate_date DESC limit 0,"+amountSignalToCheck+") AS source  group by market_type_id, strategy_id, signal_period ";
            
            if(-1 != range)
            {
                  sqlQueryString = " select id, signal_type, market_type_id, strategy_id, signal_period, generate_date, signal_value, expire_date, signal_rate, direction, profit, system_name "+
                                   " from (select * from signals_table " + 
                                   " where (signal_type=" + CommonDefine.LONG_SIGNAL+" or signal_type="+CommonDefine.SHORT_SIGNAL+") order by generate_date DESC limit 0,"+amountSignalToCheck+") AS source  group by market_type_id, strategy_id, signal_period " + " limit "+start+","+range+"";

            }
            
            result = stat.executeQuery(sqlQueryString);
            
            while (result.next())
            {
                Signal oSignal = new Signal();
                
                oSignal.setId(result.getLong("id"));
                oSignal.setDirection(result.getInt("direction"));
                
                long marketTypeId = result.getLong("market_type_id");      
                
                //get data from Data cache
                oSignal.setMarket_name(DataCache.getInstance().getMarketTypeByID(marketTypeId).getDisplay_name());
                oSignal.setMarket_topic_name(DataCache.getInstance().getMarketTypeByID(marketTypeId).getMarket_name());                
                
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
                
                oAllActiveSignals.add(oSignal);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }

        return oAllActiveSignals;
    }

    public static void main(String[] args)
    {
        try
        {
            //UserBean oUser = DBAccessor.getInstance().getUserInfoByLoginName("test");
            //System.out.println("User email is:"+oUser.getEmail());
            System.out.println(System.currentTimeMillis());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
