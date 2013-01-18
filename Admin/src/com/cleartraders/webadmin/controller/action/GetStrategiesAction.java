package com.cleartraders.webadmin.controller.action;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cleartraders.common.entity.StrategyBean;
import com.cleartraders.common.entity.StrategyComparator;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.webadmin.AdminConstants;
import com.cleartraders.webadmin.model.strategy.StrategyController;

public class GetStrategiesAction extends BaseAction
{

    @Override
    protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        String forwardName = AdminConstants.FORWARD_SUCCESS;
        
        List<StrategyBean> allStrategy = new StrategyController().getAllStrategyBaseInfo();         
        
        Comparator<StrategyBean> comp = new StrategyComparator();
        Collections.sort(allStrategy,comp);    
        
        request.setAttribute(AdminConstants.STRATEGY_LIST, allStrategy);
        
        //for log info
        UserBean currentAdmin = (UserBean)request.getSession().getAttribute(AdminConstants.USER_KEY);
        
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Request GetStrategiesAction from Account:"+currentAdmin.getLogin_name()+ ", the number of Strategy:"+allStrategy.size());
        
        return mapping.findForward(forwardName);
    }

}
