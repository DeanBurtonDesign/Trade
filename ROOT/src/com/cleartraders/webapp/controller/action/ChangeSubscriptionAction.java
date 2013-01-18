/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.cleartraders.webapp.controller.action;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.ProductBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.config.WebappResManager;
import com.cleartraders.webapp.controller.form.ChangeSubscriptionForm;
import com.cleartraders.webapp.db.DBAccessor;
import com.cleartraders.webapp.model.login.LoginController;
import com.cleartraders.webapp.model.myaccount.AccountController;

/** 
 * MyEclipse Struts
 * Creation date: 03-08-2009
 * 
 * XDoclet definition:
 * @struts.action path="/changeSubscription" name="changeSubscriptionForm" input="/change-subscription.jsp" scope="request" validate="true"
 */
public class ChangeSubscriptionAction extends Action
{
    /*
     * Generated Methods
     */

    /** 
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
    {
        String forwardName=WebConstants.FORWARD_FAILED;
        
        try
        {
            ChangeSubscriptionForm changeSubscriptionForm = (ChangeSubscriptionForm) form;
            HttpSession session = (HttpSession) request.getSession();
            
            //check security code first
            String securitycode = changeSubscriptionForm.getSecurity_code();
            String sessionSecuritycode = (String) session.getAttribute(WebConstants.SECURITY_CODE);
            
            if(sessionSecuritycode.equals(securitycode))
            {
                LoginController loginController = new LoginController(); 
                
                //check user exist or not
                if(!loginController.userExist(changeSubscriptionForm.getUsername()))
                {
                    LogTools.getInstance().insertLog(DebugLevel.WARNING,"Change Subscription (Without Login) request from IP:"+request.getRemoteAddr()+
                            ". But the user name "+changeSubscriptionForm.getUsername()+" doesn't exist!");
                    
                    forwardName=WebConstants.FORWARD_FAILED;
                    
                    ActionMessages errors = new ActionMessages();
                    errors.add("username", new ActionMessage("login.user.not.exist.error"));
                    
                    saveErrors(request, errors);
                                        
                    return new ActionForward(mapping.getInput());
                }
                
                //check user was already verified by confirmed email link
                if(!loginController.userWasVerified(changeSubscriptionForm.getUsername()))
                {
                    LogTools.getInstance().insertLog(DebugLevel.WARNING,"Change Subscription (Without Login) request from IP:"+request.getRemoteAddr()+
                            ". But the User:"+changeSubscriptionForm.getUsername()+" is not verified!");
                    
                    forwardName=WebConstants.FORWARD_FAILED;
                    
                    ActionMessages errors = new ActionMessages();
                    errors.add("username", new ActionMessage("login.user.not.verified.error"));
                    
                    saveErrors(request, errors);
                                        
                    return new ActionForward(mapping.getInput());
                }
                
                //check username and password
                if(loginController.loginCheck(changeSubscriptionForm.getUsername(),changeSubscriptionForm.getPassword()))
                {
                    UserBean oUserBean = loginController.getUserBeanByName(changeSubscriptionForm.getUsername());
                    
                    long newProductID = Long.parseLong(changeSubscriptionForm.getSubscrible_product());
                    ProductBean newProductBean = (new AccountController()).getProductByID(newProductID);
                    
                    if(null == newProductBean || newProductID == oUserBean.getMemberLevel())
                    {
                        LogTools.getInstance().insertLog(DebugLevel.WARNING,"Change Subscription (Without Login) request from IP:"+request.getRemoteAddr()+
                                " User:"+changeSubscriptionForm.getUsername()+". But the Subscription Product does not exist!");
                        
                        ActionMessages errors = new ActionMessages();
                        errors.add("subscrible_product", new ActionMessage("upgrade.membership.as.same.as.current"));
                        
                        saveErrors(request, errors);
                        
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
                        
                        LogTools.getInstance().insertLog(DebugLevel.INFO,"Send reuqest of Change Subscription (Without Login) to Paypal from IP:"+request.getRemoteAddr()+
                                "User:"+changeSubscriptionForm.getUsername()+". Current product plan:"+currentProductBean.getName()+". New product plan:"+newProductBean.getName()+
                                " ButtonID:"+hosted_button_id+". User change subscription from Free Trial to Paid.");
                                                
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
                        
                        LogTools.getInstance().insertLog(DebugLevel.INFO,"Send reuqest of Change Subscription (Without Login) to Paypal from IP:"+request.getRemoteAddr()+
                                "User:"+changeSubscriptionForm.getUsername()+". Current product plan:"+currentProductBean.getName()+". New product plan:"+newProductBean.getName());
                        
                        return mapping.findForward("upgrade");
                    }
                    else
                    {
                        //cancel subscription
                        String paypalCmd = WebappResManager.getInstance().getPaypal_cancel_subscription_cmd();
                        String paypalAccount = WebappResManager.getInstance().getPaypal_account();
                        
                        request.setAttribute("cmd", paypalCmd);
                        request.setAttribute("alias", URLEncoder.encode(paypalAccount,"UTF-8"));
                        
                        LogTools.getInstance().insertLog(DebugLevel.INFO,"Send reuqest of Cancel Subscription (Without Login) to Paypal from IP:"+request.getRemoteAddr()+
                                "User:"+changeSubscriptionForm.getUsername()+". Current product plan:"+currentProductBean.getName()+". New product plan:"+newProductBean.getName());
                                                
                        return mapping.findForward("cancel");
                    }
                }
                else
                {
                    LogTools.getInstance().insertLog(DebugLevel.ERROR,"Change Subscription (Without Login) request from IP:"+request.getRemoteAddr()+
                            " User:"+changeSubscriptionForm.getUsername()+". Username or password error!");
                    
                    ActionMessages errors = new ActionMessages();
                    errors.add("username", new ActionMessage("login.username.password.error"));                    
                    saveErrors(request, errors);
                                        
                    return new ActionForward(mapping.getInput());
                }
            }
            else
            {                
                ActionMessages errors = new ActionMessages();
                errors.add("security_code", new ActionMessage("upgrade.membership.securitycode.error"));
              
                saveErrors(request, errors);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        
        return mapping.findForward(forwardName);
    }
}