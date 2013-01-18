package com.cleartraders.webapp.model.payment;

import java.util.HashMap;

import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;

public class IPNSubscribeDefaultHandler implements IPNHandler
{
    public boolean handle(HashMap<String, String> allParameters)
    {
        InfoTrace.getInstance().printInfo(DebugLevel.WARNING, "Receive one default IPN event!");
        LogTools.getInstance().insertLog(DebugLevel.WARNING, "Receive one default IPN event!");
        
        return false;
    }
    
    public void sendEmail(UserBean oUserBean)
    {  
    }
}
