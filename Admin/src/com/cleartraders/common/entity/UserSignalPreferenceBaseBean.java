package com.cleartraders.common.entity;

/**
 * ref table details
 * 
create table user_signal_preference_table
(
  user_id               int unsigned not null,
  signal_type       int unsigned not null,  #0:BUY 1:SELL 2:Scalper
  market_type_id        int unsigned not null, 
  market_period_id  int unsigned not null,
  notify_email      varchar(100) not null,  #default is user preference, but user may custom for different type, such as EUR/USD notify, but others don't
  notify_sms        varchar(100) not null,  #default is user preference, but user may custom for different type   
  enable_email      int unsigned not null,  # 1: enable 2: disable 
  enable_sms        int unsigned not null,  # 1: enable 2: disable 
  active            int default 2           # 1: enable 2: disable  
);
 * 
 * @author Peter
 *
 */
public class UserSignalPreferenceBaseBean
{
    private long id=0;
    private long user_id=0;
    private int signal_type=0;
    private long strategy_id=0;
    private long market_type_id=0;
    private long market_period_id=0;
    private String notify_email="";
    private String notify_sms="";
    private boolean enable_email=false;
    private boolean enable_sms=false;
    private boolean active=false;
    private long time_zone_id=0;
    private long user_sms_credits=0;
    
    public long getStrategy_id()
    {
        return strategy_id;
    }
    public void setStrategy_id(long strategy_id)
    {
        this.strategy_id = strategy_id;
    }
    public long getUser_sms_credits()
    {
        return user_sms_credits;
    }
    public void setUser_sms_credits(long user_sms_credits)
    {
        this.user_sms_credits = user_sms_credits;
    }
    public long getTime_zone_id()
    {
        return time_zone_id;
    }
    public void setTime_zone_id(long time_zone_id)
    {
        this.time_zone_id = time_zone_id;
    }
    public long getId()
    {
        return id;
    }
    public void setId(long id)
    {
        this.id = id;
    }
    public boolean getActive()
    {
        return active;
    }
    public void setActive(boolean active)
    {
        this.active = active;
    }
    public boolean getEnable_email()
    {
        return enable_email;
    }
    public void setEnable_email(boolean enable_email)
    {
        this.enable_email = enable_email;
    }
    public boolean getEnable_sms()
    {
        return enable_sms;
    }
    public void setEnable_sms(boolean enable_sms)
    {
        this.enable_sms = enable_sms;
    }
    public long getMarket_period_id()
    {
        return market_period_id;
    }
    public void setMarket_period_id(long market_period_id)
    {
        this.market_period_id = market_period_id;
    }
    public long getMarket_type_id()
    {
        return market_type_id;
    }
    public void setMarket_type_id(long market_type_id)
    {
        this.market_type_id = market_type_id;
    }
    public String getNotify_email()
    {
        return notify_email;
    }
    public void setNotify_email(String notify_email)
    {
        this.notify_email = notify_email;
    }
    public String getNotify_sms()
    {
        return notify_sms;
    }
    public void setNotify_sms(String notify_sms)
    {
        this.notify_sms = notify_sms;
    }
    public int getSignal_type()
    {
        return signal_type;
    }
    public void setSignal_type(int signal_type)
    {
        this.signal_type = signal_type;
    }
    public long getUser_id()
    {
        return user_id;
    }
    public void setUser_id(long user_id)
    {
        this.user_id = user_id;
    }
    
}
