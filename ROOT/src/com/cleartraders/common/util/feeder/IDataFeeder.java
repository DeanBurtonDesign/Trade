package com.cleartraders.common.util.feeder;

import java.util.List;

import com.cleartraders.common.util.message.IMessager;

public interface IDataFeeder
{
    public void setID(long id);
    public long getID();
    
    public void addMessager(IMessager oMessager);
    public List<IMessager> getMessagerList();
    
    public boolean sendMessage(String message);
    public boolean initMessagers();
    
    public boolean startFeeder();
    public void stopFeeder();
    
    public boolean getStatus();
    public void setStatus(boolean live);
}
