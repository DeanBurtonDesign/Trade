package com.cleartraders.common.util.feeder.iqfeed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.cleartraders.common.util.config.CommonResManager;
import com.cleartraders.common.util.feeder.DataFeederAdapter;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.iqfeed.IQ_32;

public class IQDataFeedRealTimeDataHandler extends DataFeederAdapter
{
    private IQDataFeed _feeder;
    private String _symbol="";
    public static final int INITIAL_TICKER_DATA_AT_LEAST_LENGTH = 31;
        
    public IQDataFeedRealTimeDataHandler(String symbol)
    {
        super();
        _symbol = symbol;
    }
    
    public boolean closeFeedConnection()
    {
        return _feeder.removeConnection();
    }

    public boolean createFeedConnection()
    {
        _feeder = new IQDataFeed();
        return _feeder.createConnection();
    }

    public boolean startFeed()
    {
        boolean result = false;
        
        if(null != _feeder)
        {
            _feeder.setLive(true);
            new Thread(_feeder).start();
        }
        
        return result;
    }

    public boolean stopFeed()
    {
        _feeder.setLive(false);
        
        _feeder.removeConnection();
        
        return true;
    }
    
    public boolean getStatus()
    {
        return _feeder.isLive();
    }
    
    public void setStatus(boolean live)
    {
        _feeder.setLive(live);
    }
    
    private class IQDataFeed extends IQ_32 implements Runnable 
    {
        private int a=0;
        private int b=0;
        
        private Socket _socket;
        private BufferedReader _sin;
        private BufferedWriter  _sout;
        
        private boolean _fistTimeStart=true;
        private boolean _live = true;
        
        public boolean isLive()
        {
            return _live;
        }

        public void setLive(boolean live)
        {
            this._live = live;
        }

        /**
        Called by the IQ_32, JNI interface to idicate the health of IQFeed's IQConnect.
        @param a
        @param b
        */
        public void IQConnectStatus(int a, int b) 
        {
            this.a = a;
            this.b = b;
            
            InfoTrace.getInstance().printInfo(DebugLevel.INFO,"IQConnectStatus("+a+","+b+")");
            
            if(a==0 && b==0 && _fistTimeStart)
            {
                _fistTimeStart = false;
                InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Started Data Feed "+_symbol+" ..................Success");
                LogTools.getInstance().insertLog(DebugLevel.INFO,"Started Data Feed "+_symbol+" ..................Success");
            }
            else if(_fistTimeStart)
            {
                _fistTimeStart = false;
                InfoTrace.getInstance().printInfo(DebugLevel.ERROR,"Started Data Feed "+_symbol+" ..................Failed");
                LogTools.getInstance().insertLog(DebugLevel.ERROR,"Started Data Feed "+_symbol+" ..................Failed");
            }
        }
        
        public boolean createConnection()
        {
            boolean result = false;
            
            InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Prepare to register Data Feed "+_symbol+" ...");
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Prepare to register Data Feed "+_symbol+" ...");
            
            // Start IQConnect & IQFeed 
            RegisterClientApp(CommonResManager.getInstance().getApp_name(), CommonResManager.getInstance().getVersion(), CommonResManager.getInstance().getKey());
            
            try 
            {
                if (a!=0 || b!=0)
                {
                    return false;
                }
        
                _socket = new Socket(InetAddress.getByName(CommonResManager.getInstance().getIqfeed_url()), CommonResManager.getInstance().getIqfeed_realtime_socket());
        
                _sin = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
                _sout = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));
                
                result = true;
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
                
                result = false;
            }
            
            return result;
        }
        
        public boolean removeConnection()
        {
            boolean result = false;
            try
            {
                _socket.close();
                
                _sin.close();
                _sout.close();
                
                result = true;
            }
            catch(Exception e)
            {
                result = false;
                
                e.printStackTrace();
            }
            finally
            {
                _socket = null;
                _sin = null;
                _sout = null;
                
                //Stop IQConnect
                RemoveClientApp();                
            }            
            
            return result;
        }
        
        public void run() 
        {
            while(_live)
            {
                try 
                {                    
                    String initialMessage = "";
                    String convertedTickerQuote = "";
                    
                    _sout.write("w"+_symbol+"\r\n");
                    _sout.flush();
                    
                    while ((initialMessage=_sin.readLine()) != null && _live) 
                    {
                        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Initial Message: "+initialMessage);     
                        
                        convertedTickerQuote = upgradeDateFromStringToLong(initialMessage);
                        
                        if(null != convertedTickerQuote)
                        {                        
                            InfoTrace.getInstance().printInfo(DebugLevel.INFO,"After converted: "+convertedTickerQuote); 
                            
                            sendMessage(convertedTickerQuote);
                        }
                    }
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
                
                //if this feeder is still live, then, system will keep it live all the time
                if(_live)
                {
//                    InfoTrace.getInstance().printInfo(DebugLevel.ERROR,"Error: Feeder,"+_symbol+" encountered exception, it will remove connection first!");                    
//                    removeConnection();
//                    
//                    InfoTrace.getInstance().printInfo(DebugLevel.ERROR,"Error: Feeder,"+_symbol+" removed connection success, it will create connection!");
//                    createConnection();
                      InfoTrace.getInstance().printInfo(DebugLevel.ERROR,"Error: Feeder,"+_symbol+" encountered exception, system set live=false, and it will be started again!"); 
                      LogTools.getInstance().insertLog(DebugLevel.ERROR,"Error: Feeder,"+_symbol+" encountered exception, system set live=false, and it will be started again!"); 
                      
                      _live = false;
                }
            }
        }
        
        private String upgradeDateFromStringToLong(String initialQuoteString)
        {
            /*
            Ticker Quote Format as below:
            Field   Format
            1) mark    String
            2) date    long  //Time Stamp  MM/DD/YYYY HH:MM:SS,09/18/2008 12:42:38
            3) price   double
            4) volume  double
            5) other   double
            */
            if(null == initialQuoteString || "".equals(initialQuoteString))
            {
                return "";
            }
            
            String result = null;
            
            String[] subStrings = initialQuoteString.split(",");
            
            if( null != subStrings && subStrings.length >= INITIAL_TICKER_DATA_AT_LEAST_LENGTH)
            {
                //message "Q" is for quote
                if(subStrings[0].equalsIgnoreCase("Q"))
                {
                    String dateString = subStrings[30]+" "+ subStrings[17];
                    dateString = dateString.substring(0,dateString.length()-1);
                    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    int defaultOffsetTime = TimeZone.getDefault().getOffset(System.currentTimeMillis());
                    long timeOffsetTime = defaultOffsetTime - CommonResManager.getInstance().getIqfeed_server_timezone_offset()*60*60*1000;
                    
                    try
                    {
                        long timeValue = dateTimeFormat.parse(dateString).getTime()+timeOffsetTime;
                        result = "Q,"+timeValue+",";
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                    
                    result += subStrings[3] + ",";
                    result += subStrings[7] + ",";
                    result += subStrings[18];
                }
            }
            else if(subStrings[0].equalsIgnoreCase("T") && subStrings.length == 2)
            {
                //message "T" is for active the Admin client
                //Time message format is T,20081009 05:06:00
                
                //convert time string to long value
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
                
                try
                {
                    long timeValue = dateTimeFormat.parse(subStrings[1]).getTime();
                    result = "T,"+timeValue;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            
            return result;
        }
    }

}
