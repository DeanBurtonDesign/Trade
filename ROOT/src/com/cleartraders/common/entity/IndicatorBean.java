package com.cleartraders.common.entity;

import java.io.Serializable;

public class IndicatorBean implements Serializable
{
    private static final long serialVersionUID = 7694991118204821715L;

    private long id=0;
    private long strategy_id=0;
    private String name="";
    private int period=0;
    private int visable=0;
    private String color="";
    private String description="";
    private int type=0;
    
    public IndicatorBean(){};
    
    public IndicatorBean(IndicatorBean oCloneBean)
    {
        this.id = oCloneBean.getId();
        this.strategy_id = oCloneBean.getStrategy_id();
        this.name = oCloneBean.getName();
        this.period = oCloneBean.getPeriod();
        this.visable = oCloneBean.getVisable();
        this.color = oCloneBean.getColor();
        this.description = oCloneBean.getDescription();
        this.type = oCloneBean.getType();
    }
        
    public long getStrategy_id()
    {
        return strategy_id;
    }
    public void setStrategy_id(long strategy_id)
    {
        this.strategy_id = strategy_id;
    }
    public int getVisable()
    {
        return visable;
    }
    public void setVisable(int visable)
    {
        this.visable = visable;
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
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public int getPeriod()
    {
        return period;
    }
    public void setPeriod(int period)
    {
        this.period = period;
    }
    public String getColor()
    {
        return color;
    }
    public void setColor(String color)
    {
        this.color = color;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

}
