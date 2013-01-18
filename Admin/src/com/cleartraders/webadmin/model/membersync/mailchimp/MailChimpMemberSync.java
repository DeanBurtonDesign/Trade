package com.cleartraders.webadmin.model.membersync.mailchimp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cleartraders.common.define.CommonDefine;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.log.DebugLevel;
import com.cleartraders.common.util.log.LogTools;
import com.cleartraders.webadmin.config.WebadminResManager;
import com.cleartraders.webadmin.db.DBAccessor;
import com.cleartraders.webadmin.model.membersync.IMemberSync;

public class MailChimpMemberSync implements IMemberSync
{
    // API Key - see http://admin.mailchimp.com/account/api or run login() once
    private String apiKey = "";

    // A List Id to run examples against. use lists() to view all
    // Also, login to MC account, go to List, then List Tools, and look for the
    // List ID entry
    private String listId = "";
    
    private final int LIMIT_MEMBERS_TO_SYNC=15000;

    private IMailChimpServices mcServices = null;

    public static void main(final String[] args)
    {
        final MailChimpMemberSync testMCList1 = new MailChimpMemberSync();

        testMCList1.syncMembers();
    }

    public void syncMembers()
    {
        List<UserBean> allMembers = DBAccessor.getInstance().getAllMembersByType(CommonDefine.ALL_MEMBER_TYPE, true);
        
        if (initialize())
        {
            Map<String,String> existingEmails = listMembers();
            List<UserBean> needToAddMembers = new ArrayList<UserBean>();
            
            //filter out duplicated emails
            for(int i=0; i<allMembers.size(); i++)
            {
                //login name is the email address, and it can not be changed
                String userLoginName = allMembers.get(i).getLogin_name();
                
                if(!existingEmails.containsKey(userLoginName))
                {
                    needToAddMembers.add(allMembers.get(i));
                }
            }
            
            //subscribe non-duplicated members
            for(int i=0; i<needToAddMembers.size(); i++)
            {
                UserBean member = needToAddMembers.get(i);
                
                final Map<String, String> merges = new HashMap<String, String>();
    
                merges.put("LNAME", member.getLast_name());
                merges.put("FNAME", member.getFirst_name());
                merges.put("EMAIL", member.getLogin_name());
    
                try
                {
                    mcServices.listSubscribe(apiKey, listId, member.getLogin_name(), merges,IMailChimpServices.EMAIL_TYPE_HTML, false);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    
                    LogTools.getInstance().insertLog(DebugLevel.ERROR,e.toString());
                }
            }
        }
    }

    public boolean initialize()
    {
        this.apiKey = WebadminResManager.getInstance().getMailchimp_api_key();
        this.listId = WebadminResManager.getInstance().getMailchimp_list_id();
        mcServices = MailChimpServiceFactory.getMailChimpServices();

        final String ping = mcServices.ping(apiKey);
        if (IMailChimpServices.PING_SUCCESS.equals(ping))
        {
            LogTools.getInstance().insertLog(DebugLevel.INFO, "MailChimp connection pinged successfully");
            return true;
        }
        else
        {
            LogTools.getInstance().insertLog(DebugLevel.ERROR, "Failed to ping MailChimp, response: " + ping);
            return false;
        }
    }

    private Map<String,String> listMembers()
    {
        Map<String, String> allExistingEmails = new HashMap<String, String>();
        
        final Object[] listMembers = mcServices.listMembers(apiKey, listId, IMailChimpServices.STATUS_SUBSCRIBED,"2008-07-01 00:00:00", 0, LIMIT_MEMBERS_TO_SYNC);
        
        System.out.println("listMembers got " + listMembers.length + " members");
        
        for (final Object member : listMembers)
        {
            //System.out.print("\tMember: ");
            final Map<String, Object> map = (Map<String, Object>) member;
            
            for (final Object key : map.keySet())
            {
                final Object value = map.get(key);

                if (key.equals(IMailChimpServices.MEMBER_FIELD_EMAIL))
                {
                    //System.out.print(key + " = " + value + "(" + value.getClass().getSimpleName() + ")\t");
                    
                    allExistingEmails.put(value.toString(), value.toString());
                }
            }
        }

        return allExistingEmails;
    }
}
