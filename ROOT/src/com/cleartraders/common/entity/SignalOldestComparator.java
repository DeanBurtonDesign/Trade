package com.cleartraders.common.entity;

import java.util.Comparator;

public class SignalOldestComparator implements Comparator<Signal>
{

    public int compare(Signal o1, Signal o2)
    {
        if(o1.getGenerate_date() > o2.getGenerate_date())
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

}
