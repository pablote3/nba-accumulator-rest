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
import com.rossotti.basketball.app.resource.StandingAppResource;
import com.rossotti.basketball.app.resource.RosterPlayerAppResource;
import com.rossotti.basketball.app.resource.GameAppResource;
import com.rossotti.basketball.dao.resource.GameDaoResource;
import com.rossotti.basketball.dao.resource.OfficialDaoResource;
import com.rossotti.basketball.dao.resource.PlayerDaoResource;
import com.rossotti.basketball.dao.resource.RosterPlayerDaoResource;
import com.rossotti.basketball.dao.resource.StandingDaoResource;
import com.rossotti.basketball.dao.resource.TeamDaoResource;

public class AccumulatorApp extends Application {
	protected final Set<Class<?>> classes = new HashSet<>();
	private Set<Object> singletons = new HashSet<>();
	protected final Map<String, Object> properties = new HashMap<>();

	public AccumulatorApp() {
		registerClasses();
	}

	private void registerClasses() {
		// Resources
		classes.add(TeamDaoResource.class);
		classes.add(OfficialDaoResource.class);
		classes.add(PlayerDaoResource.class);
		classes.add(RosterPlayerDaoResource.class);
		classes.add(GameDaoResource.class);
		classes.add(StandingDaoResource.class);
		
		classes.add(GameAppResource.class);
		classes.add(RosterPlayerAppResource.class);
		classes.add(StandingAppResource.class);

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
