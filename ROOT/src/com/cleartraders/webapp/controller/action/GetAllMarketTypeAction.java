package com.cleartraders.webapp.controller.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cleartraders.common.db.DataCache;
import com.cleartraders.common.entity.MarketTypeBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.model.bean.UserSignalPreferenceBean;
import com.cleartraders.webapp.model.myaccount.AccountController;

public class GetAllMarketTypeAction extends BaseAction
{
    protected ActionForward executeAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        String forwardName=WebConstants.FORWARD_SUCCESS;
        
        try
        {
            HttpSession session = request.getSession();
            
            //Base action already handle logout or timeout issue, so, here we don't need check
            UserBean oUserBean = (UserBean)session.getAttribute(WebConstants.USER_KEY);
            
            //if market id > 0, then, it means this is change operation
            //if market id < 0, then, it means this is add new operation
            long selectedMarketID = Long.parseLong(request.getParameter("marketID"));
            
            LogTools.getInstance().insertLog(DebugLevel.INFO,"Get All Markets request from User:"+oUserBean.getLogin_name()+", IP:"+request.getRemoteAddr()+
                                                             " Selected Market ID:"+selectedMarketID);
            
            //get all user current subscribe signal market id
            List<UserSignalPreferenceBean> oAllMySignalPrferenceBean = (new AccountController()).getUserSignalPreferenceBean(oUserBean,false);
            
            List<MarketTypeBean> oAllMarketTypes = DataCache.getInstance().getAllMarketType();
            for(int i=0; i<oAllMarketTypes.size(); i++)
            {
                MarketTypeBean oTempMarketBean = oAllMarketTypes.get(i);
                
                //disable those market bean which are already used
                for(int j=0; j<oAllMySignalPrferenceBean.size(); j++)
                {
                    UserSignalPreferenceBean oSignalMarketPreference = oAllMySignalPrferenceBean.get(j);
                    
                    if(oTempMarketBean.getId() == oSignalMarketPreference.getMarketID())
                    {
                        if(selectedMarketID < 0)
                        {
                            //if current is add operation, then, disable which is already used
                            oTempMarketBean.setActive(WebConstants.DISABLE);
                        }
                        else
                        {
                            //if current is change operation, then, disable which is not changing one
                            if(selectedMarketID != oTempMarketBean.getId())
                            {
                                oTempMarketBean.setActive(WebConstants.DISABLE);
                            }
                        }
                    }
                }
            }
            
            request.setAttribute(WebConstants.ALL_MARKET_TYPE, oAllMarketTypes);
            request.setAttribute(WebConstants.CURRENT_MARKET_ID, selectedMarketID);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            LogTools.getInstance().insertLog(DebugLevel.ERROR,CommonTools.getExceptionDescribe(e));
        }
        
        return mapping.findForward(forwardName);
    }
}
