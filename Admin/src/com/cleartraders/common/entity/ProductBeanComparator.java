package com.cleartraders.common.entity;

import java.util.Comparator;

public class ProductBeanComparator implements Comparator<ProductBean>
{
    public int compare(ProductBean o1, ProductBean o2)
    {
        long p1=o1.getId();
        long p2=o2.getId();
        
        if(p1 > p2)
         return 1;
        else
         return 0;
    }
}
