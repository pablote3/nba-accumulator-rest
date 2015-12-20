package com.rossotti.basketball.pub;

import java.net.URI;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rossotti.basketball.models.RosterPlayer.Position;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PubRosterPlayer {

	private final URI self;
	private final String fromDate;
	private final String toDate;
	private final String position;
	private final String number;
	private final PubTeam team;
	private final PubPlayer player;

	@JsonCreator
	public PubRosterPlayer(@JsonProperty("self") URI self,
					@JsonProperty("fromDate") LocalDate fromDate,
					@JsonProperty("fromDate") LocalDate toDate,
					@JsonProperty("position") Position position,
					@JsonProperty("number") String number,
					@JsonProperty("player") PubPlayer player,
					@JsonProperty("team") PubTeam team) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		this.self = self;
		this.fromDate = fromDate.toString(fmt);
		this.toDate = fromDate.toString(fmt);
		this.position = position.name();
		this.number = number;
		this.player = player;
		this.team = team;
	}

	public URI getSelf() {
		return self;
	}
	public String getFromDate() {
		return fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public String getPosition() {
		return position;
	}
	public String getNumber() {
		return number;
	}
	public PubTeam getTeam() {
		return team;
	}
	public PubPlayer getPlayer() {
		return player;
	}
}