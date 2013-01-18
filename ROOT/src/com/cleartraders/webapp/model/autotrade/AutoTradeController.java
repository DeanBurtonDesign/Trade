package com.cleartraders.webapp.model.autotrade;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.net.URLConnection;

import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.config.WebappResManager;
import com.cleartraders.webapp.db.DBAccessor;
import com.cleartraders.webapp.model.bean.AutoTradeInfoBean;

public class AutoTradeController
{
    public AutoTradeInfoBean getAutoTradeInfo(UserBean oUserBean)
    {
        return DBAccessor.getInstance().getAutoTradeInfo();
    }
    
    public boolean changeAutoTradeStatus(UserBean oUser, int iOperationType)
    {
        if(oUser.getId() != WebConstants.AUTO_TRADE_USER_ID)
        {
            LogTools.getInstance().insertLog(DebugLevel.WARNING, "User: "+oUser.getLogin_name()+" who is not auto trader user, is trying to change auto trade flag!");
            
            return false;
        }
        
        String autoTradeControlURL = WebappResManager.getInstance().getAuto_trade_control_url();
        
        autoTradeControlURL+="username="+oUser.getLogin_name()+"&";
        autoTradeControlURL+="password="+oUser.getPwd()+"&";
        autoTradeControlURL+="operationtype="+iOperationType;
        
        try
        {
            URL oURL = new URL(autoTradeControlURL);
            URLConnection oConnect = oURL.openConnection();
            InputStream in = oConnect.getInputStream();  
            ObjectInputStream objStream = new ObjectInputStream(in);
                        
            String tempResult = (String)objStream.readObject();
            
            return "true".equals(tempResult);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            
            return false;
        }
    }
}
