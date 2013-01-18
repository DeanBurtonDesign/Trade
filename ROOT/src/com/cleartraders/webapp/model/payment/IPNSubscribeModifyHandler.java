package com.cleartraders.webapp.model.payment;

import java.util.HashMap;

import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;

public class IPNSubscribeModifyHandler implements IPNHandler
{

    public boolean handle(HashMap<String, String> allParameters)
    {
        String userLoginName = allParameters.get("custom");
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO, "Receive Subscription Modify event from "+userLoginName);
        LogTools.getInstance().insertLog(DebugLevel.INFO, "Receive Subscription Modify event from "+userLoginName);
        
        return true;
    }
    
    public void sendEmail(UserBean oUserBean)
    {
    }

}
