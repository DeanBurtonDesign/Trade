package com.cleartraders.common.util.chart.jfreechart.common;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.TimeSeriesCollection;


public class BaseLineChart extends ChartAdapter
{
    public BaseLineChart(TimeSeriesCollection dataset)
    {
        JFreeChart m_oJfreechart = ChartFactory.createTimeSeriesChart("", "", "",dataset, false, false, false);
        setJFreeChart(m_oJfreechart);
        initialize();  
        
        XYPlot xyplot = m_oJfreechart.getXYPlot();

        // 设置Price具体参数
        NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
        numberaxis.setLowerMargin(0.40000000000000002D);
        //numberaxis.setNumberFormatOverride(new DecimalFormat("00.00"));

        XYItemRenderer xyitemrenderer = xyplot.getRenderer();
        xyitemrenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{1} "+"Line"+"={2}", new SimpleDateFormat("dd/MM/yyyy"),new DecimalFormat("0.00")));
        
        xyplot.setRenderer(xyitemrenderer);
        xyplot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        xyplot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
    }

}
