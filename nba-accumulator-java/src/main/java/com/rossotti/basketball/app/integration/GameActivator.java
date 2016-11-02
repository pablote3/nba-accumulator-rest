package com.rossotti.basketball.app.integration;

import com.rossotti.basketball.app.service.GameService;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.util.DateTimeUtil;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class GameActivator {
	private final GameService gameService;
	private final Logger logger = LoggerFactory.getLogger(GameActivator.class);

	@Autowired
	public GameActivator(GameService gameService) {
		this.gameService = gameService;
	}

	public List<Game> processGames(ServiceProperties properties) {
		List<Game> games = new ArrayList<Game>();
		LocalDate gameDate = DateTimeUtil.getLocalDate(properties.getGameDate());
		if (properties.getGameTeam() == null || properties.getGameTeam().isEmpty()) {
			games = gameService.findByDate(gameDate);
			logger.info("findByDate: " + DateTimeUtil.getStringDate(gameDate));
		}
		else {
			Game game = gameService.findByDateTeam(gameDate, properties.getGameTeam());
			if (game.isFound()) {
				games.add(game);
			}
			logger.info("findByDateTeam: " + DateTimeUtil.getStringDate(gameDate) + " - " + properties.getGameTeam());
		}
		return games;
	}
}