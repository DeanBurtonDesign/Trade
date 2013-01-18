package com.cleartraders.common.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

public class UserBean implements Serializable
{
    private static final long serialVersionUID = 3696411952212547031L;
    private long id = -1;
    private String login_name="";
    private String first_name="";
    private String last_name=""; 
    private long memberType=0;
    private long memberLevel=0;
    private String pwd="";
    private String email="";
    private String mobile="";
    private long country_id=0;
    private String suburb_city="";
    private String street_address="";
    private String state="";
    private String postCode="";    
    private long trading_experience;
    private long age;
    private int gender;
    private int enable;
    private String confirm_code;
    private long time_zone_id=0;
    private long reg_date=0;
    private long expired_date=0;
    private long last_login=0;
    private int locked=0;
    private long sms_credits=0;
    private int status=0;
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public UserBean(UserBean cloneBean)
    {        
        this.id = cloneBean.getId();
        this.login_name = cloneBean.getLogin_name();
        this.first_name = cloneBean.getFirst_name();
        this.last_name = cloneBean.getLast_name();
        this.memberType = cloneBean.getMemberType();
        this.memberLevel = cloneBean.getMemberLevel();
        this.pwd = cloneBean.getPwd();
        this.email = cloneBean.getEmail();
        this.mobile = cloneBean.getMobile();
        this.country_id = cloneBean.getCountry_id();
        this.suburb_city = cloneBean.getSuburb_city();
        this.street_address = cloneBean.getStreet_address();
        this.state = cloneBean.getState();
        this.postCode = cloneBean.getPostCode();
        this.trading_experience = cloneBean.getTrading_experience();
        this.age = cloneBean.getAge();
        this.gender = cloneBean.getGender();
        this.enable = cloneBean.getEnable();
        this.confirm_code = cloneBean.getConfirm_code();
        this.time_zone_id = cloneBean.getTime_zone_id();
        this.reg_date = cloneBean.getReg_date();
        this.expired_date = cloneBean.getExpired_date();
        this.last_login = cloneBean.getLast_login();
        this.status = cloneBean.getStatus();
        this.sms_credits = cloneBean.getSms_credits();
        this.locked = cloneBean.getLocked();
    }
    
    public UserBean(){}
    
    public String getDescrption()
    {
        String descrption="";
        
        descrption += "id="+this.id;
        descrption += ";";
        
        descrption += "login_name="+this.login_name;
        descrption += ";";
        
        descrption += "first_name="+this.first_name;
        descrption += ";";
        
        descrption += "last_name="+this.last_name;
        descrption += ";";
        
        descrption += "memberType="+this.memberType;
        descrption += ";";
        
        descrption += "memberLevel="+this.memberLevel;
        descrption += ";";
        
        descrption += "email="+this.email;
        descrption += ";";
        
        descrption += "mobile="+this.mobile;
        descrption += ";";
        
        descrption += "country_id="+this.country_id;
        descrption += ";";
        
        descrption += "time_zone_id="+this.time_zone_id;
        descrption += ";";
        
        descrption += "suburb_city="+this.suburb_city;
        descrption += ";";
        
        descrption += "street_address="+this.street_address;
        descrption += ";";
        
        descrption += "state="+this.state;
        descrption += ";";
        
        descrption += "postCode="+this.postCode;
        descrption += ";";
        
        descrption += "trading_experience="+this.trading_experience;
        descrption += ";";
        
        descrption += "age="+this.age;
        descrption += ";";
        
        descrption += "gender="+this.gender;
        descrption += ";";
                
        descrption += "reg_date="+dateTimeFormat.format(new Date(this.reg_date));
        descrption += ";";
        
        descrption += "expired_date="+dateTimeFormat.format(new Date(this.expired_date));
        descrption += ";";
        
        descrption += "status="+this.status;
        descrption += ";";
        
        descrption += "sms_credits="+this.sms_credits;
        descrption += ";";
        
        descrption += "last_login="+this.last_login;
        
        return descrption;
    }
    
    public Hashtable<String,String> getProperHash()
    {
      Hashtable<String,String> thisHash = new Hashtable<String,String>();
      
      if (login_name != null)
        thisHash.put("login_name", login_name);
      
      if (first_name != null)
        thisHash.put("first_name", first_name);
      
      if (last_name != null)
        thisHash.put("last_name", last_name);
      
      if (pwd != null)
        thisHash.put("pwd", pwd);
      
      if (email != null)
        thisHash.put("email", email);
      
      if (mobile != null)
        thisHash.put("mobile", mobile);
      
      if (street_address != null)
        thisHash.put("street_address", street_address);
      
      if (confirm_code != null)
        thisHash.put("confirm_code", confirm_code);
      
      if (reg_date != 0)
        thisHash.put("reg_date", String.valueOf(reg_date));
      
      if (expired_date != 0)
        thisHash.put("expired_date", String.valueOf(expired_date));
      
      return thisHash;
    }

    public long getSms_credits()
    {
        return sms_credits;
    }

    public void setSms_credits(long sms_credits)
    {
        this.sms_credits = sms_credits;
    }
    
    public long getMemberType()
    {
        return memberType;
    }

    public void setMemberType(long memberType)
    {
        this.memberType = memberType;
    }
    
    public long getMemberLevel()
    {
        return memberLevel;
    }
    public void setMemberLevel(long memberLevel)
    {
        this.memberLevel = memberLevel;
    }
    public long getAge()
    {
        return age;
    }
    public void setAge(long age)
    {
        this.age = age;
    }
    public String getConfirm_code()
    {
        return confirm_code;
    }
    public void setConfirm_code(String confirm_code)
    {
        this.confirm_code = confirm_code;
    }
    public int getEnable()
    {
        return enable;
    }
    public void setEnable(int enabled)
    {
        this.enable = enabled;
    }
    public int getGender()
    {
        return gender;
    }
    public void setGender(int gender)
    {
        this.gender = gender;
    }
    public String getPostCode()
    {
        return postCode;
    }
    public void setPostCode(String postCode)
    {
        this.postCode = postCode;
    }
    public String getState()
    {
        return state;
    }
    public void setState(String state)
    {
        this.state = state;
    }
    public String getStreet_address()
    {
        return street_address;
    }
    public void setStreet_address(String street_address)
    {
        this.street_address = street_address;
    }
    public long getTrading_experience()
    {
        return trading_experience;
    }
    public void setTrading_experience(long trading_experience)
    {
        this.trading_experience = trading_experience;
    }
    public long getCountry_id()
    {
        return country_id;
    }
    public void setCountry_id(long country_id)
    {
        this.country_id = country_id;
    }
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public String getFirst_name()
    {
        return first_name;
    }
    public void setFirst_name(String first_name)
    {
        this.first_name = first_name;
    }
    public long getId()
    {
        return id;
    }
    public void setId(long id)
    {
        this.id = id;
    }
    public long getLast_login()
    {
        return last_login;
    }
    public void setLast_login(long last_login)
    {
        this.last_login = last_login;
    }
    public String getLast_name()
    {
        return last_name;
    }
    public void setLast_name(String last_name)
    {
        this.last_name = last_name;
    }
    public int getLocked()
    {
        return locked;
    }
    public void setLocked(int locked)
    {
        this.locked = locked;
    }
    public String getLogin_name()
    {
        return login_name;
    }
    public void setLogin_name(String login_name)
    {
        this.login_name = login_name;
    }
    public String getMobile()
    {
        return mobile;
    }
    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }
    public String getPwd()
    {
        return pwd;
    }
    public void setPwd(String pwd)
    {
        this.pwd = pwd;
    }    
    public long getExpired_date()
    {
        return expired_date;
    }
    public void setExpired_date(long expired_date)
    {
        this.expired_date = expired_date;
    }
    public void setExpired_date(String expiredDate) throws ParseException
    {
        this.expired_date = dateTimeFormat.parse(expiredDate).getTime();
    }
    public void setReg_date(String regDate) throws ParseException
    {
        this.reg_date = dateTimeFormat.parse(regDate).getTime();
    }
    public long getReg_date()
    {
        return reg_date;
    }
    public void setReg_date(long reg_date)
    {
        this.reg_date = reg_date;
    }
    public String getSuburb_city()
    {
        return suburb_city;
    }
    public void setSuburb_city(String suburb_city)
    {
        this.suburb_city = suburb_city;
    }
    public long getTime_zone_id()
    {
        return time_zone_id;
    }
    public void setTime_zone_id(long time_zone_id)
    {
        this.time_zone_id = time_zone_id;
    }
    public int getStatus()
    {
        return status;
    }
    public void setStatus(int status)
    {
        this.status = status;
    }
}
