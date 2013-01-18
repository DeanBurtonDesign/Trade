package com.cleartraders.webadmin.model.membersync.mailchimp;

import java.util.Map;

public interface IMailChimpServices
{
    static final String PING_SUCCESS = "Everything's Chimpy!";

    static final String EMAIL_TYPE_TEXT = "text";

    static final String EMAIL_TYPE_HTML = "html";

    static final String STATUS_SUBSCRIBED = "subscribed";

    static final String STATUS_UNSUBSCRIBED = "unsubscribed";

    static final String STATUS_CLEANED = "cleaned"; // hard

    // bounce

    /**
     * Login to Mailchimp account and generate an apiKey. The apiKey is valid
     * until specifically expired, thus, no need to login each time: one my just
     * generate an apiKey once and use it from that point forward.
     * 
     * @param username
     * @param password
     * @return
     */
    String login(String username, String password);

    /**
     * Checks service availability, returns {@code PING_SUCCESS} if successful.
     * 
     * @param apiKey
     * @return Ping response string
     */
    String ping(String apiKey);

    /**
     * Expire an apiKey
     * 
     * @param username
     * @param password
     * @param apiKey
     * @return
     */
    boolean apiKeyExpire(String username, String password, String apiKey);

    /**
     * Returns the lists in the account. The result is an array of {@code Map<String,
     * Object>}(could be casted individually but not as an array).
     * 
     * The fields included in each map: (See LIST_FIELD_ constants for values)
     * <ul>
     * <li>member_count (Double) - number of members</li>
     * <li>cleaned_count (Double) - number of deleted members</li>
     * <li>unsubscribe_count (Double) - number of unsubscribed members</li>
     * <li>web_id (Integer) - id on the web URLs</li>
     * <li>name (String) - Name as it appears in the web UI</li>
     * <li>date_created (String)- date in the format "Feb 03, 2009 06:43 pm",
     * parse with {@link MailChimpUtils.parseDate11}</li>
     * <li>id (String) - this is the ID which is used for APIs which receive
     * listId</li>
     * </ul>
     * 
     * @param apiKey
     * @return Array of {@code Map<String, Object>}
     */
    Object[] lists(String apiKey);

    static final String LIST_FIELD_MEMBER_COUNT = "member_count";

    static final String LIST_FIELD_CLEAND_COUNT = "cleaned_count";

    static final String LIST_FIELD_UNSUBSCRIBE_COUNT = "unsubscribe_count";

    static final String LIST_FIELD_WEB_ID = "web_id";

    static final String LIST_FIELD_NAME = "name";

    static final String LIST_FIELD_DATE_CREATED = "date_created";

    static final String LIST_FIELD_ID = "id";

    /**
     * Returns the lists in the account. The result is an array of {@code Map<String,
     * String>} (could be casted individually but not as an array).
     * 
     * The fields included in each map: (See MEMBER_FIELD_ constants for values)
     * <ul>
     * <li>email (String)</li>
     * <li>timestamp (String) - date in the format "yyyy-MM-dd hh:mm:ss" ,
     * parse with {@link MailChimpUtils.parseDate}</li>
     * </ul>
     * 
     * @param apiKey
     * @param listId returned from lists
     * @param status status of subscribers, see STATUS_ constants
     * @param since date to start from in the format "yyyy-MM-dd hh:mm:ss"
     * @param start start from member number
     * @param limit maximum number of members to return
     * @return Array of {@code Map<String, String>}
     */
    Object[] listMembers(String apiKey, String listId, String status, String since, int start, int limit);

    static final String MEMBER_FIELD_EMAIL = "email";

    static final String MEMBER_FIELD_TIMESTAMP = "timestamp";

    /**
     * Subscribes a new member to the list.
     * 
     * If the member status is unsubscribed, this will return true but will not
     * subscribe the member to the list.
     * 
     * Merges should include the standard fields, see MERGE_FIELD_ constants.
     * Other than that, may include user defined fields. Undefined fields are
     * ignored. Dates should be formated using
     * {@link MailChimpUtils.formateDate}
     * 
     * @param apiKey
     * @param listId
     * @param email
     * @param mergeVars a {@code Map<String, String>} of arguments.
     * @param emailType
     * @param doubleOptin boolean - if true, a confirmation email will be sent
     *            to the subscriber and the subscriber will not be added until
     *            the confirmation is received. Note that this email does not
     *            cost email credits.
     * @return
     * @return
     * @throws MailChimpServiceException When the subscriber is already
     *             subscribed to the list. The message will be like
     *             "me@example.com is already subscribed to list My List".
     */
    boolean listSubscribe(String apiKey, String listId, String email, Map<String, String> mergeVars, String emailType,
            boolean doubleOptin);

    static final String MERGE_FIELD_FIRST_NAME = "FNAME";

    static final String MERGE_FIELD_LAST_NAME = "LNAME";

    static final String MERGE_FIELD_EMAIL = "EMAIL";

    static final String MERGE_FIELD_IP_ADDRESS = "OPTINIP";

    static final String MERGE_FIELD_INTERESTS = "INTERESTS";

    /**
     * @param apiKey
     * @param listId
     * @param email
     * @param deleteMember
     * @param sendGoodbye
     * @param sendNotify
     * @return
     */
    boolean listUnsubscribe(String apiKey, String listId, String email, boolean deleteMember, boolean sendGoodbye,
            boolean sendNotify);

    /**
     * Updates a list member. The member must be in the list, in status
     * "subscribed", otherwise an exception is thrown.
     * 
     * @param apiKey
     * @param id
     * @param emailAddress
     * @param mergeVars
     * @param emailType
     * @param replaceInterests
     * @return @ throws MailChimpServiceException When the email is not found in
     *         the list or the user is unsubscribed from the list. The message
     *         will be like "The email address "me@example.com" does not belong
     *         to this list" .
     */
    boolean listUpdateMember(String apiKey, String id, String emailAddress, Map<String, String> mergeVars,
            String emailType, boolean replaceInterests);

    /**
     * Returns the information on a single subscriber in the list. The returned
     * map contains all the subscriber details. See MEMBER_INFO_ constants for a
     * list of fields.
     * 
     * Note that all fields are strings, except for the {@code
     * MEMBER_INFO_MERGES} which is a {@code Map<String, String>} and for lists
     * which is an array of objects (currently, it seems to be empty).
     * 
     * @param apiKey
     * @param listId
     * @param emailAddress
     * @return Member details in a Map.
     * @throws MailChimpServiceException when the email is not found in the
     *             list. The message will be like "me@example.com is not a
     *             member of My List" or "There is no record of "me@example.com"
     *             in the database".
     */
    Map<String, Object> listMemberInfo(String apiKey, String listId, String emailAddress);

    static final String MEMBER_INFO_EMAIL_TYPE = "email_type";

    static final String MEMBER_INFO_IP_SIGNUP = "ip_signup";

    static final String MEMBER_INFO_IP_OPT = "ip_opt";

    static final String MEMBER_INFO_MERGES = "merges";

    static final String MEMBER_INFO_EMAIL = "email";

    static final String MEMBER_INFO_TIMESTAMP = "timestamp";

    static final String MEMBER_INFO_STATUS = "status";

    static final String MEMBER_INFO_ID = "id";

    static final String MEMBER_INFO_LISTS = "lists";

    /**
     * Create a new draft campaign to send
     * 
     * @param apikey
     * @param type see CAMPAIGN_TYPE_ constants
     * @param optionssee CAMPAIGN_OPTION_ constants
     * @param content see CAMPAIGN_CONTENT_ constants
     * @param segmentOpts optional. For list segmentation.
     * @param typeOpts optional. Extra options for RSS campaign or A/B split
     *            campaigns.
     * @return
     */
    String campaignCreate(String apikey, String type, Map<String, Object> options, Map<String, String> content,
            Map<String, String> segmentOpts, Map<String, String> typeOpts);

    static final String CAMPAIGN_TYPE_REGULAR = "regular";

    static final String CAMPAIGN_TYPE_PLAINTEXT = "plaintext";

    static final String CAMPAIGN_TYPE_ABSPLIT = "absplit";

    static final String CAMPAIGN_TYPE_RSS = "rss";

    static final String CAMPAIGN_TYPE_TRANS = "trans";

    static final String CAMPAIGN_OPTION_LIST_ID = "list_id";

    static final String CAMPAIGN_OPTION_SUBJECT = "subject";

    static final String CAMPAIGN_OPTION_FROM_EMAIL = "from_email";

    static final String CAMPAIGN_OPTION_FROM_NAME = "from_name";

    static final String CAMPAIGN_OPTION_TEMPLATE_ID = "template_id";

    static final String CAMPAIGN_OPTION_FOLDER_ID = "folder_id";

    static final String CAMPAIGN_OPTION_TRACKING = "tracking";

    static final String CAMPAIGN_OPTION_TITLE = "title";

    static final String CAMPAIGN_OPTION_AUTHENTICATE = "authenticate";

    static final String CAMPAIGN_OPTION_ANALYTICS = "analytics";

    static final String CAMPAIGN_OPTION_ANALYTICS_GOOGLE = "google";

    static final String CAMPAIGN_OPTION_INLINE_CSS = "inline_css";

    static final String CAMPAIGN_OPTION_GENERATE_TEXT = "generate_text";

    static final String CAMPAIGN_CONTENT_HTML = "html";

    static final String CAMPAIGN_CONTENT_TEXT = "text";

    static final String CAMPAIGN_CONTENT_URL = "url";

    static final String CAMPAIGN_CONTENT_HTML_HEADER = "html_HEADER";

    static final String CAMPAIGN_CONTENT_HTML_MAIN = "html_MAIN";

    static final String CAMPAIGN_CONTENT_HTML_SIDECOLUMN = "html_SIDECOLUMN";

    static final String CAMPAIGN_CONTENT_HTML_FOOTER = "html_FOOTER";

    /**
     * Retrieve all templates defined for your user account
     * 
     * @param apikey
     * @return Array of {@code Map<String, String>}
     */
    Object[] campaignTemplates(String apikey);

    /**
     * Send a given campaign immediately
     * 
     * @param apikey
     * @param cid Campaign ID (returned from {@link campaignCreate}
     * @return
     */
    boolean campaignSendNow(String apikey, String cid);
}
