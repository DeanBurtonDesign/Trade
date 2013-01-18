package com.cleartraders.signalframe.conf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.cleartraders.common.util.config.ResBaseManager;

public class SFResManager extends ResBaseManager
{
    private static SFResManager m_oInstance=null;
    private int max_allowed_candle_bar_between_signal=0;
    
    private long default_timeOut_millseconds = 0;
    private int amount_for_each_signal = -1;
    private String auto_trader_api_user_id = "";
    private String auto_trader_api_pwd = "";
    
    private String html_email_resource_url = "";
    
    private long sms_low_level=0;
    private long sms_empty_level=0;
    
    private String sms_empty_email_file="";
    private String sms_empty_email_subject="";
    private String sms_low_email_file="";
    private String sms_low_email_subject="";
    
    private String sms_close_signal_file="";
    private String sms_long_signal_file="";
    private String sms_short_signal_file="";
    
    private int credit_per_sms = 0;
    private int credit_per_email = 0;
    
    private SFResManager() throws UnsupportedEncodingException, FileNotFoundException ,IOException
    {
        super("cleartraders_sf.properties");
        
        init();
    }
    
    public synchronized static SFResManager getInstance()
    {
        if(null == m_oInstance)
        {
            try
            {
                m_oInstance = new SFResManager();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                
                return null;
            }
        }
        
        return m_oInstance;
    }
    
    public boolean reLoad() throws UnsupportedEncodingException, FileNotFoundException ,IOException
    {
        return init();
    }
    
    protected boolean init() throws UnsupportedEncodingException, FileNotFoundException ,IOException
    {
        //for signal time out
        max_allowed_candle_bar_between_signal = Integer.parseInt(getConifPropertyHandler().getProperty("max_allowed_candle_bar_between_signal"));
        
        //for signal time out
        default_timeOut_millseconds = Long.parseLong(getConifPropertyHandler().getProperty("default_timeOut_millseconds"));
        
        //for auto-trader
        amount_for_each_signal = Integer.parseInt(getConifPropertyHandler().getProperty("amount_for_each_signal"));
        auto_trader_api_user_id = getConifPropertyHandler().getProperty("auto_trader_api_user_id");
        auto_trader_api_pwd = getConifPropertyHandler().getProperty("auto_trader_api_pwd");
        
        //for sms credits
        sms_low_level = Long.parseLong(getConifPropertyHandler().getProperty("sms_low_level"));
        sms_empty_level = Long.parseLong(getConifPropertyHandler().getProperty("sms_empty_level"));
        
        sms_empty_email_file = getConifPropertyHandler().getProperty("sms_empty_email_file");
        sms_empty_email_subject = getConifPropertyHandler().getProperty("sms_empty_email_subject");
        sms_low_email_file = getConifPropertyHandler().getProperty("sms_low_email_file");
        sms_low_email_subject = getConifPropertyHandler().getProperty("sms_low_email_subject");
        
        sms_close_signal_file=getConifPropertyHandler().getProperty("sms_close_signal_file");
        sms_long_signal_file=getConifPropertyHandler().getProperty("sms_long_signal_file");
        sms_short_signal_file=getConifPropertyHandler().getProperty("sms_short_signal_file");
        
        html_email_resource_url = getConifPropertyHandler().getProperty("html_email_resource_url");
        
        credit_per_sms = Integer.parseInt(getConifPropertyHandler().getProperty("credit_per_sms"));
        credit_per_email = Integer.parseInt(getConifPropertyHandler().getProperty("credit_per_email"));
        
        return true;
    }  
    
    public int getMax_allowed_candle_bar_between_signal()
    {
        return max_allowed_candle_bar_between_signal;
    }

    public void setMax_allowed_candle_bar_between_signal(int max_allowed_candle_bar_between_signal)
    {
        this.max_allowed_candle_bar_between_signal = max_allowed_candle_bar_between_signal;
    }
    
    public long getSms_empty_level()
    {
        return sms_empty_level;
    }

    public void setSms_empty_level(long sms_empty_level)
    {
        this.sms_empty_level = sms_empty_level;
    }

    public long getSms_low_level()
    {
        return sms_low_level;
    }

    public void setSms_low_level(long sms_low_level)
    {
        this.sms_low_level = sms_low_level;
    }
    
    public long getDefaultTimeOutMillSeconds()
    {
        return default_timeOut_millseconds;
    }

    public void setDefaultTimeOutMillSeconds(long defaultTimeOutMillSeconds)
    {
        this.default_timeOut_millseconds = defaultTimeOutMillSeconds;
    }
    
    public int getAmount_for_each_signal()
    {
        return amount_for_each_signal;
    }

    public void setAmount_for_each_signal(int amount_for_each_signal)
    {
        this.amount_for_each_signal = amount_for_each_signal;
    }

    public String getAuto_trader_api_pwd()
    {
        return auto_trader_api_pwd;
    }

    public void setAuto_trader_api_pwd(String auto_trader_api_pwd)
    {
        this.auto_trader_api_pwd = auto_trader_api_pwd;
    }

    public String getAuto_trader_api_user_id()
    {
        return auto_trader_api_user_id;
    }

    public void setAuto_trader_api_user_id(String auto_trader_api_user_id)
    {
        this.auto_trader_api_user_id = auto_trader_api_user_id;
    }
    

    public String getSms_empty_email_file()
    {
        return sms_empty_email_file;
    }

    public void setSms_empty_email_file(String sms_empty_email_file)
    {
        this.sms_empty_email_file = sms_empty_email_file;
    }

    public String getSms_empty_email_subject()
    {
        return sms_empty_email_subject;
    }

    public void setSms_empty_email_subject(String sms_empty_email_subject)
    {
        this.sms_empty_email_subject = sms_empty_email_subject;
    }

    public String getSms_low_email_file()
    {
        return sms_low_email_file;
    }

    public void setSms_low_email_file(String sms_low_email_file)
    {
        this.sms_low_email_file = sms_low_email_file;
    }

    public String getSms_low_email_subject()
    {
        return sms_low_email_subject;
    }

    public void setSms_low_email_subject(String sms_low_email_subject)
    {
        this.sms_low_email_subject = sms_low_email_subject;
    }

    public String getHtml_email_resource_url()
    {
        return html_email_resource_url;
    }

    public void setHtml_email_resource_url(String html_email_resource_url)
    {
        this.html_email_resource_url = html_email_resource_url;
    }
    
    public String getSms_close_signal_file()
    {
        return sms_close_signal_file;
    }

    public void setSms_close_signal_file(String sms_close_signal_file)
    {
        this.sms_close_signal_file = sms_close_signal_file;
    }

    public String getSms_long_signal_file()
    {
        return sms_long_signal_file;
    }

    public void setSms_long_signal_file(String sms_long_signal_file)
    {
        this.sms_long_signal_file = sms_long_signal_file;
    }

    public String getSms_short_signal_file()
    {
        return sms_short_signal_file;
    }

    public void setSms_short_signal_file(String sms_short_signal_file)
    {
        this.sms_short_signal_file = sms_short_signal_file;
    }
    
    public int getCredit_per_email()
    {
        return credit_per_email;
    }

    public void setCredit_per_email(int credit_per_email)
    {
        this.credit_per_email = credit_per_email;
    }

    public int getCredit_per_sms()
    {
        return credit_per_sms;
    }

    public void setCredit_per_sms(int credit_per_sms)
    {
        this.credit_per_sms = credit_per_sms;
    }
}
