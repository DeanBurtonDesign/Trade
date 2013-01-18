package com.cleartraders.webapp.controller.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.model.autotrade.AutoTradeController;
import com.cleartraders.webapp.model.bean.AutoTradeInfoBean;

public class GetAutoTrade extends BaseAction
{
    protected ActionForward executeAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        String forwardName=WebConstants.FORWARD_SUCCESS;
        
        try
        {
            HttpSession session = request.getSession();
            
            //Base action already handle logout or timeout issue, so, here we don't need check
            UserBean oUserBean = (UserBean)session.getAttribute(WebConstants.USER_KEY);
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Get Auto Trade Info from User:"+oUserBean.getLogin_name()+", IP:"+request.getRemoteAddr());
                                    
            //get auto trade info
            AutoTradeInfoBean oAutoTradeInfo = (new AutoTradeController()).getAutoTradeInfo(oUserBean);
            request.setAttribute(WebConstants.USER_AUTO_TRADE_INFO, oAutoTradeInfo);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));   
        }
        
        return mapping.findForward(forwardName);
    }

}
