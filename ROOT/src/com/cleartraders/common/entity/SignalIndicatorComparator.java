package com.cleartraders.common.entity;

import java.util.Comparator;

public class SignalIndicatorComparator implements Comparator<Signal>
{

    public int compare(Signal o1, Signal o2)
    {
        String p1=o1.getMarket_name();
        String p2=o2.getMarket_name();
        
        if(p1.compareTo(p2) > 0)
         return 1;
        else
         return 0;
    }

}
