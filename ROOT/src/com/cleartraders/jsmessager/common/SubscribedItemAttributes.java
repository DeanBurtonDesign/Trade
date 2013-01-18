package com.cleartraders.jsmessager.common;

/**
 * Subscribed Item Attributes for "Poker Game Demo"
 * @author  Peter at PowerTeam
 *
 */
public class SubscribedItemAttributes {
	//this flag indicates whether the snapshot has already been sent
	//(on start is obviously false)
	public volatile boolean isSnapshotSent = false;
	//this handlerId represents the key for the itemHandle object
	//related to this item inside the handles map
	public String handleId;
	//the item name
	public String itemName;	
	
	public SubscribedItemAttributes(String itemName, String handleId) {
		this.itemName = itemName;
		this.handleId = handleId;
	}
}
