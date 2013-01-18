package com.cleartraders.webapp.controller.action;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.entity.MarketPeriodBean;
import com.cleartraders.common.entity.MarketTypeBean;
import com.cleartraders.common.entity.MarketTypeBeanComparator;
import com.cleartraders.common.entity.StrategyBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.entity.UserSignalPreferenceBaseBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.model.myaccount.AccountController;

public class GetAllChartsAction extends BaseAction
{
    protected ActionForward executeAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        String forwardName=WebConstants.FORWARD_SUCCESS;
        
        try
        {
            HttpSession session = request.getSession();
            
            //Base action already handle logout or timeout issue, so, here we don't need check
            UserBean oUserBean = (UserBean)session.getAttribute(WebConstants.USER_KEY);
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Load All Charts request from User:"+oUserBean.getLogin_name()+", IP:"+request.getRemoteAddr());
            
            AccountController accountController = new AccountController();
                        
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
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));            
        }
        
        return mapping.findForward(forwardName);
    }

}
