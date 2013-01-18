<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.cleartraders.common.db.DataCache" %>
<%@ page import="com.cleartraders.common.entity.MarketTypeBean" %>
<%@ page import="com.cleartraders.common.entity.MarketPeriodBean" %>
<%@ page import="com.cleartraders.common.entity.UserBean" %>
<%@ page import="com.cleartraders.common.entity.StrategyBean" %>
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
	
	//get all strategy
	List<StrategyBean> oAllStrategies = (List<StrategyBean>)request.getAttribute(AdminConstants.STRATEGY_LIST);
	if(null == oAllStrategies)
	{
		oAllStrategies = new ArrayList<StrategyBean>();
	}
	
	//get all periods
	List<MarketPeriodBean> oAllTimeFrame = DataCache.getInstance().getAllMarketPeriod();
	String marketPeriodIDs = "'";
	for(int i=0; i<oAllTimeFrame.size(); i++)
	{
		MarketPeriodBean oTempBean = oAllTimeFrame.get(i);
		
		marketPeriodIDs += oTempBean.getId();
		
		if(i < oAllTimeFrame.size() -1)
		{
			marketPeriodIDs += ",";
		}
	}
	
	marketPeriodIDs += "'";
	
	//get All product plans
	List<ProductBean> oAllProducts = DataCache.getInstance().getAllProduct();
	String productPlansIDs = "'";
	for(int i=0; i<oAllProducts.size(); i++)
	{
		ProductBean oTempBean = oAllProducts.get(i);
		
		productPlansIDs += oTempBean.getId();
		
		if(i < oAllProducts.size() -1)
		{
			productPlansIDs += ",";
		}
	}
	
	productPlansIDs += "'";
	
	//get all markets
	List<MarketTypeBean> allMarkets = DataCache.getInstance().getAllMarketType();
	String marketIDs = "'";
	
	for(int i=0; i<allMarkets.size(); i++)
	{
		MarketTypeBean oTempBean = allMarkets.get(i);
		
		marketIDs += oTempBean.getId();
		
		if(i < allMarkets.size() -1)
		{
			marketIDs += ",";
		}
	}
	
	marketIDs += "'";
	
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

	var allMarketIDs = <%=marketIDs%>;
	var allproductPlanIDs = <%=productPlansIDs%>;
	var allTimeFrameIDs = <%=marketPeriodIDs%>;

	function  showStrategyDetails(strategyID)
	{
		//alert("show strategy id="+strategyID);
	
		document.getElementById("strategy_id").setAttribute("value", strategyID);
		
		//get member by id from background
		var actionURL = "../strategy/getStrategyByID.do?";	
		var strategyID = "id="+strategyID;
	      	
		var objHTTP = getXMLHttp();
		
		if (objHTTP)
		{
			objHTTP.open("POST",encodeURI(actionURL),false,"","");
	    	objHTTP.setRequestHeader("CONTENT-TYPE","application/x-www-form-urlencoded");
	    	objHTTP.send(strategyID);    	
	    	
	    	if(objHTTP.responseText != null)
		 	{
		 		parseAndUpdateStrategyDetails(objHTTP.responseText);
		 	}
		}
	}
	
	function parseAndUpdateStrategyDetails(strategyDetails)
	{
		//alert(strategyDetails);
		
		var allFields = strategyDetails.split(";");
		
		document.getElementById("save_strategy_button").setAttribute("value","SAVE");
		document.getElementById("operation_type").setAttribute("value", 2);
		
		clearMarketIDList();
		clearTimeframeIDList();
		clearProductPlanIDList();
		
		for(var i=0; i<allFields.length; i++)
		{
			var subField = allFields[i].split("=");
			
			if(subField.length != 2)
			{
				continue;
			}
			else
			{ 
				parseAndUpdateStrategyFields(subField);
			}
		}
	}
	
	function clearMarketIDList()
	{
		var allMarketIDsArray = allMarketIDs.split(",");
			
		for(var i=0; i<allMarketIDsArray.length; i++)
		{
			var marketCheckBoxID = "member_market"+allMarketIDsArray[i];
			document.getElementById(marketCheckBoxID).checked = false;
		}
	}
	
	function clearTimeframeIDList()
	{
		var allTimeFrameIDsArray = allTimeFrameIDs.split(",");
			
		for(var i=0; i<allTimeFrameIDsArray.length; i++)
		{
			var timeFrameCheckBoxID = "time_frame"+allTimeFrameIDsArray[i];
			document.getElementById(timeFrameCheckBoxID).checked = false;
		}
	}
	
	function clearProductPlanIDList()
	{		
		var allProductplanIDsArray = allproductPlanIDs.split(",");
			
		for(var i=0; i<allProductplanIDsArray.length; i++)
		{
			var productPlanCheckBoxID = "product_plan"+allProductplanIDsArray[i];
			document.getElementById(productPlanCheckBoxID).checked = false;
		}
	}
	
	function parseAndUpdateStrategyFields(subField)
	{
		if(subField[0] == "id")
		{
			document.getElementById("strategy_id").setAttribute("value",subField[1]);		
		}
		else if(subField[0] == "system_name")
		{
			document.getElementById("system_name").setAttribute("value",subField[1]);			
		}
		else if(subField[0] == "common_name")
		{
			document.getElementById("common_name").setAttribute("value",subField[1]);						
		}
		else if(subField[0] == "description")
		{
			document.getElementById("description_content").setAttribute("value",subField[1]);
			var descriptionContent = document.getElementById("description_content");
			
			if(isIE())
			{
				descriptionContent.innerText=subField[1];
			}
			else
			{
				descriptionContent.textContent=subField[1];
			}
		}
		else if(subField[0] == "link_url")
		{
			document.getElementById("link_url").setAttribute("value",subField[1]);			
		}
		else if(subField[0] == "active")
		{	
			document.getElementById("strategy_status").setAttribute("value",subField[1]);	
			
			if(subField[1] == 1)
			{
				document.getElementById("strategy_status_checkbox").checked=true;
			}
			else 
			{
				document.getElementById("strategy_status_checkbox").checked=false;
			}		
		}
		else if(subField[0] == "related_markets")
		{
			var marketIDs = subField[1].split(",");

			for(var i=0; i<marketIDs.length; i++)
			{
				var marketCheckBoxID = "member_market"+marketIDs[i];
				
				if(document.getElementById(marketCheckBoxID) != null)
				{
					document.getElementById(marketCheckBoxID).checked = true;
				}
			}
			
			setSubscribledMarket();
			
			initSelectAllMarketsCheckBoxStatus();
		}
		else if(subField[0] == "related_timeframes")
		{
			var timeFrames = subField[1].split(",");

			for(var i=0; i<timeFrames.length; i++)
			{
				var timeFrameCheckBoxID = "time_frame"+timeFrames[i];
				
				if(document.getElementById(timeFrameCheckBoxID) != null)
				{
					document.getElementById(timeFrameCheckBoxID).checked = true;
				}
			}
			
			setSubscribledTimeFrame();
			
			initSelectAllTimeframeCheckBoxStatus();
		}
		else if(subField[0] == "related_product_plans")
		{
			var productPlanIDs = subField[1].split(",");

			for(var i=0; i<productPlanIDs.length; i++)
			{
				var productPlanCheckBoxID = "product_plan"+productPlanIDs[i];
				
				if(document.getElementById(productPlanCheckBoxID) != null)
				{
					document.getElementById(productPlanCheckBoxID).checked = true;
				}
			}
			
			setSubscribledProductPlan();
			
			initSelectAllProductplansCheckBoxStatus();
		}
	}
	
	function changeStrategyStatus(statusBox)
	{
		if(statusBox.checked == true)
		{
			document.getElementById("strategy_status").setAttribute("value",1);	
		}
		else
		{
			document.getElementById("strategy_status").setAttribute("value",0);	
		}
	}
	
	function selectProductType(productTypeBox)
	{
		document.getElementById("product_type").setAttribute("value",productTypeBox.getAttribute("value"));
	}
	
	function resetProductDetails()
	{
		window.location.href='../strategy/getStrategies.do';	
	}
		
	//for Product Plans
	function initSelectAllProductplansCheckBoxStatus()
	{
		var allProductPlanIDsArray = allproductPlanIDs.split(",");
		document.getElementById("select_all_products").checked = true;	
		
		for(var i=0; i<allProductPlanIDsArray.length; i++)
		{
			var productPlanCheckBoxID = "product_plan"+allProductPlanIDsArray[i];
			
			if(document.getElementById(productPlanCheckBoxID).checked == false)
			{
				document.getElementById("select_all_products").checked = false;
				
				break;			
			}
		}
	}
	
	function selectAllProductPlans(selectAllCheckbox)
	{
		var allProductPlanIDsArray = allproductPlanIDs.split(",");
			
		for(var i=0; i<allProductPlanIDsArray.length; i++)
		{
			var productPlanCheckBoxID = "product_plan"+allProductPlanIDsArray[i];
			
			if(selectAllCheckbox.checked == true)
			{
				document.getElementById(productPlanCheckBoxID).checked = true;
			}
			else
			{
				document.getElementById(productPlanCheckBoxID).checked = false;
			}
		}
		
		setSubscribledProductPlan();
	}
	
	function getCheckedProductPlans()
	{
		var allProductPlanIDsArray = allproductPlanIDs.split(",");
		var subscribledProductPlanIDs = '';
			
		for(var i=0; i<allProductPlanIDsArray.length; i++)
		{
			var productPlanCheckBoxID = "product_plan"+allProductPlanIDsArray[i];
			
			if( document.getElementById(productPlanCheckBoxID).checked == true || document.getElementById(productPlanCheckBoxID).checked == 1 )
			{
				subscribledProductPlanIDs += document.getElementById(productPlanCheckBoxID).getAttribute("value")+",";
			}
		}
						
		return subscribledProductPlanIDs;
	}
	
	function setSubscribledProductPlan()
	{
		document.getElementById("all_related_product_plans").setAttribute("value",getCheckedProductPlans());
	}
	
	//for Markets
	function initSelectAllMarketsCheckBoxStatus()
	{
		var allMarketIDsArray = allMarketIDs.split(",");
		document.getElementById("select_all_markets").checked = true;
			
		for(var i=0; i<allMarketIDsArray.length; i++)
		{
			var marketCheckBoxID = "member_market"+allMarketIDsArray[i];
			
			if(document.getElementById(marketCheckBoxID).checked == false)
			{
				document.getElementById("select_all_markets").checked = false;
				
				break;			
			}
		}
	}

	function selectAllMarkets(selectAllCheckbox)
	{
		var allMarketIDsArray = allMarketIDs.split(",");
			
		for(var i=0; i<allMarketIDsArray.length; i++)
		{
			var marketCheckBoxID = "member_market"+allMarketIDsArray[i];
			
			if(selectAllCheckbox.checked == true)
			{
				document.getElementById(marketCheckBoxID).checked = true;
			}
			else
			{
				document.getElementById(marketCheckBoxID).checked = false;
			}
		}
		
		setSubscribledMarket();
	}
	
	function clearMarketIDList()
	{
		//alert('clear market checked');
		var allMarketIDsArray = allMarketIDs.split(",");
			
		for(var i=0; i<allMarketIDsArray.length; i++)
		{
			var marketCheckBoxID = "member_market"+allMarketIDsArray[i];
			document.getElementById(marketCheckBoxID).checked = false;
		}
	}
	
	function getCheckedMarkets()
	{
		var allMarketIDsArray = allMarketIDs.split(",");
		var subscribledMarketIDs = '';
			
		for(var i=0; i<allMarketIDsArray.length; i++)
		{
			var marketCheckBoxID = "member_market"+allMarketIDsArray[i];
			
			if( document.getElementById(marketCheckBoxID).checked == true || document.getElementById(marketCheckBoxID).checked == 1 )
			{
				subscribledMarketIDs += document.getElementById(marketCheckBoxID).getAttribute("value")+",";
			}
		}
				
		return subscribledMarketIDs;
	}
	
	function setSubscribledMarket()
	{
		document.getElementById("all_related_markets").setAttribute("value",getCheckedMarkets());
	}
	
	//for Time Frame
	function initSelectAllTimeframeCheckBoxStatus()
	{
		var allTimeFrameIDsArray = allTimeFrameIDs.split(",");
		document.getElementById("select_all_timeframe").checked = true;
		
		for(var i=0; i<allTimeFrameIDsArray.length; i++)
		{
			var timeFrameCheckBoxID = "time_frame"+allTimeFrameIDsArray[i];
			
			if(document.getElementById(timeFrameCheckBoxID).checked == false)
			{
				document.getElementById("select_all_timeframe").checked = false;
				
				break;			
			}
		}
	}
	
	function selectAllTimeFrame(selectAllCheckbox)
	{
		var allTimeFrameIDsArray = allTimeFrameIDs.split(",");
			
		for(var i=0; i<allTimeFrameIDsArray.length; i++)
		{
			var timeFrameCheckBoxID = "time_frame"+allTimeFrameIDsArray[i];
			
			if(selectAllCheckbox.checked == true)
			{
				document.getElementById(timeFrameCheckBoxID).checked = true;
			}
			else
			{
				document.getElementById(timeFrameCheckBoxID).checked = false;
			}
		}
		
		setSubscribledTimeFrame();
	}
	
	function getCheckedTimeFrame()
	{
		var allTimeFrameIDsArray = allTimeFrameIDs.split(",");
		var subscribledTimeFrameIDs = '';
			
		for(var i=0; i<allTimeFrameIDsArray.length; i++)
		{
			var timeFrameCheckBoxID = "time_frame"+allTimeFrameIDsArray[i];
			
			if( document.getElementById(timeFrameCheckBoxID).checked == true || document.getElementById(timeFrameCheckBoxID).checked == 1 )
			{
				subscribledTimeFrameIDs += document.getElementById(timeFrameCheckBoxID).getAttribute("value")+",";
			}
		}
						
		return subscribledTimeFrameIDs;
	}
	
	function setSubscribledTimeFrame()
	{
		document.getElementById("all_related_timeframe").setAttribute("value",getCheckedTimeFrame());
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
		  			<li class="midTopTitle">&nbsp; Strategies &nbsp;<input type="submit" name="submit" onclick="resetProductDetails();" value="ADD STRATEGY" style="cursor:pointer"/></li>
		  			<li class="midTopTitle2"></li>
		  			<li class="rightTopTitle">&nbsp;Strategy Details</li>
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
										<li><html:link forward="getProductPlans"><span>Product Plans</span></html:link></li>
										<li  class="navon"><span>Strategies</span></li>
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
					 			  <%for(int i=0; i<oAllStrategies.size(); i++) 
					 				{
					 				  	StrategyBean strategBean = oAllStrategies.get(i);
					 				%>
					 				<ul>
					 					<li class="planname"><a href="#" onclick="showStrategyDetails(<%=strategBean.getId()%>)"><%=strategBean.getCommon_name()%></a>:</li>
					 					<%
					 					 if(strategBean.getActive() == CommonDefine.ACTIVE_STRATEGY){
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
					 			<html:form action="/strategy/addOrUpdateStrategy">
					 			<input type="hidden" id="operation_type" name="operation_type" value="1" />
					 			
					 			<input type="hidden" id="strategy_id" name="strategy_id" value="-1" />
					 			<input type="hidden" id="strategy_status" name="strategy_status" value="0" />
					 			
					 			<input type="hidden" id="all_related_markets" name="all_related_markets" value="" />
					 			<input type="hidden" id="all_related_product_plans" name="all_related_product_plans" value="" />
					 			<input type="hidden" id="all_related_timeframe" name="all_related_timeframe" value="" />
					 			
					 			<div id="memberdetailsstyle">
					 				<span class="errorinfo">
						      			<html:errors property="error"/><%=errorRequest%>
						      		</span>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="strategy_status"/></span>
						 				<li class="titlestyle2"><p>Active</p></li>
						 				<li><input id="strategy_status_checkbox" name="strategy_status_checkbox" type="checkbox" onclick="changeStrategyStatus(this)" /></li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="system_name"/></span>
						 				<li class="titlestyle2"><p>System Name</p></li>
						 				<li><input id="system_name" name="system_name" type="text" value="" class="" />Don't change!</li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="common_name"/></span>
						 				<li class="titlestyle2"><p>Common Name</p></li>
						 				<li><input id="common_name" name="common_name" type="text" value="" class="" /></li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="link_url"/></span>
						 				<li class="titlestyle2"><p>Link URL</p></li>
						 				<li><input id="link_url" name="link_url" type="text" value="http://" class="" /></li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="description"/></span>
						 				<li class="titlestyle2"><p>Description</p></li>
						 				<li>
						 					<textarea id="description_content" maxLength="250" name="description_content" type="text" style="width:350;height:100;" value=""></textarea>
						 				</li>
						 			</ul>
						 			
						 			<ul>
						 				<li class="titlestyle2"><p>Product Plans</p></li>
						 				<li class="commonfont">
						 				  <input id="select_all_products" name="select_all_products" type="checkbox" value="" onclick="selectAllProductPlans(this)" />Select All Product Plans
						 				</li>
						 			</ul>
						 				<%
								    	for(int i=0; i<oAllProducts.size(); i++)
								    	{
								    		ProductBean productBean = oAllProducts.get(i);
								    		String productPlanID = "product_plan"+productBean.getId();
								     	%>
							 			<ul>
							 				<li class="titlestyle2">&nbsp;</li>
							 				<li class="commonfont">
							 					<input id="<%=productPlanID%>" name="member_market_options" type="checkbox" value="<%=productBean.getId()%>" onclick="setSubscribledProductPlan();" /><%=productBean.getName()%>
							 				</li>	
							 			</ul>
						 			<%  }%>
						 			
						 			<ul>
						 				<li class="titlestyle2"><p>Markets</p></li>
						 				<li class="commonfont">
						 				  <input id="select_all_markets" name="select_all_markets" type="checkbox" value="" onclick="selectAllMarkets(this)" />Select All Markets
						 				</li>
						 			</ul>
						 				<%
								    	for(int i=0; i<allMarkets.size(); i++)
								    	{
								    		MarketTypeBean marketBean = allMarkets.get(i);
								    		String marketID = "member_market"+marketBean.getId();
								     	%>
							 			<ul>
							 				<li class="titlestyle2">&nbsp;</li>
							 				<li class="commonfont">
							 					<input id="<%=marketID%>" name="member_market_options" type="checkbox" value="<%=marketBean.getId()%>" onclick="setSubscribledMarket();" /><%=marketBean.getDisplay_name()%>
							 				</li>	
							 			</ul>
						 			<%  }%>
						 			
						 			<ul>
						 				<li class="titlestyle2"><p>Time-Frame</p></li>
						 				<li class="commonfont">
						 				  <input id="select_all_timeframe" name="select_all_timeframe" type="checkbox" value="" onclick="selectAllTimeFrame(this)" />Select All Time-Frame
						 				</li>
						 			</ul>
						 				<%
								    	for(int i=0; i<oAllTimeFrame.size(); i++)
								    	{
								    		MarketPeriodBean timeFrameBean = oAllTimeFrame.get(i);
								    		String timeFrameID = "time_frame"+timeFrameBean.getId();
								     	%>
							 			<ul>
							 				<li class="titlestyle2">&nbsp;</li>
							 				<li class="commonfont">
							 					<input id="<%=timeFrameID%>" name="member_market_options" type="checkbox" value="<%=timeFrameBean.getId()%>" onclick="setSubscribledTimeFrame();" /><%=timeFrameBean.getValue() +" "+ timeFrameBean.getPeriod_name()%>
							 				</li>	
							 			</ul>
						 			<%  }%>
						 			
						 			<ul>
						 				<li class="savebutton"><input id="save_strategy_button" type="submit" name="submit" value="ADD" style="cursor:pointer"/></li>
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
