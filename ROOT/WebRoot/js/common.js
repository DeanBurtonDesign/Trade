


function getSignalPeriodMinutes(timeFrameType)
{
	var periodMinutes=1;
	
	if(timeFrameType == 1)
	{
		periodMinutes = 1;
	}
	else if(timeFrameType == 2)
	{
		periodMinutes = 5;
	}
	else if(timeFrameType == 3)
	{
		periodMinutes = 10;
	}
	else if(timeFrameType == 4)
	{
		periodMinutes = 30;
	}
	else if(timeFrameType == 5)
	{
		periodMinutes = 60;
	}
	else if(timeFrameType == 6)
	{
		periodMinutes = 24*60;
	}
	
	return periodMinutes;
}
	
function getPeriodNameByID(signalPeriodTypeID)
{
	if(signalPeriodTypeID == 1)
	{
		return "1 Min";
	}
	else if(signalPeriodTypeID == 2)
	{
		return "5 Min";
	}
	else if(signalPeriodTypeID == 3)
	{
		return "10 Min";
	}
	else if(signalPeriodTypeID == 4)
	{
		return "30 Min";
	}
	else if(signalPeriodTypeID == 5)
	{
		return "1 Hour";
	}
	else if(signalPeriodTypeID == 6)
	{
		return "1 Day";
	}
	
	return "";
}
	
function getSelectedValue(name)
{
	if(document.getElementById(name))
	{
		return document.getElementById(name).options[document.getElementById(name).options.selectedIndex].value;
	}
	
	return null;
}
	
function getXMLHttp() 
{
	var xmlhttp = null;
	
	if (window.ActiveXObject) 
	{
		if (navigator.userAgent.toLowerCase().indexOf("msie 5") != -1) 
		{
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		} 
		else 
		{
 			xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
		}
	}
	
	if (!xmlhttp && typeof(XMLHttpRequest) != 'undefined') 
	{
 		xmlhttp = new XMLHttpRequest();
	}
	
	return xmlhttp;
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
}

function isIE(){ 
	if (window.navigator.userAgent.toLowerCase().indexOf("msie")>=1) 
		return true; 
	else 
		return false; 
} 

var dateFormat = function () {  
     var token = /d{1,4}|m{1,4}|yy(?:yy)?|([HhMsTt])\1?|[LloSZ]|"[^"]*"|'[^']*'/g,  
         timezone = /\b(?:[PMCEA][SDP]T|(?:Pacific|Mountain|Central|Eastern|Atlantic) (?:Standard|Daylight|Prevailing) Time|(?:GMT|UTC)(?:[-+]\d{4})?)\b/g,  
         timezoneClip = /[^-+\dA-Z]/g,  
         pad = function (val, len) {  
             val = String(val);  
             len = len || 2;  
             while (val.length < len) val = "0" + val;  
             return val;  
         };  
   
     // Regexes and supporting functions are cached through closure  
     return function (date, mask, utc) {  
         var dF = dateFormat;  
   
         // You can't provide utc if you skip other args (use the "UTC:" mask prefix)  
         if (arguments.length == 1 && (typeof date == "string" || date instanceof String) && !/\d/.test(date)) {  
             mask = date;  
             date = undefined;  
         }  
   
         // Passing date through Date applies Date.parse, if necessary  
         date = date ? new Date(date) : new Date();  
         if (isNaN(date)) throw new SyntaxError("invalid date");  
   
         mask = String(dF.masks[mask] || mask || dF.masks["default"]);  
   
         // Allow setting the utc argument via the mask  
         if (mask.slice(0, 4) == "UTC:") {  
             mask = mask.slice(4);  
             utc = true;  
         }  
   
         var _ = utc ? "getUTC" : "get",  
             d = date[_ + "Date"](),  
             D = date[_ + "Day"](),  
             m = date[_ + "Month"](),  
             y = date[_ + "FullYear"](),  
             H = date[_ + "Hours"](),  
             M = date[_ + "Minutes"](),  
             s = date[_ + "Seconds"](),  
             L = date[_ + "Milliseconds"](),  
             o = utc ? 0 : date.getTimezoneOffset(),  
             flags = {  
                 d:    d,  
                 dd:   pad(d),  
                 ddd:  dF.i18n.dayNames[D],  
                 dddd: dF.i18n.dayNames[D + 7],  
                 m:    m + 1,  
                 mm:   pad(m + 1),  
                 mmm:  dF.i18n.monthNames[m],  
                 mmmm: dF.i18n.monthNames[m + 12],  
                 yy:   String(y).slice(2),  
                 yyyy: y,  
                 h:    H % 12 || 12,  
                 hh:   pad(H % 12 || 12),  
                 H:    H,  
                 HH:   pad(H),  
                 M:    M,  
                 MM:   pad(M),  
                 s:    s,  
                 ss:   pad(s),  
                 l:    pad(L, 3),  
                 L:    pad(L > 99 ? Math.round(L / 10) : L),  
                 t:    H < 12 ? "a"  : "p",  
                 tt:   H < 12 ? "am" : "pm",  
                 T:    H < 12 ? "A"  : "P",  
                 TT:   H < 12 ? "AM" : "PM",  
                 Z:    utc ? "UTC" : (String(date).match(timezone) || [""]).pop().replace(timezoneClip, ""),  
                 o:    (o > 0 ? "-" : "+") + pad(Math.floor(Math.abs(o) / 60) * 100 + Math.abs(o) % 60, 4),  
                 S:    ["th", "st", "nd", "rd"][d % 10 > 3 ? 0 : (d % 100 - d % 10 != 10) * d % 10]  
             };  
   
         return mask.replace(token, function ($0) {  
             return $0 in flags ? flags[$0] : $0.slice(1, $0.length - 1);  
         });  
     };  
 }();  
   
 // Some common format strings  
 dateFormat.masks = {  
     "default":      "ddd mmm dd yyyy HH:MM:ss",  
     shortDate:      "m/d/yy",  
     mediumDate:     "mmm d, yyyy",  
     longDate:       "mmmm d, yyyy",  
     fullDate:       "dddd, mmmm d, yyyy",  
     shortTime:      "h:MM TT",  
     mediumTime:     "h:MM:ss TT",  
     longTime:       "h:MM:ss TT Z",  
     isoDate:        "yyyy-mm-dd",  
     isoTime:        "HH:MM:ss",  
     isoDateTime:    "yyyy-mm-dd'T'HH:MM:ss",  
     isoUtcDateTime: "UTC:yyyy-mm-dd'T'HH:MM:ss'Z'"  
 };  
   
 // Internationalization strings  
 dateFormat.i18n = {  
     dayNames: [  
         "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",  
         "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"  
     ],  
     monthNames: [  
         "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",  
         "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"  
     ]  
 }; 

function insertHtml(where, el, html)
{
        where = where.toLowerCase();
        if(el.insertAdjacentHTML){
            switch(where){
                case "beforebegin":
                    el.insertAdjacentHTML('BeforeBegin', html);
                    return el.previousSibling;
                case "afterbegin":
                    el.insertAdjacentHTML('AfterBegin', html);
                    return el.firstChild;
                case "beforeend":
                    el.insertAdjacentHTML('BeforeEnd', html);
                    return el.lastChild;
                case "afterend":
                    el.insertAdjacentHTML('AfterEnd', html);
                    return el.nextSibling;
            }
            throw 'Illegal insertion point -> "' + where + '"';
        }
        
  		var range = el.ownerDocument.createRange();
        var frag;
        switch(where)
        {
             case "beforebegin":
                range.setStartBefore(el);
                frag = range.createContextualFragment(html);
                el.parentNode.insertBefore(frag, el);
                return el.previousSibling;
             case "afterbegin":
                if(el.firstChild){
                    range.setStartBefore(el.firstChild);
                    frag = range.createContextualFragment(html);
                    el.insertBefore(frag, el.firstChild);
                    return el.firstChild;
                }else{
                    el.innerHTML = html;
                    return el.firstChild;
                }
            case "beforeend":
                if(el.lastChild){
                    range.setStartAfter(el.lastChild);
                    frag = range.createContextualFragment(html);
                    el.appendChild(frag);
                    return el.lastChild;
                }else{
                    el.innerHTML = html;
                    return el.lastChild;
                }
            case "afterend":
                range.setStartAfter(el);
                frag = range.createContextualFragment(html);
                el.parentNode.insertBefore(frag, el.nextSibling);
                return el.nextSibling;
       }
       throw 'Illegal insertion point -> "' + where + '"';
}