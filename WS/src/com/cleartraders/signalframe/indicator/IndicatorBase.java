package com.cleartraders.signalframe.indicator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cleartraders.common.entity.HLOCDataBean;
import com.cleartraders.common.entity.IndicatorValueBean;
import com.cleartraders.common.entity.IndicatorsSimpleValue;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.signalframe.Utils;
import com.cleartraders.signalframe.dataengine.IQuoteEngine;
import com.cleartraders.signalframe.dataengine.IQuoteFeeder;
import com.cleartraders.signalframe.dataengine.QuoteEngineManager;

public abstract class IndicatorBase implements IIndicator,IQuoteFeeder
{
    private List<HLOCDataBean> _dataSource=null;
    private String _indicatorName="";
    private long _indicatorID = 0;
    private List<IIndicatorListener> _indicatorListeners=new ArrayList<IIndicatorListener>();
    private IQuoteEngine _relateQuoteEngine=null;
    private long _period=0L;
    private long _skip=0L;
    private Color _color=null;
    private boolean _isRealtime = false;
    
    public abstract void onQuoteUpdate(boolean isNewQuote, String quoteEngineID, HLOCDataBean quote);
    public abstract void initIndicator();
    public abstract void calculateIndicator();
    public abstract ArrayList<IndicatorsSimpleValue> getIndicatorsValue(long strategyID);
    public abstract IndicatorValueBean getIndicatorValue(String indicatorName, long date);
    
    
    public IndicatorBase(List<HLOCDataBean> dataSource,long indicatorID, String indicatorName, long period, long skip, boolean isRealtime)
    {
        if (dataSource == null)
        {
            throw new IllegalArgumentException("Null source (XYDataset).");
        }

        if (period < 1)
        {
            throw new IllegalArgumentException("period must be positive.");

        }

        if (skip < 0)
        {
            throw new IllegalArgumentException("skip must be >= 0.0.");

        }
                
        _indicatorName = indicatorName;
        _dataSource = dataSource;
        _period = period;
        _skip = skip;
        _isRealtime = isRealtime;
        _indicatorID = indicatorID;
        
        _color = Color.black;
    }
    
    protected void init()
    {
        initIndicator();
        registerQuoteEngine();
    }
    
    private void registerQuoteEngine()
    {
        if(isRealtime())
        {
            long marketID = Utils.getMarketIDFromIndicatorName(getIndicatorName());
            long periodID = Utils.getPeriodIDFromIndicatorName(getIndicatorName());
            
            _relateQuoteEngine = QuoteEngineManager.getInstance().getQuoteEngine(marketID, periodID);
            
            if(null != _relateQuoteEngine)
            {
                if(_relateQuoteEngine.addIndicatorQuoteFeeder(getIndicatorName(), this))
                {
                    InfoTrace.getInstance().printInfo(DebugLevel.INFO," Indicator: "+this.getIndicatorName()+" register quote engine success!");
                }
                else
                {
                    InfoTrace.getInstance().printInfo(DebugLevel.ERROR," Indicator: "+this.getIndicatorName()+" register quote engine failed!");
                }
            }
        }
    }
    
    protected boolean notifyIndicatorToJMS(long indicatorID, IndicatorValueBean indicatorValue)
    {
        if (null == indicatorValue)
        {
            return false;
        }
        
        boolean result = false;
        
        long marketID = 0;
        long periodID = 0;
        
        if(null == getIndicatorName() || getIndicatorName().split(":").length != 3)
        {
            return false;
        }
        else
        {
            marketID = Long.parseLong(getIndicatorName().split(":")[1]);
            periodID = Long.parseLong(getIndicatorName().split(":")[2]);
        }
                
        try
        {
            indicatorValue.setMarketID(marketID);
            indicatorValue.setPeriodID(periodID);
            indicatorValue.setIndicatorID(indicatorID);
            
            String messageContent = indicatorValue.toTextMessage();
            
            _relateQuoteEngine.sendMessage(messageContent);          
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,"Exception happened in IndicatorBase.notifyIndicatorToJMS()! Exception details=> "+ CommonTools.getExceptionDescribe(e));
        }
        
        return result;
    }
    
    public void notifyIndicator(Map<String, Double> indicatorsValue)
    {
        if(null == getIndicatorListeners())
            return;
        
        for(int i=0; i<getIndicatorListeners().size(); i++)
        {
            IIndicatorListener oIndicatorListener = getIndicatorListeners().get(i);
            
            oIndicatorListener.notifyIndicatorValue(indicatorsValue, this);
        }
    }
    
    public void addListener(IIndicatorListener indicatorListener)
    {
        if(null == indicatorListener || null == _indicatorListeners)
            return;
        
        _indicatorListeners.add(indicatorListener);
    }
    
    protected List<IIndicatorListener> getIndicatorListeners()
    {
        return _indicatorListeners;
    }
    
    protected void setIndicatorListeners(List<IIndicatorListener> listeners)
    {
        _indicatorListeners = listeners;
    }
    
    public String getIndicatorName()
    {
        return _indicatorName;
    }
    
    public void setIndicatorName(String indicatorName)
    {
        _indicatorName = indicatorName;
    }
    
    public Color getColor()
    {
        return _color;
    }

    public void setColor(Color color)
    {
        this._color = color;
    }

    public List<HLOCDataBean> getDataSource()
    {
        return _dataSource;
    }

    public void setDataSource(List<HLOCDataBean> source)
    {
        _dataSource = source;
    }

    public long getPeriod()
    {
        return _period;
    }

    public void setPeriod(long period)
    {
        this._period = period;
    }

    public long getSkip()
    {
        return _skip;
    }

    public void setSkip(long skip)
    {
        this._skip = skip;
    }
        
    public boolean isRealtime()
    {
        return _isRealtime;
    }
    public void setRealtime(boolean realtime)
    {
        _isRealtime = realtime;
    }
    public long getIndicatorID()
    {
        return _indicatorID;
    }    
    public void setIndicatorID(long _indicatorid)
    {
        _indicatorID = _indicatorid;
    }
}
