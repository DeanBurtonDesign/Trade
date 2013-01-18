package com.cleartraders.webadmin.model.login;

import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webadmin.db.DBAccessor;

public class AdminLoginController
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
    
    public boolean userIsAdmin(String userName)
    {
        UserBean userBean = getUserBeanByName(userName);
        
        return userBean.getMemberType() == CommonDefine.ADMIN_MEMBER_TYPE;
    }
    
    public boolean userExist(String userName)
    {
        UserBean userBean = getUserBeanByName(userName);
        
        return userBean != null;
    }
    
    public boolean userWasEnabled(String userName)
    {
        UserBean userBean = getUserBeanByName(userName);
        
        if(userBean == null)
        {
            return false;
        }
        
        return userBean.getEnable() == CommonDefine.USER_ENABLED;
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
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        
        return checkResult;
    }
    
   
    public boolean updateUserLastLogin(UserBean oUserBean, long lastLogin)
    {
        if(null == oUserBean)
        {
            return false;
        }
        
        return DBAccessor.getInstance().updateUserLastLogin(oUserBean, lastLogin);
    }
}
