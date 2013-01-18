package com.cleartraders.common.entity;

import java.util.Comparator;

public class UserBeanZToAComparator implements Comparator<UserBean>
{

    public int compare(UserBean arg0, UserBean arg1)
    {
        String p1=arg0.getLogin_name();
        String p2=arg1.getLogin_name();
        
        if(p1.compareTo(p2) < 0)
         return 1;
        else
         return 0;
    }

}
