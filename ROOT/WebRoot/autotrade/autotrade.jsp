<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cleartraders.webapp.model.bean.AutoTradeInfoBean" %>
<%@ page import="com.cleartraders.webapp.WebConstants" %>

<%
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	response.setHeader("Cache-Control", "private"); // HTTP 1.1 
	response.setHeader("Cache-Control", "no-store"); // HTTP 1.1 
	response.setHeader("Cache-Control", "max-stale=0"); // HTTP 1.1 

		
	if(null == session || null == session.getAttribute(WebConstants.USER_KEY) )
	{
		response.sendRedirect("../login/userLogin.jsp");
		return;
	}
	
	session.setAttribute("current_page","autotrade");
	
	AutoTradeInfoBean oCurrentTradeInfo = (AutoTradeInfoBean)request.getAttribute("user_auto_trade_info");
	
 %>
 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Clear Traders: Auto-Trade</title>
<link href="../css/cleartraders-app.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="box">
		<div class="box1">&nbsp;
				<div class="box2">
						<div class="content" id="Account_body">
						<!-- Top -->
						<jsp:include flush="true" page="../include/top.jsp"></jsp:include>	
						<div class="clear"></div>
						
						<div class="content_left">
						<span class="title"></span>			
							<jsp:include flush="true" page="../include/left_1.jsp"></jsp:include>					
						<span class="title"></span>					
							<jsp:include flush="true" page="../include/left_2.jsp"></jsp:include>	
						</div>
						<div class="content_right">
								<div class="content_R_title">
										<h2>Auto Trade</h2>
										<img src="../images/dot_.gif" width="3" height="21" class="learnmore_btn" />
										<img src="../images/learnmor_btn.gif" width="110" height="20" class="learnmore_btn"/> 
										<Span class="learnmore_btn">Description text here to be supplied Description text here to be</Span>
								</div>
								
								<div class="clear"></div>
								
								<div class="contactContent">
									<div class="tablebg">
									  <div id="tabDateCondition" class="tabboard">					  	
									  	<div class="tabcol6">
									  		<ul>
												<li><p>Current Auto Trade Status:</p></li>
												<%
						                		if(oCurrentTradeInfo.isStarted())
						                		{
						                		 %>
												<li><p style="color:#008400;">Open</p></li>
												<%
												}
												else
						                		{
						                		%>
						                		<li><p style="color:#E00000;">Close</p></li>
						                		<%}%>
											</ul>
									  		<ul>
												<li><p>Start Trade:</p></li>
												<html:form action="/autotrade/changeAutoTradeStatus">
												<input id="0" type="hidden" name="operation_type" value="0" />
												<li><input name="changeImage" type="image" src="../images/bt_change.gif"></li>
												</html:form>
											</ul>
											<ul>
												<li><p>Stop Trade:</p></li>
												<html:form action="/autotrade/changeAutoTradeStatus">
												<input id="1" type="hidden" name="operation_type" value="1" />
												<li><input name="changeImage" type="image" src="../images/bt_change.gif"></li>
												</html:form>
											</ul>									
										</div>	
									  </div>		
									</div>
								</div>
								<div class="clear"></div>
								<div class="content_right_foot">&nbsp;</div>
						</div>
						<div class="clear"></div>
						</div>
				</div>
		</div>
		<div class="clear"></div>
		<jsp:include flush="true" page="../include/bottom.jsp"></jsp:include>
</div>
<div class="body_foot"></div>
<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>

<script type="text/javascript">
try {
var pageTracker = _gat._getTracker("UA-5290585-2");
pageTracker._trackPageview();
} catch(err) {}
</script>
</body>
</html>
