package com.cleartraders.webapp.model.bean;

public class AutoTradeInfoBean
{
    private boolean started=false; //start or close

    public boolean isStarted()
    {
        return started;
    }

    public void setStarted(boolean started)
    {
        this.started = started;
    }   
}
