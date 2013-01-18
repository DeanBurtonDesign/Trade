<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>ClearTraders</title>
<link href="css/cleartraders-v1.css" rel="stylesheet" type="text/css" />
<link href="css/forms.css" rel="stylesheet" type="text/css" />
 <script src="js/jquery.js" type="text/javascript"></script>
<link href="css/facebox.css" media="screen" rel="stylesheet" type="text/css"/>

</head>
<body>

<form id="login_form" action="/login/userLogin.do" method="post" >
<div class="wrapper">
  <div class="main-login">
  <div class="header-login">
      <div class="logo-login"><a href="http://www.cleartraders.com"><img src="images/cleartraders-logo.gif"  border="0" alt="ClearTraders" width="182" height="28" /></a></div>
    </div>
    <div class="login">
    <div class="form-container-green">
    <div class="form-wide">
      <h3>Account activated</h3>
    </div>
    <div class="line"><img src="images/green-line.gif" width="100%" height="2" /></div>
    <div class="form-left-text">
      <h4>Congratulations your account has been successfully activated. Enter your email and password below to login.</h4>
    </div>
    <div class="clear"></div>
  </div>
    <div class="form-wrapper">
  <h1>Login</h1>
  <h2>Email Address</h2><div id="email_errorinfo" class="errorinfo"><html:errors property="username"/></div>
  <div class="form-container">
    <div class="form-left">
     <input name="username" type="text" id="username" size="40" />
    </div>
    <div class="clear"></div>
    
  </div>
  <h2>Password</h2><div id="password_errorinfo" class="errorinfo"><html:errors property="password"/></div>
  <div class="form-container">
    <div class="form-left">
      <input name="password" type="password" id="password" size="40" />
    </div>
    <div class="clear"></div>
    
  </div>
  <div style="float:left; margin-top:30px">Forgotten your password? <a href="http://signals.cleartraders.com/get-password.jsp">Click here</a></div>
  <a class="form-button" href="#" onclick="document.getElementById('login_form').submit();"><span>login</span></a>
  <div class="clear"></div>
</div>
    
    </div>
 
 
 </div>

</div>
</form>
<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
try {
var pageTracker = _gat._getTracker("UA-5290585-1");
pageTracker._trackPageview();
} catch(err) {}</script>
</body>
</html>
