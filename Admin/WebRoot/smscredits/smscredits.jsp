<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="java.util.List" %>

<%@ page import="com.cleartraders.common.db.DataCache" %>
<%@ page import="com.cleartraders.common.entity.ProductBean" %>
<%@ page import="com.cleartraders.common.entity.UserBean" %>
<%@ page import="com.cleartraders.common.define.CommonDefine" %>
<%@ page import="com.cleartraders.webadmin.AdminConstants" %>
<%@ page import="com.cleartraders.common.entity.SmsPackageBean" %>

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
	
	List<SmsPackageBean> allSMSPackages = DataCache.getInstance().getAllSmsPackages();

 %>
 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Clear Traders: SMS Credits</title>
<link href="../../css/admin-global.css" rel="stylesheet" type="text/css" />
<script src="../../js/admin-common.js" language="javascript"></script>
</head>
<body>
<script type="text/javascript">
	
	var indexOfID = 0;
	
	function addNewPackage(productplanID, productID)
	{
		indexOfID++;
		
		var newPackageID = "new,"+productplanID+""+indexOfID;
		
		var smspackagehtml = "<ul id='"+newPackageID+"'>";
		smspackagehtml += "<form action='../smscredits/saveSMSPackage.do' method='post'>";
		smspackagehtml += "<input type='hidden' id='smspackage_id' name='smspackage_id' value='-1' />";
		smspackagehtml += "<input type='hidden' id='product_id' name='product_id' value='"+productID+"' />";
		//smspackagehtml += "<span class='errorinfo'><html\:errors property='smspackage_name' /></span>";
		smspackagehtml += "<li><input name='smspackage_name' type='text' class='smspackagesubtitle1' /></li>";
		//smspackagehtml += "<span class='errorinfo'><html\:errors property='smspackage_sms_included' /></span>";
		smspackagehtml += "<li><input name='smspackage_sms_included' type='text' class='smspackagesubtitle22' /></li>";
		//smspackagehtml += "<span class='errorinfo'><html\:errors property='smspackage_cost' /></span>";
		smspackagehtml += "<li><input name='smspackage_cost' type='text' class='smspackagesubtitle32' /></li>";
		smspackagehtml += "<li><input name='paypal_button_id' type='text' class='smspackagesubtitle62' /></li>";
		smspackagehtml += "<li><input name='submit' type='submit' class='smspackagesubtitle4' value='Save' style='cursor:pointer' /></li>";
		smspackagehtml += "<li><input name='reset' type='reset' class='smspackagesubtitle5' value='Delete' onclick=\"deletePackage('"+productplanID+"','"+newPackageID+"')\" style='cursor:pointer' /></li>";
		smspackagehtml += "</form>";
		smspackagehtml += "</ul>";
		
		//alert(smspackagehtml);
		var productPlan = document.getElementById(productplanID);
		insertHtml("BeforeEnd",productPlan,smspackagehtml);
	}
		
	function deletePackage(productplanid, smspackageid)
	{
		var canRemoved = false;
		
		//alert("product id="+productplanid+", packageid="+packageid);
		if(isOldPackage(smspackageid))
		{
			//alert('request background for smspackage id='+smspackageid);
					
			//request background
			var actionURL = "../smscredits/deleteSMSPackageByID.do?";	
			var memberID = "id="+smspackageid;
		      	
			var objHTTP = getXMLHttp();
			
			if (objHTTP)
			{
				objHTTP.open("POST",encodeURI(actionURL),false,"","");
		    	objHTTP.setRequestHeader("CONTENT-TYPE","application/x-www-form-urlencoded");
		    	objHTTP.send(memberID);    	
		    	
		    	if(objHTTP.responseText != null)
			 	{
			 		if(objHTTP.responseText == "true")
			 		{
			 			canRemoved = true;
			 		}
			 	}
			}
		}
		else 
		{
			canRemoved = true;
		}
		
		if(canRemoved)
		{
			var productPlan = document.getElementById(productplanid);
			productPlan.removeChild(document.getElementById(smspackageid));
		}
	}
	
	function isOldPackage(smspackageid)
	{
		var smspackageids = smspackageid.split(",");
		//alert(smspackageids);
		if(smspackageids.length == 2)
		{
			if(smspackageids[0] == "new")
			{
				return false;
			}
		}
		
		return true;
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
		  			<li class="bigtoptitle">&nbsp; SMS Credits</li>
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
										<li  class="navon"><span>SMS Credits</span></li>
										<li><html:link forward="getProductPlans"><span>Product Plans</span></html:link></li>
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
					 <div id="smscreditscontent">
					 		<span class="errorinfo">
						      	<html:errors property="error"/><%=errorRequest%>
						    </span>
					 		<div class="graphicbox">
					 			<%
					 			  for(int i=0; i<oAllProducts.size(); i++) 
					 			  {
					 				ProductBean oProduct = oAllProducts.get(i);
					 				String productPlanNameID = "productplanid"+oProduct.getId();	
					 				//System.out.println(productPlanNameID);				 				
					 			%>
					 			<div id="<%=productPlanNameID%>" class="productplanname">
					 				<ul>
					 					<%if(oProduct.getActive() != CommonDefine.ACTIVE_PRODUCT)
					 					  { %>
					 					<li class="inactiveproductplantitle"><%=oProduct.getName()%></li>
					 					<%}else{%>
					 					<li class="activeproductplantitle"><%=oProduct.getName()%></li>
					 					<%}%>
					 					<li class="productplantitle"><input type="submit" name="submit" value="ADD SMS PACKAGE" onclick="addNewPackage('<%=productPlanNameID%>','<%=oProduct.getId()%>');" style="cursor:pointer"/></li>
					 				</ul>
					 				<ul>
					 					<li class="smspackagesubtitle1">Package Name</li>
                    					<li class="smspackagesubtitle2">SMS Included</li>
                    					<li class="smspackagesubtitle3">Cost (USD)</li>	
                    					<li class="smspackagesubtitle6">Paypal Button ID</li>						 						
					 				</ul>
					 				<%for(int j=0; j<allSMSPackages.size(); j++)
					 				  {
					 					SmsPackageBean oSMS = allSMSPackages.get(j);
					 					if(oSMS.getProduct_id() != oProduct.getId())
					 					{
					 						continue;
					 					}
					 				 %>
					 					<ul id="<%=oSMS.getId()%>">
					 						<html:form action="/smscredits/saveSMSPackage">
					 							<input type="hidden" name="smspackage_id" value="<%=oSMS.getId()%>" />
					 							<input type="hidden" name="product_id" value="<%=oSMS.getProduct_id()%>" />
							 					<li><input name="smspackage_name" type="text" class="smspackagesubtitle1" value="<%=oSMS.getName()%>" /></li>
							 					<li><input name="smspackage_sms_included" type="text" class="smspackagesubtitle22"  value="<%=oSMS.getSms_included()%>" /></li>
							 					<li><input name="smspackage_cost" type="text" class="smspackagesubtitle32" value="<%=oSMS.getSms_cost()%>" /></li>
							 					<li><input name="paypal_button_id" type="text" class="smspackagesubtitle62" value="<%=oSMS.getPaypal_button_id()%>" /></li>
							 					<li><input name="submit" type="submit" class="smspackagesubtitle4" value="Save" style="cursor:pointer" /></li>
							 					<li><input name="reset" type="reset" class="smspackagesubtitle5" value="Delete" onclick="deletePackage('<%=productPlanNameID%>','<%=oSMS.getId()%>');" style="cursor:pointer" /></li>
						 					</html:form>
						 				</ul>
					 				<%}%>
						 		</div>
						 		<%} %>
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
