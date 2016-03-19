package com.rossotti.basketball.dao.pub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rossotti.basketball.dao.model.BoxScorePlayer.Position;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PubBoxScorePlayer {
	private final String position;
	private final String starter;
	private final String minutes;
	private final String points;
	private final String assists;
	private final String turnovers;
	private final String steals;
	private final String blocks;
	private final String fieldGoalAttempts;
	private final String fieldGoalMade;
	private final String fieldGoalPercent;
	private final String threePointAttempts;
	private final String threePointMade;
	private final String threePointPercent;
	private final String freeThrowAttempts;
	private final String freeThrowMade;
	private final String freeThrowPercent;
	private final String reboundsOffense;
	private final String reboundsDefense;
	private final String personalFouls;
	private final PubRosterPlayer rosterPlayer;

	@JsonCreator
	public PubBoxScorePlayer(@JsonProperty("position") Position position,
					@JsonProperty("starter") Boolean starter,
					@JsonProperty("minutes") Short minutes,
					@JsonProperty("points") Short points,
					@JsonProperty("assists") Short assists,
					@JsonProperty("turnovers") Short turnovers,
					@JsonProperty("steals") Short steals,
					@JsonProperty("blocks") Short blocks,
					@JsonProperty("fieldGoalAttempts") Short fieldGoalAttempts,
					@JsonProperty("fieldGoalMade") Short fieldGoalMade,
					@JsonProperty("fieldGoalPercent") Float fieldGoalPercent,
					@JsonProperty("threePointAttempts") Short threePointAttempts,
					@JsonProperty("threePointMade") Short threePointMade,
					@JsonProperty("threePointPercent") Float threePointPercent,
					@JsonProperty("freeThrowAttempts") Short freeThrowAttempts,
					@JsonProperty("freeThrowMade") Short freeThrowMade,
					@JsonProperty("freeThrowPercent") Float freeThrowPercent,
					@JsonProperty("reboundsOffense") Short reboundsOffense,
					@JsonProperty("reboundsDefense") Short reboundsDefense,
					@JsonProperty("personalFouls") Short personalFouls,
					@JsonProperty("rosterPlayer") PubRosterPlayer rosterPlayer) {
		this.position = position.name();
		this.starter = String.valueOf(starter);
		this.minutes = String.valueOf(minutes);
		this.points = String.valueOf(points);
		this.assists = String.valueOf(assists);
		this.turnovers = String.valueOf(turnovers);
		this.steals = String.valueOf(steals);
		this.blocks = String.valueOf(blocks);
		this.fieldGoalAttempts = String.valueOf(fieldGoalAttempts);
		this.fieldGoalMade = String.valueOf(fieldGoalMade);
		this.fieldGoalPercent = String.valueOf(fieldGoalPercent);
		this.threePointAttempts = String.valueOf(threePointAttempts);
		this.threePointMade = String.valueOf(threePointMade);
		this.threePointPercent = String.valueOf(threePointPercent);
		this.freeThrowAttempts = String.valueOf(freeThrowAttempts);
		this.freeThrowMade = String.valueOf(freeThrowMade);
		this.freeThrowPercent = String.valueOf(freeThrowPercent);
		this.reboundsOffense = String.valueOf(reboundsOffense);
		this.reboundsDefense = String.valueOf(reboundsDefense);
		this.personalFouls = String.valueOf(personalFouls);
		this.rosterPlayer = rosterPlayer;
	}

	public String getPosition() {
		return position;
	}
	public String getStarter() {
		return starter;
	}
	public String getMinutes() {
		return minutes;
	}
	public String getPoints() {
		return points;
	}
	public String getAssists() {
		return assists;
	}
	public String getTurnovers() {
		return turnovers;
	}
	public String getSteals() {
		return steals;
	}
	public String getBlocks() {
		return blocks;
	}
	public String getFieldGoalAttempts() {
		return fieldGoalAttempts;
	}
	public String getFieldGoalMade() {
		return fieldGoalMade;
	}
	public String getFieldGoalPercent() {
		return fieldGoalPercent;
	}
	public String getThreePointAttempts() {
		return threePointAttempts;
	}
	public String getThreePointMade() {
		return threePointMade;
	}
	public String getThreePointPercent() {
		return threePointPercent;
	}
	public String getFreeThrowAttempts() {
		return freeThrowAttempts;
	}
	public String getFreeThrowMade() {
		return freeThrowMade;
	}
	public String getFreeThrowPercent() {
		return freeThrowPercent;
	}
	public String getReboundsOffense() {
		return reboundsOffense;
	}
	public String getReboundsDefense() {
		return reboundsDefense;
	}
	public String getPersonalFouls() {
		return personalFouls;
	}
	public PubRosterPlayer getRosterPlayer() {
		return rosterPlayer;
	}
}