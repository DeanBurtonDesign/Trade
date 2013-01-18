package com.cleartraders.webapp.model.bean;

public class SignalNotificationBean
{
    private String email="";
    private String mobile="";
    private int use_contact=0;
    
    public SignalNotificationBean(){};
    
    public SignalNotificationBean(String email, String mobile, int use_contact)
    {
        this.email = email;
        this.mobile = mobile;
        this.use_contact = use_contact;
    }
    
    public int getUse_contact()
    {
        return use_contact;
    }
    public void setUse_contact(int use_contact)
    {
        this.use_contact = use_contact;
    }
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public String getMobile()
    {
        return mobile;
    }
    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }
    
}
