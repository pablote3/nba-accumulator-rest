package com.rossotti.basketball.app.gateway;

public class FindGameActivator {
	public ServiceProperties findGames(ServiceProperties properties) {
		System.out.println("Processing order");
		String gameDate = properties.getGameDate();
		System.out.println("GameDate:  " + gameDate);
		return properties;
	}
}