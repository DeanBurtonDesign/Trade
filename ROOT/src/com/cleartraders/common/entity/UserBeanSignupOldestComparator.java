package com.cleartraders.common.entity;

import java.util.Comparator;


public class UserBeanSignupOldestComparator implements Comparator<UserBean>
{

    public int compare(UserBean o1, UserBean o2)
    {
        if(o1.getReg_date() > o2.getReg_date())
            return 1;
           else
            return 0;
    }

}
