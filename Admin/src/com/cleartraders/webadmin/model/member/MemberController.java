package com.cleartraders.webadmin.model.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.MarketStrategyBean;
import com.cleartraders.common.entity.MarketTypeAndPeriodBean;
import com.cleartraders.common.entity.ProductBean;
import com.cleartraders.common.entity.QuickLinkBean;
import com.cleartraders.common.entity.StrategyBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.email.EmailBean;
import com.cleartraders.common.util.email.EmailHandler;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.webadmin.config.WebadminResManager;
import com.cleartraders.webadmin.db.DBAccessor;

public class MemberController
{    
    public boolean resetMemberPWD(long userID, String newPWD)
    {
        UserBean oUserBean = DBAccessor.getInstance().getUserInfoByID(userID);
        if(oUserBean != null && oUserBean.getId() > 0)
        {
            if(DBAccessor.getInstance().updateUserPWDByUserID(userID, CommonTools.encryptSHA(newPWD)))
            {
                EmailBean resetPWDEmail = new EmailBean();
                resetPWDEmail.setEmailSubject("Your Password Had Been Changed");
                resetPWDEmail.setRecipients(oUserBean.getEmail());                
                String emailContent = "Your new password is "+newPWD+"";                
                resetPWDEmail.setEmailBody(emailContent);
                
                EmailHandler.getInstance().appendEmailToSend(resetPWDEmail);
                
                return true;
            }
        }
        
        return false;
    }
    
    public boolean deleteMember(long userID, String userLoginName)
    {
        //check user name is consistent with user id
        UserBean oUserBean = DBAccessor.getInstance().getUserInfoByLoginName(userLoginName);
        if(oUserBean == null || oUserBean.getId() != userID)
        {
            return false;
        }
        
        return DBAccessor.getInstance().removeUserByID(userID);
    }
    
    public boolean addMember(UserBean userInfo, String marketIDList)
    {
        List<MarketTypeAndPeriodBean> marketList = generateMarketList(userInfo,marketIDList);
        
        userInfo.setPwd(CommonTools.encryptSHA(userInfo.getPwd()));
        userInfo.setEnable(CommonDefine.USER_ENABLED);
        
        return DBAccessor.getInstance().storeUserIntoDB(userInfo, marketList,new ArrayList<QuickLinkBean>());
    }
    
    public boolean updateMember(UserBean userInfo, String marketIDList)
    {
        List<MarketTypeAndPeriodBean> marketList = generateMarketList(userInfo,marketIDList);
        
        return DBAccessor.getInstance().updateMemberDetails(userInfo,marketList);
    }
    
    private List<MarketTypeAndPeriodBean> generateMarketList(UserBean userInfo,String marketIDList)
    {
        //whenever admin add market to user, system also need limit the active number of Markets
        ProductBean productBean = DBAccessor.getInstance().getProductByID(userInfo.getMemberLevel());  
        int marketAmountOfMembership = productBean.getTotalMarkets();
        
        int enabledMarketNumber = 0;
        
        List<MarketTypeAndPeriodBean> marketList = new ArrayList<MarketTypeAndPeriodBean>();        
        if(marketIDList.trim().length() >= 0)
        {
            long marketPeriodId = WebadminResManager.getInstance().getDefault_market_peirod();
            
            String[] subscribledMarkets = marketIDList.split(";");
            for(int i=0; i<subscribledMarkets.length; i++)
            {
                if(subscribledMarkets[i].trim().length() == 0)
                {
                    continue;
                }
                
                String[] marketRelatedStrategy = subscribledMarkets[i].split(",");
                if(marketRelatedStrategy.length != 2)
                {
                    continue;
                }
                
                long marketID = Long.parseLong(marketRelatedStrategy[0]);
                long strategyID = Long.parseLong(marketRelatedStrategy[1]);
                
                MarketTypeAndPeriodBean marketTypeAndPeriod = new MarketTypeAndPeriodBean();
                marketTypeAndPeriod.setActive(false);
                marketTypeAndPeriod.setMarketID(marketID);
                marketTypeAndPeriod.setMarketPeriodID(marketPeriodId);
                marketTypeAndPeriod.setStrategyID(strategyID);
                
                if(enabledMarketNumber < marketAmountOfMembership)
                {                
                    marketTypeAndPeriod.setActive(true);
                    
                    enabledMarketNumber++;
                }
                
                marketList.add(marketTypeAndPeriod);
            }
        }
        
        return marketList;
    }
    
    public String getMemberSubcribleMarketString(long memberID)
    {
        List<MarketTypeAndPeriodBean> marketStrategyList = getMemberSubcribleMarketStrategyList(memberID);
        String marketIDList = "";
        
        for(int i=0; i < marketStrategyList.size(); i++)
        {
            MarketTypeAndPeriodBean oMarketTypeAndPeriodBean = marketStrategyList.get(i);
                        
            marketIDList += oMarketTypeAndPeriodBean.getMarketID()+"-"+oMarketTypeAndPeriodBean.getStrategyID();
            
            marketIDList += ",";
        }
        
        return marketIDList;
    }
    
    public List<MarketTypeAndPeriodBean> getMemberSubcribleMarketStrategyList(long memberID)
    {
        return DBAccessor.getInstance().getMemberSubcribleMarketStrategyList(memberID);
    }
    
    public List<Long> getMemberSubcribleMarketList(long memberID)
    {
        return DBAccessor.getInstance().getMemberSubcribleMarketIDList(memberID);
    }
    
    public long getMemberSMSCredit(long memberID)
    {
        return DBAccessor.getInstance().getUserSmsCredits(memberID);
    }
    
    public UserBean getMemberByID(long memberID)
    {
        return DBAccessor.getInstance().getUserInfoByID(memberID);
    }
    
    public List<UserBean> searchMember(String memberName, String memberEmail, int memberType)
    {
        if(null == memberName && null == memberEmail)
        {
            return new ArrayList<UserBean>(); 
        }
        
        return DBAccessor.getInstance().searchMembers(memberName, memberEmail, memberType, true);
    }
    
    public Map<Long, List<StrategyBean>> getMarketStrategyMap()
    {
        Map<Long, List<StrategyBean>> result = new HashMap<Long, List<StrategyBean>>();
        
        List<MarketStrategyBean> allMarketStrategy = DBAccessor.getInstance().getAllMarketStrategyData();
        for(int i=0; i<allMarketStrategy.size(); i++)
        {
            MarketStrategyBean marketStrategy = allMarketStrategy.get(i);
            
            Long marketID = Long.valueOf(marketStrategy.getMarket_id());
            
            if(result.get(marketID) != null)
            {
                List<StrategyBean> relatedStrategy = result.get(marketID);
                relatedStrategy.add(DBAccessor.getInstance().getStrategyFullInfoByID(marketStrategy.getStrategy_id()));
            }
            else
            {
                List<StrategyBean> relatedStrategy = new ArrayList<StrategyBean>();
                relatedStrategy.add(DBAccessor.getInstance().getStrategyFullInfoByID(marketStrategy.getStrategy_id()));
                
                result.put(marketID, relatedStrategy);
            }
        }
        
        return result;
    }
    
    public List<UserBean> getAllMembers()
    {
        return DBAccessor.getInstance().getAllMembersByType(CommonDefine.ALL_MEMBER_TYPE, true);
    }
    
    public List<UserBean> getAllRegisteredMember()
    {
        return DBAccessor.getInstance().getAllRegisteredMember();
    }
    
    public List<UserBean> getAllUnregisteredMember()
    {
        return DBAccessor.getInstance().getAllUnregisteredMember();
    }
    
    public List<UserBean> getAllCancelledMembers()
    {
        return DBAccessor.getInstance().getAllCancelledMembers();
    }
}
