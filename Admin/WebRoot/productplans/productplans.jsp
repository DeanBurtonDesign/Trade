<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@ page import="java.util.List" %>
<%@ page import="com.cleartraders.common.db.DataCache" %>
<%@ page import="com.cleartraders.common.entity.MarketTypeBean" %>
<%@ page import="com.cleartraders.common.entity.UserBean" %>
<%@ page import="com.cleartraders.common.define.CommonDefine" %>
<%@ page import="com.cleartraders.common.entity.ProductBean" %>
<%@ page import="com.cleartraders.webadmin.AdminConstants" %>

<%
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	response.setHeader("Cache-Control", "private"); // HTTP 1.1 
	response.setHeader("Cache-Control", "no-store"); // HTTP 1.1 
	response.setHeader("Cache-Control", "max-stale=0"); // HTTP 1.1 
	
	if( null == session || null == session.getAttribute(AdminConstants.USER_KEY) )
	{
		response.sendRedirect("../login/adminLogin.jsp");
		return;
	}
	else
	{
		UserBean currentUser = (UserBean)session.getAttribute(AdminConstants.USER_KEY);
		
		if(currentUser.getMemberType() != CommonDefine.ADMIN_MEMBER_TYPE)
		{
			response.sendRedirect("../login/adminLogin.jsp");
			return;
		}
	}	
	
	String errorRequest = "";
	if(null != request.getAttribute("error"))
	{
		errorRequest = (String)request.getAttribute("error");
	}
	
	List<ProductBean> oAllProducts = DataCache.getInstance().getAllProduct();
	
	List<MarketTypeBean> allMarkets = DataCache.getInstance().getAllMarketType();
 %>

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Clear Traders: Product Plans</title>
<link href="../../css/admin-global.css" rel="stylesheet" type="text/css" />
<script src="../../js/admin-common.js" language="javascript"></script>
</head>
<body>
<script type="text/javascript">

	function  showProductDetails(productID)
	{
		//alert("show product id="+productID);
	
		document.getElementById("product_id").setAttribute("value", productID);
		
		//get member by id from background
		var actionURL = "../productplans/getProductPlanByID.do?";	
		var memberID = "id="+productID;
	      	
		var objHTTP = getXMLHttp();
		
		if (objHTTP)
		{
			objHTTP.open("POST",encodeURI(actionURL),false,"","");
	    	objHTTP.setRequestHeader("CONTENT-TYPE","application/x-www-form-urlencoded");
	    	objHTTP.send(memberID);    	
	    	
	    	if(objHTTP.responseText != null)
		 	{
		 		parseAndUpdateProductDetails(objHTTP.responseText);
		 	}
		}
	}
	
	function parseAndUpdateProductDetails(productDetails)
	{
		//alert(productDetails);
		
		var allFields = productDetails.split(";");
		
		document.getElementById("save_product_button").setAttribute("value","SAVE");
		document.getElementById("operation_type").setAttribute("value", 2);
		
		for(var i=0; i<allFields.length; i++)
		{
			var subField = allFields[i].split("=");
			
			if(subField.length != 2)
			{
				continue;
			}
			else
			{ 
				parseAndUpdateProductFields(subField);
			}
		}
	}
	
	function parseAndUpdateProductFields(subField)
	{
		if(subField[0] == "name")
		{
			document.getElementById("product_name").setAttribute("value",subField[1]);
		}
		else if(subField[0] == "id")
		{
			document.getElementById("product_id").setAttribute("value",subField[1]);		
		}
		else if(subField[0] == "paid")
		{
			document.getElementById("product_type").setAttribute("value",subField[1]);	
			
			if(subField[1] == 0)
			{
				document.getElementById("product_type_paid").checked=true;
			}
			else 
			{
				document.getElementById("product_type_free").checked=true;
			}				
		}
		else if(subField[0] == "period")
		{
			document.getElementById("product_period").setAttribute("value",subField[1]);						
		}
		else if(subField[0] == "totalMarkets")
		{
			document.getElementById("product_markets").setAttribute("value",subField[1]);	
						
			var productMarketID = "product_markets_"+subField[1];
			document.getElementById(productMarketID).selected=true;	
		}
		else if(subField[0] == "price")
		{
			document.getElementById("product_price").setAttribute("value",subField[1]);		
		}
		else if(subField[0] == "includeSMS")
		{
			document.getElementById("product_sms").setAttribute("value",subField[1]);			
		}
		else if(subField[0] == "active")
		{	
			document.getElementById("product_status").setAttribute("value",subField[1]);	
			
			if(subField[1] == 1)
			{
				document.getElementById("product_status_checkbox").checked=true;
			}
			else 
			{
				document.getElementById("product_status_checkbox").checked=false;
			}		
		}
		else if(subField[0] == "paypalBtnID")
		{
			document.getElementById("paypal_btn_id").setAttribute("value",subField[1]);	
		}
	}
	
	function changeProductStatus(statusBox)
	{
		if(statusBox.checked == true)
		{
			document.getElementById("product_status").setAttribute("value",1);	
		}
		else
		{
			document.getElementById("product_status").setAttribute("value",0);	
		}
	}
	
	function selectProductType(productTypeBox)
	{
		document.getElementById("product_type").setAttribute("value",productTypeBox.getAttribute("value"));
	}
	
	function resetProductDetails()
	{
		window.location.href='../productPlans/getProductPlans.do';	
	}
	
</script>
<div class="center">
  <div id="contentMidBg">
    <div  id="contentBotBg">
      <div id="contentTopBg">
        <div id="contentWhiteFrame">
            <jsp:include flush="true" page="../include/top.jsp"></jsp:include>
	  		<div id="topTitle">
	  			<ul>
		  			<li class="leftTopTitle">MENU</li>
		  			<li class="midTopTitle">&nbsp; Product Plans &nbsp;<input type="submit" name="submit" onclick="resetProductDetails();" value="ADD NEW PLAN" style="cursor:pointer"/></li>
		  			<li class="midTopTitle2"></li>
		  			<li class="rightTopTitle">&nbsp;Product Plan Details</li>
	  			</ul>
	  		</div>
			<div id="content">  
          	<div id="contentLeft">
							<div class="leftMidBg">
								<div id="leftmenu">
									<ul>
										<li><a href="#"><span>Dashboard</span></a></li>
										<li><html:link forward="getMembers"><span>Members</span></html:link></li>
										<li><a href="#"><span>Billing</span></a></li>
										<li><a href="#"><span>Expenses</span></a></li>
										<li><a href="#"><span>Coupon Codes</span></a></li>
										<li><html:link forward="getSMSCredits"><span>SMS Credits</span></html:link></li>
										<li  class="navon"><span>Product Plans</span></li>
										<li><html:link forward="getStrategies"><span>Strategies</span></html:link></li>										
										<li class="nobd"><a href="#"><span>Messages</span></a></li>
									</ul>
								</div>   
						  </div>        
              <div class="leftBt2"></div>	
          </div>
          <div id="contentRight">
				  <div style="clear:both"></div>
					<div id="rightContent">
					 <div id="downLeftContent">
					 		<div class="graphicbox">
					 			<div id="productplanlist">
					 			  <%for(int i=0; i<oAllProducts.size(); i++) 
					 				{
					 				  	ProductBean productBean = oAllProducts.get(i);
					 				%>
					 				<ul>
					 					<li class="planname"><a href="#" onclick="showProductDetails(<%=productBean.getId()%>)"><%=productBean.getName()%></a>:</li>
					 					<%
					 					 if(productBean.getActive() == 1){
					 					 %>
					 					<li class="statuson">Active</li>
					 					<%}
					 					 else{ %>
					 					<li class="statusoff">Off</li>
					 					<%}%>
					 				</ul>
					 			   <%}%>
								</div>		
					 		</div>
					 </div>
					 <div id="downRightContent">
					 		<div class="graphicbox">
					 			<html:form action="/productplans/addOrUpdateProductPlanDetails">
					 			<input type="hidden" id="operation_type" name="operation_type" value="1" />
					 			<input type="hidden" id="product_id" name="product_id" value="-1" />
					 			<input type="hidden" id="product_status" name="product_status" value="0" />
					 			<input type="hidden" id="product_type" name="product_type" value="0" />
					 			
					 			<div id="memberdetailsstyle">
					 				<span class="errorinfo">
						      			<html:errors property="error"/><%=errorRequest%>
						      		</span>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="product_status"/></span>
						 				<li class="titlestyle"><p>Active</p></li>
						 				<li><input id="product_status_checkbox" name="product_status_checkbox" type="checkbox" onclick="changeProductStatus(this)" /></li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="product_name"/></span>
						 				<li class="titlestyle"><p>Plan Name</p></li>
						 				<li><input id="product_name" name="product_name" type="text" value="" class="" /></li>
						 			</ul>
						 		  <ul>
						 		  	<li class="titlestyle"><p>Type</p></li>
						 		  		<span class="errorinfo"><html:errors property="product_type"/></span>
						 				<li class="commonfont">
						 					<input id="product_type_paid" name="product_type" type="radio" value="0" onclick="selectProductType(this)" checked="true" />Paid
						 				  	<input id="product_type_free" name="product_type" type="radio" value="1" onclick="selectProductType(this)" />Free Trial
						 				</li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="product_period"/></span>
						 				<li class="titlestyle"><p>Period Days</p></li>
						 				<li><input id="product_period" name="product_period" type="text" value="" class=""/></li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="product_markets"/></span>
						 				<li class="titlestyle"><p>Total Markets</p></li>
						 				<li>
							 				<select id="product_markets" name="product_markets" property="product_markets">
							 				<%for(int i=1; i<=allMarkets.size(); i++) 
							 				  {
							 				     String market_option_id = "product_markets_"+i;
							 				  %>
													<option id="<%=market_option_id%>" value="<%=i%>" text=""><%=i%></option>
											<%} %>
											</select>	
										</li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="product_price"/></span>
						 				<li class="titlestyle"><p>Price &nbsp;&nbsp;&nbsp;&nbsp;USD$</p></li>
						 				<li><input id="product_price" name="product_price" type="text" value="" class=""/></li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="product_sms"/></span>
						 				<li class="titlestyle"><p>Include SMS</p></li>
						 				<li><input id="product_sms" name="product_sms" type="text" value="" class=""/></li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="paypal_btn_id"/></span>
						 				<li class="titlestyle"><p>Paypal Btn ID</p></li>
						 				<li><input id="paypal_btn_id" name="paypal_btn_id" type="text" value="0" class=""/></li>
						 			</ul>
						 			<ul>
						 				<li class="savebutton"><input id="save_product_button" type="submit" name="submit" value="ADD" style="cursor:pointer"/></li>
						 			</ul>
						 		</div>
						 		</html:form>
					 		</div>
					 </div>
				 </div>
         </div>
         <div class="clear"></div>
         </div><!-- Content End -->
		 		<div id="rightBt"></div>	
        <div class="clear"></div>
       </div>
      </div>
       <div class="clear"></div>
    </div>
    <div id="copyrighBg"></div>
  </div>
	<div class="fix228"></div>
</div>
</body>
</html>
