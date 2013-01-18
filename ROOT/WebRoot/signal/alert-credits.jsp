<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

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
 %>
 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>ClearTraders</title>
<link href="../css/forms.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div class="form-wrapper">
<h1>Alert Credits</h1>

<div class="clear"></div>
<h2>Current Alert Credit Balance</h2>

<%
if(userinfoBean.getSms_credits() > 0)
{
%>
  <div class="form-container-green">
    <div class="form-left"><img src="../images/tick_16.png" width="16" height="16" border="0" /></div>
    <div class="form-left">
      <h3><%=userinfoBean.getSms_credits()%></h3>
    </div>
    <div class="form-right-text">Email = 1 Alert Credit &amp; SMS = 2 Alert Credits</div>
    <div class="clear"></div>
  </div>  
<%}else{ %>

  <div class="form-container-red">
    <div class="form-left"><img src="../images/warning_16.png" width="16" height="16" border="0" /></div>
    <div class="form-left">
      <h3><%=userinfoBean.getSms_credits()%></h3>
    </div>
    <div class="form-right-text">You have no more Alert Credits!</div>
    <div class="clear"></div>
  </div>
  
<%} %>
    
  <h2>Alert Credit Pricing</h2>
  <form id="buyAlertCreditsForm"  name="buyAlertCreditsForm" action="../myaccount/buySMSCredits.do" method="post">
  	<input type="hidden" name="alert_credits_package" id="alert_credits_package" value="1" maxlength="50"></input>
  </form>
  
  <div class="form-container-green">
    <div class="form-right">
    	<a href="#" onclick="buyAlertCredits()"><img src="../images/add-alert-credits-green.gif" alt="Add Alert Credits" width="168" height="25" border="0" /></a>
    </div>
    
    <div class="form-left">
      <input onclick="selectCreditPackage(1,id)" type="checkbox" name="50credits" id="50credits" checked="true" />
    </div>
    <div class="form-left">
      <h4>50 = $10</h4>
      <a href="#"></a>
    </div>
    <div class="clear"></div>
    
    <div class="form-left">
      <input onclick="selectCreditPackage(2,id)" type="checkbox" name="250credits" id="250credits" />
    </div>
    <div class="form-left">
      <h4>250 = $40</h4>
      <a href="#"></a>
    </div>
    <div class="clear"></div>
    
    <div class="form-right">1 Email = 1 Alert Credit<br /> 1 SMS = 2 Alert Credits&nbsp; </div>
        
    <div class="form-left">
      <input onclick="selectCreditPackage(3,id)" type="checkbox" name="500credits" id="500credits" />
    </div>
    <div class="form-left">
      <h4>500 = $55</h4>
      <a href="#"></a>
    </div>
    <div class="clear"></div>
    
    <div class="form-left">
      <input onclick="selectCreditPackage(4,id)" type="checkbox" name="1000credits" id="1000credits" />
    </div>
    <div class="form-left">
      <h4>1000 = $90</h4>
      <a href="#"></a>
    </div>
    <div class="clear"></div>
    
    <div class="form-left">
      <input onclick="selectCreditPackage(5,id)" type="checkbox" name="5000credits" id="5000credits" />
    </div>
    <div class="form-left">
      <h4>5000 = $380</h4>
      <a href="#"></a>
    </div>
    <div class="clear"></div>
    
  </div>
  <div class="clear"></div>
</div>

<script type="text/javascript">

function selectCreditPackage(alertCreditPackageId,checkBoxId)
{	
	$("INPUT[type='checkbox']").attr('checked',false);
	
	//$(checkBoxId).attr('checked', true);
	document.getElementById(checkBoxId).checked=true;
	
	if($("#alert_credits_package"))
	{
		$("#alert_credits_package").val(alertCreditPackageId);
	}
}

function buyAlertCredits()
{
	if($("#alert_credits_package"))
	{
		$("#buyAlertCreditsForm").submit();
	}
}	

</script>

</body>
</html>
