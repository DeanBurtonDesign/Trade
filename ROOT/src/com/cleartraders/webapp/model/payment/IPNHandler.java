package com.cleartraders.webapp.model.payment;

import java.util.HashMap;

import com.cleartraders.common.entity.UserBean;

public interface IPNHandler
{
    boolean handle(HashMap<String,String> allParameters);
    void sendEmail(UserBean oUserBean);
}
