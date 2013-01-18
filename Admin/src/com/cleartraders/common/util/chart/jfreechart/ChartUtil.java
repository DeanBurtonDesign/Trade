package com.cleartraders.common.util.chart.jfreechart;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeriesCollection;

import com.cleartraders.common.util.chart.jfreechart.common.BaseLineChart;
import com.cleartraders.common.util.chart.jfreechart.common.CustomXYItemLabelGenerator;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;


public class ChartUtil
{
    public static BufferedImage getBarChartImageByData(int width, int height, int imageType, List<TimeSeriesCollection> xyDataSetList,String dateFormatter)
    {
        if(null == xyDataSetList || xyDataSetList.size() < 3)
        {
            return null;
        }
        
        //create a default combined chart
        JFreeChart oJfreechart = createDefaultCombinedChart(dateFormatter);
        oJfreechart.setBackgroundPaint(Color.white);
                
        BaseLineChart oLineChart = new BaseLineChart(xyDataSetList.get(2));
        oLineChart.getPlot().setDataset(1, xyDataSetList.get(0));
        oLineChart.getPlot().setDataset(2, xyDataSetList.get(1));
                
        NumberAxis numberaxis = (NumberAxis)oLineChart.getPlot().getRangeAxis();
        numberaxis.setUpperMargin(0.14999999999999999D);
        numberaxis.setLowerMargin(0.14999999999999999D);
                
        XYItemRenderer xyitemrenderer = oLineChart.getPlot().getRenderer(0);
        xyitemrenderer.setSeriesPaint(0, new Color(36,171,226));
                
        ((CombinedDomainXYPlot)oJfreechart.getXYPlot()).add(oLineChart.getPlot());
        oLineChart.getPlot().setOutlinePaint(new Color(36,171,226));
        oLineChart.getPlot().setOutlineVisible(true);
        
        //for postive profit
        XYBarRenderer xybarrenderer1 = new XYBarRenderer(0.20000000000000001D);
        oLineChart.getPlot().setRenderer(1, xybarrenderer1); 
        //xybarrenderer1.setDrawBarOutline(true);
        xybarrenderer1.setDrawBarOutline(false);
        
        //GradientPaint gradientpaint1 = new GradientPaint(0.0F, 0.0F, Color.green, 0.0F, 0.0F, Color.lightGray);
        GradientPaint gradientpaint1 = new GradientPaint(0.0F, 0.0F, new Color(106,155,2), 0.0F, 0.0F, new Color(116,172,5));
        xybarrenderer1.setSeriesPaint(0,gradientpaint1);
        
        xybarrenderer1.setBaseItemLabelGenerator(new CustomXYItemLabelGenerator());
        xybarrenderer1.setBaseItemLabelsVisible(true);
        xybarrenderer1.setBaseItemLabelPaint(new Color(45,73,0));
        
        //for negtive proift
        XYBarRenderer xybarrenderer2 = new XYBarRenderer(0.20000000000000001D);
        oLineChart.getPlot().setRenderer(2, xybarrenderer2); 
        //xybarrenderer2.setDrawBarOutline(true);
        xybarrenderer2.setDrawBarOutline(false);
        
        //GradientPaint gradientpaint2 = new GradientPaint(0.0F, 0.0F, Color.lightGray,0.0F, 0.0F, Color.red);
        GradientPaint gradientpaint2 = new GradientPaint(0.0F, 0.0F, new Color(168,9,5),0.0F, 0.0F, new Color(165,6,2));
        xybarrenderer2.setSeriesPaint(0,gradientpaint2);
        
        xybarrenderer2.setBaseItemLabelGenerator(new CustomXYItemLabelGenerator());
        xybarrenderer2.setBaseItemLabelsVisible(true);
        xybarrenderer2.setBaseItemLabelPaint(new Color(168,9,5));        
        
        return oJfreechart.createBufferedImage(width, height, imageType, null);
    }
    
    public static BufferedImage getLineChartImageByData(int width, int height, int imageType, List<TimeSeriesCollection> xyDataSetList,String dateFormatter)
    {
        if(null ==xyDataSetList || 0 == xyDataSetList.size())
        {
            return null;
        }
        
        //create a default combined chart
        JFreeChart oJfreechart = createDefaultCombinedChart(dateFormatter);
        oJfreechart.setBackgroundPaint(Color.white);
        
        BaseLineChart oLineChart = new BaseLineChart(xyDataSetList.get(2));
        
        oLineChart.getPlot().setDataset(1, xyDataSetList.get(0));
        oLineChart.getPlot().setDataset(2, xyDataSetList.get(1));
        
        ((CombinedDomainXYPlot)oJfreechart.getXYPlot()).add(oLineChart.getPlot());
        oLineChart.getPlot().setOutlinePaint(new Color(36,171,226));
        oLineChart.getPlot().setOutlineVisible(true);
        //oLineChart.getPlot().setBackgroundPaint(Color.black);
        //oLineChart.getPlot().setOutlinePaint(Color.white);
        
        NumberAxis numberaxis = (NumberAxis)oLineChart.getPlot().getRangeAxis();
        numberaxis.setUpperMargin(0.14999999999999999D);
        numberaxis.setLowerMargin(0.14999999999999999D);
        
        XYItemRenderer xyitemrenderer = oLineChart.getPlot().getRenderer();
        xyitemrenderer.setSeriesPaint(0, new Color(36,171,226));
                
        XYLineAndShapeRenderer xylineandshaperenderer1 = new XYLineAndShapeRenderer(false, true);
        oLineChart.getPlot().setRenderer(1, xylineandshaperenderer1);
        xylineandshaperenderer1.setBaseShapesVisible(true);        
        xylineandshaperenderer1.setBaseShapesFilled(true);
        //xylineandshaperenderer1.setSeriesFillPaint(0, new GradientPaint(0.0F, 1.0F, Color.green, 0.0F, 1.0F, Color.lightGray));
        //xylineandshaperenderer1.setSeriesPaint(0, new GradientPaint(0.0F, 1.0F, Color.green, 0.0F, 1.0F, Color.lightGray));
        
        xylineandshaperenderer1.setSeriesFillPaint(0, new GradientPaint(0.0F, 1.0F, new Color(106,155,2), 0.0F, 1.0F, new Color(116,172,5)));
        xylineandshaperenderer1.setSeriesPaint(0, new GradientPaint(0.0F, 1.0F, new Color(106,155,2), 0.0F, 1.0F, new Color(116,172,5)));
        xylineandshaperenderer1.setUseFillPaint(true);
        xylineandshaperenderer1.setSeriesShape(0,new java.awt.geom.Ellipse2D.Double(-4D, -4D, 8D, 8D));
        
        XYLineAndShapeRenderer xylineandshaperenderer2 = new XYLineAndShapeRenderer(false, true);
        oLineChart.getPlot().setRenderer(2, xylineandshaperenderer2);
        xylineandshaperenderer2.setBaseShapesVisible(true);        
        xylineandshaperenderer2.setBaseShapesFilled(true); 
        //xylineandshaperenderer2.setSeriesFillPaint(0, new GradientPaint(0.0F, 0.0F, Color.red, 0.0F, 0.0F, Color.lightGray));
        //xylineandshaperenderer2.setSeriesPaint(0, new GradientPaint(0.0F, 0.0F, Color.red, 0.0F, 0.0F, Color.lightGray));
        
        xylineandshaperenderer2.setSeriesFillPaint(0, new GradientPaint(0.0F, 0.0F, new Color(168,9,5), 0.0F, 0.0F, new Color(165,6,2)));
        xylineandshaperenderer2.setSeriesPaint(0, new GradientPaint(0.0F, 0.0F, new Color(168,9,5), 0.0F, 0.0F, new Color(165,6,2)));
        
        xylineandshaperenderer2.setUseFillPaint(true);
        xylineandshaperenderer2.setSeriesShape(0,new java.awt.geom.Ellipse2D.Double(-4D, -4D, 8D, 8D));
        
        
        
//        XYLineAndShapeRenderer xylineandshaperenderer3 = new XYLineAndShapeRenderer(true, false);
//        oLineChart.getPlot().setRenderer(2, xylineandshaperenderer3);
//        xylineandshaperenderer3.setBaseShapesVisible(true);        
//        xylineandshaperenderer3.setBaseShapesFilled(true);
//        //xylineandshaperenderer3.setSeriesFillPaint(0, Color.blue);
//        xylineandshaperenderer3.setSeriesPaint(0, Color.blue);
//        xylineandshaperenderer3.setUseFillPaint(true);
                
        return oJfreechart.createBufferedImage(width, height, imageType, null);
    }
    
    private static JFreeChart createDefaultCombinedChart(String dateFormatter)
    {        
        ValueAxis timeAxis = new DateAxis("");
//        ((DateAxis)timeAxis).setTimeline(SegmentedTimeline.newMondayThroughFridayTimeline());
//        ((DateAxis)timeAxis).setDateFormatOverride(new SimpleDateFormat("dd-MM-yyyy HH:mm"));
        ((DateAxis)timeAxis).setDateFormatOverride(new SimpleDateFormat(dateFormatter));
        ((DateAxis)timeAxis).setTickMarkPosition(DateTickMarkPosition.MIDDLE);
        
        // Don't set time zone, since it has been changed before
        //((DateAxis)timeAxis).setTimeZone(chartTimeZone);
        
        CombinedDomainXYPlot combineddomainxyplot = new CombinedDomainXYPlot(timeAxis);
        combineddomainxyplot.setGap(4D);
        combineddomainxyplot.setOrientation(PlotOrientation.VERTICAL);
        
        return new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, combineddomainxyplot, false);
    }
    
    public synchronized static void createImage(OutputStream out,BufferedImage bi)
    {
        Graphics2D g = bi.createGraphics();

        // set background:
        g.setBackground(Color.white);
        g.drawImage(bi, new AffineTransform(1f, 0f, 0f, 1f, 0, 0), null);
        g.dispose();
        bi.flush();

        // encode:
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
        param.setQuality(1.0f, false);
        encoder.setJPEGEncodeParam(param);

        try
        {
            encoder.encode(bi);
            
            bi.flush();
            out.flush();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}
