<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%

	String cmd = (String)request.getAttribute("cmd");
	String business = (String)request.getAttribute("business");
	
	String item_name = (String)request.getAttribute("item_name");
	String item_number = (String)request.getAttribute("item_number");
	String currency_code = (String)request.getAttribute("currency_code");
	String a3 = (String)request.getAttribute("a3");
	String p3 = (String)request.getAttribute("p3");
	String t3 = (String)request.getAttribute("t3");
	String modify = (String)request.getAttribute("modify");
	String custom = (String)request.getAttribute("custom");
                
 %>
<html>
<head>
<meta http-equiv=refresh content="0;URL=https://www.paypal.com/cgi-bin/webscr?cmd=<%=cmd%>&business=<%=business%>&item_name=<%=item_name%>&item_number=<%=item_number%>&currency_code=<%=currency_code%>&a3=<%=a3%>&p3=<%=p3%>&t3=<%=t3%>&modify=<%=modify%>&custom=<%=custom%>">
</head>
<body >
</body>
</html>
