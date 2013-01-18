	
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
	
	function subscribeJSMarketQuote(marketName)
	{
		if (marketName == "") 
		{
			return;
		}
		
		var newTable = new NonVisualTable(marketName,quoteSchema, "DISTINCT");
		
		newTable.setSnapshotRequired(true);
		newTable.setRequestedMaxFrequency(0.5);
		newTable.onItemUpdate = onQuoteUpdate;
		
		lsPage.addTable(newTable, marketName);
	}
	
	function onQuoteUpdate(itemPos,updateInfo,itemName) 
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
		
		//don't need update price, and only keep it original Signal Price
		//updateMarketQuote(market_name,price_value);
	}
	
	function updateMarketQuote(market_name,price_value)
	{
		if(updatedFieldOfQuote == null || updatedFieldOfQuote.length < 1)
		{
			return;
		}
				
		//parse current Signals list to update quote
		//format is:  marketTopicName,targetID;marketTopicName,targetID;marketTopicName,targetID; 
		var currentSignalsList = updatedFieldOfQuote.split(";");
		
		for(var i=0; i<currentSignalsList.length; i++)
		{
			var singleSignal = currentSignalsList[i];
			var singleSignalDetails = singleSignal.split(",");
			
			if(singleSignalDetails.length == 2 && singleSignalDetails[0] == market_name)
			{
			    if(document.getElementById(singleSignalDetails[1]) != null)
			    {
    				if(isIE())
    				{
    					document.getElementById(singleSignalDetails[1]).innerText= price_value;	
    				}
    				else
    				{
    					document.getElementById(singleSignalDetails[1]).textContent= price_value;
    				}
				}
			}
		}
	}
	
	function addNewMarketSubscrible(marketName, targetID)
	{
		var newMarketSubscrible = marketName+","+targetID;
		updatedFieldOfQuote = updatedFieldOfQuote + ";"+newMarketSubscrible;
	}
	
	function removeMarketSubscrible(targetID)
	{
		if(updatedFieldOfQuote == null || updatedFieldOfQuote.length < 1)
		{
			return;
		}
		
		//alert(updatedFieldOfQuote);
		var currentSignalsList = updatedFieldOfQuote.split(";");
		
		var newUpdatedFieldOfQuote = "";
		
		for(var i=0; i<currentSignalsList.length; i++)
		{
			var singleSignal = currentSignalsList[i];
			var singleSignalDetails = singleSignal.split(",");
			
			if(singleSignalDetails.length == 2 && singleSignalDetails[1] != targetID)
			{
				var newMarketSubscrible = singleSignalDetails[0]+","+singleSignalDetails[1];
				newUpdatedFieldOfQuote = newUpdatedFieldOfQuote + newMarketSubscrible;
			}
		}
		
		updatedFieldOfQuote = newUpdatedFieldOfQuote;		
	}
	
	function subscribeJSMarkets()
  	{
  		//alert("AllActiveMarketsList is "+AllActiveMarketsList);
  		
  	    if(AllActiveMarketsList == null || AllActiveMarketsList.length < 1)
  	    {
  	    	return;
  	    }
  	    
  	    var oAllActiveMarketsListArray = AllActiveMarketsList.split(";");
  	    
  	    for(var i=0; i<oAllActiveMarketsListArray.length; i++)
  	    {
  	    	subscribeJSMarketQuote(oAllActiveMarketsListArray[i]);
  	    }
  	}
	
	function subscribeJSSignal(jssignal)
	{
		if (jssignal == "") 
		{
			return;
		}
		
		var newTable = new NonVisualTable(jssignal,schema, "DISTINCT");
		
		newTable.setSnapshotRequired(true);
		newTable.setRequestedMaxFrequency(0.5);
		newTable.onItemUpdate = onSignalItemUpdate;
		
		lsPage.addTable(newTable, jssignal);
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
		
		if(checkMarketTimeFrameAndStrategyIDIsActiveOrNot(market_type_id,signal_period,strategy_id))
		{
			//add signal
			addSignal(signal_id,market_name,signal_type,signal_value,expire_date,expire_date_string,profit,system_name,signal_period,market_topic_name,market_type_id,strategy_id,signal_period_minutes);
		
			//add subscribed price id
			var subscribedPriceID = signal_id + ":price";
			addNewMarketSubscrible(market_topic_name,subscribedPriceID);
		}
			
		if(checkMultiTimeFrameStrategyIDIsActiveOrNot(market_type_id,strategy_id))
		{
			//refresh  multi-frame signals involved
			refreshMultiFrameSignals(market_type_id, signal_period, signal_type,generate_date);
		}
	}
	
	function checkMarketTimeFrameAndStrategyIDIsActiveOrNot(market_type_id, market_period_id, strategy_id)
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
  	    		if( oSingalActiveSignalPerferenceArray[0] == market_type_id && oSingalActiveSignalPerferenceArray[1] == market_period_id && oSingalActiveSignalPerferenceArray[2] == strategy_id )
  	    		{
  	    			//alert("new signal coming, market_type_id="+market_type_id+",market_period_id="+market_period_id+",strategy_id="+strategy_id);
  	    			//alert("AllActiveSignalPerference="+AllActiveSignalPerference);
  	    			//alert("This signal match the preference");
  	    			return true;
  	    		}
  	    	}
  	    }
  	    
  	    //alert("This signal doesn't match the preference");
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
	
	function getStrategyNameByID(strategy_id)
	{
		if(AllStrategyString && AllStrategyString.split(";").length > 0)
		{
			var oAllStrategyArray = AllStrategyString.split(";");
  	    
	  	    for(var i=0; i<oAllStrategyArray.length; i++)
	  	    {
	  	    	var strategyIDAndName = oAllStrategyArray[i];
	  	    	
	  	    	if(strategyIDAndName && strategyIDAndName.split(",").length==2)
	  	    	{
	  	    		if(strategyIDAndName.split(",")[0] == strategy_id)
	  	    		{
	  	    			return strategyIDAndName.split(",")[1];
	  	    		}
	  	    	}
	  	    }
		}
		else
		{
			return "";
		}
	}
		
	function addSignal(signalID,market_name,signal_type,signal_value,expire_date,expire_date_string,signal_profit,system_name,signal_period,market_topic_name,market_type_id,strategy_id,signal_period_minutes)
	{
		removePromotionInfo();
		
		//alert('received one signal');
		var subscribedPriceID = signalID + ":price";
		
		var signalTimeOutID = signalID+":timeout";
		
		var strategyName = getStrategyNameByID(strategy_id);
						
		var proftString="";
		if(signal_profit>0)
		{
			proftString = "+"+signal_profit;
		}
		else
		{
			proftString = signal_profit;
		}
		
		var htmlOfSignal = "";
		var pastMins = 0.0;
		
		var signalPeriodName = getPeriodNameByID(signal_period);
		
		if(expire_date < signal_period_minutes*60)
		{
			pastMins = (signal_period_minutes*60-expire_date)/60.0;
		}
				
		htmlOfSignal += "<div class='sell' id='"+signalID+"'>";
		htmlOfSignal += "	<div class='title'>";
		if(signal_type==5)
		{
		htmlOfSignal += "		<span class='sell'>SHORT</span>";
		}else if(signal_type==4){
		htmlOfSignal += "		<span class='buy'>LONG</span>";
		}else if(signal_type==6){
		htmlOfSignal += "		<span class='close'>CLOSE</span>";
		}
		
		htmlOfSignal += "		<span class='type'>"+market_name+"</span>";
		htmlOfSignal += "		<span class='period'>"+signalPeriodName+"</span>";
		htmlOfSignal += "	</div>";
		htmlOfSignal += "	<div class='pic'>";
		if(signal_type==5)
		{
		htmlOfSignal += "			<div class='flashSignal'>";
		htmlOfSignal += "				<object type='application/x-shockwave-flash' ";
		htmlOfSignal += "					id="+signalID*2+" ";
		htmlOfSignal += "					width='100%' ";
		htmlOfSignal += "					height='100%'";
		htmlOfSignal += "					data='../flash/Red_Clock.swf'>";
		htmlOfSignal += "					<param name='movie' value='../flash/Red_Clock.swf' />";
		htmlOfSignal += "					<param name='quality' value='high' />";
		htmlOfSignal += "					<param name='play' value='true' />";
		htmlOfSignal += "					<param name='bgcolor' value='#FFFFFF' />";
		htmlOfSignal += "					<param name='allowScriptAccess' value='sameDomain' />";
		htmlOfSignal += "					<param name='flashvars' value='total_time="+signal_period_minutes+"&past_time="+pastMins+"' />";
		htmlOfSignal += "			    </object>";					
		htmlOfSignal += "			</div>";
		}else if(signal_type==4){
		htmlOfSignal += "			<div class='flashSignal'>";
		htmlOfSignal += "				<object type='application/x-shockwave-flash' ";
		htmlOfSignal += "					id="+signalID+" ";
		htmlOfSignal += "					width='100%' ";
		htmlOfSignal += "					height='100%'";
		htmlOfSignal += "					data='../flash/Green_Clock.swf'>";
		htmlOfSignal += "					<param name='movie' value='../flash/Green_Clock.swf' />";
		htmlOfSignal += "					<param name='quality' value='high' />";
		htmlOfSignal += "					<param name='play' value='true' />";
		htmlOfSignal += "					<param name='bgcolor' value='#FFFFFF' />";
		htmlOfSignal += "					<param name='allowScriptAccess' value='sameDomain' />";
		htmlOfSignal += "					<param name='flashvars' value='total_time="+signal_period_minutes+"&past_time="+pastMins+"' />";
		htmlOfSignal += "			    </object>";					
		htmlOfSignal += "			</div>";
		}else if(signal_type==6){
		htmlOfSignal += "			<div class='flashSignal'>";
		htmlOfSignal += "				<object type='application/x-shockwave-flash' ";
		htmlOfSignal += "					id="+signalID*3+" ";
		htmlOfSignal += "					width='100%' ";
		htmlOfSignal += "					height='100%'";
		htmlOfSignal += "					data='../flash/Close_Clock.swf'>";
		htmlOfSignal += "					<param name='movie' value='../flash/Close_Clock.swf' />";
		htmlOfSignal += "					<param name='quality' value='high' />";
		htmlOfSignal += "					<param name='play' value='true' />";
		htmlOfSignal += "					<param name='bgcolor' value='#FFFFFF' />";
		htmlOfSignal += "					<param name='allowScriptAccess' value='sameDomain' />";
		htmlOfSignal += "					<param name='flashvars' value='total_time="+signal_period_minutes+"&past_time="+pastMins+"' />";
		htmlOfSignal += "			    </object>";					
		htmlOfSignal += "			</div>";
		}
		htmlOfSignal += "	</div>";		
		htmlOfSignal += "	<div class='strategy-name'>";										
		htmlOfSignal += "		<span class='price'>"+strategyName+"</span>";
		htmlOfSignal += "	</div>";
		htmlOfSignal += "	<div class='footli' id='footli'>";	
		htmlOfSignal += "		<span class='price-name'>Price:</span>";									
		htmlOfSignal += "		<span id='"+subscribedPriceID+"' class='price'>"+signal_value+"</span>";
		htmlOfSignal += "		<span class='time-name'>Time:</span>";
		htmlOfSignal += "		<span id='"+signalTimeOutID+"' class='time'>"+expire_date_string+"</span>";
		htmlOfSignal += "	</div>";
	    htmlOfSignal += "</div>";
										
        //alert(htmlOfSignal);
        
        var signalList = document.getElementById('signal_list');
        insertHtml("BeforeEnd",signalList,htmlOfSignal);
        
        //add signal id into signalListString
        if(signalListString.length > 0)
        {
        	signalListString += (";"+signalID);
        }
        else
        {
        	signalListString += signalID;
        }
	}
	
	function showPromotionInfo()
	{
		//if there is not promotion info
		if(document.getElementById("promotionInfoParent") != null && document.getElementById("promotionInfo") == null )
		{
			var insertPromotionInfo = "";			
			insertPromotionInfo += "<div id='promotionInfo'>";
			insertPromotionInfo += "	<div class='promotional_sginal_tip'>";
			insertPromotionInfo += "		<span class='big_font'>There are currently no active signals</span>";
			insertPromotionInfo += "	</div>";
			insertPromotionInfo += "	<div class='promotional_tip'>";
			insertPromotionInfo += "		<span class='big_font'>Get More Signals </span>";
			insertPromotionInfo += "		<span class='small_font'>- Increase your signals by adding more markets </span>";
			insertPromotionInfo += "		<span class='link_font'><a href='../myaccount/getStrategy.do'>Add Markets Now</a></span>";
			insertPromotionInfo += "	</div>";
			insertPromotionInfo += "</div>";
			
			insertHtml("BeforeEnd",document.getElementById("promotionInfoParent"),insertPromotionInfo);
		}
	}
	
	function removePromotionInfo()
	{
		if(document.getElementById("promotionInfoParent") && document.getElementById("promotionInfo"))
		{
			document.getElementById("promotionInfoParent").removeChild(document.getElementById("promotionInfo"));
		}
	}
	
//////////////////////End JS Signal/////////////////////////////////////////////
//////////////////////Begin Multi Time Frame////////////////////////////////////

	function reloadFlashChart(source,htmltargetparentid,htmltargetid,historySignalsBlockID,historySignalsID,market_name,market_display_name,market_type_id,periodID,strategy_id)
  	{
  		var flashContainer = document.getElementById(htmltargetparentid);
  		
  		//update current market id and strategy id
		updateStrategyIDWhenChangeStrategy(market_type_id, strategy_id);
		   			
  		if(source && source.getAttribute("display") ==  "block")
  		{
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
							"<div class='CRbody_580_bigger' id='"+historySignalsBlockID+"' >"+
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
		 		 
		//update current market id and strategy id
		updateStrategyIDWhenChangeStrategy(market_type_id, strategy_id);
		
		var sourceID = source.getAttribute("id");
		var sourceParent = document.getElementById(sourceID+"parent");
		
		if(flashContainer.getAttribute("display") == "block")
		{
			//hide chart
			
			source.setAttribute("src","../images/nav1_trans.gif");
			source.setAttribute("alt","Show Chart");
			source.setAttribute("title","Show Chart");
			
			if(isIE())
			{
				document.getElementById(chartImgTextName).innerText="Show";
				sourceParent.className = "show_chart_btn";
			}
			else
			{
				document.getElementById(chartImgTextName).textContent="Show";
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
			
			source.setAttribute("src","../images/nav1_trans.gif");
			source.setAttribute("alt","Hide");
			source.setAttribute("title","Hide");
			
			
			if(isIE())
			{
				document.getElementById(chartImgTextName).innerText="Hide";
				sourceParent.className = "hide_chart_btn";
			}
			else
			{
				document.getElementById(chartImgTextName).textContent="Hide";
				sourceParent.setAttribute("class","hide_chart_btn");
			}
			
			flashContainer.setAttribute("display","block");		
									
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
							"<div class='CRbody_580_bigger' id='"+historySignalsBlockID+"' >"+
								generateHisotrySignalsHtml(market_type_id,historySignalsID,periodID,strategy_id,latestTimeframeSignals)+
								"<div class='clear'></div>"+
							"</div>";
																	
				insertHtml("BeforeEnd",flashContainer,historySignalHTML);	
			}
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
			
			var currentTimeFrame = 0;	
			
			var newSignalHtml = "<span id='"+subSignalID+"'>";
			
			if(currentTimeFrame == timeFrame)
			{
				newSignalHtml += generateFlashHtml(marketID*2, 107, 87, getSignalPeriodMinutes(timeFrame), 0, flashFileName);
			}
			else
			{
				newSignalHtml += generateFlashHtml(marketID*2, 107, 70, getSignalPeriodMinutes(timeFrame), 0, flashFileName);
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
				htmlResult += generateFlashHtml(marketID*2, 107, 87, signalPeriodMinutes, pastMin, flashFileName);
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
				htmlResult += generateFlashHtml(marketID*2, 107, 70, signalPeriodMinutes, pastMin, flashFileName);
				htmlResult += "</span></li>";
				htmlResult += "</ul>";
			}
		}
		
		htmlResult+="</div>";
		
		htmlResult+="<div class='clear'></div>";
				
		return htmlResult;
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
	
	function changeStrategy(perferenceID, strategyObjID, periodID)
	{
		var strategyObj = document.getElementById(strategyObjID);
		
		if(strategyObj)
		{
			var strategyID = strategyObj.value;		
		}
	}
	
	function updateStrategyIDWhenChangeStrategy(market_type_id, new_strategy_id)
	{
		if(AllMultiTimeFrameSignalPerfence == null || AllMultiTimeFrameSignalPerfence.length < 1)
  	    {
  	    	return false;
  	    }
  	    
  	    var oNewAllActiveSignalPerference = "";
  	    
  	    var oAllActiveSignalPerferenceArray = AllMultiTimeFrameSignalPerfence.split(";");
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
  	    
  	    AllMultiTimeFrameSignalPerfence = oNewAllActiveSignalPerference;
	}
	
	function checkMultiTimeFrameStrategyIDIsActiveOrNot(market_type_id, strategy_id)
	{
		if(AllMultiTimeFrameSignalPerfence == null || AllMultiTimeFrameSignalPerfence.length < 1)
  	    {
  	    	return false;
  	    }
  	    
  	    var oAllActiveSignalPerferenceArray = AllMultiTimeFrameSignalPerfence.split(";");
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
	
//////////////////////End Multi Time Frame//////////////////////////////////////

	function addActiveSignalClock(htmltargetid,market_name,market_display_name,market_type_id,period_minute,past_time,isUP)
	{
		 var flashvars = {};  
		 flashvars.total_time = period_minute;
		 flashvars.past_time = past_time;
		 
		 var params = {};  
		 params.total_time = period_minute;
		 params.past_time = past_time;
		 
		 var attributes = {  
		     classid: "clsid:D27CDB6E-AE6D-11cf-96B8-444553540000",  
		     quality: "high",
		     bgcolor: "#FFFFFF",
		     allowScriptAccess: "sameDomain",
		     align: "middle",
		     play: "true",
		     loop: "false"
		 }; 
		 
		 if(isUP)
		 {
		 	swfobject.embedSWF("../flash/Green_Clock.swf", htmltargetid, "120", "120", "9.0.0","", flashvars, params, attributes);	
		 }
		 else
		 {
		 	swfobject.embedSWF("../flash/Red_Clock.swf", htmltargetid, "120", "120", "9.0.0","", flashvars, params, attributes);	
		 }
	}
	
	function pageOnload()
  	{
		countdownSec();
		subscribeJSSignal('jssignal');
		subscribeJSMarkets();
  	}
  	
  	function countdownSec()
  	{  			
  		//get all current signals timeout fields  			
  		var signalList = signalListString.split(";");
  		var latestAcitveSignalListString = "";

  		for(var i=0; i<signalList.length; i++)
  		{
  			//alert(signalList[i]);
  			var timeOutItemID = signalList[i]+":timeout";
  			var timeOutItem = document.getElementById(timeOutItemID);
  			//alert(timeOutItem);
  			if(document.getElementById(timeOutItemID) != null )
  			{
	  			//alert(timeOutItem);
	  			var timeOutText = "";
	  			if(isIE())
	  			{
	  				timeOutText = timeOutItem.innerText;
	  			}
	  			else
	  			{
	  				timeOutText = timeOutItem.textContent;
	  			}
	  				
  				if(timeOutText.indexOf('sec') >= 0)
  				{
  					var timeOutSecs = parseInt(timeOutText.split(' ')[0]);
  					timeOutSecs = timeOutSecs -1;
  					
  					if(timeOutSecs <= 0)
  					{
  						//remove signal
  						//var signalListElement = document.getElementById(signalList[i]).parentNode;
  						var signalListElement = document.getElementById('signal_list');
  						//alert("remove signal id: "+ signalList[i]+" by " + signalListElement.id);  						
  						
  						signalListElement.removeChild(document.getElementById(signalList[i]));
  						
  						removeTimeoutSignalFromSignalString(signalList[i]);
  						
  						removeMarketSubscrible(signalList[i]+":price");
  					}
  					else
  					{	  	
  						if(isIE())
  						{
  							timeOutItem.innerText = timeOutSecs + " sec";
  						}
  						else
  						{
  							timeOutItem.textContent = timeOutSecs + " sec";
  						}
  						
  						latestAcitveSignalListString += signalList[i];
  						latestAcitveSignalListString += ";";
  					}
  				}
  			}
  		}
  		
  		if(latestAcitveSignalListString.length > 0)
  		{
  			if(latestAcitveSignalListString.substring(latestAcitveSignalListString.length-2, 1) == ";")
  			{
  				latestAcitveSignalListString = latestAcitveSignalListString.substring(0,latestAcitveSignalListString.length-1);
  			}
  		}
  		
  		signalListString = latestAcitveSignalListString;
  		
  		if(signalListString == "")
  		{
  			showPromotionInfo();
  		}
  		
  		setTimeout('countdownSec()',1000);
  	}
  	
  	function removeTimeoutSignalFromSignalString(timeoutSignalID)
  	{
  		var signalList = signalListString.split(";");
  		var newSignalListString = "";
  		for(var i=0; i<signalList.length; i++)
  		{
  			if(signalList[i] != timeoutSignalID)
  			{
  				newSignalListString += (signalList[i]+";");
  			}
  		}
  		
  		//remove last ;
  		signalListString = newSignalListString.substring(0,newSignalListString.length-1);
  	} 