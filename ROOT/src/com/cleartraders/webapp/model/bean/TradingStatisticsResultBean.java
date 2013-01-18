package com.cleartraders.webapp.model.bean;

import java.text.DecimalFormat;

public class TradingStatisticsResultBean
{
    private double trade_success = 0;    
    private double total_profit_or_loss_points = 0;
    private double average_trade_points = 0;
    private long total_trades = 0;
    private long winning_trades = 0;
    private double average_winning_trade_points = 0;
    private long losing_trades = 0;
    private double average_losing_trade_points = 0;
    private DecimalFormat decimalFormat = null;
    
    private long total_signals = 0;
    private long long_signals=0;
    private long short_signals=0;
    private long close_signals=0;
    
    private double total_broker_points = 0.0;
    private double final_profit_or_loss_points = 0.0;
    
    public double getFinal_profit_or_loss_points()
    {
        return final_profit_or_loss_points;
    }

    public void setFinal_profit_or_loss_points(double final_profit_or_loss_points)
    {
        this.final_profit_or_loss_points = final_profit_or_loss_points;
    }

    public double getTotal_broker_points()
    {
        return total_broker_points;
    }

    public void setTotal_broker_points(double total_broker_points)
    {
        this.total_broker_points = total_broker_points;
    }

    public long getTotal_signals()
    {
        return total_signals;
    }

    public void setTotal_signals(long total_signals)
    {
        this.total_signals = total_signals;
    }

    public String getStatisticsResult()
    {
        if(null == decimalFormat)
        {
            return "";
        }
        
        DecimalFormat decimalFormatNoPoints = new DecimalFormat();
        decimalFormatNoPoints.setMaximumFractionDigits(0);
        decimalFormatNoPoints.setMinimumFractionDigits(0);
        
        String statisticsResult = "";
        
        //format is ' 1) Trade Success; 2) Total Profit / Loss (Points); 3) Average Trade (Points); 
        // 4) Total Trades; 5) Winning Trades; 6) Average Winning Trade (Points); 7) Losing Trades; 8) Average Losing Trade (Points);
        // 9) total_signals; 10) total_broker_points; 11) final_profit_or_loss_points; 12)long signals; 13)short signals; 14)close signals
        trade_success = trade_success * 100;
        statisticsResult+= decimalFormat.format(trade_success)+";";
        
        if(total_profit_or_loss_points>0)
        {
            statisticsResult+= decimalFormatNoPoints.format(total_profit_or_loss_points)+";";
        }
        else
        {
            statisticsResult+= decimalFormatNoPoints.format(total_profit_or_loss_points)+";";
        }
        
        if(average_trade_points > 0)
        {
            statisticsResult+= decimalFormat.format(average_trade_points)+";";
        }
        else
        {
            statisticsResult+= decimalFormat.format(average_trade_points)+";";
        }
        
        statisticsResult+= total_trades +";";
        statisticsResult+= winning_trades+";";
        
        if(average_winning_trade_points>0)
        {
            statisticsResult+= decimalFormat.format(average_winning_trade_points)+";";
        }
        else
        {
            statisticsResult+= decimalFormat.format(average_winning_trade_points)+";";
        }
        
        statisticsResult+= losing_trades+";";
        
        if(average_losing_trade_points>0)
        {
            statisticsResult+= decimalFormat.format(average_losing_trade_points)+";";
        }
        else
        {
            statisticsResult+= decimalFormat.format(average_losing_trade_points)+";";
        }
        
        statisticsResult+= total_signals+";";
        
        statisticsResult+= decimalFormatNoPoints.format(total_broker_points)+";";
        
        statisticsResult+= decimalFormatNoPoints.format(final_profit_or_loss_points)+";";
        
        statisticsResult+= long_signals+";";
        
        statisticsResult+= short_signals+";";
        
        statisticsResult+= close_signals;
                
        return statisticsResult;
    }
    
    public DecimalFormat getDecimalFormat()
    {
        return decimalFormat;
    }
    public void setDecimalFormat(DecimalFormat decimalFormat)
    {
        this.decimalFormat = decimalFormat;
    }
    public double getAverage_losing_trade_points()
    {
        return average_losing_trade_points;
    }
    public void setAverage_losing_trade_points(double average_losing_trade_points)
    {
        this.average_losing_trade_points = average_losing_trade_points;
    }
    public double getAverage_trade_points()
    {
        return average_trade_points;
    }
    public void setAverage_trade_points(double average_trade_points)
    {
        this.average_trade_points = average_trade_points;
    }
    public double getAverage_winning_trade_points()
    {
        return average_winning_trade_points;
    }
    public void setAverage_winning_trade_points(double average_winning_trade_points)
    {
        this.average_winning_trade_points = average_winning_trade_points;
    }
    public long getLosing_trades()
    {
        return losing_trades;
    }
    public void setLosing_trades(long losing_trades)
    {
        this.losing_trades = losing_trades;
    }
    public double getTotal_profit_or_loss_points()
    {
        return total_profit_or_loss_points;
    }
    public void setTotal_profit_or_loss_points(double total_profit_or_loss_points)
    {
        this.total_profit_or_loss_points = total_profit_or_loss_points;
    }
    public long getTotal_trades()
    {
        return total_trades;
    }
    public void setTotal_trades(long total_trades)
    {
        this.total_trades = total_trades;
    }
    public double getTrade_success()
    {
        return trade_success;
    }
    public void setTrade_success(double trade_success)
    {
        this.trade_success = trade_success;
    }
    public long getWinning_trades()
    {
        return winning_trades;
    }
    public void setWinning_trades(long winning_trades)
    {
        this.winning_trades = winning_trades;
    }

    public long getClose_signals()
    {
        return close_signals;
    }

    public void setClose_signals(long close_signals)
    {
        this.close_signals = close_signals;
    }

    public long getLong_signals()
    {
        return long_signals;
    }

    public void setLong_signals(long long_signals)
    {
        this.long_signals = long_signals;
    }

    public long getShort_signals()
    {
        return short_signals;
    }

    public void setShort_signals(long short_signals)
    {
        this.short_signals = short_signals;
    }
}
