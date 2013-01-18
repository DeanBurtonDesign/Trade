package com.cleartraders.common.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.AgeBean;
import com.cleartraders.common.entity.AgeComparator;
import com.cleartraders.common.entity.CountryBean;
import com.cleartraders.common.entity.CountryBeanComparator;
import com.cleartraders.common.entity.HLOCDataBean;
import com.cleartraders.common.entity.IndicatorBean;
import com.cleartraders.common.entity.MarketPeriodBean;
import com.cleartraders.common.entity.MarketTypeBean;
import com.cleartraders.common.entity.MarketTypeBeanComparator;
import com.cleartraders.common.entity.ProductBean;
import com.cleartraders.common.entity.ProductBeanComparator;
import com.cleartraders.common.entity.SmsPackageBean;
import com.cleartraders.common.entity.TimeFrameComparator;
import com.cleartraders.common.entity.TimeZoneBean;
import com.cleartraders.common.entity.TimeZoneComparator;
import com.cleartraders.common.entity.TradingExperienceBean;
import com.cleartraders.common.entity.TradingExperienceComparator;


/**
 * This class is used to cache some type data, such as Market Type, Period Type etc.
 * The purpose is to prevent accessing DB high frequency
 * @author Peter
 *
 */
public class DataCache
{
    private static DataCache m_oInstance = null; 
    
    //All market type data, type id is the Key
    //ref market_type_table for details
    private HashMap<Long,MarketTypeBean> m_oAllMarketTypeData = null;
    
    //All market period Data, type id is the Key
    //ref market_period_table for details
    private HashMap<Long,MarketPeriodBean> m_oAllMarketPeriodData = null;
    
    //All country information, id is the key
    //ref country_table for details
    private HashMap<Long,CountryBean> m_oAllCountryData = null;
    
    //All Time Zone data, id is the Key
    //ref time_zone_table for details
    private HashMap<Long,TimeZoneBean> m_oAllTimeZoneData = null;
    
    private HashMap<Long,TradingExperienceBean> m_oAllTradingExperience = null;
    
    private HashMap<Long,ProductBean> m_oAllProductData = null;
    
    private HashMap<Long,AgeBean> m_oAllAges = null;
    
    //Note: There are only basic information of Indicator
    private Map<Long, IndicatorBean> m_oAllIndicator = null;
        
    //All market real-time cache data  Map<MarketID+":"+PeriodID, List<HLOCData>>
    //Here system should create indicators for all market and all period, So, the Key of this Map should be MarketID:PeriodID
    private Map<String,List<HLOCDataBean>> m_oAllMarketRealTimeCacheData = new HashMap<String,List<HLOCDataBean>>();
    
    private DataCache()
    {        
        initDataCache();
    }
    
    public synchronized static DataCache getInstance()
    {
        if(null == m_oInstance)
        {
            m_oInstance = new DataCache();
        }
        
        return m_oInstance;
    }
    
    public void destroy()
    {
        if(null != m_oAllMarketTypeData)
        {
            m_oAllMarketTypeData.clear();
        }
        
        if(null != m_oAllMarketPeriodData)
        {
            m_oAllMarketPeriodData.clear();
        }
        
        if(null != m_oAllCountryData)
        {
            m_oAllCountryData.clear();
        }
        
        if(null != m_oAllTimeZoneData)
        {
            m_oAllTimeZoneData.clear();
        }
        
        if(null != m_oAllTradingExperience)
        {
            m_oAllTradingExperience.clear();
        }
        
        if(null != m_oAllAges)
        {
            m_oAllAges.clear();
        }
        
        if(null != m_oAllProductData)
        {
            m_oAllProductData.clear();
        }
        
        if(null != m_oAllMarketRealTimeCacheData)
        {
            m_oAllMarketRealTimeCacheData.clear();
        } 
        
        if(null != m_oAllIndicator)
        {
            m_oAllIndicator.clear();
        }
    }
    
    public synchronized boolean reloadDataCache()
    {
        destroy();
        
        return initDataCache();
    }
    
    private boolean initDataCache()
    {
        boolean result = false;
        
        do
        {
            m_oAllMarketTypeData = new CommonDBAccessor().getAllMarketTypeData();
            if(null == m_oAllMarketTypeData)
            {
                break;
            }
            
            m_oAllMarketPeriodData = new CommonDBAccessor().getAllMarketPeriodData();
            if(null == m_oAllMarketPeriodData)
            {
                break;
            }
            
            m_oAllCountryData = new CommonDBAccessor().getAllCountryData();
            if(null == m_oAllCountryData)
            {
                break;
            }
            
            m_oAllTimeZoneData = new CommonDBAccessor().getAllTimeZoneData();
            if(null == m_oAllTimeZoneData)
            {
                break;
            }
            
            m_oAllTradingExperience = new CommonDBAccessor().getAllTradingExperience();
            if(null == m_oAllTradingExperience)
            {
                break;
            }
            
            m_oAllAges = new CommonDBAccessor().getAllAges();
            if(null == m_oAllAges)
            {
                break;
            }
            
            m_oAllProductData = new CommonDBAccessor().getAllProduct();
            if(null == m_oAllProductData)
            {
                break;
            }
            
            m_oAllIndicator = new CommonDBAccessor().getAllIndicators();
            if(null == m_oAllProductData)
            {
                break;
            }
            
            result = true;
            
        } while(false);
        
        
        return result;
    }
    
    public IndicatorBean getIndicatorByNameAndPeriod(String indicatorName, int period)
    {
        Iterator<IndicatorBean> oIt = m_oAllIndicator.values().iterator();
        while(oIt.hasNext())
        {
            IndicatorBean oTempBean = oIt.next();
            
            if(oTempBean.getName().equalsIgnoreCase(indicatorName) && oTempBean.getPeriod() == period)
                return new IndicatorBean(oTempBean);
        }
        
        return new IndicatorBean();
    }
    
    public List<IndicatorBean> getAllIndicators()
    {
        List<IndicatorBean> oList = new ArrayList<IndicatorBean>();
        
        Iterator<IndicatorBean> oIt = m_oAllIndicator.values().iterator();
        while(oIt.hasNext())
        {
            oList.add(new IndicatorBean(oIt.next()));
        }
                
        return oList;
    }
    
    public synchronized final List<HLOCDataBean> getHLOCData(long marketID, long periodID)
    {
        String marketIDPeriodID = marketID+":"+periodID;
        
        if(m_oAllMarketRealTimeCacheData.get(marketIDPeriodID) == null)
        {
            return new ArrayList<HLOCDataBean>();
        }
        else
        {
            return m_oAllMarketRealTimeCacheData.get(marketIDPeriodID);
        }
    }
    
    public synchronized HLOCDataBean getLatestHLOCData(String marketIDPeriodID)
    {
        if(m_oAllMarketRealTimeCacheData.get(marketIDPeriodID) == null)
        {
            return new HLOCDataBean();
        }
        else
        {
            List<HLOCDataBean> oTickerCellList = m_oAllMarketRealTimeCacheData.get(marketIDPeriodID);
            return oTickerCellList.get(oTickerCellList.size()-1);
        }
    }
    
    public synchronized void updateLatestHLOCData(String marketIDPeriodID, HLOCDataBean oNewTikcerCell)
    {
        if(m_oAllMarketRealTimeCacheData.get(marketIDPeriodID) == null)
        {
            List<HLOCDataBean> oTickerCellList = new ArrayList<HLOCDataBean>();
            oTickerCellList.add(oNewTikcerCell);
            
            m_oAllMarketRealTimeCacheData.put(marketIDPeriodID, oTickerCellList);
        }
        else
        {
            List<HLOCDataBean> oTickerCellList = m_oAllMarketRealTimeCacheData.get(marketIDPeriodID);
            oTickerCellList.remove(oTickerCellList.size()-1);
            oTickerCellList.add(oNewTikcerCell);            
        }
    }
    
    public synchronized void addHLOCData(String marketIDPeriodID, HLOCDataBean oNewTikcerCell)
    {
        if(m_oAllMarketRealTimeCacheData.get(marketIDPeriodID) == null)
        {
            List<HLOCDataBean> oTickerCellList = new ArrayList<HLOCDataBean>();
            oTickerCellList.add(oNewTikcerCell);
            
            m_oAllMarketRealTimeCacheData.put(marketIDPeriodID, oTickerCellList);            
        }
        else
        {
            List<HLOCDataBean> oTickerCellList = m_oAllMarketRealTimeCacheData.get(marketIDPeriodID);
            oTickerCellList.add(oNewTikcerCell);
            
            //to avoid out of memory, if the data size more max cache bars, then, remove first one 
            if(oTickerCellList.size() > CommonDefine.MAX_CATCHE_BARS)
            {
                oTickerCellList.remove(0);
            }
        }
    }
    
    public MarketTypeBean getMarketTypeByDisplayName(String displayName)
    {
        Iterator<MarketTypeBean> oIt = m_oAllMarketTypeData.values().iterator();
        
        while(oIt.hasNext())
        {
            MarketTypeBean marketTypeBean = new MarketTypeBean(oIt.next());
            
            if(marketTypeBean.getDisplay_name().equals(displayName))
            {
                return marketTypeBean;
            }
        }
        
        return null;
    }
    
    public MarketTypeBean getMarketTypeByID(Long id)
    {
        return m_oAllMarketTypeData.get(id);
    }
    
    public MarketPeriodBean getMarketPeriodByID(Long id)
    {
        if(null == m_oAllMarketPeriodData.get(id))
        {
            return new MarketPeriodBean();
        }
        
        return m_oAllMarketPeriodData.get(id);
    }
    
    public List<MarketPeriodBean> getAllMarketPeriod()
    {
        List<MarketPeriodBean> oList = new ArrayList<MarketPeriodBean>();
        
        Iterator<MarketPeriodBean> oIt = m_oAllMarketPeriodData.values().iterator();
        while(oIt.hasNext())
        {
            oList.add(new MarketPeriodBean(oIt.next()));
        }
        
        Comparator<MarketPeriodBean> comp = new TimeFrameComparator();
        Collections.sort(oList,comp);
        
        return oList;
    }
    
    public CountryBean getCountryByID(Long id)
    {
        return m_oAllCountryData.get(id);
    }
    
    public TimeZoneBean getTimeZoneByID(Long id)
    {
        return m_oAllTimeZoneData.get(id);
    }
        
    public List<ProductBean> getAllProduct()
    {
        //since product data may be changed often, so, it should query DB each time
        m_oAllProductData.clear();
        m_oAllProductData = new CommonDBAccessor().getAllProduct();
        
        ArrayList<ProductBean> allProducts = new ArrayList<ProductBean>();
        
        Iterator<ProductBean> oIt = m_oAllProductData.values().iterator();
        while(oIt.hasNext())
        {
            allProducts.add(oIt.next());
        }
        
        Comparator<ProductBean> comp = new ProductBeanComparator();
        Collections.sort(allProducts,comp);
        
        return allProducts;
    }
    
    public List<SmsPackageBean> getAllSmsPackages()
    {
        return new CommonDBAccessor().getAllSMSPackages();
    }
    
    public List<CountryBean> getAllCountry()
    {
        ArrayList<CountryBean> oList = new ArrayList<CountryBean>();
        
        Iterator<CountryBean> oIt = m_oAllCountryData.values().iterator();
        while(oIt.hasNext())
        {
            oList.add(new CountryBean(oIt.next()));
        }
        
        Comparator<CountryBean> comp = new CountryBeanComparator();
        Collections.sort(oList,comp);
        
        return oList;
    }
    
    public List<MarketTypeBean> getAllMarketType()
    {
        ArrayList<MarketTypeBean> oList = new ArrayList<MarketTypeBean>();
        
        Iterator<MarketTypeBean> oIt = m_oAllMarketTypeData.values().iterator();
        while(oIt.hasNext())
        {
            oList.add(new MarketTypeBean(oIt.next()));
        }
        
        Comparator<MarketTypeBean> comp = new MarketTypeBeanComparator();
        Collections.sort(oList,comp);
        
        return oList;
    }
    
    public List<TimeZoneBean> getAllTimeZone()
    {
        ArrayList<TimeZoneBean> oList = new ArrayList<TimeZoneBean>();
        
        Iterator<TimeZoneBean> oIt = m_oAllTimeZoneData.values().iterator();
        while(oIt.hasNext())
        {
            oList.add(new TimeZoneBean(oIt.next()));
        }
        
        Comparator<TimeZoneBean> comp = new TimeZoneComparator();
        Collections.sort(oList,comp);
        
        return oList;
    }
    
    public List<TradingExperienceBean> getAllTradingExperience()
    {
        ArrayList<TradingExperienceBean> allTradingExperience = new ArrayList<TradingExperienceBean>();
        
        Iterator<TradingExperienceBean> oIt = m_oAllTradingExperience.values().iterator();
        while(oIt.hasNext())
        {
            allTradingExperience.add(oIt.next());
        }
        
        Comparator<TradingExperienceBean> comp = new TradingExperienceComparator();
        Collections.sort(allTradingExperience,comp);
        
        return allTradingExperience;
    }
    
    public List<AgeBean> getAllAges()
    {
        ArrayList<AgeBean> allAges = new ArrayList<AgeBean>();
        
        Iterator<AgeBean> oIt = m_oAllAges.values().iterator();
        while(oIt.hasNext())
        {
            allAges.add(oIt.next());
        }
        
        Comparator<AgeBean> comp = new AgeComparator();
        Collections.sort(allAges,comp);
        
        return allAges;
    }
}