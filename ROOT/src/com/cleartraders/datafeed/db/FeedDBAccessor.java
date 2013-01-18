package com.cleartraders.datafeed.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.cleartraders.common.db.DBBase;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.datafeed.model.bean.MarketBean;

public class FeedDBAccessor extends DBBase
{
    private static FeedDBAccessor m_oInstance = new FeedDBAccessor();

    public FeedDBAccessor()
    {
    }

    public synchronized static FeedDBAccessor getInstance()
    {
        return m_oInstance;
    }

    public ArrayList<MarketBean> getAllEnableMarket()
    {
        ArrayList<MarketBean> oList = new ArrayList<MarketBean>();

        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;
        
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select * from market_type_table where active=1");

            while (result.next())
            {
                MarketBean oMarketBean = new MarketBean();
                
                oMarketBean.setId(result.getLong("id"));
                oMarketBean.setDisplay_name(result.getString("display_name"));
                oMarketBean.setMarket_name(result.getString("market_name"));
                oMarketBean.setActive(result.getInt("active"));
                
                oList.add(oMarketBean);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        finally
        {
            try
            {
                if (null != result)
                {
                    result.close();
                }

                if (null != stat)
                {
                    stat.close();
                }

                if (null != conn)
                {
                    conn.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                
                LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
            }
        }

        return oList;
    }
    
    public static void main(String[] args)
    {
    }
}
