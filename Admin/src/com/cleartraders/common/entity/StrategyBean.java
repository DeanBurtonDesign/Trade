package com.cleartraders.common.entity;

/**
 * 
 
    create table strategy_table
    (
          id                int unsigned not null primary key,
          system_name       varchar(100) not null,
          common_name       varchar(100) not null,
          description       varchar(500) not null,
          link_url          varchar(100) not null,
          active            int unsigned not null default 0 #0: inactive  1: active 
    );

 * @author Administrator
 *
 */
public class StrategyBean
{
    private long id=0;
    private int active=0;
    private String system_name="";
    private String common_name="";
    private String description="";
    private String link_url="";
    
    private String related_markets="";
    private String related_timeframes="";
    private String related_product_plans="";
    
    public String getDescriptionString()
    {
        String descriptionString="";
        
        descriptionString += "id="+this.id;
        descriptionString += ";";
        
        descriptionString += "system_name="+this.system_name;
        descriptionString += ";";
        
        descriptionString += "common_name="+this.common_name;
        descriptionString += ";";
        
        descriptionString += "description="+this.description;
        descriptionString += ";";
        
        descriptionString += "link_url="+this.link_url;
        descriptionString += ";";
        
        descriptionString += "related_markets="+this.related_markets;
        descriptionString += ";";
        
        descriptionString += "related_timeframes="+this.related_timeframes;
        descriptionString += ";";
        
        descriptionString += "related_product_plans="+this.related_product_plans;
        descriptionString += ";";
        
        descriptionString += "active="+this.active;
        
        return descriptionString;
    }
    
    public int getActive()
    {
        return active;
    }
    public void setActive(int active)
    {
        this.active = active;
    }
    public String getCommon_name()
    {
        return common_name;
    }
    public void setCommon_name(String common_name)
    {
        this.common_name = common_name;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public long getId()
    {
        return id;
    }
    public void setId(long id)
    {
        this.id = id;
    }
    public String getLink_url()
    {
        return link_url;
    }
    public void setLink_url(String link_url)
    {
        this.link_url = link_url;
    }
    public String getRelated_markets()
    {
        return related_markets;
    }
    public void setRelated_markets(String related_markets)
    {
        this.related_markets = related_markets;
    }
    public String getRelated_product_plans()
    {
        return related_product_plans;
    }
    public void setRelated_product_plans(String related_product_plans)
    {
        this.related_product_plans = related_product_plans;
    }
    public String getRelated_timeframes()
    {
        return related_timeframes;
    }
    public void setRelated_timeframes(String related_timeframes)
    {
        this.related_timeframes = related_timeframes;
    }
    public String getSystem_name()
    {
        return system_name;
    }
    public void setSystem_name(String system_name)
    {
        this.system_name = system_name;
    }
    
    
}
