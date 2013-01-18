package com.cleartraders.common.entity;

import java.util.Comparator;

public class StrategyComparator implements Comparator<StrategyBean>
{
    public int compare(StrategyBean o1, StrategyBean o2)
    {
        String p1=o1.getCommon_name();
        String p2=o2.getCommon_name();
        
        if(p1.compareTo(p2) > 0)
         return 1;
        else
         return 0;
    }

}
