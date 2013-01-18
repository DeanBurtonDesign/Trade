package com.cleartraders.signalframe.indicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.HLOCDataBean;
import com.cleartraders.common.entity.IndicatorBean;
import com.cleartraders.common.entity.IndicatorStringValue;
import com.cleartraders.common.entity.IndicatorValueBean;
import com.cleartraders.common.entity.IndicatorsSimpleValue;
import com.cleartraders.signalframe.db.SFDBAccessor;


public class SMA extends IndicatorBase
{
    private List<IndicatorValueBean> _SMAValueSeries = null; 
    private String _valueType=CommonDefine.CLOSE_PRICE;
    private int _currentIndex=-1;
    public final static String SMA_INDICATOR_NAME="SMA";
    
    public SMA(List<HLOCDataBean> dataSource,long indicatorID, String indicatorName, long period, long skip, boolean isRealtime)
    {
        super(dataSource,indicatorID, indicatorName,period,skip,isRealtime);
        
        init();
    }

    @Override
    public IndicatorValueBean getIndicatorValue(String indicatorName, long date)
    {
        if(null == _SMAValueSeries)
            return new IndicatorValueBean(0,0);
        
        for(int i=_SMAValueSeries.size()-1; i>=0; i--)
        {
            IndicatorValueBean oIndicatorValue = _SMAValueSeries.get(i);
            
            if(oIndicatorValue.getDateValue() == date)
            {
                return oIndicatorValue;
            }
        }
        
        return new IndicatorValueBean(0,0);
    }
    
    @Override
    public ArrayList<IndicatorsSimpleValue> getIndicatorsValue(long strategyID)
    {
        ArrayList<IndicatorsSimpleValue> oIndicatorsValue = new ArrayList<IndicatorsSimpleValue>();
        
        IndicatorsSimpleValue oIndicatorValue = new IndicatorsSimpleValue();
        
        IndicatorBean oIndicator = SFDBAccessor.getInstance().getIndicatorByID(strategyID, this.getIndicatorID());        
        oIndicatorValue.setIndicatorBean(oIndicator);
        
        for(int i=0; i<_SMAValueSeries.size(); i++)
        {
            oIndicatorValue.getIndicatorValues().add(new IndicatorStringValue(_SMAValueSeries.get(i).toSimpleValueString()));
        }        
        
        oIndicatorsValue.add(oIndicatorValue);
        
        return oIndicatorsValue;
    }

    @Override
    public void calculateIndicator()
    {
        if( _currentIndex < getSkip() || 
            _currentIndex < 0 || 
            getDataSource().size() <= 0 || 
            (_currentIndex+1) > getDataSource().size())
        {
            return;
        }        

        long x = getDataSource().get(_currentIndex).getDate();

        int n = 0;
        double sum = 0.0;
        int offset = 0;
        boolean finished = false;

        while (!finished)
        {
            if ((_currentIndex - offset) >= 0 && getPeriod() > offset)
            {
                Number yValue = getDataSource().get(_currentIndex - offset).getClose();
                
                if(CommonDefine.LOW_PRICE.endsWith(_valueType))
                {
                    yValue = getDataSource().get(_currentIndex - offset).getLow();
                }
                else if(CommonDefine.HIGH_PRICE.endsWith(_valueType))
                {
                    yValue = getDataSource().get(_currentIndex - offset).getHigh();
                }
                else if(CommonDefine.OPEN_PRICE.endsWith(_valueType))
                {
                    yValue = getDataSource().get(_currentIndex - offset).getOpen();
                }                            

                if (yValue != null)
                {
                    sum = sum + yValue.doubleValue();
                    n = n + 1;
                }
            }
            else
            {
                finished = true;
            }

            offset++;
        }

        if (n > 0)
        {
            _SMAValueSeries.add(new IndicatorValueBean(x, sum / n));
        }
        else
        {
            _SMAValueSeries.add(new IndicatorValueBean(x, 0));
        }
    }

    @Override
    public void initIndicator()
    {
        _SMAValueSeries = new ArrayList<IndicatorValueBean>(); 
        
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
    public void onQuoteUpdate(boolean isNewQuote, String quoteEngineID, HLOCDataBean quote)
    {
        if(isNewQuote)
        {
            //to prevent out of memory, system should delete first item
            _SMAValueSeries.remove(0);
            
            calculateIndicator();
            
            //InfoTrace.getInstance().printInfo(DebugLevel.INFO,this.getIndicatorName()+" Update, Date:"+getDataSource().get(_currentIndex).getDate()+", Close:"+getDataSource().get(_currentIndex).getClose()+", Date:"+_SMAValueSeries.get(_currentIndex).getDateValue()+", SMA"+this.getPeriod()+":"+_SMAValueSeries.get(_currentIndex).getYValue());
        }
        else
        {
            _SMAValueSeries.remove(_SMAValueSeries.size()-1);
            
            calculateIndicator();
        }
        
        double currentSMAValue = _SMAValueSeries.get(_currentIndex).getYValue();
        
        Map<String, Double> indicatorsValue = new HashMap<String, Double>();
        indicatorsValue.put(this.getIndicatorName(), currentSMAValue);
        indicatorsValue.put("date", Double.valueOf(quote.getDate()));
        
        notifyIndicator(indicatorsValue);        
        
        if(isRealtime())
        {
            //notify to JMS
            notifyIndicatorToJMS(this.getIndicatorID(), new IndicatorValueBean(quote.getDate(), currentSMAValue));
        }
    }
    
}
