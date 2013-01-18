var acceptAgreement = false;
function checkAgreements()
{
	if(acceptAgreement)
	{
		document.getElementById("login_form").submit();
	}
	else
	{
		if(isIE())
		{
			document.getElementById("agreement_prompt").className="errorinfo-active";
			document.getElementById("agreement_prompt").innerText="Clear Traders requires that you agree to the Service Agreement before using the service. Thankyou.";
			
			document.getElementById("error_info_prompt").innerText="";
			document.getElementById("error_info_prompt").className="errorinfo";
			
			document.getElementById("normal_info_prompt").innerText="";
			document.getElementById("normal_info_prompt").className="promptinfo";
			
			document.getElementById("password_errorinfo").innerText="";
			document.getElementById("password_errorinfo").className="errorinfo";
			
			document.getElementById("email_errorinfo").innerText="";	
			document.getElementById("email_errorinfo").className="errorinfo";
			
		}
		else
		{
			document.getElementById("agreement_prompt").textContent="Clear Traders requires that you agree to the Service Agreement before using the service. Thankyou.";
			document.getElementById("error_info_prompt").textContent="";
			document.getElementById("normal_info_prompt").textContent="";
			document.getElementById("password_errorinfo").textContent="";
			document.getElementById("email_errorinfo").textContent="";
		}
		
		return false;
	}
}
function changeAgreements()
{
	if(acceptAgreement)
	{
		acceptAgreement = false;
		document.getElementById("agreement_imge").setAttribute("src","../images/icon_check_2.gif");	
		document.getElementById("login_button").disabled=true;
		
		if(isIE())
		{
			document.getElementById("agreement_prompt").className="errorinfo-active";
			document.getElementById("agreement_prompt").innerText="Clear Traders requires that you agree to the Service Agreement before using the service. Thankyou.";
			
			document.getElementById("error_info_prompt").innerText="";
			document.getElementById("error_info_prompt").className="errorinfo";
			
			document.getElementById("normal_info_prompt").innerText="";
			document.getElementById("normal_info_prompt").className="promptinfo";
			
			document.getElementById("password_errorinfo").innerText="";
			document.getElementById("password_errorinfo").className="errorinfo";
			
			document.getElementById("email_errorinfo").innerText="";	
			document.getElementById("email_errorinfo").className="errorinfo";
			
		}
		else
		{
			document.getElementById("agreement_prompt").textContent="Clear Traders requires that you agree to the Service Agreement before using the service. Thankyou.";
			document.getElementById("error_info_prompt").textContent="";
			document.getElementById("normal_info_prompt").textContent="";
			document.getElementById("password_errorinfo").textContent="";
			document.getElementById("email_errorinfo").textContent="";
		}
	}
	else
	{
		acceptAgreement = true;
		document.getElementById("agreement_imge").setAttribute("src","../images/login-tick.gif");	
		document.getElementById("login_button").disabled=false;
		
		if(isIE())
		{
			document.getElementById("agreement_prompt").innerText="";
			document.getElementById("agreement_prompt").className="errorinfo";
			
		}
		else
		{
			document.getElementById("agreement_prompt").textContent="";
		}
	}
}