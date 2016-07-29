package com.rossotti.basketball.app.gateway;

import org.springframework.integration.annotation.Gateway;

import com.rossotti.basketball.dao.model.Game;

public interface GameGateway {
	@Gateway(requestChannel = "gatewayChannel")
	Game processGames(String gameDate);
}