package com.cleartraders.webadmin.model.smscredits;

import java.util.List;

import com.cleartraders.common.entity.SmsPackageBean;
import com.cleartraders.webadmin.db.DBAccessor;

public class SMSCreditsController
{
    public List<SmsPackageBean> getAllSMSPackages()
    {
        return DBAccessor.getInstance().getAllSMSPackages();
    }
    
    public boolean deleteSMSPackageByID(long smsPackageID)
    {
        return DBAccessor.getInstance().deleteSMSPackageByID(smsPackageID);
    }
    
    public boolean addSMSPackage(SmsPackageBean smsPackageBean)
    {
        return DBAccessor.getInstance().addSMSPackage(smsPackageBean);
    }
    
    public boolean updateSMSPackage(SmsPackageBean smsPackageBean)
    {
        return DBAccessor.getInstance().updateSMSPackage(smsPackageBean);
    }
}
