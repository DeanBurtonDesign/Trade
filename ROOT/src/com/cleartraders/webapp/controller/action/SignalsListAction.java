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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cleartraders.common.entity.MarketPeriodBean;
import com.cleartraders.common.entity.MarketTypeBean;
import com.cleartraders.common.entity.MarketTypeBeanComparator;
import com.cleartraders.common.entity.Signal;
import com.cleartraders.common.entity.StrategyBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.entity.UserSignalPreferenceBaseBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.config.WebappResManager;
import com.cleartraders.webapp.model.activesignals.ActiveSignalsList;
import com.cleartraders.webapp.model.myaccount.AccountController;

/** 
 * MyEclipse Struts
 * Creation date: 09-14-2008
 * 
 * XDoclet definition:
 * @struts.action validate="true"
 * @struts.action-forward name="SUCCESS" path="/signals.jsp"
 */
public class SignalsListAction extends BaseAction
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
    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            ActiveSignalsList signalController = new ActiveSignalsList();
            HttpSession session = request.getSession();
            
            //Base action already handle logout or timeout issue, so, here we don't need check
            UserBean oUserBean = (UserBean)session.getAttribute(WebConstants.USER_KEY);
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Load signal List request from User:"+oUserBean.getLogin_name()+", IP:"+request.getRemoteAddr()+", time:"+System.currentTimeMillis());
            
            int start = 0;
            int range = WebappResManager.getInstance().getEach_page_size();
    
            if(null != request.getParameter(WebConstants.START_NUMBER))
            {
                start = Integer.parseInt(request.getParameter(WebConstants.START_NUMBER));
            }
            
            if(null != request.getParameter(WebConstants.RANGE_NUMBER))
            {
                range = Integer.parseInt(request.getParameter(WebConstants.RANGE_NUMBER));
            }
            
            AccountController accountController = new AccountController();
            
            List<Signal> oCurrentPageOfActiveSignal = signalController.getSpecificPageOfActiveSignals(oUserBean,start,range);
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Complete get current signal at "+System.currentTimeMillis());
            
            request.setAttribute(WebConstants.ALL_ACTIVE_SIGNAL, oCurrentPageOfActiveSignal);
            request.setAttribute(WebConstants.START_NUMBER, start);
            request.setAttribute(WebConstants.RANGE_NUMBER, range);
            
            //Don't need totally active signals so far Fixed by Peter 2009-12-14
            //request.setAttribute(WebConstants.TOTAL_NUMBER, signalController.getTotalNumberOfActiveSingalByUSer(oUserBean));
                 
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Prepare to get passed signal at "+System.currentTimeMillis());
            
            List<Signal> oPastedSignal = signalController.getPastedSignals(oUserBean, getUserOffsetTime(request));
            request.setAttribute(WebConstants.PASTED_SIGNAL, oPastedSignal);
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Prepare to get all active markets at "+System.currentTimeMillis());
            
            //get all active markets of current user
            List<MarketTypeBean> oActiveMarketList = accountController.getAllActiveMarkets(oUserBean);
            Comparator<MarketTypeBean> compForMarket = new MarketTypeBeanComparator();
            Collections.sort(oActiveMarketList,compForMarket);        
            request.setAttribute(WebConstants.USER_ACTIVE_MARKETS, oActiveMarketList);
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Prepare to get all strategy info at "+System.currentTimeMillis());
            
            //get all strategy info
            Map<Long, StrategyBean> oAllStrategyMap = accountController.getAllStrategy();
            request.setAttribute(WebConstants.ALL_STRATEGY_INFO, oAllStrategyMap);
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Prepare to get all active signal at "+System.currentTimeMillis());
            
            //get all My Market preference of current user
            HashMap<Long, UserSignalPreferenceBaseBean> allMyActiveSignalMap = new HashMap<Long, UserSignalPreferenceBaseBean>();
            List<UserSignalPreferenceBaseBean> oAllMySignalPrferenceBean = accountController.getUserSignalPreferenceBaseBean(oUserBean,false);
            for(int i=0; i<oAllMySignalPrferenceBean.size(); i++)
            {
                allMyActiveSignalMap.put(Long.valueOf(oAllMySignalPrferenceBean.get(i).getMarket_type_id()), oAllMySignalPrferenceBean.get(i));
            }                
            request.setAttribute(WebConstants.ALL_MY_ACTIVE_SIGNAL_MAP, allMyActiveSignalMap);
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Prepare to get market strategy info at "+System.currentTimeMillis());
            
            Map<Long, List<StrategyBean>> marketStrategy = accountController.getMarketStrategyMap();
            request.setAttribute(WebConstants.MARKET_STRATEGY_MAP, marketStrategy);
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Prepare to get strategy timeframe info at "+System.currentTimeMillis());
            
            Map<Long, List<MarketPeriodBean>> strategyTimeframe = accountController.getStrategyTimeframeMap();
            request.setAttribute(WebConstants.STRATEGY_TIMEFRAME_MAP, strategyTimeframe);
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Prepare to get active signals preference info at "+System.currentTimeMillis());
            
            //get all active signals preference
            List<UserSignalPreferenceBaseBean> oActiveSignalsPreference = accountController.getActiveUserSignalPreferenceBean(oUserBean);
            request.setAttribute(WebConstants.USER_ACTIVE_SIGNAL_PREFERENCE, oActiveSignalsPreference);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        
        return mapping.findForward(WebConstants.FORWARD_SUCCESS);
    }
}