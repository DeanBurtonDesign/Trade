package com.cleartraders.webapp.model.notification;

public class NotificationTemplate
{
    private String templateName;
    private int templateType;

    public NotificationTemplate(String templateName, int templateType)
    {
        this.templateName = templateName;
        this.templateType = templateType;
    }
    
    public int getTemplateType()
    {
        return templateType;
    }

    public void setTemplateType(int templateType)
    {
        this.templateType = templateType;
    }

    public String getTemplateName()
    {
        return templateName;
    }

    public void setTemplateName(String templateName)
    {
        this.templateName = templateName;
    }
}
