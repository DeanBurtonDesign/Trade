package com.cleartraders.webapp.model.payment;

import java.util.HashMap;
import java.util.List;

import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.webapp.config.WebappResManager;
import com.cleartraders.webapp.db.DBAccessor;
import com.cleartraders.webapp.model.bean.PaymentHistoryBean;

public abstract class IPNPaymentEventHandler implements IPNHandler
{

    public boolean handle(HashMap<String, String> allParameters)
    {
        if(checkCommonParameters(allParameters) && checkSpecialParameters(allParameters))
        {
            return handleSpecial(allParameters);
        }
        else
        {
            return false;
        }
    }
    
    public abstract boolean handleSpecial(HashMap<String, String> allParameters);
    public abstract boolean checkSpecialParameters(HashMap<String, String> allParameters);
    
    boolean checkCommonParameters(HashMap<String, String> allParameters)
    {
        //check payment status
        String payment_status = allParameters.get("payment_status");
        if(!"Completed".equalsIgnoreCase(payment_status))
        {
            InfoTrace.getInstance().printInfo(DebugLevel.WARNING, "Payment_status, '"+payment_status+"', is wrong! It should be 'Completed'");
            LogTools.getInstance().insertLog(DebugLevel.WARNING, "Payment_status, '"+payment_status+"', is wrong! It should be 'Completed'");
            
            return false;
        }
        
        //check txn id
        String txn_id = allParameters.get("txn_id");
        String userLoginName = allParameters.get("custom");
        
        //check transactionID is unique
        List<PaymentHistoryBean> oPaymentList = DBAccessor.getInstance().getPaymentListByTxnID(txn_id);
        if(null != oPaymentList && oPaymentList.size() > 0)
        {
            InfoTrace.getInstance().printInfo(DebugLevel.WARNING, "User used old TXN ID: User login name is,'"+userLoginName+"', TXN id="+txn_id);
            LogTools.getInstance().insertLog(DebugLevel.WARNING, "User used old TXN ID: User login name is,'"+userLoginName+"', TXN id="+txn_id);
            
            return false;
        }
        
        //check receiver Email
        String systemPaypalAccount = WebappResManager.getInstance().getPaypal_account();
        if(!systemPaypalAccount.equals(allParameters.get("receiver_email")))
        {
            InfoTrace.getInstance().printInfo(DebugLevel.WARNING, "Receiver, '"+allParameters.get("receiver_email")+"', is wrong! It should be "+systemPaypalAccount);
            LogTools.getInstance().insertLog(DebugLevel.WARNING, "Receiver, '"+allParameters.get("receiver_email")+"', is wrong! It should be "+systemPaypalAccount);
            
            return false;
        }
        
        //check currentcy
        String paymentCurrency = allParameters.get("mc_currency");
        if(!WebappResManager.getInstance().getPaypal_product_currency().equalsIgnoreCase(paymentCurrency))
        {
            InfoTrace.getInstance().printInfo(DebugLevel.WARNING, "Currency Error! Payment currency is , '"+paymentCurrency+"'. It should be "+WebappResManager.getInstance().getPaypal_product_currency());
            LogTools.getInstance().insertLog(DebugLevel.WARNING, "Currency Error! Payment currency is , '"+paymentCurrency+"'. It should be "+WebappResManager.getInstance().getPaypal_product_currency());
            
            return false;
        }
        
        return true;
    }

}
