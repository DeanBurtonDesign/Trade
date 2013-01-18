package com.cleartraders.jsmessager;

/**
 * This file is constant define used for "Poker Game Demo"
 * @author  Peter at PowerTeam
 *
 */
public class MessageConstants {

    public static final String LAST_UPDATED = "lastUpdated";
    public static final String MODIFIED_BY = "modifiedBy";
    public static final String ITEM = "ITEM";

    /**
     * 
     */
    public MessageConstants() {
        super();
        // TODO Auto-generated constructor stub
    }

    public static String getItemName(int i) {
        StringBuffer sb = new StringBuffer(7);
        sb.append(ITEM);
        sb.append(".");
        sb.append(i);
        return sb.toString();
    }

    public static String getItemNameValueString(String itemName, int fieldNum) {
        StringBuffer sb = new StringBuffer(itemName.length() + 6);
        sb.append(itemName);
        sb.append(".");
        sb.append(Integer.toString(fieldNum));
        return sb.toString();
    }

    public static String getItemNameLastUpdatedString(String itemName) {
        StringBuffer sb = new StringBuffer(itemName.length() + 12);
        sb.append(itemName);
        sb.append(".");
        sb.append(LAST_UPDATED);
        return sb.toString();
    }

    public static String getItemNameModifiedByString(String itemName) {
        StringBuffer sb = new StringBuffer(itemName.length() + 11);
        sb.append(itemName);
        sb.append(".");
        sb.append(MODIFIED_BY);
        return sb.toString();
    }

}
