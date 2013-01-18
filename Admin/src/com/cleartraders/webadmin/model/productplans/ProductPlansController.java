package com.cleartraders.webadmin.model.productplans;

import java.util.List;

import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.entity.ProductBean;
import com.cleartraders.webadmin.db.DBAccessor;

public class ProductPlansController
{
    public boolean addProduct(ProductBean oProduct)
    {
        if(null == oProduct)
            return false;
        
        return DBAccessor.getInstance().addProduct(oProduct);
    }
    
    public boolean updateProduct(ProductBean oProduct)
    {
        if(null == oProduct)
            return false;
        
        return DBAccessor.getInstance().updateProduct(oProduct);
    }
    
    public List<ProductBean> getAllProducts()
    {
        return DataCache.getInstance().getAllProduct();
    }
    
    public ProductBean getProductByID(long productID)
    {
        List<ProductBean> oAllProducts = getAllProducts();
        
        for(int i=0; i<oAllProducts.size(); i++)
        {
            ProductBean oTempBean = oAllProducts.get(i);
            
            if(oTempBean.getId() == productID)
            {
                return oTempBean;
            }
        }
        
        return null;
    }
}
