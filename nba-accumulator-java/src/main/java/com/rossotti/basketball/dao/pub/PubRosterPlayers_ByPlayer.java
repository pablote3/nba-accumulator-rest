package com.rossotti.basketball.dao.pub;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PubRosterPlayers_ByPlayer {
	private final URI self;
	private final PubPlayer player;
	private final List<PubRosterPlayer_ByPlayer> rosterPlayers = new ArrayList<>();

	@JsonCreator
	public PubRosterPlayers_ByPlayer(@JsonProperty("self") URI self, 
									@JsonProperty("player") PubPlayer player, 
									@JsonProperty("rosterPlayers") List<PubRosterPlayer_ByPlayer> rosterPlayers) {
		this.self = self;
		this.player = player;
		if (rosterPlayers != null) {
			this.rosterPlayers.addAll(rosterPlayers);
		}
	}

	public URI getSelf() {
		return self;
	}
	public PubPlayer getPlayer() {
		return player;
	}
	public List<PubRosterPlayer_ByPlayer> getRosterPlayers() {
		return rosterPlayers;
	}
}
