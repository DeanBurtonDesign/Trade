package com.cleartraders.common.entity;

import java.util.Comparator;

import com.cleartraders.common.entity.MarketTypeBean;

public class MarketTypeBeanComparator implements Comparator<MarketTypeBean>
{
    public int compare(MarketTypeBean o1, MarketTypeBean o2)
    {
        String p1=o1.getDisplay_name();
        String p2=o2.getDisplay_name();
        
        if(p1.compareTo(p2) > 0)
         return 1;
        else
         return 0;
    }
}
