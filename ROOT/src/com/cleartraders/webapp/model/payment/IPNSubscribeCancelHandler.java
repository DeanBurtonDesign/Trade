package com.cleartraders.webapp.model.payment;

import java.util.HashMap;

import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.email.EmailBean;
import com.cleartraders.common.util.email.EmailHandler;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.db.DBAccessor;
import com.cleartraders.webapp.model.notification.EmailNotificationFactory;

public class IPNSubscribeCancelHandler implements IPNHandler
{

    public boolean handle(HashMap<String, String> allParameters)
    {
        String userLoginName = allParameters.get("custom");
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO, "Receive Subscription Cancel event from "+userLoginName);
        LogTools.getInstance().insertLog(DebugLevel.INFO, "Receive Subscription Cancel event from "+userLoginName);
        
        UserBean userBean = DBAccessor.getInstance().getUserInfoByLoginName(userLoginName);
        if(null == userBean)
        {
            return false;
        }
        
        DBAccessor.getInstance().updateUserMembershipByUserID(userBean.getId(), WebConstants.FREE_TRIAL_PRODUCT_ID);
        
        //Dean request to stop cancelled user login immidiately, so, set this user's expire time is current time
        userBean.setExpired_date(System.currentTimeMillis());
        userBean.setStatus(CommonDefine.CANCELLED_USER);
        userBean.setEnable(CommonDefine.USER_NOT_ENABLED);
        
        DBAccessor.getInstance().updateUserStatusInfo(userBean);
        
        sendEmail(userBean);
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO, "Successfully handle Subscription Cancel event from "+userLoginName);
        LogTools.getInstance().insertLog(DebugLevel.INFO, "Successfully handle Subscription Cancel event from "+userLoginName);
        
        return true;
    }
    
    public void sendEmail(UserBean oUserBean)
    {
        //send Subscription cancel notification
        EmailBean cancelEmail = EmailNotificationFactory.getNotificationEmail(oUserBean,WebConstants.CANCEL_MEMBER_SHIP_TEMPLATE);
        
        EmailHandler.getInstance().appendEmailToSend(cancelEmail);        
    }

}
