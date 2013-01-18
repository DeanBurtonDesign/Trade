<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%@ page import="com.cleartraders.common.entity.StrategyBean" %>
<%@ page import="com.cleartraders.common.entity.MarketTypeBean" %>
<%@ page import="com.cleartraders.common.entity.MarketPeriodBean" %>
<%@ page import="com.cleartraders.common.entity.UserBean" %>
<%@ page import="com.cleartraders.webapp.WebConstants" %>
<%@ page import="com.cleartraders.common.define.CommonDefine" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>ClearTraders</title>
<link href="../css/forms.css" rel="stylesheet" type="text/css" />
</head>

<%
    if(null == session || null == session.getAttribute(WebConstants.USER_KEY) )
	{
		response.sendRedirect("../login/userLogin.jsp");
		return;
	}
	
	UserBean userinfoBean = (UserBean)session.getAttribute(WebConstants.USER_KEY);
	
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
	
	int alertType = -1;
	if(request.getParameter("alertType") != null)
	{
		alertType = Integer.parseInt(request.getParameter("alertType"));
	}
	
	String subscriptionId = "";
	if(request.getParameter("subscriptionId") != null)
	{
		subscriptionId = request.getParameter("subscriptionId");
	}
	
%>

<body>
<form id="delSignal" name="delSignal" action="../signal/delSignal.do" method="post">
	<input type="hidden" name="subscription_id" id="subscription_id" value="<%=subscriptionId%>" maxlength="50"></input>
</form>

<form id="editSignal" name="editSignal" action="../signal/editSignal.do" method="post">
<input type="hidden" name="subscription_id" id="subscription_id" value="<%=subscriptionId%>" maxlength="50"></input>
<div class="form-wrapper">
<h1>Edit Signal</h1>
<h2>Change Market</h2>
<div class="form-container">
    <div class="form-left"><img src="../images/globe_32.png" alt="Choose Market" width="32" height="32" border="0" /></div>
    <div class="form-left">
      <select name="market_id" id="market_id">
        <%
          for(int i=0; i< allMarketType.size(); i++)        
          {          
          		MarketTypeBean marketBean = allMarketType.get(i);
          		
          		if(marketBean.getId() == marketId)
          		{
        		%>
        			<option value="<%=marketBean.getId()%>" selected><%=marketBean.getDisplay_name()%></option>        	
         <%     }
                else
                {
         %>
                	<option value="<%=marketBean.getId()%>"><%=marketBean.getDisplay_name()%></option> 
         <%
                }
          }
         %>
      </select>
    </div>
    <div class="clear"></div>
  </div>
  <h2>Change Time-Frame</h2>
  <div class="form-container">
    <div class="form-left"> <img src="../images/diagram_32.png" alt="Choose Market" width="32" height="32" border="0" /></div>
    <div class="form-left">
      <select name="time_frame_id" id="time_frame_id">
         <%
          for(int i=0; i< allMarketPeriod.size(); i++)        
          {          
          	MarketPeriodBean marketPeriodBean = allMarketPeriod.get(i);
          	
          	if(marketPeriodBean.getId() == periodId)
          	{
        	%>
        	<option value="<%=marketPeriodBean.getId()%>" selected><%=marketPeriodBean.getValue() + " " + marketPeriodBean.getPeriod_name()%> Chart</option>
         <%
            }
            else
            {
         %>
         	<option value="<%=marketPeriodBean.getId()%>"><%=marketPeriodBean.getValue() + " " + marketPeriodBean.getPeriod_name()%> Chart</option>
         <%
            }
          } 
         %>
      </select>
    </div>
    <div class="clear"></div>
  </div>
  <h2>Change Indicator</h2>
  <div class="form-container">
    <div class="form-left"><img src="../images/info_32.png" alt="Choose Market" width="32" height="32" border="0" /></div>
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
        	
        	if(strategy.getId() == strategyId)
        	{       	
        %>
        		<option value="<%=strategy.getId()%>" selected><%=strategy.getCommon_name()%></option>
        <%
        	}
        	else
        	{
        %>
        		<option value="<%=strategy.getId()%>"><%=strategy.getCommon_name()%></option>
        <%
        	}        
        } 
        %>
      </select>
    </div>
    <div class="clear"></div>
  </div>
  <h2>Change Alert</h2>
  <div class="form-container">
    <div class="form-left"> <img src="../images/signal-30-icon.gif" alt="Choose Market" width="29" height="28" /></div>
    <div class="form-left">
      <select name="alert_type" id="alert_type">
      
        <%if(alertType == CommonDefine.ALERT_BY_SMS){ %>
        <option value="<%=CommonDefine.ALERT_BY_SMS%>" selected>SMS</option>
        <%}else{ %>
        <option value="<%=CommonDefine.ALERT_BY_SMS%>">SMS</option>
        <%} %>
        
        <%if(alertType == CommonDefine.ALERT_BY_EMAIL){ %>
        <option value="<%=CommonDefine.ALERT_BY_EMAIL%>" selected>Email</option>
        <%}else{ %>
        <option value="<%=CommonDefine.ALERT_BY_EMAIL%>">Email</option>
        <%} %>
        
        <%if(alertType == CommonDefine.NO_ALERT){ %>
        <option value="<%=CommonDefine.NO_ALERT%>" selected>NONE</option>
        <%}else{ %>
        <option value="<%=CommonDefine.NO_ALERT%>">NONE</option>
        <%} %>
        
      </select>
    </div>
    <%if(userinfoBean.getSms_credits() > 0) {%>
    <div class="form-right-text"> <img src="../images/tick_16.png" width="16" height="16" border="0" /></div>
    <%}else{ %>
    <div class="form-right-text"> <img src="../images/warning_16.png" width="16" height="16" border="0" /></div>
    <%} %>
    <div class="form-right-text"> <%=userinfoBean.getSms_credits()%> alert credits</div>
    <div class="clear"></div>
    <div class="line"><img src="../images/grey-line.gif" width="100%" height="2" /></div>
    <div class="form-left-text">Receive Alerts via Email &amp; SMS</div>
    <div class="form-right"><a href="#" onclick="addAlertCredits()"><img src="../images/add-alert-credits.gif" alt="Add Alert Credits" width="169" height="25" border="0" /></a></div>
    <div class="clear"></div>
  </div>
  <a class="form-button" href="#" onclick="submitEditSignal()"><span>save</span></a> <a class="form-button" href="#" onclick="submitDelSignal()"><span>delete signal</span></a>
<div class="clear"></div>
</div>
</form>

<script type="text/javascript">

	function submitDelSignal()
	{
		$("#delSignal").ajaxSubmit(function(response){			
			$.facebox.close(); 
			refreshSignal();
		});
	}
	
	function submitEditSignal()
	{
		$("#editSignal").ajaxSubmit(function(response){			
			$.facebox.close(); 
			refreshSignal();
		});		
	}
	
</script>

</body>
</html>
