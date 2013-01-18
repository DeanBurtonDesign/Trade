package com.cleartraders.webapp.controller.action;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cleartraders.common.entity.SmsPackageBean;
import com.cleartraders.common.entity.SmsPackageBeanComparator;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.model.myaccount.AccountController;

public class GetSmsCreditsAction extends BaseAction
{

    @Override
    protected ActionForward executeAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        try
        {
            HttpSession session = request.getSession();
            
            //Base action already handle logout or timeout issue, so, here we don't need check
            UserBean oUserBean = (UserBean)session.getAttribute(WebConstants.USER_KEY);
            
            List<SmsPackageBean> allSmsPackages = (new AccountController()).getAllSmsPackages(oUserBean);
            Comparator<SmsPackageBean> comp = new SmsPackageBeanComparator();
            Collections.sort(allSmsPackages,comp);
                        
            long currentSmsCredits = (new AccountController()).getUserSmsCredits(oUserBean);
            
            request.setAttribute(WebConstants.ALL_SMS_PACKAGE, allSmsPackages);
            request.setAttribute(WebConstants.MY_SMS_CREDITS, Long.valueOf(currentSmsCredits));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e)); 
        }
        
        return mapping.findForward(WebConstants.FORWARD_SUCCESS);
    }

}
