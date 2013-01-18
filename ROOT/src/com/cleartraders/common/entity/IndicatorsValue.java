package com.cleartraders.common.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IndicatorsValue implements Serializable 
{
    private static final long serialVersionUID = 7193817167516557905L;

    private IndicatorBean indicatorBean = new IndicatorBean();
    
    private List<IndicatorValueBean> indicatorValues = new ArrayList<IndicatorValueBean>();
    
    public IndicatorBean getIndicatorBean()
    {
        return indicatorBean;
    }
    public void setIndicatorBean(IndicatorBean indicatorBean)
    {
        this.indicatorBean = indicatorBean;
    }
    public List<IndicatorValueBean> getIndicatorValues()
    {
        return indicatorValues;
    }
    public void setIndicatorValues(List<IndicatorValueBean> indicatorValues)
    {
        this.indicatorValues = indicatorValues;
    }
}
