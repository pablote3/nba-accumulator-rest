package com.rossotti.basketball.pub;

import java.net.URI;
import java.util.List;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rossotti.basketball.model.Game.SeasonType;
import com.rossotti.basketball.model.Game.Status;
import com.rossotti.basketball.util.DateTimeUtil;

public class PubGame {

	private final URI self;
	private final String gameDateTime;
	private final String status;
	private final String seasonType;
	private final List<PubGameOfficial> gameOfficials;
	private final List<PubBoxScore> boxScores;

	@JsonCreator
	public PubGame(@JsonProperty("self") URI self,
					@JsonProperty("gameDateTime") LocalDateTime gameDateTime,
					@JsonProperty("status") Status status,
					@JsonProperty("seasonType") SeasonType seasonType,
					@JsonProperty("gameOfficials") List<PubGameOfficial> gameOfficials,
					@JsonProperty("boxScores") List<PubBoxScore> boxScores) {
		this.self = self;
		this.gameDateTime = DateTimeUtil.getStringDateTime(gameDateTime);
		this.status = status.name();
		this.seasonType = seasonType.name();
		this.gameOfficials = gameOfficials;
		this.boxScores = boxScores;
	}

	public URI getSelf() {
		return self;
	}
	public String getGameDateTime() {
		return gameDateTime;
	}
	public String getStatus() {
		return status;
	}
	public String getSeasonType() {
		return seasonType;
	}
	public List<PubGameOfficial> getGameOfficials() {
		return gameOfficials;
	}
	public List<PubBoxScore> getBoxScores() {
		return boxScores;
	}
}