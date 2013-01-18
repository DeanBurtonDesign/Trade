package com.cleartraders.common.entity;

import java.util.Comparator;

public class AgeComparator implements Comparator<AgeBean>
{

    public int compare(AgeBean o1, AgeBean o2)
    {
        long p1=o1.getId();
        long p2=o2.getId();
        
        if(p1 > p2)
         return 1;
        else
         return 0;
    }

}
