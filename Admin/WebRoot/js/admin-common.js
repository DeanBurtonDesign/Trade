
function getSelectedValue(name)
{
	return document.getElementById(name).options[document.getElementById(name).options.selectedIndex].value;
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