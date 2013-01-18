package com.cleartraders.webapp.controller.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.entity.TimeZoneBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.model.analysis.AnalysisController;
import com.cleartraders.webapp.model.bean.TradingStatisticsResultBean;

public class TradingStatisticsAction extends BaseAction
{
    protected ActionForward executeAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        try
        {
            HttpSession session = request.getSession();
            
            //Base action already handle logout or timeout issue, so, here we don't need check
            UserBean oUserBean = (UserBean)session.getAttribute(WebConstants.USER_KEY);
                         
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Load Trading Statisics request from User:"+oUserBean.getLogin_name()+", IP:"+request.getRemoteAddr());
            
            String strategyIDList = (String)request.getParameter("strategyID");
            String marketIDList = (String)request.getParameter("marketID");
            String periodIDList = (String)request.getParameter("periodID");
            
            String from = (String)request.getParameter("from");
            String to = (String)request.getParameter("to");
            
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            //parse from date
            long fromDate = 0;            
            if(from.trim().length() > 0)
            {
                fromDate = dateTimeFormat.parse(from+" 00:00:00").getTime();
            }
            
            //parse to date
            long toDate = 0;            
            if(to.trim().length() > 0)
            {
                toDate = dateTimeFormat.parse(to+" 23:59:59").getTime();
            }
            
            //get user time-zone         
            TimeZoneBean oTimeZone = DataCache.getInstance().getTimeZoneByID(oUserBean.getTime_zone_id());
            int defaultOffsetTime = TimeZone.getDefault().getOffset(System.currentTimeMillis());
            
            //if user is under GMT+8, and server is located at GMT-8
            //then, user time is 2008-10-27 08:00:00, and user want to query 10-27 data
            //however,since server is 2008-10-16 while user query data. if here we don't minus timeoffset, server will return empty value
            long timeOffsetTime = oTimeZone.getOffset()*60*60*1000 - defaultOffsetTime;
                        
            fromDate -= timeOffsetTime;
            toDate -= timeOffsetTime;
            
            if(fromDate < 0)
            {
                fromDate = 0;
            }
            
            if(toDate < 0)
            {
                toDate = 0;
            }
            
            TradingStatisticsResultBean oTradingStatisticsResult = new AnalysisController().getTradingStatisticsResult(oUserBean,strategyIDList, marketIDList,periodIDList,fromDate,toDate);
                    
            //pending....
            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setMinimumFractionDigits(2);
            
            oTradingStatisticsResult.setDecimalFormat(decimalFormat);
            
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setMinimumFractionDigits(2);
            PrintWriter out = new PrintWriter(response.getOutputStream());
            
            out.write(oTradingStatisticsResult.getStatisticsResult());
            out.flush();
            out.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        
        return null;
    }

}
