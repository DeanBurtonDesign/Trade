package com.cleartraders.common.util.log;

import java.io.*;

import com.cleartraders.common.util.config.CommonResManager;
import com.cleartraders.common.util.tools.CommonTools;



/**
 * 
 * @author Peter
 */

public class LogTools
{
	private static LogTools m_oInstance;
		
	// Data output stream
	private OutputStreamWriter m_oDataOut;
	
	// File output stream
	private FileOutputStream m_oFileOut;
	// log file name
	private String m_strFileName;
	
	private int m_iMaxSizOfLogFile;
	
	private int m_iCurrSizeOfLogFile;
	
	
	private LogTools(int iEachFileSize)
	{
		m_iMaxSizOfLogFile = iEachFileSize;
		
        m_iMaxSizOfLogFile = m_iMaxSizOfLogFile * 1024;
		m_iCurrSizeOfLogFile = 0;
		
        try
		{
			createNewLogFile();
		}
		catch (Exception e)
		{
			InfoTrace.getInstance().printInfo(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
		}
	}
	
	public synchronized static LogTools getInstance()
	{
		if (m_oInstance == null)
		{
			m_oInstance = new LogTools(1024);
		}
		
		return m_oInstance;
	}
	
    private void createNewLogFile() throws Exception
	{
		try
		{
			String strCurrentDate = CommonTools.getCurrentTimeForLogFile();
			m_strFileName = strCurrentDate + ".log";
			
			String strPath = CommonTools.getSysDir()+ "log" + File.separator;

			if (!(new File(strPath).exists()))
			{
				new File(strPath).mkdir();
			}
			
			m_oFileOut = new FileOutputStream(strPath + m_strFileName);
			m_oDataOut = new OutputStreamWriter(m_oFileOut, "gb2312");
		}
		catch (FileNotFoundException eFileNotFound)
		{
			InfoTrace.getInstance().printInfo(DebugLevel.ERROR, "û���ҵ��ļ���־�ļ�");
			
			throw eFileNotFound;
		}
		catch (Exception e)
		{
			InfoTrace.getInstance().printInfo(DebugLevel.ERROR,
					CommonTools.getExceptionDescribe(e));
			
			throw e;
		}
	}
	
	private int getLogLevel()
	{
		int iLogLevel = CommonResManager.getInstance().getLog_level();
		
		switch (iLogLevel)
		{
			case LogLevel.NO_LOG:
			case LogLevel.INFO:
			case LogLevel.WARNING:
			case LogLevel.ERROR:
				break;
			default:
				iLogLevel = LogLevel.NO_LOG;
				break;
		}
		
		return iLogLevel;
	}
	
	private String getFitLogInfo(int iLogLevel,String strLogInfo) throws Exception
	{
		String strCurrentDate = CommonTools.getCurrentTimeForLogFile();
		String strTempInfo = "";
		
		switch (iLogLevel)
		{
			case LogLevel.INFO:
				strTempInfo = strCurrentDate + " INFO:" + "  " + strLogInfo
						+ "\n";
				break;
			case LogLevel.WARNING:
				strTempInfo = strCurrentDate + " WARNING:" + "  " + strLogInfo
						+ "\n";
				break;
			case LogLevel.ERROR:
				strTempInfo = strCurrentDate + " ERROR:" + "  " + strLogInfo
						+ "\n";
				break;
			default:
				throw new Exception("��¼��־���𳬳�Χ!");
		}
		
		return strTempInfo;
	}
	
    public boolean insertLog(int iLevel, String strLog)
	{
		if (iLevel < getLogLevel() || (LogLevel.NO_LOG == getLogLevel())
				|| (null == strLog))
		{
			return false;
		}
		
		if ("".equals(strLog))
		{
			return true;
		}
		
		try
		{
			if (m_iMaxSizOfLogFile < m_iCurrSizeOfLogFile)
			{
				m_iCurrSizeOfLogFile = 0;
				createNewLogFile();
			}
			
			String strLogInfo = getFitLogInfo(iLevel,strLog);
			
			m_oDataOut.write(strLogInfo);
			m_iCurrSizeOfLogFile = m_iCurrSizeOfLogFile + strLogInfo.length();
			m_oDataOut.flush();
		}
		catch (Exception e)
		{
			InfoTrace.getInstance().printInfo(DebugLevel.ERROR,"��¼��־<" + strLog + ">ʧ��!");
			LogTools.getInstance().insertLog(DebugLevel.ERROR,e.getMessage()+CommonTools.getExceptionDescribe(e));
			
			return false;
		}
		
		return true;
	}
	
	
	public static void main(String args[]) throws Exception
	{
		LogTools oLog = new LogTools(1024);
		long lSendBefore = System.currentTimeMillis();
		int i = 0;
		for (; i < 100; i++)
		{
			oLog.insertLog(DebugLevel.INFO,"......................Test............................. ");
		}
		long lAfterSend = System.currentTimeMillis();
		
		System.out.println("" + i + "����ʱ��Ϊ��" + (lAfterSend - lSendBefore)+ "����!");
	}
}
