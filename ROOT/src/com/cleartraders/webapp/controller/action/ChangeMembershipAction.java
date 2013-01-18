package com.cleartraders.webapp.controller.action;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.ProductBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.config.WebappResManager;
import com.cleartraders.webapp.controller.form.ChangeMembershipForm;
import com.cleartraders.webapp.db.DBAccessor;
import com.cleartraders.webapp.model.myaccount.AccountController;

public class ChangeMembershipAction extends BaseAction
{

    @Override
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
            
            ChangeMembershipForm changeMembershipForm = (ChangeMembershipForm) form;
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Change Membership request from User:"+oUserBean.getLogin_name()+", IP:"+request.getRemoteAddr()+
                                                             ", New Member Plan:"+changeMembershipForm.getMember_plan()+", Current Member plan:"+oUserBean.getMemberLevel());      
            
            if(changeMembershipForm.getMember_plan() != null)
            {
                long newProductID = Long.parseLong(changeMembershipForm.getMember_plan());
                ProductBean newProductBean = (new AccountController()).getProductByID(newProductID);
                
                if(null == newProductBean || newProductID == oUserBean.getMemberLevel())
                {
                    //member level doesn't change
                    List<ProductBean> allProducts = (new AccountController()).getAllProducts();
                    
                    request.setAttribute(WebConstants.MY_MEMBER_SHIP, newProductBean);
                    request.setAttribute(WebConstants.ALL_MEMBER_SHIP, allProducts);
                    
                    LogTools.getInstance().insertLog(DebugLevel.WARNING,"Change Membership request from User:"+oUserBean.getLogin_name()+", IP:"+request.getRemoteAddr()+
                    ", Product ID:"+changeMembershipForm.getMember_plan()+". The Product Plan is as same as before. So, system won't request Paypal!");   
                    
                    return mapping.findForward(WebConstants.FORWARD_FAILED);
                }
                 
                ProductBean currentProductBean = (new AccountController()).getProductByID(oUserBean.getMemberLevel());
                if(currentProductBean.getPaid() == CommonDefine.FREE_TRIAL_PRODUCT && newProductBean.getPaid() == CommonDefine.PAID_PRODUCT)
                {
                    //subscrible paid member level
                    String paypalCmd = WebappResManager.getInstance().getPaypal_subscription_cmd();
                    String hosted_button_id = DBAccessor.getInstance().getProductPaypalButtonID(newProductID);
                    
                    request.setAttribute("cmd", paypalCmd);
                    request.setAttribute("hosted_button_id", hosted_button_id);
                    
                    //use Email as custom name to verify
                    request.setAttribute("custom", oUserBean.getEmail());
                    
                    LogTools.getInstance().insertLog(DebugLevel.INFO,"Send request of Membership request for User:"+oUserBean.getLogin_name()+", from IP:"+request.getRemoteAddr()+
                            ", Button ID:"+hosted_button_id+". User was free trail, but now updated to Paid Product!");                               
                    
                    return mapping.findForward("subscrible");
                }
                else if(newProductBean.getPaid() == CommonDefine.PAID_PRODUCT)
                {
                    //upgrade subscription
                    String paypalAccount = WebappResManager.getInstance().getPaypal_account();
                    
                    request.setAttribute("cmd", "_xclick-subscriptions");
                    request.setAttribute("business", URLEncoder.encode(paypalAccount,"UTF-8"));
                    
                    request.setAttribute("item_name",newProductBean.getName());
                    request.setAttribute("item_number","");              
                    
                    request.setAttribute("currency_code", "USD");
                    request.setAttribute("a3", ""+newProductBean.getPrice());
                    request.setAttribute("p3", "1");
                    request.setAttribute("t3", "M");
                    
                    request.setAttribute("modify","2");
                    
                    //use Email as custom name to verify
                    request.setAttribute("custom", oUserBean.getLogin_name());
                    
                    LogTools.getInstance().insertLog(DebugLevel.INFO,"Send request of Membership request for User:"+oUserBean.getLogin_name()+", from IP:"+request.getRemoteAddr()+
                            ", new Price:"+newProductBean.getPrice()+", current member plan:"+oUserBean.getMemberLevel()+". User is Paid Proudct now, now request to update to more advanced Paid Product!");                               
                                        
                    return mapping.findForward("upgrade");
                }
                else
                {
                    //cancel subscription
                    String paypalCmd = WebappResManager.getInstance().getPaypal_cancel_subscription_cmd();
                    String paypalAccount = WebappResManager.getInstance().getPaypal_account();
                    
                    request.setAttribute("cmd", paypalCmd);
                    request.setAttribute("alias", URLEncoder.encode(paypalAccount,"UTF-8"));
                    
                    LogTools.getInstance().insertLog(DebugLevel.INFO,"Send request of Membership request for User:"+oUserBean.getLogin_name()+", from IP:"+request.getRemoteAddr()+
                            ", new Price:"+newProductBean.getPrice()+", current member plan:"+oUserBean.getMemberLevel()+". User was Paid Proudct now, now request to cancel Paid Product!");                               
                                         
                    return mapping.findForward("cancel");
                }
            }
            else
            {
                LogTools.getInstance().insertLog(DebugLevel.INFO,"Change Membership request from User:"+oUserBean.getLogin_name()+", IP:"+request.getRemoteAddr()+
                        ", Button ID:"+changeMembershipForm.getMember_plan()+". But memberplan is NULL!"); 
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
