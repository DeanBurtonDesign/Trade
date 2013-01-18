package com.cleartraders.signalframe.dataengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jms.Message;
import javax.jms.TextMessage;

import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.entity.HLOCDataBean;
import com.cleartraders.common.entity.TickDataBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.message.IMessager;
import com.cleartraders.common.util.message.JMSTopicMessager;
import com.cleartraders.common.util.message.JMSTopicReceiver;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.signalframe.Utils;

/**
 * 1) Receive realtime data of specific market
 * 2) generate Minutes source data
 * 3) Notify to 
 * @author Administrator
 *
 */
public class RealtimeQuoteFeederEngine extends JMSTopicReceiver implements IQuoteEngine
{
    private long _marketID = 0;
    private long _periodID = 0;
    private String _topciName = "";
    private String _identifiedString = "";
    
    //one indicator name can be used for different strategy, so, there might be more than one QuoteFeeder instance which use same name
    private Map<String, List<IQuoteFeeder>> _registedIndicatorQuoteFeeders = new HashMap<String, List<IQuoteFeeder>>();
    private Map<String, IQuoteFeeder> _registedStrategyQuoteFeeders = new HashMap<String, IQuoteFeeder>();
    
    private IMessager _messagerSender = null;
    
    public RealtimeQuoteFeederEngine(String topicName, long marketID, long periodID)
    {
        super(topicName);
      
        this._topciName = topicName;
        this._marketID = marketID;
        this._periodID = periodID;
        this._identifiedString = this._marketID+":"+this._periodID;
        
        _messagerSender = new JMSTopicMessager(topicName);
    }

    public void onMessage(Message newMessage)
    {
        try
        {
        if(newMessage == null)
        {
            return;
        }
        
        if(newMessage instanceof TextMessage)
        {
            try
            {
                TextMessage textMessage = (TextMessage) newMessage;
                
                //parse data by tickerbean
                TickDataBean oTickerData = new TickDataBean(textMessage.getText());
                
                if(oTickerData.getPriceValue() <= 0)
                {
                    return;
                }
                
                //convert into current ticker data bean
                HLOCDataBean oTickerHLOCDataItem = new HLOCDataBean();
                oTickerHLOCDataItem.setVolume(oTickerData.getVolumeValue());
                oTickerHLOCDataItem.setMarketID(this._marketID);
                oTickerHLOCDataItem.setDataType(oTickerData.getDataType());
                
                long currentPeriodTime = Utils.getCurrentPeriodTime(oTickerData.getTickTime(), (int)this._periodID);
                oTickerHLOCDataItem.setDate(currentPeriodTime);
                
                HLOCDataBean previousItem = DataCache.getInstance().getLatestHLOCData(this._identifiedString);
                
                boolean isNewPeriodItem = false;
                
                if(oTickerHLOCDataItem.getDate() == previousItem.getDate())
                {            
                    oTickerHLOCDataItem.setOpen(previousItem.getOpen());
                    
                    //udpate its High, Low, Open, Close
                    if(previousItem.getHigh() > oTickerData.getPriceValue())
                    {
                        oTickerHLOCDataItem.setHigh(previousItem.getHigh());
                    }
                    else
                    {
                        oTickerHLOCDataItem.setHigh(oTickerData.getPriceValue());
                    }
                    
                    if(previousItem.getLow() < oTickerData.getPriceValue())
                    {
                        oTickerHLOCDataItem.setLow(previousItem.getLow());
                    }
                    else
                    {
                        oTickerHLOCDataItem.setLow(oTickerData.getPriceValue());
                    }
                    
                    oTickerHLOCDataItem.setClose(oTickerData.getPriceValue());
                    oTickerHLOCDataItem.setVolume(oTickerHLOCDataItem.getVolume() + previousItem.getVolume());
                }
                else
                {
                    isNewPeriodItem = true;
                    
                    oTickerHLOCDataItem.setClose(oTickerData.getPriceValue());
                    oTickerHLOCDataItem.setHigh(oTickerData.getPriceValue());
                    oTickerHLOCDataItem.setLow(oTickerData.getPriceValue());
                    oTickerHLOCDataItem.setOpen(oTickerData.getPriceValue());
                }
                
                if(isNewPeriodItem)
                {
                    DataCache.getInstance().addHLOCData(this._identifiedString, oTickerHLOCDataItem);
                    this.notifyQuote(isNewPeriodItem, oTickerHLOCDataItem);
                }
                else
                {
                    DataCache.getInstance().updateLatestHLOCData(this._identifiedString, oTickerHLOCDataItem);
                    this.notifyQuote(isNewPeriodItem, oTickerHLOCDataItem);
                }
            }
            catch(Exception e)
            {
                InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Realtime Quote Engine("+_topciName+":"+_periodID+") =>  occur exception!");
                LogTools.getInstance().insertLog(DebugLevel.INFO,"Realtime Quote Engine("+_topciName+":"+_periodID+") =>  occur exception!"+CommonTools.getExceptionDescribe(e));
                
                e.printStackTrace();
            }
        }
        }
        catch(Exception e)
        {
            InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Realtime Quote Engine occur exception!");
            e.printStackTrace();
        }
    }
    
    public void notifyQuote(boolean isNewQuote, HLOCDataBean oHLOCDataItem)
    {
        //must notify to indicator first,then notify to strategy
        //because there might be many indicators for each strategy, So, strategy could handle 
        //each quote update freely. (while indicator update or while all indicators update)
        notifyToIndicator(isNewQuote, oHLOCDataItem);
        
        notifyToStrategy(isNewQuote, oHLOCDataItem);
    }
    
    public void notifyToIndicator(boolean isNewQuote, HLOCDataBean oHLOCDataItem)
    {
        Iterator<List<IQuoteFeeder>> oIt = _registedIndicatorQuoteFeeders.values().iterator();
        while(oIt.hasNext())
        {
            List<IQuoteFeeder> oQuoteFeeders = (List<IQuoteFeeder>)oIt.next();
            for(int i=0; i<oQuoteFeeders.size(); i++)
            {
                IQuoteFeeder oQuoteFeeder = oQuoteFeeders.get(i);
                oQuoteFeeder.onQuoteUpdate(isNewQuote, this._identifiedString, oHLOCDataItem);
            }
        }
    }

    public void notifyToStrategy(boolean isNewQuote, HLOCDataBean oHLOCDataItem)
    {
        Iterator<IQuoteFeeder> oIt = _registedStrategyQuoteFeeders.values().iterator();
        while(oIt.hasNext())
        {
            IQuoteFeeder oQuoteFeeder = (IQuoteFeeder)oIt.next();
            oQuoteFeeder.onQuoteUpdate(isNewQuote, this._identifiedString, oHLOCDataItem);
        }        
    }
    
    public boolean sendMessage(String message)
    {
        if(null != _messagerSender)
        {
            boolean sentResult = _messagerSender.sendTextMessage(message);
            
            try 
            {
                while(!sentResult)
                {
                    if(null != _messagerSender)
                    {
                        //don't stop init if it was failed
                        while(_messagerSender.init() != true)
                        {
                            Thread.sleep(500);
                        }
                        
                        //if sent result is wrong, then, don't stop trying
                        sentResult = _messagerSender.sendTextMessage(message);
                    }
                    else
                    {
                        break;
                    }                                
                }
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }
            
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public synchronized boolean addIndicatorQuoteFeeder(String feederName, IQuoteFeeder quoteFeeder)
    {
        if(_registedIndicatorQuoteFeeders == null || feederName == null || feederName.length() == 0 || quoteFeeder == null)
            return false;
        
        if(_registedIndicatorQuoteFeeders.get(feederName) != null)
        {
            List<IQuoteFeeder> oQuoteFeeders = _registedIndicatorQuoteFeeders.get(feederName);
            oQuoteFeeders.add(quoteFeeder);
        }
        else
        {
            List<IQuoteFeeder> oQuoteFeeders = new ArrayList<IQuoteFeeder>();
            oQuoteFeeders.add(quoteFeeder);
            
            _registedIndicatorQuoteFeeders.put(feederName, oQuoteFeeders);
        }
        
        return true;
    }
    
    /**
     * one feeder name may related more than instance, so be care to remove it
     */
    public synchronized boolean removeIndicatorQuoteFeeder(String feederName)
    {
        if(_registedIndicatorQuoteFeeders == null || feederName == null || feederName.length() == 0 )
            return false;
        
        if(_registedIndicatorQuoteFeeders.get(feederName) == null)
            return false;
        
        _registedIndicatorQuoteFeeders.remove(feederName);
        
        return true;
    }
    
    public synchronized boolean addStrategyQuoteFeeder(String feederName, IQuoteFeeder quoteFeeder)
    {
        if(_registedStrategyQuoteFeeders == null || feederName == null || feederName.length() == 0 || quoteFeeder == null)
            return false;
        
        if(_registedStrategyQuoteFeeders.get(feederName) != null)
            return false;
        
        _registedStrategyQuoteFeeders.put(feederName, quoteFeeder);
        
        return true;
    }
    
    public synchronized boolean removeStrategyQuoteFeeder(String feederName)
    {
        if(_registedStrategyQuoteFeeders == null || feederName == null || feederName.length() == 0 )
            return false;
        
        if(_registedStrategyQuoteFeeders.get(feederName) == null)
            return false;
        
        _registedStrategyQuoteFeeders.remove(feederName);
        
        return true;
    }
    
    public boolean startEngine()
    {
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Start Realtime Quote Engine, Market Name:"+this._topciName+", Period ID:"+this._periodID);
        LogTools.getInstance().insertLog(DebugLevel.INFO,"Start Realtime Quote Engine, Market Name:"+this._topciName+", Period ID:"+this._periodID);
        
        //the JMS Topic receiver can start itself
        return true;
    }
    
    public void stopEngine()
    {
        this.destory();
    }
    
    public boolean getStatus()
    {
        return true;
    }
    
    public void setStatus(boolean live)
    {
        
    }
}
