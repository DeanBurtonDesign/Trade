package com.cleartraders.webapp.model.payment;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.email.EmailHandler;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.sms.SMSBean;
import com.cleartraders.common.util.sms.SMSHandler;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.config.WebappResManager;
import com.cleartraders.webapp.db.DBAccessor;
import com.cleartraders.webapp.model.notification.EmailNotificationFactory;

public class IPNSubscribeSignupHandler implements IPNHandler
{
    public boolean handle(HashMap<String, String> allParameters)
    {
        String userLoginName = allParameters.get("custom");
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO, "Receive Subscription Signup event from "+userLoginName);
        LogTools.getInstance().insertLog(DebugLevel.INFO, "Receive Subscription Signup event from "+userLoginName);
                        
        UserBean userBean = DBAccessor.getInstance().getUserInfoByLoginName(userLoginName);
        
        String address_city = "";
        String address_zip = "";
        String address_street = "";
        String address_state = "";
        
        try
        {
            //get address_city, address_zip (post code), address_street, address_state from Paypal info
            address_city = URLDecoder.decode(allParameters.get("address_city"), "UTF-8");
            address_zip = URLDecoder.decode(allParameters.get("address_zip"), "UTF-8");
            address_street = URLDecoder.decode(allParameters.get("address_street"), "UTF-8");
            address_state = URLDecoder.decode(allParameters.get("address_state"), "UTF-8");
        }
        catch(UnsupportedEncodingException decodeExp)
        {
            decodeExp.printStackTrace();
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(decodeExp));
            
            address_city = "";
            address_zip = "";
            address_street = "";
            address_state = "";
        }
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO, "Signup Payment User is from City="+address_city+", Zip Code="+address_zip+", Street="+address_street+",State="+address_state);
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Signup Payment User is from City="+address_city+", Zip Code="+address_zip+", Street="+address_street+",State="+address_state);
        
        DBAccessor.getInstance().updateUserResidentialInfo(userBean.getId(), address_city, address_zip, address_street, address_state);
                
        //only send email and encrypt pwd at first time register
        //user didn't login means this is new user and need encript pwd and send email
        if(null != userBean && userBean.getLast_login() == 0)
        {
            //send out signup notification email
            EmailHandler.getInstance().appendEmailToSend(EmailNotificationFactory.getNotificationEmail(userBean,WebConstants.PAID_USER_SIGNUP_TEMPLATE));
            
            //send SMS message to user
            sendSignupSMSToFreeTrialUser(userBean);
            
            DBAccessor.getInstance().updateUserPWDByUserID(userBean.getId(), CommonTools.encryptSHA(userBean.getPwd()));
                        
            return true;
        }
        else if(null == userBean)
        {
            InfoTrace.getInstance().printInfo(DebugLevel.WARNING, "Handle Subscription Signup event, but Can NOT find username: "+userLoginName);
            LogTools.getInstance().insertLog(DebugLevel.WARNING, "Handle Subscription event, but Can NOT find username: "+userLoginName);
            
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public void sendEmail(UserBean oUserBean)
    {
    }
    
    private void sendSignupSMSToFreeTrialUser(UserBean userBean)
    {
        SMSBean oSMS = new SMSBean();
        oSMS.setReceiverMobile(userBean.getMobile());
        oSMS.setSmsContent(WebappResManager.getInstance().getPaid_user_sms_notification());
        
        SMSHandler.getInstance().appendSMSToSend(oSMS);
    }
}
