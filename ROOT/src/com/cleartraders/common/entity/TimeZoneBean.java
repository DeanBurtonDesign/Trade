package com.cleartraders.common.entity;

/**
 * 
create table time_zone_table
(
    id                  int unsigned not null primary key,
    name            varchar(100)
);
 * @author Peter
 *
 */
public class TimeZoneBean
{
    private long id=0;
    private int offset=0;
    private String name="";
    
    public TimeZoneBean(){};
    
    public TimeZoneBean(TimeZoneBean cloneBean)
    {
        id=cloneBean.getId();
        offset=cloneBean.getOffset();
        name=cloneBean.getName();
    }
    
    public int getOffset()
    {
        return offset;
    }
    public void setOffset(int offset)
    {
        this.offset = offset;
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
