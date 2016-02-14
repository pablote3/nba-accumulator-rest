package com.rossotti.basketball.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourceLoader {

	private static ResourceLoader instance = new ResourceLoader();
	private static Properties properties = new Properties();

	private ResourceLoader(){}

	public static ResourceLoader getInstance(){
		return instance;
	}

	static {
		InputStream input = null;
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

	public Properties getProperties() {
		return properties;
	}
}