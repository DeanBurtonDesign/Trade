package com.cleartraders.signalframe.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.HLOCDataBean;
import com.cleartraders.common.entity.IndicatorValueBean;
import com.cleartraders.common.entity.MarketPeriodBean;
import com.cleartraders.common.entity.MarketTypeBean;
import com.cleartraders.common.entity.Signal;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.signalframe.Utils;
import com.cleartraders.signalframe.db.SFDBAccessor;
import com.cleartraders.signalframe.indicator.IndicatorBase;
import com.cleartraders.signalframe.indicator.ParabolicSAR;
import com.cleartraders.ws.db.WSDBAccessor;

public class StrategyParabolicSAR extends StrategyBase
{
    private Map<String, ParabolicSAR> _SARIndicators = new HashMap<String, ParabolicSAR>();
    private Map<String, Signal> _latestSignal = new HashMap<String, Signal>();
    
    
    public StrategyParabolicSAR(long strategyID, String strategySystemName, boolean isRealtime)
    {
        super(strategyID, strategySystemName, isRealtime);
        
        initIndicators();
        
        registerQuoteEngine();
    }

    @Override
    public void initIndicators()
    {
        List<MarketTypeBean> oAllMarketBean = DataCache.getInstance().getAllMarketType();
        List<MarketPeriodBean> oAllMarketPeriod = DataCache.getInstance().getAllMarketPeriod();
        
        if(null == oAllMarketBean || null == oAllMarketPeriod)
            return;
        
        long SARIndicatorID = DataCache.getInstance().getIndicatorByNameAndPeriod(ParabolicSAR.SAR_INDICATOR_NAME, ParabolicSAR.SAR_PERIOD).getId();

        for(int i=0; i<oAllMarketBean.size(); i++)
        {
            MarketTypeBean oMarketBean = oAllMarketBean.get(i);
            
            //for(int j=0; j<1; j++)
            for(int j=0; j<oAllMarketPeriod.size(); j++)
            {
                MarketPeriodBean oMarketPeriodBean = oAllMarketPeriod.get(j);
                
                Signal oLatestSignal = SFDBAccessor.getInstance().getLatestSignal(oMarketBean.getId(), this.getStrategyID(), oMarketPeriodBean.getId());
                String signalKey = oMarketBean.getId()+":"+oMarketPeriodBean.getId();
                if(null != oLatestSignal) 
                    _latestSignal.put(signalKey, oLatestSignal);
                
                //Note: Indicator Name must be composed by Name:MarketID:PeriodID
                //SAR
                String SARIndicatorName = Utils.generateIndicatorName(ParabolicSAR.SAR_INDICATOR_NAME+ParabolicSAR.SAR_PERIOD, oMarketBean.getId(), oMarketPeriodBean.getId());
                ParabolicSAR oSAR = new ParabolicSAR(DataCache.getInstance().getHLOCData(oMarketBean.getId(), oMarketPeriodBean.getId()),SARIndicatorID,SARIndicatorName,ParabolicSAR.SAR_PERIOD,ParabolicSAR.SAR_PERIOD-1,true);
                _SARIndicators.put(SARIndicatorName, oSAR);
                
            }
        }
    }

    @Override
    public void notifyIndicatorValue(Map<String, Double> currentIndicatorValue, IndicatorBase oIndicator)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onQuoteUpdate(boolean isNewQuote, String quoteEngineID, HLOCDataBean quote)
    {
        if(null == quoteEngineID || null == quote)
            return;
        
        if(quoteEngineID.split(":").length != 2)
            return;
        
        if(null == _SARIndicators )
            return;
        
        long marketID = Long.parseLong(quoteEngineID.split(":")[0]);
        long periodID = Long.parseLong(quoteEngineID.split(":")[1]);
        
        String SARIndicatorName = Utils.generateIndicatorName(ParabolicSAR.SAR_INDICATOR_NAME+ParabolicSAR.SAR_PERIOD, marketID, periodID);
        ParabolicSAR oSAR = _SARIndicators.get(SARIndicatorName);
        
        if(null == oSAR)
            return;
        
        IndicatorValueBean SARIndicatorValue = oSAR.getIndicatorValue("", quote.getDate());
        
        String signalKey = marketID+":"+periodID;
        
        if(null == _latestSignal.get(signalKey))
        {
            Signal latestLongShortCloseSignal = WSDBAccessor.getInstance().getLatestSignal(this.getStrategyID(), marketID, (int)periodID);
            if(null != latestLongShortCloseSignal && latestLongShortCloseSignal.getId() > 0)
            {            
                _latestSignal.put(signalKey, latestLongShortCloseSignal);
            }
            else
            {
                _latestSignal.put(signalKey, new Signal());
            }
        }
        
        //Long signal
        //When price crosses SAR from below give long signal.
        if(SARIndicatorValue.getYValue() < quote.getClose())
        {
            //if latest signal is NULL or ( signal isn't null and it is not Long_signal)
            if( null == _latestSignal.get(signalKey) || 
                ( null != _latestSignal.get(signalKey) 
                  && _latestSignal.get(signalKey).getSignal_type() != CommonDefine.LONG_SIGNAL
                  && _latestSignal.get(signalKey).getGenerate_date() != quote.getDate())
              )
            {
                InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Strategy "+this.getStrategySystemName()+": generate Long signal!");
                LogTools.getInstance().insertLog(DebugLevel.INFO,"Strategy "+this.getStrategySystemName()+": generate Long signal!");
                                
                Signal oLongSignal = createSignal(quote.getDate(), CommonDefine.LONG_SIGNAL, marketID, periodID,quote.getClose());                
                _latestSignal.put(signalKey, oLongSignal);                
                handleSignal(oLongSignal);
            }
            
            return ;
        }
        
        
        //Short signal
        //When price crosses SAR from above give short signal
        if(SARIndicatorValue.getYValue() > quote.getClose())
        {
            //if latest signal is NULL or ( signal isn't null and it is not Short Signal)
            if( null == _latestSignal.get(signalKey) || 
                ( null != _latestSignal.get(signalKey) 
                  && _latestSignal.get(signalKey).getSignal_type() != CommonDefine.SHORT_SIGNAL
                  && _latestSignal.get(signalKey).getGenerate_date() != quote.getDate())
              )
            {
                InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Strategy "+this.getStrategySystemName()+": generate Short signal!");
                LogTools.getInstance().insertLog(DebugLevel.INFO,"Strategy "+this.getStrategySystemName()+": generate Short signal!");
                                
                Signal oShortSignal = createSignal(quote.getDate(), CommonDefine.SHORT_SIGNAL, marketID, periodID,quote.getClose());                
                _latestSignal.put(signalKey, oShortSignal);                
                handleSignal(oShortSignal);
            }
            
            return;
        }
        
    }

}
