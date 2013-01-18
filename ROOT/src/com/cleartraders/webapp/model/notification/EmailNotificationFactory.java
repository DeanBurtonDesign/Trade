package com.cleartraders.webapp.model.notification;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.cleartraders.common.entity.ProductBean;
import com.cleartraders.common.entity.SmsPackageBean;
import com.cleartraders.common.entity.TempPasswordBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.email.EmailBean;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.common.util.tools.HtmlEmailTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.config.WebappResManager;
import com.cleartraders.webapp.db.DBAccessor;

/**
 * Manage all email template (Email content)
 * @author Administrator
 *
 */
public class EmailNotificationFactory
{
    private static String htmlEmailBasedPath = CommonTools.getSysDir()+ "resource" + File.separator + "emailtemplate" + File.separator;   
    private static String resourceURL = WebappResManager.getInstance().getHtml_email_resource_url();
        
    private static EmailBean getUpgradeMembershipNotificationEmail(UserBean userBean)
    {
        ProductBean oCurrentMemberLevel = DBAccessor.getInstance().getProductByID(userBean.getMemberLevel());
        int iTotalMarkets = DBAccessor.getInstance().getAllMarketTypeData().size();
        
        String[] emailAddresses = new String[1];
        emailAddresses[0] = userBean.getEmail();
        Hashtable<String,String> properties = userBean.getProperHash();
        properties.put("resource_url", resourceURL);
        properties.put("member_level_name", oCurrentMemberLevel.getName());
        properties.put("member_level_cost", String.valueOf(oCurrentMemberLevel.getPrice()));
        properties.put("member_level_include_sms", String.valueOf(oCurrentMemberLevel.getIncludeSMS()));
        properties.put("total_markets", String.valueOf(iTotalMarkets));
        
        List<EmailBean> upgradeNotificatonEmail = HtmlEmailTools.generateEmail(htmlEmailBasedPath,
                WebappResManager.getInstance().getMembership_upgrade_email_file(),
                emailAddresses,
                WebappResManager.getInstance().getMembership_upgrade_email_subject(),
                properties);
        
        return upgradeNotificatonEmail.size() > 0 ? upgradeNotificatonEmail.get(0) : null;
    }
    
    private static EmailBean getCancelMembershipNotificationEmail(UserBean userBean)
    {
        ProductBean oCurrentMemberLevel = DBAccessor.getInstance().getProductByID(userBean.getMemberLevel());
        
        String[] emailAddresses = new String[1];
        emailAddresses[0] = userBean.getEmail();
        Hashtable<String,String> properties = userBean.getProperHash();
        properties.put("resource_url", resourceURL);
        properties.put("member_level_name", oCurrentMemberLevel.getName());
        properties.put("member_level_cost", String.valueOf(oCurrentMemberLevel.getPrice()));
        properties.put("member_level_include_sms", String.valueOf(oCurrentMemberLevel.getIncludeSMS()));
        
        List<EmailBean> cancelNotificatonEmail = HtmlEmailTools.generateEmail(htmlEmailBasedPath,
                WebappResManager.getInstance().getMembership_cancellation_email_file(),
                emailAddresses,
                WebappResManager.getInstance().getMembership_cancellation_email_subject(),
                properties);
        
        return cancelNotificatonEmail.size() > 0 ? cancelNotificatonEmail.get(0) : null;
    }
    
    /*
     * after paid user signup
     */
    private static EmailBean getMemberSignupNotificationEmail(UserBean userBean)
    {        
        ProductBean oCurrentMemberLevel = DBAccessor.getInstance().getProductByID(userBean.getMemberLevel());
        
        String[] emailAddresses = new String[1];
        emailAddresses[0] = userBean.getEmail();
        Hashtable<String,String> properties = userBean.getProperHash();
        properties.put("resource_url", resourceURL);
        properties.put("member_level_name", oCurrentMemberLevel.getName());
        properties.put("member_level_cost", String.valueOf(oCurrentMemberLevel.getPrice()));
        properties.put("member_level_include_sms", String.valueOf(oCurrentMemberLevel.getIncludeSMS()));
        
        List<EmailBean> signupNotificatonEmail = HtmlEmailTools.generateEmail(htmlEmailBasedPath,
                WebappResManager.getInstance().getMembership_signup_email_file(),
                emailAddresses,
                WebappResManager.getInstance().getMembership_signup_email_subject(),
                properties);
        
        return signupNotificatonEmail.size() > 0 ? signupNotificatonEmail.get(0) : null;
    }
    
    private static EmailBean getBuySMSNotificationEmail(UserBean userBean, long smsPackageID)
    {
        ProductBean oCurrentMemberLevel = DBAccessor.getInstance().getProductByID(userBean.getMemberLevel());
        SmsPackageBean oSMSPackage = DBAccessor.getInstance().getSMSPackageByID(smsPackageID);
        
        String[] emailAddresses = new String[1];
        emailAddresses[0] = userBean.getEmail();
        Hashtable<String,String> properties = userBean.getProperHash();
        properties.put("resource_url", resourceURL);
        properties.put("new_sms_credits", String.valueOf(oSMSPackage.getSms_included()));
        properties.put("new_sms_credits_cost", String.valueOf(oSMSPackage.getSms_cost()));
        properties.put("member_level_name", oCurrentMemberLevel.getName());
        properties.put("member_level_include_sms", String.valueOf(oCurrentMemberLevel.getIncludeSMS()));
        
        List<EmailBean> buySMSNotificatonEmail = HtmlEmailTools.generateEmail(htmlEmailBasedPath,
                WebappResManager.getInstance().getSms_purchase_email_file(),
                emailAddresses,
                WebappResManager.getInstance().getSms_purchase_email_subject(),
                properties);
        
        return buySMSNotificatonEmail.size() > 0 ? buySMSNotificatonEmail.get(0) : null;
    }
    
    
    
    /*
     * after free trial user confirm link
     */
    private static EmailBean getFreeTrialUserSignupNotificationEmail(UserBean userBean)
    {
        SimpleDateFormat oDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        String emailConfirmBaseLink = WebappResManager.getInstance().getRegister_confirm_base_link();
        emailConfirmBaseLink += "username="+userBean.getLogin_name()+"&id="+userBean.getConfirm_code();
        
        String[] emailAddresses = new String[1];
        emailAddresses[0] = userBean.getEmail();
        Hashtable<String,String> properties = userBean.getProperHash();
        properties.put("resource_url", resourceURL);
        properties.put("expired_date_string", oDateFormat.format(new Date(userBean.getExpired_date())));
        
        ProductBean oProduct = DBAccessor.getInstance().getProductByID(userBean.getMemberLevel());
        properties.put("free_trial_days", String.valueOf(oProduct.getPeriod()));
        //properties.put("expired_date", new SimpleDateFormat("yyyy-MM-dd").format(new Date(userBean.getExpired_date())));
        
        List<EmailBean> registerNotificatonEmail = HtmlEmailTools.generateEmail(htmlEmailBasedPath,
                WebappResManager.getInstance().getFree_trial_signup_email_file(),
                emailAddresses,
                WebappResManager.getInstance().getFree_trial_signup_email_subject(),
                properties);
                
        return registerNotificatonEmail.size() > 0 ? registerNotificatonEmail.get(0) : null;
    }
    
    /**
     * after free trial user signup
     * @param userBean
     * @return
     */
    private static EmailBean getFreeTrialUserConfirmNotificationEmail(UserBean userBean)
    {
        String emailConfirmBaseLink = WebappResManager.getInstance().getRegister_confirm_base_link();
        emailConfirmBaseLink += "username="+userBean.getLogin_name()+"&id="+userBean.getConfirm_code();
        
        String[] emailAddresses = new String[1];
        emailAddresses[0] = userBean.getEmail();
        Hashtable<String,String> properties = userBean.getProperHash();
        properties.put("resource_url", resourceURL);
        properties.put("confirm_link_url", emailConfirmBaseLink);
        
        List<EmailBean> confirmNotificatonEmail = HtmlEmailTools.generateEmail(htmlEmailBasedPath,
                WebappResManager.getInstance().getFree_trial_confirm_email_file(),
                emailAddresses,
                WebappResManager.getInstance().getFree_trial_confirm_email_subject(),
                properties);
        
        return confirmNotificatonEmail.size() > 0 ? confirmNotificatonEmail.get(0) : null;
    }
    
    /**
     * free trail is end
     * @param userBean
     * @return
     */
    private static EmailBean getUserFreeTrialEndNotification(UserBean userBean)
    {
        SimpleDateFormat oDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        String[] emailAddresses = new String[1];
        emailAddresses[0] = userBean.getEmail();
        Hashtable<String,String> properties = userBean.getProperHash();
        properties.put("resource_url", resourceURL);
        properties.put("expired_date_string", oDateFormat.format(new Date(userBean.getExpired_date())));
        
        ProductBean oProduct = DBAccessor.getInstance().getProductByID(userBean.getMemberLevel());
        properties.put("free_trial_days", String.valueOf(oProduct.getPeriod()));
        
        List<EmailBean> expiredNotificatonEmail = HtmlEmailTools.generateEmail(htmlEmailBasedPath,
                WebappResManager.getInstance().getFree_trial_expired_email_file(),
                emailAddresses,
                WebappResManager.getInstance().getFree_trial_expired_email_subject(),
                properties);
                
        return expiredNotificatonEmail.size() > 0 ? expiredNotificatonEmail.get(0) : null;
    }
    
    /**
     * Two days before end
     * @param userBean
     * @return
     */
    private static EmailBean getThreeDaysBeforeTrialEndNotification(UserBean userBean)
    {
        SimpleDateFormat oDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        String[] emailAddresses = new String[1];
        emailAddresses[0] = userBean.getEmail();
        Hashtable<String,String> properties = userBean.getProperHash();
        properties.put("resource_url", resourceURL);
        properties.put("left_days", "3");
        properties.put("expired_date_string", oDateFormat.format(new Date(userBean.getExpired_date())));
        
        List<EmailBean> confirmNotificatonEmail = HtmlEmailTools.generateEmail(htmlEmailBasedPath,
                WebappResManager.getInstance().getFree_trial_3_days_left_email_file(),
                emailAddresses,
                WebappResManager.getInstance().getFree_trial_3_days_left_email_subject(),
                properties);
        
        return confirmNotificatonEmail.size() > 0 ? confirmNotificatonEmail.get(0) : null;
    }
    
    /**
     * Two days after end
     * @param userBean
     * @return
     */
    private static EmailBean getThreeDaysAfterTrialEndNotification(UserBean userBean)
    {
        String[] emailAddresses = new String[1];
        emailAddresses[0] = userBean.getEmail();
        Hashtable<String,String> properties = userBean.getProperHash();
        properties.put("resource_url", resourceURL);
        properties.put("expired_days", "3");
        
        List<EmailBean> confirmNotificatonEmail = HtmlEmailTools.generateEmail(htmlEmailBasedPath,
                WebappResManager.getInstance().getFree_trial_expired_3_days_ago_email_file(),
                emailAddresses,
                WebappResManager.getInstance().getFree_trial_expired_3_days_ago_email_subject(),
                properties);
        
        return confirmNotificatonEmail.size() > 0 ? confirmNotificatonEmail.get(0) : null;
    } 
    
    /**
     * Seven days after end
     * @param userBean
     * @return
     */
    private static EmailBean getSevenDaysAfterTrialEndNotification(UserBean userBean)
    {
        EmailBean freeTrialEndNotificatonEmail = new EmailBean();
        freeTrialEndNotificatonEmail.setEmailSubject("Your Free Trial Period Has Been Ended For Seven Days");
        freeTrialEndNotificatonEmail.setRecipients(userBean.getEmail());
        
        String emailContent = "Your free trial period has been ended for seven days. Please upgrade your member level.";
        
        freeTrialEndNotificatonEmail.setEmailBody(emailContent);
        
        return freeTrialEndNotificatonEmail;
    }
    
    private static EmailBean getFindPasswordNotificationEmail(UserBean userBean)
    {
        TempPasswordBean tempPassword = DBAccessor.getInstance().getFindPasswordConfirmCode(userBean.getId());
        
        String emailConfirmBaseLink = WebappResManager.getInstance().getReset_password_confirm_base_link();
        emailConfirmBaseLink += "username="+userBean.getLogin_name()+"&id="+tempPassword.getConfirm_code();
        
        //sent confirm link to user
        EmailBean resetPWDEmail = new EmailBean();
        resetPWDEmail.setEmailSubject("Your New Password");
        resetPWDEmail.setRecipients(userBean.getEmail());      
        
        String emailContent = "You had requested to reset your password. In consideration of security issue, Please confirm following link to enable your new password."+emailConfirmBaseLink;                
        resetPWDEmail.setEmailBody(emailContent);
        
        return resetPWDEmail;
    }
    
    public static EmailBean getBuySMSPackageNotificationEmail(UserBean userBean, long buiedSMSPackageID)
    {
        return getBuySMSNotificationEmail(userBean, buiedSMSPackageID);
    }
    
    public static EmailBean getNotificationEmail(UserBean userBean, int templateType)
    {
        EmailBean result = null;
        
        switch(templateType)
        {
            case WebConstants.THREE_DAYS_BEFORE_EXPIRED_TEMPLATE:
                result = getThreeDaysBeforeTrialEndNotification(userBean);
                break;
            case WebConstants.FREE_TRIAL_EXPIRED_TEMPLATE:
                result = getUserFreeTrialEndNotification(userBean);
                break;
            case WebConstants.THREE_DAYS_AFTER_EXPIRED_TEMPLATE:
                result = getThreeDaysAfterTrialEndNotification(userBean);
                break;
            case WebConstants.SEVEN_DAYS_AFTER_EXPRED_TEMPLATE:
                result = getSevenDaysAfterTrialEndNotification(userBean);
                break;
            case WebConstants.FREE_TRIAL_USER_SIGNUP_TEMPLATE:
                result = getFreeTrialUserSignupNotificationEmail(userBean);
                break;
            case WebConstants.FREE_TRIAL_USER_CONFIRM_TEMPLATE:
                result = getFreeTrialUserConfirmNotificationEmail(userBean);
                break;
            case WebConstants.PAID_USER_SIGNUP_TEMPLATE:
                result = getMemberSignupNotificationEmail(userBean);
                break;
            case WebConstants.FIND_PASSWORD_CONFIRM_TEMPLATE:
                result = getFindPasswordNotificationEmail(userBean);
                break;
            case WebConstants.CANCEL_MEMBER_SHIP_TEMPLATE:
                result = getCancelMembershipNotificationEmail(userBean);
                break;
            case WebConstants.UPGRADE_MEMBER_SHIP_TEMPLATE:
                result = getUpgradeMembershipNotificationEmail(userBean);
                break;
        }
        
        return result;
    }
    
    public static NotificationTemplate getNotificationTemplate(int templateType)
    {
        NotificationTemplate result = null;
        
        switch(templateType)
        {
            case WebConstants.THREE_DAYS_BEFORE_EXPIRED_TEMPLATE:
                result = new NotificationTemplate("twoDaysBeforeFreeTrialUserExpired", templateType);
                break;
            case WebConstants.FREE_TRIAL_EXPIRED_TEMPLATE:
                result = new NotificationTemplate("freeTrialUserExpired", templateType);
                break;
            case WebConstants.THREE_DAYS_AFTER_EXPIRED_TEMPLATE:
                result = new NotificationTemplate("twoDaysAfterFreeTrialUserExpired", templateType);
                break;
            case WebConstants.SEVEN_DAYS_AFTER_EXPRED_TEMPLATE:
                result = new NotificationTemplate("sevenDaysAfterFreeTrialUserExpired", templateType);
                break;
            case WebConstants.FREE_TRIAL_USER_SIGNUP_TEMPLATE:
                result = new NotificationTemplate("freeTrialUserSignupFreeTrialUser", templateType);
                break;
            case WebConstants.PAID_USER_SIGNUP_TEMPLATE:
                result = new NotificationTemplate("normalUserSignupFreeTrialUser", templateType);
                break;
        }
        
        return result;
    }
}
