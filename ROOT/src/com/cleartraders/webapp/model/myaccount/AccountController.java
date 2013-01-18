package com.cleartraders.webapp.model.myaccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.MarketPeriodBean;
import com.cleartraders.common.entity.MarketStrategyBean;
import com.cleartraders.common.entity.MarketTypeBean;
import com.cleartraders.common.entity.ProductBean;
import com.cleartraders.common.entity.QuickLinkBean;
import com.cleartraders.common.entity.SmsPackageBean;
import com.cleartraders.common.entity.StrategyBean;
import com.cleartraders.common.entity.StrategyTimeframeBean;
import com.cleartraders.common.entity.TimeZoneBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.entity.UserSignalPreferenceBaseBean;
import com.cleartraders.common.exception.EmailDuplicatedException;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.db.DBAccessor;
import com.cleartraders.webapp.model.bean.SignalNotificationBean;
import com.cleartraders.webapp.model.bean.UserBrokerInfo;
import com.cleartraders.webapp.model.bean.UserSignalPreferenceBean;
import com.cleartraders.webapp.model.login.LoginController;

public class AccountController
{
    public StrategyBean getStrategyByID(long strategyID)
    {
        return DBAccessor.getInstance().getStrategyFullInfoByID(strategyID);
    }
    
    public List<SmsPackageBean> getAllSmsPackages()
    {        
        return DBAccessor.getInstance().getAllSMSPackages();
    }
    
    public List<SmsPackageBean> getAllSmsPackages(UserBean userBean)
    {        
        return DBAccessor.getInstance().getAllSMSPackages(userBean.getMemberLevel());
    }
    
    public long getUserSmsCredits(UserBean oUserBean)
    {
        if(null == oUserBean)
        {
            return 0;
        }
        
        return DBAccessor.getInstance().getUserSmsCredits(oUserBean.getId());
    }
    
    public List<ProductBean> getAllProducts()
    {
        List<ProductBean> oResult = new ArrayList<ProductBean>();
        
        List<ProductBean> oAllProducts = DataCache.getInstance().getAllProduct();
        
        for(int i=0; i<oAllProducts.size(); i++)
        {
            if(oAllProducts.get(i).getActive() != CommonDefine.INACTIVE_PRODUCT)
            {
                oResult.add(oAllProducts.get(i));
            }
        }
        
        return oResult;
    }
    
    public ProductBean getProductByID(long productID)
    {
        return DBAccessor.getInstance().getProductByID(productID);
    }
    
    public ProductBean getMyMembership(UserBean oUserBean)
    {
        return DBAccessor.getInstance().getProductByID(oUserBean.getMemberLevel());
    }
    
    
    public boolean updateMarketStrategy(UserBean oUserBean,String preferenceID,String strategyID)
    {
        return DBAccessor.getInstance().updateMarketStrategy(oUserBean,preferenceID,strategyID);
    }
    
    public boolean updateMarketTimeFrame(UserBean oUserBean,String preferenceID,String timePeriodType)
    {
        return DBAccessor.getInstance().updateMarketTimeFrame(oUserBean,preferenceID,timePeriodType);
    }
    
    public UserBrokerInfo getUserBrokerFee(UserBean oUserBean)
    {
        return DBAccessor.getInstance().getUserBrokerFee(oUserBean);
    }
    
    public boolean updateUserBrokerFee(UserBean oUserBean, double brokerFee)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        }
        
        return DBAccessor.getInstance().updateUserBrokerFee(oUserBean,brokerFee);
    }
    
    public boolean updateUserTimeZoneByOffSet(UserBean oUserBean, int offset)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        } 

        //get time zone id by offset value from datacache
        long timeZoneID = 0;
        List<TimeZoneBean> oList = DataCache.getInstance().getAllTimeZone();
        for(int i=0; i<oList.size(); i++)
        {
            TimeZoneBean oTempBean = oList.get(i);
            
            if(oTempBean.getOffset() == offset)
            {
                timeZoneID = oTempBean.getId();
                break;
            }
        }
        
        boolean updateTimeZoneResult = DBAccessor.getInstance().updateUserTimeZone(oUserBean,timeZoneID);
            
        if(updateTimeZoneResult)
        {
            //udpate Session info
            oUserBean.setTime_zone_id(timeZoneID);
        }
        
        return updateTimeZoneResult;
    }
    
    public boolean updateUserQuickLinks(UserBean oUserBean, List<QuickLinkBean> oLinksList)
    {
        if(null == oUserBean || oUserBean.getId() < 0 || null == oLinksList)
        {
            return false;
        } 
        
        return DBAccessor.getInstance().updateUserQuickLinks(oUserBean,oLinksList);
    }
    
    public boolean updateAllSignalNotification(UserBean oUserBean, SignalNotificationBean oSignalAlert)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        } 
                
        return DBAccessor.getInstance().updateAllSignalNotification(oUserBean,oSignalAlert);
    }
    
    public TimeZoneBean getUserTimeZone(UserBean oUserBean)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return new TimeZoneBean();
        } 
        
        return DBAccessor.getInstance().getUserTimeZone(oUserBean);
    }
    
    public Map<Long, StrategyBean> getAllStrategy()
    {
        Map<Long, StrategyBean> oResult = new HashMap<Long, StrategyBean>();
        
        List<StrategyBean> oAllStrategy = DBAccessor.getInstance().getAllStrategyBaseInfo(); 
        if(null == oAllStrategy || oAllStrategy.size() ==0)
        {
            return oResult;
        }
        
        for(int i=0; i<oAllStrategy.size(); i++)
        {
            StrategyBean oStrategy = oAllStrategy.get(i);
            
            oResult.put(Long.valueOf(oStrategy.getId()), oStrategy);
        }
        
        return oResult;
    }
    
    public List<MarketTypeBean> getAllActiveMarkets(UserBean oUserBean)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return new ArrayList<MarketTypeBean>();
        } 
        
        return DBAccessor.getInstance().getAllActiveMarkets(oUserBean);
    }
    
    public List<QuickLinkBean> getAllQuickLinks(UserBean oUserBean)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return new ArrayList<QuickLinkBean>();
        } 
        
        List<QuickLinkBean> oDBQuickLinks =  DBAccessor.getInstance().getAllQuickLinks(oUserBean);
        List<QuickLinkBean> oReturnQuickLinks = new ArrayList<QuickLinkBean>();
        
        //if the Link name is empty, then ignore it
        for(int i=0;i<oDBQuickLinks.size(); i++)
        {
            QuickLinkBean oTempBean = oDBQuickLinks.get(i);
            
            if(oTempBean.getName().trim().length() > 0)
            {
                oReturnQuickLinks.add(oTempBean);
            }
        }
        
        return oReturnQuickLinks;
    }
    
    public List<SignalNotificationBean> getAllSignalNotification(UserBean oUserBean)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return new ArrayList<SignalNotificationBean>();
        } 
        
        return DBAccessor.getInstance().getAllSignalNotification(oUserBean);
    }
    
    public boolean updateMySignalEnableSetting(UserBean oUserBean,long mysignalID,int iActiveFlag,int iSmsFlag,int iEmailFlag)
    {
        return DBAccessor.getInstance().updateMySignalEnableSetting(oUserBean,mysignalID,iActiveFlag,iSmsFlag,iEmailFlag);
    }
    
    public boolean removeMySignalSetting(UserBean oUserBean,long mysignalID)
    {
        return DBAccessor.getInstance().removeMySignalSetting(oUserBean,mysignalID);
    }
    
    public boolean changeMarketSubscription(UserBean oUserBean,long previousMarketID,long newMarketID,long newSignalPeriodID,long newStrategyID)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        } 
        
        //Note: change is to update
        return DBAccessor.getInstance().updateMarketSubscription(oUserBean,previousMarketID,newMarketID,newSignalPeriodID,newStrategyID);
    }
    
    public boolean updateSignalAlertType(UserBean oUserBean,long newMarketID,long newSignalPeriodID,long newStrategyID,int alertType)    
    {
        //same as Add Signal Subscription
        return addSignalSubscription(oUserBean, newMarketID, newSignalPeriodID, newStrategyID, alertType);
    }
    
    public boolean delSignalSubscription(UserBean oUserBean,long strategyId, long marketId, long periodId)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        } 
        
        UserSignalPreferenceBaseBean oUserSignalPreference = new UserSignalPreferenceBaseBean();
        oUserSignalPreference.setUser_id(oUserBean.getId());
        oUserSignalPreference.setStrategy_id(strategyId);
        oUserSignalPreference.setMarket_type_id(marketId);
        oUserSignalPreference.setMarket_period_id(periodId);
        
        DBAccessor.getInstance().removeMarketSubscription(oUserSignalPreference);
        
        return true;
    }
    
    public boolean editSignalSubscription(UserBean oUserBean,String subscriptionId, long newMarketID,long newSignalPeriodID,long newStrategyID, int alertType)
    {
        if(subscriptionId != null && subscriptionId.split(":").length == 3)
        {
            long strategyId = Long.parseLong(subscriptionId.split(":")[0]);
            long marketId = Long.parseLong(subscriptionId.split(":")[1]);
            long periodId = Long.parseLong(subscriptionId.split(":")[2]);
            
            AccountController accountController = new AccountController();
            accountController.delSignalSubscription(oUserBean, strategyId, marketId, periodId);
        }
        
        //same as Add Signal Subscription
        return addSignalSubscription(oUserBean, newMarketID, newSignalPeriodID, newStrategyID, alertType);
    }
    
    public boolean addSignalSubscription(UserBean oUserBean,long newMarketID,long newSignalPeriodID,long newStrategyID, int alertType)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        } 
        
        //get notification email and mobile number
        List<SignalNotificationBean> oUserSignalNotification= DBAccessor.getInstance().getAllSignalNotification(oUserBean);
        if(oUserSignalNotification != null && oUserSignalNotification.size() > 0)
        {
            SignalNotificationBean oFirstNotificationBean = oUserSignalNotification.get(0);
            
            //create user_signal_preference bean to add
            UserSignalPreferenceBaseBean oUserSignalPreference = new UserSignalPreferenceBaseBean();
            oUserSignalPreference.setActive(true);
            oUserSignalPreference.setMarket_type_id(newMarketID);
            
            if(CommonDefine.ALERT_BY_EMAIL == alertType)
            {
                oUserSignalPreference.setEnable_email(true);
            }
            else
            {
                oUserSignalPreference.setEnable_email(false);
            }
            
            if(CommonDefine.ALERT_BY_SMS == alertType)
            {
                oUserSignalPreference.setEnable_sms(true);
            }
            else
            {
                oUserSignalPreference.setEnable_sms(false);
            }
            
            oUserSignalPreference.setMarket_period_id(newSignalPeriodID);
            oUserSignalPreference.setStrategy_id(newStrategyID);
            oUserSignalPreference.setNotify_email(oFirstNotificationBean.getEmail());
            oUserSignalPreference.setNotify_sms(oFirstNotificationBean.getMobile());
            oUserSignalPreference.setUser_id(oUserBean.getId());
            oUserSignalPreference.setSignal_type(WebConstants.BUY_SIGNAL_TYPE);
            
            //remove first 
            DBAccessor.getInstance().removeMarketSubscription(oUserSignalPreference);
            
            //add buy&sell signal by only recorder            
            DBAccessor.getInstance().addMarketSubscription(oUserSignalPreference);
            
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean addMarketSubscription(UserBean oUserBean,long newMarketID,long newSignalPeriodID,long newStrategyID)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        } 
        
        //get notification email and mobile number
        List<SignalNotificationBean> oUserSignalNotification= DBAccessor.getInstance().getAllSignalNotification(oUserBean);
        if(oUserSignalNotification != null && oUserSignalNotification.size() > 0)
        {
            SignalNotificationBean oFirstNotificationBean = oUserSignalNotification.get(0);
            
            //create user_signal_preference bean to add
            UserSignalPreferenceBaseBean oUserSignalPreference = new UserSignalPreferenceBaseBean();
            oUserSignalPreference.setActive(true);
            oUserSignalPreference.setMarket_type_id(newMarketID);
            oUserSignalPreference.setEnable_email(true);
            oUserSignalPreference.setEnable_sms(true);
            oUserSignalPreference.setMarket_period_id(newSignalPeriodID);
            oUserSignalPreference.setStrategy_id(newStrategyID);
            oUserSignalPreference.setNotify_email(oFirstNotificationBean.getEmail());
            oUserSignalPreference.setNotify_sms(oFirstNotificationBean.getMobile());
            oUserSignalPreference.setUser_id(oUserBean.getId());
            oUserSignalPreference.setSignal_type(WebConstants.BUY_SIGNAL_TYPE);
            
            //add buy&sell signal by only recorder
            DBAccessor.getInstance().addMarketSubscription(oUserSignalPreference);
            
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public List<UserSignalPreferenceBaseBean> getActiveUserSignalPreferenceBean(UserBean oUserBean)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return new ArrayList<UserSignalPreferenceBaseBean>();
        } 
        
        return DBAccessor.getInstance().getUserSignalPreferenceBean(oUserBean,true);
    }
    
    public List<UserSignalPreferenceBaseBean> getUserSignalPreferenceBaseBean(UserBean oUserBean, boolean checkActive)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return new ArrayList<UserSignalPreferenceBaseBean>();
        }     
        
        List<UserSignalPreferenceBaseBean> oResult = DBAccessor.getInstance().getUserSignalPreferenceBean(oUserBean,checkActive);
        
        if(null == oResult)
        {
            return new ArrayList<UserSignalPreferenceBaseBean>();
        }
        
        return oResult;
    }
    
    public Map<Long, List<StrategyBean>> getMarketStrategyMap()
    {
        Map<Long, List<StrategyBean>> result = new HashMap<Long, List<StrategyBean>>();
        
        List<MarketStrategyBean> allMarketStrategy = DBAccessor.getInstance().getAllMarketStrategyData();
        for(int i=0; i<allMarketStrategy.size(); i++)
        {
            MarketStrategyBean marketStrategy = allMarketStrategy.get(i);
            
            Long marketID = Long.valueOf(marketStrategy.getMarket_id());
            
            if(result.get(marketID) != null)
            {
                List<StrategyBean> relatedStrategy = result.get(marketID);
                relatedStrategy.add(DBAccessor.getInstance().getStrategyFullInfoByID(marketStrategy.getStrategy_id()));
            }
            else
            {
                List<StrategyBean> relatedStrategy = new ArrayList<StrategyBean>();
                relatedStrategy.add(DBAccessor.getInstance().getStrategyFullInfoByID(marketStrategy.getStrategy_id()));
                
                result.put(marketID, relatedStrategy);
            }
        }
        
        return result;
    }
    
    public Map<Long, List<MarketPeriodBean>> getStrategyTimeframeMap()
    {
        Map<Long, List<MarketPeriodBean>> result = new HashMap<Long, List<MarketPeriodBean>>();
        
        List<StrategyTimeframeBean> allStrategyTimeframe = DBAccessor.getInstance().getAllStrategyTimeframeData();
        for(int i=0; i<allStrategyTimeframe.size(); i++)
        {
            StrategyTimeframeBean strategyTimeframe = allStrategyTimeframe.get(i);
            
            Long strategyID = Long.valueOf(strategyTimeframe.getStrategy_id());
            
            if(result.get(strategyID) != null)
            {
                List<MarketPeriodBean> relatedPeriod = result.get(strategyID);
                relatedPeriod.add(DataCache.getInstance().getMarketPeriodByID(strategyTimeframe.getPeriod_id()));
            }
            else
            {
                List<MarketPeriodBean> relatedPeriod = new ArrayList<MarketPeriodBean>();
                relatedPeriod.add(DataCache.getInstance().getMarketPeriodByID(strategyTimeframe.getPeriod_id()));
                
                result.put(strategyID, relatedPeriod);
            }
        }
        
        return result;
    }
    
    public int getTotalMarketsOfUser(UserBean oUserBean)
    {
        if(null == oUserBean)
        {
            return 0;
        }
        
        ProductBean oUsersProduct = DBAccessor.getInstance().getProductByID(oUserBean.getMemberLevel());
        if(null == oUsersProduct)
        {
            return 0;
        }
        
        return oUsersProduct.getTotalMarkets();
    }
    
    public List<UserSignalPreferenceBean> getUserSignalPreferenceBean(UserBean oUserBean, boolean checkActive)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return new ArrayList<UserSignalPreferenceBean>();
        }     
        
        List<UserSignalPreferenceBaseBean> oResult = DBAccessor.getInstance().getUserSignalPreferenceBean(oUserBean,checkActive);
        
        if(null == oResult)
        {
            return new ArrayList<UserSignalPreferenceBean>();
        }
        
        List<UserSignalPreferenceBean> finalResult = new ArrayList<UserSignalPreferenceBean>();
        
        for (int i=0; i<oResult.size(); i++)
        {
            UserSignalPreferenceBaseBean oBean = oResult.get(i);
            
            UserSignalPreferenceBean finalBean = new UserSignalPreferenceBean();
            
            finalBean.setId(oBean.getId());
            finalBean.setActive(oBean.getActive());
            finalBean.setMarketID(oBean.getMarket_type_id());
            finalBean.setStrategy_id(oBean.getStrategy_id());
            
            //String marketTypeName = "";
            String marketDisplayName = "";
            if(null != DataCache.getInstance().getMarketTypeByID(oBean.getMarket_type_id()))
            {
                //marketTypeName = DataCache.getInstance().getMarketTypeByID(oBean.getMarket_type_id()).getMarket_type_name();
                marketDisplayName = DataCache.getInstance().getMarketTypeByID(oBean.getMarket_type_id()).getDisplay_name();
            }
            
            //String marketName = marketTypeName + "-" + marketDisplayName;
            finalBean.setMarketName(marketDisplayName);
            finalBean.setNotifyByEmail(oBean.getEnable_email());
            finalBean.setNotifyBySMS(oBean.getEnable_sms());
            finalBean.setNotifyEmail(oBean.getNotify_email());
            finalBean.setNotifySMS(oBean.getNotify_sms());
            finalBean.setTimePeriodName(DataCache.getInstance().getMarketPeriodByID(oBean.getMarket_period_id()).getPeriod_name());
            //finalBean.setTimePeriodType(DataCache.getInstance().getMarketPeriodByID(oBean.getMarket_period_id()).getPeriod_type());
            finalBean.setTimePeriodType((int)oBean.getMarket_period_id());
            finalBean.setTimePeriodValue(DataCache.getInstance().getMarketPeriodByID(oBean.getMarket_period_id()).getValue());
            finalBean.setUserID(oUserBean.getId());
            
            finalResult.add(finalBean);
        }
        
        return finalResult;
    }
    
    public void checkEmail(UserBean oUserBean, String currentEmail, String newEmail) throws EmailDuplicatedException
    {
        if(currentEmail.equals(newEmail))
        {
            //don't need to check, since it hasn't been changed
            return;
        }
        
        UserBean oExistedUser = DBAccessor.getInstance().getUserBeanByEmail(newEmail);
        if(oExistedUser != null && oExistedUser.getId() > 0)
        {
            //if this user is NOT enabled user Or is Expired user
            //then delete this existed user first
//            if(oExistedUser.getExpired_date() < System.currentTimeMillis() || oExistedUser.getEnable() != CommonDefine.USER_ENABLED)
//            {
//                //ingore this disable user's email
//            }
//            else
//            {
                throw new EmailDuplicatedException("Email Error",-1);
//            }
        }
    }
    
    public boolean updateUserContacts(UserBean oUserBean)
    {
        if(null == oUserBean || oUserBean.getId() < 0)
        {
            return false;
        }     
        
        //if user enable Use Contact for signal alerts
        //then we should update it also
        List<SignalNotificationBean> signalAlerts = getAllSignalNotification(oUserBean);
        if( null != signalAlerts && signalAlerts.size() > 0)
        {
            SignalNotificationBean oSignalAlert = signalAlerts.get(0);
            
            if(oSignalAlert.getUse_contact() == WebConstants.ENABLE)
            {
                oSignalAlert.setEmail(oUserBean.getEmail());
                oSignalAlert.setMobile(oUserBean.getMobile());
                
                updateAllSignalNotification(oUserBean,oSignalAlert);
            }
        }
        
        return DBAccessor.getInstance().updateUserContacts(oUserBean);
    }
    
    public boolean checkCurrentPassword(String userName, String password)
    {
        return new LoginController().loginCheck(userName,password);
    }
    
    public boolean changeUserPWD(long userID, String newPassword)
    {        
        if(userID < 0 || null == newPassword || "".equals(newPassword))
        {
            return false;
        }
                
        //calculate encrypted password
        String enryptedPassword = CommonTools.encryptSHA(newPassword);
        
        return DBAccessor.getInstance().updateUserPWDByUserID(userID,enryptedPassword);
    }
}
