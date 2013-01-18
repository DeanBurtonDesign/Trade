package com.cleartraders.common.util.email;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class SendMail
{
    static String mailFooter = "";

    boolean sessionDebug;// BEBUG

    MimeMessage msg;// �ʼ����

    MimeMultipart multipart;// �ʼ���

    Properties props;// �����ļ�

    Session session = null;

    Transport tpObj = null;

    String username = null;// �û���

    String password = null;//

    /**
     * �����
     * 
     * @param host String �ʼ������������
     * @param bolAuthor boolean ��֤��
     * @throws MessagingException
     */
    public SendMail(String host, boolean bolAuthor) throws MessagingException
    {
        props = System.getProperties();
        props.put("mail.host", host);
        props.put("mail.transport.protocol", "smtp");
        setNeedAuth(bolAuthor);
        session = Session.getDefaultInstance(props, null);
        session.setDebug(sessionDebug);
        tpObj = session.getTransport();
        msg = new MimeMessage(session);
        msg.setSentDate(new java.util.Date());
        multipart = new MimeMultipart();
        msg.setContent(multipart);
    }
    
    /**
     * @param host String
     * @param to String
     * @param from String
     * @param subject String
     * @param body String
     * @param bolAuthor boolean
     * @throws Exception
     */
    public SendMail(String host, String to, String fromAddress, String fromName, String subject,
            String body, boolean bolAuthor) throws Exception
    {
        this(host, new String[] { to }, fromAddress, fromName, subject, body, bolAuthor);
    }

    /**
     * @param need boolean
     */
    private void setNeedAuth(boolean need)
    {
        if (props == null)
            props = System.getProperties();
        if (need)
        {
            props.put("mail.smtp.auth", "true");
        }
        else
        {
            props.put("mail.smtp.auth", "false");
        }
    }

    /**
     * @param host String
     * @param to String[]
     * @param from String
     * @param subject String
     * @param body String
     * @param bolAuthor boolean
     * @throws Exception
     */
    public SendMail(String host, String[] to, String fromAddress, String fromName, String subject,
            String body, boolean bolAuthor) throws Exception
    {
        this(host, bolAuthor);
        this.setSendTo(to);
        this.setFrom(fromAddress, fromName);
        this.setSubject(subject);
        this.setBody(body);
    }

    /**
     * ���÷���Դ
     * 
     * @param from String
     * @throws Exception
     */
    public void setFrom(String fromAddress, String fromName) throws Exception
    {
        msg.setFrom(new InternetAddress(fromAddress, fromName));
    }

    /**
     * ���÷���Ŀ�ĵ�
     * 
     * @param to String[]
     * @throws Exception
     */
    public void setSendTo(String[] to) throws Exception
    {
        for (int i = 0; i < to.length; i++)
            setSendTo(to[i]);
    }

    /**
     * ���÷���Ŀ�ĵ�
     * 
     * @param to String
     * @throws Exception
     */
    public void setSendTo(String to) throws Exception
    {
        InternetAddress[] address = { new InternetAddress(to) };
        msg.setRecipients(Message.RecipientType.TO, address);
    }

    /**
     * ���ÿ���Ŀ�ĵ�
     * 
     * @param to String[]
     * @throws Exception
     */
    public void setCopyTo(String[] to) throws Exception
    {
        for (int i = 0; to != null && i < to.length; i++)
            setCopyTo(to[i]);
    }

    /**
     * ���ÿ���Ŀ�ĵ�
     * 
     * @param to String
     * @throws Exception
     */
    public void setCopyTo(String to) throws Exception
    {
        // System.out.println("Copying mail to :"+to[i]);
        InternetAddress[] address = { new InternetAddress(to) };
        msg.setRecipients(Message.RecipientType.CC, address);
    }

    /**
     * ���ñ���
     * 
     * @param subject String
     * @throws Exception
     */
    public void setSubject(String subject) throws Exception
    {
        msg.setSubject(subject, "GBK");
    }

    /**
     * ���÷�����
     * 
     * @param body String
     * @throws Exception
     */
    public void setBody(String body) throws Exception
    {
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(
                "<meta http-equiv=Content-Type content=text/html; charset=GBK>"
                        + body + mailFooter, "text/html;charset=GBK");

        multipart.addBodyPart(messageBodyPart);
    }

    /**
     * ���÷��͸���
     * 
     * @param fileName String
     * @throws Exception
     */
    public void setAttachFile(String fileName) throws Exception
    {
        setAttachFile(fileName, fileName);
    }

    /**
     * ���÷��͸���
     * 
     * @param fileName String
     * @param name String
     * @throws Exception
     */
    public void setAttachFile(String fileName, String name) throws Exception
    {
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(fileName);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(name);
        multipart.addBodyPart(messageBodyPart);
    }

    /**
     * ���÷��͸���
     * 
     * @param fileName String[][]
     * @throws Exception
     */
    public void setAttachFile(String[][] fileName) throws Exception
    {
        for (int i = 0; i < fileName.length; i++)
            setAttachFile(fileName[i][0], fileName[i][1]);
    }

    /**
     * ���ͷ���
     * 
     * @throws Exception
     */
    public void send() throws Exception
    {
        tpObj.connect((String) props.get("mail.smtp.host"), username, password);
        tpObj.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
    }

    /**
     * �����û���
     * 
     * @param username String
     */
    public void setUserName(String username)
    {
        this.username = username;
    }

    /**
     * �����û�����
     * 
     * @param password String
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * 
     * @param args String[]
     */
    public static void main(String args[])
    {
        try
        {
            String emailContent = "Dear Mr. Huang, \n BUY SIGNAL Occured at 1.2778";
            SendMail sm = new SendMail("mail.burmacdesign.com","bjyuzhen@gmail.com", "admin@burmacdesign.com", "Clear Traders", "Signal Notify",emailContent, true);
            sm.setUserName("bur57338");
            sm.setPassword("qwe123asd");
            //sm.setSendTo("tanlq@zhongsou.com");
            // sm.setAttachFile("d:\\DBExtractor.java");
            sm.send();
            //System.out.println("----------send sucessful-----");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
