<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="java.util.List" %>

<%@ page import="com.cleartraders.common.db.DataCache" %>
<%@ page import="com.cleartraders.common.entity.CountryBean" %>
<%@ page import="com.cleartraders.common.entity.TimeZoneBean" %>
<%@ page import="com.cleartraders.common.entity.ProductBean" %>

<%
	
	String productTypeString = (String)request.getParameter("productType");
	int productType = 1;
	if(null == productTypeString)
	{
		//free trial type
		productType = 1;
	}
	else
	{
		productType = Integer.parseInt(productTypeString);
	}
		
	
	String firstName = (String)request.getParameter("firstName");
	if(null == firstName)
	{
		firstName = "";
	}
	
	String secondName = (String)request.getParameter("secondName");
	if(null == secondName)
	{
		secondName = "";
	}
	
	String email = (String)request.getParameter("email");
	if(null == email)
	{
		email = "";
	}
	
	String errorRequest = "";
	if(null != request.getAttribute("error"))
	{
		errorRequest = (String)request.getAttribute("error");
	}
	
	String mobileNumber = (String)request.getParameter("mobileNumber");
	if(null == mobileNumber)
	{
		mobileNumber = "";
	}	
	
	//get all memeber level (products)
	List<ProductBean> allProducts = DataCache.getInstance().getAllProduct();
	
	//get all country
	List<CountryBean> allCountry = DataCache.getInstance().getAllCountry();
		
	boolean isFreeTrial = false;
   	for(int i=0; i<allProducts.size(); i++)
   	{
   		ProductBean productBean = allProducts.get(i);
   		if(productBean.getId() == productType && productBean.getPaid() == 1)
   		{
   			isFreeTrial = true;
   			break;
   		}
   	}
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Trading Strategies & Trading Systems @ Clear Traders</title>
<link href="css/cleartraders-v1.css" rel="stylesheet" type="text/css" />
<link href="css/forms.css" rel="stylesheet" type="text/css" />
 <script src="js/jquery.js" type="text/javascript"></script>
<link href="css/facebox.css" media="screen" rel="stylesheet" type="text/css"/>
<script src="js/facebox/facebox.js" type="text/javascript"></script>

  <script type="text/javascript">
    jQuery(document).ready(function($) {
      $('a[rel*=facebox]').facebox({
        loading_image : 'loading.gif',
        close_image   : 'closelabel.gif'
      }) 
    })
  </script>

</head>
<body>

<form id="signup_form" action="/userRegisterAction.do" method="post" >
<input type="hidden" id="timeZoneID" name="timeZoneID" value="1" />
<div class="wrapper">
  <div class="main-login">
  <div class="header-login">
      <div class="logo-login"><a href="http://www.cleartraders.com"><img src="images/cleartraders-logo.gif" border="0" alt="ClearTraders" width="182" height="28" /></a></div>
    </div>
    <div class="login">
    <div class="form-wrapper">
  <h1>Registration</h1>
  <h2>First Name</h2><span class="errorinfo"><html:errors property="firstname"/></span>
  <div class="form-container">
    <div class="form-left">
     <input name="firstName" type="text" id="firstName" size="40" />
    </div>
    <div class="clear"></div>
    
  </div>
  <h2>Last Name</h2><span class="errorinfo"><html:errors property="secondname"/></span>
  <div class="form-container">
    <div class="form-left">
      <input name="secondName" type="text" id="secondName" size="40" />
    </div>
    <div class="clear"></div>
    
  </div>
  
    <h2>Email Address (Username)</h2><span class="errorinfo"><html:errors property="email"/></span>
    <div class="form-container">
    <div class="form-left">
      <input name="email" type="text" id="email" size="40" />
    </div>
    <div class="clear"></div>
    
  </div>
  
    <h2>Cell / Mobile Phone Number (used for signals only)</h2><span class="errorinfo"><html:errors property="mobilenumber"/></span>
    <div class="form-container">
    <div class="form-left">
      <input name="mobileNumber" type="text" id="mobileNumber" size="40" />
    </div>
    <div class="clear"></div>
    
  </div>
  
      <h2>Create Your Password</h2><span class="errorinfo"><html:errors property="password"/></span>
      <div class="form-container">
    <div class="form-left">
      <input name="password" type="password" id="password" size="40" />
    </div>
    <div class="clear"></div>
    
  </div>
  <h2>Re-type Your Password to Confirm</h2><span class="errorinfo"><html:errors property="confirmpassword"/></span>
      <div class="form-container">
    <div class="form-left">
      <input name="confirmpassword" type="password" id="confirmpassword" size="40" />
    </div>
    <div class="clear"></div>
    
  </div>
  
   <h2>Country</h2><span class="errorinfo"><html:errors property="countryid"/></span>
      <div class="form-container">
    <div class="form-left">
      <label>
      <select name="countryID" id="countryID">
      	<%
    	for(int i=0; i<allCountry.size(); i++)
    	{
    		CountryBean countryBean = allCountry.get(i);
    		
    		if(countryBean.getName().equalsIgnoreCase("United States of America"))
    		{
     	%>
     	<option selected value="<%=countryBean.getId()%>"><%=countryBean.getName()%></option>
    	<%  }
    		else
    		{
    	%>
     	<option value="<%=countryBean.getId()%>"><%=countryBean.getName()%></option>
    	<%  }
    	}%>
      </select>
      </label>
    </div>
    <div class="clear"></div>    
  </div>
  
  <br/>  
  <div class="form-container">
        <div class="form-left">
	  		<label id="subscribe_prompt_info" style="width:310px;color:#FF0000;">        
			</label> 
		</div>
		<br />
        <div class="form-left"><input name="" type="checkbox" value="" id="agree_checkbox" onclick="checkAgreement();" /></div>        
        <div class="form-left" style="width:310px;">
			I have read and agree to the <a href="http://www.cleartraders.com/service-agreement/" target="_blank">Service Agreement</a> & <a href="http://www.cleartraders.com/privacy-policy/" target="_blank">Privacy Policy</a>.        
		</div>        
        <div class="clear"></div>    
  </div>
  
  <h2>Re-type Security Code</h2><span class="errorinfo"><html:errors property="securitycode"/></span>
  <div class="form-container">
        <div class="form-left">
        	 <img border=0 src="./image.jsp"/><input name="securityCode" type="text" id="securityCode" size="4" maxlength="4" />
        </div>
        <div class="clear">
     </div>    
  </div>
   
  <a class="form-button" href="#" onclick="submitSignup()"><span>Signup</span></a>
  <div class="clear"></div>
</div>
    
    </div>
 
 
 </div>

</div>
</form>

<script type="text/javascript">

var agreedAgreement = false;

function submitSignup()
{
	if(agreedAgreement)
	{
		document.getElementById('signup_form').submit();
	}
	else
	{
		setErrorPromptInfo();
	}
}

function checkAgreement()
{
	var checked = document.getElementById("agree_checkbox").checked;
	if(checked)
	{	
		agreedAgreement = true;
		
		setRightPromptInfo();
	}
	else
	{
		agreedAgreement = false;
	}
}

function setErrorPromptInfo()
{
	if(isIE())
	{
		document.getElementById("subscribe_prompt_info").innerText="You must agree to the service agreement and privacy policy.";
	}
	else
	{
		document.getElementById("subscribe_prompt_info").textContent="You must agree to the service agreement and privacy policy.";		
	}
}

function setRightPromptInfo()
{
	if(isIE())
	{
		document.getElementById("subscribe_prompt_info").innerText="";
	}
	else
	{
		document.getElementById("subscribe_prompt_info").textContent="";		
	}
}

function isIE()
{ 
	if (window.navigator.userAgent.toLowerCase().indexOf("msie")>=1) 
		return true; 
	else 
		return false; 
} 
</script>

</body>
</html>
