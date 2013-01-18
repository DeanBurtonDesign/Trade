package com.cleartraders.webadmin.controller.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.webadmin.AdminConstants;

public class AdminLogoffAction extends Action
{
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
        HttpSession session = request.getSession();
        UserBean userBean = (UserBean)session.getAttribute(AdminConstants.USER_KEY);
        
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Request AdminLogoffAction from Account:"+userBean.getLogin_name()+ ", from IP address:"+request.getRemoteAddr());
                
        session.removeAttribute(AdminConstants.USER_KEY);
        session.invalidate();
        
        return mapping.findForward(AdminConstants.FORWARD_SUCCESS);
    }
}
