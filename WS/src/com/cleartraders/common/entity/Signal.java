package com.cleartraders.common.entity;

import java.io.Serializable;

public class Signal implements Serializable
{
    private static final long serialVersionUID = -5651382180202086960L;
    
    private long id=0;
    private long signal_type=0;
    private long market_type_id=0;
    private long strategy_id=0;
    
    //market_name is display_name, such as USD/EUR
    private String market_name = "";
    
    //market_topic_name is JMS topic name, in this case, it is TEURUSD
    private String market_topic_name="";
    private int signal_period=0; //This is period type id
    private int signal_period_minutes=0;// this is minutes of the period type
    private String signal_period_name="";
    
    //generate_date is LONG time which will NOT be changed by Local Time-zone, it is used to identify signal position
    private long generate_date=0; 
    
    //generate_date_string is String time which is changed by Local Time-zone, it will show on Chart
    //DB doesn't store this value, it is just used display value
    private String generate_date_string=""; 
    private double signalValue=0;
    
    //Same as generate_date
    //Note: when expire date transfered to front end. Its value is The expire date - current date = time out time
    //expire date is the Seconds
    private long expire_date=0; 
    
    //Same as expire_date_string
    private String expire_date_string = ""; 
    private int signal_rate=0;
    private int direction=0;
    private double profit=0;
    private String profitString="";
    private int checked=0;
    private String system_name="";
    
    public Signal()
    {        
    }
    
    public Signal(Signal oCloneSignal)
    {
        this.id= oCloneSignal.id;
        this.signal_type= oCloneSignal.signal_type;
        this.market_type_id= oCloneSignal.market_type_id;
        this.strategy_id= oCloneSignal.strategy_id;
        this.market_name = oCloneSignal.market_name;
        this.market_topic_name= oCloneSignal.market_topic_name;
        this.signal_period= oCloneSignal.signal_period;
        this.signal_period_minutes= oCloneSignal.signal_period_minutes;
        this.signal_period_name=oCloneSignal.signal_period_name;
        this.generate_date= oCloneSignal.generate_date;
        this.generate_date_string= oCloneSignal.generate_date_string;
        this.signalValue= oCloneSignal.signalValue;
        this.expire_date= oCloneSignal.expire_date; 
        this.expire_date_string = oCloneSignal.expire_date_string; 
        this.signal_rate= oCloneSignal.signal_rate;
        this.direction= oCloneSignal.direction;
        this.profit= oCloneSignal.profit;
        this.profitString= oCloneSignal.profitString;
        this.checked= oCloneSignal.checked;
        this.system_name= oCloneSignal.system_name;
    }
    
    public int getSignal_period_minutes()
    {
        return signal_period_minutes;
    }
    public void setSignal_period_minutes(int signal_period_minutes)
    {
        this.signal_period_minutes = signal_period_minutes;
    }
    public String getGenerate_date_string()
    {
        return generate_date_string;
    }
    public void setGenerate_date_string(String generate_date_string)
    {
        this.generate_date_string = generate_date_string;
    }
    public String getExpire_date_string()
    {
        return expire_date_string;
    }
    public void setExpire_date_string(String expire_date_string)
    {
        this.expire_date_string = expire_date_string;
    }
    public String getMarket_topic_name()
    {
        return market_topic_name;
    }
    public void setMarket_topic_name(String market_topic_name)
    {
        this.market_topic_name = market_topic_name;
    }
    public String getProfitString()
    {
        return profitString;
    }
    public void setProfitString(String profitString)
    {
        this.profitString = profitString;
    }
    public long getId()
    {
        return id;
    }
    public void setId(long id)
    {
        this.id = id;
    }
    public int getChecked()
    {
        return checked;
    }
    public void setChecked(int checked)
    {
        this.checked = checked;
    }
    public int getDirection()
    {
        return direction;
    }
    public void setDirection(int direction)
    {
        this.direction = direction;
    }
    
    public long getExpire_date()
    {
        return expire_date;
    }
    public void setExpire_date(long expire_date)
    {
        this.expire_date = expire_date;
    }
    public long getGenerate_date()
    {
        return generate_date;
    }
    public void setGenerate_date(long generate_date)
    {
        this.generate_date = generate_date;
    }
    public long getMarket_type_id()
    {
        return market_type_id;
    }
    public void setMarket_type_id(long market_type_id)
    {
        this.market_type_id = market_type_id;
    }
    public double getProfit()
    {
        return profit;
    }
    public void setProfit(double profit)
    {
        this.profit = profit;
    }
    public int getSignal_period()
    {
        return signal_period;
    }
    public void setSignal_period(int signal_period)
    {
        this.signal_period = signal_period;
    }
    public int getSignal_rate()
    {
        return signal_rate;
    }
    public void setSignal_rate(int signal_rate)
    {
        this.signal_rate = signal_rate;
    }
    public double getSignalValue()
    {
        return signalValue;
    }
    public void setSignalValue(double signalValue)
    {
        this.signalValue = signalValue;
    }
    public String getSystem_name()
    {
        return system_name;
    }
    public void setSystem_name(String system_name)
    {
        this.system_name = system_name;
    }
    public long getSignal_type()
    {
        return signal_type;
    }
    public void setSignal_type(long signal_type)
    {
        this.signal_type = signal_type;
    }
    
    public String toCompletedTextMessage()
    {
        /*
        For jssignals
        
        id 
        signal_type 
        market_type_id 
        market_name 
        market_topic_name 
        signal_period 
        generate_date 
        generate_date_string 
        signalValue 
        expire_date 
        expire_date_string 
        signal_rate 
        direction 
        profit 
        profitString 
        checked 
        system_name 
        signal_period_minutes
        strategy_id
        
        19 fields
        */
        String signalMessage = "";
        
        signalMessage += this.getId();
        signalMessage += ",";
        
        signalMessage += this.getSignal_type();
        signalMessage += ",";
        
        signalMessage += getMarket_type_id();
        signalMessage += ",";
        
        signalMessage += this.getMarket_name();
        signalMessage += ",";
        
        signalMessage += this.getMarket_topic_name();
        signalMessage += ",";
        
        signalMessage += getSignal_period();
        signalMessage += ",";
        
        signalMessage += getGenerate_date();
        signalMessage += ",";
        
        signalMessage += this.getGenerate_date_string();
        signalMessage += ",";
        
        signalMessage += getSignalValue();
        signalMessage += ",";
        
        signalMessage += getExpire_date();
        signalMessage += ",";
        
        signalMessage += this.getExpire_date_string();
        signalMessage += ",";
        
        signalMessage += getSignal_rate();
        signalMessage += ",";
        
        signalMessage += getDirection();
        signalMessage += ",";
        
        signalMessage += getProfit();
        signalMessage += ",";
        
        signalMessage += this.getProfitString();
        signalMessage += ",";
        
        signalMessage += getChecked();
        signalMessage += ",";
        
        signalMessage += getSystem_name();
        signalMessage += ",";
        
        signalMessage += getSignal_period_minutes();
        signalMessage += ",";
        
        signalMessage += getStrategy_id();
        
        return signalMessage;
    }
    
    public String toTextMessage()
    {
        /*
        For Flex signals
        
        signal_type     int unsigned not null, #0:BUY 1:SELL 2:Scalper 3:Scalper Display
        market_type_id  int unsigned not null, 
        signal_period   int unsigned not null,
        generate_date   double not null,
        signal_value    double not null,
        expire_date     double not null,
        signal_rate     int unsigned not null,
        direction       int unsigned not null, #0:BUY 1:SELL
        profit          double not null,    
        system_name     varchar(100),
        strategy_id     int unsigned not null
        */
       
       String signalMessage = "SIGNAL,";
       
       signalMessage += getSignal_type();
       signalMessage += ",";
       
       signalMessage += getMarket_type_id();
       signalMessage += ",";
       
       signalMessage += getSignal_period();
       signalMessage += ",";
       
       signalMessage += getGenerate_date();
       signalMessage += ",";
       
       signalMessage += getSignalValue();
       signalMessage += ",";
       
       signalMessage += getExpire_date();
       signalMessage += ",";
       
       signalMessage += getSignal_rate();
       signalMessage += ",";
       
       signalMessage += getDirection();
       signalMessage += ",";
       
       signalMessage += getProfit();
       signalMessage += ",";
       
       signalMessage += getChecked();
       signalMessage += ",";
       
       signalMessage += getSystem_name();
       signalMessage += ",";
       
       signalMessage += getStrategy_id();
       
       return signalMessage;
    }
    public String getMarket_name()
    {
        return market_name;
    }
    public void setMarket_name(String market_name)
    {
        this.market_name = market_name;
    }
    public String getSignal_period_name()
    {
        return signal_period_name;
    }
    public void setSignal_period_name(String signal_period_name)
    {
        this.signal_period_name = signal_period_name;
    }
    public long getStrategy_id()
    {
        return strategy_id;
    }
    public void setStrategy_id(long strategy_id)
    {
        this.strategy_id = strategy_id;
    }
}
