package com.cleartraders.common.util.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.Security;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;

/**
 * ͨ�ù�����
 * 
 * @author huangyuliang
 */

public class CommonTools
{
	
	
	// 15min
	public static final long SECOND = 1000;
	// min
	public static final long MIN = 60 * SECOND;
	// day
	public static final long HOUR = 60 * MIN;
	// day
	public static final long DAY = 24 * HOUR;
	// 7day
	public static final long WEEK = 7 * DAY;
	// 31day
	public static final long MONTH = DAY * 30;
	
	public static final int SEC_SEC = 0;
	public static final int MIN_MIN = 1;
	public static final int HOUR_HOUR = 2;
	public static final int DAY_DAY = 3;
	public static final int WEEK_WEEK = 4;
	public static final int MONTH_MONTH = 5;
	
	
	
	/**
	 * ���ڵõ��쳣��ջ��Ϣ
	 * 
	 * @return String
	 */
	public static String getExceptionDescribe(Exception ex)
	{
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] info = ex.getStackTrace();
		
		for (int i = 0; i < info.length; i++)
		{
			sb.append(info[i].getFileName()).append("-->");
			sb.append(info[i].getClassName()).append(".class:");
			sb.append(info[i].getLineNumber()).append("-->");
			sb.append(info[i].getMethodName()).append("() \n");
		}
		
		return sb.toString();
	}
	
	
	
	/**
	 * ���ַ���ɲ���(�ո���ַ�
	 */
	public static String trimString(String strOrigString)
	{
		int j = 0;
		int iLastPostion = 0;
		int iSize = strOrigString.length();
		ArrayList<String> strParts = new ArrayList<String>();
		
		for (int i = 0; i < iSize; i++)
		{
			if (strOrigString.charAt(i) == ' ')
			{
				strParts.add(j, strOrigString.substring(iLastPostion, i));
				iLastPostion = i;
				j++;
			}
		}
		
		if (j > 0)
		{
			String strNewTargetPostion = "";
			
			for (int x = 0; x < j; x++)
			{
				strNewTargetPostion = strNewTargetPostion
						+ ((String) strParts.get(x)).trim();
			}
			
			strNewTargetPostion = strNewTargetPostion
					+ strOrigString.subSequence(iLastPostion + 1, iSize);
			
			return strNewTargetPostion;
			
		}
		
		return strOrigString;
	}
	
	/**
	 * �����ַ����Ƿ��(����
	 */
	public static boolean ifHaveNumberInString(String strTestString)
	{
		if("".equals(strTestString))
		{
			return false;
		}

		if (strTestString.indexOf("1") > -1 
				|| strTestString.indexOf("2") > -1
				|| strTestString.indexOf("3") > -1
				|| strTestString.indexOf("4") > -1
				|| strTestString.indexOf("5") > -1
				|| strTestString.indexOf("6") > -1
				|| strTestString.indexOf("7") > -1
				|| strTestString.indexOf("8") > -1
				|| strTestString.indexOf("9") > -1
				|| strTestString.indexOf("0") > -1 )
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * �����ַ����Ƿ��(��ĸ
	 */
	public static boolean ifHaveCharInName(String strName)
	{
		if ("".equals(strName))
		{
			return false;
		}
		
		if (strName.indexOf("a") > -1 || strName.indexOf("b") > -1
				|| strName.indexOf("c") > -1 || strName.indexOf("d") > -1
				|| strName.indexOf("e") > -1 || strName.indexOf("f") > -1
				|| strName.indexOf("g") > -1 || strName.indexOf("h") > -1
				|| strName.indexOf("i") > -1 || strName.indexOf("j") > -1
				|| strName.indexOf("k") > -1 || strName.indexOf("l") > -1
				|| strName.indexOf("m") > -1 || strName.indexOf("n") > -1
				|| strName.indexOf("o") > -1 || strName.indexOf("p") > -1
				|| strName.indexOf("q") > -1 || strName.indexOf("r") > -1
				|| strName.indexOf("s") > -1 || strName.indexOf("t") > -1
				|| strName.indexOf("u") > -1 || strName.indexOf("v") > -1
				|| strName.indexOf("w") > -1 || strName.indexOf("x") > -1
				|| strName.indexOf("y") > -1 || strName.indexOf("z") > -1
				|| strName.indexOf("A") > -1 || strName.indexOf("B") > -1
				|| strName.indexOf("C") > -1 || strName.indexOf("D") > -1
				|| strName.indexOf("E") > -1 || strName.indexOf("F") > -1
				|| strName.indexOf("G") > -1 || strName.indexOf("H") > -1
				|| strName.indexOf("I") > -1 || strName.indexOf("J") > -1
				|| strName.indexOf("K") > -1 || strName.indexOf("L") > -1
				|| strName.indexOf("M") > -1 || strName.indexOf("N") > -1
				|| strName.indexOf("O") > -1 || strName.indexOf("P") > -1
				|| strName.indexOf("Q") > -1 || strName.indexOf("R") > -1
				|| strName.indexOf("S") > -1 || strName.indexOf("T") > -1
				|| strName.indexOf("U") > -1 || strName.indexOf("V") > -1
				|| strName.indexOf("W") > -1 || strName.indexOf("X") > -1
				|| strName.indexOf("Y") > -1 || strName.indexOf("Z") > -1)
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * �����ַ����Ƿ��(�����ַ�
	 */
	public static boolean ifHaveSpesialStringInName(String strName)
	{
		if("".equals(strName))
		{
			return false;
		}
		
		if (strName.indexOf(".") > -1 || strName.indexOf(",") > -1
				|| strName.indexOf("?") > -1 || strName.indexOf("!") > -1
				|| strName.indexOf(":") > -1 || strName.indexOf(";") > -1
				|| strName.indexOf("-") > -1 || strName.indexOf("+") > -1
				|| strName.indexOf("#") > -1 || strName.indexOf("(") > -1
				|| strName.indexOf(")") > -1 || strName.indexOf("'") > -1
				|| strName.indexOf("\"") > -1 || strName.indexOf("@") > -1
				|| strName.indexOf("$") > -1 || strName.indexOf("/") > -1
				|| strName.indexOf("=") > -1 || strName.indexOf("<") > -1
				|| strName.indexOf(">") > -1 || strName.indexOf("*") > -1)
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * ����ָ�����ȵĿ��ַ�
	 */
	public static String getSpecialStringOfSpace(int iSize)
	{
		if (iSize < 0)
		{
			return null;
		}
		
		String strReturnValue = "";
		
		for (int i = 0; i < iSize; i++)
		{
			strReturnValue += " ";
		}
		
		return strReturnValue;
	}
	
	
	
	
	/**
	 * @method name:getSystemDir()��
	 * @description:�õ��������е����ϵ�һ��Ŀ¼
	 * @param:��
	 * @output:String
	 * @exception:��
	 */
	public static String getSystemDir()
	{
		String userDir = System.getProperty("user.dir");
		
		
		// �õ��������е����ϵ�һ��Ŀ¼
		String StrdirTemp = getBeforeThreeDir(splitString(userDir,
				File.separator));
		
		
		// �����windows����ϵͳ��Ҫ��Ŀ¼ǰ���ϸ�Ŀ¼���"/"
		if (!System.getProperty("os.name").startsWith("Windows"))
		{
			StrdirTemp = "/" + StrdirTemp;
		}
		
		return StrdirTemp;
	}
	
	
	
	/**
	 * @method name:splitString()��
	 * @description:�õ�������ַ��Էָ����ֵ��ַ�����
	 * @param:String splitStr �ָ���ַ�, String delim �ָ��
	 * @output:String
	 * @exception:��
	 */
	public static String[] splitString(String splitStr, String delim)
	{
		StringTokenizer toker = new StringTokenizer(splitStr, delim);
		
		
		// �õ��ָ��Ԫ�صĸ���
		int count = toker.countTokens();
		
		String[] result = new String[count];
		
		
		// ���ָ���ÿһ��Ԫ��˳�򱣴浽�ַ�������
		for (int i = 0; i < count; i++)
		{
			try
			{
				result[i] = toker.nextToken();
			}
			catch (NoSuchElementException ex)
			{
				return null;
			}
		}
		
		return result;
	}
	
	
	
	/**
	 * @method name:getBeforeThreeDir()��
	 * @description:�õ������Ŀ¼�����ϵ�һ��Ŀ¼
	 * @param:String [] splitString �ָ��ַ�����
	 * @output:String
	 * @exception:��
	 */
	public static String getBeforeThreeDir(String[] splitString)
	{
		String strTemp = "";
		
		
		// ȥ���ַ������е�������Ԫ��,�Ӷ�õ�����Ŀ¼�����ϵ�һ��Ŀ¼�ַ�
		for (int i = 0; i < splitString.length - 1; i++)
		{
			if (0 == i)
			{
				strTemp += splitString[0] + File.separator;
			}
			else
			{
				strTemp += splitString[i] + File.separator;
			}
		}
		
		return strTemp;
	}
	
	
	
	/**
	 * ��ȡ�����ļ��и����������� ע����ȡ�����ļ��Ǽ�ʱ�ԵĴ�IO��ȡ�����ܴ�۴�
	 * 
	 * @param ��
	 * @return
	 */
	public static HashMap queryProperties(String filename) throws Exception
	{
		HashMap<String, String> obj = new HashMap<String, String>();
		
		FileInputStream fi = new FileInputStream(filename);
		Properties p = new Properties();
		p.load(fi);
		
		Enumeration e = p.keys();
		
		while (e.hasMoreElements())
		{
			String key = (String) e.nextElement();
			String value = (String) p.get(key);
			
			obj.put(key, value);
		}
		
		return obj;
	}
	
	
	
	/**
	 * ��һ��������ļ����һ���ȡ�� ע��1)�ɶ�ȡ��Ӣ���ļ� 2)�ļ����������·�����ļ�(����: H:\\filename.txt)
	 * 
	 * @param strCompleteFileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public static BufferedReader createReaderFromFile(String strCompleteFileName)
			throws FileNotFoundException
	{
		return new BufferedReader(new FileReader(strCompleteFileName));
	}
	
	
	
	/**
	 * ��ȡ�ļ�����ģ���ļ�ת��Ϊһ��String
	 * 
	 * @param url
	 *            String
	 * @return String
	 */
	static public String getFileToStr(String url)
	{
		String strFile = "";
		try
		{
			BufferedReader bufReader = createReaderFromFile(url);
			if (bufReader == null)
				return null;
			String bufLine = null;
			while ((bufLine = bufReader.readLine()) != null)
			{
				strFile += bufLine + "\n";
			}
			bufReader = null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return strFile;
	}
	
	
	
	/**
	 * ��ȡ��ǰ���е�Ŀ¼·��
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getSysDir()
	{
        String path="";
        
        try
        {
    		CommonTools oTemp = new CommonTools();
    		String classname = oTemp.getClass().getName().replace('.',File.separatorChar) + ".class";
    		ClassLoader oClassLoader = oTemp.getClass().getClassLoader();
    		java.net.URL url = oClassLoader.getResource(classname);
    		
    		if (null == url)
    		{
    			return null;
    		}
    		
    		String strFilePath = java.net.URLDecoder.decode(url.getFile(), "UTF-8");
            strFilePath = strFilePath.substring(0,strFilePath.indexOf("classes"));
            path = new File(strFilePath).getAbsolutePath().replace('\\',File.separatorChar) + File.separator;
        }
        catch(UnsupportedEncodingException e)
        {            
        }
		
		return path;
	}
	
	
	
	/**
	 * ��ȡWEB������·��
	 * 
	 * @return
	 */
	public static String getWebinfPath()
	{
		String strWebPath = "";
		
		String res = null;
		
		KeyFactory oTemp = new KeyFactory();
		String classname = oTemp.getClass().getName().replace('.',
				File.separatorChar)
				+ ".class";
		
		ClassLoader oClassLoader = oTemp.getClass().getClassLoader();
		
		if (oClassLoader != null)
		{
			java.net.URL url = oClassLoader.getResource(classname);
			
			if (url != null)
			{
				String filePath = url.getFile();
				int fileStrPosition = filePath.indexOf("file:/");
				int begin = 0;
				int end = filePath.length();
				
				if (fileStrPosition >= 0)
				{
					begin = fileStrPosition + "file:".length();
				}
				
				
				// ���ж��Ƿ���δ����ļ�
				end = filePath.indexOf("classes/" + classname);
				if (end < 0)
				{
					// �������webModule�����jar��
					end = filePath.indexOf("lib/");
					if (end < 0)
					{
						// ����ͨĿ¼�µ�jar��
						int tmpend = filePath.indexOf("!/");
						
						if (tmpend >= 0)
						{
							end = filePath.substring(0, tmpend)
									.lastIndexOf("/");
						}
					}
				}
				
				String rf = "";
				if (end < 0)
				{
					rf = filePath.substring(begin, filePath.length());
				}
				else
				{
					rf = filePath.substring(begin, end);
				}
				
				res = new File(rf).getAbsolutePath().replace('\\', '/') + "/";
			}
		}
		try
		{
			strWebPath = java.net.URLDecoder.decode(res, "UTF-8");
		}
		catch (UnsupportedEncodingException ex)
		{
		}
		
		return strWebPath;
	}
	
	
	/**
	 * �ֽ�תʮ����ַ�
	 * 
	 * @param b
	 * @param buf
	 */
	public static void byte2hex(byte b, StringBuffer buf)
	{
		char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		int high = ((b & 0xf0) >> 4);
		int low = (b & 0x0f);
		
		buf.append(hexChars[high]);
		buf.append(hexChars[low]);
	}
	
	
	
	/**
	 * �ֽ�����ת���ַ�
	 * 
	 * @param t
	 * @param start
	 * @param end
	 * @return
	 */
	public static String toHexStr(byte[] t, int start, int end)
	{
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < t.length; i++)
		{
			if (start <= i && end >= i)
			{
				if (t[i] != 0)
				{
					byte2hex(t[i], sb);
					sb.append(" ");
				}
			}
		}
		
		return sb.toString();
	}
	
	
	
	/**
	 * @param filename
	 *            String
	 * @param propName
	 *            String
	 * @return String
	 */
	public static String getPropValues(String filename, String propName)
			throws Exception
	{
		HashMap resultQuery = new HashMap();
		
		String userdir = getSystemDir();
		
		
		// �����ļ�����λ�ڡ�WEB����������·��\config��Ŀ¼
		String dir = userdir + File.separator + "config" + File.separator;
		
		String strConfigFileName = dir + filename;
		
		resultQuery = queryProperties(strConfigFileName);
		
		return ((String) resultQuery.get(propName));
	}
	
	
	
	/**
	 * ����ת��Ϊint
	 * 
	 * @param o
	 *            Object
	 * @param defaultValue
	 *            int
	 * @return int
	 */
	public static final int obj2int(Object o, int defaultValue)
	{
		if (o instanceof Number)
		{
			return ((Number) o).intValue();
		}
		
		if (o instanceof String)
		{
			String s = ((String) o).trim();
			try
			{
				return Integer.parseInt(s);
			}
			catch (Exception ex)
			{
			}
			
			
			// ���ת��Longʱ�����쳣���������ת����Double
			try
			{
				return Double.valueOf(s).intValue();
			}
			catch (Exception ex)
			{
			}
			
		}
		
		return defaultValue;
	}
	
	
	
	/**
	 * ����ת��Ϊlong
	 * 
	 * @param o
	 *            Object
	 * @param defaultValue
	 *            int
	 * @return long
	 */
	public static final long obj2long(Object o, int defaultValue)
	{
		if (o instanceof Number)
		{
			return ((Number) o).longValue();
		}
		
		if (o instanceof String)
		{
			String s = ((String) o).trim();
			try
			{
				return Long.parseLong(s);
			}
			catch (Exception ex)
			{
			}
			
			
			// ���ת��Longʱ�����쳣���������ת����Double
			try
			{
				return Double.valueOf(s).longValue();
			}
			catch (Exception ex)
			{
			}
		}
		
		return defaultValue;
	}
	
	
	
	/**
	 * ����ת��Ϊint
	 * 
	 * @param o
	 *            Object
	 * @return int
	 */
	public static final int obj2int(Object o)
	{
		return obj2int(o, 0);
	}
	
	
	
	/**
	 * ����ת��Ϊlong
	 * 
	 * @param o
	 *            Object
	 * @return long
	 */
	public static final long obj2long(Object o)
	{
		return obj2long(o, 0);
	}
	
	
	
	/**
	 * ���ַ�ת��Ϊ����
	 * 
	 * @param s
	 *            String
	 * @return int
	 */
	public static int getInt(String strValue)
	{
		if (null == strValue || 0 == strValue.length())
		{
			return 0;
		}
		
		return Integer.parseInt(strValue);
	}
	
	
	
	/**
	 * ���ַ�ת��ΪDOUBLE
	 * 
	 * @param strValue
	 *            String
	 * @return double
	 */
	public static double getDouble(String strValue)
	{
		if (null == strValue || 0 == strValue.length())
		{
			return 0;
		}
		
		return Double.valueOf(strValue).doubleValue();
	}
	
	
	
	/**
	 * ������ת��Ϊ�ַ�
	 * 
	 * @param iValue
	 *            int
	 * @return String
	 */
	public static String getString(int iValue)
	{
		return String.valueOf(iValue);
	}
	
	
	
	/**
	 * ��Doubleת��Ϊ�ַ�
	 * 
	 * @param dValue
	 *            double
	 * @return String
	 */
	public static String getString(double dValue)
	{
		return String.valueOf(dValue);
	}
	
	
	
	/**
	 * ��ȡ��������
	 */
	public static Locale getCurrentLocale()
	{
		return Locale.getDefault();
	}
	
	
	
	/**
	 * ��"yyyy-MM-dd"��ʽ��ȡʱ��
	 * 
	 * @return String
	 */
	public static String getSimpleCurrentTime()
	{
		Locale currentLocale = getCurrentLocale();
		
		Calendar rightNow = Calendar.getInstance(currentLocale);
		Date currentDate = rightNow.getTime();
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		return formater.format(currentDate);
	}
	
	
	
	/**
	 * ��"yyyy-MM-dd HH:mm:ss"��ʽ��ȡʱ��
	 * 
	 * @return String
	 */
	public static String getCompleteCurrentTime()
	{
		Locale currentLocale = getCurrentLocale();
		
		Calendar rightNow = Calendar.getInstance(currentLocale);
		Date currentDate = rightNow.getTime();
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formater.format(currentDate);
	}
	
	
	
	/**
	 * �õ���ǰʱ�䣬����һ���ַ����ַ���4���ļ�����
	 * 
	 * @return String
	 */
	public static String getCurrentTimeForLogFile()
	{
		Locale currentLocale = getCurrentLocale();
		
		Calendar rightNow = Calendar.getInstance(currentLocale);
		Date currentDate = rightNow.getTime();
		SimpleDateFormat formater = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		return formater.format(currentDate);
	}
	
	
	
	/**
	 * ��Dateת����yyyy-MM-dd HH:mm:ss�ַ�
	 * 
	 * @param date
	 *            Date
	 * @return String
	 */
	public static String getDate(Date date)
	{
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formater.format(date);
	}
	
	
	
	/**
	 * ��Dateת����yyyy-MM-dd�ַ�
	 * 
	 * @param time
	 *            String
	 * @return Date
	 */
	public static String getSimpleDate(Date date)
	{
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		return formater.format(date);
	}
	
	
	
	/**
	 * ��yyyy-MM-dd HH:mm:ss�ַ�ת����Date����
	 * 
	 * @param time
	 *            String
	 * @return Date
	 */
	public static Date getDate(String time)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(time, new ParsePosition(0));
		
	}
	
	
	
	/**
	 * ��yyyy-MM-dd�ַ�ת����Date����
	 * 
	 * @param time
	 *            String
	 * @return Date
	 */
	public static Date getSimpleDateFromString(String time)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.parse(time, new ParsePosition(0));
	}
	
	/**
	 * ��ȡ��ֹ����֮����������ڼ����
	 * @param oBeginDate
	 * @param oEndDate
	 * @return
	 */
	public static String getAllWeekDate(Date oBeginDate,Date oEndDate)
	{
		//Date oDate07 = CommonTools.getSimpleDateFromString("2007-1-6");
		//Date oDate08 = CommonTools.getSimpleDateFromString("2008-1-5");
		String strReturnValue = "";
		int iWeekDays = 7;
		
		while(oBeginDate.before(oEndDate))
		{
			//��7��
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			String nextWeekDate = CommonTools.addDate(formater.format(oBeginDate),iWeekDays,"yyyy-MM-dd");
			strReturnValue += nextWeekDate;
			strReturnValue += "\n";
			oBeginDate =  CommonTools.getSimpleDateFromString(nextWeekDate);			
		}
		
		return strReturnValue;
	}
	
	/**
	 * ��ȡ���� date_ һ�����е�ĳһ�죬һ���е�ĳ��Сʱ��һ��Сʱ�е�ĳ���ӣ�һ���ӵ�ĳ���� time
	 * һ���е�ĳ��Сʱ��һ��Сʱ�е�ĳ���ӣ�һ���ӵ�ĳ���� unit �£��ܣ��죬Сʱ�����ӣ� ��λ�ĸ���
	 * 
	 * @param date_
	 *            int
	 * @param now_
	 *            int
	 * @param unit
	 *            int
	 * @param time
	 *            int
	 * @return Date
	 */
	public static Date getDate(int date_, int now_, int unit, int time)
	{
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String strDate = null;
		switch (unit)
		{
			case CommonTools.MONTH_MONTH: // month
				strDate = String.valueOf(getYear()) + "-" + getMonth() + "-"
						+ date_ + " " + time + ":" + getMinute() + ":00";
				
				break;
			case CommonTools.WEEK_WEEK: // week
				strDate = String.valueOf(getYear()) + "-" + getMonth() + "-"
						+ date_ + " " + time + ":" + getMinute() + ":00";
				
				break;
			case CommonTools.DAY_DAY: // day
				strDate = String.valueOf(getYear()) + "-" + getMonth() + "-"
						+ getDayInMonth() + " " + date_ + ":" + time + ":00";
				
				break;
			case CommonTools.HOUR_HOUR: // hour
				strDate = String.valueOf(getYear()) + "-" + getMonth() + "-"
						+ getDayInMonth() + " " + getHour() + ":" + date_ + ":"
						+ time;
				
				break;
			case CommonTools.MIN_MIN: // minute
				strDate = String.valueOf(getYear()) + "-" + getMonth() + "-"
						+ getDayInMonth() + " " + getHour() + ":" + getMinute()
						+ ":" + date_;
				
				break;
			default:
				break;
		}
		if (strDate != null)
			return formater.parse(strDate, new ParsePosition(0));
		else
			return null;
	}
	
	
	
	/**
	 * �õ���ǰ��Сʱ��
	 * 
	 * @return int
	 */
	public static int getHour()
	{
		Locale currentLocale = getCurrentLocale();
		
		Calendar rightNow = Calendar.getInstance(currentLocale);
		Date currentDate = rightNow.getTime();
		SimpleDateFormat formater = new SimpleDateFormat("HH");
		return Integer.parseInt(formater.format(currentDate));
	}
	
	
	
	/**
	 * �õ���ǰ�ķ�����
	 * 
	 * @return int
	 */
	public static int getMinute()
	{
		Locale currentLocale = getCurrentLocale();
		Calendar rightNow = Calendar.getInstance(currentLocale);
		Date currentDate = rightNow.getTime();
		SimpleDateFormat formater = new SimpleDateFormat("mm");
		return Integer.parseInt(formater.format(currentDate));
	}
	
	
	
	/**
	 * �õ���ǰ������
	 * 
	 * @return int
	 */
	public static int getWeekInYear()
	{
		Locale currentLocale = getCurrentLocale();
		Calendar rightNow = Calendar.getInstance(currentLocale);
		Date currentDate = rightNow.getTime();
		SimpleDateFormat formater = new SimpleDateFormat("ww");
		return Integer.parseInt(formater.format(currentDate));
	}
	
	
	
	/**
	 * �õ���ǰ��������
	 * 
	 * @return int
	 */
	public static int getDayInWeek()
	{
		Locale currentLocale = getCurrentLocale();
		
		Calendar rightNow = Calendar.getInstance(currentLocale);
		Date currentDate = rightNow.getTime();
		SimpleDateFormat formater = new SimpleDateFormat("FF");
		return Integer.parseInt(formater.format(currentDate));
	}
	
	
	
	/**
	 * �õ���ǰ�ĺ���
	 * 
	 * @return int
	 */
	public static int getDayInMonth()
	{
		Locale currentLocale = getCurrentLocale();
		
		Calendar rightNow = Calendar.getInstance(currentLocale);
		Date currentDate = rightNow.getTime();
		SimpleDateFormat formater = new SimpleDateFormat("dd");
		return Integer.parseInt(formater.format(currentDate));
	}
	
	
	
	/**
	 * �õ���ǰ������
	 * 
	 * @return int
	 */
	public static int getMonth()
	{
		Locale currentLocale = getCurrentLocale();
		Calendar rightNow = Calendar.getInstance(currentLocale);
		Date currentDate = rightNow.getTime();
		SimpleDateFormat formater = new SimpleDateFormat("MM");
		return Integer.parseInt(formater.format(currentDate));
	}
	
	
	
	/**
	 * �õ���ǰ������
	 * 
	 * @return int
	 */
	public static int getSecond()
	{
		Locale currentLocale = getCurrentLocale();
		Calendar rightNow = Calendar.getInstance(currentLocale);
		Date currentDate = rightNow.getTime();
		SimpleDateFormat formater = new SimpleDateFormat("ss");
		return Integer.parseInt(formater.format(currentDate));
	}
	
	
	
	/**
	 * �õ���ǰ����
	 * 
	 * @return int
	 */
	public static int getYear()
	{
		Locale currentLocale = getCurrentLocale();
		Calendar rightNow = Calendar.getInstance(currentLocale);
		Date currentDate = rightNow.getTime();
		SimpleDateFormat formater = new SimpleDateFormat("yyyy");
		return Integer.parseInt(formater.format(currentDate));
	}
	
	
	
	/**
	 * �жϣɣ��Ƿ���Ч
	 * 
	 * @param ip
	 *            String
	 * @return boolean
	 */
	public static boolean isIPValid(String ip)
	{
		StringTokenizer tokenizer = new StringTokenizer(ip, ".");
		int count = tokenizer.countTokens();
		
		if (count != 4)
		{
			return false;
		}
		
		while (tokenizer.hasMoreElements())
		{
			String segment = tokenizer.nextToken();
			try
			{
				int number = Integer.parseInt(segment);
				if (number >= 255 || number <= 0)
				{
					return false;
				}
			}
			catch (Exception e)
			{
				// SmsPubl.getLogger().error(e.getMessage());
				return false;
			}
		}
		return true;
	}
	
	
	
	/**
	 * �����ʼ��Ƿ���Ч
	 * 
	 * @param email
	 *            String
	 * @return boolean
	 */
	public static boolean isEMailValid(String email)
	{
		int index1 = email.indexOf("@");
		int index2 = email.indexOf(".");
		
		if ((index1 == -1) || (index2 == -1))
		{
			return false;
		}
		
		return true;
	}
	
	
	
	/**
	 * �õ�}���ڵ�ʱ���ֵ
	 * 
	 * @param sdt
	 *            Date
	 * @param edt
	 *            Date
	 * @return long
	 */
	public static long getInterval(Date sdt, Date edt)
	{
		return (edt.getTime() - sdt.getTime()) / (3600 * 24 * 1000);
	}
	
	
	
	/**
	 * �õ�}���ڵ�ʱ���ֵ
	 * 
	 * @param sd
	 *            String
	 * @param ed
	 *            String
	 * @return long
	 */
	public static long getInterval(String sd, String ed)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date sdt = sdf.parse(sd, new ParsePosition(0));
		Date edt = sdf.parse(ed, new ParsePosition(0));
		return getInterval(sdt, edt);
	}
	
	
	
	/**
	 * �õ�ĳһ�����뵱ǰʱ���ʱ���ֵ
	 * 
	 * @param day
	 *            Date
	 * @return long
	 */
	public static long getInterval(Date day)
	{
		Calendar rightNow = Calendar.getInstance();
		Date dt1 = rightNow.getTime();
		return getInterval(dt1, day);
	}
	
	
	
	/**
	 * �õ�}���ڵ�ʱ���ֵ
	 * 
	 * @param sd
	 *            String
	 * @return long
	 */
	public static long getInterval(String sd)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date sdt = sdf.parse(sd, new ParsePosition(0));
		return getInterval(sdt);
	}
	
	
	
	/**
	 * ��ĳһʱ��Ӽ�һ������� time ��ʽ����Ϊ"yyyyMMdd"
	 * 
	 * @param time
	 *            String
	 * @param daynum_
	 *            int
	 * @return String
	 */
	public static String addDate(String time, int daynum_)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt = sdf.parse(time, new ParsePosition(0));
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);
		rightNow.add(Calendar.DATE, daynum_); // ��Ҫ�Ӽ������
		Date dt1 = rightNow.getTime();
		return sdf.format(dt1);
	}
	
	
	
	/**
	 * ��ĳһʱ��Ӽ�һ������� ʱ���ʽ����Ϊ"yyyyMMdd"
	 * 
	 * @param time
	 *            String
	 * @param daynum_
	 *            int
	 * @param format
	 *            String
	 * @return String
	 */
	public static String addDate(String time, int daynum_, String format)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dt = sdf.parse(time, new ParsePosition(0));
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);
		rightNow.add(Calendar.DATE, daynum_); // ��Ҫ�Ӽ������
		Date dt1 = rightNow.getTime();
		return sdf.format(dt1);
	}
	
	
	
	/**
	 * ��ȡ�ڵ�ǰʱ���ϼӼ�һ��������ʱ��
	 * 
	 * @param daynum_
	 *            int
	 * @return String
	 */
	public static String getRelativeDate(int daynum_)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar rightNow = Calendar.getInstance();
		rightNow.add(Calendar.DATE, daynum_); // ��Ҫ�Ӽ������
		Date dt1 = rightNow.getTime();
		return sdf.format(dt1);
		
	}
	
	
	
	/**
	 * ��ȡ��ǰʱ��
	 * 
	 * @return Date
	 */
	public static Date getNow()
	{
		Calendar rightNow = Calendar.getInstance();
		Date dt = rightNow.getTime();
		return dt;
	}
	
	
	
	/**
	 * ��ȡ�ڵ�ǰʱ���ϼӼ�һ������������
	 * 
	 * @param daynum_
	 *            int
	 * @return Date
	 */
	public static Date returnRelativeDate(int daynum_)
	{
		Calendar rightNow = Calendar.getInstance();
		rightNow.add(Calendar.DATE, daynum_); // ��Ҫ�Ӽ������
		Date dt1 = rightNow.getTime();
		return dt1;
	}
	
	
	
	/**
	 * ��ȡ�ڵ�ǰʱ���ϼӼ�һ������������
	 * 
	 * @param monthnum_
	 *            int
	 * @return String
	 */
	public static String getRelativeMonth(int monthnum_)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar rightNow = Calendar.getInstance();
		rightNow.add(Calendar.MONTH, monthnum_); // ��Ҫ�Ӽ������
		Date dt1 = rightNow.getTime();
		return sdf.format(dt1);
		
	}
	
	
	
	/**
	 * ��ȡ�ڵ�ǰʱ���ϼӼ�һ������������
	 * 
	 * @param monthnum_
	 *            int
	 * @return Date
	 */
	public static Date returnRelativeMonth(int monthnum_)
	{
		Calendar rightNow = Calendar.getInstance();
		rightNow.add(Calendar.MONTH, monthnum_); // ��Ҫ�Ӽ������
		Date dt1 = rightNow.getTime();
		return dt1;
		
	}
	
	
	
	/**
	 * ��ȡ��ȷʱ��
	 * 
	 * @param monthnum_
	 *            int
	 * @param type
	 *            int
	 * @return Date
	 */
	public static Date getExactTime(int monthnum_, int type)
	{
		
		Calendar rightNow = Calendar.getInstance();
		switch (type)
		{
			case 1:

				// ��Ҫ�Ӽ������
				rightNow.add(Calendar.SECOND, monthnum_);
				break;
			case 2:

				// ��Ҫ�Ӽ������
				rightNow.add(Calendar.MINUTE, monthnum_);
				break;
			
			case 3:

				// ��Ҫ�Ӽ������
				rightNow.add(Calendar.HOUR, monthnum_);
				break;
			
			case 4:

				// ��Ҫ�Ӽ������
				rightNow.add(Calendar.DATE, monthnum_);
				break;
			
			case 5:

				// ��Ҫ�Ӽ������
				rightNow.add(Calendar.MONTH, monthnum_);
				break;
			
			case 6:

				// ��Ҫ�Ӽ������
				rightNow.add(Calendar.YEAR, monthnum_);
				break;
		}
		Date dt1 = rightNow.getTime();
		return dt1;
		
	}
	
	
	
	/**
	 * ��ȡĳһʱ����ڵ��·�
	 * 
	 * @param begin
	 *            String
	 * @param end
	 *            String
	 * @return Vector
	 */
	public static Vector getMonthInterval(String begin, String end)
	{
		if (end.compareTo(begin) < 0)
			return null;
		Vector<Date> vMonth = new Vector<Date>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date begindate = sdf.parse(begin, new ParsePosition(0));
		Date enddate = sdf.parse(begin, new ParsePosition(0));
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(begindate);
		// ��Ҫ�Ӽ������
		rightNow.add(Calendar.MONTH, 1);
		Date tmpdate = rightNow.getTime();
		
		while (tmpdate.before(enddate))
		{
			vMonth.add(tmpdate);
			
			
			// ��Ҫ�Ӽ������
			rightNow.add(Calendar.MONTH, 1);
		}
		return vMonth;
		
	}
	
	
	
	/**
	 * ��ȡĳһʱ��ε�����
	 * 
	 * @param begin
	 *            String
	 * @param end
	 *            String
	 * @return Vector
	 */
	public static Vector getDayInterval(String begin, String end)
	{
		if (end.compareTo(begin) < 0)
			return null;
		Vector<Date> vMonth = new Vector<Date>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date begindate = sdf.parse(begin, new ParsePosition(0));
		Date enddate = sdf.parse(begin, new ParsePosition(0));
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(begindate);
		rightNow.add(Calendar.DATE, 1); // ��Ҫ�Ӽ������
		Date tmpdate = rightNow.getTime();
		
		while (tmpdate.before(enddate))
		{
			vMonth.add(tmpdate);
			rightNow.add(Calendar.DATE, 1); // ��Ҫ�Ӽ������
		}
		return vMonth;
		
	}
	
	
	
	/**
	 * �ж������Ƿ��ڸ�Χ
	 * 
	 * @param sd
	 *            String
	 * @param ed
	 *            String
	 * @param time
	 *            int
	 * @return boolean
	 */
	public static boolean isTimeOut(String sd, String ed, int time)
	{
		return (getInterval(sd, ed) > time) ? true : false;
	}
	
	
	
	/**
	 * ����IP��ַ��Χ
	 * 
	 * @param s
	 *            String
	 * @return long[]
	 */
	public static long[] computeRange(String s)
	{
		long al[] = new long[2];
		// al[0] = 0L;
		// al[1] = 0xffffffffL;
		String[] sb = { "0", "0", "0", "0" };
		String[] se = { "255", "255", "255", "255", };
		StringTokenizer st = new StringTokenizer(s, ".");
		int i = 0;
		while (st.hasMoreTokens())
		{
			String s1 = st.nextToken();
			
			if (s1.equals("*"))
			{
				// long l = 0xff;
				// al[1] += l<<((3 - i) * 8);
			}
			else
			{
				long l = Integer.parseInt(s1);
				if (l > 255)
					s1 = "255";
				
				sb[i] = s1;
				se[i] = s1;
			}
			
			i++;
			
			if (i >= 4)
				break;
		}
		
		for (i = 0; i < 4; i++)
		{
			long l = Integer.parseInt(sb[i]);
			al[0] += l << ((3 - i) * 8);
			
			l = Integer.parseInt(se[i]);
			al[1] += l << ((3 - i) * 8);
		}
		return al;
	}
	
	
	
	/**
	 * ֧�ֽ��� 192.168.1.*��ʽ ֧�ֽ��� 192.168.1.1/192.168.1.222
	 * 
	 * @param s
	 *            String
	 * @return long[]
	 */
	public static long[] calRange(String s)
	{
		long al[] = new long[2];
		al[0] = 0L;
		al[1] = 0L;
		if (s == null || s.equals("") || s.equals("localhost"))
			return al;
		if (s.indexOf("/") < 0)
		{
			return computeRange(s);
		}
		else
		{
			String s1 = s.substring(0, s.indexOf("/"));
			String s2 = s.substring(s.indexOf("/") + 1);
			long al1[] = computeRange(s1);
			long al2[] = computeRange(s2);
			al1[1] = al2[1];
			return al1;
		}
	}
	
	
	
	/**
	 * ip ��ַת������������
	 * 
	 * @param ipString
	 *            String
	 * @return int[]
	 */
	public static int[] IPAddress2Array(String ipString)
	{
		StringTokenizer token = new StringTokenizer(ipString);
		int[] array = new int[4];
		int i = 0;
		while (token.hasMoreTokens())
		{
			int tmpByte = Integer.parseInt(token.nextToken("."));
			array[i++] = tmpByte;
		}
		return array;
	}
	
	
	
	/**
	 * ��IP��ַת��Ϊlong
	 * 
	 * @param ipString
	 *            String
	 * @return long
	 */
	public static long IPAddress2Long(String ipString)
	{
		StringTokenizer token = new StringTokenizer(ipString, ".");
		int i = 0;
		long tmp = 0;
		while (token.hasMoreTokens())
		{
			long tmpByte = Long.parseLong(token.nextToken());
			tmp += (tmpByte << ((3 - i) * 8));
			i++;
		}
		return tmp;
		
	}
	
	
	
	/**
	 * ����ת����ip��ַ
	 * 
	 * @param ipv4Addr
	 *            long
	 * @return String
	 */
	public static String addressToString(long ipv4Addr)
	{
		StringBuffer buf = new StringBuffer();
		buf.append((ipv4Addr >> 24) & 0xff);
		buf.append('.');
		buf.append((ipv4Addr >> 16) & 0xff);
		buf.append('.');
		buf.append((ipv4Addr >> 8) & 0xff);
		buf.append('.');
		buf.append(ipv4Addr & 0xff);
		return buf.toString();
	}
	
	
	
	/**
	 * ����ת����ip��ַ
	 * 
	 * @param strDate
	 *            String
	 * @return String
	 */
	public static String convertOracleDate(String strDate)
	{
		String strReturn = null;
		
		if (strDate != null)
		{
			if (strDate.indexOf(".") > 0)
			{
				strReturn = strDate.substring(0, strDate.indexOf("."));
			}
			else
			{
				strReturn = strDate;
			}
		}
		return strReturn;
	}
	
	
	
	/**
	 * �õ�yyyy-mm-dd��ʽ�ĵ�ǰ����
	 * 
	 * @param inv_time
	 *            String
	 * @return String
	 */
	public static String getSimpleDate(String inv_time)
	{ // yyyy-mm-dd
		String simple_inure = null;
		if (inv_time != null && inv_time.length() > 10)
			simple_inure = inv_time.substring(0, 10);
		else
			simple_inure = inv_time;
		return simple_inure;
	}
	
	
	
	/**
	 * �õ���ǰ��ʱ��
	 * 
	 * @return Date
	 */
	public static Date getCurrentTime()
	{
		Locale currentLocale = getCurrentLocale();
		
		Calendar rightNow = Calendar.getInstance(currentLocale);
		Date currentDate = rightNow.getTime();
		return currentDate;
	}
	
	
	
	/**
	 * �õ�yyyy-mm-dd��ʽ�ĵ�ǰ����
	 * 
	 * @param inv_time
	 *            String
	 * @param flag
	 *            boolean
	 * @return String
	 */
	public static String getSimpleDate(String inv_time, boolean flag)
	{ // yyyy-mm-dd
		String simple_inure = inv_time;
		if (flag)
			simple_inure = "";
		if (inv_time != null && inv_time.length() > 10)
			simple_inure = inv_time.substring(0, 10);
		return simple_inure;
		
	}
	
	
	
	/**
	 * ����ֻ�ŵõ���������Ӫ�̵����
	 * 
	 * @param mobile_phone
	 *            String
	 * @return String
	 */
	public static String getGateName(String mobile_phone)
	{
		String strReturn = "";
		if (mobile_phone.startsWith("130") || mobile_phone.startsWith("131")
				|| mobile_phone.startsWith("132")
				|| mobile_phone.startsWith("133"))
		{
			strReturn = "unicom"; // 0
		}
		else
			if (mobile_phone.startsWith("134")
					|| mobile_phone.startsWith("135")
					|| mobile_phone.startsWith("136")
					|| mobile_phone.startsWith("137")
					|| mobile_phone.startsWith("138")
					|| mobile_phone.startsWith("139"))
			{
				strReturn = "mobile"; // 1
			}
			else
			{
				strReturn = "xlt";
			}
		
		return strReturn;
	}
	
	
	
	/**
	 * �滻�ַ���ַ�Ϊָ���ַ�
	 * 
	 * @param con
	 *            String
	 * @param tag
	 *            String
	 * @param rep
	 *            String
	 * @return String
	 */
	public static String replace(String con, String tag, String rep)
	{
		int j = 0;
		int i = 0;
		String RETU = "";
		String temp = con;
		int tagc = tag.length();
		
		while (i < con.length())
		{
			if (con.substring(i).startsWith(tag))
			{
				temp = con.substring(j, i) + rep;
				RETU += temp;
				i += tagc;
				j = i;
			}
			else
			{
				i += 1;
			}
		}
		
		RETU += con.substring(j);
		
		return RETU;
	}
	
	public static String filteDangerString(String source)
    {
	    String target="";
        
        target = replace(source, "'", "");
        
        return target;
    }
	
	/**
	 * ����html��ǩ��JSP����ȷ��ʾ,��:'��"
	 */
	public static String HtmlEncodeOther(String s)
	{
		String re;
		re = replace(s, "'", "��");
		//re = re.replace('"',' ');
		return re;
	}
	
	
	
	/**
	 * ����html��ǩ��JSP����ȷ��ʾ,��:<,>...
	 */
	public static String HtmlEncode(String s)
	{
		if (s == null)
		{
			return "";
		}
		String re;
		re = replace(s, "<", "&lt;");
		re = replace(re, ">", "&gt;");
		re = replace(re, "\n", "<br>");
		re = replace(re, " ", "&nbsp;");
		// re = replace(re, "'", "&#39");
		re = replace(re, "%23", "#");
		
		re = HtmlEncodeOther(re);
		return re;
	}
	
	
	
	/**
	 * ����url�������ַ�,��:#,%...
	 */
	public static String encoderURL(String s)
	{
		if (s == null)
		{
			return "";
		}
		String re;
		re = replace(s, "#", "%23");
		re = replace(re, "%", "%25");
		re = replace(re, "&", "%26");
		re = replace(re, "+", "%2B");
		// re = replace(re, " ", "%24");
		re = replace(re, "=", "%3D");
		re = replace(re, "?", "%3F");
		
		re = HtmlEncodeOther(re);
		
		return re;
	}
	
	
	
	/**
	 * ��ȡ�����ַ� �����ַ����Ϊ yyyymmdd ��� yyyy��mm��dd��
	 * 
	 * @param text
	 *            String
	 * @return String
	 */
	public static String caseDateTime(String text)
	{
		if (null == text)
			return null;
		StringBuffer sb = new StringBuffer(16);
		sb.append(text.substring(0, 4));
		sb.append("��");
		sb.append(text.substring(5, 7));
		sb.append("��");
		sb.append(text.substring(8));
		sb.append("��");
		return sb.toString();
	}
	
	
	
	/**
	 * ����ʱ��
	 * 
	 * @param text
	 *            String
	 * @return StringcutDateTime
	 */
	public static String cutDateTime(String text)
	{
		if (null == text)
			return null;
		int i = text.trim().indexOf(" ");
		if (-1 == i)
			return text;
		return text.substring(0, i);
	}
	
	/**
	 * �������ַ�ʵʩGBK���룬ע�⴫���ַ��������ַ�ᱻ ����
	 * 
	 * @param str
	 *            String ����ǰ���ַ�
	 * @return String �������ַ�
	 */
	public static String encode(String str)
	{
		StringBuffer s = new StringBuffer("");
		try
		{
			byte[] gbstr = str.getBytes("GBK");
			for (int i = 0; i < gbstr.length; i++)
			{
				if (gbstr[i] < 0)
				{ // ���Ϊ����
					s.append("%"
							+ Integer.toHexString(0xff & gbstr[i])
									.toUpperCase());
				}
				else
				{
					s.append((char) (0xff & gbstr[i]));
				}
			}
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return s.toString();
	}
    
    public static void showCurrentMemory()
    {
        int   totalMem   =   (int)Runtime.getRuntime().totalMemory()/1024;//Java   虚拟机中的内存总量,以字节为单位
        int   freeMem   =   (int)Runtime.getRuntime().freeMemory()/1024;//Java   虚拟机中的空闲内存量
        InfoTrace.getInstance().printInfo(DebugLevel.INFO,"Current Totoal Memory Size:"+totalMem+" KB，Current Free Memory Size:"+freeMem+" KB");        
    }
	
    static public String encryptSHA(String strMessage)
    {
      if (strMessage == null)
        return null;
      try
      {
        byte[] plainText = strMessage.getBytes();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        //开始使用算法
        messageDigest.update(plainText);
        return byte2hex(messageDigest.digest());
      }
      catch (Exception e)
      {
        e.printStackTrace();
        return strMessage;
      }
    }
    
    static public String byte2hex(byte[] b) //二行制转字符串
    {
      String hs = "";
      String stmp = "";
      for (int n = 0; n < b.length; n++)
      {
        stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
        if (stmp.length() == 1) 
            hs = hs + "0" + stmp;
        else 
            hs = hs + stmp;
      }
      return hs.toUpperCase();
    }
    
    public static byte[] hex2byte(String strhex)
    {
        if(strhex==null) 
            return null;
        int l = strhex.length();
     
        if(l %2 ==1) return null;
        
        byte[] b = new byte[l/2];
        for(int i = 0 ; i < l/2 ;i++)
        {
            b[i] = (byte)Integer.parseInt(strhex.substring(i *2,i*2 +2),16);
        }
        return b;
    }
    
    //Define type DES,DESede,Blowfish
    private static String Algorithm = "DES";   

    static
    {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
    }

    //generate dynamic key
    public static byte[] getKey() throws Exception
    {
        KeyGenerator keygen = KeyGenerator.getInstance(Algorithm);
        SecretKey deskey = keygen.generateKey();
        return deskey.getEncoded();
    }

    //Encrypt  
    public static byte[] encode(byte[] input, byte[] key) throws Exception
    {
        SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key, Algorithm);
        Cipher c1 = Cipher.getInstance(Algorithm);
        c1.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] cipherByte = c1.doFinal(input);
        return cipherByte;
    }

    //Decrypt
    public static byte[] decode(byte[] input, byte[] key) throws Exception
    {
        SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key, Algorithm);
        Cipher c1 = Cipher.getInstance(Algorithm);
        c1.init(Cipher.DECRYPT_MODE, deskey);
        byte[] clearByte = c1.doFinal(input);
        return clearByte;
    }

    //md5() digest
    public static byte[] md5(byte[] input) throws Exception
    {
        java.security.MessageDigest alg = java.security.MessageDigest.getInstance("MD5"); //or   "SHA-1"  
        alg.update(input);
        byte[] digest = alg.digest();
        return digest;
    }
    
    public static byte[] getStableKey()
    {
        byte[] result = {-80,93,-22,-104,-128,-23,-56,-32};
        
        return result;
    }
        
    public static String getRandomNumberString(int digit)
    {
        String result = "";
        
        for(int i=0; i<digit; i++)
        {
            result += (int)(Math.random()*10);
        }
        
        return result;
    }
	
	public static void main(String args[])
	{
//        System.out.println(CommonTools.encryptSHA("admin"));
//        System.out.println(CommonTools.encryptSHA("test"));
        
        try
        {
//            System.out.println(CommonTools.getSysDir());
//            System.out.println(CommonTools.getWebinfPath());
//            
//            Date oDate = CommonTools.getDate("2008-12-25 03:40:00");
//            System.out.println(oDate.getTime());
            
            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(formater.format((new Date(1232426700000L))));

//            int defaultOffsetTime = TimeZone.getDefault().getOffset(System.currentTimeMillis());
//            System.out.println(defaultOffsetTime);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
	}
}
