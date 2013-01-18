<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.cleartraders.webadmin.AdminConstants" %>

<%
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	response.setHeader("Cache-Control", "private"); // HTTP 1.1 
	response.setHeader("Cache-Control", "no-store"); // HTTP 1.1 
	response.setHeader("Cache-Control", "max-stale=0"); // HTTP 1.1 

	if(null == session || null == session.getAttribute(AdminConstants.USER_KEY) || "".equals(session.getAttribute(AdminConstants.USER_KEY)) )
	{
	%>
	<script language="javascript">
		window.top.location = "../login/adminLogin.jsp";
	</script>
	<%
	}
	else
	{
	  session.invalidate();
	  %>
	  <script language="javascript">
		window.top.location = "../login/adminLogin.jsp";
	  </script>
	<%
	}
%>
