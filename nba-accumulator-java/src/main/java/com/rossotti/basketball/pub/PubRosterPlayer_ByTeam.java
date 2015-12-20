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
public class PubRosterPlayer_ByTeam {

	private final URI self;
	private final String fromDate;
	private final String toDate;
	private final String position;
	private final String number;
	private final PubPlayer player;

	@JsonCreator
	public PubRosterPlayer_ByTeam(@JsonProperty("self") URI self,
					@JsonProperty("fromDate") LocalDate fromDate,
					@JsonProperty("toDate") LocalDate toDate,
					@JsonProperty("position") Position position,
					@JsonProperty("number") String number,
					@JsonProperty("player") PubPlayer player) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		this.self = self;
		this.fromDate = fromDate.toString(fmt);
		this.toDate = toDate.toString(fmt);
		this.position = position.name();
		this.number = number;
		this.player = player;
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
	public PubPlayer getPlayer() {
		return player;
	}
}