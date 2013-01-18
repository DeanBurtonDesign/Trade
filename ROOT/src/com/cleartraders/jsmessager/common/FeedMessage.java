package com.cleartraders.jsmessager.common;

import java.io.Serializable;
import java.util.HashMap;

/**
 * A message published by Generator and received from Adapter for "Poker Game Demo"
 * @author  Peter at PowerTeam
 *
 */
public class FeedMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	//the item name
	public String itemName = null;
	//an HashMap containing the updates for the item (the field names are the keys) 
	public HashMap currentValues = null;
	//indicate if the map carries the entire snapshot for the item
	public boolean isSnapshot = false;
	//the id related to the handle of this item
	public String handleId = null;
	//the id related to this generator's life
	public int random;
	
	public FeedMessage(String itemName, final HashMap currentValues, boolean isSnapshot, String handleId, int random) {
		this.itemName = itemName;
		this.currentValues = currentValues; 
		this.isSnapshot = isSnapshot;
		this.handleId = handleId;
		this.random = random;
	}
	
}
