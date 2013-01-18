package com.cleartraders.common.define;

public final class CommonDefine
{
    public static boolean IS_UNIT = false;
    public static int ERROR_RESULT = -1;
    public static int SUCCESS_RESULT = 0;
    
    public static int DEFAULT_BROKER_FEE = 3;
    
    //Signal Direction
    public static int BUY_LONG=0;
    public static int SELL_SHORT=1;
    
    //Signal type
    public static int BUY_SINGAL=0;
    public static int SELL_SINGAL=1;
    public static int SCALPER_SINGAL=2;
    public static int SCALPER_DISPLAY_SINGAL=3;
    public static int LONG_SIGNAL=4;
    public static int SHORT_SIGNAL=5;
    public static int CLOSE_SIGNAL=6;
    
    public static int LINE_INDICATOR=1;
    public static int PLOT_INDICATOR=2;
    
    /*
        insert into market_period_table values(1,1,'Min',1); # 1 minute
        insert into market_period_table values(2,1,'Min',5); # 5 minute
        insert into market_period_table values(3,1,'Min',10); # 10 minute
        insert into market_period_table values(4,1,'Min',30); # 30 minute
        insert into market_period_table values(5,2,'Hour',1); # 1 hour
        insert into market_period_table values(6,3,'Day',1); # 1 day
        insert into market_period_table values(7,4,'Week',1); # 1 week
        insert into market_period_table values(8,5,'Month',1); # 1 month
        insert into market_period_table values(9,6,'Year',1); # 1 year
     */
    public final static int ONE_MINUTE=1;
    public final static int FIVE_MINUTE=2;
    public final static int TEN_MINUTE=3;
    public final static int THIRTY_MINUTE=4;
    public final static int ONE_HOUR=5;
    public final static int ONE_DAY=6;
    public final static int ONE_WEEK=7;
    public final static int ONE_MONTH=8;
    public final static int ONE_YEAR=9;
    
    public final static int MINUTE_PERIOD_TYPE=1;
    public final static int HOUR_PERIOD_TYPE=2;
    public final static int DAILY_PERIOD_TYPE=3;
    
    
    //user locked type
    public static final int USER_NOT_LOCKED = 0;
    public static final int USER_LOCKED = 1;    
    
    public static final int PAID_PRODUCT = 0;
    public static final int FREE_TRIAL_PRODUCT = 1;
    
    public static final int ACTIVE_PREFERENCE = 1;
    public static final int INACTIVE_PREFERENCE = 2;
    
    public static final int INACTIVE_PRODUCT = 0;
    public static final int ACTIVE_PRODUCT = 1;
    
    public static final int INACTIVE_STRATEGY = 0;
    public static final int ACTIVE_STRATEGY = 1;
    
    public static final int ALL_MEMBER_TYPE=-1;
    public static final int STANDARD_MEMBER_TYPE=1;
    public static final int SALES_MEMBER_TYPE=2;
    public static final int SUPPORT_MEMBER_TYPE=3;
    public static final int ADMIN_MEMBER_TYPE=4;
    
    //quote type
    public static String CLOSE_PRICE="close";
    public static String OPEN_PRICE="open";
    public static String HIGH_PRICE="high";
    public static String LOW_PRICE="low";
    
    //user enabled
    public static final int USER_ENABLED = 1;  //can NOT login
    public static final int USER_NOT_ENABLED = 0;  //can NOT login
    
    //user status
    public static final int UNREGISTERED_USER = 0;//Unregistered : user who is not enabled (didn't confirm email or didn't pay for paid member)
    public static final int REGISTERED_USER = 1;  //Registered : Signed up but not paid 
    public static final int UPAIED_USER = 2;      //Unpaid : Users have paid before, but not paid now (this is similar to Registered )
    public static final int PAID_USER = 3;        //Paid : Paid from Paypal
    public static final int CANCELLED_USER = 4;   //Cancelled : Paypal Cancelled
    public static final int FREE_TRIAL_END_USER = 5;   //Free Trial Ended : A user has registered for the free trial and it ends
    
    //Datacache Max HLOC data
    public static final int MAX_CATCHE_BARS = 600;
}
