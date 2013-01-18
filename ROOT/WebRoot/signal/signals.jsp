<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>

<%@ page import="com.cleartraders.common.entity.UserBean" %>
<%@ page import="com.cleartraders.common.entity.Signal" %>
<%@ page import="com.cleartraders.common.entity.StrategyBean" %>
<%@ page import="com.cleartraders.common.entity.UserSignalPreferenceBaseBean" %>
<%@ page import="com.cleartraders.common.entity.MarketTypeBean" %>
<%@ page import="com.cleartraders.common.entity.MarketPeriodBean" %>
<%@ page import="com.cleartraders.webapp.WebConstants" %>
<%@ page import="com.cleartraders.common.define.CommonDefine" %>

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
	
	//all latest subscribled signals
	List<Signal> oAllSignalsList = (ArrayList<Signal>)request.getAttribute(WebConstants.ALL_ACTIVE_SIGNAL);
	
	//set all signal ID string, to calculate pasted time
	String allSubscriptionIdString = "'";
	String allSubscriptionGenDateString = "'";
	for(Signal signalBean : oAllSignalsList)
	{
		String signalId = signalBean.getStrategy_id()+":"+signalBean.getMarket_type_id()+":"+signalBean.getSignal_period();
		
		allSubscriptionIdString += signalId;
		allSubscriptionIdString += ";";
		
		allSubscriptionGenDateString += (signalId + "," + signalBean.getGenerate_date());
		allSubscriptionGenDateString += ";";
	}
	
	allSubscriptionIdString += "'";
	allSubscriptionGenDateString += "'";
	
	//my subscription list
	Map<String, UserSignalPreferenceBaseBean> oMySignalSubscriptionList = (Map<String, UserSignalPreferenceBaseBean>)request.getAttribute(WebConstants.ALL_MY_ACTIVE_SIGNAL_MAP);
	
	//all market type
	List<MarketTypeBean> oAllMarketInfo = (ArrayList<MarketTypeBean>)request.getAttribute(WebConstants.ALL_MARKET_TYPE);
	session.setAttribute(WebConstants.ALL_MARKET_TYPE,oAllMarketInfo);
	
	//all market period
	List<MarketPeriodBean> oAllMarketPeriod = (ArrayList<MarketPeriodBean>)request.getAttribute(WebConstants.ALL_MARTKET_PERIOD);
	session.setAttribute(WebConstants.ALL_MARTKET_PERIOD,oAllMarketPeriod);
	
	//all strategy
	Map<Long, StrategyBean> allStrategyInfo = (Map<Long, StrategyBean>)request.getAttribute(WebConstants.ALL_STRATEGY_INFO);
	session.setAttribute(WebConstants.ALL_STRATEGY_INFO,allStrategyInfo);
		
	//sort type
	int currentSignalSortType = 1;	
	if(null != request.getAttribute(WebConstants.SIGNAL_SORT_TYPE))
	{
		currentSignalSortType = ((Integer)request.getAttribute(WebConstants.SIGNAL_SORT_TYPE)).intValue();
	}
	
 %>
 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>ClearTraders</title>

<script src="../js/jquery.js" type="text/javascript"></script>
<script src="../js/jquery.form.js" type="text/javascript"></script>
<script src="../facebox/facebox.js" type="text/javascript"></script>
<script src="../js/common.js" type="text/javascript" ></script>
<script src="../js/swfobject.js" type="text/javascript" ></script>
<script src="../ls/lscommons.js"></script>
<script src="../ls/lspushpage.js"></script>

<link href="../css/cleartraders-v1.css" rel="stylesheet" type="text/css" />
<link href="../css/forms.css" rel="stylesheet" type="text/css" />
<link href="../css/facebox.css" media="screen" rel="stylesheet" type="text/css"/>
</head>
<body onload="pageOnload()">

<div class="wrapper">
  <div class="main">
    <div class="header">
      <div class="logo"><a href="http://www.cleartraders.com"><img src="../images/cleartraders-logo.gif"  border="0"  alt="ClearTraders" /></a></div>
      <div class="nav">
      	<html:link forward="logoff" styleClass="button-logout"><span>logout</span></html:link> 
      	<a class="button" href="../myaccount/getContacts.do" rel="facebox"><span>
      		<logic:present name="userinfo" scope="session"><bean:write name="userinfo" property="first_name" /></logic:present>
			<strong>free</strong></span> 		
      	</a> 
      	<a class="button" id="alertcreditslink" href="../myaccount/getSmsCredits.do" rel="facebox">
      		<%if(userinfoBean.getSms_credits() > 0){ %>
      		<span>alert credits <strong class="credits"><%=userinfoBean.getSms_credits()%></strong></span>
      		<%}else{ %>
      		<span>alert credits <strong class="credits-negtive"><%=userinfoBean.getSms_credits()%></strong></span>
      		<%} %>
      	</a> 
      	<!-- a class="button-broker" href="#">
      		<span>broker rewards</span>
      	</a --> 
      </div>
    </div>
    <div class="container">
      <div class="title">
        <h1>Signals</h1>
        <!-- h2>This is the description text </h2-->
        <!-- div class="container-help"><a href="#"><img src="../images/help_32.png" alt="HELP" width="32" height="32" border="0" /></a></div-->
        <div class="container-sort">Group Signals by:
          <select id="sort_type" name="Sort Signals:" onchange="sortSignalsFun()">
          
            <% if(currentSignalSortType == CommonDefine.SORT_BY_LATEST){ %>
            <option value="<%=CommonDefine.SORT_BY_LATEST%>" selected>Latest</option><%}else{ %>
            <option value="<%=CommonDefine.SORT_BY_LATEST%>">Latest</option><%} %>
            
            <% if(currentSignalSortType == CommonDefine.SORT_BY_OLDEST){ %>
            <option value="<%=CommonDefine.SORT_BY_OLDEST%>" selected>Oldest</option><%}else{ %>
            <option value="<%=CommonDefine.SORT_BY_OLDEST%>">Oldest</option><%} %>
            
            <% if(currentSignalSortType == CommonDefine.SORT_BY_MARKET){ %>
            <option value="<%=CommonDefine.SORT_BY_MARKET%>" selected>A-Z Markets</option><%}else{ %>
            <option value="<%=CommonDefine.SORT_BY_MARKET%>">A-Z Markets</option><%} %>
                        
            <% if(currentSignalSortType == CommonDefine.SORT_BY_INDICATOR){ %>
            <option value="<%=CommonDefine.SORT_BY_INDICATOR%>" selected>A-Z Indicators</option><%}else{ %>
            <option value="<%=CommonDefine.SORT_BY_INDICATOR%>">A-Z Indicators</option><%} %>
            
          </select>
        </div>
      </div>
      <div class="clear"></div>
      
      <form id="sort_signals_form"  name="sort_signals_form" action="../signal/sortSignals.do" method="post" style="margin: 0px; padding: 0px;" >
			<input type="hidden" id="sort_type_id" name="sort_type_id" value="1" />
      </form>
      
      <div id="all_signals">
      <logic:iterate id="signal" name="all_active_signals" type="com.cleartraders.common.entity.Signal" indexId="count"> 
      <%
      		StrategyBean relatedStrategyBean = allStrategyInfo.get(signal.getStrategy_id());
      		      		
      		long signalId = signal.getId();
      		String alertTypeId = signalId+"alerttype";
      		
      		String subscribleId = signal.getStrategy_id()+":"+signal.getMarket_type_id()+":"+signal.getSignal_period();
      		
      		//time out id
      		String signalTimeId = subscribleId+"time";
      		
      		//signal price id
      		String signalPriceId = subscribleId+"price";
      		
      		//signal image id
      		String signalImageId = subscribleId+"image";
      		      		
      		UserSignalPreferenceBaseBean oSubscription = oMySignalSubscriptionList.get(subscribleId);
      		
      		int currentType = CommonDefine.NO_ALERT;
      		String currentTypeName = CommonDefine.NO_ALERT_NAME;
      		
      		if(oSubscription != null)
      		{
      			if(oSubscription.getEnable_email())
      			{
      				currentType = CommonDefine.ALERT_BY_EMAIL;
      				currentTypeName = CommonDefine.ALERT_BY_EMAIL_NAME;
      			}
      			else if(oSubscription.getEnable_sms())
      			{
      				currentType = CommonDefine.ALERT_BY_SMS;
      				currentTypeName = CommonDefine.ALERT_BY_SMS_NAME;
      			}
      		}      		
      		
      %>
      <div class="signal" id="<%=subscribleId%>">
        <div class="signal-direction">
        	<logic:equal name="signal" property="signal_type" value="4">
        		<img id="<%=signalImageId%>" src="../images/signal-long-time-out.gif" alt="Long" width="80" height="100" />
        	</logic:equal>
        	<logic:equal name="signal" property="signal_type" value="5">
        		<img id="<%=signalImageId%>" src="../images/signal-short-time-out.gif" alt="Short" width="80" height="100" />
        	</logic:equal>
        	<logic:equal name="signal" property="signal_type" value="6">
        		<img id="<%=signalImageId%>" src="../images/signal-close-time-out.gif" alt="Close" width="80" height="100" />
        	</logic:equal>
        </div>
        <div class="signal-info-title">
          <li><img src="../images/globe_16.png" alt="Market" />Market</li>
          <li><img src="../images/label_16.png" alt="Market" />Signal Price</li>
          <li><img src="../images/clock_16.png" alt="Market" />Signal Time</li>
          <li><img src="../images/diagram_16.png" alt="Market" />Time-Frame</li>
        </div>
        <div class="long-signal-info">
          <li>${signal.market_name}</li>
          <li id="<%=signalPriceId%>">${signal.signalValue}</li>
          <li><span id="<%=signalTimeId%>"> -- </span> Ago</li>
          <li>${signal.signal_period_name} chart</li>
        </div>
        
        <div class="clear"></div>   
             
        <logic:equal name="signal" property="signal_type" value="4">
        	<div class="long-indicator">
        </logic:equal>
        
        <logic:equal name="signal" property="signal_type" value="5">
        	<div class="short-indicator">
        </logic:equal>
        
        <logic:equal name="signal" property="signal_type" value="6">
        	<div class="close-indicator">
        </logic:equal>
        	<img src="../images/info_16.png" alt="Market" />
        	<strong><%=relatedStrategyBean.getCommon_name()%></strong>
        	<%=relatedStrategyBean.getDescription()%>
        </div>        
        	        
        <div class="signal-buttons"> 
          <a class="signal-button-alert" href="../signal/change-alert.jsp?signalId=<%=signalId%>&currentType=<%=currentType%>&marketId=<%=signal.getMarket_type_id()%>&periodId=<%= signal.getSignal_period() %>&strategyId=<%= signal.getStrategy_id() %>" rel="facebox"><span id="<%=alertTypeId%>" ><%=currentTypeName%></span></a> 
          <a class="signal-button-chart" href="../signal/chart.jsp?subscriptionId=<%=subscribleId%>&signalId=<%=signalId%>&marketId=<%=signal.getMarket_type_id()%>&marketName=<%=signal.getMarket_topic_name()%>&marketDisplayName=<%= signal.getMarket_name() %>&signalPrice=<%= signal.getSignalValue() %>&signalGenerateTime=<%= signal.getGenerate_date() %>&periodId=<%=signal.getSignal_period() %>&periodMinute=<%=signal.getSignal_period_minutes() %>&strategyId=<%= signal.getStrategy_id() %>" rel="facebox"><span>chart</span></a> 
          <a class="signal-button" href="../signal/edit-signal.jsp?subscriptionId=<%=subscribleId%>&marketId=<%=signal.getMarket_type_id()%>&periodId=<%=signal.getSignal_period() %>&strategyId=<%= signal.getStrategy_id() %>&alertType=<%=currentType%>" rel="facebox"><span>edit</span></a>
          <div class="clear"></div>
        </div>
        <div class="clear"></div>        
      </div>
      </logic:iterate>
      </div>
      
      <div class="add-signal">
      	<div class="add-signal-button"><a href="../signal/add-signal.jsp" rel="facebox" >Add Signal</a></div>
      </div>
      <div class="clear"></div>			                
			                
    </div>
    <!-- div class="banner-container-left"></div><div class="banner-container-right"></div -->
    <div class="clear"></div>
 <div class="footer-links-left"><a href="http://www.cleartraders.com/privacy-policy/" target="_blank">Privacy Policy</a>  |  <a href="http://www.cleartraders.com/service-agreement/" target="_blank">Service Agreement</a>  |  <a href="http://signals.cleartraders.com/contact-us.jsp" target="_blank">Contact Us</a>  |  Copyright 2010 Clear Traders. All Rights Reserved</div><div class="footer-links-right"><img src="../images/logo-footer.gif" alt="Trusted Signals - ClearTraders" /></div>
 </div>
 


</div>

<script type="text/javascript">
		
	$(function(){
        hideFaceboxButton();        
      });
      
    jQuery(document).ready(function($) {
    
      $('a[rel*=facebox]').facebox({
        loading_image : '../images/loading.gif',
        close_image   : '../images/closelabel.gif'
      }) 
      
    })
    
    window.onbeforeunload = hideFaceboxButton;
    
    function hideFaceboxButton()
    {
    	$(".signal-buttons").hide();
        $(".add-signal").hide();  
    }
    
    function showFaceboxButton()
    {
    	$(".signal-buttons").show();
        $(".add-signal").show(); 
    }
    
    function addAlertCredits()
    {
    	$("#alertcreditslink").click();
    }
    
    function refreshSignal()
    {
    	sortSignalsFun();
    }
    
    function sortSignalsFun()
    {    	
    	var sortTypeValue = $("#sort_type").val();
    	    	
        $("#sort_type_id").val(sortTypeValue);
                
        $("#sort_signals_form").submit();    	
    }
        
    function changeAlertType(signalId, newAlertType)
    {
    	sortSignalsFun();
    }
    
    function pageOnload()
  	{
  		showFaceboxButton(); 
  		initSignalGenTimeMap();
		countdownSec();
		subscribeJSSignal('jssignal');
  	}
  	
  	  	
  	///////////////////// For Signal pasted time ///////////////////////  	
  	var allSubscriptionIdString = <%=allSubscriptionIdString%>; 
  	var allSubscriptionGenDateString = <%=allSubscriptionGenDateString%>; 
  	var signalGenDateMap = new Map();
  	
  	function Map()
  	{
	   this.map = new Object();
	   this.length = 0;
	  
	   this.size = function()
	   {
	       return this.length;
	   }
	  
	   this.put = function(key, value)
	   { 
	       if( !this.map['_' + key])
	       {
	          this.length = this.length + 1;
	       }
	     
	       this.map['_' + key] = value;	    
	   }
	  
	   this.remove = function(key)
	   {
	      if(this.map['_' + key])
	      {
	          this.length = this.length - 1;
	          return delete this.map['_' + key];
	      }
	      else
	      {
	          return false;
	      }
	   }
	  
	   this.containsKey = function(key)
	   {
	      return this.map['_' + key] ? true:false;	  
	   }
	   
	   this.get = function(key)
	   {   
	      return this.map['_' + key] ? this.map['_' + key]:null;	 
	   }
	
	   this.inspect=function()
	   {   
	     var str = '';
	    
	     for(var each in this.map)
	     {
	          str+= '\n'+ each + '  Value:'+ this.map[each];
	     }	    
	     return str;
	   }	    
	}

  	function initSignalGenTimeMap()
  	{
  		var signalInfoList = allSubscriptionGenDateString.split(";");
  		
  		for(var i=0; i<signalInfoList.length; i++)
  		{  		
  			if(signalInfoList[i].split(",").length == 2)
  			{
  				var signalSubscribledId = signalInfoList[i].split(",")[0];
  				var signalGenDate = signalInfoList[i].split(",")[1];
  				
  				signalGenDateMap.put(signalSubscribledId, signalGenDate);
  			}
  		}
  	}
  	
  	function countdownSec()
  	{  			
  	  	//get all current signals timeout fields  			
  		var signalList = allSubscriptionIdString.split(";");
  		var latestAcitveSignalListString = "";

  		for(var i=0; i<signalList.length; i++)
  		{
  			//alert(signalList[i]);
  			var timeOutItemID = signalList[i]+"time";
  			var timeOutItem = document.getElementById(timeOutItemID);
  			//alert(timeOutItem);
  			if(timeOutItem != null )
  			{
  				var pastedTimeString = getPastedTimeString(signalList[i]);
  				
	  			if(isIE())
	  			{
	  				timeOutItem.innerText = pastedTimeString;
	  			}
	  			else
	  			{
	  				timeOutItem.textContent = pastedTimeString;
	  			}  	
  			}
  		}
  		
  		setTimeout('countdownSec()',1000);
  	}	
  	
  	function getPastedTimeString(signalSubId)
  	{
 		var signalGenDate = signalGenDateMap.get(signalSubId);
 		
 		var currentDate = new Date().getTime();
 		
 		//to prevent strange date
 		if(signalGenDate > currentDate)
 		{
 			signalGenDate = currentDate;
 			signalGenDateMap.remove(signalSubId);
			signalGenDateMap.put(signalSubId, signalGenDate);
 		}
 		 		
 		var signalPastedSeconds = Math.round(Math.abs(currentDate - signalGenDate) / 1000);
 		 		
 		if( signalPastedSeconds < 60 )
 		{
 			return signalPastedSeconds + " Sec";
 		}
 		else if(signalPastedSeconds < 60 * 60)
 		{
 			return Math.round(signalPastedSeconds / 60) + " Min";
 		}
 		else if(signalPastedSeconds < 60 * 60 * 24)
 		{
 			return Math.round(signalPastedSeconds / (60*60)) + " Hour";
 		}
 		else if(signalPastedSeconds < 60 * 60 * 24 * 30)
 		{
 			return Math.round(signalPastedSeconds / (60*60*24)) + " Days";
 		}
 		else if(signalPastedSeconds < 60 * 60 * 24 * 30 * 12)
 		{
 			return Math.round(signalPastedSeconds / (60*60*24*30)) + " Month";
 		}
 		else
 		{
 			return Math.round(signalPastedSeconds / (60*60*24*30*12)) + " Year";
 		}
  	}
  	
  	///////////////////// For Subscrible JS Signal /////////////////////
  	var schema = "signal_id signal_type market_type_id signal_period generate_date signal_value expire_date signal_rate direction profit checked system_name market_name market_topic_name generate_date_string expire_date_string profitString signal_period_minutes strategy_id";	
		
	var lsPage = new PushPage();	
	lsPage.context.setDebugAlertsOnClientError(false);
	lsPage.context.setRemoteAlertsOnClientError(false);
	lsPage.context.setDomain("cleartraders.com");
	lsPage.onEngineCreation = startEngine;
	lsPage.bind();
	lsPage.createEngine("SLEngine", "../ls/");
	
	function startEngine(eng) 
	{
		eng.policy.setMaxBandwidth(30);
		eng.policy.setIdleTimeout(30000);
		eng.policy.setPollingInterval(1000);
		eng.connection.setLSHost("push.cleartraders.com");
		eng.connection.setLSPort(8090);
		eng.connection.setAdapterName("JSSIGNAL");
		eng.changeStatus("STREAMING");
	}
	
	function subscribeJSSignal(jssignal)
	{
		if (jssignal == "") 
		{
			return;
		}
		
		var newTable = new NonVisualTable(jssignal,schema, "DISTINCT");
		
		newTable.setSnapshotRequired(true);
		newTable.setRequestedMaxFrequency(0.5);
		newTable.onItemUpdate = onSignalItemUpdate;
		
		lsPage.addTable(newTable, jssignal);
	}
	
	function onSignalItemUpdate(itemPos,updateInfo,itemName) 
	{	
		if(updateInfo == null)
		{
			return;
		}
		
		var signal_id =updateInfo.getNewValue(new FieldPositionDescriptor(1));
		var signal_type =updateInfo.getNewValue(new FieldPositionDescriptor(2));
		var market_type_id =updateInfo.getNewValue(new FieldPositionDescriptor(3));
		var signal_period =updateInfo.getNewValue(new FieldPositionDescriptor(4));
		var generate_date =updateInfo.getNewValue(new FieldPositionDescriptor(5));
		var signal_value =updateInfo.getNewValue(new FieldPositionDescriptor(6));
		var expire_date =updateInfo.getNewValue(new FieldPositionDescriptor(7));
		
		var signal_rate =updateInfo.getNewValue(new FieldPositionDescriptor(8));
		var direction =updateInfo.getNewValue(new FieldPositionDescriptor(9));
		var profit =updateInfo.getNewValue(new FieldPositionDescriptor(10));
		var checked =updateInfo.getNewValue(new FieldPositionDescriptor(11));
		var system_name =updateInfo.getNewValue(new FieldPositionDescriptor(12));
		     
		var market_name =updateInfo.getNewValue(new FieldPositionDescriptor(13));
		var market_topic_name =updateInfo.getNewValue(new FieldPositionDescriptor(14));
		var generate_date_string =updateInfo.getNewValue(new FieldPositionDescriptor(15));
		var expire_date_string =updateInfo.getNewValue(new FieldPositionDescriptor(16));
		var profitString =updateInfo.getNewValue(new FieldPositionDescriptor(17));
		
		var signal_period_minutes = updateInfo.getNewValue(new FieldPositionDescriptor(18));
		var strategy_id = updateInfo.getNewValue(new FieldPositionDescriptor(19));
		
		//add or update signal
		addOrUpdateSignal(signal_id,market_name,signal_type,signal_value,expire_date,expire_date_string,profit,system_name,signal_period,market_topic_name,market_type_id,strategy_id,signal_period_minutes);
		
	}
		
	function addOrUpdateSignal(signalID,market_name,signal_type,signal_value,expire_date,expire_date_string,signal_profit,system_name,signal_period,market_topic_name,market_type_id,strategy_id,signal_period_minutes)
	{				    
		var signalSubId = strategy_id+":"+market_type_id+":"+signal_period;
		
		//alert('gest new signal:'+signalSubId+" "+signal_type);
		
		if(document.getElementById(signalSubId) != null)
		{
			//signal already here, update it
			
			//1) update Signal Price
			var signalPriceId = signalSubId+"price";
			if(document.getElementById(signalSubId) != null)
			{
				if(isIE())
	  			{
	  				document.getElementById(signalPriceId).innerText = signal_value;
	  			}
	  			else
	  			{
	  				document.getElementById(signalPriceId).textContent = signal_value;
	  			}
			}
			
			//2) update signal generated time in order to update the pasted time
			signalGenDateMap.remove(signalSubId);
			signalGenDateMap.put(signalSubId, new Date().getTime());
			
			//3) update signal type (image)
			var signalImageId = signalSubId+"image";
			var signalImageObj = document.getElementById(signalImageId);
		    
		    if( signalImageObj != null )
			{			        
				if(signal_type == 4)
				{
					signalImageObj.setAttribute("src","../images/signal-long-time-out.gif");
				}
				else if(signal_type == 5)
				{
					signalImageObj.setAttribute("src","../images/signal-short-time-out.gif");
				}
				else if(signal_type == 6)
				{
					signalImageObj.setAttribute("src","../images/signal-close-time-out.gif");
				}
			}
			
			//re-order the signals
			refreshSignal();
		}
	}
  	
</script>

<script type="text/javascript">
  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-5290585-2']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();
</script>

</body>
</html>
