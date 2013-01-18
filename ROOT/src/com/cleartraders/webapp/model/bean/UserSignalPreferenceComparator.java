package com.cleartraders.webapp.model.bean;

import java.util.Comparator;

public class UserSignalPreferenceComparator implements Comparator<UserSignalPreferenceBean>
{

    public int compare(UserSignalPreferenceBean o1, UserSignalPreferenceBean o2)
    {
        String p1=o1.getMarketName();
        String p2=o2.getMarketName();
        
        if(p1.compareTo(p2) > 0)
         return 1;
        else
         return 0;
    }

}
