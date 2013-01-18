function isIE(){ 
	if (window.navigator.userAgent.toLowerCase().indexOf("msie")>=1) 
		return true; 
	else 
		return false; 
} 
function fetchOffset(obj) {
	var left_offset = obj.offsetLeft;
	var top_offset = obj.offsetTop;
	while((obj = obj.offsetParent) != null) {
		left_offset += obj.offsetLeft;
		top_offset += obj.offsetTop;
	}
	return { 'left' : left_offset, 'top' : top_offset };
}
function onClockDown(e,objId){
	e = e ? e : window.event;
	$obj = e.srcElement?e.srcElement:e.target;//document.getElementById(objId);e.target
	if($obj){
		e_pos = fetchOffset($obj);
		pop_x = parseInt(e_pos['left']) + 2;
		pop_y = parseInt(e_pos['top']) + 18;
		curEventObjId = objId;
		$popmenu = document.getElementById('popmenu');
		if($popmenu){		
			$popmenu.style.left = pop_x+'px';
			$popmenu.style.top = pop_y+'px';
			$popmenu.style.display = '';
		}
	}
	//stop propagation
	if(e && e.stopPropagation){
		e.stopPropagation();
	}else
	{
	window.event.cancelBubble = true;
	}
}
function setClockVal(val){
	curEventObj = document.getElementById(curEventObjId);
	if(curEventObj){
		if(!isIE()){
			curEventObj.textContent = val;
		}else{
			curEventObj.innerText = val;
		}
	}
	hideMenu();
}
function hideMenu(){
	$popmenu = document.getElementById('popmenu');
	if($popmenu){
		$popmenu.style.display = 'none';
	}
}