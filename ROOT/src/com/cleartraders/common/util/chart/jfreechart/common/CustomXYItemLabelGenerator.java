package com.cleartraders.common.util.chart.jfreechart.common;

import java.io.Serializable;
import java.text.MessageFormat;

import org.jfree.chart.labels.AbstractXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.util.PublicCloneable;

public class CustomXYItemLabelGenerator extends AbstractXYItemLabelGenerator
                                        implements XYItemLabelGenerator, 
                                        Cloneable, 
                                        PublicCloneable,
                                        Serializable 
{
    private static final long serialVersionUID = -8036183431172396978L;
    public static final String DEFAULT_ITEM_LABEL_FORMAT = "{2}";
    
    public String generateLabel(XYDataset dataset, int series, int item)
    {
        String result = null;    
        Object[] items = createItemArray(dataset, series, item);
        result = MessageFormat.format(DEFAULT_ITEM_LABEL_FORMAT, items);
        
        if(items.length >= 3)
        {
            if("0".equals(items[2]))
            {
                result = "";
            }
            else
            {
                double value = Double.parseDouble(String.valueOf(items[2]).replaceAll(",", ""));
                if(value>0)
                {
                    result = "+"+String.valueOf(value);
                }
            }
        }
        
        return result;
    }

}
