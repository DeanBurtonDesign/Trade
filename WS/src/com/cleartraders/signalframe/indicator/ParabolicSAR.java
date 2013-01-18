package com.cleartraders.signalframe.indicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cleartraders.common.entity.HLOCDataBean;
import com.cleartraders.common.entity.IndicatorBean;
import com.cleartraders.common.entity.IndicatorStringValue;
import com.cleartraders.common.entity.IndicatorValueBean;
import com.cleartraders.common.entity.IndicatorsSimpleValue;
import com.cleartraders.signalframe.db.SFDBAccessor;

public class ParabolicSAR extends IndicatorBase
{
    private List<IndicatorValueBean> _SARValueSeries = null; 
    private int _currentIndex=-1;
    public final static String SAR_INDICATOR_NAME="SAR";
    public final static int SAR_PERIOD = 6;
    
    public static final double DEFAULT_INITIAL = 0.02;
    public static final double DEFAULT_ADD = 0.02;
    public static final double DEFAULT_LIMIT = 0.2;
    private double _acceleration = DEFAULT_INITIAL;
    private double _accelerationStep = DEFAULT_ADD;
    private double _accelerationMax = DEFAULT_LIMIT;
    
    private double _firstHighValue = 0.0;
    private double _firstLowValue = 0.0;
    private double _secondHighValue = 0.0;
    private double _secondLowValue = 0.0;
    private boolean _isFall=false;
    private double _xp = 0;
    private double _sar = 0;
    private double _psar = 0;
    private double _af = 0;
    private double _t = 0;
    private double _t2 = 0;
    private boolean _bFirstUseAF=false;
    
    private double _firstHighValuePrevious = 0.0;
    private double _firstLowValuePrevious = 0.0;
    private double _secondHighValuePrevious = 0.0;
    private double _secondLowValuePrevious = 0.0;
    private boolean _isFallPrevious=false;
    private double _xpPrevious = 0;
    private double _sarPrevious = 0;
    private double _psarPrevious = 0;
    private double _afPrevious = 0;
    private double _tPrevious = 0;
    private double _t2Previous = 0;
    private boolean _bFirstUseAFPrevious=false;
    
    public ParabolicSAR(List<HLOCDataBean> dataSource, long indicatorID, String indicatorName, long period, long skip, boolean isRealtime)
    {
        super(dataSource, indicatorID, indicatorName, period, skip, isRealtime);

        init();
    }

    @Override
    public void initIndicator()
    {
        _SARValueSeries = new ArrayList<IndicatorValueBean>(); 
        
        _currentIndex=0;
        
        if(getDataSource().size() < getPeriod())
        {
            return;
        }
        
        preCalculateIndicator();
        
        //calculate SAR
        int itemCount = getDataSource().size();
        for(int i=0; i<itemCount; i++)
        {
            calculateIndicator();
            
            _currentIndex++;
        }
        
        _currentIndex--;
    }
    
    public void preCalculateIndicator()
    {                
        double firstHighestValue = getDataSource().get(_currentIndex).getHigh();
        double firstLowestValue = getDataSource().get(_currentIndex).getLow();
        
        for(int i=1; i<getPeriod(); i++)
        {
            double tempHigh = getDataSource().get(i).getHigh();
            double tempLow = getDataSource().get(i).getLow();
            
            if(tempHigh > firstHighestValue)
            {
                firstHighestValue = tempHigh;
            }
            
            if(tempLow < firstLowestValue)
            {
                firstLowestValue = tempLow;
            }
        }             
        
        //先选定period(天)时间判断为上涨或下跌，若是看涨，则第一天的SAR值必须是近期内的最低价；若是看跌，则第一天的SAR须是近期的最高价。
        _firstHighValue = getDataSource().get((int)getPeriod()-2).getHigh();
        _firstHighValue = getDataSource().get((int)getPeriod()-2).getLow();

        //获取Period当天的最高和最低值
        _secondHighValue = getDataSource().get((int)getPeriod()-1).getHigh();
        _secondLowValue = getDataSource().get((int)getPeriod()-1).getLow();

        _isFall = false;
        _xp = 0;//极点价
        _sar = 0;
        _psar = 0;
        _af = _acceleration;
        _t = _firstHighValue;
        _t2 = _secondHighValue;
        
        //如果第二天的高点大于前一天的高点
        if (_t2 > _t)
        {
            //行情看涨
            
            // we are long
            _isFall = false;
            
            _xp = firstHighestValue;

            //如果是看涨的行情，则SAR（0）为近期底部最低价
            _sar = firstLowestValue;
            _psar = firstLowestValue;
        }
        else
        {
            //行情看跌
            
            // we are short
            _isFall = true;
            
            _xp = firstLowestValue;

            //如果是看跌行情，则SAR（0）为近期顶部的最高价。
            _sar = firstHighestValue;
            _psar = firstHighestValue;
        }

        //get the current data item...
        long x = getDataSource().get((int)getPeriod()-1).getDate();
        if(_SARValueSeries.size() == 0)
        {
            _SARValueSeries.add(new IndicatorValueBean(x, _sar));
        }
        
        _bFirstUseAF=true;
    }
    
    @Override
    public void calculateIndicator()
    {
        if( _currentIndex < 0 || getDataSource().size() <= 0 || (_currentIndex+1) > getDataSource().size())
        {
            return;
        }
        
        if(_currentIndex < getSkip() )
        {
            long x = getDataSource().get(_currentIndex).getDate();
            
            _SARValueSeries.add(new IndicatorValueBean(x, 0));
            
            return;
        }

        _firstHighValuePrevious = _firstHighValue;
        _firstLowValuePrevious = _firstLowValue;
        _secondHighValuePrevious = _secondHighValue;
        _secondLowValuePrevious = _secondLowValue;
        _isFallPrevious=_isFall;
        _xpPrevious = _xp;
        _sarPrevious = _sar;
        _psarPrevious = _psar;
        _afPrevious = _af;
        _tPrevious = _t;
        _t2Previous = _t2;
        _bFirstUseAFPrevious=_bFirstUseAF;
        
        _secondHighValue = getDataSource().get(_currentIndex).getHigh();
        _secondLowValue = getDataSource().get(_currentIndex).getLow();
        
        _firstHighValue = getDataSource().get(_currentIndex-1).getHigh();
        _firstLowValue = getDataSource().get(_currentIndex-1).getLow();
        
        //get the current data item...
        long x = getDataSource().get(_currentIndex).getDate();

        //当前处理下跌
        if (_isFall)
        {
            //检测是否有反转
            _t = _secondHighValue;
            
            //在下跌时,如果当天的最高值突破昨天的SAR,表示有反转
            if (_t >= _sar)
            {
                //如果有反转,那么,SAR等于反转前的XP值
                _sar = _xp;
                _psar = _xp;
                _xp = _t;
                _isFall = false;
                
                //加速因子设置为初始值
                _af = _acceleration;
                _bFirstUseAF = true;
            }
            else
            {                 
                //没有反转时,如果创新低,则加速因递增.没有创新低则保持不变
                if (_secondLowValue < _xp)
                {
                    _xp = _secondLowValue;
                    
                    if(!_bFirstUseAF)
                    {
                        _af = _af + _accelerationStep;
                        
                        //加速因子大于限制值时,取限制值
                        if (_af > _accelerationMax)
                            _af = _accelerationMax;
                    }
                    
                    _bFirstUseAF=false;
                }

                //计算初始的当天SAR
                _t = _psar + (_af * (_xp - _psar));   
                
                _t2 = _secondHighValue;
                
                //如果当天的最高值大于 当天的SAR时
                if (_t < _t2)
                {
                    double t3 = _firstHighValue;
                    
                    //和昨天的最高值对比, 当天SAR等于两者当中更大的一方
                    if (t3 > _t2)
                        _t = t3;
                    else
                        _t = _t2;
                }
                
                _sar = _t;
                _psar = _sar;
            }
        }
        else
        {
            // we are long
            // check for a switch
            _t = _secondLowValue;
            if (_t <= _sar)
            {
                _sar = _xp;
                _psar = _xp;
                _xp = _t;
                _isFall = true;
                _af = _acceleration;
                _bFirstUseAF = true;
            }
            else
            {                                    
                if (_secondHighValue > _xp)
                {
                    _xp = _secondHighValue;
                    
                    if(!_bFirstUseAF)
                    {
                        _af = _af + _accelerationStep;
                        
                        //加速因子大于限制值时,取限制值
                        if (_af > _accelerationMax)
                            _af = _accelerationMax;
                    }
                    _bFirstUseAF=false;
                }
                
                _t = _psar + (_af * (_xp - _psar));
                
                _t2 = _secondLowValue;
                if (_t > _t2)
                {
                    double t3 = _firstLowValue;
                    if (t3 < _t2)
                        _t = t3;
                    else
                        _t = _t2;
                }
                
                _sar = _t;
                _psar = _sar;
            }
        }

        _SARValueSeries.add(new IndicatorValueBean(x, _sar));
    }

    @Override
    public IndicatorValueBean getIndicatorValue(String indicatorName, long date)
    {
        if(null == _SARValueSeries)
            return new IndicatorValueBean(0,0);
        
        for(int i=_SARValueSeries.size()-1; i>=0; i--)
        {
            IndicatorValueBean oIndicatorValue = _SARValueSeries.get(i);
            
            if(oIndicatorValue.getDateValue() == date)
            {
                return oIndicatorValue;
            }
        }
        
        return new IndicatorValueBean(0,0);
    }
    
    public IndicatorValueBean getLatestIndicatorValue()
    {
        if(null == _SARValueSeries)
            return new IndicatorValueBean(0,0);
        
        return _SARValueSeries.get(_SARValueSeries.size()-1);
    }

    @Override
    public ArrayList<IndicatorsSimpleValue> getIndicatorsValue(long strategyID)
    {
        ArrayList<IndicatorsSimpleValue> oIndicatorsValue = new ArrayList<IndicatorsSimpleValue>();
        
        IndicatorsSimpleValue oIndicatorValue = new IndicatorsSimpleValue();
        
        IndicatorBean oIndicator = SFDBAccessor.getInstance().getIndicatorByID(strategyID, this.getIndicatorID());        
        oIndicatorValue.setIndicatorBean(oIndicator);
        
        for(int i=0; i<_SARValueSeries.size(); i++)
        {
            oIndicatorValue.getIndicatorValues().add(new IndicatorStringValue(_SARValueSeries.get(i).toSimpleValueString()));
        }        
        
        oIndicatorsValue.add(oIndicatorValue);
        
        return oIndicatorsValue;
    }

    @Override
    public void onQuoteUpdate(boolean isNewQuote, String quoteEngineID, HLOCDataBean quote)
    {
        if(isNewQuote)
        {
            //to prevent out of memory, system should delete first item
            _SARValueSeries.remove(0);
            
            calculateIndicator();
            
            //InfoTrace.getInstance().printInfo(DebugLevel.INFO,this.getIndicatorName()+" Update, Date:"+getDataSource().get(_currentIndex).getDate()+", Close:"+getDataSource().get(_currentIndex).getClose()+", Date:"+_SMAValueSeries.get(_currentIndex).getDateValue()+", SMA"+this.getPeriod()+":"+_SMAValueSeries.get(_currentIndex).getYValue());
        }
        else
        {
            _SARValueSeries.remove(_SARValueSeries.size()-1);
            
            _firstHighValue = _firstHighValuePrevious;
            _firstLowValue = _firstLowValuePrevious;
            _secondHighValue = _secondHighValuePrevious;
            _secondLowValue = _secondLowValuePrevious;
            _isFall = _isFallPrevious;
            _xp = _xpPrevious;
            _sar = _sarPrevious;
            _psar = _psarPrevious;
            _af = _afPrevious;
            _t = _tPrevious;
            _t2 = _t2Previous;
            _bFirstUseAF = _bFirstUseAFPrevious;
            
            calculateIndicator();
        }
        
        double currentSARValue = _SARValueSeries.get(_currentIndex).getYValue();
        
        Map<String, Double> indicatorsValue = new HashMap<String, Double>();
        indicatorsValue.put(this.getIndicatorName(), currentSARValue);
        indicatorsValue.put("date", Double.valueOf(quote.getDate()));
        
        notifyIndicator(indicatorsValue);        
        
        if(isRealtime())
        {
            //notify to JMS
            notifyIndicatorToJMS(this.getIndicatorID(), new IndicatorValueBean(quote.getDate(), currentSARValue));
        }
    }
}
