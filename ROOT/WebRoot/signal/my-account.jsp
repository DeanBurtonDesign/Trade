<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.cleartraders.common.entity.UserBean" %>
<%@ page import="com.cleartraders.webapp.WebConstants" %>
<%@ page import="com.cleartraders.common.entity.TimeZoneBean" %>
<%@ page import="com.cleartraders.common.entity.ProductBean" %>

<%
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	response.setHeader("Cache-Control", "private"); // HTTP 1.1 
	response.setHeader("Cache-Control", "no-store"); // HTTP 1.1 
	response.setHeader("Cache-Control", "max-stale=0"); // HTTP 1.1 

		
	if(null == session || null == session.getAttribute(WebConstants.USER_KEY) )
	{
		response.sendRedirect("login/userLogin.jsp");
		return;
	}
	
	session.setAttribute("current_page","myaccount");
	
	UserBean userinfoBean = (UserBean)session.getAttribute(WebConstants.USER_KEY);
	
	TimeZoneBean oCurrentTimeZoneBean = (TimeZoneBean)request.getAttribute("user_trading_time_zone");
	
	ProductBean currentProductBean = (ProductBean)request.getAttribute(WebConstants.MY_MEMBER_SHIP);
	if(null == currentProductBean)
	{
		currentProductBean = new ProductBean();
	}
	
 %>
 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>ClearTraders</title>
<link href="../css/forms.css" rel="stylesheet" type="text/css" />
</head>

<style type="text/css">
		.validateTips { border: 1px solid transparent; padding: 0.3em; }	
		.ui-state-error { padding: .3em; }	
		.ui-state-highlight {border: 1px solid #EF9D9C; color: #363636; }
</style>
	
<body>
<div class="form-wrapper">
<h1>My Account</h1>
<h2 class="validateTips"></h2>
<h2>Account Type</h2>

<div class="form-container-green">
	<div class="form-wide">
			<h3>Free Forever</h3>
    </div>
	<!-- div class="line"><img src="../images/green-line.gif" width="100%" height="2" /></div>
	<div class="form-left">Buy more &amp;   you'll save more!</div>
	<div class="form-right"><a href="#"><img src="../images/upgrade-account.gif" alt="Add Alert Credits" width="153" height="25" border="0" /></a></div>
	<div class="clear"></div-->
</div>

<form id="updateAccount" name="updateAccount" action="../myaccount/updateContacts.do" method="post">
<h2>Email &amp; SMS Alerts</h2>

<div class="form-container">
	<div class="form-wide">Email Address (Username)<br />
		<label><input type="text" name="email" id="email" size="40" value="${userinfo.email}" maxlength="50" /></label>
	</div>
	<div class="form-left">Mobile Number<br />
		<label><input type="text" name="mobile" id="mobile" value="${userinfo.mobile}" maxlength="50"/></label>
	</div>
	<div class="clear"> Only used for sending SMS Text Alerts. </div>
	<div class="clear"></div>
</div>

<h2>Personal Details</h2>
<div class="form-container">
  <div class="form-left">First Name<br />
		<label><input type="text" name="first_name" id="first_name" value="${userinfo.first_name}" maxlength="20" /></label>
  </div>
  <div class="form-right">Last Name<br />
		<label><input type="text" name="last_name" id="last_name" value="${userinfo.last_name}" maxlength="20" /></label>
  </div>
  <div class="clear"></div>
  <div class="form-wide">Country<br />
		<select id="country" name="country" property="country">
        	<logic:iterate id="countrybean" name="all_country_data" type="com.cleartraders.common.entity.CountryBean" indexId="indexID">
				<%if(countrybean.getId() == userinfoBean.getCountry_id()){ %>
				<option id="country_option" value="${countrybean.id}" text="${countrybean.id}" selected="1">${countrybean.name}</option><%}else{ %>
				<option id="country_option" value="${countrybean.id}" text="${countrybean.id}">${countrybean.name}</option><%}%>
			</logic:iterate>
        </select>
  </div>
  <input type="hidden" id="time_zone" name="time_zone" value="1" />
  <div class="clear"></div>
</div>

<h2>Change Password</h2>
<div class="form-container">
	<div class="form-left">New Password<br />
		<label><input type="password" name="password" id="password" maxlength="50" /></label>
	</div>
	<div class="form-right">Confirm New Password<br />
		<label><input type="password" name="confirmpassword" id="confirmpassword" maxlength="50" /></label>
	</div>
	<div class="clear"></div>
	<div class="line"><img src="../images/grey-line.gif" width="100%" height="2" /></div>
	<div class="form-wide">A password confirmation email will be sent to your email address.<br />
	</div>
</div>

<a class="form-button" href="#" onclick="submitMyAccountInfo()" ><span>save</span></a>
<div class="clear"></div>
</form>
</div>

<script type="text/javascript">
	        
	$(function(){
        var options = {};
        $('#updateAccount').ajaxForm(options);        
      });   
    			
	function submitMyAccountInfo()
	{
		if(checkAllField())
		{
		  $("#updateAccount").ajaxSubmit(function(response){
				$.facebox.close(); 
				refreshSignal();
		    });
	  	}
	}
	
	function updateTips(t) {
		$(".validateTips").text(t).addClass('ui-state-highlight');
	}
	
	function checkEqual(o1,n1,o2,n2) { 
	    if(o1.val() != o2.val()) {
	    	o1.addClass('ui-state-error');
	    	updateTips(n1 + " must be equal to "+n2);
	    	o1.focus();
	    	
	    	return false;
	    }
	    else{
	    	return true;
	    }
	}
	
	function checkLength(o,n,min,max) {

		if ( o.val().length > max || o.val().length < min ) {
			o.addClass('ui-state-error');
			updateTips("Length of " + n + " must be between "+min+" and "+max+".");
			o.focus();
			return false;
		} else {
			return true;
		}

	}

	function checkRegexp(o,regexp,n) {

		if ( !( regexp.test( o.val() ) ) ) {
			o.addClass('ui-state-error');
			updateTips(n);
			o.focus();
			return false;
		} else {
			return true;
		}

	}
	
	function checkAllField()
	{
		var bValid = true;
		
		bValid = bValid && checkLength($("#email"),"email",1,50);
		bValid = bValid && checkLength($("#mobile"),"mobile",1,50);
		bValid = bValid && checkLength($("#first_name"),"First Name",1,20);
		bValid = bValid && checkLength($("#last_name"),"Last Name",1,20);
		
		if($("#password").val().length > 0 || $("#confirmpassword").val().length > 0)
		{
			bValid = bValid && checkLength($("#password"),"password",1,50);
			bValid = bValid && checkLength($("#confirmpassword"),"confirmpassword",1,50);
			
			bValid = bValid && checkEqual($("#password"),"password",$("#confirmpassword"),"confirmpassword");
		}
		
		bValid = bValid && checkRegexp($("#first_name"),/^[a-z]([0-9a-z_\. ])+$/i,"Username may consist of a-z, 0-9, underscores, begin with a letter.");
		bValid = bValid && checkRegexp($("#last_name"),/^[a-z]([0-9a-z_\. ])+$/i,"Username may consist of a-z, 0-9, underscores, begin with a letter.");
		bValid = bValid && checkRegexp($("#email"),/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i,"Email format isn't right. Reference example, user@cleartraders.com");
	
		return bValid;		
	}
	
	function isValidEmail(str) 
	{
		return (str.indexOf(".") > 2) && (str.indexOf("@") > 0); 
	}
	
</script>
</body>
</html>
