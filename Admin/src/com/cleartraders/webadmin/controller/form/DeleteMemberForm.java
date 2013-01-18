/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.cleartraders.webadmin.controller.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.cleartraders.common.util.tools.CommonTools;

/** 
 * MyEclipse Struts
 * Creation date: 03-11-2009
 * 
 * XDoclet definition:
 * @struts.form name="deleteMemberForm"
 */
public class DeleteMemberForm extends ActionForm
{
    /*
     * Generated Methods
     */

    /**
     * 
     */
    private static final long serialVersionUID = 647415376338484895L;
    
    private String member_delete_id="";
    private String member_delete_name="";
    
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
        
        if(this.member_delete_id == null || this.member_delete_id.trim().length() == 0)
        {
            errors.add("error", new ActionMessage("member.delete.request.data.error"));
        }
        else if(this.member_delete_id.trim().length() > 100)
        {
            errors.add("error", new ActionMessage("member.delete.request.data.error"));
        }
        else
        {
            this.member_delete_id = CommonTools.filteDangerString(member_delete_id);
        }
        
        if(this.member_delete_name == null || this.member_delete_name.trim().length() == 0)
        {
            errors.add("member_name", new ActionMessage("member.delete.request.data.error"));
        }
        else if(this.member_delete_name.trim().length() > 100)
        {
            errors.add("member_name", new ActionMessage("member.delete.request.data.error"));
        }
        else
        {
            this.member_delete_name = CommonTools.filteDangerString(member_delete_name);
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
    
    public String getMember_delete_id()
    {
        return member_delete_id;
    }

    public void setMember_delete_id(String member_delete_id)
    {
        this.member_delete_id = member_delete_id;
    }

    public String getMember_delete_name()
    {
        return member_delete_name;
    }

    public void setMember_delete_name(String member_delete_name)
    {
        this.member_delete_name = member_delete_name;
    }
    
}
