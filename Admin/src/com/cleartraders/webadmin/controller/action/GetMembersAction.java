package com.cleartraders.webadmin.controller.action;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cleartraders.common.entity.StrategyBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.entity.UserBeanAToZComparator;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.webadmin.AdminConstants;
import com.cleartraders.webadmin.model.member.MemberController;

public class GetMembersAction extends BaseAction
{

    @Override
    protected ActionForward executeAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        String forwardName = AdminConstants.FORWARD_SUCCESS;
        
        MemberController oMemberController = new MemberController();
        //for log info
        UserBean currentAdmin = (UserBean)request.getSession().getAttribute(AdminConstants.USER_KEY);
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Request GetMembersAction from Account:"+currentAdmin.getLogin_name());
        
        List<UserBean> searchResult = oMemberController.getAllMembers();
        
        Comparator<UserBean> comp = new UserBeanAToZComparator();
        Collections.sort(searchResult,comp);
        
        request.setAttribute(AdminConstants.MEMBER_LIST, searchResult);
        
        Map<Long, List<StrategyBean>> marketStrategy = oMemberController.getMarketStrategyMap();
        request.setAttribute(AdminConstants.MARKET_STRATEGY_MAP, marketStrategy);
        
        return mapping.findForward(forwardName);
    }

}
