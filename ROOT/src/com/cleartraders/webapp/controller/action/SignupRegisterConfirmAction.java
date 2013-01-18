/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.cleartraders.webapp.controller.action;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.entity.MarketPeriodBean;
import com.cleartraders.common.entity.MarketTypeBean;
import com.cleartraders.common.entity.MarketTypeBeanComparator;
import com.cleartraders.common.entity.ProductBean;
import com.cleartraders.common.entity.QuickLinkBean;
import com.cleartraders.common.entity.QuickLinkComparator;
import com.cleartraders.common.entity.StrategyBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.entity.UserSignalPreferenceBaseBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.model.login.LoginController;
import com.cleartraders.webapp.model.myaccount.AccountController;
import com.cleartraders.webapp.model.signup.SignupController;

/** 
 * MyEclipse Struts
 * Creation date: 02-03-2009
 * 
 * XDoclet definition:
 * @struts.action validate="true"
 */
public class SignupRegisterConfirmAction extends Action
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
    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
    {
        String forwardName=WebConstants.FORWARD_FAILED;
        
        try
        {
            //get parameter
            String userName = request.getParameter("username");
            String confirmedString = request.getParameter("id");
            
            if(null != userName && null != confirmedString)
            {
                LoginController loginController = new LoginController();
                UserBean userBean = loginController.getUserBeanByName(userName);
                
                LogTools.getInstance().insertLog(DebugLevel.INFO,"Signup Register Confirm request from User:"+userBean.getLogin_name()+", IP:"+request.getRemoteAddr()+", Confirm Code:"+confirmedString);
                
                SignupController signupController = new SignupController();
                if(signupController.checkUserConfirmed(userBean,confirmedString))
                {
                    LogTools.getInstance().insertLog(DebugLevel.INFO,"Signup Register Confirm passed from User:"+userBean.getLogin_name()+", IP:"+request.getRemoteAddr()+", Confirm Code:"+confirmedString);
                                        
                    forwardName=WebConstants.FORWARD_SUCCESS;
                    
                    HttpSession session = (HttpSession) request.getSession();
                    UserBean oUserBean = new LoginController().getUserBeanByName(userName);
                    session.setAttribute(WebConstants.USER_KEY, oUserBean);
                    
                    AccountController accountController = new AccountController();
                    
                    //get all quick links
                    List<QuickLinkBean> oQuickLinks = accountController.getAllQuickLinks(oUserBean);
                    Comparator<QuickLinkBean> comp = new QuickLinkComparator();
                    Collections.sort(oQuickLinks,comp);
                    request.setAttribute(WebConstants.USER_QUICK_LINKS, oQuickLinks);
                    
                    //get current sms credits
                    long currentSmsCredits = accountController.getUserSmsCredits(oUserBean);
                    request.setAttribute(WebConstants.MY_SMS_CREDITS, Long.valueOf(currentSmsCredits));
                    
                    //get current membership
                    ProductBean productBean = accountController.getMyMembership(oUserBean);
                    request.setAttribute(WebConstants.MY_MEMBER_SHIP, productBean);
                    
                    //get all active markets of current user
                    List<MarketTypeBean> oActiveMarketList = accountController.getAllActiveMarkets(oUserBean);
                    Comparator<MarketTypeBean> compForMarket = new MarketTypeBeanComparator();
                    Collections.sort(oActiveMarketList,compForMarket);
                    request.setAttribute(WebConstants.USER_ACTIVE_MARKETS, oActiveMarketList);
                    
                    //get all My Market preference of current user
                    HashMap<Long, UserSignalPreferenceBaseBean> allMyActiveSignalMap = new HashMap<Long, UserSignalPreferenceBaseBean>();
                    List<UserSignalPreferenceBaseBean> oAllMySignalPrferenceBean = accountController.getUserSignalPreferenceBaseBean(oUserBean,false);
                    for(int i=0; i<oAllMySignalPrferenceBean.size(); i++)
                    {
                        allMyActiveSignalMap.put(Long.valueOf(oAllMySignalPrferenceBean.get(i).getMarket_type_id()), oAllMySignalPrferenceBean.get(i));
                    }                
                    request.setAttribute(WebConstants.ALL_MY_ACTIVE_SIGNAL_MAP, allMyActiveSignalMap);
                    
                    //get all current markets
                    request.setAttribute(WebConstants.ALL_CURRENT_MARKETS, DataCache.getInstance().getAllMarketType());
                    
                    List<MarketPeriodBean> allTimeFrames = DataCache.getInstance().getAllMarketPeriod();
                    request.setAttribute(WebConstants.ALL_MARTKET_PERIOD, allTimeFrames);
                    
                    Map<Long, List<StrategyBean>> marketStrategy = accountController.getMarketStrategyMap();
                    request.setAttribute(WebConstants.MARKET_STRATEGY_MAP, marketStrategy);
                    
                    Map<Long, List<MarketPeriodBean>> strategyTimeframe = accountController.getStrategyTimeframeMap();
                    request.setAttribute(WebConstants.STRATEGY_TIMEFRAME_MAP, strategyTimeframe);
                    
                    ActionForward forward = new ActionForward();
                    forward.setPath(mapping.findForward(forwardName).getPath());
                    forward.setRedirect(true);
                    
                    return forward;
                }
                else
                {
                    LogTools.getInstance().insertLog(DebugLevel.ERROR,"Signup Register Confirm failed from User:"+userBean.getLogin_name()+", IP:"+request.getRemoteAddr()+", Confirm Code:"+confirmedString);
                                        
                    ActionMessages infos = new ActionMessages();
                    infos.add("username", new ActionMessage("signup.confirm.failed.error"));
                    saveErrors(request, infos);
                }
            }
            else
            {
                LogTools.getInstance().insertLog(DebugLevel.ERROR,"Signup Register Confirm failed from IP:"+request.getRemoteAddr()+", because the parameter is wrong!");
                                
                ActionMessages errors = new ActionMessages();
                errors.add("username", new ActionMessage("signup.confirm.link.error"));
                saveErrors(request, errors);
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