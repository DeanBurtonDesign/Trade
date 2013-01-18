package com.cleartraders.webapp.controller.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class UpdateUserSessionAttributeAction extends BaseAction
{

    @Override
    protected ActionForward executeAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        HttpSession session = request.getSession();
        
        String attributeName = (String)request.getParameter("attribute_name");
        String attributeValue = (String)request.getParameter("attribute_value");
        
        session.setAttribute(attributeName, attributeValue);
        
        return null;
    }

}
