<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%@ page import="com.cleartraders.common.entity.UserBean" %>
<%@ page import="com.cleartraders.common.db.DataCache" %>
<%@ page import="com.cleartraders.common.entity.CountryBean" %>
<%@ page import="com.cleartraders.common.entity.ProductBean" %>
<%@ page import="com.cleartraders.common.entity.StrategyBean" %>
<%@ page import="com.cleartraders.common.entity.TimeZoneBean" %>
<%@ page import="com.cleartraders.common.entity.MarketTypeBean" %>
<%@ page import="com.cleartraders.common.define.CommonDefine" %>
<%@ page import="com.cleartraders.webadmin.AdminConstants" %>

<%
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	response.setHeader("Cache-Control", "private"); // HTTP 1.1 
	response.setHeader("Cache-Control", "no-store"); // HTTP 1.1 
	response.setHeader("Cache-Control", "max-stale=0"); // HTTP 1.1 
	response.setHeader("charset","UTF-8");   
	
	
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
		
	int memberSortType = AdminConstants.SORT_MEMBER_A_TO_Z;
	if(null != request.getAttribute(AdminConstants.MEMBER_SORT_TYPE))
	{
		memberSortType = Integer.parseInt((String)request.getAttribute(AdminConstants.MEMBER_SORT_TYPE));
	}
	
	Map<Long, List<StrategyBean>> allMarketStrategy = (Map<Long, List<StrategyBean>>)request.getAttribute(AdminConstants.MARKET_STRATEGY_MAP);
	if(null == allMarketStrategy)
	{
		allMarketStrategy = new HashMap<Long, List<StrategyBean>>();
	}
	
	//get all memeber level (products)
	List<ProductBean> allProducts = DataCache.getInstance().getAllProduct();
	
	//get all country
	List<CountryBean> allCountry = DataCache.getInstance().getAllCountry();
	
	List<TimeZoneBean> allTimezone = DataCache.getInstance().getAllTimeZone();
			
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Clear Traders: Members</title>
<link href="../../css/admin-global.css" rel="stylesheet" type="text/css" />
<script src="../../js/admin-common.js" language="javascript"></script>
</head>
<body>
<script type="text/javascript">

var allMarketIDs = <%=marketIDs%>;

function showMemberDetails(memberID, memberName)
{
	//alert("show member id="+memberID);
	
	//update member id	
	document.getElementById("member_id").setAttribute("value", memberID);
	document.getElementById("member_delete_id").setAttribute("value", memberID);	
	//document.getElementById("reset_pwd_member_id").setAttribute("value", memberID);		
	document.getElementById("member_delete_name").setAttribute("value", memberName);
	
	//get member by id from background
	var actionURL = "../members/getMemberByID.do?";	
	var parameters = "id="+memberID;
      	
	var objHTTP = getXMLHttp();
	
	if (objHTTP)
	{
		objHTTP.open("POST",encodeURI(actionURL),false,"","");
    	objHTTP.setRequestHeader("CONTENT-TYPE","application/x-www-form-urlencoded;charset=UTF-8");
    	objHTTP.send(parameters);    	
    	
    	if(objHTTP.responseText != null)
	 	{
	 		parseAndUpdateMemberDetails(objHTTP.responseText);
	 	}
	}
}

function parseAndUpdateMemberDetails(memberDetails)
{
	//alert("get member details is ' "+memberDetails+" '");
	
	var allFields = memberDetails.split(";");
	

	//chage save button text
	document.getElementById("save_member_button").setAttribute("value","SAVE");
	document.getElementById("delete_member_button").disabled=false;
	//document.getElementById("reset_pwd").disabled=false;	
	document.getElementById("operation_type").setAttribute("value", 2);
	
	//clear market id list
	clearMarketIDList();
	
	for(var i=0; i<allFields.length; i++)
	{
		var subField = allFields[i].split("=");
		
		if(subField.length != 2)
		{
			continue;
		}
		else
		{ 
			parseAndUpdateMemberFields(subField);
		}
	}
}

function resetMemberDetails()
{
	window.location.href='../members/getMembers.do';	
}

function setRegTime()
{
	var currentTime = getCurrentTime();
	document.getElementById("member_signup_date").setAttribute("value",currentTime);   
}

function setExpiredTime()
{
	var currentTime = getCurrentTime();    
    document.getElementById("member_expired_date").setAttribute("value",currentTime);    
}

function getCurrentTime()
{
	var today = new Date();   
    var day = today.getDate();   
    var month = today.getMonth() + 1;   
    var year = today.getFullYear();   
    
    var hour = today.getHours();
    var minute = today.getMinutes();
    var second = today.getSeconds();
    
    var currentDateString = year + "-" + month + "-" + day + " "+hour+":"+minute+":"+second;
    
    return currentDateString;
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

function initSelectAllCheckBoxStatus()
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

function getCheckedMarkets()
{
	var allMarketIDsArray = allMarketIDs.split(",");
	var subscribledMarketIDs = '';
		
	for(var i=0; i<allMarketIDsArray.length; i++)
	{
		var marketCheckBoxID = "member_market"+allMarketIDsArray[i];
		var marketStrategyID = "member_strategy"+allMarketIDsArray[i];
		
		var checkedMarketValue = document.getElementById(marketCheckBoxID).getAttribute("value");
		var marketRelatedStrategyID = getSelectedValue(marketStrategyID);
		
		if( document.getElementById(marketCheckBoxID).checked == true || document.getElementById(marketCheckBoxID).checked == 1 )
		{			
			subscribledMarketIDs += checkedMarketValue+","+marketRelatedStrategyID;
		}
		
		if(i < allMarketIDsArray.length-1)
			subscribledMarketIDs+=";";
	}
	
	return subscribledMarketIDs;
}

function parseAndUpdateMemberFields(subField)
{			
	if(subField[0] == "email")
	{
		document.getElementById("member_email").setAttribute("value",subField[1]);
		document.getElementById("member_password").setAttribute("value","********");
	}
	else if(subField[0] == "memberType")
	{
		var memberTypeID = "member_type"+subField[1];
		document.getElementById(memberTypeID).checked = true;	
		document.getElementById("member_type_value").setAttribute("value",subField[1]);
		
		//alert(memberTypeID+"="+document.getElementById(memberTypeID).getAttribute("checked"));		
	}
	else if(subField[0] == "memberLevel")
	{
		var memberLevelID = "product_id"+subField[1];
		document.getElementById(memberLevelID).selected=true;				
	}
	else if(subField[0] == "first_name")
	{
		document.getElementById("member_first_name").setAttribute("value",subField[1]);
	}
	else if(subField[0] == "last_name")
	{
		document.getElementById("member_last_name").setAttribute("value",subField[1]);
	}	
	else if(subField[0] == "suburb_city")
	{
		document.getElementById("member_city").setAttribute("value",subField[1]);
	}
	else if(subField[0] == "street_address")
	{
		document.getElementById("member_street").setAttribute("value",subField[1]);
	}
	else if(subField[0] == "state")
	{
		document.getElementById("member_state").setAttribute("value",subField[1]);
	}
	else if(subField[0] == "postCode")
	{
		document.getElementById("member_zip_code").setAttribute("value",subField[1]);
	}
	else if(subField[0] == "mobile")
	{
		document.getElementById("member_mobile").setAttribute("value",subField[1]);
	}
	else if(subField[0] == "country_id")
	{
		var memberCountryID = "member_country"+subField[1];
		document.getElementById(memberCountryID).selected=true;
	}
	else if(subField[0] == "time_zone_id")
	{
		var memberTimeZoneID = "member_timezone"+subField[1];

		if(document.getElementById(memberTimeZoneID) != null)
		{
			document.getElementById(memberTimeZoneID).selected=true;
		}
	}
	else if(subField[0] == "status")
	{
		var memberStatusID = "member_billing_status"+subField[1];
		document.getElementById(memberStatusID).selected=true;
	}
	else if(subField[0] == "reg_date")
	{
		document.getElementById("member_signup_date").setAttribute("value",subField[1]);
	}
	else if(subField[0] == "expired_date")
	{
		document.getElementById("member_expired_date").setAttribute("value",subField[1]);
	}
	else if(subField[0] == "sms_credits")
	{
		document.getElementById("member_sms_credits").setAttribute("value",subField[1]);
	}
	else if(subField[0] == "markets")
	{
		var marketIDs = subField[1].split(",");

		for(var i=0; i<marketIDs.length; i++)
		{
			var marketStrategy = marketIDs[i].split("-");
			var marketID = marketStrategy[0];
			var strategyID = marketStrategy[1];
			
			var marketCheckBoxID = "member_market"+marketID;
			
			if(document.getElementById(marketCheckBoxID) != null)
			{
				document.getElementById(marketCheckBoxID).checked = true;
			}
			
			var strategyObjID = "member_strategy"+marketID;
			if(document.getElementById(strategyObjID) != null)
			{
				document.getElementById(strategyObjID).value = strategyID;
			}
		}
		
		setSubscribledMarket();
		
		initSelectAllCheckBoxStatus();
		
		//alert("set Subscribled Market are: "+document.getElementById("member_subscription_markets").getAttribute("value"));
	}
}

function setSubscribledMarket()
{
	document.getElementById("member_subscription_markets").setAttribute("value",getCheckedMarkets());
}

function resetMemberPWD()
{
	//set new pwd
	var newPwd = document.getElementById("member_password").value;
	document.getElementById("reset_new_pwd").setAttribute("value", newPwd);
		
	document.resetMemberPWDForm.submit();
}

function selectMemberType(memberTypeField)
{	
	document.getElementById("member_type_value").setAttribute("value",memberTypeField.getAttribute("value"));
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
		  			<li class="midTopTitle">&nbsp; Members &nbsp;<input type="submit" onclick="resetMemberDetails();" name="submit" value="ADD NEW MEMBER" style="cursor:pointer"/></li>
		  			<li class="midTopTitle2"></li>
		  			<li class="rightTopTitle">&nbsp;Members Details 
		  				&nbsp;<input id="save_member_button"  type="submit" onclick="document.memberDetailsForm.submit();" name="submit" value="ADD" style="cursor:pointer" />
		  				&nbsp;<input id="delete_member_button" disabled="true" type="submit" onclick="document.deleteMemberForm.submit();" name="submit" value="DELETE" style="cursor:pointer"/>
		  			</li>
	  			</ul>
	  		</div>
			<div id="content">  
          	<div id="contentLeft">
							<div class="leftMidBg">
								<div id="leftmenu">
									<ul>
										<li><a href="#"><span>Dashboard</span></a></li>
										<li class="navon"><span>Members</span></li>
										<li><a href="#"><span>Billing</span></a></li>
										<li><a href="#"><span>Expenses</span></a></li>
										<li><a href="#"><span>Coupon Codes</span></a></li>
										<li><html:link forward="getSMSCredits"><span>SMS Credits</span></html:link></li>
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
					 <div id="downLeftContent">
					 		<div class="graphicbox">
					 			<div id="searchCondition">
					 			<html:form action="/member/searchMember">
						 			<ul>
										<li class="searchtitle">Search Name:</li>
										<li><input name="search_name" type="text" class="iLongBox"/></li>									
									</ul>
									<ul>
										<li class="searchtitle">Search Email:</li>
										<li><input name="search_email" type="text" class="iLongBox"/></li>
									</ul>
									<div id="searchButton">
										<ul>
											<li><input type="submit" name="submit" value="SEARCH" style="cursor:pointer"/></li>
										</ul>
									</div>
								</html:form>
								</div>									
								<div class="searchresult">
									<ul>
										<html:form action="/members/sortMember">
										<li class="sortlist">
											<select id="sort_condition" name="member_sort_type" property="member_sort_type" onChange="form.submit();">
											
												<%if(memberSortType == AdminConstants.SORT_MEMBER_A_TO_Z){ %>
												<option id="member_sort_type_a_z" value="1" text="" selected >Member Name A - Z</option>
												<%}else{ %>
												<option id="member_sort_type_a_z" value="1" text="" >Member Name A - Z</option>
												<%}%>
												
												<%if(memberSortType == AdminConstants.SORT_MEMBER_Z_TO_A){ %>
												<option id="member_sort_type_z_a" value="2" text="" selected >Member Name Z - A</option>
												<%}else{ %>
												<option id="member_sort_type_z_a" value="2" text="">Member Name Z - A</option>
												<%}%>
												
												<%if(memberSortType == AdminConstants.SORT_MEMBER_SIGNUP_NEWEST){ %>
												<option id="member_sort_type_signup_newest" value="3" text="" selected >Signup Date Newest</option>
												<%}else{ %>
												<option id="member_sort_type_signup_newest" value="3" text="">Signup Date Newest</option>
												<%}%>
												
												<%if(memberSortType == AdminConstants.SORT_MEMBER_SIGNUP_OLDEST){ %>
												<option id="member_sort_type_signup_oldest" value="4" text="" selected >Signup Date Oldest</option>
												<%}else{ %>
												<option id="member_sort_type_signup_oldest" value="4" text="">Signup Date Oldest</option>
												<%}%>
												
												<%if(memberSortType == AdminConstants.SORT_MEMBER_PRODUCT_PLAN){ %>
												<option id="member_sort_type_product_plan" value="5" text="" selected >Product Plan</option>
												<%}else{ %>
												<option id="member_sort_type_product_plan" value="5" text="">Product Plan</option>
												<%}%>
												
												<%if(memberSortType == AdminConstants.SORT_MEMBER_CANCELLED){ %>
												<option id="member_sort_type_cancelled" value="6" text="" selected >Cancelled</option>
												<%}else{ %>
												<option id="member_sort_type_cancelled" value="6" text="">Cancelled</option>
												<%}%>
												
												<%if(memberSortType == AdminConstants.SORT_MEMBER_SMS){ %>
												<option id="member_sort_type_sms" value="7" text="" selected >Value SMS + Plan</option>
												<%}else{ %>
												<option id="member_sort_type_sms" value="7" text="">Value SMS + Plan</option>
												<%}%>
												
												<%if(memberSortType == AdminConstants.SORT_REGISTERED_MEMBER){ %>
												<option id="member_sort_type_sms" value="8" text="" selected >Registered Member</option>
												<%}else{ %>
												<option id="member_sort_type_sms" value="8" text="">Registered Member</option>
												<%}%>
												
												<%if(memberSortType == AdminConstants.SORT_UNREGISTERED_MEMBER){ %>
												<option id="member_sort_type_sms" value="9" text="" selected >Unregistered Member</option>
												<%}else{ %>
												<option id="member_sort_type_sms" value="9" text="">Unregistered Member</option>
												<%}%>
												
											</select>	
										</li>										
										<li class="pagelist"> < 1 2 3 4 > </li>
										</html:form>
									</ul>
									<ul>
										<logic:iterate id="userbean" name="member_list" type="com.cleartraders.common.entity.UserBean" indexId="count"> 
											<li class="membername"><a href="#" onclick="showMemberDetails(${userbean.id}, '${userbean.login_name}')">${userbean.login_name}</a></li>
										</logic:iterate>
									</ul>									
								</div>
					 		</div>
					 </div>
					 <div id="downRightContent">
					 		<div class="graphicbox">
					 		
					 			<html:form action="/members/deleteMember">
					 				<input type="hidden" id="member_delete_id" name="member_delete_id" value="-1" />
					 				<input type="hidden" id="member_delete_name" name="member_delete_name" value="" />
					 			</html:form>
					 			
					 			<html:form action="/members/resetMemberPWD">
					 				<input type="hidden" id="reset_pwd_member_id" name="reset_pwd_member_id" value="-1" />
					 				<input type="hidden" id="reset_new_pwd" name="reset_new_pwd" value="" />
					 			</html:form>
					 			
					 			<html:form action="/members/addOrUpdateMemberDetails">
					 			<input type="hidden" id="operation_type" name="operation_type" value="1" />
					 			<input type="hidden" id="member_type_value" name="member_type_value" value="1" />
					 			<input type="hidden" id="member_subscription_markets" name="member_subscription_markets" value="" />
					 			<input type="hidden" id="member_id" name="member_id" value="-1" />
					 			
					 			<div id="memberdetailsstyle">
					 				<span class="errorinfo"><html:errors property="error"/></span>
						      		<span class="promptinfo"><html:errors property="prompt_info"/></span>
						 			<ul>
						 				<li class="titlestyle"><p>User Type</p></li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="membertype"/></span>
						 				<li class="commonfont">
						 				  <input id="member_type1" name="usertype" type="radio" value="1"  onclick="selectMemberType(this)" checked />Standard	
						 				  <input id="member_type2" name="usertype" type="radio" value="2"  onclick="selectMemberType(this)" />Sales	
						 				  <input id="member_type3" name="usertype" type="radio" value="3"  onclick="selectMemberType(this)" />Support
						 				  <input id="member_type4" name="usertype" type="radio" value="4"  onclick="selectMemberType(this)" />Admin
						 				</li>	
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="memberlevel"/></span>
						 				<li class="titlestyle"><p>Product Plan</p></li>
						 				<li>
							 				<select id="member_plan" name="member_plan" property="member_plan">
							 				    <%
											    	for(int i=0; i<allProducts.size(); i++)
											    	{
											    		ProductBean productBean = allProducts.get(i);
											    		String product_option_id = "product_id"+productBean.getId();
											     %>
													<option id="<%=product_option_id%>" value="<%=productBean.getId()%>" ><%=productBean.getName()%></option>
												<%  }%>
											</select>	
										</li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="email"/></span>
						 				<li class="titlestyle"><p>Email</p></li>
						 				<li><input id="member_email" name="member_email" type="text" class="" value="" /></li>
						 			</ul>
						 		  <ul>
						 		  		<span class="errorinfo"><html:errors property="password"/></span>
						 				<li class="titlestyle"><p>Password</p></li>
						 				<li>
						 					<input id="member_password" name="member_password" type="password" value="" style="width:153px;" />
						 					<!-- input disabled="true" id="reset_pwd" name="reset_pwd" type="reset" onClick="resetMemberPWD();" value="RESET" style="cursor:pointer" /-->
						 				</li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="firstname"/></span>
						 				<li class="titlestyle"><p>First Name</p></li>
						 				<li><input id="member_first_name" name="member_first_name" type="text" value="" class=""/></li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="lastname"/></span>
						 				<li class="titlestyle"><p>Last Name</p></li>
						 				<li><input id="member_last_name" name="member_last_name" type="text" value="" class=""/></li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="mobilenumber"/></span>
						 				<li class="titlestyle"><p>Mobile</p></li>
						 				<li><input id="member_mobile" name="member_mobile" type="text" value="" class="" /></li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="countryid"/></span>
						 				<li class="titlestyle"><p>Country</p></li>
						 				<li class="">
							 				<select id="member_country" name="member_country" property="member_country">
									 			<%
											    	for(int i=0; i<allCountry.size(); i++)
											    	{
											    		CountryBean countryBean = allCountry.get(i);
											    		String countryID = "member_country"+countryBean.getId();
											     	%>
											     	<option id="<%=countryID%>" value="<%=countryBean.getId()%>"><%=countryBean.getName()%></option>
												<%  }%>
											</select>	
										</li>
						 			</ul>
						 			
						 			<ul>
						 				<span class="errorinfo"><html:errors property="timezone"/></span>
						 				<li class="titlestyle"><p>Time Zone</p></li>
						 				<li class="">
							 				<select id="member_timezone" name="member_timezone" property="member_timezone">
									 			<%
											    	for(int i=0; i<allTimezone.size(); i++)
											    	{
											    		TimeZoneBean timeZoneBean = allTimezone.get(i);
											    		String timezoneID = "member_timezone"+timeZoneBean.getId();
											     	%>
											     	<option id="<%=timezoneID%>" value="<%=timeZoneBean.getId()%>"><%=timeZoneBean.getName()%></option>
												<%  }%>
											</select>	
										</li>
						 			</ul>
						 			
						 			<ul>
						 				<span class="errorinfo"><html:errors property="firstname"/></span>
						 				<li class="titlestyle"><p>Street</p></li>
						 				<li><input disabled="true" id="member_street" name="member_first_name" type="text" value="" class=""/></li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="lastname"/></span>
						 				<li class="titlestyle"><p>City</p></li>
						 				<li><input disabled="true" id="member_city" name="member_last_name" type="text" value="" class=""/></li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="firstname"/></span>
						 				<li class="titlestyle"><p>State</p></li>
						 				<li><input disabled="true" id="member_state" name="member_first_name" type="text" value="" class=""/></li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="lastname"/></span>
						 				<li class="titlestyle"><p>Zip Code</p></li>
						 				<li><input disabled="true" id="member_zip_code" name="member_last_name" type="text" value="" class=""/></li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="expireddate"/></span>
						 				<li class="titlestyle"><p>Expired Date</p></li>
						 				<li><input id="member_expired_date" name="member_expired_date" onclick="setExpiredTime();" type="text" value="" class=""/></li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="signupdate"/></span>
						 				<li class="titlestyle"><p>Signup Date</p></li>
						 				<li><input id="member_signup_date" name="member_signup_date" onclick="setRegTime();" type="text" value="" class=""/></li>
						 			</ul>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="billingstatus"/></span>
						 				<li class="titlestyle"><p>Billing Status</p></li>
						 				<li>
							 				<select id="member_billing_status" name="member_billing_status" property="member_billing_status">
							 						<option id="member_billing_status0" value="0" text="">Unregistered</option>
													<option id="member_billing_status1" value="1" text="">Registered</option>
													<option id="member_billing_status2" value="2" text="">Unpaid</option>
													<option id="member_billing_status3" value="3" text="">Paid</option>
													<option id="member_billing_status4" value="4" text="">Cancelled</option>
													<option id="member_billing_status5" value="5" text="">Free Trial Ended</option>
											</select>
										</li>
						 			</ul>
						 			<ul>
						 				<li class="titlestyle"><p>Markets</p></li>
						 				<li class="commonfont">
						 				  <input id="select_all_markets" name="select_all_markets" type="checkbox" value="" onclick="selectAllMarkets(this)" />Select All Markets
						 				</li>
						 			</ul>
						 			<%
								    	for(int i=0; i<allMarkets.size(); i++)
								    	{
								    		MarketTypeBean marketBean = allMarkets.get(i);
								    		String marketID = "member_market"+marketBean.getId();
								    		
								    		List<StrategyBean> relatedStrategy = allMarketStrategy.get(Long.valueOf(marketBean.getId()));
								    		if( null == relatedStrategy )
								    		{
								    			relatedStrategy = new ArrayList<StrategyBean>();
								    		}
								    					  						  	 
				  						  	String strategyID = "member_strategy"+marketBean.getId();
			  						  	
								     	%>
							 			<ul>
							 				<li class="titlestyle">&nbsp;</li>
							 				<li class="commonfont">
							 					<input id="<%=marketID%>" name="member_market_options" type="checkbox" value="<%=marketBean.getId()%>" onclick="setSubscribledMarket();" /><%=marketBean.getDisplay_name()%>
							 					<select id="<%=strategyID%>" onchange="setSubscribledMarket();">
													<%
													for (int j=0; j<relatedStrategy.size(); j++)
													{
														StrategyBean strategy = relatedStrategy.get(j);
														if(strategy.getActive() == CommonDefine.ACTIVE_STRATEGY)
			  						  					{
														%>
														<option value="<%=strategy.getId()%>" selected="1"><%=strategy.getCommon_name()%></option>
													<%
														}
														else if(strategy.getActive() == CommonDefine.ACTIVE_STRATEGY)
														{
														%>
														<option value="<%=strategy.getId()%>"><%=strategy.getCommon_name()%></option>
													<%
														}
													}%>
												</select>
							 				</li>	
							 			</ul>
						 			<%  }%>
						 			<ul>
						 				<span class="errorinfo"><html:errors property="smscredits"/></span>
						 				<li class="titlestyle"><p>SMS Credits</p></li>
						 				<li><input id="member_sms_credits" name="member_sms_credits" type="text" value="0" class=""/></li>
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
