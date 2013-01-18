package com.cleartraders.webapp.controller.action;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
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
import com.cleartraders.common.entity.ProductBean;
import com.cleartraders.common.entity.StrategyBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.model.analysis.AnalysisController;
import com.cleartraders.webapp.model.bean.AnalysisMarketBeanComparator;
import com.cleartraders.webapp.model.bean.TradeBean;
import com.cleartraders.webapp.model.bean.UserAnalysisMarketBean;
import com.cleartraders.webapp.model.myaccount.AccountController;

public class GetDashbordAction extends BaseAction
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
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Get Dashbord request from User:"+oUserBean.getLogin_name()+", IP:"+request.getRemoteAddr());
            
            AccountController accountController = new AccountController();
            
            ProductBean productBean = accountController.getMyMembership(oUserBean);
            if(productBean.getId() != WebConstants.MASTER_MEMBER_ID)
            {
                forwardName=WebConstants.FORWARD_ADD_ANALYSIS;
                
                List<TradeBean> oTop20Trades = (new AnalysisController()).getTopProfitTrade(20);
                request.setAttribute(WebConstants.TOP_TRADES, oTop20Trades);
            }
            else
            {
                Map<Long, List<StrategyBean>> marketStrategy = accountController.getMarketStrategyMap();
                request.setAttribute(WebConstants.MARKET_STRATEGY_MAP, marketStrategy);
                
                Map<Long, List<MarketPeriodBean>> strategyTimeframe = accountController.getStrategyTimeframeMap();
                request.setAttribute(WebConstants.STRATEGY_TIMEFRAME_MAP, strategyTimeframe);
                
                //get all user current analysis market list
                List<UserAnalysisMarketBean> oAllAnalysisCondition = (new AnalysisController()).getUserAnalysisMarketList(oUserBean); 
                Comparator<UserAnalysisMarketBean> compMarket = new AnalysisMarketBeanComparator();
                Collections.sort(oAllAnalysisCondition,compMarket);
                request.setAttribute(WebConstants.MY_ANALYSIS_CONDITION, oAllAnalysisCondition);       
                
                List<MarketPeriodBean> allTimeFrames = DataCache.getInstance().getAllMarketPeriod();
                request.setAttribute(WebConstants.ALL_MARTKET_PERIOD, allTimeFrames);
                
                List<TradeBean> oTop20Trades = (new AnalysisController()).getTopProfitTrade(20);
                request.setAttribute(WebConstants.TOP_TRADES, oTop20Trades);
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
