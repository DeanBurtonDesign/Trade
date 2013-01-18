package com.cleartraders.common.util.feeder.iqfeed;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.cleartraders.common.entity.HLOCDataBean;
import com.cleartraders.common.util.config.CommonResManager;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;

/**
 * Note: If there is no real time data, then, we should Enable extends IQ_32
 * 
 * Peter
 * 
*/
public class IQDataFeedHistoryDataHandler //extends IQ_32 
{
    int a=0;
    int b=0;
    public static final int HISTORY_TICKER_DATA_LENGTH = 7;

    /**
    BufferedReaders for reading data from System.in and from the socket
    */
    BufferedReader  sin;
    /**
    BufferedWriter for writing to the socket
    */
    BufferedWriter  sout;
    
    private static IQDataFeedHistoryDataHandler m_oInstance=null;
    
    private IQDataFeedHistoryDataHandler()
    {
        //Note: IQFeed can not call RegisterClientApp more than one time in same Procedure
        //If there is realtime data, then, don't need register history client app
        //RegisterClientApp(CommonResManager.getInstance().getApp_name(), CommonResManager.getInstance().getVersion(), CommonResManager.getInstance().getKey());
    }
    
    public static synchronized IQDataFeedHistoryDataHandler getInstance()
    {
        if( null == m_oInstance )
        {
            m_oInstance = new IQDataFeedHistoryDataHandler();
        }
        
        return m_oInstance;
    }
    
    public void destroy()
    {
        //RemoveClientApp();
    }

    /**
    Receives the status of IQConnect.  a==0 and b==0 indicates connected
    active status.  Other statuses indicate incorrect connection status.
    */
    public void IQConnectStatus(int a, int b) {
        this.a = a;
        this.b = b;
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"IQConnectStatus("+a+","+b+")");
    }
    
    /**
    Retreives monthly composite data
    
    According to IQFeed 4.5
    
    HMX,[Symbol],[MaxDatapoints],[DataDirection],[RequestID],[DatapointsPerSend]<CR><LF>

    Retrieves up to [MaxDatapoints] datapoints of composite monthly datapoints for the specified [Symbol].

    [Symbol] - Required - Max Length 20 characters.
    [MaxDatapoints] - Required - The maximum number of datapoints to be retrieved.
    [DataDirection] - Optional - '0' (default) for "newest to oldest" or '1' for "oldest to newest".
    [RequestID] - Optional - Will be sent back at the start of each line of data returned for this request.
    [DatapointsPerSend] - Optional - Specifies the number of datapoints that IQConnect.exe will queue before attempting to send across the socket to your app.
    
    */ 
    private ArrayList<String> getMonthlyData(String symbol, String months) throws Exception 
    {
        return getDataByCommand("HMX"+","+symbol+","+months+";");
    }
    
    /**
    Retreives daily composite data
    
    According to IQFeed 4.5, update this version.
    
    HWX,[Symbol],[MaxDatapoints],[DataDirection],[RequestID],[DatapointsPerSend]<CR><LF>

    Retrieves up to [MaxDatapoints] datapoints of composite weekly datapoints for the specified [Symbol].

    [Symbol] - Required - Max Length 20 characters.
    [MaxDatapoints] - Required - The maximum number of datapoints to be retrieved.
    [DataDirection] - Optional - '0' (default) for "newest to oldest" or '1' for "oldest to newest".
    [RequestID] - Optional - Will be sent back at the start of each line of data returned for this request.
    [DatapointsPerSend] - Optional - Specifies the number of datapoints that IQConnect.exe will queue before attempting to send across the socket to your app.
    
    */ 
    private ArrayList<String> getWeeklyData(String symbol, String weeks) throws Exception 
    {
        return getDataByCommand("HWX"+","+symbol+","+weeks+";");
    }
    
    /**
    Retreives daily composite data
    
    According to IQFeed 4.5, update current version
    
    HDX,[Symbol],[MaxDatapoints],[DataDirection],[RequestID],[DatapointsPerSend]<CR><LF>

    Retrieves up to [MaxDatapoints] number of End-Of-Day Data for the specified [Symbol].

    [Symbol] - Required - Max Length 20 characters.
    [MaxDatapoints] - Required - The maximum number of datapoints to be retrieved.
    [DataDirection] - Optional - '0' (default) for "newest to oldest" or '1' for "oldest to newest".
    [RequestID] - Optional - Will be sent back at the start of each line of data returned for this request.
    [DatapointsPerSend] - Optional - Specifies the number of datapoints that IQConnect.exe will queue before attempting to send across the socket to your app.
    */ 
    private ArrayList<String> getDailyData(String symbol, String days) throws Exception 
    {
        return getDataByCommand("HDX"+","+symbol+","+days+";");
    }
    
    /**
    Retreives minute composite data
    
    According to IQFeeder 4.5, update current version
    
    HIX,[Symbol],[Interval],[MaxDatapoints],[DataDirection],[RequestID],[DatapointsPerSend]
    Retrieves [MaxDatapoints] number of Intervals of data for the specified [Symbol].
    
    [Symbol] - Required - Max Length 20 characters.
    [Interval] - Required - The interval in seconds.
    [MaxDatapoints] - Required - The maximum number of datapoints to be retrieved.
    [DataDirection] - Optional - '0' (default) for "newest to oldest" or '1' for "oldest to newest".
    [RequestID] - Optional - Will be sent back at the start of each line of data returned for this request.
    [DatapointsPerSend] - Optional - Specifies the number of datapoints that IQConnect.exe will queue before attempting to send across the socket to your app.

    */  
    private ArrayList<String> getMinuteData(String symbol, String interval, String maxDatapoints) throws Exception 
    {
        
        return getDataByCommand("HIX"+","+symbol+","+interval+","+maxDatapoints+";");
    }
    
    private ArrayList<String> getDataByCommand(String command) throws Exception 
    {
        ArrayList<String> oResult = new ArrayList<String>();
        
        sout.write(command);
        sout.flush();
        String line = null;
        
        while ((line = sin.readLine()) != null) 
        {
            if (line.equals("!SYNTAX_ERROR!"))
            {
                break;
            }
            
            if("".equals(line.trim()))
            {
                continue;
            }
            
            if("!ENDMSG!".equals(line.trim()) || "!ENDMSG!,".equals(line.trim()))
            {
                break;
            }
            
            //System.out.println(line);
            oResult.add(line);            
        }
        
        return oResult;
    }
    
    /**
     * Get monthly data
     * @param symbolName
     * @param months
     * @return
     */
    public ArrayList<String> getMonthFinanceData(String symbolName, String months) 
    {
        ArrayList<String> oResult = new ArrayList<String>();
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"prepare to call getMonthFinanceData() symbolName="+symbolName+";months="+months+";");
        LogTools.getInstance().insertLog(DebugLevel.INFO,"prepare to call getMonthFinanceData() symbolName="+symbolName+";months="+months+";");
        
        //RegisterClientApp("CLEARTRADERS_981", "1.0", "0.11111111");
        try 
        {
            Socket  s;
            
            if (a!=0 || b!=0)
                return oResult;
    
            s = new Socket(InetAddress.getByName("localhost"), 9100);
    
            sin = new BufferedReader(new InputStreamReader(s.getInputStream()));
            sout = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
    
            ArrayList<String> oTempResult = getMonthlyData(symbolName,months);
            if(oTempResult != null)
            {
                for(int i=oTempResult.size()-1; i>=0; i--)
                {
                    oResult.add(oTempResult.get(i));
                }
            }
            oTempResult.clear();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        //RemoveClientApp();
        
        return oResult;
    }
    
    /**
     * Get weekly data
     * @param symbolName
     * @param weeks
     * @return
     */
    public ArrayList<String> getWeekFinanceData(String symbolName, String weeks) 
    {
        ArrayList<String> oResult = new ArrayList<String>();
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"prepare to call getWeekFinanceData() symbolName="+symbolName+";weeks="+weeks+";");
        LogTools.getInstance().insertLog(DebugLevel.INFO,"prepare to call getWeekFinanceData() symbolName="+symbolName+";weeks="+weeks+";");
        
        //RegisterClientApp("CLEARTRADERS_981", "1.0", "0.11111111");
        try 
        {
            Socket  s;
            
            if (a!=0 || b!=0)
                return oResult;
    
            s = new Socket(InetAddress.getByName("localhost"), 9100);
    
            sin = new BufferedReader(new InputStreamReader(s.getInputStream()));
            sout = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
    
            ArrayList<String> oTempResult = getWeeklyData(symbolName,weeks);
            if(oTempResult != null)
            {
                for(int i=oTempResult.size()-1; i>=0; i--)
                {
                    oResult.add(oTempResult.get(i));
                }
            }
            oTempResult.clear();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        //RemoveClientApp();
        
        return oResult;
    }
    
    /**
     * Get daily data
     * @param symbolName
     * @param days
     * @return
     */
    public ArrayList<String> getDayFinanceData(String symbolName, String days) 
    {
        ArrayList<String> oResult = new ArrayList<String>();
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"prepare to call getDayFinanceData() symbolName="+symbolName+";days="+days+";");
        LogTools.getInstance().insertLog(DebugLevel.INFO,"prepare to call getDayFinanceData() symbolName="+symbolName+";days="+days+";");
        
        //RegisterClientApp("CLEARTRADERS_981", "1.0", "0.11111111");
        try 
        {
            Socket  s;
            
            if (a!=0 || b!=0)
                return oResult;
    
            s = new Socket(InetAddress.getByName("localhost"), 9100);
    
            sin = new BufferedReader(new InputStreamReader(s.getInputStream()));
            sout = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
    
            ArrayList<String> oTempResult = getDailyData(symbolName,days);
            if(oTempResult != null)
            {
                //since the daily data should be divided by "day" not minutes (otherwise the signal will be shown in wrong position)
                //so, the hisotry data should be format as yyyy-MM-dd, and don't need to do offset
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
                
//                int defaultOffsetTime = TimeZone.getDefault().getOffset(System.currentTimeMillis());
//                long timeOffsetTime = defaultOffsetTime - CommonResManager.getInstance().getIqfeed_server_timezone_offset()*60*60*1000;
                                
                for(int i=oTempResult.size()-1; i>=0; i--)
                {
                    //oResult.add(upgradeDateFromStringToLong(oTempResult.get(i),dateTimeFormat, timeOffsetTime));
                    oResult.add(upgradeDateFromStringToLong(oTempResult.get(i),dateTimeFormat, 0));
                }
            }
            
            oTempResult.clear();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        //RemoveClientApp();
        
        return oResult;
    }


    /**
     * Get minute period data for specific days
     * @param symbolName
     * @param dayAccount
     * @param minAccount
     * @return  minute period data list
     */
    public ArrayList<String> getMinuteFinanceData(String symbol, long interval, long maxDatapoints) 
    {
        ArrayList<String> oResult = new ArrayList<String>();
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,System.currentTimeMillis()+" prepare to call getMinuteFinanceData() symbol Name="+symbol+";max Datapoints="+maxDatapoints+";min interval="+interval);
        LogTools.getInstance().insertLog(DebugLevel.INFO,System.currentTimeMillis()+" prepare to call getMinuteFinanceData() symbol Name="+symbol+";max Datapoints="+maxDatapoints+";min interval="+interval);
                
        //RegisterClientApp("CLEARTRADERS_981", "1.0", "0.11111111");
        
        try 
        {
            Socket  s=null;
            
            if (a!=0 || b!=0)
                return oResult;
    
            s = new Socket(InetAddress.getByName(CommonResManager.getInstance().getIqfeed_url()), CommonResManager.getInstance().getIqfeed_history_socket());
    
            sin = new BufferedReader(new InputStreamReader(s.getInputStream()));
            sout = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
    
            //interval is in second, so, interval should multiply 60
            ArrayList<String> oTempResult = getMinuteData(symbol,String.valueOf(interval*60),String.valueOf(maxDatapoints));
            
            if(oTempResult != null)
            {
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                int defaultOffsetTime = TimeZone.getDefault().getOffset(System.currentTimeMillis());
                long timeOffsetTime = defaultOffsetTime - CommonResManager.getInstance().getIqfeed_server_timezone_offset()*60*60*1000;
                                                
                //Note, you should get all return result. Otherwise, we have to get it next time
                for(int i=oTempResult.size()-1; i>=0; i--)
                {
                    oResult.add(upgradeDateFromStringToLong(oTempResult.get(i),dateTimeFormat,timeOffsetTime));
                }              
            }
            oTempResult.clear();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        //RemoveClientApp();
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,System.currentTimeMillis()+" Complete to call getMinuteFinanceData() symbol Name="+symbol+";max Datapoints="+maxDatapoints+";min interval="+interval);
        LogTools.getInstance().insertLog(DebugLevel.INFO,System.currentTimeMillis()+" Complete to call getMinuteFinanceData() symbol Name="+symbol+";max Datapoints="+maxDatapoints+";min interval="+interval);
        
        return oResult;
    }
    
    public List<HLOCDataBean> convertStringDataToHLOC(ArrayList<String> oStringData)
    {
        /*
        Result Format for History Data format
        Field   Format
        
        Time    float //Stamp  YYYY-MM-DD HH:MM:SS 2008-09-17 00:47:00
        High    Decimal
        Low     Decimal
        Open    Decimal
        Close   Decimal
        Total Volume    Integer
        Period Volume   Integer
        
        */
        
        if(null == oStringData)
        {
            return new ArrayList<HLOCDataBean>();
        }
        
        List<HLOCDataBean> hlocDataList = new ArrayList<HLOCDataBean>();
        
        for(int i=0; i<oStringData.size(); i++)
        {
            String hlocData = oStringData.get(i);
            
            String[] hlocDataItems = hlocData.split(",");
            if(hlocDataItems.length != 7)
            {
                continue;
            }
            
            HLOCDataBean oHlocDataBean = new HLOCDataBean();
            oHlocDataBean.setDate(Long.parseLong(hlocDataItems[0]));
            oHlocDataBean.setHigh(Double.parseDouble(hlocDataItems[1]));
            oHlocDataBean.setLow(Double.parseDouble(hlocDataItems[2]));
            oHlocDataBean.setOpen(Double.parseDouble(hlocDataItems[3]));
            oHlocDataBean.setClose(Double.parseDouble(hlocDataItems[4]));
            oHlocDataBean.setVolume(Double.parseDouble(hlocDataItems[6]));
            
            hlocDataList.add(oHlocDataBean);
        }
        
        return hlocDataList;
    }
    
    private String upgradeDateFromStringToLong(String initialQuoteString, SimpleDateFormat dateTimeFormat, long timeOffsetTime)
    {    
        /*
        Result Format for History Data format
        Field   Format
        
        Time    float //Stamp  YYYY-MM-DD HH:MM:SS 2008-09-17 00:47:00
        High    Decimal
        Low     Decimal
        Open    Decimal
        Close   Decimal
        Total Volume    Integer
        Period Volume   Integer
        
        */
        
        if(null == initialQuoteString || "".equals(initialQuoteString))
        {
            return "";
        }
        
        StringBuffer result = new StringBuffer();
        
        String[] subStrings = initialQuoteString.split(",");
        
        if( null != subStrings && subStrings.length == HISTORY_TICKER_DATA_LENGTH)
        {
            String dateString = subStrings[0];
            
            try
            {
                result.append(dateTimeFormat.parse(dateString).getTime()+timeOffsetTime);
                result.append(",");
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
            result.append(subStrings[1]);
            result.append(",");
            result.append(subStrings[2]);
            result.append(",");
            result.append(subStrings[3]);
            result.append(",");
            result.append(subStrings[4]);
            result.append(",");
            result.append(subStrings[5]);
            result.append(",");
            result.append(subStrings[6]);
        }
        else
        {
            result.append("0,0,0,0,0,0,0");
        }
        
        return result.toString();
    }
    
    /**
    Main static method.  It allocates a <code>tc</code> then calls it's
    run() method.
    @param args Ignored.
    */
    public static void main(String args[]) {
        //IQDataFeedSender me = new IQDataFeedSender();

        //me.run();
    }
}
