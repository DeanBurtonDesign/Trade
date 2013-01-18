package com.cleartraders.webapp.model.contact;

import java.io.File;
import java.util.Hashtable;
import java.util.List;

import com.cleartraders.common.entity.TempPasswordBean;
import com.cleartraders.common.entity.UserBean;
import com.cleartraders.common.util.email.EmailBean;
import com.cleartraders.common.util.email.EmailHandler;
import com.cleartraders.common.util.tools.CommonTools;
import com.cleartraders.common.util.tools.HtmlEmailTools;
import com.cleartraders.webapp.WebConstants;
import com.cleartraders.webapp.config.WebappResManager;
import com.cleartraders.webapp.db.DBAccessor;
import com.cleartraders.webapp.model.bean.EnquiryBean;
import com.cleartraders.webapp.model.bean.TellAFriendsBean;
import com.cleartraders.webapp.model.notification.EmailNotificationFactory;

public class ContactController
{
    private static String htmlEmailBasedPath = CommonTools.getSysDir()+ "resource" + File.separator + "emailtemplate" + File.separator;   
    private static String resourceURL = WebappResManager.getInstance().getHtml_email_resource_url();
    private final String SALE_ENQUIRY = "Sale Enquiry";
    private final String GENERAL_ENQUIRY = "General Enquiry";
    private final String TECHNICAL_ENQUIRY = "Technical Enquiry";

    public boolean tellAFriends(TellAFriendsBean oTellAFriends)
    {
        //send email to reference friends and administrator
        
        sendTellAFriendMessage(oTellAFriends.getSender_name(),"Administrator", WebappResManager.getInstance().getAdministrator_email_address());
                
        sendTellAFriendMessage(oTellAFriends.getSender_name(),oTellAFriends.getReceiver_name_1(), oTellAFriends.getReceiver_email_1());
        
        sendTellAFriendMessage(oTellAFriends.getSender_name(),oTellAFriends.getReceiver_name_2(), oTellAFriends.getReceiver_email_2());
                
        return true;
    }
    
    private void sendTellAFriendMessage(String senderFirstName, String receiveFirstName, String receiveEmail)
    {
        Hashtable<String,String> properties = new Hashtable<String,String>();
        properties.put("first_name", receiveFirstName);
        properties.put("friend_first_name", senderFirstName);
        properties.put("resource_url", resourceURL);
        
        String[] emailAddresses = new String[1];
        emailAddresses[0] = receiveEmail;
        
        List<EmailBean> confirmNotificatonEmail = HtmlEmailTools.generateEmail(htmlEmailBasedPath,
                                                    WebappResManager.getInstance().getTell_a_friend_email_file(),
                                                    emailAddresses,
                                                    WebappResManager.getInstance().getTell_a_friend_email_subject(),
                                                    properties);
        
        EmailHandler.getInstance().appendEmailToSend(confirmNotificatonEmail);
    }
    
    public boolean sendNewPassword(UserBean userBean, String newPassword)
    {
        if(null == userBean || null == newPassword)
        {
            return false;
        }
        
        String encryptedPassword = CommonTools.encryptSHA(newPassword);
        String confirmCode = CommonTools.encryptSHA(userBean.getConfirm_code()+System.currentTimeMillis());
        
        //store password into temp_password_table
        if(DBAccessor.getInstance().insertUserTempPWD(userBean.getId(), encryptedPassword, confirmCode))
        {
            //send confirm link to user, after click confirm the password will be enable
            EmailBean resetPWDEmail = EmailNotificationFactory.getNotificationEmail(userBean,WebConstants.FIND_PASSWORD_CONFIRM_TEMPLATE);
            
            EmailHandler.getInstance().appendEmailToSend(resetPWDEmail);
        }
        
        return true;
    }
    
    public boolean confirmNewPassword(String userEmail, String confirmCode)
    { 
        UserBean userBean = DBAccessor.getInstance().getUserBeanByEmailOrLoginName(userEmail);
        
        if(null == userBean || null == confirmCode)
        {
            return false;
        }
        
        //check confirm code in temp_password_table
        TempPasswordBean tempPasswordBean = DBAccessor.getInstance().getFindPasswordConfirmCode(userBean.getId());
        if( tempPasswordBean != null && tempPasswordBean.getConfirm_code().equals(confirmCode))
        {
            //don't need send password again, since this was set by user
            //so, just reset password in users_table, and remove password in temp_password_table
            return DBAccessor.getInstance().updateUserPWDByTempPWD(userBean.getId(),tempPasswordBean);
        }
        else
        {
            return false;
        }
    }
    
    public boolean newContactUsMessage(EnquiryBean contactInfo)
    {                
        sendMessageToSender(contactInfo);
        
        sendMessageToAdmin(contactInfo);
        
        return true;
    }
    
    private void sendMessageToAdmin(EnquiryBean contactInfo)
    {
        //for administrator
        String adminEmailAddress = WebappResManager.getInstance().getAdministrator_email_address();
        String emailTitle = "New Enquiry From ClearTraders";
        
        String emailContent = "";
        emailContent += "Enquiry Type, " + getEnquiryTypeName(contactInfo.getEnquiry_type())+", ";
        emailContent += " From " +contactInfo.getFirst_name()+" "+contactInfo.getLast_name()+". Contact Email is "+contactInfo.getEmail()+". ";
        emailContent += " Enquiry content is ' "+contactInfo.getEnquiry()+" '";
        
        //send Email to them
        EmailBean oEmail = new EmailBean();
        oEmail.setRecipients(adminEmailAddress);
        oEmail.setEmailSubject(emailTitle);
        oEmail.setEmailBody(emailContent);
        
        EmailHandler.getInstance().appendEmailToSend(oEmail);
    }
    
    private void sendMessageToSender(EnquiryBean contactInfo)
    {
        //for Enquiry Sender
        Hashtable<String,String> properties = new Hashtable<String,String>();
        properties.put("first_name", contactInfo.getFirst_name());
        properties.put("message_content", contactInfo.getEnquiry());
        properties.put("resource_url", resourceURL);
        
        String[] emailAddresses = new String[1];
        emailAddresses[0] = contactInfo.getEmail();
        
        List<EmailBean> confirmNotificatonEmail = HtmlEmailTools.generateEmail(htmlEmailBasedPath,
                                                    WebappResManager.getInstance().getContact_us_confirm_email_file(),
                                                    emailAddresses,
                                                    WebappResManager.getInstance().getContact_us_confirm_email_subject(),
                                                    properties);
        
        EmailHandler.getInstance().appendEmailToSend(confirmNotificatonEmail);
    }
    
    private String getEnquiryTypeName(int type)
    {
        String typeName = "";
        
        switch(type)
        {
            case WebConstants.SALE_Enquiry_TYPE:
                typeName = SALE_ENQUIRY;
                break;
            case WebConstants.GENERAL_Enquiry_TYPE:
                typeName = GENERAL_ENQUIRY;
                break;
            case WebConstants.TECHNICAL_Enquiry_TYPE:
                typeName = TECHNICAL_ENQUIRY;
                break;
            default:
                break;
        }
        
        return typeName;
    }
}   
