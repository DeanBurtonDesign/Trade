    	
	function changeStrategy(perferenceID, strategyObjID, timeFrameObjID)
	{
		var strategyObj = document.getElementById(strategyObjID);
		var timeFrameObj = document.getElementById(timeFrameObjID);
		
		if(strategyObj && timeFrameObj)
		{
			var strategyID = strategyObj.value;		
			var currentTimeFrameID = getSelectedValue(timeFrameObjID);
			
			//clear time frame
			timeFrameObj.options.length = 0;
						
			//set related timeframe
			setTimeFrameOptions(timeFrameObj, currentTimeFrameID, getStrategyTimeFrameStringByID(strategyID));			
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
	
	function setTimeFrameOptions(timeFrameObj, currentTimeFrameID, strategyTimeFrames)
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
			        
			        if(timeFrameID == currentTimeFrameID)
			        {
			        	varItem.selected=true;
			        }
		            
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
	
	function startEngine(eng) 
	{	    
		eng.policy.setMaxBandwidth(30);
		eng.policy.setIdleTimeout(30000);
		eng.policy.setPollingInterval(1000);
		eng.connection.setLSHost("push.cleartraders.com");
		eng.connection.setLSPort(8090);
		eng.connection.setAdapterName("JSSIGNAL");
		eng.changeStatus("STREAMING");
	}
	
	function subscribeJSSignal(jssignal)
	{
		if (jssignal == "") 
		{
			return;
		}
		
		var newTable = new NonVisualTable(jssignal,siganlschema, "DISTINCT");
		
		newTable.setSnapshotRequired(true);
		newTable.setRequestedMaxFrequency(0.5);
		newTable.onItemUpdate = onSignalItemUpdate;
		
		lsPage.addTable(newTable, jssignal);
		
		
	}
	
	function subscribeJSMarketQuote(marketName)
	{
		if (marketName == "") 
		{
			return;
		}
		
		var newTable = new NonVisualTable(marketName,schema, "DISTINCT");
		
		newTable.setSnapshotRequired(true);
		newTable.setRequestedMaxFrequency(0.5);
		newTable.onItemUpdate = onItemUpdate;
		
		lsPage.addTable(newTable, marketName);
	}
	
	function onSignalItemUpdate(itemPos,updateInfo,itemName) 
	{	
		if(updateInfo == null)
		{
			return;
		}
		
		var signal_id =updateInfo.getNewValue(new FieldPositionDescriptor(1));
		var signal_type =updateInfo.getNewValue(new FieldPositionDescriptor(2));
		var market_type_id =updateInfo.getNewValue(new FieldPositionDescriptor(3));
		var signal_period =updateInfo.getNewValue(new FieldPositionDescriptor(4));
		var generate_date =updateInfo.getNewValue(new FieldPositionDescriptor(5));
		var signal_value =updateInfo.getNewValue(new FieldPositionDescriptor(6));
		var expire_date =updateInfo.getNewValue(new FieldPositionDescriptor(7));
		
		var signal_rate =updateInfo.getNewValue(new FieldPositionDescriptor(8));
		var direction =updateInfo.getNewValue(new FieldPositionDescriptor(9));
		var profit =updateInfo.getNewValue(new FieldPositionDescriptor(10));
		var checked =updateInfo.getNewValue(new FieldPositionDescriptor(11));
		var system_name =updateInfo.getNewValue(new FieldPositionDescriptor(12));
		     
		var market_name =updateInfo.getNewValue(new FieldPositionDescriptor(13));
		var market_topic_name =updateInfo.getNewValue(new FieldPositionDescriptor(14));
		var generate_date_string =updateInfo.getNewValue(new FieldPositionDescriptor(15));
		var expire_date_string =updateInfo.getNewValue(new FieldPositionDescriptor(16));
		var profitString =updateInfo.getNewValue(new FieldPositionDescriptor(17));
		
		var signal_period_minutes = updateInfo.getNewValue(new FieldPositionDescriptor(18));
		var strategy_id = updateInfo.getNewValue(new FieldPositionDescriptor(19));
		
		if(checkMarketIsActiveOrNot(market_topic_name) != true)
		{
			return;
		}
		
		if(checkStrategyIDIsActiveOrNot(market_type_id,strategy_id) != true)
		{
			return;
		}
		
		//refresh  multi-frame signals involved
		refreshMultiFrameSignals(market_type_id, signal_period, signal_type,generate_date);
	}
	
	function updateStrategyIDWhenChangeStrategy(market_type_id, new_strategy_id)
	{
		if(AllActiveSignalPerference == null || AllActiveSignalPerference.length < 1)
  	    {
  	    	return false;
  	    }
  	    
  	    var oNewAllActiveSignalPerference = "";
  	    
  	    var oAllActiveSignalPerferenceArray = AllActiveSignalPerference.split(";");
  	    for(var i=0; i<oAllActiveSignalPerferenceArray.length; i++)
  	    {
  	    	var oSingalActiveSignalPerferenceArray = oAllActiveSignalPerferenceArray[i].split(",");
  	    	
  	    	if(oSingalActiveSignalPerferenceArray.length == 3)
  	    	{
  	    		if( oSingalActiveSignalPerferenceArray[0] == market_type_id)
  	    		{
  	    			oNewAllActiveSignalPerference += ( market_type_id + "," + oSingalActiveSignalPerferenceArray[1] + "," + new_strategy_id );
  	    		}  	    		
	  	    	else
	  	    	{
	  	    		oNewAllActiveSignalPerference += ( oSingalActiveSignalPerferenceArray[0] + "," + oSingalActiveSignalPerferenceArray[1] + "," + oSingalActiveSignalPerferenceArray[2] );
	  	    	}
	  	    	
	  	    	if(i < (oAllActiveSignalPerferenceArray.length - 1))
	  	    	{
	  	    		oNewAllActiveSignalPerference += ";";
	  	    	}
  	    	}
  	    }
  	    
  	    AllActiveSignalPerference = oNewAllActiveSignalPerference;
	}
	
	function checkStrategyIDIsActiveOrNot(market_type_id, strategy_id)
	{
		if(AllActiveSignalPerference == null || AllActiveSignalPerference.length < 1)
  	    {
  	    	return false;
  	    }
  	    
  	    var oAllActiveSignalPerferenceArray = AllActiveSignalPerference.split(";");
  	    for(var i=0; i<oAllActiveSignalPerferenceArray.length; i++)
  	    {
  	    	var oSingalActiveSignalPerferenceArray = oAllActiveSignalPerferenceArray[i].split(",");
  	    	
  	    	if(oSingalActiveSignalPerferenceArray.length == 3)
  	    	{
  	    		if( oSingalActiveSignalPerferenceArray[0] == market_type_id && 
  	    		    oSingalActiveSignalPerferenceArray[2] == strategy_id )
  	    		{
  	    			return true;
  	    		}
  	    	}
  	    }
  	    
  	    return false;
	}
		
	function checkMarketIsActiveOrNot(market_topic_name)
	{
  	    if(AllActiveMarketsList == null || AllActiveMarketsList.length < 1)
  	    {
  	    	return false;
  	    }
  	    
  	    var oAllActiveMarketsListArray = AllActiveMarketsList.split(";");
  	    
  	    for(var i=0; i<oAllActiveMarketsListArray.length; i++)
  	    {
  	    	if(oAllActiveMarketsListArray[i] == market_topic_name)
  	    	{
  	    		return true;
  	    	}
  	    }
  	    
  	    return false;
	}
	
	function onItemUpdate(itemPos,updateInfo,itemName) 
	{	
		if(updateInfo == null)
		{
			return;
		}
		
		var market_name =updateInfo.getNewValue(new FieldPositionDescriptor(1));
		var quote_date =updateInfo.getNewValue(new FieldPositionDescriptor(2));
		var price_value =updateInfo.getNewValue(new FieldPositionDescriptor(3));
		var volume_value =updateInfo.getNewValue(new FieldPositionDescriptor(4));
		var other_value =updateInfo.getNewValue(new FieldPositionDescriptor(5));
			
		//update market price (only need price so far)
		if(isIE())
		{
			if(document.getElementById(market_name) != null)
			{
				document.getElementById(market_name).innerText= price_value;
			}	
			
			if(document.getElementById(market_name+2) != null)
			{
				document.getElementById(market_name+2).innerText= price_value;
			}
		}
		else
		{
			if(document.getElementById(market_name) != null)
			{
				document.getElementById(market_name).textContent= price_value;
			}
			
			if(document.getElementById(market_name+2) != null)
			{
				document.getElementById(market_name+2).textContent= price_value;
			}
		}
	}
	
	function subscribeJSMarkets()
  	{
  	    //subscribe all markets
		subscribeJSMarketQuote('BEURUSD');
		subscribeJSMarketQuote('BUSDJPY');
		subscribeJSMarketQuote('BEURJPY');
		subscribeJSMarketQuote('BAUDUSD');
		subscribeJSMarketQuote('BUSDCHF');
		
		subscribeJSMarketQuote('BEURCHF');
		subscribeJSMarketQuote('BGBPUSD');
		subscribeJSMarketQuote('BEURGBP');
		subscribeJSMarketQuote('BUSDCAD');
		subscribeJSMarketQuote('BGBPEUR');
  	}
	
//////////////////////End For JS Quote/////////////////////////////////////////////

  	function pageOnload()
  	{
  	    subscribeJSMarkets();  	 
  	    subscribeJSSignal('jssignal');
  	    
  	    if(loadMarketInfo != "")
  	    {   
  	    	var loadMarketInfos = loadMarketInfo.split(",");
  	    	
  	    	if(loadMarketInfos.length == 11)
  	    	{
  	    		addFlashChart(document.getElementById(loadMarketInfos[0]),loadMarketInfos[1],loadMarketInfos[2],loadMarketInfos[8],loadMarketInfos[9],loadMarketInfos[3],loadMarketInfos[4],loadMarketInfos[5],loadMarketInfos[6],1,loadMarketInfos[7],loadMarketInfos[10]);
  	    	}
  	    }
  	}
  	
  	//call from flash
  	function changeChartHeight(flashObjID, newHeight)
  	{
  		//alert(flashObjID + " " + newHeight);
  		var flashPlayer = document.getElementById(flashObjID);
  		
  		if(flashPlayer != null)
  		{
  			flashPlayer.setAttribute('height',newHeight);
  		}
  	}
  	
  	function reloadFlashChart(source,htmltargetparentid,htmltargetid,historySignalsBlockID,historySignalsID,market_name,market_display_name,market_type_id,periodID,strategy_id)
  	{
  		var flashContainer = document.getElementById(htmltargetparentid);
  		var flashObjID = "flash"+htmltargetid;
  		var flashPlayer = document.getElementById(flashObjID);
  		
  		var parameters = market_name+";"+market_display_name+";"+market_type_id+";"+periodID+";"+strategy_id+";"+flashObjID;
  		
  		//update current market id and strategy id
		updateStrategyIDWhenChangeStrategy(market_type_id, strategy_id);
		   			
  		if(source && source.getAttribute("display") ==  "block")
  		{
  			//this means that chart is opened now, and can be reloaded
  			flashPlayer.reload(parameters);
  			
  			//change current signal from all recent time frame signals
  			var flashContainer = document.getElementById(htmltargetparentid);
  			
  			//step 1, remove history signals
			if(flashContainer && document.getElementById(historySignalsBlockID))
			{
				flashContainer.removeChild(document.getElementById(historySignalsBlockID));
				
				//insert history signals
				var historySignalTextID = historySignalsBlockID+"text";
				var historySignalImgID = historySignalsBlockID+"image";
				var historySignalImgIDParent = historySignalsBlockID+"imageparent";
				
				var latestTimeframeSignals="";
				
				//historySignalsID id format is Market ID : Time Frame ID : Strategy ID : Signal
				if(historySignalsID && historySignalsID.split(":").length == 4)		
				{
					marketID = historySignalsID.split(":")[0];
					strategyTimeFrameID = historySignalsID.split(":")[1];
					
					latestTimeframeSignals = loadLatestTimeframeSignals(marketID,strategy_id);
				}
				
				var historySignalHTML = ""+
							"<div class='CRbody_580' id='"+historySignalsBlockID+"' >"+
								"<div class='toptitle'>"+
									"<span><em>Multi Time-Frame Signals</em> | The last signals in all time-frames</span>"+
									"<span class='hide' id='"+historySignalTextID+"' >Hide</span><span id='"+historySignalImgIDParent+"' class='hide_chart_btn'><img id='"+historySignalImgID+"' style='cursor:pointer' onclick=\"handleHistorySignals(id,'"+market_type_id+"','"+historySignalTextID+"','"+historySignalsBlockID+"','"+historySignalsID+"','"+periodID+"','"+strategy_id+"')\" src='../images/nav1_trans.gif' title='Hide' alt='Hide' style='cursor:pointer' width='16' height='16' /></span>"+
								"</div>"+
								generateHisotrySignalsHtml(market_type_id,historySignalsID,periodID,strategy_id,latestTimeframeSignals)+
								"<div class='clear'></div>"+
							"</div>";
							
				insertHtml("BeforeEnd",flashContainer,historySignalHTML);
			}
  		}
  	}

	function addFlashChart(source,htmltargetparentid,htmltargetid,historySignalsBlockID,historySignalsID,market_name,market_display_name,market_type_id,periodID,chartType,chartImgTextName,strategy_id)
	{
		 var flashContainer = document.getElementById(htmltargetparentid);
		 var flashChart = document.getElementById(htmltargetid);
		 var flashObjID = "flash"+htmltargetid;
		 		 
		 var flashvars = {};  

		 flashvars.username = currentUsername;
		 flashvars.userpassword = currentUserpassword;
		 flashvars.period_minute = periodID;
		 flashvars.market_name = market_name;
		 flashvars.market_display_name = market_display_name;
		 flashvars.market_type_id = market_type_id;
		 flashvars.strategy_id = strategy_id;
		 flashvars.object_id = flashObjID;
		 
		 var params = {};  
		 params.username = currentUsername;
		 params.userpassword = currentUserpassword;
		 params.period_minute = periodID;
		 params.market_name = market_name;
		 params.market_display_name = market_display_name;
		 params.market_type_id = market_type_id;
		 params.strategy_id = strategy_id;
		 params.object_id = flashObjID;
		 
		 var attributes = {  
		 	 id:flashObjID,
		     classid: "clsid:D27CDB6E-AE6D-11cf-96B8-444553540000",  
		     quality: "high",
		     bgcolor: "#ffffff",
		     allowScriptAccess: "sameDomain",
		     align: "middle",
		     play: "true",
		     loop: "false"
		 };  
		 
		 //update current market id and strategy id
		 updateStrategyIDWhenChangeStrategy(market_type_id, strategy_id);
		
		var sourceID = source.getAttribute("id");
		var sourceParent = document.getElementById(sourceID+"parent");
		
		if(flashContainer.getAttribute("display") == "block")
		{
			//hide chart
			
			//source.setAttribute("src","../images/hidechart2.gif");		
			source.setAttribute("src","../images/nav1_trans.gif");
			source.setAttribute("alt","Show Chart");
			source.setAttribute("title","Show Chart");
			
			if(isIE())
			{
				document.getElementById(chartImgTextName).innerText="Show Chart";
				sourceParent.className = "show_chart_btn";
			}
			else
			{
				document.getElementById(chartImgTextName).textContent="Show Chart";
				sourceParent.setAttribute("class","show_chart_btn");
			}
			
			//remove history signals
			if(document.getElementById(historySignalsBlockID))
			{
				flashContainer.removeChild(document.getElementById(historySignalsBlockID));
			}

			//remove main chart
			swfobject.removeSWF(flashObjID);
						
			var newLi = "<li id="+htmltargetid+"></li>";
						
			insertHtml("BeforeEnd",flashContainer,newLi);	
			flashContainer.setAttribute("display","none");	
		}
		else
		{	
			//show chart
			
			//source.setAttribute("src","../images/hidechart1.gif");
			source.setAttribute("src","../images/nav1_trans.gif");
			source.setAttribute("alt","Hide Chart");
			source.setAttribute("title","Hide Chart");
			
			
			if(isIE())
			{
				document.getElementById(chartImgTextName).innerText="Hide Chart";
				sourceParent.className = "hide_chart_btn";
			}
			else
			{
				document.getElementById(chartImgTextName).textContent="Hide Chart";
				sourceParent.setAttribute("class","hide_chart_btn");
			}
			
			flashContainer.setAttribute("display","block");		
			
			if(chartType == 1)
			{	
				swfobject.embedSWF("../flash/OnlineChart.swf", htmltargetid, "640", "418", "9.0.0","#FFFFFF", flashvars, params, attributes);	
			}	
			else if(chartType == 2)
			{
				swfobject.embedSWF("../flash/OnlineChart2.swf", htmltargetid, "640", "428", "9.0.0","#FFFFFF", flashvars, params, attributes);
			}
						
			if(historySignalsBlockID != -1)
			{
				//insert history signals
				var historySignalTextID = historySignalsBlockID+"text";
				var historySignalImgID = historySignalsBlockID+"image";
				var historySignalImgIDParent = historySignalsBlockID+"imageparent";
				
				var latestTimeframeSignals="";
				
				//historySignalsID id format is Market ID : Time Frame ID : Strategy ID : Signal
				if(historySignalsID && historySignalsID.split(":").length == 4)		
				{
					marketID = historySignalsID.split(":")[0];
					strategyTimeFrameID = historySignalsID.split(":")[1];
					
					latestTimeframeSignals = loadLatestTimeframeSignals(marketID,strategy_id);
				}
			
				var historySignalHTML = ""+
							"<div class='CRbody_580' id='"+historySignalsBlockID+"' >"+
								"<div class='toptitle'>"+
									"<span><em>Multi Time-Frame Signals</em> | The last signals in all time-frames</span>"+
									"<span class='hide' id='"+historySignalTextID+"' >Hide</span><span id='"+historySignalImgIDParent+"' class='hide_chart_btn'><img id='"+historySignalImgID+"' style='cursor:pointer' onclick=\"handleHistorySignals(id,'"+market_type_id+"','"+historySignalTextID+"','"+historySignalsBlockID+"','"+historySignalsID+"','"+periodID+"','"+strategy_id+"')\" src='../images/nav1_trans.gif' title='Hide' alt='Hide' width='16' height='16' /></span>"+
								"</div>"+
								generateHisotrySignalsHtml(market_type_id,historySignalsID,periodID,strategy_id,latestTimeframeSignals)+
								"<div class='clear'></div>"+
							"</div>";
																	
				insertHtml("BeforeEnd",flashContainer,historySignalHTML);	
			}
		}
	}
		
	function getSignalTimeByTimeframe(latestTimeframeSignals, timeFrameID)
	{
		//alert(latestTimeframeSignals + "target time frame is:" + timeFrameID);
		if(latestTimeframeSignals)
		{
			var allSignalInfoArray = latestTimeframeSignals.split(";");
			
			for(var i=0; i<allSignalInfoArray.length; i++)
			{
				var signalInfoArray = allSignalInfoArray[i].split(",");
				if(signalInfoArray.length > 7 && signalInfoArray[5] == timeFrameID)
				{					
					return signalInfoArray[7];
				}
			}
		}
		
		return "";
	}
		
	function getSignalPastMinByTimeframe(latestTimeframeSignals, timeFrameID, signalPeriodMinutes)
	{
		if(latestTimeframeSignals)
		{
			var allSignalInfoArray = latestTimeframeSignals.split(";");
			
			for(var i=0; i<allSignalInfoArray.length; i++)
			{
				var signalInfoArray = allSignalInfoArray[i].split(",");
				//alert("time frame id:"+timeFrameID+", date time:"+ signalDate.getMinutes()+":"+signalDate.getSeconds());
				
				if(signalInfoArray.length > 9 && signalInfoArray[5] == timeFrameID)
				{
					var pastMins = 0;
					if(signalInfoArray[9] < signalPeriodMinutes*60)
					{
						pastMins = (signalPeriodMinutes*60 - signalInfoArray[9])/60.0;
					}
										
					return pastMins;
				}
			}
		}
		
		return signalPeriodMinutes;
	}
	
	function getSignalType(latestTimeframeSignals, timeFrameID)
	{
		if(latestTimeframeSignals)
		{
			var allSignalInfoArray = latestTimeframeSignals.split(";");
			
			for(var i=0; i<allSignalInfoArray.length; i++)
			{
				var signalInfoArray = allSignalInfoArray[i].split(",");
				
				if(signalInfoArray.length > 5 && signalInfoArray[5] == timeFrameID)
				{
					return signalInfoArray[1];
				}
			}
		}
		
		return 0;
	}
	
	function generateFlashHtml(id, width, heigth, total_minutes, pasted_minutes,flash_name)
	{
		var htmlResult ="";
		
		htmlResult += "<object type='application/x-shockwave-flash'"; 
		htmlResult += "id='"+id+"'"; 
		htmlResult += "width='"+width+"'"; 
		htmlResult += "height='"+heigth+"'";
		htmlResult += "data='"+flash_name+"'>";
		htmlResult += "<param name='movie' value='"+flash_name+"' />";
		htmlResult += "<param name='quality' value='high' />";
		htmlResult += "<param name='play' value='true' />";
		htmlResult += "<param name='bgcolor' value='#FFFFFF' />";
		htmlResult += "<param name='allowScriptAccess' value='sameDomain' />";
		htmlResult += "<param name='flashvars' value='total_time="+total_minutes+"&past_time="+pasted_minutes+"' />";
	    htmlResult += "</object>";
	    		
		return htmlResult;			
	}
		
	function getSignalFlashFileNameBySignalType(signalType)
	{
		var flashFileName = "../flash/Green_Clock.swf";
		if(signalType == 4)
		{
			flashFileName = "../flash/Green_Clock.swf"
		}
		else if(signalType == 5)
		{
			flashFileName = "../flash/Red_Clock.swf"
		}
		else if(signalType == 6)
		{
			flashFileName = "../flash/Close_Clock.swf"
		}
		
		return flashFileName;
	}
	
	function refreshMultiFrameSignals(marketID, timeFrame, signalType,generate_date)
	{
		var subSignalContainID = marketID+":"+timeFrame+":signal:contain";
		var subSignalID = marketID+":"+timeFrame+":signal";
		var signalTimeID = marketID+":"+timeFrame+":signaltime";
		
		var signalTimeIDObj = document.getElementById(signalTimeID);
		//refresh time
		//alert(generate_date+":"+signalTimeIDObj);
		if(signalTimeIDObj)
		{
			var signalDate = new Date();
			signalDate.setTime(generate_date);
			//alert("new signal date time:"+ signalDate.getMinutes()+":"+signalDate.getSeconds());
			var signalMinutes = "00";
			var signalHours = "00";
			
			if(signalDate.getHours())
			{
				signalHours =""+signalDate.getHours();

				if(signalHours.length == 1)
				{
					signalHours = "0"+signalDate.getHours();
				}
			}
			
			if(signalDate.getMinutes())
			{
				signalMinutes = ""+signalDate.getMinutes();
				
				if(signalMinutes.length == 1)
				{
					signalMinutes = "0"+signalDate.getMinutes();
				}
			}
					
			if(isIE())
			{
				signalTimeIDObj.innerText=signalHours +":"+ signalMinutes;
			}
			else
			{
				signalTimeIDObj.textContent=signalHours +":"+ signalMinutes;
			}
		}
		
		//if Market related Chart is opened now, then refresh the multi-frame signals
		var subSignalContainObj = document.getElementById(subSignalContainID);
		var subSignalObj = document.getElementById(subSignalID);
		
		if(subSignalContainObj && subSignalObj)
		{
			//remove current flash object
			subSignalContainObj.removeChild(subSignalObj);
			
			//add new one
			var flashFileName = getSignalFlashFileNameBySignalType(signalType);
			
			var timePeriodID = marketID+":period";
			var currentTimeFrame = document.getElementById(timePeriodID).value;	
			
			var newSignalHtml = "<span id='"+subSignalID+"'>";
			
			if(currentTimeFrame == timeFrame)
			{
				newSignalHtml += generateFlashHtml(marketID*2, 87, 87, getSignalPeriodMinutes(timeFrame), 0, flashFileName);
			}
			else
			{
				newSignalHtml += generateFlashHtml(marketID*2, 70, 70, getSignalPeriodMinutes(timeFrame), 0, flashFileName);
			}
			
			newSignalHtml += "</span>";
			
			insertHtml("BeforeEnd",subSignalContainObj,newSignalHtml);
		}
	}
	
	function generateHisotrySignalsHtml(marketID,historySignalsID,currentTimeFrameID,strategy_id,latestTimeframeSignals)
	{
		var strategyTimeFrameID;	
		var latestTimeframeSignals;
		var latestTimeframeSignals;
				 
		var htmlResult = "<div id='"+historySignalsID+"' display='block' >";
		
		for(var timeFrame=1; timeFrame<7; timeFrame++)
		{
			var signalTime = "";
			var pastMin = 0;
			var flashFileName="";
			
			var signalType=getSignalType(latestTimeframeSignals,timeFrame);			
			var signalPeriodMinutes = getSignalPeriodMinutes(timeFrame);			
			var subSignalContainID = marketID+":"+timeFrame+":signal:contain";			
			var subSignalID = marketID+":"+timeFrame+":signal";			
			var signalTimeID = marketID+":"+timeFrame+":signaltime";			
			var signalPeriodName = getPeriodNameByID(timeFrame);
			var flashFileName = getSignalFlashFileNameBySignalType(signalType);
			
			if(currentTimeFrameID == timeFrame)
			{			
				signalTime = getSignalTimeByTimeframe(latestTimeframeSignals, timeFrame);
				pastMin = getSignalPastMinByTimeframe(latestTimeframeSignals, timeFrame, signalPeriodMinutes);
								
				htmlResult += "<ul>";
				htmlResult += "<li class='title_on'><span>"+signalPeriodName+"</span><span id='"+signalTimeID+"'>"+signalTime+"</span></li>";
				htmlResult += "<li class='Cbody' id='"+subSignalContainID+"'><span id='"+subSignalID+"'>";
				htmlResult += generateFlashHtml(marketID*2, 87, 87, signalPeriodMinutes, pastMin, flashFileName);
				htmlResult += "</span></li>";
				htmlResult += "</ul>";
			}
			else
			{
				signalTime = getSignalTimeByTimeframe(latestTimeframeSignals, timeFrame);
				pastMin = getSignalPastMinByTimeframe(latestTimeframeSignals, timeFrame, signalPeriodMinutes);
				
				htmlResult += "<ul>";
				htmlResult += "<li class='title'><span>"+signalPeriodName+"</span><span id='"+signalTimeID+"'>"+signalTime+"</span></li>";
				htmlResult += "<li class='Cbody2' id='"+subSignalContainID+"'><span id='"+subSignalID+"'>";
				htmlResult += generateFlashHtml(marketID*2, 70, 70, signalPeriodMinutes, pastMin, flashFileName);
				htmlResult += "</span></li>";
				htmlResult += "</ul>";
			}
		}
		
		htmlResult+="</div>";
		
		htmlResult+="<div class='clear'></div>";
				
		return htmlResult;
	}
	
	
	function handleHistorySignals(historySignalImgID, marketTypeID, historySignalTextID, historySignalsBlockID, historySignalsID, currentTimeFrameID,strategy_id)
	{
		_market_type_id = marketTypeID;
		_historySignalsObj = document.getElementById(historySignalsID);
		_historySignalsBlockObj = document.getElementById(historySignalsBlockID);
		_sourceObj = document.getElementById(historySignalImgID);
		_textObj = document.getElementById(historySignalTextID);
		_sourceParent = document.getElementById(historySignalImgID+"parent");
		_historySignalsID = historySignalsID;
		_currentTimeFrameID = currentTimeFrameID;
		_strategy_id = strategy_id;
		
		if(_historySignalsBlockObj && _sourceObj && _textObj)
		{			
			if(_historySignalsObj)
			{
				//hide signals	
				
				//sourceObj.setAttribute("src","../images/hidechart2.gif");	
				_sourceObj.setAttribute("src","../images/nav1_trans.gif");		
				_sourceObj.setAttribute("alt","Show");
				_sourceObj.setAttribute("title","Show");
				
				if(isIE())
				{
					_textObj.innerText="Show";
					_sourceParent.className = "show_chart_btn";
				}
				else
				{
					_textObj.textContent="Show";
					_sourceParent.setAttribute("class","show_chart_btn");
				}
				
				_historySignalsBlockObj.removeChild(_historySignalsObj);				
			}
			else
			{
				//show signals		
				_sourceObj.setAttribute("width",0);					
				
				if(isIE())
				{
					_textObj.innerText="Loading...";
					_sourceParent.className = "";
				}
				else
				{
					_textObj.textContent="Loading...";
					_sourceParent.setAttribute("class","");
				}
				
				//historySignalsID id format is Market ID : Time Frame ID : Strategy ID : Signal
				if(historySignalsID && historySignalsID.split(":").length == 4)		
				{
					var marketID = historySignalsID.split(":")[0];
					var strategyTimeFrameID = historySignalsID.split(":")[1];
					
					//get latest signal of all time frame. 
					getLatestTimeframeSignals(marketID,strategy_id);
				}
			}
		}
	}
	
	var _objHTTP;
	var _historySignalsObj;
	var _historySignalsBlockObj;
	var _sourceObj;
	var _textObj;
	var _sourceParent;
	var _historySignalsID;
	var _market_type_id;
	var _currentTimeFrameID;
	var _strategy_id;
	
	//asyncronized
	function getLatestTimeframeSignals(marketID, strategyID)
	{
		var actionURL = "../charts/getLatestTimeframeSignals.do?";	
		var parameters = "marketid="+marketID+"&strategyid="+strategyID;
	      	
		_objHTTP = getXMLHttp();
		var result="";
		
		if (_objHTTP)
		{
			_objHTTP.open("POST",encodeURI(actionURL),true,"","");
	    	_objHTTP.setRequestHeader("CONTENT-TYPE","application/x-www-form-urlencoded;charset=UTF-8");
	    	_objHTTP.onreadystatechange = getLatestTimeframeSignalsCallback;	
	    	_objHTTP.send(parameters);    	
		}
	}
	
	//syncronized
	function loadLatestTimeframeSignals(marketID, strategyID)
	{
		var actionURL = "../charts/getLatestTimeframeSignals.do?";	
		var parameters = "marketid="+marketID+"&strategyid="+strategyID;
	      	
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
	
	function getLatestTimeframeSignalsCallback()
	{
		if( _objHTTP.readyState != 4 )
		{
			return;
		}
		
		if( _objHTTP.status != 200 )
		{
			return;
		}
		
		var htmlContent = generateHisotrySignalsHtml(_market_type_id, _historySignalsID,_currentTimeFrameID,_strategy_id,_objHTTP.responseText);
		
		_sourceObj.setAttribute("src","../images/nav1_trans.gif");		
		_sourceObj.setAttribute("width",16);	
		_sourceObj.setAttribute("alt","Hide");
		_sourceObj.setAttribute("title","Hide");
		
		if(isIE())
		{
			_textObj.innerText="Hide";
			_sourceParent.className = "hide_chart_btn";
		}
		else
		{
			_textObj.textContent="Hide";
			_sourceParent.setAttribute("class","hide_chart_btn");
		}
		
		insertHtml("BeforeEnd",_historySignalsBlockObj,htmlContent);
	}