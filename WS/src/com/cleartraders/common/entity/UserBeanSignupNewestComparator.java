package com.cleartraders.common.entity;

import java.util.Comparator;

public class UserBeanSignupNewestComparator implements Comparator<UserBean>
{

    public int compare(UserBean arg0, UserBean arg1)
    {
        if(arg0.getReg_date() < arg1.getReg_date())
            return 1;
           else
            return 0;
    }

}
