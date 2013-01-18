package com.cleartraders.webapp.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.cleartraders.common.util.config.ResBaseManager;

public class WebappResManager extends ResBaseManager
{
    private static WebappResManager m_oInstance = null;
    
    private int each_page_size = 0;
    private String auto_trade_control_url="";
    private String register_confirm_base_link="";
    private String reset_password_confirm_base_link="";
    private String html_email_resource_url = "";
    
    private String paypal_account="";
    private String paypal_product_currency="";
    private String paypal_payment_query_url="";
    private String paypal_account_indentity_token="";
    private String request_payment_detail_cmd="";
    private String paypal_subscription_cmd="";
    private String paypal_cancel_subscription_cmd="";
    
    private String free_trial_user_first_market="";
    private String free_trial_user_first_market_peirod="";
    
    private String administrator_email_address="";
    
    private String free_trial_sms_notification="";
    private String paid_user_sms_notification="";
    
    private String number_of_pasted_signals="";
    
    //Email templates
    private String free_trial_signup_email_file="";
    private String free_trial_signup_email_subject="";
    private String free_trial_confirm_email_file="";
    private String free_trial_confirm_email_subject="";
    private String free_trial_3_days_left_email_file="";
    private String free_trial_3_days_left_email_subject="";
    private String free_trial_expired_3_days_ago_email_file="";
    private String free_trial_expired_3_days_ago_email_subject="";
    private String free_trial_expired_email_file="";
    private String free_trial_expired_email_subject="";
    private String membership_cancellation_email_file="";
    private String membership_cancellation_email_subject="";
    private String membership_signup_email_file="";
    private String membership_signup_email_subject="";
    private String membership_upgrade_email_file="";
    private String membership_upgrade_email_subject="";
    private String sms_purchase_email_file="";
    private String sms_purchase_email_subject="";    
    private String contact_us_confirm_email_subject="";
    private String contact_us_confirm_email_file="";    
    private String contact_us_to_admin_email_subject="";
    private String contact_us_to_admin_email_file="";    
    private String tell_a_friend_email_subject="";
    private String tell_a_friend_email_file="";    
    private String forgot_password_email_subject="";
    private String forgot_password_email_file="";
    
    private String default_quick_link_name="";
    private String default_quick_link_url="";
    
    private String default_strategy_id="";
    
    private String sms_50_button_id="";
    private String sms_250_button_id="";
    private String sms_500_button_id="";
    private String sms_1000_button_id="";
    private String sms_5000_button_id="";
    private String specialist_button_id="";
    private String expert_button_id="";
    private String master_button_id="";
    
    public String getExpert_button_id()
    {
        return expert_button_id;
    }

    public void setExpert_button_id(String expert_button_id)
    {
        this.expert_button_id = expert_button_id;
    }

    public String getMaster_button_id()
    {
        return master_button_id;
    }

    public void setMaster_button_id(String master_button_id)
    {
        this.master_button_id = master_button_id;
    }

    public String getSms_1000_button_id()
    {
        return sms_1000_button_id;
    }

    public void setSms_1000_button_id(String sms_1000_button_id)
    {
        this.sms_1000_button_id = sms_1000_button_id;
    }

    public String getSms_250_button_id()
    {
        return sms_250_button_id;
    }

    public void setSms_250_button_id(String sms_250_button_id)
    {
        this.sms_250_button_id = sms_250_button_id;
    }

    public String getSms_50_button_id()
    {
        return sms_50_button_id;
    }

    public void setSms_50_button_id(String sms_50_button_id)
    {
        this.sms_50_button_id = sms_50_button_id;
    }

    public String getSms_500_button_id()
    {
        return sms_500_button_id;
    }

    public void setSms_500_button_id(String sms_500_button_id)
    {
        this.sms_500_button_id = sms_500_button_id;
    }

    public String getSms_5000_button_id()
    {
        return sms_5000_button_id;
    }

    public void setSms_5000_button_id(String sms_5000_button_id)
    {
        this.sms_5000_button_id = sms_5000_button_id;
    }

    public String getSpecialist_button_id()
    {
        return specialist_button_id;
    }

    public void setSpecialist_button_id(String specialist_button_id)
    {
        this.specialist_button_id = specialist_button_id;
    }

    public String getDefault_strategy_id()
    {
        return default_strategy_id;
    }

    public void setDefault_strategy_id(String default_strategy_id)
    {
        this.default_strategy_id = default_strategy_id;
    }

    public String getDefault_quick_link_name()
    {
        return default_quick_link_name;
    }

    public void setDefault_quick_link_name(String default_quick_link_name)
    {
        this.default_quick_link_name = default_quick_link_name;
    }

    public String getDefault_quick_link_url()
    {
        return default_quick_link_url;
    }

    public void setDefault_quick_link_url(String default_quick_link_url)
    {
        this.default_quick_link_url = default_quick_link_url;
    }

    public String getContact_us_confirm_email_file()
    {
        return contact_us_confirm_email_file;
    }

    public void setContact_us_confirm_email_file(
            String contact_us_confirm_email_file)
    {
        this.contact_us_confirm_email_file = contact_us_confirm_email_file;
    }

    public String getContact_us_confirm_email_subject()
    {
        return contact_us_confirm_email_subject;
    }

    public void setContact_us_confirm_email_subject(
            String contact_us_confirm_email_subject)
    {
        this.contact_us_confirm_email_subject = contact_us_confirm_email_subject;
    }

    public String getContact_us_to_admin_email_file()
    {
        return contact_us_to_admin_email_file;
    }

    public void setContact_us_to_admin_email_file(
            String contact_us_to_admin_email_file)
    {
        this.contact_us_to_admin_email_file = contact_us_to_admin_email_file;
    }

    public String getContact_us_to_admin_email_subject()
    {
        return contact_us_to_admin_email_subject;
    }

    public void setContact_us_to_admin_email_subject(
            String contact_us_to_admin_email_subject)
    {
        this.contact_us_to_admin_email_subject = contact_us_to_admin_email_subject;
    }

    public String getForgot_password_email_file()
    {
        return forgot_password_email_file;
    }

    public void setForgot_password_email_file(String forgot_password_email_file)
    {
        this.forgot_password_email_file = forgot_password_email_file;
    }

    public String getForgot_password_email_subject()
    {
        return forgot_password_email_subject;
    }

    public void setForgot_password_email_subject(
            String forgot_password_email_subject)
    {
        this.forgot_password_email_subject = forgot_password_email_subject;
    }

    public String getTell_a_friend_email_file()
    {
        return tell_a_friend_email_file;
    }

    public void setTell_a_friend_email_file(String tell_a_friend_email_file)
    {
        this.tell_a_friend_email_file = tell_a_friend_email_file;
    }

    public String getTell_a_friend_email_subject()
    {
        return tell_a_friend_email_subject;
    }

    public void setTell_a_friend_email_subject(String tell_a_friend_email_subject)
    {
        this.tell_a_friend_email_subject = tell_a_friend_email_subject;
    }

    public String getFree_trial_3_days_left_email_file()
    {
        return free_trial_3_days_left_email_file;
    }

    public void setFree_trial_3_days_left_email_file(
            String free_trial_3_days_left_email_file)
    {
        this.free_trial_3_days_left_email_file = free_trial_3_days_left_email_file;
    }

    public String getFree_trial_3_days_left_email_subject()
    {
        return free_trial_3_days_left_email_subject;
    }

    public void setFree_trial_3_days_left_email_subject(
            String free_trial_3_days_left_email_subject)
    {
        this.free_trial_3_days_left_email_subject = free_trial_3_days_left_email_subject;
    }

    public String getFree_trial_expired_3_days_ago_email_file()
    {
        return free_trial_expired_3_days_ago_email_file;
    }

    public void setFree_trial_expired_3_days_ago_email_file(
            String free_trial_expired_3_days_ago_email_file)
    {
        this.free_trial_expired_3_days_ago_email_file = free_trial_expired_3_days_ago_email_file;
    }

    public String getFree_trial_expired_3_days_ago_email_subject()
    {
        return free_trial_expired_3_days_ago_email_subject;
    }

    public void setFree_trial_expired_3_days_ago_email_subject(
            String free_trial_expired_3_days_ago_email_subject)
    {
        this.free_trial_expired_3_days_ago_email_subject = free_trial_expired_3_days_ago_email_subject;
    }

    public String getFree_trial_expired_email_file()
    {
        return free_trial_expired_email_file;
    }

    public void setFree_trial_expired_email_file(
            String free_trial_expired_email_file)
    {
        this.free_trial_expired_email_file = free_trial_expired_email_file;
    }

    public String getFree_trial_expired_email_subject()
    {
        return free_trial_expired_email_subject;
    }

    public void setFree_trial_expired_email_subject(
            String free_trial_expired_email_subject)
    {
        this.free_trial_expired_email_subject = free_trial_expired_email_subject;
    }

    public String getMembership_cancellation_email_file()
    {
        return membership_cancellation_email_file;
    }

    public void setMembership_cancellation_email_file(
            String membership_cancellation_email_file)
    {
        this.membership_cancellation_email_file = membership_cancellation_email_file;
    }

    public String getMembership_cancellation_email_subject()
    {
        return membership_cancellation_email_subject;
    }

    public void setMembership_cancellation_email_subject(
            String membership_cancellation_email_subject)
    {
        this.membership_cancellation_email_subject = membership_cancellation_email_subject;
    }

    public String getMembership_signup_email_file()
    {
        return membership_signup_email_file;
    }

    public void setMembership_signup_email_file(String membership_signup_email_file)
    {
        this.membership_signup_email_file = membership_signup_email_file;
    }

    public String getMembership_signup_email_subject()
    {
        return membership_signup_email_subject;
    }

    public void setMembership_signup_email_subject(
            String membership_signup_email_subject)
    {
        this.membership_signup_email_subject = membership_signup_email_subject;
    }

    public String getMembership_upgrade_email_file()
    {
        return membership_upgrade_email_file;
    }

    public void setMembership_upgrade_email_file(
            String membership_upgrade_email_file)
    {
        this.membership_upgrade_email_file = membership_upgrade_email_file;
    }

    public String getMembership_upgrade_email_subject()
    {
        return membership_upgrade_email_subject;
    }

    public void setMembership_upgrade_email_subject(
            String membership_upgrade_email_subject)
    {
        this.membership_upgrade_email_subject = membership_upgrade_email_subject;
    }

    public String getSms_purchase_email_file()
    {
        return sms_purchase_email_file;
    }

    public void setSms_purchase_email_file(String sms_purchase_email_file)
    {
        this.sms_purchase_email_file = sms_purchase_email_file;
    }

    public String getSms_purchase_email_subject()
    {
        return sms_purchase_email_subject;
    }

    public void setSms_purchase_email_subject(String sms_purchase_email_subject)
    {
        this.sms_purchase_email_subject = sms_purchase_email_subject;
    }

    public String getFree_trial_confirm_email_file()
    {
        return free_trial_confirm_email_file;
    }

    public void setFree_trial_confirm_email_file(String free_trial_confirm_email_file)
    {
        this.free_trial_confirm_email_file = free_trial_confirm_email_file;
    }

    public String getFree_trial_confirm_email_subject()
    {
        return free_trial_confirm_email_subject;
    }

    public void setFree_trial_confirm_email_subject(
            String free_trial_confirm_email_subject)
    {
        this.free_trial_confirm_email_subject = free_trial_confirm_email_subject;
    }

    public String getFree_trial_signup_email_subject()
    {
        return free_trial_signup_email_subject;
    }

    public void setFree_trial_signup_email_subject(
            String free_trial_signup_email_subject)
    {
        this.free_trial_signup_email_subject = free_trial_signup_email_subject;
    }

    public String getFree_trial_signup_email_file()
    {
        return free_trial_signup_email_file;
    }

    public void setFree_trial_signup_email_file(String free_trial_signup_email_file)
    {
        this.free_trial_signup_email_file = free_trial_signup_email_file;
    }

    public String getAdministrator_email_address()
    {
        return administrator_email_address;
    }

    public void setAdministrator_email_address(String administrator_email_address)
    {
        this.administrator_email_address = administrator_email_address;
    }

    public String getPaypal_account()
    {
        return paypal_account;
    }

    public void setPaypal_account(String paypal_account)
    {
        this.paypal_account = paypal_account;
    }

    public String getFree_trial_user_first_market_peirod()
    {
        return free_trial_user_first_market_peirod;
    }

    public void setFree_trial_user_first_market_peirod(
            String free_trial_user_first_market_peirod)
    {
        this.free_trial_user_first_market_peirod = free_trial_user_first_market_peirod;
    }

    public String getFree_trial_user_first_market()
    {
        return free_trial_user_first_market;
    }

    public void setFree_trial_user_first_market(String free_trial_user_first_market)
    {
        this.free_trial_user_first_market = free_trial_user_first_market;
    }

    public String getRequest_payment_detail_cmd()
    {
        return request_payment_detail_cmd;
    }

    public void setRequest_payment_detail_cmd(String request_payment_detail_cmd)
    {
        this.request_payment_detail_cmd = request_payment_detail_cmd;
    }

    public String getPaypal_account_indentity_token()
    {
        return paypal_account_indentity_token;
    }

    public void setPaypal_account_indentity_token(
            String paypal_account_indentity_token)
    {
        this.paypal_account_indentity_token = paypal_account_indentity_token;
    }

    public String getPaypal_payment_query_url()
    {
        return paypal_payment_query_url;
    }

    public void setPaypal_payment_query_url(String paypal_payment_query_url)
    {
        this.paypal_payment_query_url = paypal_payment_query_url;
    }

    public String getHtml_email_resource_url()
    {
        return html_email_resource_url;
    }

    public void setHtml_email_resource_url(String html_email_resource_url)
    {
        this.html_email_resource_url = html_email_resource_url;
    }

    public String getRegister_confirm_base_link()
    {
        return register_confirm_base_link;
    }

    public void setRegister_confirm_base_link(String register_confirm_base_link)
    {
        this.register_confirm_base_link = register_confirm_base_link;
    }

    public String getAuto_trade_control_url()
    {
        return auto_trade_control_url;
    }

    public void setAuto_trade_control_url(String auto_trade_control_url)
    {
        this.auto_trade_control_url = auto_trade_control_url;
    }

    public int getEach_page_size()
    {
        return each_page_size;
    }

    public void setEach_page_size(int each_page_size)
    {
        this.each_page_size = each_page_size;
    }

    public synchronized static WebappResManager getInstance()
    {
        if(null == m_oInstance)
        {
            try
            {
                m_oInstance = new WebappResManager();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                
                return null;
            }
        }
        
        return m_oInstance;
    }
    
    public WebappResManager() throws UnsupportedEncodingException, FileNotFoundException ,IOException
    {
        super("cleartraders_web.properties");
        
        init();
    }

    @Override
    protected boolean init() throws UnsupportedEncodingException,
            FileNotFoundException, IOException
    {
        each_page_size=Integer.parseInt(getConifPropertyHandler().getProperty("each_page_size"));
        auto_trade_control_url=getConifPropertyHandler().getProperty("auto_trade_control_url");
        register_confirm_base_link=getConifPropertyHandler().getProperty("register_confirm_base_link");
        reset_password_confirm_base_link = getConifPropertyHandler().getProperty("reset_password_confirm_base_link");
        html_email_resource_url = getConifPropertyHandler().getProperty("html_email_resource_url");
        
        paypal_account = getConifPropertyHandler().getProperty("paypal_account");
        paypal_product_currency = getConifPropertyHandler().getProperty("paypal_product_currency");
        paypal_payment_query_url = getConifPropertyHandler().getProperty("paypal_payment_query_url");
        paypal_account_indentity_token = getConifPropertyHandler().getProperty("paypal_account_indentity_token");
        request_payment_detail_cmd = getConifPropertyHandler().getProperty("request_payment_detail_cmd");
        paypal_subscription_cmd = getConifPropertyHandler().getProperty("paypal_subscription_cmd");
        paypal_cancel_subscription_cmd = getConifPropertyHandler().getProperty("paypal_cancel_subscription_cmd");
        
        free_trial_user_first_market = getConifPropertyHandler().getProperty("free_trial_user_first_market");
        free_trial_user_first_market_peirod = getConifPropertyHandler().getProperty("free_trial_user_first_market_peirod");
        
        administrator_email_address = getConifPropertyHandler().getProperty("administrator_email_address");
        
        free_trial_sms_notification = getConifPropertyHandler().getProperty("free_trial_sms_notification");
        paid_user_sms_notification = getConifPropertyHandler().getProperty("paid_user_sms_notification");
        
        free_trial_signup_email_file = getConifPropertyHandler().getProperty("free_trial_signup_email_file");
        free_trial_signup_email_subject = getConifPropertyHandler().getProperty("free_trial_signup_email_subject");
        
        free_trial_confirm_email_file = getConifPropertyHandler().getProperty("free_trial_confirm_email_file");
        free_trial_confirm_email_subject = getConifPropertyHandler().getProperty("free_trial_confirm_email_subject");
        
        free_trial_3_days_left_email_file = getConifPropertyHandler().getProperty("free_trial_3_days_left_email_file");
        free_trial_3_days_left_email_subject = getConifPropertyHandler().getProperty("free_trial_3_days_left_email_subject");
        free_trial_expired_3_days_ago_email_file = getConifPropertyHandler().getProperty("free_trial_expired_3_days_ago_email_file");
        free_trial_expired_3_days_ago_email_subject = getConifPropertyHandler().getProperty("free_trial_expired_3_days_ago_email_subject");
        free_trial_expired_email_file = getConifPropertyHandler().getProperty("free_trial_expired_email_file");
        free_trial_expired_email_subject = getConifPropertyHandler().getProperty("free_trial_expired_email_subject");
        membership_cancellation_email_file = getConifPropertyHandler().getProperty("membership_cancellation_email_file");
        membership_cancellation_email_subject = getConifPropertyHandler().getProperty("membership_cancellation_email_subject");
        membership_signup_email_file = getConifPropertyHandler().getProperty("membership_signup_email_file");
        membership_signup_email_subject = getConifPropertyHandler().getProperty("membership_signup_email_subject");
        membership_upgrade_email_file = getConifPropertyHandler().getProperty("membership_upgrade_email_file");
        membership_upgrade_email_subject = getConifPropertyHandler().getProperty("membership_upgrade_email_subject");
        sms_purchase_email_file = getConifPropertyHandler().getProperty("sms_purchase_email_file");
        sms_purchase_email_subject = getConifPropertyHandler().getProperty("sms_purchase_email_subject");
        
        contact_us_confirm_email_subject = getConifPropertyHandler().getProperty("contact_us_confirm_email_subject");
        contact_us_confirm_email_file = getConifPropertyHandler().getProperty("contact_us_confirm_email_file"); 
        contact_us_to_admin_email_subject = getConifPropertyHandler().getProperty("contact_us_to_admin_email_subject");
        contact_us_to_admin_email_file = getConifPropertyHandler().getProperty("contact_us_to_admin_email_file");  
        tell_a_friend_email_subject = getConifPropertyHandler().getProperty("tell_a_friend_email_subject");
        tell_a_friend_email_file = getConifPropertyHandler().getProperty("tell_a_friend_email_file");  
        forgot_password_email_subject = getConifPropertyHandler().getProperty("forgot_password_email_subject");
        forgot_password_email_file = getConifPropertyHandler().getProperty("forgot_password_email_file");
        
        number_of_pasted_signals = getConifPropertyHandler().getProperty("number_of_pasted_signals");
        
        default_quick_link_name=getConifPropertyHandler().getProperty("default_quick_link_name");
        default_quick_link_url=getConifPropertyHandler().getProperty("default_quick_link_url");
        
        default_strategy_id=getConifPropertyHandler().getProperty("default_strategy_id");
        
        sms_50_button_id=getConifPropertyHandler().getProperty("sms_50_button_id");
        sms_250_button_id=getConifPropertyHandler().getProperty("sms_250_button_id");
        sms_500_button_id=getConifPropertyHandler().getProperty("sms_500_button_id");
        sms_1000_button_id=getConifPropertyHandler().getProperty("sms_1000_button_id");
        sms_5000_button_id=getConifPropertyHandler().getProperty("sms_5000_button_id");
        specialist_button_id=getConifPropertyHandler().getProperty("specialist_button_id");
        expert_button_id=getConifPropertyHandler().getProperty("expert_button_id");
        master_button_id=getConifPropertyHandler().getProperty("master_button_id");
        
        return true;
    }

    public boolean reLoad() throws UnsupportedEncodingException,
            FileNotFoundException, IOException
    {
        return init();
    }

    public String getPaypal_product_currency()
    {
        return paypal_product_currency;
    }

    public void setPaypal_product_currency(String paypal_product_currency)
    {
        this.paypal_product_currency = paypal_product_currency;
    }

    public String getPaypal_subscription_cmd()
    {
        return paypal_subscription_cmd;
    }

    public void setPaypal_subscription_cmd(String paypal_subscription_cmd)
    {
        this.paypal_subscription_cmd = paypal_subscription_cmd;
    }

    public String getPaypal_cancel_subscription_cmd()
    {
        return paypal_cancel_subscription_cmd;
    }

    public void setPaypal_cancel_subscription_cmd(
            String paypal_cancel_subscription_cmd)
    {
        this.paypal_cancel_subscription_cmd = paypal_cancel_subscription_cmd;
    }

    public String getReset_password_confirm_base_link()
    {
        return reset_password_confirm_base_link;
    }

    public void setReset_password_confirm_base_link(
            String reset_password_confirm_base_link)
    {
        this.reset_password_confirm_base_link = reset_password_confirm_base_link;
    }

    public String getFree_trial_sms_notification()
    {
        return free_trial_sms_notification;
    }

    public void setFree_trial_sms_notification(String free_trial_sms_notification)
    {
        this.free_trial_sms_notification = free_trial_sms_notification;
    }

    public String getPaid_user_sms_notification()
    {
        return paid_user_sms_notification;
    }

    public void setPaid_user_sms_notification(String paid_user_sms_notification)
    {
        this.paid_user_sms_notification = paid_user_sms_notification;
    }

    public String getNumber_of_pasted_signals()
    {
        return number_of_pasted_signals;
    }

    public void setNumber_of_pasted_signals(String number_of_pasted_signals)
    {
        this.number_of_pasted_signals = number_of_pasted_signals;
    }

}
