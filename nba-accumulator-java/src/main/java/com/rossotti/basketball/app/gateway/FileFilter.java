package com.rossotti.basketball.app.gateway;

import java.io.File;

import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;

public class FileFilter implements MessageSelector {

	public boolean accept(Message<?> message) {
		if (message.getPayload() instanceof File && ((File) message.getPayload()).getName().startsWith("msg")) {
			return true;
		}
		return false;
	}

}