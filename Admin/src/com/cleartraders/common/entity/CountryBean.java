package com.cleartraders.common.entity;

/**
 * 
create table country_table
(
    id                  int unsigned not null primary key,
    name            varchar(100)
);
 * 
 * @author Peter
 *
 */
public class CountryBean
{
    private long id=0;
    private int zone_num=0;
    private String name="";
    
    public CountryBean(){};
    
    public CountryBean(CountryBean oClone)
    {
        id = oClone.getId();
        zone_num = oClone.getZone_num();
        name = oClone.getName();
    }
    
    public int getZone_num()
    {
        return zone_num;
    }
    public void setZone_num(int zone_num)
    {
        this.zone_num = zone_num;
    }
    public long getId()
    {
        return id;
    }
    public void setId(long id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    
}
