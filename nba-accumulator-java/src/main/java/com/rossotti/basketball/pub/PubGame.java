package com.rossotti.basketball.pub;

import java.net.URI;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rossotti.basketball.model.Game.SeasonType;
import com.rossotti.basketball.model.Game.Status;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PubGame {

	private final URI self;
	private final String date;
	private final String status;
	private final String seasonType;
	private final List<PubBoxScore> boxScores;

	@JsonCreator
	public PubGame(@JsonProperty("self") URI self,
					@JsonProperty("date") LocalDateTime date,
					@JsonProperty("status") Status status,
					@JsonProperty("seasonType") SeasonType seasonType,
					@JsonProperty("boxScores") List<PubBoxScore> boxScores) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
		this.self = self;
		this.date = date.toString(fmt);
		this.status = status.name();
		this.seasonType = seasonType.name();
		this.boxScores = boxScores;
	}

	public URI getSelf() {
		return self;
	}
	public String getDate() {
		return date;
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