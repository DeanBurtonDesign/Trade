package com.cleartraders.webapp;

public final class WebConstants
{
    public static final String FORWARD_SUCCESS="successful";
    public static final String FORWARD_FAILED="failed";
    
    public static final String FORWARD_CUMULATIVE="cumulative";
    public static final String FORWARD_DASHBOARD="dashboard";
    public static final String FORWARD_PROFIT_LOSS="profitloss";
    public static final String FORWARD_STATISTICS="statistics";
    public static final String FORWARD_ADD_ANALYSIS="add_analysis";
    
    public static final String USER_KEY="userinfo";
    public static final String SECURITY_CODE="securitycode";
    public static final String ALL_MARKET_TYPE="all_market_type";
    public static final String ALL_ACTIVE_SIGNAL="all_active_signals";
    public static final String PASTED_SIGNAL="pasted_signals";
    public static final String ALL_ACTIVE_SIGNAL_STRING="all_active_signals_string";
    public static final String ALL_MY_ACTIVE_SIGNAL="all_my_active_signals";
    public static final String ALL_MY_ACTIVE_SIGNAL_MAP="all_my_active_signals_map";
    public static final String MY_MEMBER_SHIP="my_member_ship";    
    public static final String ALL_MEMBER_SHIP="all_member_ship";
    public static final String MY_SMS_CREDITS="my_sms_credits";
    public static final String ALL_SMS_PACKAGE="all_sms_packages";
    public static final String MY_ANALYSIS_CONDITION="my_analysis_condition";
    public static final String ALL_MARTKET_PERIOD="all_market_period";
    public static final String MARKET_STRATEGY_MAP="market_strategy_map";
    public static final String STRATEGY_TIMEFRAME_MAP="strategy_timeframe_map";
    public static final String TOTAL_MARKETS_OF_MEMBERSHIP="total_markets_of_membership";
    public static final String ACTIVED_MARKET_AMOUNT="actived_market_amount";
    public static final String TOP_TRADES="top_trades";
        
    public static final String STRATEGY_LIST="strategy_list";
    
    public static final String START_NUMBER="start";
    public static final String RANGE_NUMBER="range";
    public static final String TOTAL_NUMBER="total";
    
    public static final int REMOVE_SPECIFIC_ANALYSIS_CONDITIOn=2;

    public static final int UPDATE_SPECIFIC_SIGNAL_SETTING=1;
    public static final int REMOVE_SPECIFIC_SIGNAL_SETTING=2;
    public static final int SAVE_ALL_SPECIFIC_SIGNAL_SETTING=3;
    
    public static final String USER_TRADING_TIME="user_trading_time";
    public static final String USER_TRADING_TIME_ZONE="user_trading_time_zone";
    public static final String ALL_TIME_ZONE="all_time_zone";
    public static final String ALL_COUNTRY_DATA="all_country_data";
    public static final String USER_SIGNAL_NOTIFICATION="user_signal_notification";
    public static final String USER_QUICK_LINKS="user_quick_links";
    public static final String USER_ACTIVE_MARKETS="user_active_markets";
    public static final String USER_ACTIVE_SIGNAL_PREFERENCE="user_active_signal_preference";
    public static final String ALL_CURRENT_MARKETS="all_current_markets";
    public static final String USER_BROKER_INFO="user_broker_info";
    public static final String USER_AUTO_TRADE_INFO="user_auto_trade_info";
    public static final String ALL_STRATEGY_INFO="all_strategy_info";
    
    public static final String SUBSCR_PAYMENT_IPN_EVENT = "subscr_payment";
    public static final String SUBSCR_SIGNUP_IPN_EVENT = "subscr_signup";
    public static final String SUBSCR_CANCEL_IPN_EVENT = "subscr_cancel";
    public static final String SUBSCR_MODIFY_IPN_EVENT = "subscr_modify";
    public static final String BUY_NOW_IPN_EVENT = "web_accept";
    
    //1: news  2:broker
    public static final int NEWS_QUICK_LINK=1;
    public static final int BROKER_QUICK_LINK=2;
    
    public static final String CURRENT_MARKET_ID="current_market_id";
    
    public static final int ENABLE=1;
    public static final int DISABLE=2;
    
    //signal type
    //0:BUY 1:SELL 2:Scalper
    public static final int BUY_SIGNAL_TYPE=0;
    public static final int SELL_SIGNAL_TYPE=1;
    public static final int SCALPER_SIGNAL_TYPE=2;
    
    //signal sort type
    public static final String SIGNAL_SORT_TYPE="signal_sort_type";
    
    //image chart type
    public static final int CUMULATIVE_PROFIT_CHART=1;
    public static final int LOST_PROFIT_CHART=2;
    
    public static final int DAILY_PROFIT_CHART=1;
    public static final int TRADE_GRAPH_CHART=2;
    
    public static final int OPEN_AUTO_TRADE = 0;
    public static final int CLOSE_AUTO_TRADE = 1;
    public static final int CLOSE_ALL_OPEN_POSITION = 2;
    
    public static final long AUTO_TRADE_USER_ID=1;
    
    //gender type
    public static final int MALE = 0;
    public static final int FEMALE = 1;
                        
    public static final long MINI_SECONDS_EACH_DAY = 24*60*60*1000;
    //public static final long MINI_SECONDS_EACH_DAY = 2000;
    
    public static final int FREE_TRIAL_PRODUCT_ID = 1;
    
    public static final int STANDARD_MEMBER_TYPE=1;
    public static final int SALES_MEMBER_TYPE=2;
    public static final int SUPPORT_MEMBER_TYPE=3;
    public static final int ADMIN_MEMBER_TYPE=4;
    
    //email notification template type
    public static final int THREE_DAYS_BEFORE_EXPIRED_TEMPLATE = 0;
    public static final int FREE_TRIAL_EXPIRED_TEMPLATE = 1; 
    public static final int THREE_DAYS_AFTER_EXPIRED_TEMPLATE = 2;
    public static final int SEVEN_DAYS_AFTER_EXPRED_TEMPLATE = 3;
    public static final int FREE_TRIAL_USER_SIGNUP_TEMPLATE = 4;
    public static final int PAID_USER_SIGNUP_TEMPLATE = 5;
    public static final int FREE_TRIAL_USER_CONFIRM_TEMPLATE = 6;
    public static final int FIND_PASSWORD_CONFIRM_TEMPLATE = 7;
    public static final int BUY_SMS_TEMPLATE = 8;
    public static final int CANCEL_MEMBER_SHIP_TEMPLATE = 9;
    public static final int UPGRADE_MEMBER_SHIP_TEMPLATE = 10;
    public static final int SMS_IS_LOW_TEMPLATE = 11;
    public static final int SMS_EMPTY_TEMPLATE = 12;
    
    //payment history 
    public static final int PAYMENT_NOT_COMPLETED_STATUS = 0;
    public static final int PAYMENT_COMPLETED_STATUS = 1;
    
    //Enquiry us message type
    public static final int SALE_Enquiry_TYPE = 1;
    public static final int GENERAL_Enquiry_TYPE = 2;
    public static final int TECHNICAL_Enquiry_TYPE = 3;
    
    public static final String REQUEST_RESULT = "";
    
    
    //Paypal payment type
    public static final int SMS_PACKAGE_50 = 1;
    public static final int SMS_PACKAGE_250 = 2;
    public static final int SMS_PACKAGE_500 = 3;
    public static final int SMS_PACKAGE_1000 = 4;
    public static final int SMS_PACKAGE_5000 = 5;
    public static final int SPECIALIST_MEMBER  = 6;
    public static final int EXPERT_MEMBER = 7;
    public static final int MASTER_MEMBER = 8;
    
    //paypal payment forward name
    public static final String SMS_PACKAGE_50_NAME = "50sms";
    public static final String SMS_PACKAGE_250_NAME = "250sms";
    public static final String SMS_PACKAGE_500_NAME = "500sms";
    public static final String SMS_PACKAGE_1000_NAME = "1000sms";
    public static final String SMS_PACKAGE_5000_NAME = "5000sms";
    public static final String SPECIALIST_MEMBER_NAME  = "specialist";
    public static final String EXPERT_MEMBER_NAME = "expert";
    public static final String MASTER_MEMBER_NAME = "master";
    
    //member id type
    public static final int FREE_TRIAL_MEMBER_ID = 1;
    public static final int SPECIALIST_MEMBER_ID = 2;
    public static final int EXPERT_MEMBER_ID = 3;
    public static final int MASTER_MEMBER_ID = 4;
    public static final int PAID_MEMBER_ID = 5;
}
