package com.rossotti.basketball.app.gateway;

import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.messaging.Message;

public class GameSplitter extends AbstractMessageSplitter {
	@Override
	protected Object splitMessage(Message<?> message) { 
		return message.getPayload();
	}
}