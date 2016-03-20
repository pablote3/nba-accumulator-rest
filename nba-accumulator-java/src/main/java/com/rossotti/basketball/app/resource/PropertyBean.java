package com.rossotti.basketball.app.resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.app.exception.PropertyException;

@Service

@Configuration
@PropertySource("service.properties")
public class PropertyBean {
	@Autowired
	private Environment env;

	public String getProperty(String propertyName) {
		String property = env.getProperty(propertyName);
		if (StringUtils.isEmpty(property)) {
			throw new PropertyException(propertyName);
		}
		return property;
	}
}
