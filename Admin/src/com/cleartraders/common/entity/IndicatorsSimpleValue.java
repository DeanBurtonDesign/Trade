package com.cleartraders.common.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IndicatorsSimpleValue implements Serializable 
{    
    private static final long serialVersionUID = 6492920469950264601L;

    private IndicatorBean indicatorBean = new IndicatorBean();
    
    private List<IndicatorStringValue> indicatorValues = new ArrayList<IndicatorStringValue>();
    
    public IndicatorBean getIndicatorBean()
    {
        return indicatorBean;
    }
    public void setIndicatorBean(IndicatorBean indicatorBean)
    {
        this.indicatorBean = indicatorBean;
    }
    public List<IndicatorStringValue> getIndicatorValues()
    {
        return indicatorValues;
    }
    public void setIndicatorValues(List<IndicatorStringValue> indicatorValues)
    {
        this.indicatorValues = indicatorValues;
    }
}
