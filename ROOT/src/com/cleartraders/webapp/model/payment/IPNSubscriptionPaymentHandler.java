package com.cleartraders.webapp.model.payment;

import java.util.HashMap;
import java.util.List;

import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.MarketTypeAndPeriodBean;
import com.cleartraders.common.entity.ProductBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.email.EmailBean;
import com.cleartraders.common.util.email.EmailHandler;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.InfoTrace;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.config.WebappResManager;
import com.cleartraders.webapp.db.DBAccessor;
import com.cleartraders.webapp.model.bean.PaymentHistoryBean;
import com.cleartraders.webapp.model.notification.EmailNotificationFactory;
import com.cleartraders.webapp.model.signup.SignupController;

public class IPNSubscriptionPaymentHandler implements IPNHandler
{
    
    //check following info
    /**
     * 1) Confirm that the payment_status is Completed, since IPNs are also sent for other results such as Pending or Failed
     * 2) Check that the txn_id is unique, to prevent a fraudster from reusing an old, completed transaction
     * 3) Validate that the receiver_email is an email address registered in your PayPal account, to prevent the payment from being sent to a fraudster's account
     * 4) Check other transaction details, such as the item number and price, to confirm that the price has not been changed. We also recommend using the custom or invoice variable to pass your own transaction ID that can you check.
     */
    public boolean handle(HashMap<String, String> allParameters)
    {
        InfoTrace.getInstance().printInfo(DebugLevel.INFO, "Receive Subscription Payment event!");
        LogTools.getInstance().insertLog(DebugLevel.INFO, "Receive Subscription Payment event!");
                
        if(checkParameters(allParameters))
        {
            String userLoginName = allParameters.get("custom");
            String txnID = allParameters.get("txn_id");
            String itemName = allParameters.get("item_name");
            double paymentGross = Double.valueOf(allParameters.get("payment_gross")).doubleValue();
            
            UserBean userBean = DBAccessor.getInstance().getUserInfoByLoginName(userLoginName);

            ProductBean oCurrentProduct = DBAccessor.getInstance().getProductByPrice(paymentGross); 
            
            //if product id is NOT changed, it means this is normal subscription payment or first time
            //otherwise, that means that this is the member ship upgrade
            if(oCurrentProduct.getId() == userBean.getMemberLevel())
            {
                //normal subscription or first subscription payment
                
                commonProcedureForSubscriptionPayment(userBean, oCurrentProduct, txnID, itemName);
                
                InfoTrace.getInstance().printInfo(DebugLevel.INFO, "Complete handle Paypal Subscription payment (Norml Payment) from user,"+userBean.getLogin_name());
                LogTools.getInstance().insertLog(DebugLevel.INFO, "Complete handle Paypal Subscription payment (Norml Payment) from user,"+userBean.getLogin_name());
            
                return true;
            }
            else
            {
                //upgrade subscription
                
                //since user member level will affect Market Amount and SMS Credits
                //So, system should remove previous setting and change to new Member Level setting
                if(DBAccessor.getInstance().removeUserSignalPreferencesAndMarketAnalysis(userBean))
                {
                    DBAccessor.getInstance().updateUserMembershipByUserID(userBean.getId(), oCurrentProduct.getId());
                    
                    //insert new markets by new member level
                    List<MarketTypeAndPeriodBean> marketIDList = new SignupController().getMarketsByProductInfo(oCurrentProduct);
                    DBAccessor.getInstance().storeUserSignalPreferenceAndMarkets(userBean, marketIDList);
                }
                
                commonProcedureForSubscriptionPayment(userBean, oCurrentProduct, txnID, itemName);
                
                InfoTrace.getInstance().printInfo(DebugLevel.INFO, "Complete upgrade Subscription from user,"+userBean.getLogin_name());
                LogTools.getInstance().insertLog(DebugLevel.INFO, "Complete upgrade Subscription from user,"+userBean.getLogin_name());
                
                sendEmail(userBean);
                
                return true;
            }
        }
        else
        {
            InfoTrace.getInstance().printInfo(DebugLevel.WARNING, "Receive Subscription Payment event, but parameters are not right!");
            LogTools.getInstance().insertLog(DebugLevel.WARNING, "Receive Subscription Payment event, but parameters are not right!");
        }
                
        return false;
    }
    
    public void sendEmail(UserBean oUserBean)
    {
        //send upgrade membership email notification
        EmailBean upgradeEmail = EmailNotificationFactory.getNotificationEmail(oUserBean,WebConstants.UPGRADE_MEMBER_SHIP_TEMPLATE);
        
        EmailHandler.getInstance().appendEmailToSend(upgradeEmail);
        
    }
    
    private boolean commonProcedureForSubscriptionPayment(UserBean userBean, ProductBean oCurrentProduct, String txnID, String itemName)
    {
        //enable user
        userBean.setEnable(CommonDefine.USER_ENABLED);
        userBean.setStatus(CommonDefine.PAID_USER);
        
        //update user's expired date
        //Expired date should be = Current date + Product Period
        long expiredDate = System.currentTimeMillis() + oCurrentProduct.getPeriod()*WebConstants.MINI_SECONDS_EACH_DAY;
        userBean.setExpired_date(expiredDate);
        
        //store into DB
        DBAccessor.getInstance().updateUserStatusInfo(userBean);
        
        //update users' sms credits according to product included SMS
        DBAccessor.getInstance().addSMSCreditsToUser(userBean.getId(), oCurrentProduct.getIncludeSMS());
        
        //add new payment recorder
        PaymentHistoryBean paymentBean = new PaymentHistoryBean();
        paymentBean.setUser_id(userBean.getId());
        paymentBean.setPay_item_name(itemName);
        paymentBean.setPay_amount(oCurrentProduct.getPrice());
        paymentBean.setTxn_id(txnID);
        paymentBean.setDate(System.currentTimeMillis());
        paymentBean.setCompleted(WebConstants.PAYMENT_COMPLETED_STATUS);
        
        DBAccessor.getInstance().storePaymentIntoDB(paymentBean); 
        
        return true;
    }
    
    private boolean checkParameters(HashMap<String, String> allParameters)
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
        
        //check price
        double paymentGross = Double.valueOf(allParameters.get("payment_gross")).doubleValue();
        ProductBean oProductBean = DBAccessor.getInstance().getProductByPrice(paymentGross);        
        if(oProductBean == null || oProductBean.getActive() == CommonDefine.INACTIVE_PRODUCT)
        {
            InfoTrace.getInstance().printInfo(DebugLevel.WARNING, "Payment gross Error, can NOT find suitable product by price! Payment gross is , '"+paymentGross+"'.");
            LogTools.getInstance().insertLog(DebugLevel.WARNING, "Payment gross Error, can NOT find suitable product by price! Payment gross is , '"+paymentGross+"'.");
            
            return false;
        }
        
        InfoTrace.getInstance().printInfo(DebugLevel.INFO, "Pass Paypal Subscription payment check!");
        LogTools.getInstance().insertLog(DebugLevel.INFO, "Pass Paypal Subscription payment check!");
        
        return true;
    }
}
