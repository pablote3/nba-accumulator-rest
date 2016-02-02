package com.rossotti.basketball.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

public class PropertyResourceLoader implements ResourceLoaderAware {
	private ResourceLoader resourceLoader;
	private String resource;
	private Properties properties = new Properties();
	private String agentToken;
	private String userAgent;

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Autowired
	public void setResource(String resource) {
		this.resource = resource;
	}

	public Properties getProperties() throws IOException {
		InputStream inputStream = resourceLoader.getResource(resource).getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		properties.load(reader);
		reader.close();
		inputStream.close();
		return properties;
	}
}