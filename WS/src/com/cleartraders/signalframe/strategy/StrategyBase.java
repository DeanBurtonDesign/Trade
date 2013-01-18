package com.cleartraders.signalframe.strategy;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.cleartraders.autotrader.AutoTrader;
import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.HLOCDataBean;
import com.cleartraders.common.entity.MarketPeriodBean;
import com.cleartraders.common.entity.MarketTypeBean;
import com.cleartraders.common.entity.ProductBean;
import com.cleartraders.common.entity.Signal;
import com.cleartraders.common.entity.StrategyBean;
import com.cleartraders.common.entity.TimeZoneBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.entity.UserSignalPreferenceBaseBean;
import com.cleartraders.common.util.config.CommonResManager;
import com.cleartraders.common.util.email.EmailBean;
import com.cleartraders.common.util.email.EmailHandler;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.message.IMessager;
import com.cleartraders.common.util.message.JMSTopicMessager;
import com.cleartraders.common.util.sms.SMSBean;
import com.cleartraders.common.util.sms.SMSHandler;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.common.util.tools.HtmlEmailTools;
import com.cleartraders.signalframe.Utils;
import com.cleartraders.signalframe.conf.SFResManager;
import com.cleartraders.signalframe.dataengine.IQuoteEngine;
import com.cleartraders.signalframe.dataengine.IQuoteFeeder;
import com.cleartraders.signalframe.dataengine.QuoteEngineManager;
import com.cleartraders.signalframe.indicator.IIndicatorListener;
import com.cleartraders.signalframe.indicator.IndicatorBase;
import com.cleartraders.ws.db.WSDBAccessor;

public abstract class StrategyBase implements IStrategy, IIndicatorListener,IQuoteFeeder
{
    private long strategyID = 0;
    private boolean _isRealtime = false;
    
    private String strategySystemName = "";
    private static String htmlEmailBasedPath = CommonTools.getSysDir()+ "resource" + File.separator + "emailtemplate" + File.separator; 
    private static String resourceURL = SFResManager.getInstance().getHtml_email_resource_url();
    
    public StrategyBase(long strategyID,String strategySystemName, boolean isRealtime)
    {
        this.strategyID = strategyID;
        this.strategySystemName = strategySystemName;
        
        _isRealtime = isRealtime;
    }
    
    public abstract void initIndicators();
    public abstract void onQuoteUpdate(boolean isNewQuote, String quoteEngineID, HLOCDataBean quote);
    public abstract void notifyIndicatorValue(Map<String, Double> currentIndicatorValue, IndicatorBase oIndicator);
    
    protected void registerQuoteEngine()
    {
        if(isRealtime())
        {
            List<MarketTypeBean> oAllMarketBean = DataCache.getInstance().getAllMarketType();
            List<MarketPeriodBean> oAllMarketPeriod = DataCache.getInstance().getAllMarketPeriod();
            
            if(null == oAllMarketBean || null == oAllMarketPeriod)
                return;
            
            for(int i=0; i<oAllMarketBean.size(); i++)
            {
                MarketTypeBean oMarketBean = oAllMarketBean.get(i);
                
                for(int j=0; j<oAllMarketPeriod.size(); j++)
                {
                    MarketPeriodBean oMarketPeriodBean = oAllMarketPeriod.get(j);
                    
                    IQuoteEngine relateQuoteEngine = QuoteEngineManager.getInstance().getQuoteEngine(oMarketBean.getId(), oMarketPeriodBean.getId());
                    
                    if(null != relateQuoteEngine)
                    {
                        InfoTrace.getInstance().printInfo(DebugLevel.INFO," Strategy: "+strategySystemName+" register quote engine "+oMarketBean.getMarket_name()+":"+oMarketPeriodBean.getId()+" success!");
                        
                        relateQuoteEngine.addStrategyQuoteFeeder(strategySystemName, this);
                    }
                }
            }
        }
    }
    
    public boolean handleSignal(Signal oSignal)
    {        
        LogTools.getInstance().insertLog(DebugLevel.INFO,"New signal generated, details is "+oSignal.toTextMessage()+". Current time: " + System.currentTimeMillis());
        
        //calculate profit
        oSignal.setProfit(this.calculateLongShortCloseSignalProfit(oSignal));
        
        //the direction has been changed while caulculateProfit
        oSignal.setDirection(oSignal.getDirection());
        
        // store signal into DB
        // system has to store into DB successfully and then notify
        boolean result = WSDBAccessor.getInstance().storeSignalIntoDB(oSignal);
        
        // Note: three notify methods are independent
        // That means if one of them failed, others should still notify
        // Only notify the Signal while this strategy is active
        if (result && this.isActive())
        {     
            LogTools.getInstance().insertLog(DebugLevel.INFO,"New signal had been stored successfully! Current time: " + System.currentTimeMillis());
            
            //add this signal to auto trader, if auto-trade is not open
            //then, autotrader will NOT handle this signal
            AutoTrader.getInstance().tradeBySignal(oSignal);
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Pass the signal trading. Current time: " + System.currentTimeMillis());
            
            //send SMS
            notifySignalToSMS(oSignal);
            
            // sent Email
            notifySignalToEmail(oSignal);
            
            // notify to normal client (Flex)
            notifySignalToMQ(oSignal);
            
            //notify to normal client (JS)
            notifySignalToMQToJS(oSignal);
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Complete processing signal. Current time: " + System.currentTimeMillis());
        }
        
        return result;
    }
    
    /**
     * Calculate signal's profit which contains Long Short and Close signal
     * @param oNewSignal
     * @return profit
     */
    protected double calculateLongShortCloseSignalProfit(Signal oNewSignal)
    {
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Call function, calculateSignalProfit! Signal_Type ="+oNewSignal.getSignal_type()+"Current time: " + System.currentTimeMillis());
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Call function, calculateSignalProfit! Signal_Type ="+oNewSignal.getSignal_type()+"Current time: " + System.currentTimeMillis());
                
        double profit=0.0;
        
        //only calculate for Sell or Buy signal
        if(CommonDefine.LONG_SIGNAL == oNewSignal.getSignal_type() || CommonDefine.SHORT_SIGNAL == oNewSignal.getSignal_type() || CommonDefine.CLOSE_SIGNAL == oNewSignal.getSignal_type())
        {
            //get previous signal(Long or Short or Close) under same Signal Market (e.g, EUR/USD), Period ID (e.g, 1min)
            Signal oPreviousSignal = WSDBAccessor.getInstance().getPreviousSignal(oNewSignal);
            
            if(oPreviousSignal == null)
            {                
                return 0.0;
            }
            
            //if previous is same or Close, then profit is 0
            if(oNewSignal.getSignal_type() == oPreviousSignal.getSignal_type() || CommonDefine.CLOSE_SIGNAL == oPreviousSignal.getSignal_type())
            {
                return 0.0;
            }

            //this signal is converted signal, so, system should get all reversed signal before previous same signal
            Signal oPreviousSameDirectionSignal = null;
            
            if(CommonDefine.LONG_SIGNAL == oNewSignal.getSignal_type() || CommonDefine.SHORT_SIGNAL == oNewSignal.getSignal_type())
            {
                oPreviousSameDirectionSignal = WSDBAccessor.getInstance().getPreviousSameDirectionOrCloseSignal(oNewSignal);
                
                InfoTrace.getInstance().printInfo(DebugLevel.INFO,"This is Long or Short signal!");
            }
            else
            {
                //For Close signal, previous one should be Long or Short
                oPreviousSameDirectionSignal = WSDBAccessor.getInstance().getPreviousSameDirectionOrLongShortSignal(oNewSignal);
                
                InfoTrace.getInstance().printInfo(DebugLevel.INFO,"This is CLose signal!");
            }
            
            List<Signal> oAllPreviousConversedSignals = null;
            if(oPreviousSameDirectionSignal == null)
            {
                //get all previous reversed signals, since this signal is the first one in this direction
                oAllPreviousConversedSignals = WSDBAccessor.getInstance().getAllPreviousConversedSignals(oNewSignal,0);
            }
            else
            {
                //get all previous reversed signals from previous Same Direction signal
                oAllPreviousConversedSignals = WSDBAccessor.getInstance().getAllPreviousConversedSignals(oNewSignal,oPreviousSameDirectionSignal.getGenerate_date());
            }
            
            if(oAllPreviousConversedSignals != null)
            {
                int iSignalNums = oAllPreviousConversedSignals.size();   
                double previousSignalValueAmount = 0.0;
                
                long previousSignalType = CommonDefine.LONG_SIGNAL;
                
                BigDecimal bAmount = new BigDecimal(String.valueOf(previousSignalValueAmount)); 
                for(int i=0; i<iSignalNums; i++)
                {
                    Signal oTempSignal = oAllPreviousConversedSignals.get(i);
                    previousSignalType = oTempSignal.getSignal_type();
                        
                    BigDecimal bTemp = new BigDecimal(String.valueOf(oTempSignal.getSignalValue())); 
                    previousSignalValueAmount = bAmount.add(bTemp).doubleValue();
                    
                    bAmount = new BigDecimal(String.valueOf(previousSignalValueAmount)); 
                }         
                
                //current signal amount value
                BigDecimal bSignalNums = new BigDecimal(String.valueOf(iSignalNums)); 
                BigDecimal bSignalValue = new BigDecimal(String.valueOf(oNewSignal.getSignalValue()));                 
                double dSignalAmount = bSignalNums.multiply(bSignalValue).doubleValue();
                
                BigDecimal bSignalAmount = new BigDecimal(String.valueOf(dSignalAmount)); 
                
                //double signalNumsValue = (double)iSignalNums;
                if(CommonDefine.LONG_SIGNAL == oNewSignal.getSignal_type())
                {                    
                    BigDecimal previousSignalAmount = new BigDecimal(String.valueOf(previousSignalValueAmount)); 
                    
                    profit = bSignalAmount.subtract(previousSignalAmount).doubleValue();
                    
                    //since this is Short Signal, so, we should change its value by multiply -1
                    BigDecimal profitDeciaml = new BigDecimal(String.valueOf(profit)); 
                    profit = profitDeciaml.multiply(new BigDecimal(String.valueOf(-1))).doubleValue();
                    
                    //add by Peter to calculate its direction
                    oNewSignal.setDirection(CommonDefine.SELL_SHORT);
                }
                else if(CommonDefine.SHORT_SIGNAL == oNewSignal.getSignal_type())
                {
                    BigDecimal previousSignalAmount = new BigDecimal(String.valueOf(previousSignalValueAmount)); 
                    
                    profit = bSignalAmount.subtract(previousSignalAmount).doubleValue();
                    
                    //add by Peter to calculate its direction
                    oNewSignal.setDirection(CommonDefine.BUY_LONG);
                }
                else if(CommonDefine.CLOSE_SIGNAL == oNewSignal.getSignal_type())
                {
                    if( CommonDefine.LONG_SIGNAL == previousSignalType)
                    {
                        BigDecimal previousSignalAmount = new BigDecimal(String.valueOf(previousSignalValueAmount)); 
                        
                        profit = bSignalAmount.subtract(previousSignalAmount).doubleValue();
                        
                        //add by Peter to calculate its direction
                        oNewSignal.setDirection(CommonDefine.BUY_LONG);
                    }
                    else if(CommonDefine.SHORT_SIGNAL == previousSignalType)
                    {
                        BigDecimal previousSignalAmount = new BigDecimal(String.valueOf(previousSignalValueAmount)); 
                        
                        profit = bSignalAmount.subtract(previousSignalAmount).doubleValue();
                        
                        //since previous Signal is SHORT, so, we should change its value by multiply -1
                        BigDecimal profitDeciaml = new BigDecimal(String.valueOf(profit)); 
                        profit = profitDeciaml.multiply(new BigDecimal(String.valueOf(-1))).doubleValue();
                        
                        //add by Peter to calculate its direction
                        oNewSignal.setDirection(CommonDefine.SELL_SHORT);
                    }
                }
            }
        }
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Calculated Profit is "+profit+". Current time: " + System.currentTimeMillis());
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Calculated Profit is "+profit+". Current time: " + System.currentTimeMillis());
        
        //get decimal of market type
        MarketTypeBean oPeriodBean = DataCache.getInstance().getMarketTypeByID(new Long(oNewSignal.getMarket_type_id()));
        BigDecimal finalProift = new BigDecimal(String.valueOf(profit)); 
        BigDecimal quoteDecimal = new BigDecimal(String.valueOf(oPeriodBean.getQuote_decimal())); 
        double points = finalProift.multiply(quoteDecimal).doubleValue();
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Convert calculated profit to points are "+points);
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Convert calculated profit to points are "+points);
                
        return points;
    }
    
    private boolean notifySignalToSMS(Signal signalBean)
    {
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Call function, notifySignalToSMS. Current time: " + System.currentTimeMillis());
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Call function, notifySignalToSMS. Current time: " + System.currentTimeMillis());
                
        if (null == signalBean)
        {
            return false;
        }
        
        boolean result = false;
        
        //get signal preference list
        List<SMSBean> oSMSList = getAllSMSListForSignal(signalBean);
        
        //send SMS to them
        SMSHandler.getInstance().appendSMSToSend(oSMSList);
        
        return result;
    }
    
    /**
     * get all active and enable SMS prefence
     * @param oAllUserSignalPreferencList
     * @return
     */
    private List<UserSignalPreferenceBaseBean> getActiveSMSPreference(List<UserSignalPreferenceBaseBean> oAllUserSignalPreferencList)
    {
        List<UserSignalPreferenceBaseBean> oActiveEmailPreferenceList = new ArrayList<UserSignalPreferenceBaseBean>();
        
        if(null == oAllUserSignalPreferencList)
        {
            return oActiveEmailPreferenceList; 
        }
        
        for(int i=0; i< oAllUserSignalPreferencList.size(); i++)
        {
            UserSignalPreferenceBaseBean oTempBean = oAllUserSignalPreferencList.get(i);
            
            if(oTempBean.getActive() && oTempBean.getEnable_sms())
            {
                oActiveEmailPreferenceList.add(oTempBean);
            }
        }
        
        return oActiveEmailPreferenceList;
    }
    
    private List<SMSBean> getAllSMSListForSignal(Signal signalBean)
    {
        List<SMSBean> oResult = new ArrayList<SMSBean>();
        if(null == signalBean)
        {
            return oResult;
        }
        
        //get all Mobile number which need send while current signal occured
        List<UserSignalPreferenceBaseBean> oPreferenceList = WSDBAccessor.getInstance().getAllUserCustomSpecificSignal(signalBean, true);
        if( null == oPreferenceList || oPreferenceList.size() == 0)
        {
            return oResult;
        }
        
        //filter those disable email alert user
        List<UserSignalPreferenceBaseBean> oActiveSMSPreferenceList = getActiveSMSPreference(oPreferenceList);
        oPreferenceList.clear();
        
        String smsContent="";
        
        //get signal period info
        MarketPeriodBean oPeriodBean = DataCache.getInstance().getMarketPeriodByID(new Long(signalBean.getSignal_period()));
        
        //Strategy Name
        StrategyBean relatedStrategy = WSDBAccessor.getInstance().getStrategyFullInfoByID(signalBean.getStrategy_id());
        String strategyName = "";
        if(null != relatedStrategy)
        {
            strategyName = relatedStrategy.getCommon_name();
        }
                
        //Set email subject and body
        if(signalBean.getSignal_type() == CommonDefine.LONG_SIGNAL)
        {
            smsContent = "Long Signal\n"+signalBean.getMarket_name()+"\n"+strategyName+"\n"+oPeriodBean.getValue()+" "+oPeriodBean.getPeriod_name()+"\n"+signalBean.getSignalValue()+"\n"+CommonResManager.getInstance().getSMSEndingInfo();
        }
        else if(signalBean.getSignal_type() == CommonDefine.SHORT_SIGNAL)
        {
            smsContent = "Short Signal\n"+signalBean.getMarket_name()+"\n"+strategyName+"\n"+oPeriodBean.getValue()+" "+oPeriodBean.getPeriod_name()+"\n"+signalBean.getSignalValue()+"\n"+CommonResManager.getInstance().getSMSEndingInfo();
        }
        else if(signalBean.getSignal_type() == CommonDefine.CLOSE_SIGNAL)
        {
            smsContent = "Close Signal\n"+signalBean.getMarket_name()+"\n"+strategyName+"\n"+oPeriodBean.getValue()+" "+oPeriodBean.getPeriod_name()+"\n"+signalBean.getSignalValue()+"\n"+CommonResManager.getInstance().getSMSEndingInfo();
        }
        else
        {
            //If signal is not BUY or SELL, then, it will be ignored
            return oResult;
        }
        
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int defaultOffsetTime = TimeZone.getDefault().getOffset(System.currentTimeMillis());
        
        //collcect user id list to reduce their sms credits
        List<Long> userIdList = new ArrayList<Long>();
        
        List<Long> smsLowLevelUserIdList = new ArrayList<Long>();
        long smsLowLevel= SFResManager.getInstance().getSms_low_level();
        
        List<Long> smsEmptyUserIdList = new ArrayList<Long>();
        long smsEmptyLevel = SFResManager.getInstance().getSms_empty_level();
        
        for(int i=0; i<oActiveSMSPreferenceList.size(); i++)
        {
            UserSignalPreferenceBaseBean oPreference = oActiveSMSPreferenceList.get(i);
            
            TimeZoneBean oTimeZone = DataCache.getInstance().getTimeZoneByID(oPreference.getTime_zone_id());            
            long timeOffsetTime = oTimeZone.getOffset()*60*60*1000 - defaultOffsetTime;
            
            String signalDate = dateTimeFormat.format(new Date((long)signalBean.getGenerate_date()+timeOffsetTime));
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Default Time zone off set time is:"+defaultOffsetTime);
            LogTools.getInstance().insertLog(DebugLevel.INFO,"User Time zone id is:"+oPreference.getTime_zone_id());
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Adjust time is:"+timeOffsetTime);
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Signal Date is:"+signalDate);
            
            SMSBean oSMS = new SMSBean();
            oSMS.setReceiverMobile(oPreference.getNotify_sms());
            oSMS.setSmsContent(smsContent);
            
            oResult.add(oSMS);
            
            userIdList.add(Long.valueOf(oPreference.getUser_id()));
            
            //check sms credits
            if(oPreference.getUser_sms_credits() == (smsLowLevel+1))
            {
                smsLowLevelUserIdList.add(Long.valueOf(oPreference.getUser_id()));
            }
            else if(oPreference.getUser_sms_credits() == (smsEmptyLevel+1))
            {
                smsEmptyUserIdList.add(Long.valueOf(oPreference.getUser_id()));
            }
        }
        
        oActiveSMSPreferenceList.clear();
        
        //reduce sms credits
        if(WSDBAccessor.getInstance().reduceUserSMSCredits(userIdList, SFResManager.getInstance().getCredit_per_sms()))
        {
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Reduce users' SMS credits successfully for this signal!");
        }
        else
        {
            LogTools.getInstance().insertLog(DebugLevel.ERROR,"Reduce users' SMS credits failed for this signal!");
        }
        
        //send sms alert emails
        //2010-06-07 remove SMS alert emails(Low level & empty)
        //sendSMSLowLevelNotificaitonEmail(smsLowLevelUserIdList);
        //sendSMSEmptyNotificationEmail(smsEmptyUserIdList);
        
        return oResult;   
    }
    
    private void sendSMSLowLevelNotificaitonEmail(List<Long> userIdList)
    {
        if(null == userIdList || userIdList.size() == 0)
        {
            return;
        }
        
        for(int i=0; i<userIdList.size(); i++)
        {
            long userID = userIdList.get(i);
            UserBean oUserBean = WSDBAccessor.getInstance().getUserInfoByID(userID);
                        
            EmailHandler.getInstance().appendEmailToSend(getSmsIsLowNotificationEmail(oUserBean));
        }
    }
    
    private void sendSMSEmptyNotificationEmail(List<Long> userIdList)
    {
        if(null == userIdList || userIdList.size() == 0)
        {
            return;
        }
        
        for(int i=0; i<userIdList.size(); i++)
        {
            long userID = userIdList.get(i);
            UserBean oUserBean = WSDBAccessor.getInstance().getUserInfoByID(userID);
            
            EmailHandler.getInstance().appendEmailToSend(getSmsEmptyNotificationEmail(oUserBean));
        }
    }
    
    private EmailBean getSmsEmptyNotificationEmail(UserBean userBean)
    {
        ProductBean oCurrentMemberLevel = WSDBAccessor.getInstance().getProductByID(userBean.getMemberLevel());
        
        String[] emailAddresses = new String[1];
        emailAddresses[0] = userBean.getEmail();
        Hashtable<String,String> properties = userBean.getProperHash();
        properties.put("resource_url", resourceURL);
        properties.put("remain_sms_credits", String.valueOf(userBean.getSms_credits()));
        properties.put("member_level_name", oCurrentMemberLevel.getName());
        properties.put("member_level_include_sms", String.valueOf(oCurrentMemberLevel.getIncludeSMS()));
        
        List<EmailBean> smsEmptyNotificatonEmail = HtmlEmailTools.generateEmail(htmlEmailBasedPath,
                SFResManager.getInstance().getSms_empty_email_file(),
                emailAddresses,
                SFResManager.getInstance().getSms_empty_email_subject(),
                properties);
        
        return smsEmptyNotificatonEmail.size() > 0 ? smsEmptyNotificatonEmail.get(0) : null;
    }
    
    private EmailBean getSmsIsLowNotificationEmail(UserBean userBean)
    {
        ProductBean oCurrentMemberLevel = WSDBAccessor.getInstance().getProductByID(userBean.getMemberLevel());
        
        String[] emailAddresses = new String[1];
        emailAddresses[0] = userBean.getEmail();
        Hashtable<String,String> properties = userBean.getProperHash();
        properties.put("resource_url", resourceURL);
        properties.put("remain_sms_credits", String.valueOf(userBean.getSms_credits()));
        properties.put("member_level_name", oCurrentMemberLevel.getName());
        properties.put("member_level_include_sms", String.valueOf(oCurrentMemberLevel.getIncludeSMS()));
        
        List<EmailBean> smsLowNotificatonEmail = HtmlEmailTools.generateEmail(htmlEmailBasedPath,
                SFResManager.getInstance().getSms_low_email_file(),
                emailAddresses,
                SFResManager.getInstance().getSms_low_email_subject(),
                properties);
        
        return smsLowNotificatonEmail.size() > 0 ? smsLowNotificatonEmail.get(0) : null;
    }
    
    private boolean notifySignalToEmail(Signal signalBean)
    {
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Call function, notifySignalToEmail. Current time: " + System.currentTimeMillis());
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Call function, notifySignalToEmail. Current time: " + System.currentTimeMillis());
                
        if (null == signalBean)
        {
            return false;
        }
        
        //get signal preference list
        List<EmailBean> oEmailList = getAllEmailListForSignal(signalBean);
        
        //send Email to them
        EmailHandler.getInstance().appendEmailToSend(oEmailList);
                
        return true;
    }
    
    private List<EmailBean> getAllEmailListForSignal(Signal signalBean)
    {
        List<EmailBean> oResult = new ArrayList<EmailBean>();
        
        if(null == signalBean)
        {
            return oResult;
        }
        
        //get all email address which need send while current signal occured
        List<UserSignalPreferenceBaseBean> oPreferenceList = WSDBAccessor.getInstance().getAllUserCustomSpecificSignal(signalBean, true);
        if( null == oPreferenceList || oPreferenceList.size() == 0)
        {
            return oResult;
        }
        
        //filter those disable email alert user
        List<UserSignalPreferenceBaseBean> oActiveEmailPreferenceList = getActiveEmailPreference(oPreferenceList);
        oPreferenceList.clear();
        
        //get signal period info
        MarketPeriodBean oPeriodBean = DataCache.getInstance().getMarketPeriodByID(new Long(signalBean.getSignal_period()));
        
        String signalName="";
        String signalEmailFile="";
        String signalPeriodName=oPeriodBean.getValue()+" "+oPeriodBean.getPeriod_name();
        String relatedStrategyName="";
        String relatedStrategyDescription="";
        
        StrategyBean oRelatedStrategy = WSDBAccessor.getInstance().getStrategyFullInfoByID(signalBean.getStrategy_id());
        if(oRelatedStrategy != null)
        {
            relatedStrategyName = oRelatedStrategy.getCommon_name();
            relatedStrategyDescription = oRelatedStrategy.getDescription();
        }
        
        //Set email subject and body
        if(signalBean.getSignal_type() == CommonDefine.LONG_SIGNAL)
        {
            signalName = "LONG SIGNAL";
            signalEmailFile= SFResManager.getInstance().getSms_long_signal_file();
        }
        else if(signalBean.getSignal_type() == CommonDefine.SHORT_SIGNAL)
        {
            signalName = "SHORT SIGNAL";
            signalEmailFile = SFResManager.getInstance().getSms_short_signal_file();
        }
        else if(signalBean.getSignal_type() == CommonDefine.CLOSE_SIGNAL)
        {
            signalName = "CLOSE SIGNAL";
            signalEmailFile = SFResManager.getInstance().getSms_close_signal_file();
        }
        else
        {
            //If signal is not BUY or SELL, then, it will be ignored
            return oResult;
        }
        
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int defaultOffsetTime = TimeZone.getDefault().getOffset(System.currentTimeMillis());
        
        //collcect user id list to reduce their sms credits
        List<Long> userIdList = new ArrayList<Long>();
        
        //create Email Bean List
        for(int i=0; i<oActiveEmailPreferenceList.size(); i++)
        {
            UserSignalPreferenceBaseBean oPreference = oActiveEmailPreferenceList.get(i);
            
            userIdList.add(oPreference.getUser_id());
            
            TimeZoneBean oTimeZone = DataCache.getInstance().getTimeZoneByID(oPreference.getTime_zone_id());            
            long timeOffsetTime = oTimeZone.getOffset()*60*60*1000 - defaultOffsetTime;
            
            String signalDate = dateTimeFormat.format(new Date((long)signalBean.getGenerate_date()+timeOffsetTime));
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Default Time zone off set time is:"+defaultOffsetTime);
            LogTools.getInstance().insertLog(DebugLevel.INFO,"User Time zone id is:"+oPreference.getTime_zone_id()+", Offset is "+oTimeZone.getOffset());
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Adjust time is:"+timeOffsetTime);
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Signal Date is:"+signalDate);
            
            EmailBean oEmail = getSignalEmail(signalName, relatedStrategyName, relatedStrategyDescription, oPreference.getNotify_email(), signalBean, signalPeriodName, signalEmailFile);
            
            oResult.add(oEmail);
        }
        oActiveEmailPreferenceList.clear();
        
        //reduce credits
        if(WSDBAccessor.getInstance().reduceUserSMSCredits(userIdList, SFResManager.getInstance().getCredit_per_email()))
        {
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Reduce users' SMS credits successfully for this signal!");
        }
        else
        {
            LogTools.getInstance().insertLog(DebugLevel.ERROR,"Reduce users' SMS credits failed for this signal!");
        }
        
        return oResult;        
    }
    
    /**
     * get all active and enable Email prefence
     * @param oAllUserSignalPreferencList
     * @return
     */
    private List<UserSignalPreferenceBaseBean> getActiveEmailPreference(List<UserSignalPreferenceBaseBean> oAllUserSignalPreferencList)
    {
        List<UserSignalPreferenceBaseBean> oActiveEmailPreferenceList = new ArrayList<UserSignalPreferenceBaseBean>();
        
        if(null == oAllUserSignalPreferencList)
        {
            return oActiveEmailPreferenceList; 
        }
        
        for(int i=0; i< oAllUserSignalPreferencList.size(); i++)
        {
            UserSignalPreferenceBaseBean oTempBean = oAllUserSignalPreferencList.get(i);
            
            if(oTempBean.getActive() && oTempBean.getEnable_email())
            {
                oActiveEmailPreferenceList.add(oTempBean);
            }
        }
        
        return oActiveEmailPreferenceList;
    }
        
    private EmailBean getSignalEmail(String signalName, String relatedStrategyName, String relatedStrategyDescrption, String emailAddress, Signal oSignal, String periodName, String emailFile)
    {
        String[] emailAddresses = new String[1];
        emailAddresses[0] = emailAddress;
        Hashtable<String,String> properties = new Hashtable<String, String>();
        properties.put("resource_url", resourceURL); 
        
        properties.put("market_name", oSignal.getMarket_name());
        properties.put("period_name", periodName);
        properties.put("signal_price", String.valueOf(oSignal.getSignalValue()));
        properties.put("strategy_name", relatedStrategyName);
        properties.put("strategy_description", relatedStrategyDescrption);
        
        //Subject Line: SHORT SIGNAL  |  MARKET  |  Strategy Name  | 5 MIN  |  1.3333
        String subject = signalName+" | "+oSignal.getMarket_name()+" | "+relatedStrategyName+" | "+periodName+" | "+oSignal.getSignalValue();
        
        List<EmailBean> smsEmptyNotificatonEmail = HtmlEmailTools.generateEmail(htmlEmailBasedPath,
                emailFile,
                emailAddresses,
                subject,
                properties);
        
        return smsEmptyNotificatonEmail.size() > 0 ? smsEmptyNotificatonEmail.get(0) : null;
    }
    
    private boolean notifySignalToMQ(Signal signalBean)
    {
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Call function, notifySignalToMQ");
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Call function, notifySignalToMQ");
                
        if (null == signalBean)
        {
            return false;
        }
        
        boolean result = false;
        
        IMessager oSenderHandle = null;
        
        try
        {
            String messageContent = signalBean.toTextMessage();
            
            //send this new data to message server                
            oSenderHandle = new JMSTopicMessager(signalBean.getMarket_topic_name());
            oSenderHandle.sendTextMessage(messageContent);
            
            InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Nofity signal message: "+messageContent);    
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Nofity signal message: "+messageContent);              
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,
                    "Exception happened in DataServiceImpl.notifySignalToMQ()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            if(oSenderHandle != null)
            {
                oSenderHandle.destory();
            }
        }
        
        return result;
    }
    
    /**
     * This method is used to send text message to JMS Topic, then,
     * JMS server will transfer data to LightStreamer, finally, the text message will publish to JS
     * @param signalBean
     * @return
     */
    private boolean notifySignalToMQToJS(Signal signalBean)
    {
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Call function, notifySignalToMQToJS. Current time: " + System.currentTimeMillis());
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Call function, notifySignalToMQToJS. Current time: " + System.currentTimeMillis());
                
        boolean result = false;
        IMessager oSenderHandle = null;
        
        try
        {
            //update signal value
            MarketPeriodBean oMarketPeriodBean = DataCache.getInstance().getMarketPeriodByID((long)signalBean.getSignal_period());
            signalBean.setSignal_period_minutes(oMarketPeriodBean.getPeriodMinutes());
            
            long signalExpireTime = signalBean.getExpire_date();
            double currentTime = System.currentTimeMillis();
            
            signalBean.setExpire_date((long)((signalExpireTime-currentTime)/1000));
            String expire_date = String.valueOf(signalBean.getExpire_date()) + " sec";
            signalBean.setExpire_date_string(expire_date);
            
            if(signalBean.getProfit() > 0)
            {
                signalBean.setProfitString("+"+signalBean.getProfit());
            }
            else if(signalBean.getProfit() < 0)
            {
                signalBean.setProfitString("-"+signalBean.getProfit()*-1);
            }
            else
            {
                signalBean.setProfitString("0");
            }
            
            if(signalBean.getId() <= 0)
            {
                // since the  signal ID is generated by DB
                // so, here set it a temp ID
                signalBean.setId(System.currentTimeMillis());
            }
            
            String messageContent = signalBean.toCompletedTextMessage();
            
            //add mark for this message
            //CommonResManager.getInstance().getSignaltojs_topic() is the subscribed item name
            messageContent = CommonResManager.getInstance().getSignaltojs_topic()+","+messageContent;
            
            //System.out.println("Send mockup signal to JMS server, message content is "+messageContent);
            
            //send this new data to message server                
            oSenderHandle = new JMSTopicMessager(CommonResManager.getInstance().getSignaltojs_topic());
            oSenderHandle.sendTextMessage(messageContent);
            
            InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Nofity signal message to JS: "+messageContent);    
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Nofity signal message to JS: "+messageContent);              
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,
                    "Exception happened in DataServiceImpl.notifySignalToMQToJS()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            if(oSenderHandle != null)
            {
                oSenderHandle.destory();
            }
        }
        
        return result;
    }
    
    protected Signal createSignal(long signalDate, int signalType, long marketID, long periodID, double signalValue)
    {
        Signal oNewSignal = new Signal();
        
        oNewSignal.setDirection(CommonDefine.BUY_LONG);
        oNewSignal.setSignalValue(signalValue);
        oNewSignal.setGenerate_date(Utils.getTimeByPeriodType(signalDate, (int)periodID));
        oNewSignal.setGenerate_date_string(Utils.getDateString(signalDate, (int)periodID));
        
        //attention: the expire date must be the Current Time + Period Time. Never use generate time + period time
        //because the generate time is used to shown on Chart, and it is NOT the exact time
        oNewSignal.setExpire_date(System.currentTimeMillis()+Utils.getMinutesByPeriodType((int)periodID)*60*1000);
        oNewSignal.setExpire_date_string(Utils.getDateString(oNewSignal.getExpire_date(), (int)periodID));
                
        oNewSignal.setMarket_name(DataCache.getInstance().getMarketTypeByID(marketID).getDisplay_name());
        oNewSignal.setMarket_topic_name(DataCache.getInstance().getMarketTypeByID(marketID).getMarket_name());
        oNewSignal.setMarket_type_id(marketID);
        
        oNewSignal.setProfit(0.0);
        oNewSignal.setSignal_period((int)periodID);
        oNewSignal.setSignal_period_minutes(Utils.getMinutesByPeriodType((int)periodID));
        oNewSignal.setSignal_period_name(DataCache.getInstance().getMarketPeriodByID(periodID).getPeriod_name());
        oNewSignal.setSignal_rate(0);
        
        oNewSignal.setSignal_type(signalType);
        oNewSignal.setStrategy_id(getStrategyID());
        oNewSignal.setSystem_name("cleartraders");
        
        return oNewSignal;
    }
    
    public long getStrategyID()
    {
        return strategyID;
    }

    public void setStrategyID(long strategyID)
    {
        this.strategyID = strategyID;
    }

    public boolean isActive()
    {
        StrategyBean strategyBean = StrategyManager.getInstance().getStrategy(this.strategyID);
        
        if(null != strategyBean && CommonDefine.ACTIVE_STRATEGY == strategyBean.getActive())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public void active(boolean status)
    {
        
    }
    
    public String getStrategySystemName()
    {
        return strategySystemName;
    }

    public void setStrategySystemName(String strategySystemName)
    {
        this.strategySystemName = strategySystemName;
    }
    
    public boolean isRealtime()
    {
        return _isRealtime;
    }
    public void setRealtime(boolean realtime)
    {
        _isRealtime = realtime;
    }
}
