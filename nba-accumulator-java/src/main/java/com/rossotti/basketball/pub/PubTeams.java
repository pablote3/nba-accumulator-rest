package com.rossotti.basketball.pub;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PubTeams {

	private final URI self;
	
	private final List<PubTeam> teams = new ArrayList<>();

	@JsonCreator
	public PubTeams(@JsonProperty("self") URI self, 
					@JsonProperty("teams") List<PubTeam> teams) {
		this.self = self;

		if (teams != null) {
			this.teams.addAll(teams);
		}
	}

	public URI getSelf() {
		return self;
	}

	public List<PubTeam> getTeams() {
		return teams;
	}
}
