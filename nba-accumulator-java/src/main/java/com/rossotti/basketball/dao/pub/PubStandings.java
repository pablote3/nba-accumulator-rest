package com.rossotti.basketball.dao.pub;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PubStandings {
	private final URI self;
	private final List<PubStanding> standings = new ArrayList<>();

	@JsonCreator
	public PubStandings(@JsonProperty("self") URI self, 
					@JsonProperty("standings") List<PubStanding> standings) {
		this.self = self;
		if (standings != null) {
			this.standings.addAll(standings);
		}
	}

	public URI getSelf() {
		return self;
	}
	public List<PubStanding> getStandings() {
		return standings;
	}
}
