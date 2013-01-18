package com.cleartraders.webapp.controller.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cleartraders.webapp.WebConstants;

public class GetPasswordInfoAction extends BaseAction
{

    @Override
    protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        String forward = WebConstants.FORWARD_SUCCESS;
        
        return mapping.findForward(forward);
    }

}
