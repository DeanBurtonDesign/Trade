package com.cleartraders.common.entity;

import java.util.Comparator;

public class UserBeanSMSComparator implements Comparator<UserBean>
{

    public int compare(UserBean o1, UserBean o2)
    {
        if(o1.getSms_credits() > o2.getSms_credits())
            return 1;
           else
            return 0;
    }

}
