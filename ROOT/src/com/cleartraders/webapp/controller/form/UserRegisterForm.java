/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.cleartraders.webapp.controller.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.cleartraders.common.util.tools.CommonTools;

/** 
 * MyEclipse Struts
 * Creation date: 01-30-2009
 * 
 * XDoclet definition:
 * @struts.form name="FreeTrialRegisterForm"
 */
public class UserRegisterForm extends ActionForm
{
    /*
     * Generated fields
     */

    /**
     * 
     */
    private static final long serialVersionUID = 1832103493777951050L;

    /** securityCode property */
    private String securityCode;

    /** email property */
    private String email;

    /** secondName property */
    private String secondName;

    /** firstName property */
    private String firstName;
        
    private String mobileNumber;
    
    private String countryID;
    
    private String timeZoneID;
    
    private String password;
    private String confirmpassword;
    
    /*
     * Generated Methods
     */

    public static long getSerialVersionUID()
    {
        return serialVersionUID;
    }

    public String getCountryID()
    {
        return countryID;
    }

    public void setCountryID(String countryID)
    {
        this.countryID = countryID;
    }

    public String getMobileNumber()
    {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber)
    {
        this.mobileNumber = mobileNumber;
    }


    public String getTimeZoneID()
    {
        return timeZoneID;
    }

    public void setTimeZoneID(String timeZoneID)
    {
        this.timeZoneID = timeZoneID;
    }
    
    public String getConfirmpassword()
    {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword)
    {
        this.confirmpassword = confirmpassword;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    /** 
     * Method validate
     * @param mapping
     * @param request
     * @return ActionErrors
     */
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        
        if(this.password == null || this.password.trim().length() == 0 )
        {
            errors.add("password", new ActionMessage("signup.no.password.error"));
        }
        else if(this.password.trim().length() > 100)
        {
            errors.add("password", new ActionMessage("signup.password.too.long.error"));
        }
        else
        {
            password = CommonTools.filteDangerString(password);
        }
        
        if(this.confirmpassword == null || this.confirmpassword.trim().length() == 0 )
        {
            errors.add("confirmpassword", new ActionMessage("signup.no.confirmpassword.error"));
        }
        else if(this.confirmpassword.trim().length() > 100)
        {
            errors.add("confirmpassword", new ActionMessage("signup.confirmpassword.too.long.error"));
        }
        else
        {
            confirmpassword = CommonTools.filteDangerString(confirmpassword);
            
            if(!password.equals(confirmpassword))
            {
                errors.add("confirmpassword", new ActionMessage("signup.password.not.as.same.as.confirmpassword"));
            }
        }
                
        if(this.securityCode == null || this.securityCode.trim().length() == 0 )
        {
            errors.add("securitycode", new ActionMessage("signup.no.securitycode.error"));
        }
        else if(this.securityCode.trim().length() > 100)
        {
            errors.add("securitycode", new ActionMessage("signup.securitycode.too.long.error"));
        }
        else
        {
            securityCode = CommonTools.filteDangerString(securityCode);
        }
        
        if(this.firstName == null || this.firstName.trim().length() == 0)
        {
            errors.add("firstname", new ActionMessage("signup.no.firstname.error"));
        }
        else if(this.firstName.trim().length() > 100)
        {
            errors.add("firstname", new ActionMessage("signup.firstname.too.long.error"));
        }
        else
        {
            this.firstName = CommonTools.filteDangerString(firstName);
        }
                
        if(this.secondName == null || this.secondName.trim().length() == 0)
        {
            errors.add("secondname", new ActionMessage("signup.no.secondname.error"));
        }
        else if(this.secondName.trim().length() > 100)
        {
            errors.add("secondname", new ActionMessage("signup.secondname.too.long.error"));
        }
        else
        {
            this.secondName = CommonTools.filteDangerString(secondName);
        }
        
        if(this.email == null || this.email.trim().length() == 0)
        {
            errors.add("email", new ActionMessage("signup.no.email.error"));
        }
        else if(this.email.trim().length() > 100)
        {
            errors.add("email", new ActionMessage("signup.email.too.long.error"));
        }
        else
        {
            this.email = CommonTools.filteDangerString(email);
            
            if(!CommonTools.isEMailValid(email))
            {
                errors.add("email", new ActionMessage("signup.email.format.error"));
            }
        }
                      
        if(this.mobileNumber == null || this.mobileNumber.trim().length() == 0)
        {
            errors.add("mobilenumber", new ActionMessage("signup.no.mobile.number.error"));
        }
        else if(this.mobileNumber.trim().length() > 100)
        {
            errors.add("mobilenumber", new ActionMessage("signup.mobile.number.too.long.error"));
        }
        else
        {
            this.mobileNumber = CommonTools.filteDangerString(mobileNumber);
        }
        
        if(this.countryID == null || this.countryID.trim().length() == 0)
        {
            errors.add("countryid", new ActionMessage("signup.no.country.id.error"));
        }
        else if(this.countryID.trim().length() > 100)
        {
            errors.add("countryid", new ActionMessage("signup.country.id.too.long.error"));
        }
        else
        {
            this.countryID = CommonTools.filteDangerString(countryID);
        }
        
        if(this.timeZoneID == null || this.timeZoneID.trim().length() == 0)
        {
            errors.add("timezoneid", new ActionMessage("signup.no.time.zone.id.error"));
        }
        else if(this.timeZoneID.trim().length() > 100)
        {
            errors.add("timezoneid", new ActionMessage("signup.time.zone.id.too.long.error"));
        }
        else
        {
            this.timeZoneID = CommonTools.filteDangerString(timeZoneID);
        }
                
        return errors;
    }

    /** 
     * Method reset
     * @param mapping
     * @param request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        // TODO Auto-generated method stub
    }

    /** 
     * Returns the securityCode.
     * @return String
     */
    public String getSecurityCode()
    {
        return securityCode;
    }

    /** 
     * Set the securityCode.
     * @param securityCode The securityCode to set
     */
    public void setSecurityCode(String securityCode)
    {
        this.securityCode = securityCode;
    }

    /** 
     * Returns the email.
     * @return String
     */
    public String getEmail()
    {
        return email;
    }

    /** 
     * Set the email.
     * @param email The email to set
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /** 
     * Returns the secondName.
     * @return String
     */
    public String getSecondName()
    {
        return secondName;
    }

    /** 
     * Set the secondName.
     * @param secondName The secondName to set
     */
    public void setSecondName(String secondName)
    {
        this.secondName = secondName;
    }

    /** 
     * Returns the firstName.
     * @return String
     */
    public String getFirstName()
    {
        return firstName;
    }

    /** 
     * Set the firstName.
     * @param firstName The firstName to set
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
}
