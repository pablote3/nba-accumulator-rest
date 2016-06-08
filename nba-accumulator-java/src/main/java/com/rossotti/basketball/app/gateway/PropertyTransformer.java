package com.rossotti.basketball.app.gateway;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class PropertyTransformer {
	public Message<ServiceProperties> transform(Message<File> message) {
		Properties properties = new Properties();
		ServiceProperties serviceProperties = new ServiceProperties();
		FileInputStream fis;
		try {
			fis = new FileInputStream((File) message.getPayload());
			properties.load(fis);
			serviceProperties.setGameDate(properties.getProperty("game.date"));
			serviceProperties.setTeam(properties.getProperty("game.team"));
			System.out.println("Game Date = " + serviceProperties.getGameDate());
			fis.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return MessageBuilder.withPayload(serviceProperties).build();
	}
}