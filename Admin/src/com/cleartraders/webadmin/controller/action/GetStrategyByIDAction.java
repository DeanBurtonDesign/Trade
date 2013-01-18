package com.cleartraders.webadmin.controller.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cleartraders.common.entity.StrategyBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.webadmin.AdminConstants;
import com.cleartraders.webadmin.model.strategy.StrategyController;

public class GetStrategyByIDAction extends BaseAction
{

    @Override
    protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        StrategyController strategyController = new StrategyController();
        PrintWriter out = null;

        String strategyIDPara = (String)request.getParameter("id");
        
        if(null == strategyIDPara)
        {
            return null;
        }
        
        try
        {
            String result = "";
            
            long strategyID = Long.parseLong(strategyIDPara);
            
            //for log info
            UserBean currentAdmin = (UserBean)request.getSession().getAttribute(AdminConstants.USER_KEY);
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Request GetStrategyByIDAction from Account:"+currentAdmin.getLogin_name()+
                    ", get strategy id:"+strategyID);
            
            StrategyBean strategyBean = strategyController.getStrategyByID(strategyID);
            if(null != strategyBean)
            {
                result = strategyBean.getDescriptionString();
            }
            
            out = new PrintWriter(response.getOutputStream());            
            out.write(result);
            out.flush();
            out.close();
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }
        
        return null;
    }

}
