package com.rossotti.basketball.app;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.rossotti.basketball.app.providers.NoSuchEntityExceptionMapper;
import com.rossotti.basketball.app.providers.JsonProvider;
import com.rossotti.basketball.app.resources.TeamResource;

public class AccumulatorApp extends Application {
	protected final Set<Class<?>> classes = new HashSet<>();
	private Set<Object> singletons = new HashSet<>();
	protected final Map<String, Object> properties = new HashMap<>();

	public AccumulatorApp() {
		registerClasses();
	}

	private void registerClasses() {
		// Resources
		classes.add(TeamResource.class);

		// Providers
		classes.add(JsonProvider.class);
	
		// Exception Mappers
		classes.add(NoSuchEntityExceptionMapper.class);
	}

	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
