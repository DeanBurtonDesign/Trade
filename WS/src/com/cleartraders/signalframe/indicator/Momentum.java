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

public class Momentum extends IndicatorBase
{
    private List<IndicatorValueBean> _MomentumValueSeries = null; 
    private int _currentIndex=-1;
    public final static String MOMENTUM_INDICATOR_NAME="Momentum";
        
    public Momentum(List<HLOCDataBean> dataSource, long indicatorID, String indicatorName, long period, long skip, boolean isRealtime)
    {
        super(dataSource, indicatorID, indicatorName, period, skip, isRealtime);
        
        init();
    }

    @Override
    public void initIndicator()
    {
        _MomentumValueSeries = new ArrayList<IndicatorValueBean>(); 
        
        _currentIndex=0;
        
        //calculate Momentum
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
        if(  _currentIndex < 0 || getDataSource().size() <= 0 || (_currentIndex+1) > getDataSource().size())
        {
            return;
        }

        //获取X坐示的日期
        long x = getDataSource().get(_currentIndex).getDate();
        double currentClose = getDataSource().get(_currentIndex).getClose();
        
        if(_currentIndex == 0)
        {
            _MomentumValueSeries.add(new IndicatorValueBean(x, 0));
            
            return;
        }
        
        double compareCloseValue = 0;
        
        if(_currentIndex < getPeriod())
        {
            compareCloseValue = getDataSource().get(_currentIndex).getClose();
        }
        else
        {
            
            compareCloseValue = getDataSource().get((int)(_currentIndex-getPeriod())).getClose();
        }
        
        _MomentumValueSeries.add(new IndicatorValueBean(x, currentClose - compareCloseValue));
    }

    @Override
    public IndicatorValueBean getIndicatorValue(String indicatorName, long date)
    {
        if(null == _MomentumValueSeries)
            return new IndicatorValueBean(0,0);
        
        for(int i=_MomentumValueSeries.size()-1; i>=0; i--)
        {
            IndicatorValueBean oIndicatorValue = _MomentumValueSeries.get(i);
            
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
        
        for(int i=0; i<_MomentumValueSeries.size(); i++)
        {
            oIndicatorValue.getIndicatorValues().add(new IndicatorStringValue(_MomentumValueSeries.get(i).toSimpleValueString()));
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
            _MomentumValueSeries.remove(0);
            
            calculateIndicator();
            
            //InfoTrace.getInstance().printInfo(DebugLevel.INFO,this.getIndicatorName()+" Update, Date:"+getDataSource().get(_currentIndex).getDate()+", Close:"+getDataSource().get(_currentIndex).getClose()+", Date:"+_SMAValueSeries.get(_currentIndex).getDateValue()+", SMA"+this.getPeriod()+":"+_SMAValueSeries.get(_currentIndex).getYValue());
        }
        else
        {
            _MomentumValueSeries.remove(_MomentumValueSeries.size()-1);
            
            calculateIndicator();
        }
        
        double currentMonmentumValue = _MomentumValueSeries.get(_currentIndex).getYValue();
        
        Map<String, Double> indicatorsValue = new HashMap<String, Double>();
        indicatorsValue.put(this.getIndicatorName(), currentMonmentumValue);
        indicatorsValue.put("date", Double.valueOf(quote.getDate()));
        
        notifyIndicator(indicatorsValue);        
        
        if(isRealtime())
        {
            //notify to JMS
            notifyIndicatorToJMS(this.getIndicatorID(), new IndicatorValueBean(quote.getDate(), currentMonmentumValue));
        }
    }

}
