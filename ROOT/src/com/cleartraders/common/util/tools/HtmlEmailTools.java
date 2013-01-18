package com.cleartraders.common.util.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.cleartraders.common.util.email.EmailBean;

public class HtmlEmailTools
{
    public static List<EmailBean> generateEmail(String filePath,
            String fileName, String[] email_address, String title,
            Hashtable macro)
    {
        List<EmailBean> allEmails = new ArrayList<EmailBean>();

        String strFile = convertFileToStr(filePath, fileName);
        String strFiles = macro == null ? strFile : macroReplace(strFile,
                macro, '[', ']');

        try
        {

            for (int i = 0; i < email_address.length; i++)
            {

                EmailBean email = new EmailBean();
                email.setRecipients(email_address[i]);
                email.setEmailSubject(title);
                email.setEmailBody(strFiles);

                allEmails.add(email);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return allEmails;
    }

    private static final String macroReplace(String text, Hashtable macro,
            char prefix, char suffix) // 1.1
    {
        return macroReplace(text, macro, prefix, suffix, true);
    }

    private static final String macroReplace(String text, Hashtable macro,
            char prefix, char suffix, boolean replaceStr) // 1.1
    {
        return macroReplace(text, macro, prefix, suffix, replaceStr ? 1 : 0);
    }

    private static final String macroReplace(String text, Hashtable macro,
            char prefix, char suffix, int options)
    {
        if (text == null)
            return null;
        final boolean replaceStr = (options & 1) != 0;
        int nCount = 0;
        StringBuffer strBuffer = new StringBuffer();
        char charStr = 0, charStr2 = 0; // In " or '
        int ltext = text.length();
        // int j0 = 0;
        for (int i = 0; i < ltext;)
        {
            char c = text.charAt(i);
            if (charStr == 0 && Character.isJavaIdentifierStart(c))
            {
                char id[] = new char[64];
                id[0] = c;
                int j = 1;
                int p$ = -1;
                int nPara = 0;
                for (j = 1; j < 64 && i + j < ltext;)
                {
                    c = text.charAt(i + j);
                    if (!Character.isJavaIdentifierPart(c) && c != '.')
                        break;
                    if (c == '$')
                        p$ = j;
                    else if (c < '0' || c > '9')
                        p$ = -1;
                    else if (p$ > 0)
                        nPara = nPara * 10 + (c - '0');
                    id[j++] = c;
                }
                i += j;
                // String isStr = ;
                // boolean notMacroId = t==null ;
                // if( !notMacroId && suffix!=0 && ( i>=ltext ||
                // text.charAt(i)!=suffix ) )
                // notMacroId = true;
                boolean notMacroId = suffix != 0
                        && (i >= ltext || text.charAt(i) != suffix);
                if (!notMacroId && prefix != 0)
                {
                    int l = strBuffer.length();
                    if (l == 0 || strBuffer.charAt(l - 1) != prefix)
                        notMacroId = true;
                }
                String t = null;
                if (!notMacroId)
                {
                    Object ot = null;
                    try
                    {
                        ot = macro.get(new String(id, 0, j));
                    }
                    catch (Throwable ex)
                    {
                    }
                    t = ot == null ? null : ot.toString();
                    if (t == null)
                    {
                        if ((charStr2 == 0 && (options & 4) != 0)
                                || (charStr2 != 0 && (options & 9) == 9))
                            return null;
                        if ((options & 2) != 0)
                            t = "";
                    }
                    if (t == null)
                        notMacroId = true;
                }
                if (notMacroId)
                {
                    strBuffer.append(id, 0, j);
                }
                else
                {
                    if (prefix != 0)
                    {
                        // strBuffer.deleteCharAt(strBuffer.length()-1); // 1.2
                        strBuffer.setLength(strBuffer.length() - 1);
                    }
                    if (suffix != 0)
                        i++;
                    nCount++;

                    if (nPara > 0)
                    {
                        String[] parmList = new String[64];
                        i += parseParameterList(text, i, parmList);
                        if (parmList[nPara] != null
                                || parmList[nPara - 1] == null)
                        {
                            // System.out.println("Error parameter count");
                        }
                        strBuffer.append(format(t, parmList));
                    }
                    else
                        // nPara==0
                        strBuffer.append(t);
                }
            }
            else
            {
                strBuffer.append(c);
                if (!replaceStr)
                {
                    if (charStr == 0 && (c == '"' || c == '\''))
                        charStr = c;
                    else if (charStr == c && text.charAt(i - 1) != '\\')
                        charStr = 0;
                    charStr2 = charStr;
                }
                else
                {
                    if (charStr2 == 0 && (c == '"' || c == '\''))
                        charStr2 = c;
                    else if (charStr2 == c && text.charAt(i - 1) != '\\')
                        charStr2 = 0;
                }
                i++;
            }
        }
        return nCount > 0 ? new String(strBuffer) : text;
    }

    private static final int parseParameterList(String text, int iStart,
            String[] list)
    {
        for (int i = 0; i < list.length;)
            list[i++] = null;
        int jStack = 0;
        int ltext = text.length();
        int nList = 0;
        int n = countStartSpace(text, iStart, ltext);
        if (n >= ltext || text.charAt(iStart + n) != '(')
            return 0;
        n++; // skip '(' ')'
        n += countStartSpace(text, iStart + n, ltext);
        char charStr = 0;
        int i0 = n;
        for (; n < ltext;)
        {
            char c = text.charAt(iStart + n);
            if (c == '"' || c == '\'')
            {
                if (charStr == 0)
                    charStr = c;
                else if (c == charStr && text.charAt(iStart + n - 1) != '\\')
                    charStr = 0;
            }
            if (charStr != 0)
            {
                n++;
                continue;
            }
            if (jStack == 0 && (c == ',' || c == ')'))
            {
                String s = text.substring(iStart + i0, iStart + n).trim();
                if (s.length() > 0)
                    list[nList++] = s;
                else if (c == ',' || nList > 0)
                    throw new java.lang.IllegalArgumentException(text);
                n++;
                if (c == ')')
                    break;
                i0 = n += countStartSpace(text, iStart + n, ltext);
                continue;
            }
            if (c == ')')
                jStack--;
            else if (c == '(')
                jStack++;
            n++;
        }
        return n;
    }

    private static final int countStartSpace(String text, int iStart, int iEnd)
    {
        int n = 0;
        for (; iStart + n < iEnd; n++)
        {
            if (!Character.isSpaceChar(text.charAt(iStart + n)))
                break;
        }
        return n;
    }

    private static final String format(String fmt, Object parm[])
    {
        StringBuffer r = new StringBuffer();
        int l = fmt.length();
        for (int i = 0; i < l;)
        {
            int p = fmt.indexOf('%', i);
            if (p < i || p >= l - 1)
            {
                r.append(fmt.substring(i));
                break;
            }
            // p>=i && p<l-1
            r.append(fmt.substring(i, p));
            char c = fmt.charAt(p + 1);
            if (c >= '0' && c <= '9')
            {
                int j = c - '0';
                char c2;
                if (p + 2 < l && (c2 = fmt.charAt(p + 2)) >= '0' && c2 <= '9')
                {
                    int j2 = j * 10 + (c2 - '0');
                    if (j2 < parm.length)
                    {
                        if (parm[j2] != null)
                            r.append(parm[j2].toString());
                        i = p + 3;
                        continue;
                    }
                }
                if (j < parm.length && parm[j] != null)
                    r.append(parm[j].toString());
                i = p + 2;
            }
            else if (c == '%')
            {
                r.append('%');
                i = p + 2;
            }
            else
            {
                r.append('%');
                i = p + 1;
            }
        }
        return r.toString();
    }

    private static String convertFileToStr(String filePath, String fileName)
    {
        String htmlFileName = fileName;

        if (null != filePath)
            htmlFileName = filePath + File.separator + fileName;

        return convertFileToStr(htmlFileName);
    }

    private static String convertFileToStr(String pathName)
    {
        FileReader fileReader = null;
        String strFile = "";

        try
        {
            fileReader = new FileReader(pathName);
            BufferedReader bufReader = new BufferedReader(fileReader);
            if (bufReader == null)
                return null;
            String bufLine = null;
            while ((bufLine = bufReader.readLine()) != null)
            {
                strFile += bufLine + "\n";
            }
            bufReader = null;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            fileReader = null;
        }
        // System.err.println("strFile ----------> " + strFile);
        return strFile;
    }
}
