package com.rossotti.basketball.app.gateway;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rossotti.basketball.app.service.GameService;
import com.rossotti.basketball.dao.model.GameDay;
import com.rossotti.basketball.util.DateTimeUtil;

public class GameActivator {
	@Autowired
	private GameService gameService;
	private final Logger logger = LoggerFactory.getLogger(GameActivator.class);
	public GameDay processGames(String stringDate) {
		logger.info("begin processGames");
//		String gameTeam = properties.getGameTeam();
//		LocalDate gameDate = DateTimeUtil.getLocalDate(properties.getGameDate());
		LocalDate gameDate = DateTimeUtil.getLocalDate(stringDate);
		GameDay games;
//		if (gameTeam == null || gameTeam.isEmpty()) {
			games = gameService.findByDate(gameDate);
//		}
//		else {
//			games = new ArrayList<Game>();
//			games.add(gameService.findByDateTeam(gameDate, gameTeam));
//		}
		logger.info("end processGames");
		return games;
	}
}