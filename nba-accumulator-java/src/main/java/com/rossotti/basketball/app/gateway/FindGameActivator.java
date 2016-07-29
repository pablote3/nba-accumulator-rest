package com.rossotti.basketball.app.gateway;

import java.util.ArrayList;
import java.util.List;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import com.rossotti.basketball.app.service.GameService;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.util.DateTimeUtil;

public class FindGameActivator {
	@Autowired
	private GameService gameService;

	public List<Game> processGames(String stringDate) {
		System.out.println("begin processGames");
//		String gameTeam = properties.getGameTeam();
//		LocalDate gameDate = DateTimeUtil.getLocalDate(properties.getGameDate());
		LocalDate gameDate = DateTimeUtil.getLocalDate(stringDate);
		List<Game> games;
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