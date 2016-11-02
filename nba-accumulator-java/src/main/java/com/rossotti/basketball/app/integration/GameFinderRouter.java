package com.rossotti.basketball.app.integration;

import com.rossotti.basketball.dao.model.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GameFinderRouter {
	private final Logger logger = LoggerFactory.getLogger(GameFinderRouter.class);
	public String routeGame(List<Game> games) {
		if (games.size() > 0) {
			logger.info("gameCount: " + games.size() + ": route to gameSplitterChannel");
			return "gameSplitterChannel";
		}
		else {
			logger.info("gameCount: " + games.size() + ": route to outputChannel");			
			return "outputChannel";
		}
	}
}