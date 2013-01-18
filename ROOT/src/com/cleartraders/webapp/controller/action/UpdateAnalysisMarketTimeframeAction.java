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
import com.cleartraders.webapp.model.analysis.AnalysisController;

public class UpdateAnalysisMarketTimeframeAction extends BaseAction
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
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Update analysis markets request from User:"+oUserBean.getLogin_name()+", IP:"+request.getRemoteAddr());
            
            String preferenceID="";
            String timePeriodType="";
            String strategyID="";
            
            if(null != request.getParameter("id"))
            {
                preferenceID = (String)request.getParameter("id");
            }
            
            if(null != request.getParameter("timeframe"))
            {
                timePeriodType = (String)request.getParameter("timeframe");
            }
            
            if(null != request.getParameter("strategyID"))
            {
                strategyID = (String)request.getParameter("strategyID");
            }
            
            new AnalysisController().updateAnalysisMarketTimeFrame(oUserBean,preferenceID,timePeriodType,strategyID);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        
        return null;
    }

}
