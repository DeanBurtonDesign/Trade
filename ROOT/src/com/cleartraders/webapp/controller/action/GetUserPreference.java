package com.cleartraders.webapp.controller.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.model.bean.SignalNotificationBean;
import com.cleartraders.webapp.model.bean.UserBrokerInfo;
import com.cleartraders.webapp.model.myaccount.AccountController;

public class GetUserPreference extends BaseAction
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
                    
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Get User preference request from User:"+oUserBean.getLogin_name()+", IP:"+request.getRemoteAddr());
            
            //get all trading time zone
            request.setAttribute(WebConstants.USER_TRADING_TIME_ZONE, (new AccountController()).getUserTimeZone(oUserBean));
            request.setAttribute(WebConstants.ALL_TIME_ZONE, DataCache.getInstance().getAllTimeZone());
            
            //get all signal notifications
            List<SignalNotificationBean> oAllSignalNotification = (new AccountController()).getAllSignalNotification(oUserBean);
            request.setAttribute(WebConstants.USER_SIGNAL_NOTIFICATION, oAllSignalNotification);
                                
            //get broker fee
            UserBrokerInfo oUserBrokerInfo =  new AccountController().getUserBrokerFee(oUserBean);
            request.setAttribute(WebConstants.USER_BROKER_INFO, oUserBrokerInfo);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));   
        }
        
        return mapping.findForward(WebConstants.FORWARD_SUCCESS);
    }

}
