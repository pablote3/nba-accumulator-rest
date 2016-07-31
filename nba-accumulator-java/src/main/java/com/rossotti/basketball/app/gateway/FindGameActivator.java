package com.rossotti.basketball.app.gateway;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;

import com.rossotti.basketball.app.service.GameService;
import com.rossotti.basketball.dao.model.GameDay;
import com.rossotti.basketball.util.DateTimeUtil;

public class FindGameActivator {
	@Autowired
	private GameService gameService;

	public GameDay processGames(String stringDate) {
		System.out.println("begin processGames");
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
		System.out.println("end processGames");
		return games;
	}
}