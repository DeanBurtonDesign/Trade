package com.cleartraders.common.entity;

import java.util.Comparator;

public class SmsPackageBeanComparator implements Comparator<SmsPackageBean>
{

    public int compare(SmsPackageBean o1, SmsPackageBean o2)
    {
        if(o1.getSms_included() > o2.getSms_included())
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

}
