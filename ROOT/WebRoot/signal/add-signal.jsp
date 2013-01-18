<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%@ page import="com.cleartraders.common.entity.StrategyBean" %>
<%@ page import="com.cleartraders.common.entity.MarketTypeBean" %>
<%@ page import="com.cleartraders.common.entity.MarketPeriodBean" %>
<%@ page import="com.cleartraders.webapp.WebConstants" %>
<%@ page import="com.cleartraders.common.define.CommonDefine" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>ClearTraders</title>
<link href="css/forms.css" rel="stylesheet" type="text/css" />
</head>

<%

    if(null == session || null == session.getAttribute(WebConstants.USER_KEY) )
	{
		response.sendRedirect("../login/userLogin.jsp");
		return;
	}
	
	List<MarketTypeBean> allMarketType = (List<MarketTypeBean>)session.getAttribute(WebConstants.ALL_MARKET_TYPE);
	if(null == allMarketType)
	{
		allMarketType = new ArrayList<MarketTypeBean>();
	}
	
	List<MarketPeriodBean> allMarketPeriod = (List<MarketPeriodBean>)session.getAttribute(WebConstants.ALL_MARTKET_PERIOD);
	if(null == allMarketPeriod)
	{
		allMarketPeriod = new ArrayList<MarketPeriodBean>();
	}
	
    Map<Long, StrategyBean> allStrategyInfo = (Map<Long, StrategyBean>)session.getAttribute(WebConstants.ALL_STRATEGY_INFO);
    if(null == allStrategyInfo)
    {
    	allStrategyInfo = new HashMap<Long, StrategyBean>();
    }
    
%>

<body>
<form id="addSignal" name="addSignal" action="../signal/addSignal.do" method="post">
<div class="form-wrapper">
<h1>Add Signal</h1>
<h2><strong>1</strong> Choose Market</h2>
<div class="form-container">
    <div class="form-left"><img src="../images/globe_32.png" alt="Choose Market" width="32" height="32" border="0" /></div>
    <div class="form-left">
      <select name="market_id" id="market_id">
        <%
          for(int i=0; i< allMarketType.size(); i++)        
          {          
          		MarketTypeBean marketBean = allMarketType.get(i);
        		%>
        		<option value="<%=marketBean.getId()%>"><%=marketBean.getDisplay_name()%></option>        	
        <%} %>
      </select>
    </div>
    <div class="clear"></div>
  </div>
  <h2><strong>2</strong> Choose Time-Frame</h2>
  <div class="form-container">
    <div class="form-left"><img src="../images/diagram_32.png" alt="Choose Time-Frame" width="32" height="32" border="0" /></div>
    <div class="form-left">
      <select name="time_frame_id" id="time_frame_id">
         <%
          for(int i=0; i< allMarketPeriod.size(); i++)        
          {          
          	MarketPeriodBean marketPeriodBean = allMarketPeriod.get(i);
        	%>
        	<option value="<%=marketPeriodBean.getId()%>"><%=marketPeriodBean.getValue() + " " + marketPeriodBean.getPeriod_name()%> Chart</option>
         <%} %>
      </select>
    </div>
    <div class="form-right-text">(Lower Time = More Signals)</div>
    <div class="clear"></div>
  </div>
  <h2><strong>3</strong> Choose Indicator</h2>
  <div class="form-container">
    <div class="form-left"><img src="../images/info_32.png" alt="Choose Indicator" width="32" height="32" border="0" /></div>
    <div class="form-left">
      <select name="indicator_id" id="indicator_id">
        <%
        
        Iterator oIt = allStrategyInfo.values().iterator();
        while(oIt.hasNext())
        {
        	StrategyBean strategy = (StrategyBean)oIt.next();    
        	    	
        	if(strategy.getActive() != 1)
        	{
        		continue;
        	}
        %>
        <option value="<%=strategy.getId()%>"><%=strategy.getCommon_name()%></option>
        <%} %>
      </select>
    </div>
    <div class="clear"></div>
  </div>
  <h2><strong>4</strong> Choose Alert</h2>
  <div class="form-container">
    <div class="form-left"> <img src="../images/signal-30-icon.gif" alt="Choose Alert" width="29" height="28" /></div>
    <div class="form-left">
      <select name="alert_type" id="alert_type">
        <option value="<%=CommonDefine.ALERT_BY_SMS%>" selected>SMS</option>
        <option value="<%=CommonDefine.ALERT_BY_EMAIL%>">Email</option>
        <option value="<%=CommonDefine.NO_ALERT%>">NONE</option>
      </select>
    </div>
    <div class="form-right-text">0 alert credits</div>
    <div class="form-right-text"><img src="../images/warning_16.png" width="16" height="16" border="0" /></div>
    <div class="clear"></div>
    <div class="line"><img src="../images/grey-line.gif" width="100%" height="2" /></div>
    <div class="form-right-text"> Receive Alerts via Email &amp; SMS</div>
    <div class="form-left"><a href="#"><img src="../images/add-alert-credits.gif" onclick="addAlertCredits()" alt="Add Alert Credits" width="169" height="25" border="0" /></a></div>
    <div class="clear"></div>
  
  </div>
<a class="form-button" href="#" onclick="submitAddSignal()"><span>add signal</span></a>
<div class="clear"></div>
</div>
</form>

<script type="text/javascript">

	function submitAddSignal()
	{
		$("#addSignal").ajaxSubmit(function(response){			
			$.facebox.close(); 
			refreshSignal();
		});			
	}
	
</script>

</body>
</html>
