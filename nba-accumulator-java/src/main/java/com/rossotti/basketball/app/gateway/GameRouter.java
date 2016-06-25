package com.rossotti.basketball.app.gateway;

import com.rossotti.basketball.dao.model.Game;

public class GameRouter {
	public String routeGame(Game game) {
		System.out.println("begin gameRouter");
		return (game.isScheduled()) ? "scheduledRouteChannel" : "completedRouteChannel";
	}
}