package com.rossotti.basketball.dao.pub;

import java.net.URI;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rossotti.basketball.dao.model.Position;
import com.rossotti.basketball.util.DateTimeUtil;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PubRosterPlayer_ByPlayer {
	private final URI self;
	private final String fromDate;
	private final String toDate;
	private final String position;
	private final String number;
	private final PubTeam team;

	@JsonCreator
	public PubRosterPlayer_ByPlayer(@JsonProperty("self") URI self,
					@JsonProperty("fromDate") LocalDate fromDate,
					@JsonProperty("toDate") LocalDate toDate,
					@JsonProperty("position") Position position,
					@JsonProperty("number") String number,
					@JsonProperty("team") PubTeam team) {
		this.self = self;
		this.fromDate = DateTimeUtil.getStringDate(fromDate);
		this.toDate = DateTimeUtil.getStringDate(toDate);
		this.position = position.name();
		this.number = number;
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
}
