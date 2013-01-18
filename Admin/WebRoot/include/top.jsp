<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cleartraders.webadmin.AdminConstants" %>

<link href="../../css/admin-global.css" rel="stylesheet" type="text/css" />
<div id="logo">
	<div id="smallButton">
		<ul>
			<li class="welcometext"><span>Welcome,&nbsp;<logic:present name="<%=AdminConstants.USER_KEY%>" scope="session"><bean:write name="<%=AdminConstants.USER_KEY%>" property="first_name" /></logic:present></li>
			<li class="login"><html:link forward="logoff"><img src="../../images/logout_button.gif" width="73" height="20" border="0" /></html:link></li>
		</ul>
	</div>
	<img src="../../images/admin-logo.gif" width="290" height="50" />
</div>
<div id="nav">
</div>
<div class="fix14"></div>
