/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.cleartraders.webadmin.controller.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.cleartraders.common.util.tools.CommonTools;

/** 
 * MyEclipse Struts
 * Creation date: 03-02-2009
 * 
 * XDoclet definition:
 * @struts.form name="searchMemberForm"
 */
public class SearchMemberForm extends ActionForm
{
    /*
     * Generated Methods
     */
    
    private String search_name="";
    private String search_email="";

    /**
     * 
     */
    private static final long serialVersionUID = -2270555267636161993L;

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
        
        search_name = CommonTools.filteDangerString(search_name);        
        
        search_email = CommonTools.filteDangerString(search_email);
        
        
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

    public String getSearch_email()
    {
        return search_email;
    }

    public void setSearch_email(String search_email)
    {
        this.search_email = search_email;
    }

    public String getSearch_name()
    {
        return search_name;
    }

    public void setSearch_name(String search_name)
    {
        this.search_name = search_name;
    }
}
