package com.cleartraders.common.util.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class CommonResManager extends ResBaseManager
{
    private static CommonResManager m_oInstance = null;
    
    //---------for DB----------------
    private String db_jini_name="";
    
    //---------for UNIT TestDB-------
    private String db_user_name="";
    private String db_password="";
    private String db_driver_class_name="";
    private String db_url="";
    
    //---------for JMS----------------
    private String jms_server_url="";
    private String jms_username="";
    private String jms_password="";
    private String signaltojs_topic="";
    
    //---------for IQFeed----------------
    private String app_name="";
    private String version="";
    private String key="";
    private String iqfeed_url="";
    private int iqfeed_realtime_socket=0;
    private int iqfeed_history_socket=0;
    private String symbol_prefix="";
    private int iqfeed_server_timezone_offset=0;
    
    //---------for SMTP----------------
    private String SMTPServerURL="";
    private String SMTPUser="";
    private String SMTPPWD="";    
    private long SendEmailDelay=1000;
    private String email_sent_from="";
    private String email_sent_from_name="";
    
    //---------for SMS-----------------
    private long SendSMSDelay = 1000;
    private String SMSHTTPServerURL="";
    private String SMSAccountUserName="";
    private String SMSAccountPassword="";
    private String SMSAPIID="";
    private String SMSEndingInfo="";
            
    //---------for Log and Debug-----------------
    private int debug_level=0;
    private int log_level=0;
     
    private CommonResManager()throws UnsupportedEncodingException, FileNotFoundException ,IOException
    {
        super("cleartraders_common.properties");
        
        init();
    }
    
    public synchronized static CommonResManager getInstance()
    {
        if(null == m_oInstance)
        {
            try
            {
                m_oInstance = new CommonResManager();
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
        db_jini_name = getConifPropertyHandler().getProperty("db_jini_name");
        db_user_name=getConifPropertyHandler().getProperty("db_user_name");
        db_password=getConifPropertyHandler().getProperty("db_password");
        db_driver_class_name=getConifPropertyHandler().getProperty("db_driver_class_name");
        db_url=getConifPropertyHandler().getProperty("db_url");
        
        jms_server_url = getConifPropertyHandler().getProperty("jms_server_url");
        jms_username = getConifPropertyHandler().getProperty("jms_username");
        jms_password = getConifPropertyHandler().getProperty("jms_password");
        signaltojs_topic = getConifPropertyHandler().getProperty("signaltojs_topic");
        
        app_name = getConifPropertyHandler().getProperty("app_name");
        version = getConifPropertyHandler().getProperty("version");
        key = getConifPropertyHandler().getProperty("key");
        iqfeed_url = getConifPropertyHandler().getProperty("iqfeed_url");
        symbol_prefix = getConifPropertyHandler().getProperty("symbol_prefix");            
        iqfeed_realtime_socket = Integer.parseInt(getConifPropertyHandler().getProperty("iqfeed_realtime_socket"));
        iqfeed_history_socket = Integer.parseInt(getConifPropertyHandler().getProperty("iqfeed_history_socket"));
        iqfeed_server_timezone_offset= Integer.parseInt(getConifPropertyHandler().getProperty("iqfeed_server_timezone_offset"));
            
        SMTPServerURL = getConifPropertyHandler().getProperty("smtp_server_url");
        SMTPUser = getConifPropertyHandler().getProperty("smtp_user");
        SMTPPWD = getConifPropertyHandler().getProperty("smtp_pwd");
        SendEmailDelay = Long.parseLong(getConifPropertyHandler().getProperty("send_email_delay"));
        email_sent_from = getConifPropertyHandler().getProperty("email_sent_from");
        email_sent_from_name = getConifPropertyHandler().getProperty("email_sent_from_name");
        
        SMSHTTPServerURL = getConifPropertyHandler().getProperty("sms_http_server_url");
        SMSAccountUserName = getConifPropertyHandler().getProperty("sms_account_user_name");
        SMSAccountPassword = getConifPropertyHandler().getProperty("sms_account_password");
        SMSAPIID = getConifPropertyHandler().getProperty("sms_api_id");            
        SendSMSDelay = Long.parseLong(getConifPropertyHandler().getProperty("send_sms_delay"));
        SMSEndingInfo = getConifPropertyHandler().getProperty("sms_ending_info");           
                
        debug_level=Integer.parseInt(getConifPropertyHandler().getProperty("debug_level"));
        log_level=Integer.parseInt(getConifPropertyHandler().getProperty("log_level"));
       
        return true;
    }
    
    public String getApp_name()
    {
        return app_name;
    }

    public void setApp_name(String app_name)
    {
        this.app_name = app_name;
    }

    public int getIqfeed_realtime_socket()
    {
        return iqfeed_realtime_socket;
    }

    public void setIqfeed_realtime_socket(int iqfeed_realtime_socket)
    {
        this.iqfeed_realtime_socket = iqfeed_realtime_socket;
    }

    public String getIqfeed_url()
    {
        return iqfeed_url;
    }

    public void setIqfeed_url(String iqfeed_url)
    {
        this.iqfeed_url = iqfeed_url;
    }

    public String getJms_password()
    {
        return jms_password;
    }

    public void setJms_password(String jms_password)
    {
        this.jms_password = jms_password;
    }

    public String getJms_server_url()
    {
        return jms_server_url;
    }

    public void setJms_server_url(String jms_server_url)
    {
        this.jms_server_url = jms_server_url;
    }

    public String getJms_username()
    {
        return jms_username;
    }

    public void setJms_username(String jms_username)
    {
        this.jms_username = jms_username;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getSymbol_prefix()
    {
        return symbol_prefix;
    }

    public void setSymbol_prefix(String symbol_prefix)
    {
        this.symbol_prefix = symbol_prefix;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getDb_jini_name()
    {
        return db_jini_name;
    }

    public void setDb_jini_name(String db_jini_name)
    {
        this.db_jini_name = db_jini_name;
    }

    public long getSendEmailDelay()
    {
        return SendEmailDelay;
    }

    public void setSendEmailDelay(long sendEmailDelay)
    {
        SendEmailDelay = sendEmailDelay;
    }

    public long getSendSMSDelay()
    {
        return SendSMSDelay;
    }

    public void setSendSMSDelay(long sendSMSDelay)
    {
        SendSMSDelay = sendSMSDelay;
    }

    public String getSMSAccountPassword()
    {
        return SMSAccountPassword;
    }

    public void setSMSAccountPassword(String accountPassword)
    {
        SMSAccountPassword = accountPassword;
    }

    public String getSMSAccountUserName()
    {
        return SMSAccountUserName;
    }

    public void setSMSAccountUserName(String accountUserName)
    {
        SMSAccountUserName = accountUserName;
    }

    public String getSMSAPIID()
    {
        return SMSAPIID;
    }

    public void setSMSAPIID(String smsapiid)
    {
        SMSAPIID = smsapiid;
    }

    public String getSMSHTTPServerURL()
    {
        return SMSHTTPServerURL;
    }

    public void setSMSHTTPServerURL(String serverURL)
    {
        SMSHTTPServerURL = serverURL;
    }

    public String getSMTPPWD()
    {
        return SMTPPWD;
    }

    public void setSMTPPWD(String smtppwd)
    {
        SMTPPWD = smtppwd;
    }

    public String getSMTPServerURL()
    {
        return SMTPServerURL;
    }

    public void setSMTPServerURL(String serverURL)
    {
        SMTPServerURL = serverURL;
    }

    public String getSMTPUser()
    {
        return SMTPUser;
    }

    public void setSMTPUser(String user)
    {
        SMTPUser = user;
    }
    
    public int getDebug_level()
    {
        return debug_level;
    }

    public void setDebug_level(int debug_level)
    {
        this.debug_level = debug_level;
    }

    public int getLog_level()
    {
        return log_level;
    }

    public void setLog_level(int log_level)
    {
        this.log_level = log_level;
    }

    public int getIqfeed_history_socket()
    {
        return iqfeed_history_socket;
    }

    public void setIqfeed_history_socket(int iqfeed_history_socket)
    {
        this.iqfeed_history_socket = iqfeed_history_socket;
    }

    public String getSignaltojs_topic()
    {
        return signaltojs_topic;
    }

    public void setSignaltojs_topic(String signaltojs_topic)
    {
        this.signaltojs_topic = signaltojs_topic;
    }

    public int getIqfeed_server_timezone_offset()
    {
        return iqfeed_server_timezone_offset;
    }

    public void setIqfeed_server_timezone_offset(int iqfeed_server_timezone_offset)
    {
        this.iqfeed_server_timezone_offset = iqfeed_server_timezone_offset;
    }

    public String getDb_driver_class_name()
    {
        return db_driver_class_name;
    }

    public void setDb_driver_class_name(String db_driver_class_name)
    {
        this.db_driver_class_name = db_driver_class_name;
    }

    public String getDb_password()
    {
        return db_password;
    }

    public void setDb_password(String db_password)
    {
        this.db_password = db_password;
    }

    public String getDb_url()
    {
        return db_url;
    }

    public void setDb_url(String db_url)
    {
        this.db_url = db_url;
    }

    public String getDb_user_name()
    {
        return db_user_name;
    }

    public void setDb_user_name(String db_user_name)
    {
        this.db_user_name = db_user_name;
    }

    public String getEmail_sent_from()
    {
        return email_sent_from;
    }

    public void setEmail_sent_from(String email_sent_from)
    {
        this.email_sent_from = email_sent_from;
    }

    public String getSMSEndingInfo()
    {
        return SMSEndingInfo;
    }

    public void setSMSEndingInfo(String endingInfo)
    {
        SMSEndingInfo = endingInfo;
    }

    public String getEmail_sent_from_name()
    {
        return email_sent_from_name;
    }

    public void setEmail_sent_from_name(String email_sent_from_name)
    {
        this.email_sent_from_name = email_sent_from_name;
    }
}
