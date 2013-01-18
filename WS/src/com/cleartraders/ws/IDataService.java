package com.cleartraders.ws;

import java.util.ArrayList;

import com.cleartraders.common.entity.IndicatorsSimpleValue;
import com.cleartraders.common.entity.Signal;

public interface IDataService
{
    public ArrayList<String> getMinuteFinanceData(String userName,
            String password, String dataType, String symbol,
            long maxDatapoints, int intervalMin);

    public ArrayList<String> getDayFinanceData(String userName,
            String password, String dataType, String symbol, int days);

    // IQFeed can not support weekly data, we have to calculate it if need
    public ArrayList<String> getWeekFinanceData(String userName,
            String password, String dataType, String symbol, int weeks);

    // IQFeed can not support monthly data, we have to calculate it if need
    public ArrayList<String> getMonthFinanceData(String userName,
            String password, String dataType, String symbol, int months);

    // add buy or sell or scalper signal
    public boolean addAndNotifySignals(String userName, String password,
            Signal oSignal);

    public ArrayList<Signal> getSignals(String userName, String password,
            int signalType, long market_type_id, int signal_period, int strategy_id, int signalsCount);
    
    //get indicator value by id
    public ArrayList<IndicatorsSimpleValue> getIndicatorValue(String userName, String password,int market_type_id, int signal_period,int strategy_id);
}
