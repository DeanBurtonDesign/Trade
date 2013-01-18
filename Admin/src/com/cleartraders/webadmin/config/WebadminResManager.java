package com.cleartraders.webadmin.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.cleartraders.common.util.config.ResBaseManager;

public class WebadminResManager extends ResBaseManager
{
    private static WebadminResManager m_oInstance = null;
    
    private long default_market_peirod = 0;
    private String mailchimp_api_key = "";
    private String mailchimp_list_id = "";
    
    public String getMailchimp_api_key()
    {
        return mailchimp_api_key;
    }

    public void setMailchimp_api_key(String mailchimp_api_key)
    {
        this.mailchimp_api_key = mailchimp_api_key;
    }

    public String getMailchimp_list_id()
    {
        return mailchimp_list_id;
    }

    public void setMailchimp_list_id(String mailchimp_list_id)
    {
        this.mailchimp_list_id = mailchimp_list_id;
    }

    public long getDefault_market_peirod()
    {
        return default_market_peirod;
    }

    public void setDefault_market_peirod(long default_market_peirod)
    {
        this.default_market_peirod = default_market_peirod;
    }

    public synchronized static WebadminResManager getInstance()
    {
        if(null == m_oInstance)
        {
            try
            {
                m_oInstance = new WebadminResManager();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                
                return null;
            }
        }
        
        return m_oInstance;
    }
    
    public WebadminResManager() throws UnsupportedEncodingException, FileNotFoundException ,IOException
    {
        super("cleartraders_admin.properties");
        
        init();
    }
    
    @Override
    protected boolean init() throws UnsupportedEncodingException,
            FileNotFoundException, IOException
    {
        default_market_peirod = Long.parseLong(getConifPropertyHandler().getProperty("default_market_peirod"));
        mailchimp_api_key = getConifPropertyHandler().getProperty("mailchimp_api_key");
        mailchimp_list_id = getConifPropertyHandler().getProperty("mailchimp_list_id");
        
        return true;
    }

    @Override
    public boolean reLoad() throws UnsupportedEncodingException,
            FileNotFoundException, IOException
    {
        return init();
    }

}
