package com.cleartraders.common.entity;

import java.util.Comparator;

public class TimeFrameComparator implements Comparator<MarketPeriodBean>
{

    public int compare(MarketPeriodBean arg0, MarketPeriodBean arg1)
    {
        long p1=arg0.getId();
        long p2=arg1.getId();
        
        if(p1 > p2)
         return 1;
        else
         return 0;
    }

}
