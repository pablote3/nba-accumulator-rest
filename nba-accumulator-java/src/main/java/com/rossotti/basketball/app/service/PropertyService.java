package com.rossotti.basketball.app.service;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.app.exception.PropertyException;
import com.rossotti.basketball.app.resource.ClientSource;

@Service
@Configuration
@PropertySource("service.properties")
public class PropertyService {
	@Autowired
	private Environment env;

	public String getProperty_String(String propertyName) {
		String property = env.getProperty(propertyName);
		if (StringUtils.isEmpty(property)) {
			throw new PropertyException(propertyName);
		}
		return property;
	}

	public String getProperty_Http(String propertyName) {
		String http = getProperty_String(propertyName);
		if (!StringUtils.startsWith(http, "https://")) {
			throw new PropertyException(propertyName);
		}
		return http;
	}

	public String getProperty_Path(String propertyName) {
		String path = getProperty_String(propertyName);
		if (!new File(path).exists()) {
			throw new PropertyException(propertyName);
		}
		return path;
	}

	public ClientSource getProperty_ClientSource(String propertyName) {
		String property = getProperty_String(propertyName);
		ClientSource clientSource = null;
		try {
			clientSource = ClientSource.valueOf(property);
		} catch (IllegalArgumentException e) {
			throw new PropertyException(propertyName);
		}
		return clientSource;
	}
}
