package com.rossotti.basketball.app.integration;

import com.rossotti.basketball.dao.model.AppGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameResultsRouter {
	private final Logger logger = LoggerFactory.getLogger(GameResultsRouter.class);
	public String routeGame(AppGame appGame) {
		if (appGame.isAppRosterUpdate()) {
			logger.info("Game " + appGame.getGame().getBoxScoreAway().getTeam().getAbbr() + " at " + 
						appGame.getGame().getBoxScoreHome().getTeam().getAbbr() + " " + 
						appGame.getAppStatus() + 
						": route to gameRouterChannel"
			);
			return "gameRouterChannel";
		}
		else {
			logger.info("Game " + appGame.getGame().getBoxScoreAway().getTeam().getAbbr() + " at " + 
						appGame.getGame().getBoxScoreHome().getTeam().getAbbr() + " " + 
						appGame.getAppStatus() + 
						": route to gameAggregatorChannel"
			);			
			return "gameAggregatorChannel";
		}
	}
}