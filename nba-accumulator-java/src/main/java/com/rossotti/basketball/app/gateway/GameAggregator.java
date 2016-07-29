package com.rossotti.basketball.app.gateway;

import java.util.ArrayList;
import java.util.List;

import com.rossotti.basketball.dao.model.Game;

public class GameAggregator {
	private static List<Game> games = new ArrayList<Game>();
	public List<Game> add(Game game) {
		games.add(game);
		return games;
	}
}