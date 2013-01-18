	
	function activeSMSOrEmail(type,id)
	{
		var idArray = id.split(":");
		var signal_preference_id = idArray[1];
		
		var objBox = document.getElementById(id);
		var objField;
		if(type==1)
		{
		    objField = document.getElementById("emailEnableField:"+idArray[1]);
		}
		else
		{
			objField = document.getElementById("smsEnableField:"+idArray[1]);
		}
		
		var activeBox = document.getElementById(idArray[1]);
		
		if(objBox.getAttribute("value") != "3" && activeBox.getAttribute("value") == "1")
		{
			if(objBox.getAttribute("value") == "2")
			{
				//alert('acitve');
				//active
				objBox.setAttribute("src","../images/dot_check2.gif");	
				objBox.setAttribute("value","1");
				objField.setAttribute("value","1");
			}
			else
			{
				//alert('disable');
				//disable
				objBox.setAttribute("src","../images/dot_check.gif");	
				objBox.setAttribute("value","2");
				objField.setAttribute("value","2");
				//alert(objField.getAttribute("value"));
			}
		}
		
		updateAllSignalSettingString(signal_preference_id);
		
		submitSaveAllForm();
	}
	
	function activeMarket(id)
	{	
		var activeBox=document.getElementById(id);
		var activeField=document.getElementById("Field"+id);
		
		var emailBox = document.getElementById("emailEnableFlag:"+id);
		var emailField = document.getElementById("emailEnableField:"+id);
		
		var smsBox = document.getElementById("smsEnableFlag:"+id);
		var smsField = document.getElementById("smsEnableField:"+id);
		
		if(activeBox.getAttribute("value") == "2")
		{
			if(currentActivedMarketAmount >= totalMarketsOfUser)
			{
				return;
			}
			else
			{
				currentActivedMarketAmount++;
			}
			
			//active
			activeBox.setAttribute("src","../images/dot_check2.gif");	
			activeBox.setAttribute("value","1");
			activeField.setAttribute("value","1");
			
			if(emailBox.getAttribute("value") == "3")
			{
				emailBox.setAttribute("src","../images/dot_check2.gif");	
				emailBox.setAttribute("value","1");
				emailField.setAttribute("value","1");
			}
			
			if(smsBox.getAttribute("value") == "3")
			{
				smsBox.setAttribute("src","../images/dot_check2.gif");	
				smsBox.setAttribute("value","1");
				smsField.setAttribute("value","1");
			}
		}
		else
		{
			currentActivedMarketAmount--;
			
			//disable
			activeBox.setAttribute("src","../images/dot_check.gif");	
			activeBox.setAttribute("value","2");
			activeField.setAttribute("value","2");
			
			if(emailBox.getAttribute("value") == "1")
			{
				emailBox.setAttribute("src","../images/dot_check3.gif");	
				emailBox.setAttribute("value","3");	
				emailField.setAttribute("value","1");
			}
			
			if(smsBox.getAttribute("value") == "1")
			{
				smsBox.setAttribute("src","../images/dot_check3.gif");	
				smsBox.setAttribute("value","3");
				smsField.setAttribute("value","1");
			}
		}
				
		updateAllSignalSettingString(id);
		
		submitSaveAllForm();
	}
	
	function updateAllSignalSettingString(id)
	{
		//the ID is the signal_preference_id
		//So, update the all_signal_setting_value first
		var all_signal_setting_value = document.getElementById("all_signal_setting_value").getAttribute("value");		
		//alert(all_signal_setting_value);		
		var all_single_setting_list =  all_signal_setting_value.split(";");
		
		var new_all_signal_setting_value = "";
		for(var i=0; i<all_single_setting_list.length; i++)
		{
			if(all_single_setting_list[i] != '')
			{
				var all_single_setting = all_single_setting_list[i].split(",")
				if(all_single_setting[0] == id)
				{
					//alert('this one '+all_single_setting_list[i]);					
					//update
					var new_active_flag = document.getElementById("Field"+id).getAttribute("value");
					var new_email_flag = document.getElementById("emailEnableField:"+id).getAttribute("value");
					var new_sms_flag = document.getElementById("smsEnableField:"+id).getAttribute("value");
					
					all_single_setting_list[i] = all_single_setting[0]+","+new_active_flag+","+new_email_flag+","+new_sms_flag;
				}
				
				new_all_signal_setting_value = new_all_signal_setting_value + all_single_setting_list[i]+";";
			}
		}
		
		document.getElementById("all_signal_setting_value").setAttribute("value",new_all_signal_setting_value);		
		//alert(document.getElementById("all_signal_setting_value").getAttribute("value"));
	}
	
	function submitChangeForm(id)
	{
		var formOperationID="operationType"+id;
		var formOperation = document.getElementById(formOperationID);
		formOperation.setAttribute("value","1");
		
		var formID="form"+id;
		document.getElementById(formID).submit();
	}
	
	function submitRemoveForm(id)
	{
		var formOperationID="operationType"+id;
		var formOperation = document.getElementById(formOperationID);
		formOperation.setAttribute("value","2");		
		
		var formID="form"+id;
		document.getElementById(formID).submit();
	}
	
	function submitSaveAllForm()
	{		
		//document.getElementById('all_signals_form').submit();
		
		var all_signal_setting_value = document.getElementById("all_signal_setting_value").getAttribute("value");
			
		var shortURL = "updateMyMarketSettings.do?value="+all_signal_setting_value;

		var objHTTP = getXMLHttp();
		if (objHTTP) 
		{
			objHTTP.open("POST", shortURL, true);
			objHTTP.send(null);
		}
	}
	
	function changeStrategy(perferenceID, strategyObjID, timeFrameObjID)
	{
		var strategyObj = document.getElementById(strategyObjID);
		var timeFrameObj = document.getElementById(timeFrameObjID);
		
		var strategyID = strategyObj.value;
		
		if(strategyObj && timeFrameObj)
		{
			//clear time frame
			timeFrameObj.options.length = 0;
						
			//set related timeframe
			setTimeFrameOptions(timeFrameObj, getStrategyTimeFrameStringByID(strategyID));
			
			//update strategy details if opened
			var strategyTypeID = perferenceID+":"+strategyID;
			if(document.getElementById(strategyTypeID))
			{
				document.getElementById(strategyTypeID).checked=true;
			}
			
			//update strategy setting on background
			updateBackgroundStrategy(perferenceID, strategyID);
			
			//update related time frame
			updateBackgroundTimeframe(perferenceID, document.getElementById(timeFrameObjID).value);
		}
	}
	
	function updateBackgroundStrategy(perferenceID, strategyID)
	{
		var shortURL = "updateMyMarketStrategy.do?id="+perferenceID+"&value="+strategyID;

		var objHTTP = getXMLHttp();
		if (objHTTP) 
		{
			objHTTP.open("POST", shortURL, true);
			objHTTP.send(null);
		}
	}
	
	function updateBackgroundTimeframe(perferenceID, timeFrameID)
	{
		var shortURL = "updateMyMarketTimeframe.do?id="+perferenceID+"&value="+timeFrameID;

		var objHTTP = getXMLHttp();
		
		if (objHTTP) 
		{
			objHTTP.open("POST", shortURL, true);
			objHTTP.send(null);
		}
	}
	
	function getStrategyTimeFrameStringByID(strategyID)
	{			
		var allStrategyTimeFrameArray = allStrategyTimeFrameString.split(";");
		
		for(var i=0; i<allStrategyTimeFrameArray.length; i++)
		{
			var strategyTimeframeInfoArray = allStrategyTimeFrameArray[i].split("=");
			
			if(strategyTimeframeInfoArray && strategyTimeframeInfoArray.length==2 && strategyTimeframeInfoArray[0] == strategyID)
			{
				return strategyTimeframeInfoArray[1];
			}
		}
		
		return "";
	}
	
	function setTimeFrameOptions(timeFrameObj, strategyTimeFrames)
	{
		var timeFrameOptionsHtml = "";
		var strategyTimeFrameArray = strategyTimeFrames.split(',');
		for(var i=0; i<strategyTimeFrameArray.length; i++)
		{
			var timeFrameID = strategyTimeFrameArray[i];
			var timeFrameName = getTimeFrameNameByID(timeFrameID);
			
			if(timeFrameName)
			{
				if (!jsSelectIsExitItem(timeFrameObj, timeFrameID)) 
				{             
			        var varItem = new Option(timeFrameName, timeFrameID);      
			        timeFrameObj.options.add(varItem);     
				}
			 }
		}
	}
	
	function jsSelectIsExitItem(objSelect, objItemValue) 
	{        
	    var isExit = false;        
	    for (var i = 0; i < objSelect.options.length; i++) {        
	        if (objSelect.options[i].value == objItemValue) {        
	            isExit = true;        
	            break;        
	        }        
	    }        
	    return isExit;        
	} 
	
	function changeTimeFrameOnStrategy(id)
	{
		var referenceID = "";
		var strategyID = "";
		
		if(id && id.split(":").length == 3)
		{
			referenceID = id.split(":")[0];
			strategyID = id.split(":")[1];
		}
		
		if(document.getElementById(referenceID+":"+strategyID) && document.getElementById(referenceID+":"+strategyID).checked == true)
		{
			//only update market timeframe while this is selected
			if(document.getElementById("timeFrame:"+referenceID) && document.getElementById(id))
			{
				document.getElementById("timeFrame:"+referenceID).value = document.getElementById(id).value;
				
				//update related time frame
				updateBackgroundTimeframe(referenceID, document.getElementById(id).value);
			}
		}
	}
	
	function changeTimeFrame(id)
	{
		var referenceID = "";
		
		if(id != null)
		{
			referenceID = id.split(":")[1];
		}
		
		//get current strategy ID
		var strategyID=0;
		if(document.getElementById("strategy:"+referenceID))
		{
			strategyID = document.getElementById("strategy:"+referenceID).value;
		}
		
		var strategyDetailsTimeframeObj = document.getElementById(referenceID+":"+strategyID+":timeframe");
		if(strategyDetailsTimeframeObj)
		{
			strategyDetailsTimeframeObj.value = document.getElementById(id).value;
		}
		
		//update related time frame
		updateBackgroundTimeframe(referenceID, getSelectedValue(id));
	}
	
	function hideShowStrategy(preferenceID, source, strategyIDs, currentStrategy)
	{
		var statusObj = document.getElementById("hideShowStatus:"+preferenceID);
		var promptTextObj = document.getElementById("hideShowStrategyText:"+preferenceID);
		var imageObj = document.getElementById("hideShowStrategyImage:"+preferenceID);
		var imageObjParentID = document.getElementById("hideShowStrategyImage:"+preferenceID+"parent");
		
		var strategyDivObj = document.getElementById("strategydiv:"+preferenceID);
		var strategyLiObj = document.getElementById("strategyli:"+preferenceID);
		var strategyLiID = "strategyli:"+preferenceID;
		
		if(statusObj != null && promptTextObj != null && imageObj != null && imageObjParentID != null)
		{
			if(statusObj.getAttribute("value") == "1")
			{
				//current is Hide, will Show strategy
				statusObj.setAttribute("value","2");
				
				//imageObj.setAttribute("src","../images/hidechart1.gif");
				imageObj.setAttribute("src","../images/nav1_trans.gif");
				imageObj.setAttribute("alt","Hide Strategy");
				imageObj.setAttribute("title","Hide Strategy");
				
				if(isIE())
				{
					imageObjParentID.className = "hide_strategy_btn";
					promptTextObj.innerText="Hide Strategy";
				}
				else
				{
					imageObjParentID.className = "hide_strategy_btn";
					promptTextObj.textContent="Hide Strategy";
				}
				
				//show strategy
				showStrategies(preferenceID, strategyDivObj, strategyLiID, strategyIDs, currentStrategy);
			}
			else if (imageObjParentID != null)
			{
				//current is Show, will Hide strategy
				statusObj.setAttribute("value","1");
				
				//imageObj.setAttribute("src","../images/hidechart2.gif");
				imageObj.setAttribute("src","../images/nav1_trans.gif");
				imageObj.setAttribute("alt","Show Strategy");
				imageObj.setAttribute("title","Show Strategy");
				
				if(isIE())
				{
					imageObjParentID.className = "show_strategy_btn";
					promptTextObj.innerText="Show Strategy";
				}
				else
				{
					imageObjParentID.className = "show_strategy_btn";
					promptTextObj.textContent="Show Strategy";
				}
				
				//hide strategy
				if(strategyDivObj && strategyLiObj)
				{
					strategyDivObj.style.display = '';
					strategyDivObj.removeChild(strategyLiObj);
				}
			}
		}
	}
	
	function showStrategies(preferenceID, strategyDivObj, strategyLiID, strategyIDs, currentStrategy)
	{	
		var newLi = "<div class='bodyWrap' id='"+strategyLiID+"'></div>";		
		insertHtml("BeforeEnd",strategyDivObj,newLi);	
		
		var strategyLiObj = document.getElementById(strategyLiID);
		
		var strategyIDArray = strategyIDs.split(',');
		var isLeft=true;
		
		for(var i=0; i<strategyIDArray.length; i++)
		{
			if(strategyIDArray[i].length > 0)
			{
				var styleName = '';
				if(isLeft)
				{
					isLeft = false;
					styleName = 'left';
				}
				else
				{
					isLeft = true;
					styleName = 'right';
				}
				
				var strategyContentHTML = createStrategyHTML(preferenceID, strategyIDArray[i], styleName, currentStrategy);
				insertHtml("BeforeEnd",strategyLiObj,strategyContentHTML);	
			}
		}
	}
		
	function updateMarketStrategy(marketStrategyDetailsID)
	{
		if(marketStrategyDetailsID && marketStrategyDetailsID.split(":").length == 2)
		{
			var preferenceID = marketStrategyDetailsID.split(":")[0];
			var strategyID = marketStrategyDetailsID.split(":")[1];
			
			var marketStrategyObj = document.getElementById("strategy:"+preferenceID);
			var marketTimeframeObj = document.getElementById("timeFrame:"+preferenceID);
			var strategyTimeframeObj = document.getElementById(marketStrategyDetailsID+":timeframe");
			
			if(marketStrategyObj && marketTimeframeObj && strategyTimeframeObj)
			{
				marketStrategyObj.value = strategyID;
				changeStrategy(preferenceID,"strategy:"+preferenceID,"timeFrame:"+preferenceID);
				
				//strategyTimeframeObj.removeAttribute("disabled");
			}
		}
	}
	
	function createStrategyHTML(preferenceID, strategyID, styleName, currentStrategy)
	{
		var strategyDetails = getStrategyByID(strategyID);
		var strategyName = getStrategyAttributeValue(strategyDetails,"common_name");
		var strategyDescription = getStrategyAttributeValue(strategyDetails,"description");
		var strategyLinkURL = getStrategyAttributeValue(strategyDetails,"link_url");
		var strategyTimeFrames = getStrategyAttributeValue(strategyDetails,"related_timeframes");
		
		//alert(strategyName + " " + strategyDescription + " " + strategyLinkURL);
		
		var strategyTypeID = preferenceID+":"+strategyID;
		var strategyTimeframeID = preferenceID+":"+strategyID+":timeframe";
		
		var timeFrameOptions = getTimeFrameOptions(strategyTimeFrames);
		
		var radioHtml = "<li class='names'><input id='"+strategyTypeID+"' name='"+preferenceID+"' type='radio' value='1' onclick='updateMarketStrategy(id)' /><span style='margin-left:10px;'>"+strategyName+"</span></li>";
		var selectHtml = "<li class='times'><select id='"+strategyTimeframeID+"' onchange='changeTimeFrameOnStrategy(id)'>"+timeFrameOptions+"</select></li>";
		if(currentStrategy == strategyID)
		{
			styleName = styleName+"selected";
			radioHtml = "<li class='names'><input id='"+strategyTypeID+"' name='"+preferenceID+"' type='radio' value='1' onclick='updateMarketStrategy(id)' checked /><span style='margin-left:10px;'>"+strategyName+"</span></li>";
			selectHtml = "<li class='times'><select id='"+strategyTimeframeID+"' onchange='changeTimeFrameOnStrategy(id)'>"+timeFrameOptions+"</select></li>";
		}
		
		var strategyThickBoxURL = strategyLinkURL+"?keepThis=true&TB_iframe=true&height=340&width=560";
		
		var strategyHtml = "<div class='"+styleName+"'>"+
							"<ul>"+
							radioHtml+
							selectHtml+
							"<li class='contents'>"+strategyDescription+"</li>"+
							
							//"<li class='learnmore'><a href='"+strategyLinkURL+"'><img src='../images/learnmor_btn.gif' width='110' height='20' border='0' /></a></li>"+
							
							"<li class='learnmore'>"+
							//"<a href='"+strategyThickBoxURL+"' title='"+strategyName+"' class='thickbox'>"+
							//   "<img src='../images/learnmor_btn.gif' width='110' height='20' border='0' />"+
							//"</a>"+
							
							"<img src='../images/learnmor_btn.gif' width='110' height='20' border='0' onclick=\"tb_show('"+strategyName+"','"+strategyThickBoxURL+"',false)\" style='cursor:pointer' />"+
							
							"</li>"+
							"</ul>"+
							"</div>";
		
		return strategyHtml;
	}
	
	function getTimeFrameOptions(strategyTimeFrames)
	{
		var timeFrameOptionsHtml = "";
		var strategyTimeFrameArray = strategyTimeFrames.split(',');
		for(var i=0; i<strategyTimeFrameArray.length; i++)
		{
			var timeFrameID = strategyTimeFrameArray[i];
			var timeFrameName = getTimeFrameNameByID(timeFrameID);
			
			if(timeFrameName)
				timeFrameOptionsHtml += "<option value='"+timeFrameID+"'>"+timeFrameName+"</option>";
		}
								   		
		return timeFrameOptionsHtml;
	}
	
	function getTimeFrameNameByID(timeFrameID)
	{
		var allTimeFrameStingArray = allTimeFrameSting.split(';');
		for(var i=0; i<allTimeFrameStingArray.length; i++)
		{
			var timeFrameStringArray = allTimeFrameStingArray[i].split('=');
			
			if(timeFrameStringArray.length == 2 && timeFrameID == timeFrameStringArray[0])
			{
				return timeFrameStringArray[1]
			}
		}
		
		return null;
	}
	
	function getStrategyAttributeValue(strategyDetails, strategyAttribute)
	{
		var allFields = strategyDetails.split(";");
		
		for(var i=0; i<allFields.length; i++)
		{
			var subField = allFields[i].split("=");
			
			if(subField.length != 2)
			{
				continue;
			}
			else
			{ 
				if(subField[0] == strategyAttribute)
				{
					return subField[1];
				}
			}
		}
		
		return "";
	}
	
	function getStrategyByID(strategyID)
	{
		var actionURL = "../strategy/getStrategyByID.do?";	
		var parameters = "id="+strategyID;
	      	
		var objHTTP = getXMLHttp();
		var result="";
		
		if (objHTTP)
		{
			objHTTP.open("POST",encodeURI(actionURL),false,"","");
	    	objHTTP.setRequestHeader("CONTENT-TYPE","application/x-www-form-urlencoded;charset=UTF-8");
	    	objHTTP.send(parameters);    	
	    	
	    	if(objHTTP.responseText != null)
		 	{
		 		result = objHTTP.responseText;
		 	}
		}
		
		return result;
	}
	
	function openNewWindow(url,winName,w,h)
	{
		var winl = (screen.width-w)/2;
		var wint = (screen.height-h)/2;
		
		var settings ='height='+h+',';
		settings +='width='+w+',';
		settings +='top='+wint+',';
		settings +='left='+winl+',';
		
		settings +='resizable=no,fullscreen=no,channelmode=no,toolbar=no,location=no,directories=no,menubar=no,scrollbars=nostatus=no,';
          
		window.open(url,winName,settings);
        
		//window.open(url,winName,features); 
	}
	
	function refreshPage()
	{
		setTimeout('reloadPage()',500);		
	}
	
	function reloadPage()
	{
		window.location.href='getMySignals.do';
	}
	
	function getSelectedValue(name)
	{
		return document.getElementById(name).options[document.getElementById(name).options.selectedIndex].value;
	}