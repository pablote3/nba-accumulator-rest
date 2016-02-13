package com.rossotti.basketball.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourceLoader {
	private static Properties properties = new Properties();
	private static InputStream input = null;

	static {
		try {
			input = ClassLoader.getSystemResourceAsStream("service.properties");
			properties.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	public static Properties getProperties() {
		return properties;
	}
}