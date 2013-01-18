<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Clear Traders: Login</title>
<script src="../../js/common.js" type="text/javascript" ></script>
<link href="../../css/admin-login.css" type="text/css" rel="stylesheet" />
</head>

<body>
<html:form action="/login/adminLogin">
	<div class="container">
		<div class="head">
			<p><a href="http://www.cleartraders.com"><img src="../../images/login-logo.png" border="0" /></a></p>
	  </div>
		<div class="main">	
			<p class="menber">Admin Login </p>
			
      		<div id="email_errorinfo" class="errorinfo-active"><html:errors property="username"/></div>
      		<div id="password_errorinfo" class="errorinfo-active"><html:errors property="password"/></div>
      		<div id="error_info_prompt" class="errorinfo-active"><html:errors property="error"/></div>
      		      		   		
			<p><span>&nbsp;Email</span><input type="text" name="username" id="username" style="width:150px;height:18px;"/></p>
			<p><span>&nbsp;Password</span><input type="password" name="password" id="password" style="width:150px; height:18px;"/></p>

		</div>
		<div id="tc">
			<p>
				<span>Forgotten your password <a href="http://www.cleartraders.com/get-password.jsp">Click here</a></span>&nbsp; &nbsp;
				<input id="login_button" name="saveImage" type="image" src="../../images/login-login.png" width="80" height="32" />
			</p>
	  </div>
	</div>
</html:form>
</body>
</html>
