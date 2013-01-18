package com.cleartraders.webapp.model.bean;

public class EnquiryBean
{
    private int enquiry_type = 0;
    private String first_name = "";
    private String last_name = "";
    private String email = "";
    private String enquiry = "";
    
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public String getEnquiry()
    {
        return enquiry;
    }
    public void setEnquiry(String enquiry)
    {
        this.enquiry = enquiry;
    }
    public int getEnquiry_type()
    {
        return enquiry_type;
    }
    public void setEnquiry_type(int enquiry_type)
    {
        this.enquiry_type = enquiry_type;
    }
    public String getFirst_name()
    {
        return first_name;
    }
    public void setFirst_name(String first_name)
    {
        this.first_name = first_name;
    }
    public String getLast_name()
    {
        return last_name;
    }
    public void setLast_name(String last_name)
    {
        this.last_name = last_name;
    }
    
}
