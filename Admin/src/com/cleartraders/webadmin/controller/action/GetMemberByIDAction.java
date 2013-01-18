package com.cleartraders.webadmin.controller.action;

import java.io.IOException;

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
import com.cleartraders.webadmin.model.member.MemberController;

public class GetMemberByIDAction extends BaseAction
{

    @Override
    protected ActionForward executeAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        MemberController memberController = new MemberController();

        response.setContentType("text/xml");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Charset","UTF-8");
        
        String memberIDPara = (String)request.getParameter("id");
        
        if(null == memberIDPara)
        {
            return null;
        }
        
        try
        {
            long memberID = Long.parseLong(memberIDPara);
            
            UserBean userBean = memberController.getMemberByID(memberID);
            
            //for log info
            UserBean currentAdmin = (UserBean)request.getSession().getAttribute(AdminConstants.USER_KEY);
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Request GetMemberByIDAction from Account:"+currentAdmin.getLogin_name()+
                    ", target user:"+userBean.getLogin_name());
            
            String result = userBean.getDescrption();            
            result += ";markets="+memberController.getMemberSubcribleMarketString(memberID);
            
            response.getWriter().println(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e)); 
        }
        
        return null;
    }

}
