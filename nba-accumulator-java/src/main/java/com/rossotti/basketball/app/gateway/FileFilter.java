package com.rossotti.basketball.app.gateway;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;

public class FileFilter implements MessageSelector {

	public boolean accept(Message<?> message) {
		Properties prop = new Properties();

		if (message.getPayload() instanceof File) {
			try {
				FileInputStream fis = new FileInputStream((File) message.getPayload());
				prop.load(fis);
				String gameDate = prop.getProperty("game.date");
				System.out.println("gameDate = " + gameDate);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		else {
			return false;
		}
	}
}