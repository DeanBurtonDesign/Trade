package com.cleartraders.common.util.message;

public interface IMessager
{
    public boolean init();
    public boolean destory();
    public boolean sendTextMessage(String textMsg);
}
