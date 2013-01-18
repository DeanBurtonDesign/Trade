package com.cleartraders.common.util.chart.jfreechart.common;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;

public abstract class ChartAdapter implements Chart
{
    private  JFreeChart m_oJfreechart=null;
    
    public void setJFreeChart(JFreeChart oJfreechart)
    {
        m_oJfreechart = oJfreechart;
    }
    
    public void initialize()
    {
        if(m_oJfreechart != null)
        {
            getPlot().setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);            
            getPlot().setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
            getPlot().setRangeGridlinesVisible(true);
            getPlot().setDomainGridlinesVisible(true);
            getPlot().setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);            
            getPlot().setOrientation(PlotOrientation.VERTICAL);
        }
    }
    
    public XYPlot getPlot()
    {
        return m_oJfreechart.getXYPlot();
    }

}
