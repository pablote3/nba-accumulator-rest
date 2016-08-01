package com.rossotti.basketball.app.gateway;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.GameDay;

public class GameSplitter extends AbstractMessageSplitter {
	private final Logger logger = LoggerFactory.getLogger(GameSplitter.class);
	private Map<String, List<Message<?>>> splittedMessages = new HashMap<String, List<Message<?>>>();

	@Override
	protected Object splitMessage(Message<?> message) { 
		Collection<Message<?>> messages = new ArrayList<Message<?>>();
		GameDay games = (GameDay) message.getPayload();
		logger.info("begin gameSplitter: game count = " + games.getGames().size());
		Iterator<?> iterator = games.getGames().iterator();
		while (iterator.hasNext()) {
			Game game = (Game) iterator.next();
			Message<?> msg = MessageBuilder.withPayload(game)
				.setHeaderIfAbsent(GameCorrelationStrategy.CORRELATION_KEY, games.getGameDate())
				.setHeaderIfAbsent(GameCorrelationStrategy.LAST_KEY, !iterator.hasNext())
				.build();
			messages.add(msg);
			addMessage(""+ games.getGameDate(), msg);
		}
		logger.info("end gameSplitter");
		return messages;
	}

	public Map<String, List<Message<?>>> getSplittedMessages() {
		return this.splittedMessages;
	}

	public List<Message<?>> getSplittedMessagesByKey(String key) {
		if (!getSplittedMessages().containsKey(key)) {
			addListOfSplittedMessages(key);
		}
		return getSplittedMessages().get(key);
	}

	private void addMessage(String key, Message<?> message) {
		getSplittedMessagesByKey(key).add(message);
	}

	private void addListOfSplittedMessages(String key) {
		getSplittedMessages().put(key, (new ArrayList<Message<?>>()));
	}
}