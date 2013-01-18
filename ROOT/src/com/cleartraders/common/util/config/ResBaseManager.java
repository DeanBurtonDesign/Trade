package com.cleartraders.common.util.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import com.cleartraders.common.util.tools.CommonTools;


/**
 * 
 * @author Peter
 *
 */
public abstract class ResBaseManager
{
    private String systemPath = null;
    
    // config file
    private Properties m_oConif;
    // input stream of config file
    private FileInputStream fin;
    
    public Properties getConifPropertyHandler()
    {
        return m_oConif;
    }

    public ResBaseManager(String configFilePath) throws UnsupportedEncodingException, FileNotFoundException ,IOException
    {
        m_oConif = new Properties();
        
        
        systemPath = CommonTools.getSysDir()+ "config" + File.separator;
        fin = new FileInputStream(systemPath + configFilePath);
        
        if (fin != null)
        {
            m_oConif.load(fin);
            fin.close();
        }
    }
    
    protected abstract boolean init() throws UnsupportedEncodingException, FileNotFoundException ,IOException;    
    public abstract boolean reLoad() throws UnsupportedEncodingException, FileNotFoundException ,IOException;
}
