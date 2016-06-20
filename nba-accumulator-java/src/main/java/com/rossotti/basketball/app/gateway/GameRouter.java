package com.rossotti.basketball.app.gateway;

import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.GameStatus;

public class GameRouter {
	public String routeGame(Game game) {
		System.out.println("begin gameRouter");
		return (game.getStatus() == GameStatus.Scheduled) ? "scheduledChannelRoute" : "completedChannelRoute";
	}
}