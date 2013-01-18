package com.cleartraders.webapp.model.payment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.config.WebappResManager;


public class PaymentPDTController
{
    public boolean handlePaypalPayment(String transactionID)
    {
        String transactionDetails = getTransactionDetails(transactionID);
        if(null == transactionDetails || "".equals(transactionDetails))
        {
            return false;
        }
                
        return handleTransactionDetails(transactionID, transactionDetails);
    }
    
    public int getPaymentType(String transactionID)
    {
        InfoTrace.getInstance().printInfo(DebugLevel.INFO, "Call PaymentPDTController.getPaymentType(),"+transactionID);
        LogTools.getInstance().insertLog(DebugLevel.INFO, "Call PaymentPDTController.getPaymentType(),"+transactionID);
        
        String transactionDetails = getTransactionDetails(transactionID);
        
        if(null == transactionDetails || "".equals(transactionDetails))
        {
            return -1;
        }
        
        int paymentType = -1;
        
        if(verifyPaymentType(transactionDetails,WebappResManager.getInstance().getSms_50_button_id()))
        {
            return WebConstants.SMS_PACKAGE_50;
        }
        else if(verifyPaymentType(transactionDetails,WebappResManager.getInstance().getSms_250_button_id()))
        {
            return WebConstants.SMS_PACKAGE_250;
        }
        else if(verifyPaymentType(transactionDetails,WebappResManager.getInstance().getSms_500_button_id()))
        {
            return WebConstants.SMS_PACKAGE_500;
        }
        else if(verifyPaymentType(transactionDetails,WebappResManager.getInstance().getSms_1000_button_id()))
        {
            return WebConstants.SMS_PACKAGE_1000;
        }
        else if(verifyPaymentType(transactionDetails,WebappResManager.getInstance().getSms_5000_button_id()))
        {
            return WebConstants.SMS_PACKAGE_5000;
        }
        else if(verifyPaymentType(transactionDetails,WebappResManager.getInstance().getSpecialist_button_id()))
        {
            return WebConstants.SPECIALIST_MEMBER;
        }
        else if(verifyPaymentType(transactionDetails,WebappResManager.getInstance().getExpert_button_id()))
        {
            return WebConstants.EXPERT_MEMBER;
        }
        else if(verifyPaymentType(transactionDetails,WebappResManager.getInstance().getMaster_button_id()))
        {
            return WebConstants.MASTER_MEMBER;
        }
        
        return paymentType;
    }
    
    private boolean verifyPaymentType(String paymentResponce, String buttonID)
    {
        String checkKeyword = "btn_id="+buttonID;
        if(paymentResponce.indexOf(checkKeyword) != -1)
        {
            InfoTrace.getInstance().printInfo(DebugLevel.INFO, "return True from PaymentPDTController.verifyPaymentType(), keyword is "+checkKeyword);
            LogTools.getInstance().insertLog(DebugLevel.INFO, "return True from PaymentPDTController.verifyPaymentType(), keyword is "+checkKeyword);
            
            return true;
        }
        else
        {
            InfoTrace.getInstance().printInfo(DebugLevel.INFO, "return False from PaymentPDTController.verifyPaymentType(), keyword is "+checkKeyword);
            LogTools.getInstance().insertLog(DebugLevel.INFO, "return False from PaymentPDTController.verifyPaymentType(), keyword is "+checkKeyword);
            
            return false;
        }
    }
    
    public boolean handleTransactionDetails(String transactionID, String transactionDetails)
    {
        return analyzePaymentResultFromPDT(transactionDetails);
    }
    
    private String getTransactionDetails(String transactionID)
    {
        if(null == transactionID || "".equals(transactionID))
        {
            return null;
        }
        
        String result = null;
        
        String requestPayPalURL = WebappResManager.getInstance().getPaypal_payment_query_url();
        String identityToken = WebappResManager.getInstance().getPaypal_account_indentity_token();
        String requestPaymentDetailCMD = WebappResManager.getInstance().getRequest_payment_detail_cmd();
        
        String checkURL = requestPayPalURL+"cmd="+requestPaymentDetailCMD+"&at="+identityToken+"&tx="+transactionID;
        
        try
        {
            URL url = new URL(checkURL);
            URLConnection urlconn = url.openConnection();
            BufferedReader dataStream = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
            StringBuffer paypalResponceResult = new StringBuffer();
            
            String currentLine=null;
            while(null!=(currentLine=dataStream.readLine()))
            {
                paypalResponceResult.append(currentLine);
            }
            
            InfoTrace.getInstance().printInfo(DebugLevel.INFO, "Paypal responce ' "+paypalResponceResult+" ' while system request transaction,'"+transactionID+"'");
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Paypal responce ' "+paypalResponceResult+" ' while system request transaction,'"+transactionID+"'");
        
            result = paypalResponceResult.toString();
        }
        catch(Exception e)
        {
            result = null;
            
            e.printStackTrace();
        }
        
        return result;
    }
    
    public boolean analyzePaymentResultFromPDT(String paymentResponce)
    {
        if(null == paymentResponce || "".equals(paymentResponce))
        {
            return false;
        }
        
        if(paymentResponce.indexOf("SUCCESS") != -1 && paymentResponce.indexOf("payment_status=Completed") != -1)
        {
            InfoTrace.getInstance().printInfo(DebugLevel.INFO, "Check PDT info, and Paypal responce is successful!");
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Check PDT info, and Paypal responce is successful!");
            
            return true;
        }
        else
        {
            return false;
        }
    }
}
