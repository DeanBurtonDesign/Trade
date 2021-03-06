package com.cleartraders.webadmin.controller.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webadmin.AdminConstants;
import com.cleartraders.webadmin.model.smscredits.SMSCreditsController;

public class DeleteSMSPackageByID extends BaseAction
{
    protected ActionForward executeAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        SMSCreditsController smspackageController = new SMSCreditsController();
        PrintWriter out = null;

        String smspackageIDPara = (String)request.getParameter("id");
        
        if(null == smspackageIDPara)
        {
            return null;
        }
        
        try
        {
            String result = "false";
            
            long smspackageID = Long.parseLong(smspackageIDPara);
            
            //for log info
            UserBean currentAdmin = (UserBean)request.getSession().getAttribute(AdminConstants.USER_KEY);
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Request DeleteSMSPackageByID from Account:"+currentAdmin.getLogin_name()+
                    ", sms package ID:"+smspackageID);
            
            if(smspackageController.deleteSMSPackageByID(smspackageID))
            {
                result = "true";
                
                LogTools.getInstance().insertLog(DebugLevel.INFO,"Delete sms package id="+smspackageID+" successfully!");
            }
            
            out = new PrintWriter(response.getOutputStream());            
            out.write(result);
            out.flush();
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));    
        }
        
        return null;
    }

}
