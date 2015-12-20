package com.rossotti.basketball.pub;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PubRosterPlayers {

	private final URI self;
	
	private final List<PubRosterPlayer> rosterPlayers = new ArrayList<>();

	@JsonCreator
	public PubRosterPlayers(@JsonProperty("self") URI self, 
					@JsonProperty("rosterPlayers") List<PubRosterPlayer> rosterPlayers) {
		this.self = self;

		if (rosterPlayers != null) {
			this.rosterPlayers.addAll(rosterPlayers);
		}
	}

	public URI getSelf() {
		return self;
	}

	public List<PubRosterPlayer> getRosterPlayers() {
		return rosterPlayers;
	}
}