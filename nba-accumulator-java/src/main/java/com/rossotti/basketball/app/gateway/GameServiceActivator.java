package com.rossotti.basketball.app.gateway;

import org.springframework.messaging.Message;

public class GameServiceActivator {

	public Message<ServiceProperties> printMessage(Message<ServiceProperties> properties) {
		System.out.println("Processing order");
		String gameDate = properties.getPayload().getGameDate();
		System.out.println("GameDate:  " + gameDate);
		return properties;
	}

}