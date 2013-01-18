/******************************************************************************
*
*
*			IQ_JNI
*			IQ_32.java
*
*			Proprietary Software Product
*
*
*			Market Data Co.
*		9110 West Dodge Road Suite 200
*			Omaha, Nebraska 68114
*
*
*
*	Copyright (c) Market Data Co. 2002
*			All Rights Reserved
*
*******************************************************************************
*
*	Module Descr.:	Interface to iq_jni.dll
*		   Author:	Matthew J. Battey
*	  Modified by:
*
******************************************************************************/

/*********************************
*     Revision History            
**********************************/
//$Archive:   S:\InterQuote\sys097\archives\iqf_iq_jni\com\iqfeed\IQ_32.java-arc  $
//$Author:   vkodipel  $
//$Date:   Mar 17 2004 15:37:34  $
//$Modtime:   Jul 18 2002 09:21:16  $
//$Revision:   1.0  $
//$Workfile:   IQ_32.java  $
//$Log:   S:\InterQuote\sys097\archives\iqf_iq_jni\com\iqfeed\IQ_32.java-arc  $
//
//   Rev 1.0   Mar 17 2004 15:37:34   vkodipel
//Initial revision.
//
//   Rev 1.2   18 Jul 2002 10:25:14   mbattey
//Added QA Headers


package com.iqfeed;

public abstract class IQ_32 {
	
	public native void RegisterClientApp(String appname, String version, String key);
	public native void RemoveClientApp();
	public native void SetAutoLogin(String username, String password);
	public native void CleartAutoLogin();

	abstract public void IQConnectStatus(int a, int b);

	static {
		System.loadLibrary("iq_jni");
	}
}
