<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ page import="com.cleartraders.common.entity.UserBean" %>
<%@ page import="com.cleartraders.common.define.CommonDefine" %>
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
	
	session.setAttribute("current_page","myaccount");
	
	UserBean userinfoBean = (UserBean)session.getAttribute(WebConstants.USER_KEY);
	
	int currentType = -1;
	if(request.getParameter("currentType") != null)
	{
		currentType = Integer.parseInt(request.getParameter("currentType"));
	}
	
	long signalId = -1;
	if(request.getParameter("signalId") != null)
	{
		signalId = Long.parseLong(request.getParameter("signalId"));
	}
	
	long marketId = -1;
	if(request.getParameter("marketId") != null)
	{
		marketId = Long.parseLong(request.getParameter("marketId"));
	}
	
	long periodId = -1;
	if(request.getParameter("periodId") != null)
	{
		periodId = Long.parseLong(request.getParameter("periodId"));
	}
	
	long strategyId = -1;
	if(request.getParameter("strategyId") != null)
	{
		strategyId = Long.parseLong(request.getParameter("strategyId"));
	}
	
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>ClearTraders</title>
<link href="../css/forms.css" rel="stylesheet" type="text/css" />
</head>

<body>

<form id="changeAlertForm"  name="changeAlertForm" action="../myaccount/changeAlert.do" method="post">

<input type="hidden" name="market_id" id="market_id" value="<%=marketId%>" maxlength="50"></input>
<input type="hidden" name="period_id" id="period_id" value="<%=periodId%>"></input>
<input type="hidden" name="strategy_id" id="strategy_id" value="<%=strategyId%>"></input>
<input type="hidden" name="alert_type" id="alert_type" value="<%=currentType%>"></input>

<div class="form-wrapper">
<h1>Change Alert</h1>
<div class="clear"></div>
<div class="form-container">
  <div class="form-left"> <img src="../images/signal-30-icon.gif" alt="Choose Market" width="29" height="28" /></div>
  	  <div class="form-left">
	      <select name="new_alert_type" id="new_alert_type">
			<%if(currentType==CommonDefine.ALERT_BY_SMS){ %>
	        <option value="<%=CommonDefine.ALERT_BY_SMS%>" selected><%=CommonDefine.ALERT_BY_SMS_NAME %></option><%} else {%>
	        <option value="<%=CommonDefine.ALERT_BY_SMS%>"><%=CommonDefine.ALERT_BY_SMS_NAME %></option><%} %>
	        
	        <%if(currentType==CommonDefine.ALERT_BY_EMAIL){ %>
	        <option value="<%=CommonDefine.ALERT_BY_EMAIL%>" selected><%=CommonDefine.ALERT_BY_EMAIL_NAME %></option><%} else {%>
	        <option value="<%=CommonDefine.ALERT_BY_EMAIL%>"><%=CommonDefine.ALERT_BY_EMAIL_NAME %></option><%} %>
	        
	        <%if(currentType==CommonDefine.NO_ALERT){ %>
	        <option value="<%=CommonDefine.NO_ALERT%>" selected><%=CommonDefine.NO_ALERT_NAME %></option><%} else {%>
	        <option value="<%=CommonDefine.NO_ALERT%>"><%=CommonDefine.NO_ALERT_NAME %></option><%} %>
	      </select>
      </div>
      <div class="form-right-text"><%=userinfoBean.getSms_credits()%> alert credits</div>
        <%if(userinfoBean.getSms_credits() > 0){ %>
	    <div class="form-right-text"><img src="../images/tick_16.png" width="16" height="16" border="0" /></div>
	    <%}else{ %>
	    <div class="form-right-text"><img src="../images/warning_16.png" width="16" height="16" border="0" /></div>
	    <%} %>
	    <div class="clear"></div>
	    <div class="line"><img src="../images/grey-line.gif" width="100%" height="2" /></div>
	    <div class="form-left-text">Increase Your Alert Credits</div>
	    <div class="form-right"><a href="#" onclick="addAlertCredits()"><img src="../images/add-alert-credits.gif" alt="Add Alert Credits" width="169" height="25" border="0" /></a></div>
	    <div class="clear">
      </div>      
   </div>
   <a class="form-button" href="#" onclick="changeAlertTypeInDialog()"><span>save</span></a>
   <div class="clear"></div>
</div>
</form>

<script type="text/javascript">
	var currentAlertType = <%=currentType%>;
	var signalId = <%=signalId%>;
		
	$(function(){
       var options = {};
       $('#changeAlertForm').ajaxForm(options);        
     });  
      
	function changeAlertTypeInDialog()
	{		
		var newAlertType = $('#new_alert_type').val();
		$("#alert_type").val(newAlertType);
		
		if(currentAlertType == newAlertType)
		{
			$.facebox.close(); 
		}
		else
		{
			//change alert type through Ajax
			$("#changeAlertForm").ajaxSubmit(function(response){
				$.facebox.close(); 
				changeAlertType(signalId, newAlertType);
		    });
		}
	}
</script>

</body>
</html>
