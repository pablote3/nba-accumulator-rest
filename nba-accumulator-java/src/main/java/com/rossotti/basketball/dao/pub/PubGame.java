package com.rossotti.basketball.dao.pub;

import java.net.URI;
import java.util.List;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rossotti.basketball.dao.model.GameStatus;
import com.rossotti.basketball.dao.model.Game.SeasonType;
import com.rossotti.basketball.util.DateTimeUtil;

public class PubGame {

	private final URI self;
	private final String gameDateTime;
	private final String status;
	private final String seasonType;
	private final List<PubGameOfficial> gameOfficials;
	private final List<PubBoxScore> boxScores;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final URI score;

	@JsonCreator
	//completed game
	public PubGame(@JsonProperty("self") URI self,
					@JsonProperty("gameDateTime") LocalDateTime gameDateTime,
					@JsonProperty("status") GameStatus status,
					@JsonProperty("seasonType") SeasonType seasonType,
					@JsonProperty("gameOfficials") List<PubGameOfficial> gameOfficials,
					@JsonProperty("boxScores") List<PubBoxScore> boxScores) {
		this.self = self;
		this.score = null;
		this.gameDateTime = DateTimeUtil.getStringDateTime(gameDateTime);
		this.status = status.name();
		this.seasonType = seasonType.name();
		this.gameOfficials = gameOfficials;
		this.boxScores = boxScores;
	}
	
	//scheduled game
	public PubGame(@JsonProperty("self") URI self,
			@JsonProperty("score") URI score,
			@JsonProperty("gameDateTime") LocalDateTime gameDateTime,
			@JsonProperty("status") GameStatus status,
			@JsonProperty("seasonType") SeasonType seasonType,
			@JsonProperty("boxScores") List<PubBoxScore> boxScores) {
		this.self = self;
		this.score = score;
		this.gameDateTime = DateTimeUtil.getStringDateTime(gameDateTime);
		this.status = status.name();
		this.seasonType = seasonType.name();
		this.gameOfficials = null;
		this.boxScores = boxScores;
	}

	public URI getSelf() {
		return self;
	}
	public URI getScore() {
		return score;
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