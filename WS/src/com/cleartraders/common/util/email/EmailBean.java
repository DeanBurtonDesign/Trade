package com.cleartraders.common.util.email;

import java.io.Serializable;

public class EmailBean  implements Serializable
{
    private static final long serialVersionUID = 4358185133995184898L;
    
    private String emailSubject="";
    private String emailBody="";
    private String recipients="";
    
    public EmailBean()
    {
    }
    
    public EmailBean(EmailBean oEmail)
    {
        emailSubject=oEmail.getEmailSubject();
        emailBody = oEmail.getEmailBody();
        recipients = oEmail.getRecipients();
    }
    
    public String getEmailBody()
    {
        return emailBody;
    }
    public void setEmailBody(String emailBody)
    {
        this.emailBody = emailBody;
    }
    public String getEmailSubject()
    {
        return emailSubject;
    }
    public void setEmailSubject(String emailSubject)
    {
        this.emailSubject = emailSubject;
    }
    public String getRecipients()
    {
        return recipients;
    }
    public void setRecipients(String recipients)
    {
        this.recipients = recipients;
    }
    
    
}
