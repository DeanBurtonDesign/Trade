package com.cleartraders.common.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.AgeBean;
import com.cleartraders.common.entity.CountryBean;
import com.cleartraders.common.entity.IndicatorBean;
import com.cleartraders.common.entity.MarketPeriodBean;
import com.cleartraders.common.entity.MarketStrategyBean;
import com.cleartraders.common.entity.MarketTypeAndPeriodBean;
import com.cleartraders.common.entity.MarketTypeBean;
import com.cleartraders.common.entity.ProductBean;
import com.cleartraders.common.entity.QuickLinkBean;
import com.cleartraders.common.entity.Signal;
import com.cleartraders.common.entity.SmsPackageBean;
import com.cleartraders.common.entity.StrategyBean;
import com.cleartraders.common.entity.StrategyTimeframeBean;
import com.cleartraders.common.entity.TempPasswordBean;
import com.cleartraders.common.entity.TimeZoneBean;
import com.cleartraders.common.entity.TradingExperienceBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;

public class CommonDBAccessor extends DBBase
{
    public Signal getLatestSignal()
    {
        Signal oSignal=new Signal();
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            String sqlString = "select * from signals_table where (signal_type="+CommonDefine.LONG_SIGNAL+" or signal_type="+CommonDefine.SHORT_SIGNAL+" or signal_type="+CommonDefine.CLOSE_SIGNAL+") ORDER BY generate_date desc limit 0,1 ";

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
    
    public List<Long> getStrategyIDByMarketID(long marketID)
    {
        List<Long> strategyID = new ArrayList<Long>();
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;        
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select A.strategy_id from strategy_market_table A, strategy_table B where A.market_id="+marketID+" and A.strategy_id=B.id and B.active="+CommonDefine.ACTIVE_STRATEGY);
            
            while (result.next())
            {
                strategyID.add(result.getLong("strategy_id"));
            }            
        }
        catch (Exception exp)
        {
            exp.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(exp));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }  
        
        return strategyID;
    }
    
    public synchronized boolean addStrategy(StrategyBean strategyBean, List<Long> marketIDs, List<Long> timeFrameIDs, List<Long> productPlans)
    {
        if(null == strategyBean || null == marketIDs || null == timeFrameIDs || null == productPlans)
            return false;
        
        boolean result = false;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet resultSet = null;
        boolean startTran = false;
        boolean isCommit=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            conn.setAutoCommit(false);
            startTran = true;
            
            long strategyID=0;
            resultSet = stat.executeQuery("select max(id) current_id from strategy_table");
            if(resultSet.next())
            {
                String currentStrategyID = resultSet.getString("current_id");
                if(null != currentStrategyID)
                {
                    strategyID = Long.parseLong(currentStrategyID);
                    strategyID++;
                }
            }
            
            String insertStrategySql = "insert into strategy_table(id,system_name,common_name,description,link_url,active) values("+strategyID+",'"+strategyBean.getSystem_name()+"','"+
            strategyBean.getCommon_name()+"','"+strategyBean.getDescription()+"','"+strategyBean.getLink_url()+"',"+strategyBean.getActive()+")";
                        
            stat.execute(insertStrategySql);
            
            //insert related market
            for(int i=0; i<marketIDs.size(); i++)
            {
                stat.execute("insert into strategy_market_table values("+strategyID+","+marketIDs.get(i)+")");
            }
            
            //insert related timeframe
            for(int i=0; i<timeFrameIDs.size(); i++)
            {
                stat.execute("insert into strategy_timeframe_table values("+strategyID+","+timeFrameIDs.get(i)+")");
            }
            
            //insert related product plans
            for(int i=0; i<productPlans.size(); i++)
            {
                stat.execute("insert into strategy_product_plan_table values("+strategyID+","+productPlans.get(i)+")");
            }
            
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
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            try
            {
                if(null != resultSet)
                {
                    resultSet.close();
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public synchronized boolean updateStrategy(StrategyBean strategyBean, List<Long> marketIDs, List<Long> timeFrameIDs, List<Long> productPlans)
    {
        if(null == strategyBean || null == marketIDs || null == timeFrameIDs || null == productPlans)
            return false;
        
        boolean result = false;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet resultSet = null;
        boolean startTran = false;
        boolean isCommit=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            conn.setAutoCommit(false);
            startTran = true;
                        
            String updateStrategySql = "update strategy_table set system_name='"+strategyBean.getSystem_name()+"', common_name='"+
            strategyBean.getCommon_name()+"', description='"+strategyBean.getDescription()+"', link_url='"+strategyBean.getLink_url()+"', active="+strategyBean.getActive()+" where id="+strategyBean.getId();
                        
            stat.execute(updateStrategySql);
            
            stat.execute("delete from strategy_market_table where strategy_id="+strategyBean.getId());
            
            //insert related market
            for(int i=0; i<marketIDs.size(); i++)
            {
                stat.execute("insert into strategy_market_table values("+strategyBean.getId()+","+marketIDs.get(i)+")");
            }
            
            stat.execute("delete from strategy_timeframe_table where strategy_id="+strategyBean.getId());
            
            //insert related timeframe
            for(int i=0; i<timeFrameIDs.size(); i++)
            {
                stat.execute("insert into strategy_timeframe_table values("+strategyBean.getId()+","+timeFrameIDs.get(i)+")");
            }
            
            stat.execute("delete from strategy_product_plan_table where strategy_id="+strategyBean.getId());
            
            //insert related product plans
            for(int i=0; i<productPlans.size(); i++)
            {
                stat.execute("insert into strategy_product_plan_table values("+strategyBean.getId()+","+productPlans.get(i)+")");
            }
            
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
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            try
            {
                if(null != resultSet)
                {
                    resultSet.close();
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public List<StrategyTimeframeBean> getAllStrategyTimeframeData()
    {
        List<StrategyTimeframeBean> allStrategyTimeframe = new ArrayList<StrategyTimeframeBean>();
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;        
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select * from strategy_timeframe_table");
            
            while (result.next())
            {
                StrategyTimeframeBean oBean = new StrategyTimeframeBean();
                
                oBean.setStrategy_id(result.getLong("strategy_id"));
                oBean.setPeriod_id(result.getLong("period_id"));
                
                allStrategyTimeframe.add(oBean);
            }            
        }
        catch (Exception exp)
        {
            exp.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(exp));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }  
        
        return allStrategyTimeframe;
    }
    
    public List<MarketStrategyBean> getAllMarketStrategyData()
    {
        List<MarketStrategyBean> allMarketStrategy = new ArrayList<MarketStrategyBean>();
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;        
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select * from strategy_market_table");
            
            while (result.next())
            {
                MarketStrategyBean oBean = new MarketStrategyBean();
                
                oBean.setStrategy_id(result.getLong("strategy_id"));
                oBean.setMarket_id(result.getLong("market_id"));
                
                allMarketStrategy.add(oBean);
            }            
        }
        catch (Exception exp)
        {
            exp.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(exp));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }  
        
        return allMarketStrategy;
    }
    
    public StrategyBean getStrategyFullInfoByID(long id)
    {
        StrategyBean strategy = new StrategyBean();
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;        
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select * from strategy_table where id="+id);
            
            if (result.next())
            {
                strategy.setId(result.getLong("id"));
                strategy.setActive(result.getInt("active"));
                strategy.setSystem_name(result.getString("system_name"));
                strategy.setCommon_name(result.getString("common_name"));
                strategy.setDescription(result.getString("description"));
                strategy.setLink_url(result.getString("link_url"));
            }            
            
            //query related markets
            result = stat.executeQuery("select market_id from strategy_market_table where strategy_id="+id+" order by market_id");
            String relatedMarketIDs = "";
            while(result.next())
            {
                relatedMarketIDs += result.getString("market_id");
                relatedMarketIDs += ",";
            }
            strategy.setRelated_markets(relatedMarketIDs);
            
            //query related time frames
            result = stat.executeQuery("select period_id from strategy_timeframe_table where strategy_id="+id+" order by period_id");
            String relatedTimeframes = "";
            while(result.next())
            {
                relatedTimeframes += result.getString("period_id");
                relatedTimeframes += ",";
            }
            strategy.setRelated_timeframes(relatedTimeframes);
            
            //query related product plans 
            result = stat.executeQuery("select product_plan_id from strategy_product_plan_table where strategy_id="+id+" order by product_plan_id");
            String relatedProductPlans = "";
            while(result.next())
            {
                relatedProductPlans += result.getString("product_plan_id");
                relatedProductPlans += ",";
            }
            strategy.setRelated_product_plans(relatedProductPlans);
            
        }
        catch (Exception exp)
        {
            exp.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(exp));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }        
        
        return strategy;
    }
    
    public List<StrategyBean> getAllStrategyBaseInfo()
    {
        List<StrategyBean> allStrategies = new ArrayList<StrategyBean>();
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;        
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select * from strategy_table");
            
            while (result.next())
            {
                StrategyBean oBean = new StrategyBean();
                
                oBean.setId(result.getLong("id"));
                oBean.setActive(result.getInt("active"));
                oBean.setSystem_name(result.getString("system_name"));
                oBean.setCommon_name(result.getString("common_name"));
                oBean.setDescription(result.getString("description"));
                oBean.setLink_url(result.getString("link_url"));
                
                allStrategies.add(oBean);
            }            
        }
        catch (Exception exp)
        {
            exp.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(exp));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }        
        
        return allStrategies;
    }
    
    public HashMap<Long,MarketTypeBean> getAllMarketTypeData()
    {
        HashMap<Long,MarketTypeBean> finalResult = null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select * from market_type_table");
            
            finalResult = new HashMap<Long,MarketTypeBean>();
            
            while (result.next())
            {
                MarketTypeBean oBean = new MarketTypeBean();
                
                oBean.setId(result.getLong("id"));
                oBean.setActive(result.getInt("active"));
                oBean.setDisplay_name(result.getString("display_name"));
                oBean.setMarket_name(result.getString("market_name"));
                oBean.setMarket_type(result.getInt("market_type"));
                oBean.setMarket_type_name(result.getString("market_type_name"));
                oBean.setQuote_decimal(result.getInt("quote_decimal"));
                
                finalResult.put(new Long(result.getLong("id")), oBean);
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
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return finalResult;
    }
    
    public HashMap<Long,MarketPeriodBean> getAllMarketPeriodData()
    {
        HashMap<Long,MarketPeriodBean> finalResult = null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select * from market_period_table");
            
            finalResult = new HashMap<Long,MarketPeriodBean>();
            
            while (result.next())
            {
                MarketPeriodBean oBean = new MarketPeriodBean();
                
                oBean.setId(result.getLong("id"));
                oBean.setPeriod_type(result.getInt("period_type"));
                oBean.setPeriod_name(result.getString("period_name"));
                oBean.setValue(result.getInt("value"));
                
                finalResult.put(new Long(result.getLong("id")), oBean);
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
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return finalResult;
    }
    
    public HashMap<Long,CountryBean> getAllCountryData()
    {
        HashMap<Long,CountryBean> finalResult = null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select * from country_table");
            
            finalResult = new HashMap<Long,CountryBean>();
            
            while (result.next())
            {
                CountryBean oBean = new CountryBean();
                
                oBean.setId(result.getLong("id"));
                oBean.setZone_num(result.getInt("zone_num"));
                oBean.setName(result.getString("name"));
                
                finalResult.put(new Long(result.getLong("id")), oBean);
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
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return finalResult;
    }
    
    public HashMap<Long,TimeZoneBean> getAllTimeZoneData()
    {
        HashMap<Long,TimeZoneBean> finalResult = null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select * from time_zone_table");
            
            finalResult = new HashMap<Long,TimeZoneBean>();
            
            while (result.next())
            {
                TimeZoneBean oBean = new TimeZoneBean();
                
                oBean.setId(result.getLong("id"));
                oBean.setOffset(result.getInt("offset"));
                oBean.setName(result.getString("name"));
                
                finalResult.put(new Long(result.getLong("id")), oBean);
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
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return finalResult;
    }
    
    public HashMap<Long,TradingExperienceBean> getAllTradingExperience()
    {
        HashMap<Long,TradingExperienceBean> allTradingExperience = null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select * from trading_experience_table");
            
            allTradingExperience = new HashMap<Long,TradingExperienceBean>();
            
            while (result.next())
            {
                TradingExperienceBean oBean = new TradingExperienceBean();
                
                oBean.setId(result.getLong("id"));
                oBean.setName(result.getString("name"));
                
                allTradingExperience.put(new Long(result.getLong("id")), oBean);
            }
        }
        catch (Exception e)
        {
            if(allTradingExperience != null)
            {
                allTradingExperience.clear();
            }
            allTradingExperience = null;
            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return allTradingExperience;
    }
    
    public HashMap<Long, AgeBean> getAllAges()
    {
        HashMap<Long,AgeBean> allAges = null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select * from ages_table");
            
            allAges = new HashMap<Long,AgeBean>();
            
            while (result.next())
            {
                AgeBean oBean = new AgeBean();
                
                oBean.setId(result.getLong("id"));
                oBean.setName(result.getString("name"));
                
                allAges.put(new Long(result.getLong("id")), oBean);
            }
        }
        catch (Exception e)
        {
            if(allAges != null)
            {
                allAges.clear();
            }
            allAges = null;
            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return allAges;
    }
    
    public String getProductPaypalButtonID(long productID)
    {
        String buttonID=null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            result = stat.executeQuery("select paypal_btn_id from products_table where id="+productID);
            
            if (result.next())
            {
                buttonID = result.getString("paypal_btn_id");
            }
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return buttonID;
    }
    
    public ProductBean getProductByPrice(double price)
    {
        ProductBean productBean = null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            result = stat.executeQuery("select * from products_table where price="+price);
            
            if (result.next())
            {
                /*
                 *  create table products_table
                    (
                        id              int unsigned not null primary key,
                        name            varchar(100), #product name
                        paid            int unsigned not null, #0: paid   1: free trial
                        period          int unsigned not null, #days
                        total_markets   int unsigned not null, #market numbers
                        price           double not null,# product price US$
                        include_sms     int unsigned not null,
                        active          int unsigned not null default 0 #0: inactive  1: active 
                    );
                 */
                
                productBean = new ProductBean();
                
                productBean.setId(result.getLong("id"));
                productBean.setName(result.getString("name"));
                productBean.setPaypal_btn_id(result.getString("paypal_btn_id"));
                productBean.setPaid(result.getInt("paid"));
                productBean.setPeriod(result.getInt("period"));
                productBean.setTotalMarkets(result.getInt("total_markets"));
                productBean.setPrice(result.getDouble("price"));
                productBean.setIncludeSMS(result.getInt("include_sms"));
                productBean.setActive(result.getInt("active"));
            }
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return productBean;
    }
    
    public ProductBean getProductByID(long productID)
    {
        if(productID < 0)
        {
            return null;
        }
        
        ProductBean productBean = null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            result = stat.executeQuery("select * from products_table where id="+productID);
            
            if (result.next())
            {
                /*
                 *  create table products_table
                    (
                        id              int unsigned not null primary key,
                        name            varchar(100), #product name
                        paid            int unsigned not null, #0: paid   1: free trial
                        period          int unsigned not null, #days
                        total_markets   int unsigned not null, #market numbers
                        price           double not null,# product price US$
                        include_sms     int unsigned not null,
                        active          int unsigned not null default 0 #0: inactive  1: active 
                    );
                 */
                
                productBean = new ProductBean();
                
                productBean.setId(result.getLong("id"));
                productBean.setName(result.getString("name"));
                productBean.setPaypal_btn_id(result.getString("paypal_btn_id"));
                productBean.setPaid(result.getInt("paid"));
                productBean.setPeriod(result.getInt("period"));
                productBean.setTotalMarkets(result.getInt("total_markets"));
                productBean.setPrice(result.getDouble("price"));
                productBean.setIncludeSMS(result.getInt("include_sms"));
                productBean.setActive(result.getInt("active"));
            }
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return productBean;
    }
    
    public HashMap<Long, IndicatorBean> getAllIndicators()
    {
        HashMap<Long, IndicatorBean> allProducts = new HashMap<Long, IndicatorBean>();
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            result = stat.executeQuery("select * from indicator_table");
            
            while (result.next())
            {
                /*
                 * 
                    create table indicator_table
                    (
                      id              int unsigned not null primary key,
                      name              varchar(100) not null,
                      period            int unsigned not null,
                      description       varchar(255) not null
                    );
                    
                 */
                
                IndicatorBean indicatorBean = new IndicatorBean();
                
                indicatorBean.setId(result.getLong("id"));
                indicatorBean.setName(result.getString("name"));
                indicatorBean.setPeriod(result.getInt("period"));
                indicatorBean.setDescription(result.getString("description"));
                indicatorBean.setType(result.getInt("type"));
                
                allProducts.put(Long.valueOf(indicatorBean.getId()), indicatorBean);
            }
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return allProducts;
    }
    
    public HashMap<Long, ProductBean> getAllProduct()
    {
        HashMap<Long, ProductBean> allProducts = new HashMap<Long, ProductBean>();
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            result = stat.executeQuery("select * from products_table");
            
            while (result.next())
            {
                /*
                 *  create table products_table
                    (
                        id              int unsigned not null primary key,
                        name            varchar(100), #product name
                        paid            int unsigned not null, #0: paid   1: free trial
                        period          int unsigned not null, #days
                        total_markets   int unsigned not null, #market numbers
                        price           double not null,# product price US$
                        include_sms     int unsigned not null,
                        active          int unsigned not null default 0 #0: inactive  1: active 
                    );
                 */
                
                ProductBean productBean = new ProductBean();
                
                productBean.setId(result.getLong("id"));
                productBean.setName(result.getString("name"));
                productBean.setPaypal_btn_id(result.getString("paypal_btn_id"));
                productBean.setPaid(result.getInt("paid"));
                productBean.setPeriod(result.getInt("period"));
                productBean.setTotalMarkets(result.getInt("total_markets"));
                productBean.setPrice(result.getDouble("price"));
                productBean.setIncludeSMS(result.getInt("include_sms"));
                productBean.setActive(result.getInt("active"));
                
                allProducts.put(Long.valueOf(productBean.getId()), productBean);
            }
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return allProducts;
    }
    
    public List<UserBean> searchMembers(String memberName, String memberEmail, int memberType, boolean isAllMemberType)
    {
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        List<UserBean> standardMemberList = new ArrayList<UserBean>();
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            String searchSql = "select * from users_table where (first_name like '%"+memberName+"%' or last_name like '%"+memberName+"%' or email like '%"+memberEmail+"%')";
            
            if(memberName.length() == 0 && memberEmail.length() != 0)
            {
                searchSql = "select * from users_table where email like '%"+memberEmail+"%' ";
            }
            else if(memberName.length() != 0 && memberEmail.length() == 0)
            {
                searchSql = "select * from users_table where first_name like '%"+memberName+"%' or last_name like '%"+memberName+"%'";
            }
            
            if(!isAllMemberType)
            {
                searchSql += " and member_type="+memberType+"";
            }
            
            result = stat.executeQuery(searchSql);
            
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
                
                standardMemberList.add(userBean);
            }
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return standardMemberList;
    }
    
    public SmsPackageBean getSMSPackageByID(long smsPackageID)
    {
        SmsPackageBean smsPackageBean = null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            result = stat.executeQuery("select * from sms_package_table where id="+smsPackageID);
            
            if (result.next())
            {
                smsPackageBean = new SmsPackageBean();
                
                smsPackageBean.setId(result.getLong("id"));
                smsPackageBean.setProduct_id(result.getLong("product_id"));
                smsPackageBean.setName(result.getString("name"));
                smsPackageBean.setSms_included(result.getInt("sms_included"));
                smsPackageBean.setSms_cost(result.getDouble("sms_cost"));
                smsPackageBean.setPaypal_button_id(result.getString("name"));
            }
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return smsPackageBean;
    }
    
    public SmsPackageBean getSMSPackageByPrice(double price, String btnID)
    {
        SmsPackageBean smsPackageBean = null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            result = stat.executeQuery("select * from sms_package_table where sms_cost="+price+" and paypal_btn_id='"+btnID+"'");
            
            if (result.next())
            {
                smsPackageBean = new SmsPackageBean();
                
                smsPackageBean.setId(result.getLong("id"));
                smsPackageBean.setProduct_id(result.getLong("product_id"));
                smsPackageBean.setName(result.getString("name"));
                smsPackageBean.setSms_included(result.getInt("sms_included"));
                smsPackageBean.setSms_cost(result.getDouble("sms_cost"));
                smsPackageBean.setPaypal_button_id(result.getString("paypal_btn_id"));
            }
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return smsPackageBean;
    }
    
    public List<SmsPackageBean> getAllSMSPackages(long productID)
    {
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        List<SmsPackageBean> allSmsPackages = new ArrayList<SmsPackageBean>();
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            String sqlString = "select * from sms_package_table where product_id="+productID;
                        
            result = stat.executeQuery(sqlString);
            
            while (result.next())
            {
                SmsPackageBean smsPackageBean = new SmsPackageBean();
                smsPackageBean.setId(result.getLong("id"));
                smsPackageBean.setProduct_id(result.getLong("product_id"));
                smsPackageBean.setName(result.getString("name"));
                smsPackageBean.setPaypal_button_id(result.getString("paypal_btn_id"));
                smsPackageBean.setSms_included(result.getInt("sms_included"));
                smsPackageBean.setSms_cost(result.getDouble("sms_cost"));
                
                allSmsPackages.add(smsPackageBean);
            }
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return allSmsPackages;
    }
    
    public List<SmsPackageBean> getAllSMSPackages()
    {
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        List<SmsPackageBean> allSmsPackages = new ArrayList<SmsPackageBean>();
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            String sqlString = "select * from sms_package_table";
                        
            result = stat.executeQuery(sqlString);
            
            while (result.next())
            {
                SmsPackageBean smsPackageBean = new SmsPackageBean();
                smsPackageBean.setId(result.getLong("id"));
                smsPackageBean.setProduct_id(result.getLong("product_id"));
                smsPackageBean.setName(result.getString("name"));
                smsPackageBean.setPaypal_button_id(result.getString("paypal_btn_id"));
                smsPackageBean.setSms_included(result.getInt("sms_included"));
                smsPackageBean.setSms_cost(result.getDouble("sms_cost"));
                
                allSmsPackages.add(smsPackageBean);
            }
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return allSmsPackages;
    }
    
    public synchronized boolean removeUserByID(long userID)
    {
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
            
            stat.execute("delete from users_table where id="+userID);
            
            stat.execute("delete from signal_notification_table where user_id="+userID);
            
            stat.execute("delete from trading_time_zones_table where user_id="+userID);
            
            stat.execute("delete from user_broker_fee_table where user_id="+userID);
            
            stat.execute("delete from user_signal_preference_table where user_id="+userID);
            
            stat.execute("delete from user_analysis_market_table where user_id="+userID);
            
            stat.execute("delete from scheduler_notifications_table where user_id="+userID);
            
            stat.execute("delete from payment_history_table where user_id="+userID);
            
            stat.execute("delete from temp_password_table where user_id="+userID);
            
            stat.execute("delete from quick_link_info_table where user_id="+userID);
                        
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
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public boolean addSMSCreditsToUser(long userId, long smsCredits)
    {
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("update users_table set sms_credit=sms_credit+"+smsCredits+" where id="+userId);
            result = true;
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public UserBean getUserInfoByID(long userID)
    {        
        String querySql = "select * from users_table where id="+userID;
        
        return getUserBySql(querySql);
    }
    
    public UserBean getUserInfoByLoginName(String userName)
    {
        if(null == userName)
        {
            return null;
        }
        
        String querySql = "select * from users_table where login_name='"+userName+"'";
        
        return getUserBySql(querySql);
    }
    
    public UserBean getUserBySql(String sqlString)
    {
        if(null == sqlString || sqlString.trim().length() == 0)
        {
            return null;
        }
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        UserBean userBean = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            result = stat.executeQuery(sqlString);
            
            if (result.next())
            {
                userBean = new UserBean();
                
                /*
                 *  #field of user table is based on register info
                    drop table if exists users_table;
                    create table users_table
                    (
                        id                int unsigned not null primary key,
                        login_name      char(100) not null ,#Can't be duplicated
                        first_name      char(100) not null,
                        last_name       char(100) not null,
                        member_level    int unsigned not null default 0, #0:Trial  1:Single Market 2:Multiple Market 3:Pro All Market 4:Administrator
                        pwd                 varchar(100) not null,
                        email               varchar(100),
                        mobile              varchar(100),
                        country_id      int unsigned not null, #its value is stored in country table
                        suburb_city     varchar(100),
                        street_addr     varchar(100),
                        state           varchar(100),
                        post_code       varchar(100),
                        trading_exper   int unsigned not null, #experience id
                        ages            int unsigned not null, #age id
                        gender          int unsigned not null default 0, #0: Male  1:Female
                        time_zone_id    int unsigned not null, #its time zone is stored in time_zone table
                        reg_date            double not null,
                        expired_date    double not null,
                        last_login          double not null,
                        enable          int unsigned not null default 0, #0: Not confirmed  1:confirmed  
                        confirm_code    varchar(100),
                        locked              int unsigned not null  #0: Non-locked  1: locked
                    );
                 */
                
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
            }
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return userBean;
    }
    
    public List<UserBean> getAllCancelledMembers()
    {
        String sqlString = "select *  from users_table where status="+CommonDefine.CANCELLED_USER;
        
        return getUserListBySql(sqlString);
    }
    
    public List<UserBean> getAllRegisteredMember()
    {
        String sqlString = "select * from users_table where status="+CommonDefine.REGISTERED_USER;
        
        return getUserListBySql(sqlString);
    }
    
    public List<UserBean> getAllUnregisteredMember()
    {
        String sqlString = "select * from users_table where status="+CommonDefine.UNREGISTERED_USER;
        
        return getUserListBySql(sqlString);
    }
    
    public List<UserBean> getAllMembersByType(int memberType, boolean isAllMemberType)
    {
        String sqlString = "select * from users_table where member_type="+memberType;
        
        if(isAllMemberType)
        {
            sqlString = "select * from users_table";
        }
        
        return getUserListBySql(sqlString);
    }
    
    public List<UserBean> getUserListBySql(String sqlString)
    {
        if(null == sqlString || sqlString.trim().length() == 0)
        {
            return new ArrayList<UserBean>();
        }
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        List<UserBean> standardMemberList = new ArrayList<UserBean>();
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            result = stat.executeQuery(sqlString);
            
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
                
                standardMemberList.add(userBean);
            }
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return standardMemberList;
    }
    
    public TempPasswordBean getFindPasswordConfirmCode(long userID)
    {
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        TempPasswordBean tempPassword = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            result = stat.executeQuery("select * from temp_password_table where user_id="+userID);
            
            if (result.next())
            {
                tempPassword =new TempPasswordBean();
                
                tempPassword.setUser_id(result.getLong("user_id"));
                tempPassword.setTemp_pwd(result.getString("temp_pwd"));
                tempPassword.setConfirm_code(result.getString("confirm_code"));
            }
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return tempPassword;
    }
    
    public boolean insertUserTempPWD(long userID, String newPassword, String confirmCode)
    {
        if(userID < 0 || null == newPassword || "".equals(newPassword) || null == confirmCode || "".equals(confirmCode))
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
            
            stat.execute("delete from temp_password_table where user_id="+userID);
            stat.execute("insert into temp_password_table values("+userID+",'"+newPassword+"','"+confirmCode+"')");
            
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }

        return result;
    }
    
    public boolean updateUserPWDByTempPWD(long userID, TempPasswordBean tempPasswordBean)
    {
        if( userID < 0 )
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
            
            stat.execute("delete from temp_password_table where user_id="+userID);
            
            stat.execute("update users_table set pwd='"+tempPasswordBean.getTemp_pwd()+"' where id="+userID);
            
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }

        return result;
    }
    
    public boolean updateUserPWDByUserID(long userID, String newPassword)
    {
        if(userID < 0 || null == newPassword || "".equals(newPassword))
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
            stat.execute("update users_table set pwd='"+newPassword+"' where id="+userID+"");
            
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }

        return result;
    }
    
    public List<MarketTypeAndPeriodBean> getMemberSubcribleMarketStrategyList(long memberID)
    {
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        List<MarketTypeAndPeriodBean> marketStrategyList = new ArrayList<MarketTypeAndPeriodBean>();
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            result = stat.executeQuery("select strategy_id,market_type_id,market_period_id,active from user_signal_preference_table where user_id="+memberID);
            
            while (result.next())
            {
                MarketTypeAndPeriodBean oMarketStrategy = new MarketTypeAndPeriodBean();
                oMarketStrategy.setStrategyID(result.getLong("strategy_id"));
                oMarketStrategy.setMarketID(result.getLong("market_type_id"));
                oMarketStrategy.setMarketPeriodID(result.getLong("market_period_id"));
                
                int activeStatus = result.getInt("active");
                    
                if( activeStatus == CommonDefine.INACTIVE_PREFERENCE )
                {
                    oMarketStrategy.setActive(false);
                }
                else
                {
                    oMarketStrategy.setActive(true);
                }
                
                marketStrategyList.add(oMarketStrategy);
            }
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return marketStrategyList;
    }
    
    public List<Long> getMemberSubcribleMarketIDList(long memberID)
    {
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        List<Long> marketIDList = new ArrayList<Long>();
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            result = stat.executeQuery("select market_type_id from user_signal_preference_table where user_id="+memberID);
            
            while (result.next())
            {
                marketIDList.add(Long.valueOf(result.getLong("market_type_id")));
            }
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return marketIDList;
    }
    
    public long getUserSmsCredits(long userID)
    {        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        long smsCredits = 0;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            result = stat.executeQuery("select sms_credit from users_table where id="+userID);
            
            if (result.next())
            {
                smsCredits = result.getLong("sms_credit");
            }
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return smsCredits;
    }
    
        
    public boolean updateUserLastLogin(UserBean oUserBean, long lastLogin)
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
            stat.execute("update users_table set last_login="+lastLogin+" where id="+oUserBean.getId());
            result = true;
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    public boolean addSMSPackage(SmsPackageBean smsPackageBean)
    {
        boolean storeResult = false;
        
        Connection conn = null;
        Statement stat = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
                        
            String insertSMSPackageSQL = "insert into sms_package_table (product_id, name, sms_included, paypal_btn_id, sms_cost) values("+smsPackageBean.getProduct_id()+",'"+smsPackageBean.getName()+"',"+smsPackageBean.getSms_included()+",'"+smsPackageBean.getPaypal_button_id()+"',"+smsPackageBean.getSms_cost()+")";
            stat.execute(insertSMSPackageSQL);
                        
            //don't need update product Included-SMS
//            String updateProductSMS = "update products_table set include_sms = " + smsPackageBean.getSms_included()+" where id = "+smsPackageBean.getProduct_id();
//            stat.execute(updateProductSMS);
            
            storeResult = true;
            
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return storeResult;
    }
    
    public boolean updateSMSPackage(SmsPackageBean smsPackageBean)
    {
        boolean updateResult = false;
        
        Connection conn = null;
        Statement stat = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
                        
            String updateSMSPackageSQL = "update sms_package_table set ";
            updateSMSPackageSQL += " name='"+smsPackageBean.getName()+"', ";
            updateSMSPackageSQL += " paypal_btn_id='"+smsPackageBean.getPaypal_button_id()+"', ";
            updateSMSPackageSQL += " product_id="+smsPackageBean.getProduct_id()+", ";
            updateSMSPackageSQL += " sms_included="+smsPackageBean.getSms_included()+", ";
            updateSMSPackageSQL += " sms_cost="+smsPackageBean.getSms_cost();
            updateSMSPackageSQL += " where id="+smsPackageBean.getId();
            
            stat.execute(updateSMSPackageSQL);
            
            //don't need update product Included-SMS
//            long newTotalSMS = 0;
//            String getTotalSMSSql = "select sum(sms_included) sms_included from sms_package_table where product_id="+smsPackageBean.getProduct_id()+";";
//            ResultSet totalSmsResult = stat.executeQuery(getTotalSMSSql);
//            if (totalSmsResult.next())
//            {
//                newTotalSMS = totalSmsResult.getLong("sms_included");
//            }
//            
//            String updateProductSQL = "update products_table set include_sms = "+newTotalSMS+" where id = "+smsPackageBean.getProduct_id();            
//            stat.execute(updateProductSQL);
            
            updateResult = true;
            
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return updateResult;
    }
    
    public String getSMSPackageButtonByID(long smsPackageID)
    {
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        String buttonID = "";
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            result = stat.executeQuery("select paypal_btn_id from sms_package_table where id="+smsPackageID);
            
            if (result.next())
            {
                buttonID = result.getString("paypal_btn_id");
            }
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return buttonID;
    }
    
    
    public boolean deleteSMSPackageByID(long smsPackageID)
    {
        Connection conn = null;
        Statement stat = null;
        boolean result=false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            stat.execute("delete from sms_package_table where id="+smsPackageID);
            result = true;
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
    
    
    public boolean updateProduct(ProductBean oProduct)
    {
        boolean updateResult = false;
        
        Connection conn = null;
        Statement stat = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
                        
            String updateSQL = "update products_table set ";
            updateSQL += " name='"+oProduct.getName()+"', ";
            updateSQL += " paypal_btn_id='"+oProduct.getPaypal_btn_id()+"', ";
            updateSQL += " paid="+oProduct.getPaid()+", ";
            updateSQL += " period="+oProduct.getPeriod()+", ";
            updateSQL += " total_markets="+oProduct.getTotalMarkets()+", ";
            updateSQL += " price="+oProduct.getPrice()+", ";
            updateSQL += " include_sms="+oProduct.getIncludeSMS()+", ";
            updateSQL += " active="+oProduct.getActive()+" ";
            updateSQL += " where id="+oProduct.getId();
            
            stat.execute(updateSQL);
            
            updateResult = true;
            
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return updateResult;
    }
    
    public boolean addProduct(ProductBean oProduct)
    {
        boolean storeResult = false;
        
        Connection conn = null;
        Statement stat = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
                        
            String insertSQL = "insert into products_table (name, paid, period, total_markets, price, include_sms, paypal_btn_id, active) values('"+oProduct.getName()+"',"+oProduct.getPaid()+","+oProduct.getPeriod()+","+oProduct.getTotalMarkets()+","+oProduct.getPrice()+","+oProduct.getIncludeSMS()+",'"+oProduct.getPaypal_btn_id()+"',"+oProduct.getActive()+")";
            
            stat.execute(insertSQL);
            
            storeResult = true;
            
        }
        catch (Exception e)
        {            
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return storeResult;
    }
    
    public synchronized boolean storeUserIntoDB(UserBean userBean, List<MarketTypeAndPeriodBean> marketList, List<QuickLinkBean> defaultQuickLink)
    {
        if(userBean == null || "".equals(userBean.getLogin_name()) || "".equals(userBean.getEmail()) || marketList == null )
        {
            return false;
        }
        
        boolean storeResult = false;
        
        Connection conn = null;
        Statement stat = null;
        boolean startTran = false;
        boolean isCommit=false;
        long currentUserID = 0;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            
            //insert User SQL
            String insertUserSQL = "insert into users_table (login_name, first_name, last_name,member_type, member_level, pwd, email, mobile, country_id, suburb_city, "+
                                   "street_addr, state, post_code, trading_exper, ages, gender, time_zone_id, reg_date, expired_date, last_login, enable, confirm_code, sms_credit, status, locked) values('"+
                                   userBean.getLogin_name()+"','"+userBean.getFirst_name()+"','"+userBean.getLast_name()+"',"+userBean.getMemberType()+","+userBean.getMemberLevel()+",'"+userBean.getPwd()+"','"+
                                   userBean.getEmail()+"','"+userBean.getMobile()+"',"+userBean.getCountry_id()+",'"+userBean.getSuburb_city()+"','"+userBean.getStreet_address()+"','"+
                                   userBean.getState()+"','"+userBean.getPostCode()+"',"+userBean.getTrading_experience()+","+userBean.getAge()+","+userBean.getGender()+","+userBean.getTime_zone_id()+","+
                                   userBean.getReg_date()+","+userBean.getExpired_date()+","+userBean.getLast_login()+","+userBean.getEnable()+",'"+userBean.getConfirm_code()+"',"+userBean.getSms_credits()+","+userBean.getStatus()+","+userBean.getLocked()+")";
            
            stat.execute(insertUserSQL);
            
            UserBean oCurrentUserBean = getUserInfoByLoginName(userBean.getLogin_name());
            currentUserID = oCurrentUserBean.getId();
            
            if(oCurrentUserBean != null && currentUserID > 0)
            {
                conn.setAutoCommit(false);
                startTran = true;
                
                //insert Signal Notification
                String insertSignalNotificationSQL = "insert into signal_notification_table values("+currentUserID+",'"+oCurrentUserBean.getEmail()+"','"+oCurrentUserBean.getMobile()+"',1)";
                stat.execute(insertSignalNotificationSQL);
                
                //insert trading time zones 
                String insertTradingTimeZoneSQL = "insert into trading_time_zones_table values("+currentUserID+","+oCurrentUserBean.getTime_zone_id()+")";
                stat.execute(insertTradingTimeZoneSQL);
                
                //insert user_broker_fee_table
                String insertBrokerFee = "insert into user_broker_fee_table (user_id,broker_fee) values("+currentUserID+","+CommonDefine.DEFAULT_BROKER_FEE+")";
                stat.execute(insertBrokerFee);
                
                for(int i=0; i<defaultQuickLink.size(); i++)
                {
                    QuickLinkBean oQuickLinkBean = defaultQuickLink.get(i);
                    
                    String insertQuickBeanSql = "insert into quick_link_info_table (user_id, link_type, link_index, name, url) values("+currentUserID+","+oQuickLinkBean.getType()+","+oQuickLinkBean.getIndex()+",'"+oQuickLinkBean.getName()+"','"+oQuickLinkBean.getUrl()+"')";
                    
                    stat.execute(insertQuickBeanSql);
                }
                
                //insert custom signal market and analysis markets
                for(int i=0; i<marketList.size(); i++)
                {
                    MarketTypeAndPeriodBean marketTypeAndPeriodBean = marketList.get(i);
                    int activeStatus = CommonDefine.INACTIVE_PREFERENCE;
                    if(marketTypeAndPeriodBean.isActive())
                    {
                        activeStatus = CommonDefine.ACTIVE_PREFERENCE;
                    }
                    
                    String insertUserSignalPreferenceSQL = "insert into user_signal_preference_table (user_id,signal_type,strategy_id,market_type_id,market_period_id,notify_email,notify_sms,enable_email,enable_sms,active) "+
                                                           "values("+currentUserID+",0,"+marketTypeAndPeriodBean.getStrategyID()+","+marketTypeAndPeriodBean.getMarketID()+","+marketTypeAndPeriodBean.getMarketPeriodID()+",'"+userBean.getEmail()+"','"+userBean.getMobile()+"',1,2,"+activeStatus+")";
                    
                    stat.execute(insertUserSignalPreferenceSQL);
                    
                    String insertUserAnalysisMarketSQL = "insert into user_analysis_market_table (user_id,market_type_id,strategy_id,market_period_id,analysis_from,analysis_to,active) " +
                                                         "values("+currentUserID+","+marketTypeAndPeriodBean.getMarketID()+","+marketTypeAndPeriodBean.getStrategyID()+","+marketTypeAndPeriodBean.getMarketPeriodID()+",0,0,"+activeStatus+");";
                    
                    stat.execute(insertUserAnalysisMarketSQL);

                }
                
                conn.setAutoCommit(true);
                startTran = false;
                isCommit=true;                
                storeResult = true;
            }
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
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return storeResult;
    }
        
    public boolean updateMemberDetails(UserBean userBean, List<MarketTypeAndPeriodBean> marketList)
    {
        if(null == userBean)
        {
            return false;
        }
        
        Connection conn = null;
        Statement stat = null;
        boolean startTran = false;
        boolean isCommit=false;
        boolean result = false;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            
            conn.setAutoCommit(false);
            startTran = true;
            
            //update users_table;
            String updateUserInfoSql = "update users_table set";            
            updateUserInfoSql += " first_name='"+userBean.getFirst_name()+"'";
            updateUserInfoSql += ",last_name='"+userBean.getLast_name()+"'";
            updateUserInfoSql += ",member_type="+userBean.getMemberType();
            updateUserInfoSql += ",member_level="+userBean.getMemberLevel();
            updateUserInfoSql += ",email='"+userBean.getEmail()+"'";
            updateUserInfoSql += ",mobile='"+userBean.getMobile()+"'";
            updateUserInfoSql += ",country_id="+userBean.getCountry_id();
            updateUserInfoSql += ",time_zone_id="+userBean.getTime_zone_id();
            updateUserInfoSql += ",ages="+userBean.getAge();
            updateUserInfoSql += ",gender="+userBean.getGender();
            updateUserInfoSql += ",reg_date="+userBean.getReg_date();   
            updateUserInfoSql += ",expired_date="+userBean.getExpired_date();
            updateUserInfoSql += ",sms_credit="+userBean.getSms_credits();
            updateUserInfoSql += ",status="+userBean.getStatus();
            updateUserInfoSql += " where id="+userBean.getId();
            
            stat.execute(updateUserInfoSql);
                        
            //update user_signal_preference_table and user_analysis_market_table;
            String removeSignalPreference = "delete from user_signal_preference_table where user_id="+userBean.getId();
            String removeAnalysisMarkets = "delete from user_analysis_market_table where user_id="+userBean.getId();
            
            stat.execute(removeSignalPreference);
            stat.execute(removeAnalysisMarkets);
            
            
            //insert custom signal market and analysis markets
            for(int i=0; i<marketList.size(); i++)
            {
                MarketTypeAndPeriodBean marketTypeAndPeriodBean = marketList.get(i);
                int activeStatus = CommonDefine.INACTIVE_PREFERENCE;
                if(marketTypeAndPeriodBean.isActive())
                {
                    activeStatus = CommonDefine.ACTIVE_PREFERENCE;
                }
                
                String insertUserSignalPreferenceSQL = "insert into user_signal_preference_table (user_id,signal_type,strategy_id,market_type_id,market_period_id,notify_email,notify_sms,enable_email,enable_sms,active) "+
                                                       "values("+userBean.getId()+",0,"+marketTypeAndPeriodBean.getStrategyID()+","+marketTypeAndPeriodBean.getMarketID()+","+marketTypeAndPeriodBean.getMarketPeriodID()+",'"+userBean.getEmail()+"','"+userBean.getMobile()+"',1,2,"+activeStatus+")";
                
                stat.execute(insertUserSignalPreferenceSQL);
                
                String insertUserAnalysisMarketSQL = "insert into user_analysis_market_table (user_id,market_type_id,strategy_id,market_period_id,analysis_from,analysis_to,active) " +
                                                     "values("+userBean.getId()+","+marketTypeAndPeriodBean.getMarketID()+","+marketTypeAndPeriodBean.getStrategyID()+","+marketTypeAndPeriodBean.getMarketPeriodID()+",0,0,"+activeStatus+")";
                
                stat.execute(insertUserAnalysisMarketSQL);

            }
            
            conn.setAutoCommit(true);
            startTran = false;
            isCommit=true;        
            result = true;
        }
        catch (Exception e)
        {      
            e.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            
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
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }
        
        return result;
    }
}
