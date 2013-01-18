package com.cleartraders.jsmessager.common;

import javax.jms.ExceptionListener;
import javax.jms.MessageListener;

/**
 * combine MessageListener and ExceptionListener in one interface for "Poker Game Demo"
 * @author  Peter at PowerTeam
 *
 */
public interface ExtendedMessageListener extends MessageListener, ExceptionListener {
}
