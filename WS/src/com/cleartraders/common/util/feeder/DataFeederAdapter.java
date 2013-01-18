package com.cleartraders.common.util.feeder;

import java.util.ArrayList;
import java.util.List;

import com.cleartraders.common.util.message.IMessager;

public abstract class DataFeederAdapter implements IDataFeeder
{
    private List<IMessager> _messagerList = null;
    private long _id=-1;
    
    public abstract boolean createFeedConnection();
    public abstract boolean closeFeedConnection();
    public abstract boolean startFeed();
    public abstract boolean stopFeed();
    public abstract boolean getStatus();
    public abstract void setStatus(boolean live);
    
    public DataFeederAdapter()
    {
        _messagerList = new ArrayList<IMessager>();
    }
    
    public void setID(long id)
    {
        _id = id;
    }
    public long getID()
    {
        return _id;
    }
    
    public void addMessager(IMessager oMessager)
    {
        _messagerList.add(oMessager);
    }
    
    public final List<IMessager> getMessagerList()
    {
        return _messagerList;
    }
    
    public boolean sendMessage(String message)
    {
        if(null != _messagerList)
        {
            for(int i=0; i<_messagerList.size(); i++)
            {
                IMessager messager = _messagerList.get(i);
                
                boolean sentResult = messager.sendTextMessage(message);
                
                try 
                {
                    while(!sentResult)
                    {
                        if(null != messager)
                        {
                            //don't stop init if it was failed
                            while(messager.init() != true)
                            {
                                Thread.sleep(500);
                            }
                            
                            //if sent result is wrong, then, don't stop trying
                            sentResult = messager.sendTextMessage(message);
                        }
                        else
                        {
                            break;
                        }                                
                    }
                }
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
            
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean initMessagers()
    {
        if(null != _messagerList)
        {
            for(int i=0; i<_messagerList.size(); i++)
            {
                IMessager messager = _messagerList.get(i);
                messager.init();
            }
            
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean startFeeder()
    {        
        if(null != _messagerList)
        {
            if(createFeedConnection())
            {
                startFeed();
            }
        }
        
        return false;
    }

    public void stopFeeder()
    {
        if(null != _messagerList)
        {
            for(int i=0; i<_messagerList.size(); i++)
            {
                IMessager messager = _messagerList.get(i);
                messager.destory();
            }
        }
        
        stopFeed();
        
        closeFeedConnection();
    }

}
