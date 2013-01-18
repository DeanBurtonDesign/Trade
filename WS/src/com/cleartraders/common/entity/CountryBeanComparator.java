package com.cleartraders.common.entity;

import java.util.Comparator;

public class CountryBeanComparator implements Comparator<CountryBean>
{

    public int compare(CountryBean arg0, CountryBean arg1)
    {
        String p1=arg0.getName();
        String p2=arg1.getName();
        
        if(p1.compareTo(p2) > 0)
            return 1;
        else
            return 0;
    }

}
