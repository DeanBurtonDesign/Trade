package com.cleartraders.common.util.chart.jfreechart.common;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.time.TimeSeriesCollection;


public class BaseBarChart extends ChartAdapter
{
    public BaseBarChart(TimeSeriesCollection dataset)
    {
        JFreeChart m_oJfreechart = ChartFactory.createTimeSeriesChart("", "", "",dataset, false, false, false);
        setJFreeChart(m_oJfreechart);
        initialize();        
        XYPlot xyplot = m_oJfreechart.getXYPlot();  
        
        // 设置Price具体参数
        NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
        numberaxis.setLowerMargin(0.40000000000000002D);
        //numberaxis.setNumberFormatOverride(new DecimalFormat("0.000E0"));

        XYBarRenderer xybarrenderer = new XYBarRenderer(0.20000000000000001D);
        xybarrenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{1} "+"Volume"+"={2}", new SimpleDateFormat("dd/MM/yyyy"),new DecimalFormat("0.00")));
        xybarrenderer.setMargin(0.1);
        xyplot.setRenderer(xybarrenderer);  
        xyplot.setDomainAxis(null);
        
    }
}
