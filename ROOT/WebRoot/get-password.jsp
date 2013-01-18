<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Clear Traders: Get Password</title>

<link href="css/cleartraders-v1.css" rel="stylesheet" type="text/css" />
<link href="css/forms.css" rel="stylesheet" type="text/css" />
<link href="css/facebox.css" media="screen" rel="stylesheet" type="text/css"/>

<script src="js/jquery.js" type="text/javascript"></script>
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

<form id="getpassword_form" action="/getPassword.do" method="post" >
<div class="wrapper">
  <div class="main-login">
  <div class="header-login">
      <div class="logo-login"><a href="http://www.cleartraders.com"><img src="images/cleartraders-logo.gif" border="0" alt="ClearTraders" width="182" height="28" /></a></div>
    </div>
    <div class="login">
    <div class="form-wrapper">
  <h1>Reset Your Password</h1>
  <h3>Type a new password plus your registered email address. A confirmation email will be sent to you. Please click the link in this email to complete the process.</h3>
  <span id="error_info_prompt" class="errorinfo"><html:errors property="error"/></span>
  <span id="normal_info_prompt" class="promptinfo"><html:errors property="prompt_info"/></span>
  <h2>New Password</h2><span class="errorinfo"><html:errors property="password"/></span>
  <div class="form-container">
    <div class="form-left">
     <input name="password" type="password" id="password" size="40" />
    </div>
    <div class="clear"></div>    
  </div>
  
  <h2>Confirm New Password</h2><span class="errorinfo"><html:errors property="confirmpassword"/></span>
  <div class="form-container">
    <div class="form-left">
      <input name="confirmpassword" type="password" id="confirmpassword" size="40" />
    </div>
    <div class="clear"></div>    
  </div>
  
  <h2>Email</h2><span class="errorinfo"><html:errors property="email"/></span>
  <div class="form-container">
    <div class="form-left">
      <input name="email_address" type="text" id="email_address" size="40" />
    </div>
    <div class="clear"></div>    
  </div>
    
  <h2>Re-type Security Code</h2><span class="errorinfo"><html:errors property="securitycode"/></span>
  <div class="form-container">
        <div class="form-left">
        	 <img border=0 src="./image.jsp"/>
        	 <input name="security_code" type="text" id="security_code" size="4" maxlength="4" />
        </div>
        <div class="clear">
     </div>    
  </div>
  
  <a class="form-button" href="#" onclick="document.getElementById('getpassword_form').submit();"><span>Confirm Password</span></a>
  
  <div class="clear"></div>
  </div>    
 </div> 
 </div>
</div>
</form>

</body>
</html>
