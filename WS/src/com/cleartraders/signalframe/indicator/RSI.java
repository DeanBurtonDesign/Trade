package com.cleartraders.signalframe.indicator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cleartraders.common.entity.HLOCDataBean;
import com.cleartraders.common.entity.IndicatorBean;
import com.cleartraders.common.entity.IndicatorStringValue;
import com.cleartraders.common.entity.IndicatorValueBean;
import com.cleartraders.common.entity.IndicatorsSimpleValue;
import com.cleartraders.signalframe.Utils;
import com.cleartraders.signalframe.db.SFDBAccessor;

public class RSI extends IndicatorBase
{
    private List<IndicatorValueBean> _RSIValueSeries = null; 
    private int _currentIndex=-1;
    public final static String RSI_INDICATOR_NAME="RSI";
    public final static String RSI_LOW="RSI Low";
    public final static String RSI_HIGH="RSI High";
    public double LOW_VALUE=30.0;
    public double HIGH_VALUE=70.0;
    
    private Map<Long,Double> _previousUpAvgSet = new HashMap<Long,Double>();
    private Map<Long,Double> _previousDownAvgSet = new HashMap<Long,Double>();
        
    private long _highIndicatorID=0;
    private long _lowIndicatorID=0;
    
    private List<Double> _upValue = new ArrayList<Double>();
    private List<Double> _downValue = new ArrayList<Double>();
    
    private DecimalFormat decimalFormat = new DecimalFormat();
    
    public RSI(List<HLOCDataBean> dataSource, long indicatorID, String indicatorName, long period, long skip, boolean isRealtime)
    {
        super(dataSource, indicatorID, indicatorName, period, skip, isRealtime);
        
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
                
        init();
    }
    
    @Override
    public void initIndicator()
    {
        _RSIValueSeries = new ArrayList<IndicatorValueBean>(); 
        
        _currentIndex=0;
        
        //calculate SMA
        int itemCount = getDataSource().size();
        for(int i=0; i<itemCount; i++)
        {
            calculateIndicator();
            
            _currentIndex++;
        }
        
        _currentIndex--;
    }

    
    
    
    @Override
    public void calculateIndicator()
    {
        //注意: RSI是第一天开始计算,所以,以下不用跳过SKIP, 
        //但是skip之前的不添加到结果(series)当中
        if(  _currentIndex < 0 || getDataSource().size() <= 0 || (_currentIndex+1) > getDataSource().size())
        {
            return;
        }
        
        //获取X坐示的日期
        long x = getDataSource().get(_currentIndex).getDate();
        
        //如果是第一天,up,down的平均值都等close
        if(_currentIndex == 0)
        {
            //特别注意:RSI的upavg和downavg是和两日的价格差相关的,由
            //于这是第一天,所有,取零为初始值(不是取当日的价格)
            _upValue.add(Double.valueOf(0));
            _downValue.add(Double.valueOf(0));
            
            _RSIValueSeries.add(new IndicatorValueBean(x, 0));
            
            _previousUpAvgSet.put(Long.valueOf(x), Double.valueOf(0));
            _previousDownAvgSet.put(Long.valueOf(x), Double.valueOf(0));
            
            return;
        }
        
        double RSI = 0.0;
        double currClose = getDataSource().get(_currentIndex).getClose();   
        double previousClose = getDataSource().get(_currentIndex-1).getClose(); 
        
        double currUp = 0.0;
        double currDown = 0.0;
        
        if(currClose > previousClose)
        {
            currUp = currClose - previousClose;
            currDown = 0.0;
        }
        else
        {
            currUp = 0.0;
            currDown = previousClose - currClose;
        }
        
        if(_currentIndex+1 < getPeriod())
        {
            _upValue.add(Double.valueOf(currUp));
            _downValue.add(Double.valueOf(currDown));
            
            _RSIValueSeries.add(new IndicatorValueBean(x, 0));
            
            _previousUpAvgSet.put(Long.valueOf(x), Double.valueOf(SMA(_upValue)));
            _previousDownAvgSet.put(Long.valueOf(x), Double.valueOf(SMA(_downValue)));
            
            return;
        }
                
        double currentUpAvg = 0;
        double currentDownAvg = 0;
        
        if((_currentIndex + 1) == getPeriod())
        {
            currentUpAvg = SMA(_upValue);
            currentDownAvg = SMA(_downValue);
            
            _previousUpAvgSet.put(Long.valueOf(x), Double.valueOf(currentUpAvg));
            _previousDownAvgSet.put(Long.valueOf(x), Double.valueOf(currentDownAvg));
        }
        else
        {
            long previousItemTime = getDataSource().get(_currentIndex-1).getDate();
            
            double previousUpAvg = _previousUpAvgSet.get(Long.valueOf(previousItemTime));
            double previousDownAvg = _previousDownAvgSet.get(Long.valueOf(previousItemTime));
            
            currentUpAvg = (previousUpAvg*(getPeriod()-1)+currUp)/getPeriod();
            currentDownAvg =(previousDownAvg*(getPeriod()-1)+currDown)/getPeriod();
            
            _previousUpAvgSet.put(Long.valueOf(x), Double.valueOf(currentUpAvg));
            _previousDownAvgSet.put(Long.valueOf(x), Double.valueOf(currentDownAvg));
        }
                
        
        
        //计算RS, formula: double rs    = avgUp[0] / (avgDown[0] == 0 ? 1 : avgDown[0]);
        double rs = currentUpAvg / (currentDownAvg == 0 ? 1 : currentDownAvg);
        
        //计算RSI, formula: double rsi      = 100 - (100 / (1 + rs));
        RSI = 100 - (100 / (1+rs));
                        
        //skip之前的不添加到结果(series)当中
        if(_currentIndex < getSkip())
        {
            return;
        }
        
        double cutRSI = Double.valueOf(decimalFormat.format(RSI));
               
        _RSIValueSeries.add(new IndicatorValueBean(x, cutRSI));
    }
    
    private double SMA(List<Double> originalValue)
    {
        if(null == originalValue)
            return 0;
        
        double sum = 0;
        
        for(int i=0; i<originalValue.size(); i++)
        {
            sum += originalValue.get(i);
        }
        
        return sum/originalValue.size();
    }
    
    @Override
    public void onQuoteUpdate(boolean isNewQuote, String quoteEngineID, HLOCDataBean quote)
    {                
        if(isNewQuote)
        {
            //to prevent out of memory, system should delete first item
            _RSIValueSeries.remove(0);
            
            calculateIndicator();
            
            long x = getDataSource().get(0).getDate();
            
            //remove previous UP and Down avg data
            _previousUpAvgSet.remove(Long.valueOf(x));
            _previousDownAvgSet.remove(Long.valueOf(x));
        }
        else
        {
            _RSIValueSeries.remove(_RSIValueSeries.size()-1);
            
            calculateIndicator();
        }
        
        double currentRSIValue = _RSIValueSeries.get(_currentIndex).getYValue();
        
        Map<String, Double> indicatorsValue = new HashMap<String, Double>();
        indicatorsValue.put(this.getIndicatorName(), currentRSIValue);
        indicatorsValue.put("date", Double.valueOf(quote.getDate()));
        
        notifyIndicator(indicatorsValue);        
        
        if(isRealtime())
        {
            //notify to JMS
            notifyIndicatorToJMS(this.getIndicatorID(), new IndicatorValueBean(quote.getDate(), currentRSIValue));
            
            //for High
            notifyIndicatorToJMS(this.getHighIndicatorID(), new IndicatorValueBean(quote.getDate(), this.HIGH_VALUE));
            
            //for low
            notifyIndicatorToJMS(this.getLowIndicatorID(), new IndicatorValueBean(quote.getDate(), this.LOW_VALUE));
        }
    }

    @Override
    public IndicatorValueBean getIndicatorValue(String indicatorName, long date)
    {
        if(null == _RSIValueSeries)
            return new IndicatorValueBean(0,0);
        
        for(int i=_RSIValueSeries.size()-1; i>=0; i--)
        {
            IndicatorValueBean oIndicatorValue = _RSIValueSeries.get(i);
            
            if(oIndicatorValue.getDateValue() == date)
            {
                return oIndicatorValue;
            }
        }
        
        return new IndicatorValueBean(0,0);
    }
    
    public IndicatorValueBean getPreviousIndicatorValue(String indicatorName, long date)
    {
        if(null == _RSIValueSeries || indicatorName == null)
            return new IndicatorValueBean(0,0);
        
        if(indicatorName.split(":").length != 3)
        {
            return new IndicatorValueBean(0,0);
        }
        int marketPeriod = Integer.parseInt(indicatorName.split(":")[2]);
        
        long previousItemDate = date - Utils.getMinutesByPeriodType(marketPeriod)*60*1000;
        
        for(int i=_RSIValueSeries.size()-1; i>=0; i--)
        {
            IndicatorValueBean oIndicatorValue = _RSIValueSeries.get(i);
            
            if(oIndicatorValue.getDateValue() == previousItemDate || oIndicatorValue.getDateValue() < previousItemDate)
            {
                return oIndicatorValue;
            }
        }
        
        return new IndicatorValueBean(0,0);
    }

    @Override
    public ArrayList<IndicatorsSimpleValue> getIndicatorsValue(long strategy_id)
    {
        ArrayList<IndicatorsSimpleValue> oIndicatorsValue = new ArrayList<IndicatorsSimpleValue>();
        
        IndicatorsSimpleValue oIndicatorValue = new IndicatorsSimpleValue();
        
        IndicatorBean oIndicator = SFDBAccessor.getInstance().getIndicatorByID(strategy_id,this.getIndicatorID());        
        oIndicatorValue.setIndicatorBean(oIndicator);
        
        for(int i=0; i<_RSIValueSeries.size(); i++)
        {
            oIndicatorValue.getIndicatorValues().add(new IndicatorStringValue(_RSIValueSeries.get(i).toSimpleValueString()));
        }        
        
        oIndicatorsValue.add(oIndicatorValue);
        
        return oIndicatorsValue;
    }
    
    public ArrayList<IndicatorsSimpleValue> getRSILowValue(long strategy_id)
    {
        ArrayList<IndicatorsSimpleValue> oIndicatorsValue = new ArrayList<IndicatorsSimpleValue>();
        
        IndicatorsSimpleValue oIndicatorValue = new IndicatorsSimpleValue();
        
        IndicatorBean oIndicator = SFDBAccessor.getInstance().getIndicatorByID(strategy_id,this.getIndicatorID());        
        oIndicatorValue.setIndicatorBean(oIndicator);
        
        for(int i=0; i<_RSIValueSeries.size(); i++)
        {
            IndicatorValueBean oLowValue = new IndicatorValueBean(_RSIValueSeries.get(i).getDateValue(), this.LOW_VALUE);
            oIndicatorValue.getIndicatorValues().add(new IndicatorStringValue(oLowValue.toSimpleValueString()));
        }        
        
        oIndicatorsValue.add(oIndicatorValue);
        
        return oIndicatorsValue;
    }
    
    public ArrayList<IndicatorsSimpleValue> getRSIHighValue(long strategy_id)
    {
        ArrayList<IndicatorsSimpleValue> oIndicatorsValue = new ArrayList<IndicatorsSimpleValue>();
        
        IndicatorsSimpleValue oIndicatorValue = new IndicatorsSimpleValue();
        
        IndicatorBean oIndicator = SFDBAccessor.getInstance().getIndicatorByID(strategy_id,this.getIndicatorID());        
        oIndicatorValue.setIndicatorBean(oIndicator);
        
        for(int i=0; i<_RSIValueSeries.size(); i++)
        {
            IndicatorValueBean oLowValue = new IndicatorValueBean(_RSIValueSeries.get(i).getDateValue(), this.HIGH_VALUE);
            oIndicatorValue.getIndicatorValues().add(new IndicatorStringValue(oLowValue.toSimpleValueString()));
        }        
        
        oIndicatorsValue.add(oIndicatorValue);
        
        return oIndicatorsValue;
    }

    public long getHighIndicatorID()
    {
        return _highIndicatorID;
    }

    public void setHighIndicatorID(long indicatorID)
    {
        _highIndicatorID = indicatorID;
    }

    public long getLowIndicatorID()
    {
        return _lowIndicatorID;
    }

    public void setLowIndicatorID(long indicatorID)
    {
        _lowIndicatorID = indicatorID;
    }
}
