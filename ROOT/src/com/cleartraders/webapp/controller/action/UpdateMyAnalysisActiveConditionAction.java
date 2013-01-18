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

public class UpdateMyAnalysisActiveConditionAction extends BaseAction
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
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Update My Analysis Actived flag request from User:"+oUserBean.getLogin_name()+", IP:"+request.getRemoteAddr());
                        
            long conditionId = 0;
            int activeFlag = WebConstants.ENABLE;        
        
            if(null != request.getParameter("id"))
            {
                conditionId = Long.parseLong((String)request.getParameter("id"));
            }
            
            if(null != request.getParameter("flag"))
            {
                activeFlag = Integer.parseInt((String)request.getParameter("flag"));
            }
            
            new AnalysisController().updateMyAnalysisConditionActive(oUserBean,conditionId,activeFlag);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        
        return null;
    }

}
