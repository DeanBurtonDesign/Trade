package com.cleartraders.signalframe.indicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.HLOCDataBean;
import com.cleartraders.common.entity.IndicatorValueBean;
import com.cleartraders.common.entity.IndicatorsSimpleValue;

public class TopBottomTurnIndicator extends IndicatorBase
{
    private List<IndicatorValueBean> _indicatorValueSeries = new ArrayList<IndicatorValueBean>();
    private int _currentIndex=-1;
    private String _valueType=CommonDefine.CLOSE_PRICE;
    
    public TopBottomTurnIndicator(List<HLOCDataBean> dataSource, long indicatorID, String indicatorName, long period, long skip, boolean isRealtime)
    {
        super(dataSource, indicatorID, indicatorName, period, skip, isRealtime);
        
        init();
    }

    @Override
    public void initIndicator()
    {
        _currentIndex=0;
        
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
        if( _currentIndex < getSkip() || _currentIndex < 0 || (_currentIndex+1) > getDataSource().size())
        {
            return;
        }

        long quoteDate = getDataSource().get(_currentIndex).getDate();

        double quoteValue = getDataSource().get(_currentIndex).getClose();
        
        if(CommonDefine.LOW_PRICE.endsWith(_valueType))
        {
            quoteValue = getDataSource().get(_currentIndex).getLow();
        }
        else if(CommonDefine.HIGH_PRICE.endsWith(_valueType))
        {
            quoteValue = getDataSource().get(_currentIndex).getHigh();
        }
        else if(CommonDefine.OPEN_PRICE.endsWith(_valueType))
        {
            quoteValue = getDataSource().get(_currentIndex).getOpen();
        } 
        
        _indicatorValueSeries.add(new IndicatorValueBean(quoteDate, quoteValue));
    }

    @Override
    public void onQuoteUpdate(boolean isNewQuote, String quoteEngineID, HLOCDataBean quote)
    {
        if(isNewQuote)
        {
            //InfoTrace.getInstance().printInfo(DebugLevel.INFO," Indicator: "+this.getIndicatorName()+" got new quote=> "+quote.quoteToString());
            
            //to prevent out of memory, system should delete first item
            _indicatorValueSeries.remove(0);
            
            calculateIndicator();
        }
        else
        {
            _indicatorValueSeries.remove(_indicatorValueSeries.size()-1);
            
            calculateIndicator();
        }
        
        double currentPriceValue = _indicatorValueSeries.get(_indicatorValueSeries.size()-1).getYValue();
        
        Map<String, Double> indicatorsValue = new HashMap<String, Double>();
        indicatorsValue.put(this.getIndicatorName(), currentPriceValue);
        indicatorsValue.put("date", Double.valueOf(quote.getDate()));
                
        notifyIndicator(indicatorsValue);
    }

    @Override
    public ArrayList<IndicatorsSimpleValue> getIndicatorsValue(long strategy_id)
    {
        return new ArrayList<IndicatorsSimpleValue>();
    }

    @Override
    public IndicatorValueBean getIndicatorValue(String indicatorName, long date)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
