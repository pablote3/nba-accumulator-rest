package com.rossotti.basketball.app.gateway;

import org.springframework.integration.aggregator.ReleaseStrategy;
import org.springframework.integration.store.MessageGroup;
import org.springframework.messaging.Message;

public class GameReleaseStrategy implements ReleaseStrategy {
	@Override
	public boolean canRelease(MessageGroup messageGroup) {
		for (Message<?> msg : messageGroup.getMessages()) {
			if ((boolean)msg.getHeaders().get(GameCorrelationStrategy.LAST_KEY)) {
				return true;
			}
		}
		return false;
	}
}