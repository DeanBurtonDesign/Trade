package com.cleartraders.webapp.model.payment;

import com.cleartraders.webapp.WebConstants;

public class IPNHandlerFactory
{
    public static IPNHandler getHandler(String txn_type)
    {
        if(WebConstants.SUBSCR_CANCEL_IPN_EVENT.equals(txn_type))
        {
            return new IPNSubscribeCancelHandler();
        }
        else if(WebConstants.SUBSCR_PAYMENT_IPN_EVENT.equals(txn_type))
        {
            return new IPNSubscriptionPaymentHandler();
        }
        else if(WebConstants.SUBSCR_SIGNUP_IPN_EVENT.equals(txn_type))
        {
            return new IPNSubscribeSignupHandler();
        }
        else if(WebConstants.SUBSCR_MODIFY_IPN_EVENT.equals(txn_type))
        {
            return new IPNSubscribeModifyHandler();
        }
        else if(WebConstants.BUY_NOW_IPN_EVENT.equals(txn_type))
        {
            return new IPNBuyNowPaymentHandler();
        }
        else
        {
            return new IPNSubscribeDefaultHandler();
        }
    }
}
