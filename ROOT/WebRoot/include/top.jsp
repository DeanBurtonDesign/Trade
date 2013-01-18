<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="com.cleartraders.common.entity.UserBean" %>
<%@ page import="com.cleartraders.webapp.WebConstants" %>

<link href="../css/cleartraders-app.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
<!--
function updateStyle(obj)
{
	if(obj != null)
	{
		obj.styleClass = "over";
	}
}
//-->
</script>

<%
	String current_page="signal_page";
	
	if(null != session && null != session.getAttribute("current_page") )
	{
		current_page = (String)session.getAttribute("current_page");		
	}
		
	long userID=0;
	if(null != session && null != session.getAttribute(WebConstants.USER_KEY) )
	{
		UserBean currentUserInfo = (UserBean)session.getAttribute(WebConstants.USER_KEY);	
		userID = currentUserInfo.getId();
	}
 %>
 
<div id="Logo"><img src="../images/logo.gif" width="199" height="51" /></div>
<div style="height:30px;">
	<div class="logo_right">
		<span class="logoff"><html:link forward="logoff"><img src="../images/nav1_trans.gif" width="73" height="20" /></html:link></span>
		<span class="contact"><a href="http://www.cleartraders.com/contact-us.jsp" target="_blank"><img src="../images/nav1_trans.gif" width="93" height="20" /></a></span>
	</div>
</div>
<div class="menu">
	<ul>
		<%if(current_page.endsWith("myaccount")){ %>
		<li><html:link forward="getStrategy" property="charts_name" onclick="updateStyle(this)"><img src="../images/my_account_on.gif" width="144" height="30" /></html:link></li><%}else{%>
		<li class="mayaccount"><html:link forward="getStrategy" property="charts_name" onclick="updateStyle(this)"><img src="../images/nav1_trans.gif" width="144" height="30" /></html:link></li><%}%>
		
		<%if(current_page.endsWith("analysis")){ %>
		<li><html:link forward="getAnalysis" property="analysis_name" onclick="updateStyle(this)"><img src="../images/anaiysis_on.gif" width="121" height="30" /></html:link></li><%}else{%>
		<li class="analysis"><html:link forward="getAnalysis" property="analysis_name" onclick="updateStyle(this)"><img src="../images/nav1_trans.gif" width="121" height="30" /></html:link></li><%}%>
		
		<%if(current_page.endsWith("signal_page")){ %>
		<li><html:link forward="signalList" property="signal_name" onclick="updateStyle(this)"><img src="../images/signals_on.gif" width="121" height="30" /></html:link></li><%}else{%>
		<li class="signal_page"><html:link forward="signalList" property="signal_name" onclick="updateStyle(this)"><img src="../images/nav1_trans.gif" width="121" height="30" /></html:link></li><%}%>
		
		<%if(current_page.endsWith("charts_page")){ %>
		<li><html:link forward="getAllCharts" property="charts_name" styleClass="over" onclick="updateStyle(this)"><img src="../images/charts_on.gif" width="121" height="30" /></html:link></li><%}else{%>
		<li class="charts_page"><html:link forward="getAllCharts" property="charts_name" onclick="updateStyle(this)"><img src="../images/nav1_trans.gif" width="121" height="30" /></html:link></li><%}%>
		
		<%if(userID == WebConstants.AUTO_TRADE_USER_ID){
      	if(current_page.endsWith("autotrade")) { %>
     	<li><html:link forward="getAutoTrade" property="autotrade_name" onclick="updateStyle(this)"><img src="../images/auto_trade_over.gif" width="144" height="30" border="0" /></html:link></li><%}else{%>
	  	<li><html:link forward="getAutoTrade" property="autotrade_name" onclick="updateStyle(this)"><img src="../images/auto_trade.gif" width="144" height="30" border="0" /></html:link></li><%}}%>
            
	</ul>
</div>