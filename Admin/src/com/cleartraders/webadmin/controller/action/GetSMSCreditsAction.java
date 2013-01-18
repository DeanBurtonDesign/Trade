package com.cleartraders.webadmin.controller.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cleartraders.common.entity.SmsPackageBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.webadmin.AdminConstants;
import com.cleartraders.webadmin.model.smscredits.SMSCreditsController;

public class GetSMSCreditsAction extends BaseAction
{

    @Override
    protected ActionForward executeAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        String forwardName = AdminConstants.FORWARD_SUCCESS;
                
        List<SmsPackageBean> allSMSPackages = new SMSCreditsController().getAllSMSPackages();
        request.setAttribute(AdminConstants.SMS_PACKAGE_LIST, allSMSPackages);
        
        //for log info
        UserBean currentAdmin = (UserBean)request.getSession().getAttribute(AdminConstants.USER_KEY);
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Request GetSMSCreditsAction from Account:"+currentAdmin.getLogin_name()+
                ", the number of SMS packages:"+allSMSPackages.size());
        
        return mapping.findForward(forwardName);
    }

}
