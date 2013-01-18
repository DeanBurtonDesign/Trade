package com.cleartraders.common.entity;

/**
 * 
 * 
create table temp_password_table
(
    user_id             int unsigned not null primary key,
    temp_pwd        varchar(100), # generated password for users who forgot
    confirm_code    varchar(100)  # Confirm code which need user to click
);

 *
 */
public class TempPasswordBean
{
    private long user_id=0;
    private String temp_pwd="";
    private String confirm_code="";
    
    public String getConfirm_code()
    {
        return confirm_code;
    }
    public void setConfirm_code(String confirm_code)
    {
        this.confirm_code = confirm_code;
    }
    public String getTemp_pwd()
    {
        return temp_pwd;
    }
    public void setTemp_pwd(String temp_pwd)
    {
        this.temp_pwd = temp_pwd;
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
