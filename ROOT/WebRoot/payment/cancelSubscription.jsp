<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%

String cmd = (String)request.getAttribute("cmd");
String alias = (String)request.getAttribute("alias");

 %>
<html>
<head>
<meta http-equiv=refresh content="0;URL=https://www.paypal.com/cgi-bin/webscr?cmd=<%=cmd%>&alias=<%=alias%>">
</head>
<body >
</body>
</html>
