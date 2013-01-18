<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/pagination.tld" prefix="pagination-tag"%>
<%@ page import="com.cleartraders.common.entity.ProductBean" %>
<%@ page import="com.cleartraders.webapp.WebConstants" %>

<link href="../css/cleartraders-app.css" rel="stylesheet" type="text/css" />

<%

Long currentSmsCredits = (Long)request.getAttribute(WebConstants.MY_SMS_CREDITS);
if(null == currentSmsCredits)
{
	currentSmsCredits = Long.valueOf(0);
}

ProductBean currentProductBean = (ProductBean)request.getAttribute(WebConstants.MY_MEMBER_SHIP);
if(null == currentProductBean)
{
	currentProductBean = new ProductBean();
}

%>
<!-- Start Left one -->
<div class="left_body"> 
	<div class="Lbody_wrap">
		<div class="CLbody" id="login_name">
			<div class="CLbody_up">
				<em><logic:present name="userinfo" scope="session"><bean:write name="userinfo" property="first_name" /></logic:present></em>
			</div>
			<div class="CLbody_foot">
				<span class="span_gray">
					<logic:present name="userinfo" scope="session"><bean:write name="userinfo" property="last_name" /></logic:present>
				</span>
				<span class="account"><html:link forward="getContacts"><img src="../images/nav1_trans.gif" width="48" height="16" /></html:link></span>
			</div>
		</div>
	</div>
	<div class="Lbody_wrap">
		<div class="CLbody" id="markets">
			<div class="CLbody_up">
				<%if(currentProductBean.getId() == WebConstants.FREE_TRIAL_MEMBER_ID){ %>
					<em>Free Trial Member</em>
				<%}else if(currentProductBean.getId() == WebConstants.SPECIALIST_MEMBER_ID){%>
					<em>Specialist Member</em>
				<%}else if(currentProductBean.getId() == WebConstants.EXPERT_MEMBER_ID){%>
					<em>Expert Member</em>
				<%}else if(currentProductBean.getId() == WebConstants.MASTER_MEMBER_ID){%>
					<em>Master Member</em>
				<%}else{ %>
					<em>Free Trial Member</em>
				<%}%>
			</div>
			<div class="CLbody_foot">
				<%if(currentProductBean.getTotalMarkets() < 2){ %>
					<span><%=currentProductBean.getTotalMarkets()%> Market</span>
				<%}else{ %>
					<span><%=currentProductBean.getTotalMarkets()%> Markets</span>
				<%}%>
				<span class="upgrade"><html:link forward="getMymembership"><img src="../images/nav1_trans.gif" width="48" height="16" /></html:link></span>
				<div style="clear:both;"></div>
			</div>
		</div>
	</div>
	<div class="Lbody_wrap">
		<div class="CLbody" id="sms">
			<div class="CLbody_up">
				<em><%=currentSmsCredits.longValue()%> SMS</em>
			</div>
			<div class="CLbody_foot">
				<span>Alert Credits</span>
				<span class="buy-sms"><html:link forward="getSMSCredits"><img src="../images/nav1_trans.gif" width="48" height="16" /></html:link></span>
			</div>
		</div>
	</div>
	<span class="upgrade_membership_left_btn"><html:link forward="getMymembership"><img src="../images/nav1_trans.gif" width="142" height="20" /></html:link></span>
</div>
<div class="left_foot"></div>
<!-- End of Left one-->

