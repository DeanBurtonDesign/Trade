package com.cleartraders.webadmin.model.membersync.mailchimp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MailChimpUtils
{
    public static final SimpleDateFormat DATE_FORMAT_1_1 = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static final SimpleDateFormat MERGES_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    public static Date parseDate(final String date) throws ParseException
    {
        return DATE_FORMAT.parse(date);
    }

    public static Date parseDateMerges(final String date) throws ParseException
    {
        return MERGES_DATE_FORMAT.parse(date);
    }

    public static Date parseDate11(final String date) throws ParseException
    {
        return DATE_FORMAT_1_1.parse(date);
    }

    public static String formatDate(final Date date)
    {
        return DATE_FORMAT.format(date);
    }

    public static String formatDate11(final Date date)
    {
        return DATE_FORMAT_1_1.format(date);
    }
}
