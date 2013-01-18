package com.cleartraders.ws;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;

public class DataServiceServlet extends HttpServlet
{
    private static final long serialVersionUID = -1778888567198953088L;
    
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        PrintWriter out = new PrintWriter(res.getOutputStream());
        String strDataType = (String)req.getParameter("dataType");
        String strSymbol = (String)req.getParameter("symbol");
        String strItems = (String)req.getParameter("items");
        String strUserName = (String)req.getParameter("username");
        String strPWD = (String)req.getParameter("password");
        String strIntervalMin = (String)req.getParameter("intervalMin");
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Call DataServiceServlet, parameter is,dataType="+strDataType+",symbol="+strSymbol+",items="+strItems+",intervalMin="+strIntervalMin);
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Call DataServiceServlet, parameter is,dataType="+strDataType+",symbol="+strSymbol+",items="+strItems+",intervalMin="+strIntervalMin);
        
        if(null == strDataType || null == strSymbol || null == strIntervalMin || null == strItems || null == strUserName || null == strPWD)
        {
            out.write("");
            out.flush();
            out.close();
            
            return;
        }
        
        int iIntervalMin = Integer.parseInt(strIntervalMin);
        int iItems = Integer.parseInt(strItems);
        
        IDataService oDataWS = new DataServiceImpl();
        ArrayList<String> result = oDataWS.getMinuteFinanceData(strUserName, strPWD, strDataType, strSymbol, iItems, iIntervalMin);
        if(null == result)
        {
            out.write("");
            out.flush();
            out.close();
            
            return;
        }
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"DataServiceServlet totally got "+result.size()+" quote items. The last quote is "+result.get(result.size()-1));
        LogTools.getInstance().insertLog(DebugLevel.INFO,"DataServiceServlet totally got "+result.size()+" quote items. The last quote is "+result.get(result.size()-1));
        
        if(result != null && result.size() >= iItems)
        {
            String resultString = "";
            
            //get result according to items number
            for(int i=iItems; i>=1; i--)
            {
                resultString += result.get(result.size()-i);
                
                if(i>1)
                {
                    resultString += ";";
                }
            }
            
            out.write(resultString);
            out.flush();
            out.close();
        }
        else
        {
            out.write("");
            out.flush();
            out.close();
        }
        
        return;
    }
}
