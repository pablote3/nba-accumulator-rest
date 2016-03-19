package com.rossotti.basketball.dao.pub;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PubPlayers {
	private final URI self;
	private final List<PubPlayer> players = new ArrayList<>();

	@JsonCreator
	public PubPlayers(@JsonProperty("self") URI self, 
					@JsonProperty("players") List<PubPlayer> players) {
		this.self = self;
		if (players != null) {
			this.players.addAll(players);
		}
	}

	public URI getSelf() {
		return self;
	}
	public List<PubPlayer> getPlayers() {
		return players;
	}
}
