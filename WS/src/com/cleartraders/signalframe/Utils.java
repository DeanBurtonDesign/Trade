package com.cleartraders.signalframe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cleartraders.common.define.CommonDefine;


public class Utils
{
    public static String generateIndicatorName(String indicatorBaseName, long marketID, long periodID)
    {
        return indicatorBaseName+":"+marketID+":"+periodID;
    }
    
    public static long getMarketIDFromIndicatorName(String indicatorName)
    {
        if(null == indicatorName)
            return 0;
        
        if(indicatorName.split(":").length != 3)
            return 0;
        
        return Long.parseLong(indicatorName.split(":")[1]);
    }
    
    public static long getPeriodIDFromIndicatorName(String indicatorName)
    {
        if(null == indicatorName)
            return 0;
        
        if(indicatorName.split(":").length != 3)
            return 0;
        
        return Long.parseLong(indicatorName.split(":")[2]);
    }
    
    public static String getIndicatorBaseName(String indicatorName)
    {
        if(null == indicatorName)
            return "";
        
        if(indicatorName.split(":").length != 3)
            return "";
        
        return indicatorName.split(":")[0];
    }
    
    public static long getTimeByPeriodType(long dateValue, int periodType)
    {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long dateTimeValue = dateValue;
        
        //remove Hour, Minute and Second from Daily date String
        if( periodType == CommonDefine.ONE_DAY || 
            periodType == CommonDefine.ONE_WEEK || 
            periodType == CommonDefine.ONE_MONTH || 
            periodType == CommonDefine.ONE_YEAR )
        {
            dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            String updateTimeString = dateTimeFormat.format(new Date(dateValue));
            try
            {
                dateTimeValue = dateTimeFormat.parse(updateTimeString).getTime();
            }
            catch (ParseException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        return dateTimeValue;
    }
    public static String getDateString(long dateValue, int periodType)
    {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        //remove Hour, Minute and Second from Daily date String
        if( periodType == CommonDefine.ONE_DAY || 
            periodType == CommonDefine.ONE_WEEK || 
            periodType == CommonDefine.ONE_MONTH || 
            periodType == CommonDefine.ONE_YEAR )
        {
            dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        
        return dateTimeFormat.format(new Date(dateValue));
    }
    
    public static int getMinutesByPeriodType(int timePeriodType)
    {
        int minuteAmount = 1;
        
        switch(timePeriodType)
        {
            case CommonDefine.ONE_MINUTE:
                minuteAmount = 1;
                break;
            case CommonDefine.FIVE_MINUTE:
                minuteAmount = 5;
                break;
            case CommonDefine.TEN_MINUTE:
                minuteAmount = 10;
                break;
            case CommonDefine.THIRTY_MINUTE:
                minuteAmount = 30;
                break;
            case CommonDefine.ONE_HOUR:
                minuteAmount = 60;
                break;
            case CommonDefine.ONE_DAY:
                minuteAmount = 60*24;
                break;
            default:
                break;
        }
        
        return minuteAmount;
    }
    
    public static long getCurrentPeriodTime(long quoteTime, int timePeriodType)
    {
        long result = quoteTime;
        
        int minuteAmount = 1;
        
        switch(timePeriodType)
        {
            case CommonDefine.ONE_MINUTE:
                minuteAmount = 1;
                break;
            case CommonDefine.FIVE_MINUTE:
                minuteAmount = 5;
                break;
            case CommonDefine.TEN_MINUTE:
                minuteAmount = 10;
                break;
            case CommonDefine.THIRTY_MINUTE:
                minuteAmount = 30;
                break;
            case CommonDefine.ONE_HOUR:
                minuteAmount = 60;
                break;
            case CommonDefine.ONE_DAY:
                minuteAmount = 60*24;
                break;
            default:
                break;
        }
        
        long amountOfPeriod = quoteTime / (minuteAmount*60*1000);
        result = (amountOfPeriod+1)*(minuteAmount*60*1000);
        
        return result;
    }
}
