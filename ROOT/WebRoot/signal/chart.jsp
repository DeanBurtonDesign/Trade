<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.cleartraders.common.entity.UserBean" %>
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
	String currentUserName = "'"+userinfoBean.getLogin_name()+"'";
	String currentUserPWD = "'"+userinfoBean.getPwd()+"'";
	
	String signalSubId = "";
	if(request.getParameter("subscriptionId") != null)
	{
		signalSubId = "'" + request.getParameter("subscriptionId") + "'" ;
	}
	
	String signalChartTimeId = request.getParameter("subscriptionId")+"charttime";
	
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
	
	String marketName="";
	if(request.getParameter("marketName") != null)
	{
		marketName = request.getParameter("marketName");
	}
	String marketNameForJs = "'"+marketName+"'";
	
	String marketDisplayName="";
	if(request.getParameter("marketDisplayName") != null)
	{
		marketDisplayName = request.getParameter("marketDisplayName");
	}
	
	String marketDisplayNameForJs = "'"+marketDisplayName+"'";
	
	double signalPrice=0;
	if(request.getParameter("signalPrice") != null)
	{
		signalPrice = Double.parseDouble(request.getParameter("signalPrice"));
	}
	
	long signalGenerateTime=0;
	if(request.getParameter("signalGenerateTime") != null)
	{
		signalGenerateTime = Long.parseLong(request.getParameter("signalGenerateTime"));
	}
	
	long periodId = -1;
	if(request.getParameter("periodId") != null)
	{
		periodId = Long.parseLong(request.getParameter("periodId"));
	}
	
	long periodMinute = -1;
	if(request.getParameter("periodMinute") != null)
	{
		periodMinute = Long.parseLong(request.getParameter("periodMinute"));
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

<div class="chart-wrapper">

  <div class="chart-info">
    <div class="icon"><img src="../images/globe_32.png" alt="Market" width="32" height="32" /></div>
    <div class="text">
      <div class="top">Market</div>
      <div class="text">
        <div class="bottom"><%=marketDisplayName%></div>
      </div>
    </div>
  </div>
  
  <div class="chart-info">
    <div class="icon"><img src="../images/label_32.png" alt="Market" width="32" height="32" /></div>
    <div class="text">
      <div class="top">Signal Price</div>
      <div class="text">
        <div class="bottom"><%=signalPrice%></div>
      </div>
    </div>
  </div>
  
  <div class="chart-info">
    <div class="icon"><img src="../images/clock_32.png" alt="Market" width="32" height="32" /></div>
    <div class="text">
      <div class="top">Signal Time</div>
      <div class="text">
        <div class="bottom"><span id="<%=signalChartTimeId%>"></span> Ago</div>
      </div>
    </div>
  </div>
  
  <div class="chart-info">
    <div class="icon"><img src="../images/diagram_32.png" alt="Market" /></div>
    <div class="text">
      <div class="top">Time-Frame</div>
      <div class="text">
        <div class="bottom"><%=periodMinute%> Min chart</div>
      </div>
    </div>
  </div>
    
  <div class="chart-flash">
     <div display="none">
	     <li id="<%= "flash" + signalId %>" style="display:none"></li>
     </div>
   </div>
  
  </div>
  
  <div class="clear"></div>
  <br />
  
	<script type="text/javascript">
	
		var currentUsername = <%=currentUserName%>;
		var currentUserpassword = <%=currentUserPWD%>;
		var signalId = <%=signalId%>;
		var periodID = <%=periodId%>;
		var market_name = <%=marketNameForJs%>;
		var market_display_name = <%=marketDisplayNameForJs%>;
		var market_type_id = <%=marketId%>;
		var strategy_id = <%=strategyId%>;
		var signalSubId = <%=signalSubId%>;
				
		jQuery(document).ready(function($) {
			addFlashChart();
			freshSignalTime();
	    });
	    
	    function freshSignalTime()
	    {	    	
	    	var timeOutItemID = signalSubId+"charttime";
  			var timeOutItem = document.getElementById(timeOutItemID);
  			  			
  			if(timeOutItem != null )
  			{
  				var pastedTimeString = getPastedTimeString(signalSubId);
  				
	  			if(isIE())
	  			{
	  				timeOutItem.innerText = pastedTimeString;
	  			}
	  			else
	  			{
	  				timeOutItem.textContent = pastedTimeString;
	  			}  	
  			}
  			
  			setTimeout('freshSignalTime()',1000);
	    }
	    
	    //call from flash
	  	function changeChartHeight(flashObjID, newHeight)
	  	{
	  		//alert(flashObjID + " " + newHeight);
	  		var flashPlayer = document.getElementById(flashObjID);
	  		
	  		if(flashPlayer != null)
	  		{
	  			flashPlayer.setAttribute('height',newHeight);
	  		}
	  	}
	    
	    function addFlashChart()
		{				
			 var flashObjID = "flash"+signalId;
			 		 
			 var flashvars = {};  
			 flashvars.username = currentUsername;
			 flashvars.userpassword = currentUserpassword;
			 flashvars.period_minute = periodID;
			 flashvars.market_name = market_name;
			 flashvars.market_display_name = market_display_name;
			 flashvars.market_type_id = market_type_id;
			 flashvars.strategy_id = strategy_id;
			 flashvars.object_id = flashObjID;
			 		 
			 var params = {};  
			 params.username = currentUsername;
			 params.userpassword = currentUserpassword;
			 params.period_minute = periodID;
			 params.market_name = market_name;
			 params.market_display_name = market_display_name;
			 params.market_type_id = market_type_id;
			 params.strategy_id = strategy_id;
			 params.object_id = flashObjID;
			 		 
			 var attributes = {  
			 	 id:flashObjID,
			     classid: "clsid:D27CDB6E-AE6D-11cf-96B8-444553540000",  
			     quality: "high",
			     bgcolor: "#ffffff",
			     allowScriptAccess: "sameDomain",
			     align: "middle",
			     play: "true",
			     loop: "false"
			 };  
			 			 
			 swfobject.embedSWF("../flash/OnlineChart.swf", flashObjID, "640", "418", "9.0.0","#FFFFFF", flashvars, params, attributes);	
		}
				
	</script>
</body>
</html>

