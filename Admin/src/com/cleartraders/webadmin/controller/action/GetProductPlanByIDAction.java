package com.cleartraders.webadmin.controller.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cleartraders.common.entity.ProductBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.webadmin.AdminConstants;
import com.cleartraders.webadmin.model.productplans.ProductPlansController;

public class GetProductPlanByIDAction extends BaseAction
{

    @Override
    protected ActionForward executeAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        ProductPlansController productPlansController = new ProductPlansController();
        PrintWriter out = null;

        String productIDPara = (String)request.getParameter("id");
        
        if(null == productIDPara)
        {
            return null;
        }
        
        try
        {
            String result = "";
            
            long productID = Long.parseLong(productIDPara);
            
            //for log info
            UserBean currentAdmin = (UserBean)request.getSession().getAttribute(AdminConstants.USER_KEY);
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Request GetProductPlanByIDAction from Account:"+currentAdmin.getLogin_name()+
                    ", get product id:"+productID);
            
            ProductBean searchResult = productPlansController.getProductByID(productID);
            if(null != searchResult)
            {
                result = searchResult.getDescrption();
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
