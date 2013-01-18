package com.cleartraders.webapp.model.bean;

import java.util.Comparator;

public class DateStringComparator implements Comparator<String>
{

    public int compare(String arg0, String arg1)
    {
        if(arg0.compareTo(arg1) > 0)
            return 1;
           else
            return 0;
    }

}
