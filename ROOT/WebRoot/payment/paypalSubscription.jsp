<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%

String cmd = (String)request.getAttribute("cmd");
String hosted_button_id = (String)request.getAttribute("hosted_button_id");
String custom = (String)request.getAttribute("custom");

 %>
<html>
<head>
<meta http-equiv=refresh content="0;URL=https://www.paypal.com/cgi-bin/webscr?cmd=<%=cmd%>&hosted_button_id=<%=hosted_button_id%>&custom=<%=custom%>">
</head>
<body >
</body>
</html>
