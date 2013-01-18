/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.cleartraders.webapp.controller.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.controller.form.GetPasswordForm;
import com.cleartraders.webapp.db.DBAccessor;
import com.cleartraders.webapp.model.contact.ContactController;

/** 
 * MyEclipse Struts
 * Creation date: 03-14-2009
 * 
 * XDoclet definition:
 * @struts.action path="/getPassword" name="getPasswordForm" input="/get-password.jsp" scope="request" validate="true"
 */
public class GetPasswordAction extends Action
{
    /*
     * Generated Methods
     */

    /** 
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
    {
        String forwardName=WebConstants.FORWARD_FAILED;
        
        try
        {
            GetPasswordForm getPasswordForm = (GetPasswordForm) form;
            
            ActionMessages errors = new ActionMessages();
            HttpSession session = (HttpSession) request.getSession();
            
            String securitycode = getPasswordForm.getSecurity_code();
            String sessionSecuritycode = (String) session.getAttribute(WebConstants.SECURITY_CODE);
            
            if(!sessionSecuritycode.equals(securitycode))
            {
                errors.add("securitycode", new ActionMessage("get.password.securitycode.error"));            
                saveErrors(request, errors);
                
                return mapping.findForward(forwardName);
            }
            
            UserBean oUser = DBAccessor.getInstance().getUserBeanByEmailOrLoginName(getPasswordForm.getEmail_address());
                                            
            if( null == oUser )
            {
                LogTools.getInstance().insertLog(DebugLevel.ERROR,"Find Back Password request from IP:"+request.getRemoteAddr()+". But the user is not exist!");
                
                errors.add("error", new ActionMessage("get.password.email.not.existed.error"));          
                saveErrors(request, errors);
                
                return mapping.findForward(forwardName);
            }
            
            if(new ContactController().sendNewPassword(oUser, getPasswordForm.getPassword()))
            {
                LogTools.getInstance().insertLog(DebugLevel.INFO,"Sent new Password to User:"+oUser.getLogin_name()+" from IP:"+request.getRemoteAddr()+" successfully!");
                
                forwardName = WebConstants.FORWARD_SUCCESS;
                
                return mapping.findForward(forwardName);
            }
            else
            {
                LogTools.getInstance().insertLog(DebugLevel.INFO,"Sent new Password to User:"+oUser.getLogin_name()+" from IP:"+request.getRemoteAddr()+" failed!");
                
                errors.add("error", new ActionMessage("get.password.email.sent.failed"));          
                saveErrors(request, errors);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));    
        }
        
        return mapping.findForward(forwardName);
    }
}
