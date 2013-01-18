<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="com.cleartraders.webapp.WebConstants" %>
<%@ page import="com.cleartraders.common.entity.QuickLinkBean" %>
<%@ page import="java.util.List" %>

<link href="../css/cleartraders-app.css" rel="stylesheet" type="text/css" />


<!-- Start of Left Two -->
<div class="left_body">
	<div class="Lbody_wrap">
		<div class="quicklink">Quick Links </div>
	</div>
	<div class="Lbody_wrap">
		<div class="L2">
			<%
			
			List<QuickLinkBean> oQuickLinks = (List<QuickLinkBean>)request.getAttribute(WebConstants.USER_QUICK_LINKS);
			
			if(oQuickLinks != null)
			{			
			%>
			<logic:iterate id="quickbroker" name="user_quick_links" type="com.cleartraders.common.entity.QuickLinkBean" indexId="count"> 
				<ul><a target="_blank" href="${quickbroker.url}">${quickbroker.name}</a></ul>
			</logic:iterate>		
			<%
			}
			%>	
			<ul class="account">
				<html:link forward="getUserPreference"><img src="../images/nav1_trans.gif" width="48" height="16" /></html:link>
			</ul>
		</div>
	</div>
	<div class="left_ad">
		<a target="_blank" href="http://www.cleartradersblog.com" border="0" ><img src="../images/clear-traders-blog.jpg" width="140" height="60" /></a>
	</div>
</div>				
<div class="left_foot"></div>
<!-- End of Left Two -->