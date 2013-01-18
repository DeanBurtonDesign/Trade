package com.cleartraders.webapp.controller.action;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.chart.jfreechart.ChartUtil;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.model.analysis.AnalysisController;

public class GenerateChartImageAction extends BaseAction
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

            int chartDataType = Integer.parseInt((String)request.getParameter("chartData"));
            int dailyChartType = Integer.parseInt((String)request.getParameter("type"));
            
            int width = Integer.parseInt((String)request.getParameter("width"));
            int height = Integer.parseInt((String)request.getParameter("height"));
            
            String strategyIDList = (String)request.getParameter("strategyID");
            String marketIDList = (String)request.getParameter("marketID");
            String periodIDList = (String)request.getParameter("periodID");
            
            String from = (String)request.getParameter("from");
            String to = (String)request.getParameter("to");
            
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            

            LogTools.getInstance().insertLog(DebugLevel.INFO,"Generate Chart request from User:"+oUserBean.getLogin_name()+", IP:"+request.getRemoteAddr()+
                    ", From Date:"+from+", To Date:"+to+", Strategy ID:"+strategyIDList+", Market ID:"+marketIDList+", period ID List:"+periodIDList);
            
            //parse from date
            long fromDate = 0;
            
            try
            {
                fromDate = dateTimeFormat.parse(from+" 00:00:00").getTime();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
            //parse to date
            long toDate = 0;
            
            try
            {
                toDate = dateTimeFormat.parse(to+" 23:59:59").getTime();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
            //get time offset to search analysis result
            long timeOffsetTime = getUserOffsetTime(request);
                        
            fromDate -= timeOffsetTime;
            toDate -= timeOffsetTime;
            
            BufferedImage bi = null;
            
            if(chartDataType == WebConstants.TRADE_GRAPH_CHART)
            {
                if(dailyChartType == WebConstants.CUMULATIVE_PROFIT_CHART)
                {
                    bi = ChartUtil.getLineChartImageByData(width, height, BufferedImage.TYPE_INT_RGB,(new AnalysisController()).getEveryTradeDataSeries(strategyIDList, marketIDList,periodIDList,fromDate,toDate,timeOffsetTime,false),"yyyy-MM-dd HH:mm");
                }
                else
                {
                    bi = ChartUtil.getBarChartImageByData(width, height, BufferedImage.TYPE_INT_RGB,(new AnalysisController()).getEveryTradeDataSeries(strategyIDList, marketIDList,periodIDList,fromDate,toDate,timeOffsetTime,true),"yyyy-MM-dd HH:mm");
                }
            }
            else
            {
                if(dailyChartType == WebConstants.CUMULATIVE_PROFIT_CHART)
                {
                    bi = ChartUtil.getLineChartImageByData(width, height, BufferedImage.TYPE_INT_RGB,(new AnalysisController()).getCumulativeProftDataSeries(strategyIDList, marketIDList,periodIDList,fromDate,toDate,timeOffsetTime),"yyyy-MM-dd");
                }
                else if(dailyChartType == WebConstants.LOST_PROFIT_CHART)
                {
                    bi = ChartUtil.getBarChartImageByData(width, height, BufferedImage.TYPE_INT_RGB,(new AnalysisController()).getProftDataSeries(strategyIDList, marketIDList,periodIDList,fromDate,toDate,timeOffsetTime),"yyyy-MM-dd");
                }
            }
            
            if(null != bi)
            {
                ChartUtil.createImage(response.getOutputStream(),bi);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        
        return null;
    }
}
