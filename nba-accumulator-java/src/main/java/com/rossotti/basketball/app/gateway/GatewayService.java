package com.rossotti.basketball.app.gateway;

import org.springframework.integration.annotation.Gateway;

import com.rossotti.basketball.dao.model.Game;

public interface GatewayService {
	@Gateway(requestChannel = "gatewayRequestChannel")
	Game processGames(String gameDate);
//	public void score(String gameDate);
}