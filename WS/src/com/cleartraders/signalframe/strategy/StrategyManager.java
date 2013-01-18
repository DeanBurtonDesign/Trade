package com.cleartraders.signalframe.strategy;

import java.util.ArrayList;
import java.util.List;

import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.IndicatorBean;
import com.cleartraders.common.entity.IndicatorStringValue;
import com.cleartraders.common.entity.IndicatorsSimpleValue;
import com.cleartraders.common.entity.Signal;
import com.cleartraders.common.entity.StrategyBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.signalframe.db.SFDBAccessor;
import com.cleartraders.signalframe.indicator.ParabolicSAR;
import com.cleartraders.signalframe.indicator.RSI;
import com.cleartraders.signalframe.indicator.SMA;

public class StrategyManager
{
    private List<StrategyBase>  _allStrategy = null;
    private static StrategyManager m_oInstance = new StrategyManager();
    
    private StrategyManager()
    {
        _allStrategy = new ArrayList<StrategyBase>();
        
        init();
    }
    
    public synchronized static StrategyManager getInstance()
    {               
        return m_oInstance;
    }
    
    public void init()
    {
        List<StrategyBean>  _allStrategyBean  = SFDBAccessor.getInstance().getAllStrategyBaseInfo();
        
        //Note: although the in-active strategy should be handled, but won't notify to user        
        for(int i=0; i<_allStrategyBean.size(); i++)
        {
            StrategyBean strategyBean = _allStrategyBean.get(i);
            
            if(CommonDefine.ACTIVE_STRATEGY != strategyBean.getActive())
            {
                continue;
            }
            
            if(strategyBean.getSystem_name().toLowerCase().indexOf("top") == 0)
            {
                //handle this basic BUY or SELL signal through the Top Bottom Turn strategy                
                StrategyBase topBottomTurn = new StrategyTopBottomTurn(strategyBean.getId(), strategyBean.getSystem_name(),true);
                
                _allStrategy.add(topBottomTurn);
            }
            else if(strategyBean.getSystem_name().toLowerCase().indexOf("support") == 0)
            {
                //handle this basic BUY or SELL signal through the Support & Resistance strategy
                StrategyBase supportResistance = new StrategySupportResistance(strategyBean.getId(),strategyBean.getSystem_name(),true);
                
                _allStrategy.add(supportResistance);
            }
            else if(strategyBean.getSystem_name().toLowerCase().indexOf("ma") == 0)
            {
                StrategyBase supportResistance = new StrategyMACrossover(strategyBean.getId(),strategyBean.getSystem_name(),true);
                
                _allStrategy.add(supportResistance);
            }
            else if(strategyBean.getSystem_name().toLowerCase().indexOf("rsi") == 0)
            {
                StrategyBase supportResistance = new StrategyRSIHighLow(strategyBean.getId(),strategyBean.getSystem_name(),true);
                
                _allStrategy.add(supportResistance);
            }
            else if(strategyBean.getSystem_name().toLowerCase().indexOf("sar") == 0)
            {
                StrategyBase SAR = new StrategyParabolicSAR(strategyBean.getId(),strategyBean.getSystem_name(),true);
                
                _allStrategy.add(SAR);
            }
            else if(strategyBean.getSystem_name().toLowerCase().indexOf("switch") == 0)
            {
                StrategyBase switchSAR = new StrategySwitchParabolicSAR(strategyBean.getId(),strategyBean.getSystem_name(),true);
                
                _allStrategy.add(switchSAR);
            }
        }
    }
    
    public ArrayList<IndicatorsSimpleValue> getIndicatorValue(int market_type_id, int signal_period,long strategy_id, IndicatorBean oIndicatorBean)
    {
        if(null == oIndicatorBean)
            return new ArrayList<IndicatorsSimpleValue>();
        
        if(oIndicatorBean.getName().toUpperCase().indexOf(SMA.SMA_INDICATOR_NAME) != -1)
        {
            SMA oSMA = new SMA(DataCache.getInstance().getHLOCData(market_type_id, signal_period),oIndicatorBean.getId(),oIndicatorBean.getName(),oIndicatorBean.getPeriod(),0,false);
            
            return (ArrayList<IndicatorsSimpleValue>)oSMA.getIndicatorsValue(strategy_id);
        }
        else if(oIndicatorBean.getName().toUpperCase().indexOf(RSI.RSI_INDICATOR_NAME) != -1)
        {
            RSI oRSI = new RSI(DataCache.getInstance().getHLOCData(market_type_id, signal_period),oIndicatorBean.getId(),oIndicatorBean.getName(),oIndicatorBean.getPeriod(),0,false);
            
            if(oIndicatorBean.getName().indexOf(RSI.RSI_HIGH) != -1)
            {
                return (ArrayList<IndicatorsSimpleValue>)oRSI.getRSIHighValue(strategy_id);
            }
            else if(oIndicatorBean.getName().indexOf(RSI.RSI_LOW) != -1)
            {
                return (ArrayList<IndicatorsSimpleValue>)oRSI.getRSILowValue(strategy_id);
            }
            else
            {
                return (ArrayList<IndicatorsSimpleValue>)oRSI.getIndicatorsValue(strategy_id);
            }
        }
        else if(oIndicatorBean.getName().toUpperCase().indexOf(ParabolicSAR.SAR_INDICATOR_NAME) != -1)
        {
            ParabolicSAR oSAR = new ParabolicSAR(DataCache.getInstance().getHLOCData(market_type_id, signal_period),oIndicatorBean.getId(),oIndicatorBean.getName(),oIndicatorBean.getPeriod(),oIndicatorBean.getPeriod()-1,false);
            
            return (ArrayList<IndicatorsSimpleValue>)oSAR.getIndicatorsValue(strategy_id);
        }
        else
        {
            ArrayList<IndicatorsSimpleValue> oIndicatorsValue = new ArrayList<IndicatorsSimpleValue>();
            
            IndicatorsSimpleValue oIndicatorValue = new IndicatorsSimpleValue();      
            oIndicatorValue.setIndicatorBean(oIndicatorBean);            
            oIndicatorValue.setIndicatorValues(new ArrayList<IndicatorStringValue>());
            
            oIndicatorsValue.add(oIndicatorValue);
            
            return oIndicatorsValue;
        }
    }
    
    public void handleSignalByRelatedStrategy(Signal oBasicSignal)
    {       
        for(int i=0; i<_allStrategy.size(); i++)
        {
            StrategyBase strategy = _allStrategy.get(i);
            
            if(strategy.getStrategySystemName().toLowerCase().indexOf("top") == 0)
            {
                //handle this basic BUY or SELL signal through the Top Bottom Turn strategy
                
                StrategyTopBottomTurn topBottomTurn = (StrategyTopBottomTurn)strategy;
                oBasicSignal.setStrategy_id(strategy.getStrategyID());
                
                if(topBottomTurn.calculateSignal(oBasicSignal))
                {
                    InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Handle signal by TopBottomTurn strategy successfully!");
                    LogTools.getInstance().insertLog(DebugLevel.INFO,"Handle signal by TopBottomTurn strategy successfully!");
                }
                else
                {
                    InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Handle signal by TopBottomTurn strategy failed!");
                    LogTools.getInstance().insertLog(DebugLevel.INFO,"Handle signal by TopBottomTurn strategy failed!");
                }
            }
            else if(strategy.getStrategySystemName().toLowerCase().indexOf("support") == 0)
            {
                //handle this basic BUY or SELL signal through the Support & Resistance strategy
                StrategySupportResistance supportResistance = (StrategySupportResistance)strategy;
                oBasicSignal.setStrategy_id(strategy.getStrategyID());
                
                if(supportResistance.calculateSignal(oBasicSignal))
                {
                    InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Handle signal by Support&Resistance strategy successfully!");
                    LogTools.getInstance().insertLog(DebugLevel.INFO,"Handle signal by Support&Resistance strategy successfully!");
                }
                else
                {
                    InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Handle signal by Support&Resistance strategy failed!");
                    LogTools.getInstance().insertLog(DebugLevel.INFO,"Handle signal by Support&Resistance strategy failed!");
                }
            }
        }
    }
        
    public StrategyBean getStrategy(long strategyID)
    {
        return SFDBAccessor.getInstance().getStrategyFullInfoByID(strategyID);
    }
}
