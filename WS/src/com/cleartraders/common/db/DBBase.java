package com.cleartraders.common.db;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.util.config.CommonResManager;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.tools.CommonTools;

public class DBBase
{    
    public Connection getConnection()
    {
        try
        {
            if(CommonDefine.IS_UNIT)
            {
                //directly get DB connection for Unit Test
                String userName = CommonResManager.getInstance().getDb_user_name();
                String pwd = CommonResManager.getInstance().getDb_password();
                String url = CommonResManager.getInstance().getDb_url()+"&user="+userName+"&password="+pwd;
                String driverName = CommonResManager.getInstance().getDb_driver_class_name();
                
                Class.forName(driverName);
                return DriverManager.getConnection(url);
            }
            else
            {
                Context context = new InitialContext();
                String jini_url = CommonResManager.getInstance().getDb_jini_name();
                DataSource ds = (DataSource) context.lookup(jini_url);
    
                return ds.getConnection();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        
        return null;
    }
}
