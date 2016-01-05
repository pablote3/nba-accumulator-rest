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
	private final String dateTime;
	private final String status;
	private final String seasonType;
	private final List<PubBoxScore> boxScores;

	@JsonCreator
	public PubGame(@JsonProperty("self") URI self,
					@JsonProperty("dateTime") LocalDateTime dateTime,
					@JsonProperty("status") Status status,
					@JsonProperty("seasonType") SeasonType seasonType,
					@JsonProperty("boxScores") List<PubBoxScore> boxScores) {
		this.self = self;
		this.dateTime = DateTimeUtil.getStringDateTime(dateTime);
		this.status = status.name();
		this.seasonType = seasonType.name();
		this.boxScores = boxScores;
	}

	public URI getSelf() {
		return self;
	}
	public String getDateTime() {
		return dateTime;
	}
	public String getStatus() {
		return status;
	}
	public String getSeasonType() {
		return seasonType;
	}
	public List<PubBoxScore> getBoxScores() {
		return boxScores;
	}
}