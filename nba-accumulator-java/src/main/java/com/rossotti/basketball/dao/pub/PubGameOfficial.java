package com.rossotti.basketball.dao.pub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PubGameOfficial {
	private final PubOfficial official;

	@JsonCreator
	public PubGameOfficial(@JsonProperty("official") PubOfficial official) {
		this.official = official;
	}

	public PubOfficial getOfficial() {
		return official;
	}
}
