package com.cleartraders.webadmin;

public class AdminConstants
{
    public static final long MINI_SECONDS_EACH_DAY = 24*60*60*1000;
    
    public static final String FORWARD_SUCCESS="successful";
    public static final String FORWARD_FAILED="failed";
    
    public static final String MEMBER_LIST="member_list";
    public static final String MEMBER_SORT_TYPE="member_sort_type";
    public static final String PRODUCT_PLAN_LIST="product_plan_list";
    public static final String SMS_PACKAGE_LIST="sms_package_list";
    public static final String STRATEGY_LIST="strategy_list";
    public static final String MARKET_STRATEGY_MAP="market_strategy_map";
    
    public static final String USER_KEY="admininfo";
            
    public static final int ADD_OPERATION_TYPE = 1;
    public static final int UPDATE_OPERATION_TYPE = 2;
        
    public static final int SORT_MEMBER_A_TO_Z=1;
    public static final int SORT_MEMBER_Z_TO_A=2;
    public static final int SORT_MEMBER_SIGNUP_NEWEST=3;
    public static final int SORT_MEMBER_SIGNUP_OLDEST=4;
    public static final int SORT_MEMBER_PRODUCT_PLAN=5;
    public static final int SORT_MEMBER_CANCELLED=6;
    public static final int SORT_MEMBER_SMS=7;
    public static final int SORT_REGISTERED_MEMBER=8;
    public static final int SORT_UNREGISTERED_MEMBER=9;
}
