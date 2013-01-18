package com.cleartraders.signalframe.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.cleartraders.common.db.CommonDBAccessor;
import com.cleartraders.common.entity.IndicatorBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;

public class SFDBAccessor extends CommonDBAccessor
{
    private static SFDBAccessor m_oInstance = new SFDBAccessor();

    public SFDBAccessor()
    {
    }

    public synchronized static SFDBAccessor getInstance()
    {
        return m_oInstance;
    }
        
    
    public IndicatorBean getIndicatorByID(long strategyID, long indicatorID)
    {
        IndicatorBean oResult = null;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;

        try
        {
            conn = getConnection();
            stat = conn.createStatement();
            result = stat.executeQuery("select A.id,A.name,A.period,A.color, A.description, A.type ,B.strategy_id,B.visable from indicator_table A, strategy_indicator_table B where B.indicator_id = "+ indicatorID+" and A.id=B.indicator_id and B.strategy_id="+strategyID);
            
            if (result.next())
            {
                oResult = new IndicatorBean();
                
                oResult.setId(result.getLong("id"));
                oResult.setStrategy_id(result.getLong("strategy_id"));
                oResult.setName(result.getString("name"));
                oResult.setPeriod(result.getInt("period"));
                oResult.setColor(result.getString("color"));
                oResult.setDescription(result.getString("description"));
                oResult.setVisable(result.getInt("visable"));
                oResult.setType(result.getInt("type"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,
            "Exception happened in SFDBAccessor.getIndicatorByID()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
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
                
                LogTools.getInstance().insertLog(DebugLevel.ERROR,
                        "Exception happened in SFDBAccessor.getIndicatorByID()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
            }
        }

        return oResult;
    }
}
