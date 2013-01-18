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
 * Creation date: 09-22-2008
 * 
 * XDoclet definition:
 * @struts.form name="ChangePWDForm"
 */
public class ChangePWDForm extends ActionForm
{
    private static final long serialVersionUID = -6780962392911361373L;

    /** current_password property */
    private String current_password;
    
    /** new_password property */
    private String new_password;

    /** confirm_new_password property */
    private String confirm_new_password;

    /*
     * Generated Methods
     */

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
        
        if(null != current_password )
        {
            current_password = CommonTools.filteDangerString(current_password);
        }
        
        if(null != new_password)
        {
            new_password = CommonTools.filteDangerString(new_password);
        }
                
        if(null != confirm_new_password)
        {
            confirm_new_password = CommonTools.filteDangerString(confirm_new_password);
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
     * Returns the new_password.
     * @return String
     */
    public String getNew_password()
    {
        return new_password;
    }

    /** 
     * Set the new_password.
     * @param new_password The new_password to set
     */
    public void setNew_password(String new_password)
    {
        this.new_password = new_password;
    }

    /** 
     * Returns the current_password.
     * @return String
     */
    public String getCurrent_password()
    {
        return current_password;
    }

    /** 
     * Set the current_password.
     * @param current_password The current_password to set
     */
    public void setCurrent_password(String current_password)
    {
        this.current_password = current_password;
    }

    /** 
     * Returns the confirm_new_password.
     * @return String
     */
    public String getConfirm_new_password()
    {
        return confirm_new_password;
    }

    /** 
     * Set the confirm_new_password.
     * @param confirm_new_password The confirm_new_password to set
     */
    public void setConfirm_new_password(String confirm_new_password)
    {
        this.confirm_new_password = confirm_new_password;
    }
}
