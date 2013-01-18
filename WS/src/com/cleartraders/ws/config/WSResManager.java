package com.cleartraders.ws.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.cleartraders.common.util.config.ResBaseManager;

public class WSResManager extends ResBaseManager
{
    private static WSResManager m_oInstance=null;
    
    private WSResManager() throws UnsupportedEncodingException, FileNotFoundException ,IOException
    {
        super("cleartraders_ws.properties");
        
        init();
    }
    
    public synchronized static WSResManager getInstance()
    {
        if(null == m_oInstance)
        {
            try
            {
                m_oInstance = new WSResManager();
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
