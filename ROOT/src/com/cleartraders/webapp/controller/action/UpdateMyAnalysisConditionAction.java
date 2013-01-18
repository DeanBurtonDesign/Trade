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

import com.cleartraders.common.entity.QuickLinkBean;
import com.cleartraders.common.entity.QuickLinkComparator;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.controller.form.UpdateMyAnalysisConditionForm;
import com.cleartraders.webapp.model.analysis.AnalysisController;
import com.cleartraders.webapp.model.bean.UserAnalysisMarketBean;
import com.cleartraders.webapp.model.myaccount.AccountController;

public class UpdateMyAnalysisConditionAction extends BaseAction
{
    protected ActionForward executeAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        String forwardName=WebConstants.FORWARD_FAILED;
        
        try
        {
            HttpSession session = request.getSession();
            
            //Base action already handle logout or timeout issue, so, here we don't need check
            UserBean oUserBean = (UserBean)session.getAttribute(WebConstants.USER_KEY);
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Update My Analysis Condition request from User:"+oUserBean.getLogin_name()+", IP:"+request.getRemoteAddr());
            
            UpdateMyAnalysisConditionForm updateMySignalForm = (UpdateMyAnalysisConditionForm) form; 
            
            int iOperation_type = Integer.parseInt(updateMySignalForm.getOperation_type());
            
            String analysis_type = updateMySignalForm.getAnalysis_type();
            
            if(WebConstants.REMOVE_SPECIFIC_ANALYSIS_CONDITIOn == iOperation_type)
            {
                long conditionID = Long.parseLong(updateMySignalForm.getCondition_id());
                
                if(new AnalysisController().removeMyAnalysisCondition(oUserBean,conditionID))
                {
                    if("1".equals(analysis_type))
                    {
                        forwardName=WebConstants.FORWARD_CUMULATIVE;
                    }
                    else if("2".equals(analysis_type))
                    {
                        forwardName=WebConstants.FORWARD_DASHBOARD;
                    }
                    else if("3".equals(analysis_type))
                    {
                        forwardName=WebConstants.FORWARD_PROFIT_LOSS;
                    }
                    else if("4".equals(analysis_type))
                    {
                        forwardName=WebConstants.FORWARD_STATISTICS;
                    }
                    
                    //get all user current analysis market list
                    List<UserAnalysisMarketBean> oAllAnalysisCondition = (new AnalysisController()).getUserAnalysisMarketList(oUserBean);       
                    request.setAttribute(WebConstants.MY_ANALYSIS_CONDITION, oAllAnalysisCondition);
                    
                    //get all quick links
                    List<QuickLinkBean> oQuickLinks = (new AccountController()).getAllQuickLinks(oUserBean);
                    Comparator<QuickLinkBean> comp = new QuickLinkComparator();
                    Collections.sort(oQuickLinks,comp);
                    request.setAttribute(WebConstants.USER_QUICK_LINKS, oQuickLinks);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        
        return mapping.findForward(forwardName);
    }

}
