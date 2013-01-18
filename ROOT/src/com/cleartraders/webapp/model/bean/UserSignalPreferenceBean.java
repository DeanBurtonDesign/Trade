package com.cleartraders.webapp.model.bean;

public class UserSignalPreferenceBean
{
    private long id=0;
    private long userID=0;
    private long marketID=0;
    private long strategy_id=0;
    private boolean isActive=false;
    private String marketName="";
    private int timePeriodValue=0;
    private int timePeriodType=0;
    private String timePeriodName="";
    private boolean notifyByEmail=false;
    private String notifyEmail="";
    private boolean notifyBySMS=false;
    private String notifySMS="";
    
    public long getStrategy_id()
    {
        return strategy_id;
    }
    public void setStrategy_id(long strategy_id)
    {
        this.strategy_id = strategy_id;
    }
    public long getId()
    {
        return id;
    }
    public void setId(long id)
    {
        this.id = id;
    }
    public boolean isActive()
    {
        return isActive;
    }
    public void setActive(boolean isActive)
    {
        this.isActive = isActive;
    }
    public long getMarketID()
    {
        return marketID;
    }
    public void setMarketID(long marketID)
    {
        this.marketID = marketID;
    }
    public String getMarketName()
    {
        return marketName;
    }
    public void setMarketName(String marketName)
    {
        this.marketName = marketName;
    }
    public boolean isNotifyByEmail()
    {
        return notifyByEmail;
    }
    public void setNotifyByEmail(boolean notifyByEmail)
    {
        this.notifyByEmail = notifyByEmail;
    }
    public boolean isNotifyBySMS()
    {
        return notifyBySMS;
    }
    public void setNotifyBySMS(boolean notifyBySMS)
    {
        this.notifyBySMS = notifyBySMS;
    }
    public String getNotifyEmail()
    {
        return notifyEmail;
    }
    public void setNotifyEmail(String notifyEmail)
    {
        this.notifyEmail = notifyEmail;
    }
    public String getNotifySMS()
    {
        return notifySMS;
    }
    public void setNotifySMS(String notifySMS)
    {
        this.notifySMS = notifySMS;
    }
    public String getTimePeriodName()
    {
        return timePeriodName;
    }
    public void setTimePeriodName(String timePeriodName)
    {
        this.timePeriodName = timePeriodName;
    }
    public int getTimePeriodType()
    {
        return timePeriodType;
    }
    public void setTimePeriodType(int timePeriodType)
    {
        this.timePeriodType = timePeriodType;
    }
    public int getTimePeriodValue()
    {
        return timePeriodValue;
    }
    public void setTimePeriodValue(int timePeriodValue)
    {
        this.timePeriodValue = timePeriodValue;
    }
    public long getUserID()
    {
        return userID;
    }
    public void setUserID(long userID)
    {
        this.userID = userID;
    }
        
}
