package com.rossotti.basketball.dao.pub;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PubRosterPlayers_ByTeam {
	private final URI self;
	private final PubTeam team;
	private final List<PubRosterPlayer_ByTeam> rosterPlayers = new ArrayList<>();

	@JsonCreator
	public PubRosterPlayers_ByTeam(@JsonProperty("self") URI self, 
									@JsonProperty("team") PubTeam team, 
									@JsonProperty("rosterPlayers") List<PubRosterPlayer_ByTeam> rosterPlayers) {
		this.self = self;
		this.team = team;
		if (rosterPlayers != null) {
			this.rosterPlayers.addAll(rosterPlayers);
		}
	}

	public URI getSelf() {
		return self;
	}
	public PubTeam getTeam() {
		return team;
	}
	public List<PubRosterPlayer_ByTeam> getRosterPlayers() {
		return rosterPlayers;
	}
}
