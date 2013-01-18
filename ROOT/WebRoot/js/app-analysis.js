var objHTTP;
function getStatisticsResult()
{
	var marketIDList = "marketID="+all_market_id_string;
	var strategyIDList = "&strategyID="+all_strategy_id_string;
	var periodIDList = "&periodID="+all_period_id_string;
	var fromDate = "&from="+start_date;
	var toDate = "&to="+finish_date;
	
	//alert(marketIDList+" "+strategyIDList+" "+periodIDList);
	var actionURL = "tradingStatistics.do?";
      	
	objHTTP = getXMLHttp();
	if (objHTTP) 
	{
		setLoadingForStatisticsResult();
		
		objHTTP.open("POST",encodeURI(actionURL),true,"","");
    	objHTTP.setRequestHeader("CONTENT-TYPE","application/x-www-form-urlencoded");
    	objHTTP.onreadystatechange = parseAndUpdateStatisticsResult;	
    	objHTTP.send(marketIDList+periodIDList+strategyIDList+fromDate+toDate);    	
	}
}

function setLoadingForStatisticsResult()
{
	if(isIE())
	{
		document.getElementById("Trade_Success").setAttribute("className", "loading");
		document.getElementById("Total_Profit_Loss").setAttribute("className", "loading");
		document.getElementById("Average_Trade").setAttribute("className", "loading");
		document.getElementById("Close_Signals").setAttribute("className", "loading");
		document.getElementById("Long_Signals").setAttribute("className", "loading");
		document.getElementById("Short_Signals").setAttribute("className", "loading");
	
		document.getElementById("Trade_Success").innerText= "Loading Data...";			
		document.getElementById("Total_Profit_Loss").innerText= "Loading Data...";
		document.getElementById("Average_Trade").innerText= "Loading Data...";
		
		document.getElementById("Close_Signals").innerText= "Loading Data...";
		document.getElementById("Long_Signals").innerText= "Loading Data...";
		document.getElementById("Short_Signals").innerText= "Loading Data...";
		
		document.getElementById("load_equity_curve_chart").innerText= "Loading...";
		document.getElementById("load_profit_loss_chart").innerText= "Loading...";
	}
	else
	{
		document.getElementById("Trade_Success").setAttribute("class", "loading");
		document.getElementById("Total_Profit_Loss").setAttribute("class", "loading");
		document.getElementById("Average_Trade").setAttribute("class", "loading");
		document.getElementById("Close_Signals").setAttribute("class", "loading");
		document.getElementById("Long_Signals").setAttribute("class", "loading");
		document.getElementById("Short_Signals").setAttribute("class", "loading");
		
		document.getElementById("Trade_Success").textContent= "Loading Data...";			
		document.getElementById("Total_Profit_Loss").textContent= "Loading Data...";
		document.getElementById("Average_Trade").textContent= "Loading Data...";
		
		document.getElementById("Close_Signals").textContent= "Loading Data...";
		document.getElementById("Long_Signals").textContent= "Loading Data...";
		document.getElementById("Short_Signals").textContent= "Loading Data...";		
		
		document.getElementById("load_equity_curve_chart").textContent= "Loading...";
		document.getElementById("load_profit_loss_chart").textContent= "Loading...";
	}
}

function parseAndUpdateStatisticsResult(statisticsResult)
{
	if( objHTTP.readyState != 4 )
	{
		return;
	}
	
	if( objHTTP.status != 200 )
	{
		return;
	}
	
	var statisticsResult = objHTTP.responseText;
	
	//format is ' 1) Trade Success; 2) Total Profit / Loss (Points); 3) Average Trade (Points); 
	// 4) Total Trades; 5) Winning Trades; 6) Average Winning Trade (Points); 7) Losing Trades; 8) Average Losing Trade (Points)
	// 9) Close_Signals; 10) total_broker_points; 11) final_profit_or_loss_points; 12)long signals; 13)short signals; 14)close signals
	var statisticsResultList = statisticsResult.split(";");
	
	if(statisticsResultList.length != 14)
	{
		return;
	}
	//alert(statisticsResult);
	var Trade_Success = statisticsResultList[0];
	Trade_Success = Trade_Success.replace(',','');
	
	var Total_Profit_Loss = statisticsResultList[1];
	Total_Profit_Loss = Total_Profit_Loss.replace(',','');
	
	var Average_Trade = statisticsResultList[2];
	Average_Trade = Average_Trade.replace(',','');
	
	var Total_Trades = statisticsResultList[3];
	Total_Trades = Total_Trades.replace(',','');
	
	var Winning_Trades = statisticsResultList[4];
	Winning_Trades = Winning_Trades.replace(',','');
	
	var Average_Winning_Trade = statisticsResultList[5];
	Average_Winning_Trade = Average_Winning_Trade.replace(',','');
	
	var Losing_Trades = statisticsResultList[6];
	Losing_Trades = Losing_Trades.replace(',','');
	
	var Average_Losing_Trade = statisticsResultList[7];
	Average_Losing_Trade = Average_Losing_Trade.replace(',','');
	
	var Close_Signals = statisticsResultList[8];
	Close_Signals = Close_Signals.replace(',','');
	
	var Total_Brokerage = statisticsResultList[9];
	Total_Brokerage = Total_Brokerage.replace(',','');
	
	var Final_Profit_Loss = statisticsResultList[10];
	Final_Profit_Loss = Final_Profit_Loss.replace(',','');
	
	var Long_Signals=statisticsResultList[11];
	Long_Signals = Long_Signals.replace(',','');
	
	var Short_Signals=statisticsResultList[12];
	Short_Signals = Short_Signals.replace(',','');
	
	var Close_Signals=statisticsResultList[13];
	Close_Signals = Close_Signals.replace(',','');
		
	if(isIE())
	{
		//Any + numbers should be in green. And - numbers should be in red.
		if(Trade_Success*1 >0 )
		{
			document.getElementById("Trade_Success").setAttribute("className", "green");
		}
		else
		{
			document.getElementById("Trade_Success").setAttribute("className", "red");
		}	
		
		if(Total_Profit_Loss*1 >0 )
		{
			document.getElementById("Total_Profit_Loss").setAttribute("className","green");	
			Total_Profit_Loss = "+"+Total_Profit_Loss;
		}
		else
		{
			document.getElementById("Total_Profit_Loss").setAttribute("className","red");	
		}
		
		if(Average_Trade*1 >0 )
		{
			document.getElementById("Average_Trade").setAttribute("className","green");	
			Average_Trade = "+"+Average_Trade;	
		}
		else
		{
			document.getElementById("Average_Trade").setAttribute("className","red");	
		}
		
		if(Close_Signals*1 >=0 )
		{
			document.getElementById("Close_Signals").setAttribute("className","black");	
		}
		else
		{
			document.getElementById("Close_Signals").setAttribute("className","red");	
		}	
		
		if(Long_Signals*1 >=0 )
		{
			document.getElementById("Long_Signals").setAttribute("className","black");	
		}
		else
		{
			document.getElementById("Long_Signals").setAttribute("className","red");	
		}
		
		//Short_Signals always is red
		if(Short_Signals*1 >=0 )
		{
			document.getElementById("Short_Signals").setAttribute("className","black");	
		}
		else
		{
			document.getElementById("Short_Signals").setAttribute("className","red");
		}
					
		
		document.getElementById("Trade_Success").innerText= Trade_Success+"%";			
		document.getElementById("Total_Profit_Loss").innerText= Total_Profit_Loss;
		document.getElementById("Average_Trade").innerText= Average_Trade;
		
		document.getElementById("Close_Signals").innerText= Close_Signals;
		document.getElementById("Long_Signals").innerText= Long_Signals;
		document.getElementById("Short_Signals").innerText= Short_Signals;
		
		document.getElementById("load_equity_curve_chart").innerText= "";
		document.getElementById("load_profit_loss_chart").innerText= "";
	}
	else
	{
		//Any + numbers should be in green. And - numbers should be in red.
		if(Trade_Success*1 >0 )
		{
			document.getElementById("Trade_Success").setAttribute("class", "green");
		}
		else
		{
			document.getElementById("Trade_Success").setAttribute("class", "red");
		}	
		
		if(Total_Profit_Loss*1 >0 )
		{
			document.getElementById("Total_Profit_Loss").setAttribute("class","green");	
			Total_Profit_Loss = "+"+Total_Profit_Loss;
		}
		else
		{
			document.getElementById("Total_Profit_Loss").setAttribute("class","red");	
		}
		
		if(Average_Trade*1 >0 )
		{
			document.getElementById("Average_Trade").setAttribute("class","green");	
			Average_Trade = "+"+Average_Trade;	
		}
		else
		{
			document.getElementById("Average_Trade").setAttribute("class","red");	
		}
		
		if(Close_Signals*1 >=0 )
		{
			document.getElementById("Close_Signals").setAttribute("class","black");	
		}
		else
		{
			document.getElementById("Close_Signals").setAttribute("class","red");	
		}	
		
		if(Long_Signals*1 >=0 )
		{
			document.getElementById("Long_Signals").setAttribute("class","black");	
		}
		else
		{
			document.getElementById("Long_Signals").setAttribute("class","red");	
		}
		
		//Short_Signals always is red
		if(Short_Signals*1 >=0 )
		{
			document.getElementById("Short_Signals").setAttribute("class","black");	
		}
		else
		{
			document.getElementById("Short_Signals").setAttribute("class","red");
		}
		
		document.getElementById("Trade_Success").textContent= Trade_Success+"%";			
		document.getElementById("Total_Profit_Loss").textContent= Total_Profit_Loss;
		document.getElementById("Average_Trade").textContent= Average_Trade;
		
		document.getElementById("Long_Signals").textContent= Long_Signals;
		document.getElementById("Short_Signals").textContent= Short_Signals;	
		document.getElementById("Close_Signals").textContent= Close_Signals;	
		
		document.getElementById("load_equity_curve_chart").textContent= "";	
		document.getElementById("load_profit_loss_chart").textContent= "";
	}
}

//////////////////Calendar////////////////////////////////////

var curEventObjId = '';

//1: Daily chart 2: Trade Graph
var equity_chart_data_type = 1;
var profit_loss_chart_data_type = 1;

function refreshPage()
{
setTimeout('reloadPage()',500);		
}

function reloadPage()
{
	window.location.href='getDashbord.do';
}

function selectQuickDate(id)
{
	var latestDays = getSelectedValue(id);
		
	if(latestDays == -1)
	{
		//doesn't select any days
		return;
	}
	
	//begin date is Today - latestDays
	var beginDate = new Date();
	var beginTime = beginDate.getTime() - latestDays*24*60*60*1000;
	beginDate.setTime(beginTime);
	
	if(document.getElementById("start_date_input"))
	{
		var fromDateField = document.getElementById("start_date_input");
		fromDateField.value =  beginDate.print("%Y-%m-%d");
		
		//alert(fromDateField.value);
	}
	
	//end date is NOW
	var endDate = new Date();
	if(document.getElementById("finish_date_input"))
	{
		var toDateField = document.getElementById("finish_date_input");
		toDateField.value =  endDate.print("%Y-%m-%d");
		
		//alert(toDateField.value);
	}
	
	calendarStart.setDate(beginDate);	
	calendarEnd.setDate(endDate);
	
	changeFromToDate();
}

function submitRemoveForm(id)
{
	var formOperationID="operationType"+id;
	var formOperation = document.getElementById(formOperationID);
	formOperation.setAttribute("value","2");		
	
	var formID="form"+id;
	document.getElementById(formID).submit();
}

function changeStrategy(timeframeObjID,strategyObjID)
{
	//change related time frame
	var strategyObj = document.getElementById(strategyObjID);
	var timeFrameObj = document.getElementById(timeframeObjID);
	
	if(strategyObj && timeFrameObj)
	{
		var strategyID = strategyObj.value;
		var currentTimeFrameID = getSelectedValue(timeframeObjID);
	
		//clear time frame
		timeFrameObj.options.length = 0;
							
		//set related timeframe
		setTimeFrameOptions(timeFrameObj, currentTimeFrameID, getStrategyTimeFrameStringByID(strategyID));	
		
		changeTimeFrame(timeframeObjID,strategyObjID);		
	}
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

function changeTimeFrame(id,strategyID)
{
	var referenceID = "";
	var marketID="";
	
	if(id != null && id.split(":").length == 3)
	{
		referenceID = id.split(":")[1];
		marketID = id.split(":")[2]
	}
	
	var shortURL = "updateAnalysisMarketTimeframe.do?id="+referenceID+"&timeframe="+getSelectedValue(id)+"&strategyID="+getSelectedValue(strategyID);	

	var objHTTP = getXMLHttp();
	if (objHTTP) 
	{
		objHTTP.open("POST", shortURL, true);
		objHTTP.send(null);
	}
	
	if (true == updateMarketPeriodList(marketID,getSelectedValue(id),getSelectedValue(strategyID)))
	{	
		getStatisticsResult();
		showAnalysisChart();
	}
}

function updateMarketPeriodList(marketID,marketPeriodValue, strategyValue)
{
	var marketIsActived = false; 
	
	if(all_market_id_string.length > 0 && all_strategy_id_string.length > 0 && all_period_id_string.length > 0)
	{
		var new_all_market_id_string = "";
		var new_all_period_id_string = "";
		var new_all_strategy_id_string = "";
				
		var all_market_id_list = all_market_id_string.split(";");
		var all_strategy_id_list = all_strategy_id_string.split(";");
		var all_period_id_list = all_period_id_string.split(";");		
		
		for(var i=0; i<all_market_id_list.length; i++)
		{
			if(all_market_id_list[i] != marketID)
			{
				new_all_market_id_string += (all_market_id_list[i]+";");
				new_all_period_id_string += (all_period_id_list[i]+";");
				new_all_strategy_id_string += (all_strategy_id_list[i]+";");
			}
			else
			{
				marketIsActived = true;
			}
		}
		
		if(marketIsActived)
		{
			new_all_market_id_string += marketID;
			new_all_strategy_id_string += strategyValue;
			new_all_period_id_string += marketPeriodValue;
			
			all_market_id_string = new_all_market_id_string;
			all_strategy_id_string = new_all_strategy_id_string;
			all_period_id_string = new_all_period_id_string;
		}
	}
	
	return marketIsActived;
}

////////////////////////////////////////////////////////////////////

function setConditionFlag(imageId,condition,flag)
{
	//alert(imageId+" ; "+condition+" ; "+flag);
	var conditions = condition.split(',');
	
	var condition_id = conditions[0];
	var market_id = conditions[1];
	var period_id = conditions[2];
	var strategy_id = conditions[3];
	
	period_id = getSelectedValue(period_id);
	strategy_id = getSelectedValue(strategy_id);
	
	var active_flag = 1;
	
	//change chart
	if(flag == 'active')
	{
		active_flag = 1;
		
		//first tick the checkbox, then update data
		document.getElementById(imageId).setAttribute("src","../images/dot_check2.gif");	
		document.getElementById(imageId).setAttribute("title","disable");
	
		//active market
		if(all_market_id_string != '')
		{
			all_market_id_string += ";";			
		}
		
		all_market_id_string += market_id;
		
		if(all_strategy_id_string != '')
		{
			all_strategy_id_string += ";"
		}
		
		all_strategy_id_string += strategy_id;
		
		if(all_period_id_string != '')
		{
			all_period_id_string += ";";			
		}
		
		all_period_id_string += period_id;
								
		activeMarketBaseFunction(all_market_id_string, all_period_id_string, all_strategy_id_string);
		getStatisticsResult();
	}
	else
	{
		active_flag = 2;
		
		//first change checkbox, then update data
		document.getElementById(imageId).setAttribute("src","../images/dot_check.gif");
		document.getElementById(imageId).setAttribute("title","active");
		
		if(all_market_id_string.length > 0 && all_period_id_string.length > 0 && all_strategy_id_string.length > 0)
		{
			var new_all_market_id_string = "";
			var new_all_period_id_string = "";
			var new_all_strategy_id_string = "";
			
			var all_market_id_list = all_market_id_string.split(";");
			var all_period_id_list = all_period_id_string.split(";");
			var all_strategy_id_list = all_strategy_id_string.split(";");
			
			//alert('market_id='+market_id);
			//alert('period_id='+period_id);
			
			for(var i=0; i<all_market_id_list.length; i++)
			{
				if(all_market_id_list[i] != market_id)
				{
					new_all_market_id_string += (all_market_id_list[i]+";");
					new_all_period_id_string += (all_period_id_list[i]+";");
					new_all_strategy_id_string += (all_strategy_id_list[i]+";");
				}
			}			
			
			//alert(new_all_market_id_string+" | "+new_all_period_id_string);
			
			if(new_all_market_id_string.length > 1 && new_all_period_id_string.length > 1)
			{
				new_all_market_id_string = new_all_market_id_string.substring(0, new_all_market_id_string.length -1);
				new_all_period_id_string = new_all_period_id_string.substring(0, new_all_period_id_string.length -1);
				new_all_strategy_id_string = new_all_strategy_id_string.substring(0, new_all_strategy_id_string.length -1);
			}
			
			all_market_id_string = new_all_market_id_string;
			all_period_id_string = new_all_period_id_string;
			all_strategy_id_string = new_all_strategy_id_string;
						
			activeMarketBaseFunction(all_market_id_string, all_period_id_string, all_strategy_id_string);
			getStatisticsResult();
		}
	}	
	
	//alert(all_market_id_string+" | "+all_period_id_string);
	
	//update 
	var shortURL = "updateMyAnalysisActiveCondition.do?source=2&id="+condition_id+"&flag="+active_flag;
   
	var objHTTP = getXMLHttp();
	if (objHTTP) 
	{
		objHTTP.open("POST", shortURL, true);
		objHTTP.send(null);
	}
}

function activeMarketBaseFunction(all_market_id_list, all_period_id_list, all_strategy_id_list)
{
		var new_base_cumu_chart_active_url = base_cumu_chart_active_url + "&marketID="+all_market_id_list+"&chartData="+equity_chart_data_type;
		new_base_cumu_chart_active_url = new_base_cumu_chart_active_url + "&periodID="+all_period_id_list+"&strategyID="+all_strategy_id_list;
				
		var new_base_profitloss_chart_active_url = base_profitloss_chart_active_url + "&marketID="+all_market_id_list+"&chartData="+profit_loss_chart_data_type;
		new_base_profitloss_chart_active_url = new_base_profitloss_chart_active_url + "&periodID="+all_period_id_list+"&strategyID="+all_strategy_id_list;
				
		var fromDateList = "&from=";
		var toDateList = "&to=";
				
		var fromDateField = document.getElementById("start_date_input");
		var toDateField = document.getElementById("finish_date_input");
			  		
		fromDateList+= fromDateField.value;
		toDateList+= toDateField.value;
		
		var new_cumu_chart_full_url = new_base_cumu_chart_active_url+fromDateList+toDateList;
		var new_profitloss_chart_full_url = new_base_profitloss_chart_active_url+fromDateList+toDateList;
		
		//alert(new_cumu_chart_full_url);
		//alert(new_profitloss_chart_full_url);
						
		var profitLossChartImage = document.getElementById("profit_loss_chart_image");
		var cumulativeChartImage = document.getElementById("cumulative_chart_image");
		cumulativeChartImage.setAttribute("src",new_cumu_chart_full_url);	
		profitLossChartImage.setAttribute("src",new_profitloss_chart_full_url);	
}

function setFromDateString(dateString)
{
	if(dateString && dateString.split('-').length >= 3)
	{
		var oCurrentDate = new Date(dateString.split('-')[0],dateString.split('-')[1]-1,dateString.split('-')[2]);
		
		var dateFormatString = dateFormat(oCurrentDate, "mmmm, yyyy");
		if(document.getElementById("start_date_string"))
		{
			if(isIE())
			{
				document.getElementById("start_date_string").innerText=dateFormatString;
			}
			else
			{
				document.getElementById("start_date_string").textContent=dateFormatString;
			}
		}
	}
}

function setToDateString(dateString)
{
	if(dateString && dateString.split('-').length >= 3)
	{
		var oCurrentDate = new Date(dateString.split('-')[0],dateString.split('-')[1]-1,dateString.split('-')[2]);
		
		var dateFormatString = dateFormat(oCurrentDate, "mmmm, yyyy");
	
		if(document.getElementById("finish_date_string"))
		{
			if(isIE())
			{
				document.getElementById("finish_date_string").innerText=dateFormatString;
			}
			else
			{
				document.getElementById("finish_date_string").textContent=dateFormatString;
			}
		}
	}
}

function changeFromToDate()
{		
	var fromDateField = document.getElementById("start_date_input");
	var toDateField = document.getElementById("finish_date_input");
	
	if(fromDateField && toDateField)
	{	
     	start_date = fromDateField.value;
	    finish_date = toDateField.value;
	    
	    setFromDateString(start_date);
	    setToDateString(finish_date);
	    
	    getStatisticsResult();
		showAnalysisChart();
		
		//update from, to Date	
		submitDateChange(start_date, finish_date);
	}
}

function submitDateChange(fromDateCondition,toDateCondition) 
{
   	var shortURL = "updateMyAnalysisDateCondition.do?fromDate="+fromDateCondition+"&toDate="+toDateCondition;
   
	var objHTTP = getXMLHttp();
	if (objHTTP) 
	{
		objHTTP.open("POST", shortURL, true);
		objHTTP.send(null);
	}	 
}

function showAnalysisChart()
{
	var profitLossChartImage = document.getElementById("profit_loss_chart_image");
	var cumulativeChartImage = document.getElementById("cumulative_chart_image");
	
	var fromDateList = "&from=";
	var toDateList = "&to=";
	
	var strategyIDList = "&strategyID="+all_strategy_id_string;
	var marketIDList = "&marketID="+all_market_id_string;
	var periodIDList = "&periodID="+all_period_id_string;	

	var fromDateField = document.getElementById("start_date_input");
	var toDateField = document.getElementById("finish_date_input");
		  		
	fromDateList+= fromDateField.value;
	toDateList+= toDateField.value;
	
	var new_cumu_chart_full_url = base_cumu_chart_active_url+marketIDList+strategyIDList+periodIDList+fromDateList+toDateList+"&chartData="+equity_chart_data_type;
	var new_profitloss_chart_full_url = base_profitloss_chart_active_url+marketIDList+strategyIDList+periodIDList+fromDateList+toDateList+"&chartData="+profit_loss_chart_data_type;
	
	cumulativeChartImage.setAttribute("src",new_cumu_chart_full_url);	
	profitLossChartImage.setAttribute("src",new_profitloss_chart_full_url);	
}

function pageOnload()
{	
	var date2 = new Date();
								
	var fromDateField = document.getElementById("start_date_input");		        
	if(start_date != '')
	{
		fromDateField.value =  start_date;
	}
	else
	{		
		fromDateField.value =  date2.print("%Y-%m-%d");
	}
	
	var toDateField = document.getElementById("finish_date_input");
	if(finish_date != '')
	{	
		toDateField.value = finish_date;
	}
	else
	{
		toDateField.value =  date2.print("%Y-%m-%d");
	}
	
	setFromDateString(fromDateField.value);
	setToDateString(toDateField.value);
	    
	getStatisticsResult();
	showAnalysisChart();
	
	if(showDateCondition == 'true')
	{
		showDateCondtion(false);
	}
	else
	{
		hideDateCondtion(false);
	}
	
	if(showMarketCondition == 'true')
	{
		showMarketCondtion(true);
	}
	else
	{
		hideMarketCondtion(false);
	}
}

//////////////////////////////////////////hide show date//////////////////////////////////////////////////
var currentIsShowDateCondition = false;

function hideShowDateCondition(updateServer)
{
	if(currentIsShowDateCondition)
	{
		hideDateCondtion(updateServer);
	}
	else
	{
		showDateCondtion(updateServer);
	}
}

function showDateCondtion(updateServer)
{
	var dateCondtion = document.getElementById('tabDateCondition');
	var dateCondtion2 = document.getElementById('tabDateCondition2');
	
	if(dateCondtion && dateCondtion2)
	{
		dateCondtion.style.display = '';
		dateCondtion2.style.display = '';
		
		currentIsShowDateCondition=true;
		
		var source = document.getElementById('hideDateConditionImg');
		var sourceParent = document.getElementById('show_hide_date_btn_id');
		
		
		if(source && sourceParent)
		{
			//source.setAttribute("src","../images/close_button.gif");
			source.setAttribute("src","../images/nav1_trans.gif");
			if(isIE())
			{
				sourceParent.className = "hide_analysis_btn";
			}
			else
			{
				sourceParent.setAttribute("class","hide_analysis_btn");
			}
			source.setAttribute("alt","Hide Date");
			source.setAttribute("title","Hide Date");
			
			if(updateServer)
			{
				//update 
				var shortURL = "updateUserSessionAttribute.do?attribute_name=showDateCondition&attribute_value=true";
			   
				var objHTTP = getXMLHttp();
				if (objHTTP) 
				{
					objHTTP.open("POST", shortURL, true);
					objHTTP.send(null);
				}
			}
		}
	}
}

function hideDateCondtion(updateServer)
{
	var dateCondtion = document.getElementById('tabDateCondition');
	var dateCondtion2 = document.getElementById('tabDateCondition2');
	
	if(dateCondtion && dateCondtion2)
	{
		dateCondtion.style.display = 'none';
		dateCondtion2.style.display = 'none';
		
		currentIsShowDateCondition=false;
		
		var source = document.getElementById('hideDateConditionImg');
		var sourceParent = document.getElementById('show_hide_date_btn_id');
		
		if(source && sourceParent)
		{
			//source.setAttribute("src","../images/open_button.gif");
			source.setAttribute("src","../images/nav1_trans.gif");
			if(isIE())
			{
				sourceParent.className = "show_analysis_btn";
			}
			else
			{
				sourceParent.setAttribute("class","show_analysis_btn");
			}
			source.setAttribute("alt","Open Date");
			source.setAttribute("title","Open Date");
			
			if(updateServer)
			{
				//update 
				var shortURL = "updateUserSessionAttribute.do?attribute_name=showDateCondition&attribute_value=false";
			   
				var objHTTP = getXMLHttp();
				if (objHTTP) 
				{
					objHTTP.open("POST", shortURL, true);
					objHTTP.send(null);
				}
			}
		}
	}
}
//////////////////////////////////////////hide show date//////////////////////////////////////////////////

//////////////////////////////////////////hide show markets///////////////////////////////////////////////
var currentIsShowMarketCondition = false;

function hideShowMarketCondition(updateServer)
{
	if(currentIsShowMarketCondition)
	{
		hideMarketCondtion(updateServer);
	}
	else
	{
		showMarketCondtion(updateServer);
	}
}

function hideMarketCondtion(updateServer)
{
	var marketCondition = document.getElementById('tabMarketCondition');
	var marketCondition2 = document.getElementById('tabMarketCondition2');
	
	if(marketCondition && marketCondition2)
	{
		marketCondition.style.display = 'none';
		marketCondition2.style.display = 'none';
		
		currentIsShowMarketCondition=false;
		
		var source = document.getElementById('marketConditionImg');
		var sourceParent = document.getElementById('show_hide_strategy_btn_id');
		
		if(source && sourceParent)
		{
			//source.setAttribute("src","../images/open_button.gif");
			source.setAttribute("src","../images/nav1_trans.gif");
			if(isIE())
			{
				sourceParent.className = "show_analysis_btn";
			}
			else
			{
				sourceParent.setAttribute("class","show_analysis_btn");
			}
			source.setAttribute("alt","Open Strategies");
			source.setAttribute("title","Open Strategies");
			
			if(updateServer)
			{
				//update 
				var shortURL = "updateUserSessionAttribute.do?attribute_name=showMarketCondition&attribute_value=false";
			   
				var objHTTP = getXMLHttp();
				if (objHTTP) 
				{
					objHTTP.open("POST", shortURL, true);
					objHTTP.send(null);
				}
			}
		}
	}
}

function showMarketCondtion(updateServer)
{
	var marketCondition = document.getElementById('tabMarketCondition');
	var marketCondition2 = document.getElementById('tabMarketCondition2');
	
	if(marketCondition && marketCondition2)
	{
		marketCondition.style.display = '';
		marketCondition2.style.display = '';
		currentIsShowMarketCondition=true;
		
		var marketConditionImgParent = document.getElementById('marketConditionImgParent');
		
		var source = document.getElementById('marketConditionImg');
		var sourceParent = document.getElementById('show_hide_strategy_btn_id');
		
		if(source && sourceParent)
		{
			//source.setAttribute("src","../images/close_button.gif");
			source.setAttribute("src","../images/nav1_trans.gif");
			if(isIE())
			{
				sourceParent.className = "hide_analysis_btn";
			}
			else
			{
				sourceParent.setAttribute("class","hide_analysis_btn");
			}
			source.setAttribute("alt","Hide Strategies");
			source.setAttribute("title","Hide Strategies");
			
			if(updateServer)
			{
				//update 
				var shortURL = "updateUserSessionAttribute.do?attribute_name=showMarketCondition&attribute_value=true";
			   
				var objHTTP = getXMLHttp();
				if (objHTTP) 
				{
					objHTTP.open("POST", shortURL, true);
					objHTTP.send(null);
				}
			}
		}
	}
}
//////////////////////////////////////////hide show markets///////////////////////////////////////////////

function changeProfitLossChartTimeFrame(id)
{
	var profitLossChartObj = document.getElementById(id);
	
	if(profitLossChartObj)
	{
		profit_loss_chart_data_type = profitLossChartObj.value;
			
		showAnalysisChart();
	}
}

function changeEquityCurveChartTimeFrame(id)
{
	var equityCurveChartObj = document.getElementById(id);
	
	if(equityCurveChartObj)
	{
		equity_chart_data_type = equityCurveChartObj.value;
			
		showAnalysisChart();
	}
}
