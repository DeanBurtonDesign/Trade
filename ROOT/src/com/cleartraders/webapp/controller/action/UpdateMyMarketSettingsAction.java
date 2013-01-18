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
import com.cleartraders.webapp.model.myaccount.AccountController;

public class UpdateMyMarketSettingsAction extends BaseAction
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

            LogTools.getInstance().insertLog(DebugLevel.INFO,"Update My Market Setting request from User:"+oUserBean.getLogin_name()+", IP:"+request.getRemoteAddr());
            
            String all_signal_setting_value="";
            
            if(null != request.getParameter("value"))
            {
                all_signal_setting_value = (String)request.getParameter("value");
                
                //update all
                String[] all_signal_setting_list = all_signal_setting_value.split(";");
                
                if(checkRequestMarketWithMembership(all_signal_setting_list, oUserBean))
                {
                    updateAllPreference(all_signal_setting_list, oUserBean);
                }
                else
                {
                    LogTools.getInstance().insertLog(DebugLevel.WARNING,"Failed to save all Signals setting request from User:"+oUserBean.getLogin_name()+", IP:"+request.getRemoteAddr()
                            +", because user trying to active markets more than Membership limitation!");
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        
        return null;
    }

    private void updateAllPreference(String[] all_signal_setting_list, UserBean oUserBean)
    {
        if(null == all_signal_setting_list && null == oUserBean)
        {
            return;
        }
        
        AccountController accounterController = new AccountController();
        for(int i=0; i<all_signal_setting_list.length; i++)
        {
            all_signal_setting_list[i] = all_signal_setting_list[i].trim();
            if(all_signal_setting_list[i].length() > 0)
            {
                String[] single_signal_setting = all_signal_setting_list[i].split(",");
                
                long mysignalID = Long.parseLong(single_signal_setting[0]);
                
                int iActiveFlag = Integer.parseInt(single_signal_setting[1]);
                
                int iEmailFlag = Integer.parseInt(single_signal_setting[2]);
                
                int iSmsFlag = Integer.parseInt(single_signal_setting[3]);
                
                accounterController.updateMySignalEnableSetting(oUserBean,mysignalID,iActiveFlag,iSmsFlag,iEmailFlag);
            }
        }
    }
    
    private boolean checkRequestMarketWithMembership(String[] all_signal_setting_list, UserBean oUserBean)
    {
        if(null == all_signal_setting_list && null == oUserBean)
        {
            return false;
        }
        
        int totalMarketsOfMembership = new AccountController().getTotalMarketsOfUser(oUserBean);
        
        int currentActivedMarketAmount = 0;
        for(int i=0; i<all_signal_setting_list.length; i++)
        {
            all_signal_setting_list[i] = all_signal_setting_list[i].trim();
            if(all_signal_setting_list[i].length() > 0)
            {
                String[] single_signal_setting = all_signal_setting_list[i].split(",");
                
                int iActiveFlag = Integer.parseInt(single_signal_setting[1]);
                if(iActiveFlag == WebConstants.ENABLE)
                {
                    currentActivedMarketAmount ++;
                }
            }
        }
        
        return currentActivedMarketAmount<=totalMarketsOfMembership;
    }
}
