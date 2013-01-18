<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.cleartraders.webapp.WebConstants" %>

<%
	//clear page cache
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	response.setHeader("Cache-Control", "private"); // HTTP 1.1 
	response.setHeader("Cache-Control", "no-store"); // HTTP 1.1 
	response.setHeader("Cache-Control", "max-stale=0"); // HTTP 1.1 

	if(null == session || null == session.getAttribute(WebConstants.USER_KEY) || "".equals(session.getAttribute(WebConstants.USER_KEY)) )
	{
	%>
	<script language="javascript">
	window.top.location = "../login/userLogin.jsp";
	</script>
	<%
	}
	else
	{
	  session.invalidate();
	  %>
	  <script language="javascript">
		window.top.location = "../login/userLogin.jsp";
	  </script>
	<%
	}
%>
<jsp:forward page="logout.jsp" />
