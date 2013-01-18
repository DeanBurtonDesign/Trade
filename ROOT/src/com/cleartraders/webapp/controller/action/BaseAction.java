package com.cleartraders.webapp.controller.action;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.entity.ProductBean;
import com.cleartraders.common.entity.QuickLinkBean;
import com.cleartraders.common.entity.QuickLinkComparator;
import com.cleartraders.common.entity.TimeZoneBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.model.myaccount.AccountController;

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
        
        if(!this.userIsNotExpired(request))
        {
            ActionMessages errors = new ActionMessages();
            errors.add("error", new ActionMessage("login.user.expired.error"));
            
            saveErrors(request, errors);
            
            return mapping.findForward(WebConstants.FORWARD_FAILED);
        }
        
        //if user is Normal, then, get all common data which is shown at common page (left_1.jsp and left_2.jsp)
        getAllCommonData(request);
        
        return executeAction(mapping, form, request, response);
    }

    protected abstract ActionForward executeAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException;

    private boolean userIsNotExpired(HttpServletRequest request)
    {
        if (request.getSession().getAttribute(WebConstants.USER_KEY) == null)
        {
            return false;            
        }
        else
        {
            UserBean userBean = (UserBean)request.getSession().getAttribute(WebConstants.USER_KEY);
            
            return userBean.getExpired_date() > System.currentTimeMillis();
        }
    }
    
    private boolean userIsLoggedIn(HttpServletRequest request)
    {
        if (request.getSession().getAttribute(WebConstants.USER_KEY) == null)
        {
            return false;
        }

        return true;
    }
    
    private void getAllCommonData(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        UserBean oUserBean = (UserBean)session.getAttribute(WebConstants.USER_KEY);
        
        AccountController accountController = new AccountController();    
        
        //get all quick links
        List<QuickLinkBean> oQuickLinks = accountController.getAllQuickLinks(oUserBean);
        Comparator<QuickLinkBean> comp = new QuickLinkComparator();
        Collections.sort(oQuickLinks,comp);
        request.setAttribute(WebConstants.USER_QUICK_LINKS, oQuickLinks);
        
        //get current sms credits
        long currentSmsCredits = accountController.getUserSmsCredits(oUserBean);
        request.setAttribute(WebConstants.MY_SMS_CREDITS, Long.valueOf(currentSmsCredits));
        
        //get current membership
        ProductBean productBean = accountController.getMyMembership(oUserBean);
        request.setAttribute(WebConstants.MY_MEMBER_SHIP, productBean);
        
        return;
    }
    
    protected long getUserOffsetTime(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        UserBean oUserBean = (UserBean)session.getAttribute(WebConstants.USER_KEY);
        
        TimeZoneBean oUserTimeZone = DataCache.getInstance().getTimeZoneByID(oUserBean.getTime_zone_id());
                
        //System should use server time, since the Admin client generates signal are all running at server
        int serverOffsetTime = TimeZone.getDefault().getOffset(System.currentTimeMillis());
        
        //InfoTrace.getInstance().printInfo(DebugLevel.INFO,"The time offset of IQFeed server is: "+CommonResManager.getInstance().getIqfeed_server_timezone_offset()+"; Current server is: "+TimeZone.getDefault().getOffset(System.currentTimeMillis()));
        
        //if user is under GMT+8, and server is located at GMT-8
        //then, user time is 2008-10-27 08:00:00, and user want to query 10-27 data
        //however,since server is 2008-10-16 while user query data. if here we don't minus timeoffset, server will return empty value
        long userOffsetTime = oUserTimeZone.getOffset()*60*60*1000 - serverOffsetTime;
        
        return userOffsetTime;
    }
}
