package com.cleartraders.webadmin.model.strategy;

import java.util.ArrayList;
import java.util.List;

import com.cleartraders.common.entity.StrategyBean;
import com.cleartraders.webadmin.db.DBAccessor;

public class StrategyController
{
    public StrategyBean getStrategyByID(long strategyID)
    {
        return DBAccessor.getInstance().getStrategyFullInfoByID(strategyID);
    }
    
    public List<StrategyBean> getAllStrategyBaseInfo()
    {
        return DBAccessor.getInstance().getAllStrategyBaseInfo();
    }
    
    public boolean addStrategy(StrategyBean strategyBean)
    {
        if(null == strategyBean)
            return false;
        
        List<Long> marketIDs = parseMarketIDs(strategyBean.getRelated_markets());
        List<Long> timeFrames = parseTimeFrames(strategyBean.getRelated_timeframes());
        List<Long> productPlans = parseProductPlans(strategyBean.getRelated_product_plans());
        
        return DBAccessor.getInstance().addStrategy(strategyBean, marketIDs, timeFrames, productPlans);
    }
    

    public boolean updateStrategy(StrategyBean strategyBean)
    {
        if(null == strategyBean)
            return false;
        
        List<Long> marketIDs = parseMarketIDs(strategyBean.getRelated_markets());
        List<Long> timeFrames = parseTimeFrames(strategyBean.getRelated_timeframes());
        List<Long> productPlans = parseProductPlans(strategyBean.getRelated_product_plans());
        
        return DBAccessor.getInstance().updateStrategy(strategyBean, marketIDs, timeFrames, productPlans);
    }
    
    private List<Long> parseMarketIDs(String marketIDString)
    {
        List<Long> marketIDs = new ArrayList<Long>();
        String[] subscribledMarkets = marketIDString.split(",");
        
        for(int i=0; i<subscribledMarkets.length; i++)
        {
            if(subscribledMarkets[i].trim().length() == 0)
            {
                continue;
            }
            
            long marketID = Long.parseLong(subscribledMarkets[i]);
            
            marketIDs.add(marketID);
        }
        
        return marketIDs;
    }
    
    private List<Long> parseTimeFrames(String timeFrameString)
    {
        List<Long> timeFrameIDs = new ArrayList<Long>();
        String[] subscribledTimeFrames = timeFrameString.split(",");
        
        for(int i=0; i<subscribledTimeFrames.length; i++)
        {
            if(subscribledTimeFrames[i].trim().length() == 0)
            {
                continue;
            }
            
            long marketID = Long.parseLong(subscribledTimeFrames[i]);
            
            timeFrameIDs.add(marketID);
        }
        
        return timeFrameIDs;
    }
    
    private List<Long> parseProductPlans(String productPlanString)
    {
        List<Long> productPlanIDs = new ArrayList<Long>();
        String[] subscribledProductPlans = productPlanString.split(",");
        
        for(int i=0; i<subscribledProductPlans.length; i++)
        {
            if(subscribledProductPlans[i].trim().length() == 0)
            {
                continue;
            }
            
            long marketID = Long.parseLong(subscribledProductPlans[i]);
            
            productPlanIDs.add(marketID);
        }
        
        return productPlanIDs;
    }
    
}
