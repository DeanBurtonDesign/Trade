package com.cleartraders.webapp.model.signup;

import java.util.ArrayList;
import java.util.List;

import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.MarketTypeAndPeriodBean;
import com.cleartraders.common.entity.MarketTypeBean;
import com.cleartraders.common.entity.ProductBean;
import com.cleartraders.common.entity.QuickLinkBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.exception.DataInvalideException;
import com.cleartraders.common.exception.EmailDuplicatedException;
import com.cleartraders.common.exception.MobileDuplicatedException;
import com.cleartraders.common.util.email.EmailBean;
import com.cleartraders.common.util.email.EmailHandler;
import com.cleartraders.common.util.sms.SMSBean;
import com.cleartraders.common.util.sms.SMSHandler;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.config.WebappResManager;
import com.cleartraders.webapp.db.DBAccessor;
import com.cleartraders.webapp.model.notification.EmailNotificationFactory;
import com.cleartraders.webapp.model.notification.EmailNotificationHandler;

public class SignupController
{
    public boolean checkUserConfirmed(UserBean userBean, String confirmedString)
    {
        boolean result = false;
        
        if( userBean != null 
           && userBean.getConfirm_code().equals(confirmedString) 
           && userBean.getEnable() == CommonDefine.USER_NOT_ENABLED)
        {
            long periodTime = userBean.getExpired_date() - userBean.getReg_date();
            String originalPwd = userBean.getPwd();
                
            userBean.setStatus(CommonDefine.REGISTERED_USER);
            userBean.setEnable(CommonDefine.USER_ENABLED);
            userBean.setReg_date(System.currentTimeMillis());
            userBean.setExpired_date(userBean.getReg_date()+periodTime);
            
            result = DBAccessor.getInstance().updateUserStatusInfo(userBean);
            
            if(result)
            {
                DBAccessor.getInstance().updateUserPWDByUserID(userBean.getId(), CommonTools.encryptSHA(originalPwd));
                
                //create user's notifications
                EmailNotificationHandler.getInstance().createNotificationSchedulersForUser(userBean);
                
                //send signup email to user
                sendSignupEmailToFreeTrialUser(userBean, originalPwd);
                
                //send SMS notification to user after free trial user click confirm link
                sendSignupSMSToFreeTrialUser(userBean);
            }
        }
        
        return result;
    }
    
    public boolean registerNormalUser(UserBean userBean) throws EmailDuplicatedException, DataInvalideException
    {
        if(null == userBean)
        {
            throw new DataInvalideException("Data Error",-1);
        }
        
        boolean result = false;
        
        //check new user info is allowed or not
        UserBean oExistedUser = DBAccessor.getInstance().getUserBeanByEmailOrLoginName(userBean.getLogin_name());
        if(oExistedUser != null && oExistedUser.getId() > 0)
        {
            //if this user is NOT enabled user Or is Expired user
            //then delete this existed user first
            if(oExistedUser.getExpired_date() < System.currentTimeMillis() || oExistedUser.getEnable() != CommonDefine.USER_ENABLED)
            {
                //update user's status info
                DBAccessor.getInstance().updateUserStatusInfo(userBean);
                
                //don't need to remove user here, Only Admin can remove user
                //in this case, system just update user register info which are from Signup Form
                userBean.setId(oExistedUser.getId());
                
                //if last login is 0, then, after user payment completed, system will notify email and update encrypted password
                userBean.setLast_login(0);
                
                return DBAccessor.getInstance().updateUserSignupInfo(userBean);
            }
            else
            {
                throw new EmailDuplicatedException("Email Error",-1);
            }
        }
        
        //get user Market list
        ProductBean productBean = DBAccessor.getInstance().getProductByID(userBean.getMemberLevel());        
        if(productBean == null || productBean.getActive() == CommonDefine.INACTIVE_PRODUCT)
        {
            throw new DataInvalideException("Data Error",-1);
        }
                
        //store into DB
        List<MarketTypeAndPeriodBean> marketIDList = getMarketsByProductInfo(productBean);
        
        List<QuickLinkBean> oDefaultQuickLinkBeans = new ArrayList<QuickLinkBean>();
        QuickLinkBean oDefaultQuickLinkBean = new QuickLinkBean();
        oDefaultQuickLinkBean.setIndex(1);
        oDefaultQuickLinkBean.setType(WebConstants.NEWS_QUICK_LINK);
        oDefaultQuickLinkBean.setName(WebappResManager.getInstance().getDefault_quick_link_name());
        oDefaultQuickLinkBean.setUrl(WebappResManager.getInstance().getDefault_quick_link_url());
        
        oDefaultQuickLinkBeans.add(oDefaultQuickLinkBean);
        
        result = DBAccessor.getInstance().storeUserIntoDB(userBean,marketIDList,oDefaultQuickLinkBeans);
                
        return result;
    }
    
    public boolean registerFreeTrialUser(UserBean userBean) throws EmailDuplicatedException, DataInvalideException, MobileDuplicatedException
    {
        if(null == userBean)
        {
            throw new DataInvalideException("Data Error",-1);
        }
        
        boolean result = false;
        
        //check new user info is allowed or not
        //Free trial user can not use same email for two times. Therefore, if there is any users ( Enable and non-Enable ) used this email, 
        //then, system don't allow user to register
        UserBean oExistedUserByEmail = DBAccessor.getInstance().getUserBeanByEmailOrLoginName(userBean.getEmail());
        if(oExistedUserByEmail != null && oExistedUserByEmail.getId() > 0)
        {
            throw new EmailDuplicatedException("Email duplicated Error",-1);
        }
        
        //check Mobile number, if someone else used same one, then report Error
        //Free trial user can not use same Mobile Phone for two times. Therefore, if there is any users ( Enable and non-Enable ) used this Mobile Phone, 
        //then, system don't allow user to register
        UserBean oExistedUserByMobile = DBAccessor.getInstance().getUserBeanByMobileNumber(userBean.getMobile());
        if( null != oExistedUserByMobile && oExistedUserByMobile.getId() > 0)
        {
            throw new MobileDuplicatedException("Mobile duplicated Error",-1);
        }
        
        //get user Market list
        ProductBean productBean = DBAccessor.getInstance().getProductByID(userBean.getMemberLevel());        
        if(productBean == null || productBean.getActive() == CommonDefine.INACTIVE_PRODUCT)
        {
            throw new DataInvalideException("Data Error",-1);
        }
        
        //set expired date
        long expiredDate = userBean.getReg_date() + productBean.getPeriod()*WebConstants.MINI_SECONDS_EACH_DAY;
        userBean.setExpired_date(expiredDate);
        userBean.setConfirm_code(CommonTools.encryptSHA(userBean.getConfirm_code()+System.currentTimeMillis()));
        
        //store into DB
        List<MarketTypeAndPeriodBean> marketIDList = getMarketsByProductInfo(productBean);
        
        //according to Dean (2009-05-13), only active EUR/USD for Free Trial user
        String marketName = WebappResManager.getInstance().getFree_trial_user_first_market();
        long marketID = DataCache.getInstance().getMarketTypeByDisplayName(marketName).getId();
        
        for(int i=0; i<marketIDList.size(); i++)
        {
            MarketTypeAndPeriodBean oMarketPeriodBean = marketIDList.get(i);
            if(oMarketPeriodBean.getMarketID() == marketID)
            {
                oMarketPeriodBean.setActive(true);
            }
            else
            {
                oMarketPeriodBean.setActive(false);
            }
        }
        
        List<QuickLinkBean> oDefaultQuickLinkBeans = new ArrayList<QuickLinkBean>();
        QuickLinkBean oDefaultQuickLinkBean = new QuickLinkBean();
        oDefaultQuickLinkBean.setIndex(1);
        oDefaultQuickLinkBean.setType(WebConstants.NEWS_QUICK_LINK);
        oDefaultQuickLinkBean.setName(WebappResManager.getInstance().getDefault_quick_link_name());
        oDefaultQuickLinkBean.setUrl(WebappResManager.getInstance().getDefault_quick_link_url());
        
        oDefaultQuickLinkBeans.add(oDefaultQuickLinkBean);
        
        result = DBAccessor.getInstance().storeUserIntoDB(userBean,marketIDList,oDefaultQuickLinkBeans);
        
        if(result)
        {
            //before user confirm link, the password is stored NOT Encripted
            sendConfirmEmailToFreetrialUser(userBean);
        }
        
        return result;
    }
    
    public List<MarketTypeAndPeriodBean> getMarketsByProductInfo(ProductBean productBean)
    {
        //update at 2009-5-14, regardless that user's membership includes how many markets
        //system should add all markets to user, but ONLY active Markets according to membership
        List<MarketTypeAndPeriodBean> marketTypeAndPeriodList = new ArrayList<MarketTypeAndPeriodBean>();
        
        //first active market
        String firstMarketName = WebappResManager.getInstance().getFree_trial_user_first_market();
        String firstMarketPeriodId = WebappResManager.getInstance().getFree_trial_user_first_market_peirod();
        long firstMarketID = DataCache.getInstance().getMarketTypeByDisplayName(firstMarketName).getId();
        boolean enabledFirstMarket = false;
        
        //total market of membership
        int marketAmountOfMembership = productBean.getTotalMarkets();
        
        //all market size
        int allMarkets = DataCache.getInstance().getAllMarketType().size();
        
        int enabledMarketNumber = 0;
        
        for(int i=0; i<allMarkets; i++)
        {
            MarketTypeBean marketType = DataCache.getInstance().getAllMarketType().get(i);
            
            MarketTypeAndPeriodBean marketTypeAndPeriod = new MarketTypeAndPeriodBean();
            marketTypeAndPeriod.setActive(false);
            marketTypeAndPeriod.setMarketID(marketType.getId());
            marketTypeAndPeriod.setMarketPeriodID(Long.valueOf(firstMarketPeriodId));
            marketTypeAndPeriod.setStrategyID(getDefaultStrategyID(marketType.getMarket_type()));
            
            if(enabledMarketNumber < marketAmountOfMembership-1)
            {                
                marketTypeAndPeriod.setActive(true);
                
                enabledMarketNumber++;
                
                if(marketType.getId() == firstMarketID)
                {
                    enabledFirstMarket=true;
                }
            }
            else if(enabledMarketNumber == marketAmountOfMembership-1)
            {
                if(enabledFirstMarket)
                {
                    marketTypeAndPeriod.setActive(true);
                    
                    enabledMarketNumber++;
                }
                else if(marketType.getId() == firstMarketID)
                {
                    marketTypeAndPeriod.setActive(true);
                    
                    enabledMarketNumber++;                   
                    enabledFirstMarket=true;                    
                }
            }
                        
            marketTypeAndPeriodList.add(marketTypeAndPeriod);
        }
        
        return marketTypeAndPeriodList;
    }
    
    private long getDefaultStrategyID(long marketTypeID)
    {
        List<Long> allAvailableStrategyID = DBAccessor.getInstance().getStrategyIDByMarketID(marketTypeID);
        
        long defaultStrategyID = Integer.parseInt(WebappResManager.getInstance().getDefault_strategy_id());
        if(null == allAvailableStrategyID || allAvailableStrategyID.size() == 0)
        {
            return 0;
        }
        
        for(int i=0; i<allAvailableStrategyID.size(); i++)
        {
            if(allAvailableStrategyID.get(i) == defaultStrategyID)
            {
                return defaultStrategyID;
            }
        }
        
        return allAvailableStrategyID.get(0);
    }
    
    private void sendSignupSMSToFreeTrialUser(UserBean userBean)
    {
        SMSBean oSMS = new SMSBean();
        oSMS.setReceiverMobile(userBean.getMobile());
        oSMS.setSmsContent(WebappResManager.getInstance().getFree_trial_sms_notification());
        
        SMSHandler.getInstance().appendSMSToSend(oSMS);
    }
    
    private void sendSignupEmailToFreeTrialUser(UserBean userBean, String originalPassword)
    {
        //email should contain NO encypted password
        UserBean newUserBean = new UserBean(userBean);
        newUserBean.setPwd(originalPassword);
        
        EmailHandler.getInstance().appendEmailToSend(EmailNotificationFactory.getNotificationEmail(newUserBean,WebConstants.FREE_TRIAL_USER_SIGNUP_TEMPLATE));
    }
    
    private void sendConfirmEmailToFreetrialUser(UserBean userBean)
    {
        //email should contain NO encypted password
        UserBean newUserBean = new UserBean(userBean);
        
        EmailBean oConfirmEmail = EmailNotificationFactory.getNotificationEmail(newUserBean,WebConstants.FREE_TRIAL_USER_CONFIRM_TEMPLATE);
        EmailHandler.getInstance().appendEmailToSend(oConfirmEmail);
        
        //test spam checker
//        EmailBean oCheckEmail = new EmailBean(oConfirmEmail);
//        oCheckEmail.setEmailSubject("TEST"+oConfirmEmail.getEmailSubject());
//        oCheckEmail.setRecipients("spamcheck-faul@sitesell.net");
//        EmailHandler.getInstance().appendEmailToSend(oCheckEmail);
        //end test
    }
}
