package com.cleartraders.datafeed.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.cleartraders.common.util.config.ResBaseManager;

public class DataFeedResManager extends ResBaseManager
{
    private static DataFeedResManager m_oInstance = null;
    
    private DataFeedResManager() throws UnsupportedEncodingException, FileNotFoundException ,IOException
    {
        super("cleartraders_datafeed.properties");
        
        init();
    }
    
    public synchronized static DataFeedResManager getInstance()
    {
        if(null == m_oInstance)
        {
            try
            {
                m_oInstance = new DataFeedResManager();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                
                return null;
            }
        }
        
        return m_oInstance;
    }
    
    public boolean reLoad() throws UnsupportedEncodingException, FileNotFoundException ,IOException
    {
        return init();
    }

    protected boolean init() throws UnsupportedEncodingException, FileNotFoundException ,IOException
    {
        return true;
    }    
}
