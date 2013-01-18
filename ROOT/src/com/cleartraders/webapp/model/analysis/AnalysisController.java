package com.cleartraders.webapp.model.analysis;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Month;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;

import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.MarketPeriodBean;
import com.cleartraders.common.entity.Signal;
import com.cleartraders.common.entity.StrategyBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.db.DBAccessor;
import com.cleartraders.webapp.model.bean.CumulativeProftDataBean;
import com.cleartraders.webapp.model.bean.DateStringComparator;
import com.cleartraders.webapp.model.bean.TradeBean;
import com.cleartraders.webapp.model.bean.TradingStatisticsResultBean;
import com.cleartraders.webapp.model.bean.UserAnalysisMarketBean;
import com.cleartraders.webapp.model.bean.UserBrokerInfo;

public class AnalysisController
{
    public boolean updateAnalysisMarketTimeFrame(UserBean oUserBean,String preferenceID,String timePeriodType, String strategyID)
    {
        return DBAccessor.getInstance().updateAnalysisMarketTimeFrame(oUserBean,preferenceID,timePeriodType,strategyID);
    }
        
    public TradingStatisticsResultBean getTradingStatisticsResult(UserBean oUserBean, String strategyIDList, String marketIdList,String periodIdList, long fromDate, long toDate)
    {
        double trade_success = 0;    
        double total_profit_or_loss_points = 0;
        double average_trade_points = 0;
        long total_trades = 0;
        long winning_trades = 0;
        double average_winning_trade_points = 0;
        long losing_trades = 0;
        double average_losing_trade_points = 0;
        long long_signals=0;
        long short_signals=0;
        long close_signals=0;
        
        TradingStatisticsResultBean oResult = new TradingStatisticsResultBean();
        
        //get total long signals
        long_signals = getAllSignals(CommonDefine.LONG_SIGNAL,strategyIDList, marketIdList,periodIdList,fromDate,toDate);
        short_signals = getAllSignals(CommonDefine.SHORT_SIGNAL,strategyIDList, marketIdList,periodIdList,fromDate,toDate);
        close_signals = getAllSignals(CommonDefine.CLOSE_SIGNAL,strategyIDList, marketIdList,periodIdList,fromDate,toDate);
        
        //get all signals by market id list and period id list and from date and end date        
        List<CumulativeProftDataBean> oCumulativeProftDataList = getProiftByMarketPeriodAndDate(strategyIDList, marketIdList,periodIdList,fromDate,toDate);
        
        if(oCumulativeProftDataList == null || oCumulativeProftDataList.size() < 1)
        {
            return oResult;
        }
        
        double winTradePoints = 0;
        double lossTradePoints = 0;
        for(int i=0; i<oCumulativeProftDataList.size(); i++)
        {
            CumulativeProftDataBean oTemp = oCumulativeProftDataList.get(i);
            
            //if value is 0, that means this is only signal not trade
            if(oTemp.get_value() == 0){continue;}
            
            total_trades++;
            
            BigDecimal bCurrentTradeProfit = new BigDecimal(String.valueOf(oTemp.get_value()));
            
            if(oTemp.get_value() > 0)
            {
                winning_trades++;
                
                BigDecimal bWinAmount = new BigDecimal(String.valueOf(winTradePoints)); 
                
                winTradePoints = bWinAmount.add(bCurrentTradeProfit).doubleValue();
            }
            else
            {
                losing_trades++;
                
                BigDecimal bLossAmount = new BigDecimal(String.valueOf(lossTradePoints)); 
                
                lossTradePoints = bLossAmount.add(bCurrentTradeProfit).doubleValue();
            }
        }
        
        if(0 == winning_trades)
        {
            average_winning_trade_points = 0;      
        }
        else
        {
            average_winning_trade_points = winTradePoints/winning_trades;      
        }
          
        if(0 == losing_trades)
        {
            average_losing_trade_points = 0;
        }
        else
        {
            average_losing_trade_points = lossTradePoints/losing_trades;
        }
        
        if(0 == total_trades)
        {
            trade_success = 0;
        }
        else
        {
            trade_success = (double)winning_trades/(double)total_trades;
        }
        
        total_profit_or_loss_points = (new BigDecimal(String.valueOf(winTradePoints))).add(new BigDecimal(String.valueOf(lossTradePoints))).doubleValue();
        
        if(0 == total_trades)
        {
            average_trade_points = 0;
        }
        else
        {
            average_trade_points = total_profit_or_loss_points/total_trades;
        }        
        
        oResult.setTrade_success(trade_success);
        oResult.setTotal_profit_or_loss_points(total_profit_or_loss_points);
        oResult.setAverage_trade_points(average_trade_points);
        oResult.setTotal_trades(total_trades);
        oResult.setWinning_trades(winning_trades);
        oResult.setAverage_winning_trade_points(average_winning_trade_points);
        oResult.setLosing_trades(losing_trades);
        oResult.setAverage_losing_trade_points(average_losing_trade_points);
        oResult.setTotal_signals(oCumulativeProftDataList.size());
        oResult.setLong_signals(long_signals);
        oResult.setShort_signals(short_signals);
        oResult.setClose_signals(close_signals);
        
        //set user broker fee points        
        UserBrokerInfo oBrokeInfo = DBAccessor.getInstance().getUserBrokerFee(oUserBean);
        
        BigDecimal bBrokerFee = new BigDecimal(String.valueOf(oBrokeInfo.getBroker_fee())); 
        BigDecimal bTotoalSinals = new BigDecimal(String.valueOf(oCumulativeProftDataList.size())); 
        
        oResult.setTotal_broker_points(bBrokerFee.multiply(bTotoalSinals).doubleValue());
        
        //set user final proift
        BigDecimal bTotoalSinalsBrokerFee = new BigDecimal(String.valueOf(bBrokerFee.multiply(bTotoalSinals).doubleValue())); 
        BigDecimal bTotoalProfitOrLoss = new BigDecimal(String.valueOf(total_profit_or_loss_points)); 
        
        oResult.setFinal_profit_or_loss_points(bTotoalProfitOrLoss.subtract(bTotoalSinalsBrokerFee).doubleValue());
        
        return oResult;
    }
    public boolean updateMyAnalysisConditionActive(UserBean oUserBean,long conditionId,int activeFlag)
    {
        return DBAccessor.getInstance().updateMyAnalysisConditionActive(oUserBean,conditionId,activeFlag);
    }
    
    public boolean updateMyAnalysisConditionDate(UserBean oUserBean, long fromDate, long toDate)
    {
        return DBAccessor.getInstance().updateMyAnalysisConditionDate(oUserBean,fromDate,toDate);
    }
    
    public boolean changeAnalysisMarketCondition(UserBean oUserBean,long previousMarketID,long newMarketID,long newStrategyID,long newSignalPeriodID)
    {
        return DBAccessor.getInstance().changeAnalysisMarketCondition(oUserBean,previousMarketID,newMarketID,newStrategyID, newSignalPeriodID); 
    }
    
    public boolean addAnalysisMarketCondition(UserBean oUserBean,long newMarketID,long newStrategyID, long newSignalPeriodID)
    {
        //whenever user add new market, its date can NOT be specified. So, we should get the previous date for this new one
        
        //get current conditions' date, if so, then, FromDate and ToDate are both 0
        long[] conditionDate = DBAccessor.getInstance().getAnalysisMarketConditionDate(oUserBean);
        
        if(null == conditionDate || conditionDate.length != 2)
        {
            conditionDate = null;
            conditionDate = new long[2];
            conditionDate[0] = 0;
            conditionDate[1] = 0;
        }
        
        return DBAccessor.getInstance().addAnalysisMarketCondition(oUserBean,newMarketID,newStrategyID,newSignalPeriodID,conditionDate[0],conditionDate[1]);
    }
    
    public boolean removeMyAnalysisCondition(UserBean oUserBean, long conditionID)
    {
        if(null == oUserBean)
        {
            return false;
        }
        
        return DBAccessor.getInstance().removeMyAnalysisCondition(oUserBean,conditionID);
    }
    
    public List<UserAnalysisMarketBean> getUserAnalysisMarketList(UserBean oUserBean)
    {
        if(null == oUserBean)
        {
            return new ArrayList<UserAnalysisMarketBean>();
        }
        
        List<UserAnalysisMarketBean> oDBResult = DBAccessor.getInstance().getUserAnalysisMarketList(oUserBean); 
        
        if(null == oDBResult)
        {
            return new ArrayList<UserAnalysisMarketBean>();
        }
        
        List<UserAnalysisMarketBean> oFinalResult = new ArrayList<UserAnalysisMarketBean>();
        
        for(int i=0; i<oDBResult.size(); i++)
        {
            UserAnalysisMarketBean oTemp = oDBResult.get(i);
            
            UserAnalysisMarketBean oFinalBean = new UserAnalysisMarketBean();
            oFinalBean.setId(oTemp.getId());
            oFinalBean.setActive(oTemp.isActive());
            oFinalBean.setAnalysis_from(oTemp.getAnalysis_from());
            oFinalBean.setAnalysis_to(oTemp.getAnalysis_to());
            oFinalBean.setMarket_period_id(oTemp.getMarket_period_id());
            oFinalBean.setMarket_type_id(oTemp.getMarket_type_id());
            oFinalBean.setStrategy_id(oTemp.getStrategy_id());
            oFinalBean.setUser_id(oTemp.getUser_id());
            
            //String marketTypeName = "";
            String marketDisplayName = "";
            if(null != DataCache.getInstance().getMarketTypeByID(oTemp.getMarket_type_id()))
            {
                //marketTypeName = DataCache.getInstance().getMarketTypeByID(oTemp.getMarket_type_id()).getMarket_type_name();
                marketDisplayName = DataCache.getInstance().getMarketTypeByID(oTemp.getMarket_type_id()).getDisplay_name();
            }
            //String marketName = marketTypeName + "-" + marketDisplayName;
            oFinalBean.setMarketName(marketDisplayName);
            
            oFinalBean.setTimePeriodName(DataCache.getInstance().getMarketPeriodByID(oTemp.getMarket_period_id()).getPeriod_name());
            oFinalBean.setPeriodValue(DataCache.getInstance().getMarketPeriodByID(oTemp.getMarket_period_id()).getValue());
            
            oFinalResult.add(oFinalBean);
        }
        
        return oFinalResult;
    }
    
    public List<TimeSeriesCollection> getEveryTradeDataSeries(String strategyID, String marketId,String periodId, long from, long to, long timeOffsetTime,boolean isProfitLoss) 
    {
        TimeSeriesCollection oPositiveDataSeries = new TimeSeriesCollection();
        TimeSeries oPositiveTimeSeries = createTimeSeriesByPeriodID(CommonDefine.ONE_MINUTE); 
        
        TimeSeriesCollection oNegtiveDataSeries = new TimeSeriesCollection();
        TimeSeries oNegtiveTimeSeries = createTimeSeriesByPeriodID(CommonDefine.ONE_MINUTE); 
        
        TimeSeriesCollection oLineDataSeries = new TimeSeriesCollection();
        TimeSeries oLineTimeSeries = createTimeSeriesByPeriodID(CommonDefine.ONE_MINUTE); 
        
        oPositiveDataSeries.addSeries(oPositiveTimeSeries);
        oNegtiveDataSeries.addSeries(oNegtiveTimeSeries);
        oLineDataSeries.addSeries(oLineTimeSeries);
       
        
        List<CumulativeProftDataBean> oCumulativeProftDataList = getProiftByMarketPeriodAndDate(strategyID, marketId,periodId,from,to);
        
        if(oCumulativeProftDataList == null || oCumulativeProftDataList.size() < 1)
        {
            List<TimeSeriesCollection> oResult =  new ArrayList<TimeSeriesCollection>();
            oResult.add(0,oPositiveDataSeries);
            oResult.add(1,oNegtiveDataSeries);
            oResult.add(2,oLineDataSeries);
            
            return oResult;
        }
                    
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        
        HashMap<String, Double> oDataMap = new HashMap<String,Double>();
        
        //InfoTrace.getInstance().printInfo(DebugLevel.INFO,"AnalysisController.getEveryTradeDataSeries() Time zone offset is:"+timeOffsetTime);
        
        //classification data
        for(int i=0; i<oCumulativeProftDataList.size(); i++)
        {
            CumulativeProftDataBean oTemp = oCumulativeProftDataList.get(i);
            
            //if value is 0, that means this is only signal not trade
            if(oTemp.get_value() == 0)
            {
                continue;
            }
                                    
            String dateString = dateTimeFormat.format(new Date(oTemp.get_date()+timeOffsetTime));
            
            //InfoTrace.getInstance().printInfo(DebugLevel.INFO,"AnalysisController.getEveryTradeDataSeries() Analyze dates are:"+dateString);
                        
            if(null != oDataMap.get(dateString))
            {
                double currentValue = oDataMap.get(dateString).doubleValue();
                
                BigDecimal bAmount = new BigDecimal(String.valueOf(currentValue)); 
                BigDecimal bNew = new BigDecimal(String.valueOf(oTemp.get_value())); 
                
                double newValue = bAmount.add(bNew).doubleValue();
                
                oDataMap.put(dateString, Double.valueOf(newValue));
            }
            else
            {
                oDataMap.put(dateString, Double.valueOf(oTemp.get_value()));
            }
        }
        
        //add data into ArrayList and sort it by date
        Iterator<String> oDateKeyList = oDataMap.keySet().iterator();
        List<String> oDateStringList = new ArrayList<String>();
        
        try
        {
            int i=0;
            
            while(oDateKeyList.hasNext())
            {
                String currentKey = oDateKeyList.next();
                
                oDateStringList.add(i,currentKey);
                
                i++;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        
        Comparator<String> comp = new DateStringComparator();
        Collections.sort(oDateStringList,comp);
        
        //add data into series
        try
        {
            long currentTime = 0;
            double cumulativeProft=0.0;
            
            for(int i=0; i<oDateStringList.size(); i++)
            {
                String currentKey = oDateStringList.get(i);
                
                currentTime=dateTimeFormat.parse(currentKey).getTime();
                double currentValue = oDataMap.get(currentKey).doubleValue();
                
                BigDecimal bAmount = new BigDecimal(String.valueOf(cumulativeProft)); 
                BigDecimal bNew = new BigDecimal(String.valueOf(currentValue)); 
                
                cumulativeProft = bAmount.add(bNew).doubleValue();
                                
                if(currentValue >= 0)
                {                    
                    if(isProfitLoss)
                    {
                        oPositiveTimeSeries.addOrUpdate(createTimePeriodByTime(CommonDefine.ONE_MINUTE,currentTime),currentValue); 
                    }
                    else
                    {
                        oPositiveTimeSeries.addOrUpdate(createTimePeriodByTime(CommonDefine.ONE_MINUTE,currentTime),cumulativeProft); 
                    }
                }
                else
                {
                    if(isProfitLoss)
                    {
                        oNegtiveTimeSeries.addOrUpdate(createTimePeriodByTime(CommonDefine.ONE_MINUTE,currentTime),currentValue); 
                    }
                    else
                    {
                        oNegtiveTimeSeries.addOrUpdate(createTimePeriodByTime(CommonDefine.ONE_MINUTE,currentTime),cumulativeProft); 
                    }
                }

                if(isProfitLoss)
                {
                    oLineTimeSeries.addOrUpdate(createTimePeriodByTime(CommonDefine.ONE_MINUTE,currentTime),0);
                }
                else
                {
                    oLineTimeSeries.addOrUpdate(createTimePeriodByTime(CommonDefine.ONE_MINUTE,currentTime),cumulativeProft);
                }
            }
            
            if(isProfitLoss)
            {
                oLineTimeSeries.addOrUpdate(createTimePeriodByTime(CommonDefine.ONE_MINUTE,currentTime+60*1000),0);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        
        List<TimeSeriesCollection> oResult =  new ArrayList<TimeSeriesCollection>();
        oResult.add(0,oPositiveDataSeries);
        oResult.add(1,oNegtiveDataSeries);
        oResult.add(2,oLineDataSeries);
        
        return oResult;
    }
        
    public List<TimeSeriesCollection> getProftDataSeries(String strategyID, String marketId,String periodId, long from, long to, long timeOffsetTime) 
    {
        TimeSeriesCollection oPositiveDataSeries = new TimeSeriesCollection();
        TimeSeries oPositiveTimeSeries = createTimeSeriesByPeriodID(CommonDefine.ONE_DAY); 
        
        TimeSeriesCollection oNegtiveDataSeries = new TimeSeriesCollection();
        TimeSeries oNegtiveTimeSeries = createTimeSeriesByPeriodID(CommonDefine.ONE_DAY); 
        
        TimeSeriesCollection oLineDataSeries = new TimeSeriesCollection();
        TimeSeries oLineTimeSeries = createTimeSeriesByPeriodID(CommonDefine.ONE_DAY); 
        
        oPositiveDataSeries.addSeries(oPositiveTimeSeries);
        oNegtiveDataSeries.addSeries(oNegtiveTimeSeries);
        oLineDataSeries.addSeries(oLineTimeSeries);
        
        List<CumulativeProftDataBean> oCumulativeProftDataList = getProiftByMarketPeriodAndDate(strategyID, marketId,periodId,from,to);
        
        if(oCumulativeProftDataList == null || oCumulativeProftDataList.size() < 1)
        {
            List<TimeSeriesCollection> oResult =  new ArrayList<TimeSeriesCollection>();
            oResult.add(0,oPositiveDataSeries);
            oResult.add(1,oNegtiveDataSeries);
            oResult.add(2,oLineDataSeries);
            
            return oResult;
        }
        
        //get user time-zone         
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        HashMap<String, Double> oDataMap = new HashMap<String,Double>();
        
        //classification data
        for(int i=0; i<oCumulativeProftDataList.size(); i++)
        {
            CumulativeProftDataBean oTemp = oCumulativeProftDataList.get(i);
            String dateString = dateTimeFormat.format(new Date(oTemp.get_date()+timeOffsetTime));
            
            if(null != oDataMap.get(dateString))
            {
                double currentValue = oDataMap.get(dateString).doubleValue();
                
                BigDecimal bAmount = new BigDecimal(String.valueOf(currentValue)); 
                BigDecimal bNew = new BigDecimal(String.valueOf(oTemp.get_value())); 
                
                double newValue = bAmount.add(bNew).doubleValue();
                
                oDataMap.put(dateString, Double.valueOf(newValue));
            }
            else
            {
                oDataMap.put(dateString, Double.valueOf(oTemp.get_value()));
            }
        }
        
        //add data into ArrayList and sort it by date
        Iterator<String> oDateKeyList = oDataMap.keySet().iterator();
        List<String> oDateStringList = new ArrayList<String>();
        
        try
        {
            int i=0;
            
            while(oDateKeyList.hasNext())
            {
                String currentKey = oDateKeyList.next();
                
                oDateStringList.add(i,currentKey);
                
                i++;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        
        Comparator<String> comp = new DateStringComparator();
        Collections.sort(oDateStringList,comp);
        
        //add data into series
        try
        {
            long currentTime = 0;
            for(int i=0; i<oDateStringList.size(); i++)
            {
                String currentKey = oDateStringList.get(i);
                
                currentTime=dateTimeFormat.parse(currentKey).getTime();
                double value = oDataMap.get(currentKey).doubleValue();
                                
                if(value >= 0)
                {
                    oPositiveTimeSeries.addOrUpdate(createTimePeriodByTime(CommonDefine.ONE_DAY,currentTime),value); 
                }
                else
                {
                    oNegtiveTimeSeries.addOrUpdate(createTimePeriodByTime(CommonDefine.ONE_DAY,currentTime),value); 
                }

                oLineTimeSeries.addOrUpdate(createTimePeriodByTime(CommonDefine.ONE_DAY,currentTime),0);
            }
            
            oLineTimeSeries.addOrUpdate(createTimePeriodByTime(CommonDefine.ONE_DAY,currentTime+24*60*60*1000),0);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
             
        List<TimeSeriesCollection> oResult =  new ArrayList<TimeSeriesCollection>();
        oResult.add(0,oPositiveDataSeries);
        oResult.add(1,oNegtiveDataSeries);
        oResult.add(2,oLineDataSeries);
        
        return oResult;
    }
    
    public List<TimeSeriesCollection> getCumulativeProftDataSeries(String strategyID, String marketId,String periodId, long from, long to, long timeOffsetTime) 
    {
        
        //create three series
        TimeSeriesCollection oPositiveDataSeries = new TimeSeriesCollection();
        TimeSeries oPositiveTimeSeries = createTimeSeriesByPeriodID(CommonDefine.ONE_DAY); 
        
        TimeSeriesCollection oNegtiveDataSeries = new TimeSeriesCollection();
        TimeSeries oNegtiveTimeSeries = createTimeSeriesByPeriodID(CommonDefine.ONE_DAY); 
        
        TimeSeriesCollection oWholeDataSeries = new TimeSeriesCollection();
        TimeSeries oWholeTimeSeries = createTimeSeriesByPeriodID(CommonDefine.ONE_DAY); 
        
        oPositiveDataSeries.addSeries(oPositiveTimeSeries);
        oNegtiveDataSeries.addSeries(oNegtiveTimeSeries);
        oWholeDataSeries.addSeries(oWholeTimeSeries);
        
        List<CumulativeProftDataBean> oCumulativeProftDataList = getProiftByMarketPeriodAndDate(strategyID, marketId,periodId,from,to);
        
        if(oCumulativeProftDataList == null || oCumulativeProftDataList.size() < 1)
        {
            List<TimeSeriesCollection> oResult =  new ArrayList<TimeSeriesCollection>();
            oResult.add(0,oWholeDataSeries);
            oResult.add(1,oPositiveDataSeries);
            oResult.add(2,oNegtiveDataSeries);
            
            return oResult;
        }
        
        //get user time-zone         
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        HashMap<String, Double> oDataMap = new HashMap<String,Double>();
        
        //classification data
        for(int i=0; i<oCumulativeProftDataList.size(); i++)
        {
            CumulativeProftDataBean oTemp = oCumulativeProftDataList.get(i);
            String dateString = dateTimeFormat.format(new Date(oTemp.get_date()+timeOffsetTime));
            
            if(null != oDataMap.get(dateString))
            {
                double currentValue = oDataMap.get(dateString).doubleValue();
                
                BigDecimal bAmount = new BigDecimal(String.valueOf(currentValue)); 
                BigDecimal bNew = new BigDecimal(String.valueOf(oTemp.get_value())); 
                
                double newValue = bAmount.add(bNew).doubleValue();
                
                oDataMap.put(dateString, Double.valueOf(newValue));
            }
            else
            {
                oDataMap.put(dateString, Double.valueOf(oTemp.get_value()));
            }
        }
        
        //add data into ArrayList and sort it by date
        Iterator<String> oDateKeyList = oDataMap.keySet().iterator();
        List<String> oDateStringList = new ArrayList<String>();
        
        try
        {
            int i=0;
            
            while(oDateKeyList.hasNext())
            {
                String currentKey = oDateKeyList.next();
                
                oDateStringList.add(i,currentKey);
                
                i++;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        
        Comparator<String> comp = new DateStringComparator();
        Collections.sort(oDateStringList,comp);
        
        //add data into series
        double cumulativeProft=0.0;
        try
        {
            for(int i=0; i<oDateStringList.size(); i++)
            {
                String currentKey = oDateStringList.get(i);
                
                long currentTime=dateTimeFormat.parse(currentKey).getTime();
                double value = oDataMap.get(currentKey).doubleValue();
                
                BigDecimal bAmountCumulativeProft = new BigDecimal(cumulativeProft); 
                BigDecimal bCurrent = new BigDecimal(String.valueOf(value)); 
                
                cumulativeProft = bAmountCumulativeProft.add(bCurrent).doubleValue();
                
                if(value >= 0)
                {
                    oPositiveTimeSeries.addOrUpdate(createTimePeriodByTime(CommonDefine.ONE_DAY,currentTime),cumulativeProft); 
                }
                else
                {
                    oNegtiveTimeSeries.addOrUpdate(createTimePeriodByTime(CommonDefine.ONE_DAY,currentTime),cumulativeProft); 
                }
                
                oWholeTimeSeries.addOrUpdate(createTimePeriodByTime(CommonDefine.ONE_DAY,currentTime),cumulativeProft);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        
        List<TimeSeriesCollection> oResult =  new ArrayList<TimeSeriesCollection>();
        oResult.add(0,oPositiveDataSeries);
        oResult.add(1,oNegtiveDataSeries);
        oResult.add(2,oWholeDataSeries);
        
        return oResult;
    }
    
    private long getAllSignals(int signalType, String strategyID, String marketId,String periodId, long from, long to)
    {
        if("".equals(marketId) || "".equals(periodId))
        {
            return 0;
        }
        
        int allSignalsAmount = 0;
        
        int selectedMarketAmount = 0;
        
        //parse market id
        String[] marketIDStringList = marketId.split(";");
        long[] marketIDList = new long[marketIDStringList.length];
        
        for(int i=0; i<marketIDStringList.length; i++)
        {
            marketIDList[i] = Long.parseLong(marketIDStringList[i]);
        }
        
        selectedMarketAmount = marketIDStringList.length;
        
        //parse the strategy ID
        String[] strategyIDStringList = strategyID.split(";");
        long[] strategyIDList = new long[strategyIDStringList.length];
        
        for(int i=0; i<strategyIDStringList.length; i++)
        {
            strategyIDList[i] = Long.parseLong(strategyIDStringList[i]);
        }
        
        if( strategyIDList.length != selectedMarketAmount)
        {            
            return 0;
        }
        
        //parse market period id
        String[] marketPeriodIDStringList = periodId.split(";");
        long[] marketPeriodIDList = new long[marketPeriodIDStringList.length];
        
        for(int i=0; i<marketPeriodIDStringList.length; i++)
        {
            marketPeriodIDList[i] = Long.parseLong(marketPeriodIDStringList[i]);
        }
        
        if( marketPeriodIDList.length != selectedMarketAmount)
        {            
            return 0;
        }
        
        
        for(int i=0; i<marketIDList.length; i++)
        {
            int signalsAmount = DBAccessor.getInstance().getSignalsAmount(signalType, strategyIDList[i], marketIDList[i],marketPeriodIDList[i],from,to);
            
            allSignalsAmount += signalsAmount;
        }
        
        return allSignalsAmount;
    }
    
    public List<CumulativeProftDataBean> getProiftByMarketPeriodAndDate(String strategyID, String marketId,String periodId, long from, long to)
    {
        if("".equals(marketId) || "".equals(periodId))
        {
            return new ArrayList<CumulativeProftDataBean>();
        }
        
        int selectedMarketAmount = 0;
        
        //parse market id
        String[] marketIDStringList = marketId.split(";");
        long[] marketIDList = new long[marketIDStringList.length];
        
        for(int i=0; i<marketIDStringList.length; i++)
        {
            marketIDList[i] = Long.parseLong(marketIDStringList[i]);
        }
        
        selectedMarketAmount = marketIDStringList.length;
        
        //parse the strategy ID
        String[] strategyIDStringList = strategyID.split(";");
        long[] strategyIDList = new long[strategyIDStringList.length];
        
        for(int i=0; i<strategyIDStringList.length; i++)
        {
            strategyIDList[i] = Long.parseLong(strategyIDStringList[i]);
        }
        
        if( strategyIDList.length != selectedMarketAmount)
        {            
            return new ArrayList<CumulativeProftDataBean>();
        }
        
        //parse market period id
        String[] marketPeriodIDStringList = periodId.split(";");
        long[] marketPeriodIDList = new long[marketPeriodIDStringList.length];
        
        for(int i=0; i<marketPeriodIDStringList.length; i++)
        {
            marketPeriodIDList[i] = Long.parseLong(marketPeriodIDStringList[i]);
        }
        
        if( marketPeriodIDList.length != selectedMarketAmount)
        {            
            return new ArrayList<CumulativeProftDataBean>();
        }
        
        List<CumulativeProftDataBean> oCumulativeProftDataList = new ArrayList<CumulativeProftDataBean>();
        
        for(int i=0; i<marketIDList.length; i++)
        {
            List<CumulativeProftDataBean> oTempCumulativeProftDataList = DBAccessor.getInstance().getSignalsProftData(strategyIDList[i], marketIDList[i],marketPeriodIDList[i],from,to);
            
            oCumulativeProftDataList.addAll(oTempCumulativeProftDataList);
        }
        
        return oCumulativeProftDataList;
    }
    
    public List<TradeBean> getTopProfitTrade(int topNum)
    {
        if(topNum <= 0)
        {
            return new ArrayList<TradeBean>();
        }
        
        List<TradeBean> oTopTrades = null;
        long startDate = System.currentTimeMillis() - 24*60*60*1000;
        
        Signal oLatestSignal = DBAccessor.getInstance().getLatestSignal();
        if(null != oLatestSignal && oLatestSignal.getGenerate_date() > 24*60*60*1000)
        {
            startDate = oLatestSignal.getGenerate_date() - 24*60*60*1000; //previous 24 hours
        }
        
        List<Signal> oTopSignal = DBAccessor.getInstance().getTopProfitSignal(topNum, startDate);
        
        //add by Peter, check previous 48 or 72 hours if number of top trades is less than topNum
        if(null != oTopSignal && oTopSignal.size() < topNum)
        {
            startDate = oLatestSignal.getGenerate_date() - 2*24*60*60*1000; // previous 48 hours
            oTopSignal = DBAccessor.getInstance().getTopProfitSignal(topNum, startDate);
            
            if(null != oTopSignal && oTopSignal.size() < topNum)
            {
                startDate = oLatestSignal.getGenerate_date() - 3*24*60*60*1000; // previous 72 hours
                oTopSignal = DBAccessor.getInstance().getTopProfitSignal(topNum, startDate);
            }
        }
        
        if(null == oTopSignal || oTopSignal.size() == 0)
        {
            return new ArrayList<TradeBean>();
        }
        
        oTopTrades = new ArrayList<TradeBean>();
        
        List<StrategyBean> allStrategy = DBAccessor.getInstance().getAllStrategyBaseInfo();
        
        for(int i=0; i<oTopSignal.size(); i++)
        {
            Signal oTempSignal = oTopSignal.get(i);
            
            TradeBean oTrade = new TradeBean();
            
            oTrade.setPoints((long)oTempSignal.getProfit());
            oTrade.setTrade_direction(oTempSignal.getDirection());
            
            oTrade.setMarket_type_id(oTempSignal.getMarket_type_id());
            oTrade.setMarket_type_name(DataCache.getInstance().getMarketTypeByID(oTempSignal.getMarket_type_id()).getDisplay_name());
            
            oTrade.setStrategy_id(oTempSignal.getStrategy_id());  
            oTrade.setStrategy_name(getStrategyName(oTempSignal.getStrategy_id(), allStrategy));
            
            oTrade.setTime_frame_id(oTempSignal.getSignal_period());
            MarketPeriodBean oMarketPeriod = DataCache.getInstance().getMarketPeriodByID((long)oTempSignal.getSignal_period());
            oTrade.setTime_frame_name(oMarketPeriod.getValue() + " "+ oMarketPeriod.getPeriod_name());
            
            oTopTrades.add(oTrade);
        }
        
        return oTopTrades;
    }
    
    private String getStrategyName(long strategyID, final List<StrategyBean> allStrategy)
    {
        if(strategyID == 0 || null == allStrategy)
            return "";
        
        for(int i=0; i<allStrategy.size(); i++)
        {
            StrategyBean oStrategy = allStrategy.get(i);
            
            if(oStrategy.getId() == strategyID)
                return oStrategy.getCommon_name();
        }
        
        return "";
    }
    
    private TimeSeries createTimeSeriesByPeriodID(int periodId)
    {
        TimeSeries oTimeSeries = null;
        
        switch(periodId)
        {
            case CommonDefine.ONE_MINUTE:
            case CommonDefine.FIVE_MINUTE:
            case CommonDefine.TEN_MINUTE:
            case CommonDefine.THIRTY_MINUTE:
                oTimeSeries = new TimeSeries("",org.jfree.data.time.Minute.class);  
                 break;
            case CommonDefine.ONE_HOUR:
                oTimeSeries = new TimeSeries("",org.jfree.data.time.Hour.class);  
                break;
            case CommonDefine.ONE_DAY:
                oTimeSeries = new TimeSeries("",org.jfree.data.time.Day.class);  
                break;
            case CommonDefine.ONE_WEEK:
                oTimeSeries = new TimeSeries("",org.jfree.data.time.Week.class);  
                break;
            case CommonDefine.ONE_MONTH:
                oTimeSeries = new TimeSeries("",org.jfree.data.time.Month.class);  
                break;
            case CommonDefine.ONE_YEAR:
                oTimeSeries = new TimeSeries("",org.jfree.data.time.Year.class);  
                break;
            default:
                oTimeSeries = new TimeSeries("",org.jfree.data.time.Minute.class);  
                break;
        }
        
        return oTimeSeries;
    }
    
    private RegularTimePeriod createTimePeriodByTime(int periodId,long time)
    {
        RegularTimePeriod oTimePeriod = null;
        
        switch(periodId)
        {
            case CommonDefine.ONE_MINUTE:
            case CommonDefine.FIVE_MINUTE:
            case CommonDefine.TEN_MINUTE:
            case CommonDefine.THIRTY_MINUTE:
                oTimePeriod = new Minute(new Date(time));
                 break;
            case CommonDefine.ONE_HOUR:
                oTimePeriod = new Hour(new Date(time));  
                break;
            case CommonDefine.ONE_DAY:
                oTimePeriod = new Day(new Date(time));   
                break;
            case CommonDefine.ONE_WEEK:
                oTimePeriod = new Week(new Date(time));   
                break;
            case CommonDefine.ONE_MONTH:
                oTimePeriod = new Month(new Date(time));   
                break;
            case CommonDefine.ONE_YEAR:
                oTimePeriod = new Year(new Date(time));    
                break;
            default:
                oTimePeriod = new Minute(new Date(time));
                break;
        }
        
        return oTimePeriod;
    }    
}
