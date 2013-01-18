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
import com.cleartraders.webapp.controller.form.ChangeAutoTradeStatusForm;
import com.cleartraders.webapp.model.autotrade.AutoTradeController;
import com.cleartraders.webapp.model.bean.AutoTradeInfoBean;

public class ChangeAutoTradeStatus extends BaseAction
{

    @Override
    protected ActionForward executeAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        String forwardName=WebConstants.FORWARD_SUCCESS;
        UserBean oUserBean=null;
        try
        {
            HttpSession session = request.getSession();
            
            //Base action already handle logout or timeout issue, so, here we don't need check
            oUserBean = (UserBean)session.getAttribute(WebConstants.USER_KEY);        
            
            ChangeAutoTradeStatusForm updateMySignalForm = (ChangeAutoTradeStatusForm) form; 
            int operationType = Integer.parseInt(updateMySignalForm.getOperation_type());
            
            boolean changeResult = (new AutoTradeController()).changeAutoTradeStatus(oUserBean, operationType);
            if(changeResult)
            {
                LogTools.getInstance().insertLog(DebugLevel.INFO, "User: "+oUserBean.getLogin_name()+", Operation Type:"+operationType+". Change auto trade flag successfully!");
            }
            else
            {
                LogTools.getInstance().insertLog(DebugLevel.ERROR, "User: "+oUserBean.getLogin_name()+", Operation Type:"+operationType+". Change auto trade flag failed!");
            }     
        }
        catch(Exception e)
        {
            forwardName=WebConstants.FORWARD_FAILED;
            
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            //get auto trade info
            AutoTradeInfoBean oAutoTradeInfo = (new AutoTradeController()).getAutoTradeInfo(oUserBean);
            request.setAttribute(WebConstants.USER_AUTO_TRADE_INFO, oAutoTradeInfo);
        }
        
        return mapping.findForward(forwardName);
    }

}
