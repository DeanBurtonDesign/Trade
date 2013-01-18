package com.cleartraders.webapp.model.login;

import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.db.DBAccessor;

public class LoginController
{    
    public UserBean getUserBeanByName(String userName)
    {
        if(null == userName || "".equals(userName))
        {
            return null;
        }
        
        //get password from db by username
        return DBAccessor.getInstance().getUserInfoByLoginName(userName);     
    }
    
    public boolean userIsNotExpired(String userName)
    {
        UserBean userBean = getUserBeanByName(userName);
        
        if(userBean == null)
        {
            return false;
        }
        
        return userBean.getExpired_date() > System.currentTimeMillis();
    }
    
    public boolean userExist(String userName)
    {
        UserBean userBean = getUserBeanByName(userName);
        
        return userBean != null;
    }
    
    public boolean userWasVerified(String userName)
    {
        UserBean userBean = getUserBeanByName(userName);
        
        if(userBean == null)
        {
            return false;
        }
        
        return userBean.getEnable() == CommonDefine.USER_ENABLED;
    }
    
    public boolean updateUserLastLogin(UserBean oUserBean, long lastLogin)
    {
        if(null == oUserBean)
        {
            return false;
        }
        
        return DBAccessor.getInstance().updateUserLastLogin(oUserBean, lastLogin);
    }
    
    public boolean loginCheck(String userName, String password)
    {
        if(null == userName || null == password || "".equals(userName) || "".equals(password))
        {
            return false;
        }
                
        //calculate encrypted password
        String enryptedPassword = CommonTools.encryptSHA(password);
        
        boolean checkResult = false;
        
        try
        {
            //get paasowrd from db by username
            UserBean oUserFromDBByUserName = DBAccessor.getInstance().getUserInfoByLoginName(userName);
            
            if(null != oUserFromDBByUserName && oUserFromDBByUserName.getPwd().equals(enryptedPassword))
            {
                checkResult = true;
            }
            else
            {
                checkResult = false;
            }
        }
        catch(Exception e)
        {
            checkResult = false;
            
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        
        return checkResult;
    }    
}
