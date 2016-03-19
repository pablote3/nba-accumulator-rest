package com.rossotti.basketball.app;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.rossotti.basketball.app.provider.DuplicateEntityExceptionMapper;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.app.provider.NoSuchEntityExceptionMapper;
import com.rossotti.basketball.app.provider.PropertyExceptionMapper;
import com.rossotti.basketball.client.resource.ScoreResource;
import com.rossotti.basketball.dao.resource.GameResource;
import com.rossotti.basketball.dao.resource.OfficialResource;
import com.rossotti.basketball.dao.resource.PlayerResource;
import com.rossotti.basketball.dao.resource.RosterPlayerResource;
import com.rossotti.basketball.dao.resource.StandingResource;
import com.rossotti.basketball.dao.resource.TeamResource;

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
		classes.add(OfficialResource.class);
		classes.add(PlayerResource.class);
		classes.add(RosterPlayerResource.class);
		classes.add(GameResource.class);
		classes.add(StandingResource.class);
		classes.add(ScoreResource.class);

		// Providers
		classes.add(JsonProvider.class);
	
		// Exception Mappers
		classes.add(PropertyExceptionMapper.class);
		classes.add(NoSuchEntityExceptionMapper.class);
		classes.add(DuplicateEntityExceptionMapper.class);
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
