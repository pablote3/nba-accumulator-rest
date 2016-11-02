package com.rossotti.basketball.app.integration;

import com.rossotti.basketball.dao.model.AppStandings;

public interface GatewayService {
	AppStandings processGames(ServiceProperties serviceProperties);
}