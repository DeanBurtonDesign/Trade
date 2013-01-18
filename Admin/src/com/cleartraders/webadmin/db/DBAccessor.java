package com.cleartraders.webadmin.db;

import com.cleartraders.common.db.CommonDBAccessor;


public class DBAccessor extends CommonDBAccessor
{
    private static DBAccessor m_oInstance = new DBAccessor();

    public DBAccessor()
    {
    }

    public synchronized static DBAccessor getInstance()
    {
        return m_oInstance;
    }
}
