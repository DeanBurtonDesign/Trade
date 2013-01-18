package com.cleartraders.webapp.model.payment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.config.WebappResManager;
import com.cleartraders.webapp.db.DBAccessor;

public class PaymentIPNController
{
    public boolean handlePaypalIPNNotification(HttpServletRequest request, HttpServletResponse response)
    {
        boolean result = false;
        
        String validationURL = WebappResManager.getInstance().getPaypal_payment_query_url();
        validationURL += "cmd=_notify-validate";
        
        //get all parameters and values, formed request to Paypal to verify
        Enumeration allParameterName = request.getParameterNames();
        HashMap<String,String> allParameters = new HashMap<String,String>();
        
        try
        {
            while(allParameterName.hasMoreElements())
            {
                String paramName = (String)allParameterName.nextElement();
                String paramValue = request.getParameter(paramName);
                
                validationURL += "&"+paramName+"="+URLEncoder.encode(paramValue,"UTF-8");
                allParameters.put(paramName, paramValue);
            }
        }
        catch(UnsupportedEncodingException e)
        {
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            
            return false;
        }
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO, "Verify URL is: ' "+validationURL+" '");
        LogTools.getInstance().insertLog(DebugLevel.INFO, "Verify URL is: ' "+validationURL+" '");
                
        try
        {
            //request Paypal to confirm
            URL url = new URL(validationURL);
            URLConnection urlconn = url.openConnection();
            BufferedReader dataStream = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
            StringBuffer paypalResponceResult = new StringBuffer();
            
            String currentLine=null;
            while(null!=(currentLine=dataStream.readLine()))
            {
                paypalResponceResult.append(currentLine);
            }
            
            if("VERIFIED".equalsIgnoreCase(paypalResponceResult.toString()))
            {
                InfoTrace.getInstance().printInfo(DebugLevel.INFO, "Pass verify of IPN: Responce is: "+paypalResponceResult.toString()+" for request URL "+validationURL+" ,Responce from IP: ' "+request.getRemoteAddr()+" ' is valid request!");
                LogTools.getInstance().insertLog(DebugLevel.INFO, "Pass verify of IPN: Responce is: "+paypalResponceResult.toString()+" for request URL "+validationURL+" ,Responce from IP: ' "+request.getRemoteAddr()+" ' is valid request!");
                
                //valid info
                response.getOutputStream().print("200 OK");
                
                //handle this notification
                if(handleIPNPaymentResult(allParameters))
                {
                    //update session
                    String userLoginName = allParameters.get("custom");
                    UserBean userBean = DBAccessor.getInstance().getUserInfoByLoginName(userLoginName);
                    
                    HttpSession session = request.getSession();
                    session.removeAttribute(WebConstants.USER_KEY);
                    session.setAttribute(WebConstants.USER_KEY, userBean);
                                        
                    result = true;
                    
                    InfoTrace.getInstance().printInfo(DebugLevel.INFO, "Successfully handling payment by IPN notify for User:"+userLoginName+", User session info had been changed. User status="+userBean.getStatus()+", User Expired Date= "+userBean.getExpired_date()+" , User Enable status=: "+userBean.getEnable()+".");
                    LogTools.getInstance().insertLog(DebugLevel.INFO, "Successfully handling payment by IPN notify for User:"+userLoginName+", User session info had been changed. User status="+userBean.getStatus()+", User Expired Date= "+userBean.getExpired_date()+" , User Enable status=: "+userBean.getEnable()+".");
                }
                else
                {
                    InfoTrace.getInstance().printInfo(DebugLevel.WARNING, "Failed handling payment by IPN notify, User session info had been changed.");
                    LogTools.getInstance().insertLog(DebugLevel.WARNING, "Failed handling payment by IPN notify, User session info had been changed.");
                }
            }
            else
            {
                //invalid info
                response.getOutputStream().print("200 OK");
                
                //ignore this request and log this request IP
                InfoTrace.getInstance().printInfo(DebugLevel.WARNING, "Verify Error: Responce is: "+paypalResponceResult.toString()+" for request URL "+validationURL+" ,Responce from IP: ' "+request.getRemoteAddr()+" ' is Invalid request!");
                LogTools.getInstance().insertLog(DebugLevel.WARNING, "Verify Error: Responce is: "+paypalResponceResult.toString()+" for request URL "+validationURL+" ,Responce from IP: ' "+request.getRemoteAddr()+" ' is Invalid request!");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return result;        
    }
    
    private boolean handleIPNPaymentResult(HashMap<String,String> allParameters)
    {
        if(null == allParameters)
        {
            return false;
        }
        
        //get txn type (Event type)
        String txn_type = allParameters.get("txn_type");
                
        return IPNHandlerFactory.getHandler(txn_type).handle(allParameters);
    }
}
