
function WorldClock()
{	
	if(document.getElementById("time_zone") != null)
	{
		now=new Date();
		ofst=now.getTimezoneOffset()/60;
		secs=now.getSeconds();
		sec=-1.57+Math.PI*secs/30;
		mins=now.getMinutes();
		min=-1.57+Math.PI*mins/30;
		hr=(now.getHours() + parseInt(ofst)) + parseInt(getSelectedValue("time_zone"));
		hrs=-1.575+Math.PI*hr/6+Math.PI*parseInt(now.getMinutes())/360;
		if (hr < 0) hr+=24;
		if (hr > 23) hr-=24;
		ampm = (hr > 11)?"PM":"AM";
		statusampm = ampm.toLowerCase();
		
		hr2 = hr;
		if (hr2 == 0) hr2=12;
		(hr2 < 13)?hr2:hr2 %= 12;
		if (hr2<10) hr2="0"+hr2
		
		var finaltime=hr2+':'+((mins < 10)?"0"+mins:mins)+':'+((secs < 10)?"0"+secs:secs)+' '+statusampm;
		
		var currentTimeZoneName = getSelectedText("time_zone");
		var currentTime = finaltime + " "+currentTimeZoneName;
		if(isIE())
		{
			document.getElementById("current_time").innerText= currentTime;	
		}
		else
		{
			document.getElementById("current_time").textContent= currentTime;
		}
	}
	setTimeout('WorldClock()',1000);
}

function isIE()
{
	if (window.navigator.userAgent.toLowerCase().indexOf("msie")>=1)
	    return true;
	else
	    return false;
} 

function getSelectedText(name)
{	
	return document.getElementById(name).options[document.getElementById(name).options.selectedIndex].text;
}

function getSelectedValue(name)
{
	return document.getElementById(name).options[document.getElementById(name).options.selectedIndex].value;
}

function pageOnLoad()
{
	var dailyGraphImg = document.getElementById('checkDefaultContactsImage');

	if(useContactInfoFlag == 1)
	{
		dailyGraphImg.setAttribute("src","../images/dot_check2.gif");
		dailyGraphImg.setAttribute("name","1")
		
		document.getElementById('email_address').readOnly=true;
		document.getElementById('mobile_number').readOnly=true;		
		document.getElementById('use_conact_info_flag').setAttribute("value","1");
	}
	else
	{
		dailyGraphImg.setAttribute("src","../images/dot_check.gif");
		dailyGraphImg.setAttribute("name","2")
		
		document.getElementById('email_address').readOnly=false;
		document.getElementById('mobile_number').readOnly=false;	
		document.getElementById('use_conact_info_flag').setAttribute("value","2");
	}
	
	WorldClock();
}

function checkDefaultContacts(flag)
{	
	var dailyGraphImg = document.getElementById('checkDefaultContactsImage');
		
	if(flag != 1)
	{
		dailyGraphImg.setAttribute("src","../images/dot_check2.gif");
		dailyGraphImg.setAttribute("name","1")
		
		document.getElementById('email_address').readOnly=true;
		document.getElementById('mobile_number').readOnly=true;		
		document.getElementById('use_conact_info_flag').setAttribute("value","1");
	}
	else
	{
		dailyGraphImg.setAttribute("src","../images/dot_check.gif");
		dailyGraphImg.setAttribute("name","2")
		
		document.getElementById('email_address').readOnly=false;
		document.getElementById('mobile_number').readOnly=false;	
		document.getElementById('use_conact_info_flag').setAttribute("value","2");
	}
}