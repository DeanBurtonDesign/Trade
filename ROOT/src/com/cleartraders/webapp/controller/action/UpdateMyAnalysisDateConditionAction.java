package com.cleartraders.webapp.controller.action;

import java.io.IOException;
import java.text.SimpleDateFormat;

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
import com.cleartraders.webapp.model.analysis.AnalysisController;

public class UpdateMyAnalysisDateConditionAction extends BaseAction
{
    protected ActionForward executeAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        try
        {
            HttpSession session = request.getSession();
            
            //Base action already handle logout or timeout issue, so, here we don't need check
            UserBean oUserBean = (UserBean)session.getAttribute(WebConstants.USER_KEY);
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Update My Analysis Date request from User:"+oUserBean.getLogin_name()+", IP:"+request.getRemoteAddr());
                        
            long fromDate = 0;
            long toDate = 0;
            
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");        
        
            if(null != request.getParameter("fromDate"))
            {
                fromDate = dateTimeFormat.parse((String)request.getParameter("fromDate")).getTime();
            }
            
            if(null != request.getParameter("toDate"))
            {
                toDate = dateTimeFormat.parse((String)request.getParameter("toDate")).getTime();
            }
            
            new AnalysisController().updateMyAnalysisConditionDate(oUserBean,fromDate,toDate);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        
        return null;
    }
}
