package com.cleartraders.webapp.model.payment;

import java.util.HashMap;

import com.cleartraders.common.entity.SmsPackageBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.email.EmailBean;
import com.cleartraders.common.util.email.EmailHandler;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.webapp.db.DBAccessor;
import com.cleartraders.webapp.model.notification.EmailNotificationFactory;

public class IPNBuyNowPaymentHandler extends IPNPaymentEventHandler
{
    @Override
    public boolean checkSpecialParameters(HashMap<String, String> allParameters)
    {
        InfoTrace.getInstance().printInfo(DebugLevel.INFO, "Receive Buy Now Payment event!");
        LogTools.getInstance().insertLog(DebugLevel.INFO, "Receive Buy Now Payment event!");
        
        //check price
        double paymentGross = Double.valueOf(allParameters.get("payment_gross")).doubleValue();
        String buttonID = allParameters.get("btn_id");
        
        SmsPackageBean oProductBean = DBAccessor.getInstance().getSMSPackageByPrice(paymentGross, buttonID);        
        if(oProductBean == null)
        {
            InfoTrace.getInstance().printInfo(DebugLevel.WARNING, "Payment gross Error, can NOT find suitable product by price! Payment gross is , '"+paymentGross+"'.");
            LogTools.getInstance().insertLog(DebugLevel.WARNING, "Payment gross Error, can NOT find suitable product by price! Payment gross is , '"+paymentGross+"'.");
            
            return false;
        }
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO, "Pass Paypal Subscription payment check!");
        LogTools.getInstance().insertLog(DebugLevel.INFO, "Pass Paypal Subscription payment check!");
        
        return true;
    }

    @Override
    public boolean handleSpecial(HashMap<String, String> allParameters)
    {
        String userLoginName = allParameters.get("custom");
        String buttonID = allParameters.get("btn_id");
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO, "Handle Buy Now Payment event from User:"+userLoginName+", Button ID:"+buttonID);
        LogTools.getInstance().insertLog(DebugLevel.INFO, "Handle Buy Now Payment event from User:"+userLoginName+", Button ID:"+buttonID);
        
        UserBean userBean = DBAccessor.getInstance().getUserInfoByLoginName(userLoginName);        
        if(null == userBean)
        {
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR, "Handle Buy Now Payment event from User:"+userLoginName+", but this user doesn't exist!");
            LogTools.getInstance().insertLog(DebugLevel.ERROR, "Handle Buy Now Payment event from User:"+userLoginName+", but this user doesn't exist!");
            
            return false;
        }
        
        double paymentGross = Double.valueOf(allParameters.get("payment_gross")).doubleValue();
        
        SmsPackageBean oSmsPackage = DBAccessor.getInstance().getSMSPackageByPrice(paymentGross, buttonID); 
        
        //add more sms credits for user
        DBAccessor.getInstance().addSMSCreditsToUser(userBean.getId(), oSmsPackage.getSms_included());
        
        //send BUY SMS notification
        EmailBean buySMSEmail = EmailNotificationFactory.getBuySMSPackageNotificationEmail(userBean,oSmsPackage.getId());
                
        EmailHandler.getInstance().appendEmailToSend(buySMSEmail);
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO, "Complete Handle Buy Now Payment event from User:"+userLoginName+", Button ID:"+buttonID+", Payment Gross:"+paymentGross+", SMS package id:"+oSmsPackage.getId());
        LogTools.getInstance().insertLog(DebugLevel.INFO, "Complete Handle Buy Now Payment event from User:"+userLoginName+", Button ID:"+buttonID+", Payment Gross:"+paymentGross+", SMS package id:"+oSmsPackage.getId());
                
        return true;
    }
    
    public void sendEmail(UserBean oUserBean)
    {        
    }

}
