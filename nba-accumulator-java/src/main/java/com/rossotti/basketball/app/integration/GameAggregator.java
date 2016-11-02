package com.rossotti.basketball.app.integration;

import com.rossotti.basketball.dao.model.AppGame;
import com.rossotti.basketball.dao.model.AppStatus;
import com.rossotti.basketball.dao.model.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameAggregator {
	private final Logger logger = LoggerFactory.getLogger(GameAggregator.class);
	public List<Game> aggregate(Collection<Message<?>> games) {
		logger.debug("begin gameAggregator");
		List<Game> gameList = new ArrayList<Game>();
		for (Message<?> msg : games) {
			logger.debug("msg.correlationId = " + msg.getHeaders().get("correlationId"));
			logger.debug("msg.sequenceNumber = " + msg.getHeaders().get("sequenceNumber"));
			logger.debug("msg.sequenceSize = " + msg.getHeaders().get("sequenceSize"));			
			AppGame appGame = (AppGame)msg.getPayload();
			if (appGame.getAppStatus().equals(AppStatus.ServerError) || appGame.getAppStatus().equals(AppStatus.ClientError)) {
				gameList.add(null);
			}
			else {
				logger.info(msg.getHeaders().get("sequenceNumber") + " of " +
							msg.getHeaders().get("sequenceSize") + "  " +
							appGame.getGame().getBoxScoreAway().getTeam().getAbbr() + " at " +
							appGame.getGame().getBoxScoreHome().getTeam().getAbbr() + ": " +
							appGame.getAppStatus()
				);
				gameList.add(appGame.getGame());
			}
		}
		logger.debug("end gameAggregator");
		return gameList;
	}
}