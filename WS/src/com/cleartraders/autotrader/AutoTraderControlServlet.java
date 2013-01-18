package com.cleartraders.autotrader;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.ws.WSConstants;
import com.cleartraders.ws.db.WSDBAccessor;

public class AutoTraderControlServlet extends HttpServlet
{
    private static final long serialVersionUID = 4595219308258141929L;

    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        OutputStream out=res.getOutputStream();
        ObjectOutputStream objStream=new ObjectOutputStream(out);
        
        String strUserName = (String)req.getParameter("username");
        String strPWD = (String)req.getParameter("password");
        String operationtype = (String)req.getParameter("operationtype");
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Call AutoTraderControlServlet, parameter is,operationtype="+operationtype);
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Call AutoTraderControlServlet, parameter is,operationtype="+operationtype);
        
        if(null == strUserName || null == strPWD || null == operationtype)
        {
            objStream.writeObject("false");
            res.setContentType("text/plain");
            
            return;
        }
        
        if(!checkRights(strUserName,strPWD))
        {
            objStream.writeObject("false");
            res.setContentType("text/plain");
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,"Change AutoTrade flag from User="+strUserName+", IP="+req.getRemoteAddr()+ " is not allowable!");
                        
            return;
        }
        
        String result="false";
        
        if(operationtype.equals(WSConstants.OPEN_AUTO_TRADE+""))
        {
            if(AutoTrader.getInstance().setEnablTrade(true))
            {
                LogTools.getInstance().insertLog(DebugLevel.ERROR,"OPEN AutoTrade from User="+strUserName+", IP="+req.getRemoteAddr()+ " Successfully!");
                
                result = "true";
            }
        }
        else if(operationtype.equals(WSConstants.CLOSE_AUTO_TRADE+""))
        {
            if(AutoTrader.getInstance().setEnablTrade(false))
            {
                result = "true";
                
                //close all position also
                AutoTrader.getInstance().closeAllPosition();
                
                LogTools.getInstance().insertLog(DebugLevel.ERROR,"CLOSE AutoTrade from User="+strUserName+", IP="+req.getRemoteAddr()+ " Successfully!");
                
            }
        }
        else if(operationtype.equals(WSConstants.CLOSE_ALL_OPEN_POSITION+""))
        {
            //pending...
        }
        
        objStream.writeObject(result);
        res.setContentType("text/plain");
    }
    
    private boolean checkRights(String userName, String password)
    {
        UserBean oUser = WSDBAccessor.getInstance().getUserInfoByLoginName(userName);
        
        if(null == oUser || null == oUser.getPwd())
        {
            return false;
        }
        
        return oUser.getPwd().equals(password);
    }
}
