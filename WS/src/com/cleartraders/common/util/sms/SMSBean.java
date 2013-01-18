package com.cleartraders.common.util.sms;

import java.io.Serializable;

public class SMSBean implements Serializable
{
    private static final long serialVersionUID = 2748892572332188457L;

    private String smsContent="";
    private String receiverMobile="";
    
    public SMSBean()
    {        
    }
    
    public SMSBean(SMSBean oSMS)
    {
        smsContent = oSMS.getSmsContent();
        receiverMobile = oSMS.getReceiverMobile();
    }
    
    public String getReceiverMobile()
    {
        return receiverMobile;
    }
    public void setReceiverMobile(String receiverMobile)
    {
        this.receiverMobile = receiverMobile;
    }
    public String getSmsContent()
    {
        return smsContent;
    }
    public void setSmsContent(String smsContent)
    {
        this.smsContent = smsContent;
    }
}
