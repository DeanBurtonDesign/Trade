package com.cleartraders.webapp.model.bean;

public class TellAFriendsBean
{
    private String sender_name = "";
    private String sender_email = "";
    private String receiver_name_1 = "";
    private String receiver_email_1 = "";
    private String receiver_name_2 = "";
    private String receiver_email_2 = "";
    
    public String getReceiver_email_1()
    {
        return receiver_email_1;
    }
    public void setReceiver_email_1(String receiver_email_1)
    {
        this.receiver_email_1 = receiver_email_1;
    }
    public String getReceiver_email_2()
    {
        return receiver_email_2;
    }
    public void setReceiver_email_2(String receiver_email_2)
    {
        this.receiver_email_2 = receiver_email_2;
    }
    public String getReceiver_name_1()
    {
        return receiver_name_1;
    }
    public void setReceiver_name_1(String receiver_name_1)
    {
        this.receiver_name_1 = receiver_name_1;
    }
    public String getReceiver_name_2()
    {
        return receiver_name_2;
    }
    public void setReceiver_name_2(String receiver_name_2)
    {
        this.receiver_name_2 = receiver_name_2;
    }
    public String getSender_email()
    {
        return sender_email;
    }
    public void setSender_email(String sender_email)
    {
        this.sender_email = sender_email;
    }
    public String getSender_name()
    {
        return sender_name;
    }
    public void setSender_name(String sender_name)
    {
        this.sender_name = sender_name;
    }
    
    
}
