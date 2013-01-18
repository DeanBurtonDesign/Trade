package com.cleartraders.common.util.tools;

import java.util.Calendar;

/**
 * ��������
 * 
 * @author huangyuliang
 */

public class KeyFactory
{
	// ���ֵ
	private static long addValue = 0;
	
	
	// ��ʼʱ��
	private static long beginTime = 0;
	
	
	// �Ƚ�ʱ��
	private static long compareTime = 0;
	
	static
	{
		beginTime = getBeginDate(1970, 1, 1);
		compareTime = getBeginDate(2005, 1, 1);
	}
	
	
	
	/**
	 * ��ȡ��ʼʱ��
	 * 
	 * @param year
	 *            int ��
	 * @param month
	 *            int ��
	 * @param day
	 *            int ��
	 * @return long ����long��ʱ��
	 */
	private static long getBeginDate(int year, int month, int day)
	{
		Calendar oTemp = Calendar.getInstance();
		oTemp.set(year, month, day);
		
		return oTemp.getTimeInMillis();
	}
	
	
	
	/**
	 * ȡ��Ψһ��ֵ
	 * 
	 * @return String Ψһ��ֵ
	 */
	public synchronized static long getOnlyKeyWord()
	{
		addValue++;
		
		if (addValue > 9000)
		{
			addValue = 1;
			try
			{
				Thread.sleep(5);
			}
			catch (InterruptedException ex)
			{
				// SmsPubl.getLogger().error(ex.getMessage());
			}
		}
		
		return Long.parseLong(getStr(System.currentTimeMillis() + beginTime
				- compareTime, addValue));
	}
	
	
	
	/**
	 * ת��Ϊ����
	 * 
	 * @param n
	 *            long
	 * @return String
	 */
	private static String getHandsetLenStr(long n)
	{
		String s = String.valueOf(n);
		
		if (4 == s.length())
		{
			return s;
		}
		
		for (int i = 0; i <= 4; i++)
		{
			if (s.length() < i)
			{
				s = "0" + s;
			}
		}
		
		return s;
	}
	
	
	
	/**
	 * �ϳ�str
	 * 
	 * @param data
	 *            long LONG�����1
	 * @param data1
	 *            long LONG�����2
	 * @return String
	 */
	private static String getStr(long data, long data1)
	{
		return String.valueOf(data) + getHandsetLenStr(data1);
	}
	
	
	public static void main(String args[]) throws Exception
	{
		// long lSendBefore = System.currentTimeMillis();
		// int i=0;
		// for( ; i<100; i++ )
		// {
		// //AcountKeyWord.getOnlyKeyWord();
		// System.out.println(KeyFactory.getOnlyKeyWord());
		// }
		// long lAfterSend = System.currentTimeMillis();
		//	  
		// System.out.println(""+i+"����ʱ��Ϊ��"+(lAfterSend-lSendBefore)+"����!");
		
	}
}
