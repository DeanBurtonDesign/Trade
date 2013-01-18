package com.cleartraders.common.util.log;

import com.cleartraders.common.util.config.CommonResManager;

public class InfoTrace
{
	private static InfoTrace m_oInstance;
		
	private InfoTrace()
	{
	}
			
    public synchronized static InfoTrace getInstance()
	{
		if (m_oInstance == null)
		{
			m_oInstance = new InfoTrace();
		}
		
		return m_oInstance;
	}
	
    private int getDebugLevel()
	{
        int iLevel = CommonResManager.getInstance().getDebug_level();
		
		switch (iLevel)
		{
			case DebugLevel.NO_DEBUG:
			case DebugLevel.INFO:
			case DebugLevel.WARNING:
			case DebugLevel.ERROR:
				break;
			default:
				iLevel = DebugLevel.NO_DEBUG;
				break;
		}
		
		return iLevel;
	}

    public void printInfo(int iLevel, String strInfo)
	{
        if (iLevel < getDebugLevel() || (DebugLevel.NO_DEBUG == getDebugLevel())
				|| (null == strInfo) || ("".equals(strInfo)))
		{
			return;
		}
		
		switch (iLevel)
		{
			case DebugLevel.INFO:
				System.out.println("Info:" + strInfo);
				break;
			case DebugLevel.WARNING:
				System.out.println("Warning:" + strInfo);
				break;
			case DebugLevel.ERROR:
				System.out.println("Error:" + strInfo);
				break;
			default:
				break;
		}
	}
	
	
	
	/**
	 * ��ͬһ���ϰ��չ涨�ļ����ӡ��Ϣ
	 * 
	 * @param iLevel
	 * @param strInfo
	 */
	public void printInfoAtLine(int iLevel, String strInfo)
	{
		// ����ӡ������Ϣ�ļ�������õļ���Ҫ�ͻ��ߵ�ǰΪ�ǵ��Լ�����ֱ�ӷ���
		if (iLevel < getDebugLevel() || (DebugLevel.NO_DEBUG == getDebugLevel())
				|| (null == strInfo) || ("".equals(strInfo)))
		{
			return;
		}
		
		switch (getDebugLevel())
		{
			case DebugLevel.INFO:
				System.out.print("Info:" + strInfo);
				break;
			case DebugLevel.WARNING:
				System.out.print("Warning:" + strInfo);
				break;
			case DebugLevel.ERROR:
				System.out.print("Error:" + strInfo);
				break;
			default:
				break;
		}
	}
}
