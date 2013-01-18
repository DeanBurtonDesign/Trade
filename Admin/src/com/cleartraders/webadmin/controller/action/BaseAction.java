package com.cleartraders.webadmin.controller.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.webadmin.AdminConstants;

public abstract class BaseAction extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        response.setHeader("Cache-Control", "no-cache");
        //Forces caches to obtain a new copy of the page from the origin server
        response.setHeader("Cache-Control", "no-store");
        //Directs caches not to store the page under any circumstance
        response.setDateHeader("Expires", 0);
        //Causes the proxy cache to see the page as "stale"
        response.setHeader("Pragma", "no-cache");
        //HTTP 1.0 backward compatibility

        //check session is NOT end
        if (!this.userIsLoggedIn(request))
        {
            ActionMessages errors = new ActionMessages();
            errors.add("error", new ActionMessage("logon.sessionEnded"));
            
            saveErrors(request, errors);
            
            return mapping.findForward("sessionended");
        }
        
        //check user is not expired
        if(!this.userIsNotExpired(request))
        {
            ActionMessages errors = new ActionMessages();
            errors.add("error", new ActionMessage("login.user.expired.error"));
            
            saveErrors(request, errors);
            
            return mapping.findForward(AdminConstants.FORWARD_FAILED);
        }
        
        //check user is admin
        if(!this.userIsAdmin(request))
        {
            ActionMessages errors = new ActionMessages();
            errors.add("error", new ActionMessage("login.username.password.error"));
            
            saveErrors(request, errors);
            
            return mapping.findForward(AdminConstants.FORWARD_FAILED);
        }
        

        return executeAction(mapping, form, request, response);
    }

    protected abstract ActionForward executeAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException;
    
    private boolean userIsAdmin(HttpServletRequest request)
    {
        if (request.getSession().getAttribute(AdminConstants.USER_KEY) == null)
        {
            return false;            
        }
        else
        {
            UserBean userBean = (UserBean)request.getSession().getAttribute(AdminConstants.USER_KEY);
            
            return userBean.getMemberType() == CommonDefine.ADMIN_MEMBER_TYPE;
        }
    }

    private boolean userIsNotExpired(HttpServletRequest request)
    {
        if (request.getSession().getAttribute(AdminConstants.USER_KEY) == null)
        {
            return false;            
        }
        else
        {
            UserBean userBean = (UserBean)request.getSession().getAttribute(AdminConstants.USER_KEY);
            
            return userBean.getExpired_date() > System.currentTimeMillis();
        }
    }
    
    private boolean userIsLoggedIn(HttpServletRequest request)
    {
        if (request.getSession().getAttribute(AdminConstants.USER_KEY) == null)
        {
            return false;
        }

        return true;
    }
}
