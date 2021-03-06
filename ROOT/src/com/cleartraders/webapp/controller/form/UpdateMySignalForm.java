/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.cleartraders.webapp.controller.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.cleartraders.common.util.tools.CommonTools;

/** 
 * MyEclipse Struts
 * Creation date: 09-24-2008
 * 
 * XDoclet definition:
 * @struts.form name="updateMySignalForm"
 */
public class UpdateMySignalForm extends ActionForm
{
    private static final long serialVersionUID = 3099353267946665518L;

    /** email_enable_flag property */
    private String email_enable_flag;

    /** sms_enable_flag property */
    private String sms_enable_flag;

    /** signal_id property */
    private String signal_id;

    /** active_enable_flag property */
    private String active_enable_flag;
    
    private String operation_type;
    
    private String all_signal_setting_value;
    

    /*
     * Generated Methods
     */

    public String getAll_signal_setting_value()
    {
        return all_signal_setting_value;
    }

    public void setAll_signal_setting_value(String all_signal_setting_value)
    {
        this.all_signal_setting_value = all_signal_setting_value;
    }

    public String getOperation_type()
    {
        return operation_type;
    }

    public void setOperation_type(String operation_type)
    {
        this.operation_type = operation_type;
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
        // TODO Auto-generated method stub
        if(null == email_enable_flag)
        {
            email_enable_flag = "-1";
        }
        else
        {
            email_enable_flag = CommonTools.filteDangerString(email_enable_flag);
        }
        
        if(null == sms_enable_flag)
        {
            sms_enable_flag = "-1";
        }
        else
        {
            sms_enable_flag = CommonTools.filteDangerString(sms_enable_flag);
        }

        if(null == signal_id)
        {
            signal_id = "-1";
        }
        else
        {
            signal_id = CommonTools.filteDangerString(signal_id);
        }

        if(null == active_enable_flag)
        {
            active_enable_flag = "-1";
        }
        else
        {
            active_enable_flag = CommonTools.filteDangerString(active_enable_flag);
        }

        if(null == operation_type)
        {
            operation_type = "-1";
        }
        else
        {
            operation_type = CommonTools.filteDangerString(operation_type);
        }

        if(null == all_signal_setting_value)
        {
            all_signal_setting_value = "";
        }
        else
        {
            all_signal_setting_value = CommonTools.filteDangerString(all_signal_setting_value);
        }
        
        return null;
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
     * Returns the email_enable_flag.
     * @return String
     */
    public String getEmail_enable_flag()
    {
        return email_enable_flag;
    }

    /** 
     * Set the email_enable_flag.
     * @param email_enable_flag The email_enable_flag to set
     */
    public void setEmail_enable_flag(String email_enable_flag)
    {
        this.email_enable_flag = email_enable_flag;
    }

    /** 
     * Returns the sms_enable_flag.
     * @return String
     */
    public String getSms_enable_flag()
    {
        return sms_enable_flag;
    }

    /** 
     * Set the sms_enable_flag.
     * @param sms_enable_flag The sms_enable_flag to set
     */
    public void setSms_enable_flag(String sms_enable_flag)
    {
        this.sms_enable_flag = sms_enable_flag;
    }

    /** 
     * Returns the signal_id.
     * @return String
     */
    public String getSignal_id()
    {
        return signal_id;
    }

    /** 
     * Set the signal_id.
     * @param signal_id The signal_id to set
     */
    public void setSignal_id(String signal_id)
    {
        this.signal_id = signal_id;
    }

    /** 
     * Returns the active_enable_flag.
     * @return String
     */
    public String getActive_enable_flag()
    {
        return active_enable_flag;
    }

    /** 
     * Set the active_enable_flag.
     * @param active_enable_flag The active_enable_flag to set
     */
    public void setActive_enable_flag(String active_enable_flag)
    {
        this.active_enable_flag = active_enable_flag;
    }
}
