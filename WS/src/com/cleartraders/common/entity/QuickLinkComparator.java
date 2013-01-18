package com.cleartraders.common.entity;

import java.util.Comparator;

public class QuickLinkComparator implements Comparator<QuickLinkBean>
{

    public int compare(QuickLinkBean o1, QuickLinkBean o2)
    {
        int p1=o1.getIndex();
        int p2=o2.getIndex();
        
        if(p1 > p2)
         return 1;
        else
         return 0;
    }

}
