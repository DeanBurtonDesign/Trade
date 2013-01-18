<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>ClearTraders</title>
<link href="css/cleartraders-v1.css" rel="stylesheet" type="text/css" />
<link href="css/forms.css" rel="stylesheet" type="text/css" />
 
</head>
<body>

<form id="enquiry_form" action="./enquiryAction.do" method="post" >
<div class="wrapper">
  <div class="main-login">
  <div class="header-login">
      <div class="logo-login"><a href="http://www.cleartraders.com"><img src="images/cleartraders-logo.gif" border="0" alt="ClearTraders" width="182" height="28" /></a></div>
    </div>
    <div class="login">
    <div class="form-wrapper">
  <h1>Contact Us</h1>
  
  <input type="hidden" name="enquiry_type" id="enquiry_type" value="2" maxlength="50"></input>
  
  <h2>Your First Name</h2>
  <span class="errorinfo"><html:errors property="first_name"/></span>
  <div class="form-container">
    <div class="form-left">
     <input name="first_name" type="text" id="first_name" size="30" />
    </div>
    <div class="clear"></div>
  </div>
  
  <h2>Your Last Name</h2>
  <span class="errorinfo"><html:errors property="last_name"/></span>
  <div class="form-container">
    <div class="form-left">
     <input name="last_name" type="text" id="last_name" size="30" />
    </div>
    <div class="clear"></div>
  </div>
  
  <h2>Your Email Address</h2>
  <span class="errorinfo"><html:errors property="email"/></span>
  <div class="form-container">
    <div class="form-left">
     <input name="email" type="text" id="email" size="30" />
    </div>
    <div class="clear"></div>
    
  </div>
  <h2>Your message</h2>
  <span class="errorinfo"><html:errors property="enquiry"/></span>
  <div class="form-container">
    <div class="form-left">
      <textarea name="enquiry" id="enquiry" cols="40" rows="5"></textarea>      
    </div>
    <div class="clear"></div>
    
  </div>
  <h2>Retype Security Code</h2>
  <div class="form-container">
    <div class="form-left">
    	<img border=0 src="./image.jsp"/><input name="security_code" type="text" id="security_code" size="4" maxlength="4" />
    </div>
    <div class="clear"></div>    
  </div>
  <a class="form-button" href="#" onclick="document.getElementById('enquiry_form').submit();"><span>Send message</span></a>
  <div class="clear"></div>
</div>
</div>
</div>
</div>
</form>
</body>
</html>
