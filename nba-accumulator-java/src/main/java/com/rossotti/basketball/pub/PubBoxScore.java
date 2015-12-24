package com.rossotti.basketball.pub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rossotti.basketball.model.BoxScore.Location;
import com.rossotti.basketball.model.BoxScore.Result;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PubBoxScore {

	private final String location;
	private final String result;
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
	private final String pointsPeriod1;
	private final String pointsPeriod2;
	private final String pointsPeriod3;
	private final String pointsPeriod4;
	private final String pointsPeriod5;
	private final String pointsPeriod6;
	private final String pointsPeriod7;
	private final String pointsPeriod8;
	private final String daysOff;
	private final PubTeam team;

	@JsonCreator
	public PubBoxScore(@JsonProperty("location") Location location,
					@JsonProperty("result") Result result,
					@JsonProperty("minutes") Short minutes,
					@JsonProperty("points") Short points,
					@JsonProperty("assists") Short assists,
					@JsonProperty("turnovers") Short turnovers,
					@JsonProperty("steals") Short steals,
					@JsonProperty("blocks") Short blocks,
					@JsonProperty("fieldGoalAttempts") Short fieldGoalAttempts,
					@JsonProperty("fieldGoalMade") Short fieldGoalMade,
					@JsonProperty("fieldGoalPercent") Short fieldGoalPercent,
					@JsonProperty("threePointAttempts") Short threePointAttempts,
					@JsonProperty("threePointMade") Short threePointMade,
					@JsonProperty("threePointPercent") Short threePointPercent,
					@JsonProperty("freeThrowAttempts") Short freeThrowAttempts,
					@JsonProperty("freeThrowMade") Short freeThrowMade,
					@JsonProperty("freeThrowPercent") Short freeThrowPercent,
					@JsonProperty("reboundsOffense") Short reboundsOffense,
					@JsonProperty("reboundsDefense") Short reboundsDefense,
					@JsonProperty("personalFouls") Short personalFouls,
					@JsonProperty("pointsPeriod1") Short pointsPeriod1,
					@JsonProperty("pointsPeriod2") Short pointsPeriod2,
					@JsonProperty("pointsPeriod3") Short pointsPeriod3,
					@JsonProperty("pointsPeriod4") Short pointsPeriod4,
					@JsonProperty("pointsPeriod5") Short pointsPeriod5,
					@JsonProperty("pointsPeriod6") Short pointsPeriod6,
					@JsonProperty("pointsPeriod7") Short pointsPeriod7,
					@JsonProperty("pointsPeriod8") Short pointsPeriod8,
					@JsonProperty("daysOff") Short daysOff,
					@JsonProperty("team") PubTeam team) {
		this.location = location.name();
		this.result = result.name();
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
		this.pointsPeriod1 = String.valueOf(pointsPeriod1);
		this.pointsPeriod2 = String.valueOf(pointsPeriod2);
		this.pointsPeriod3 = String.valueOf(pointsPeriod3);
		this.pointsPeriod4 = String.valueOf(pointsPeriod4);
		this.pointsPeriod5 = String.valueOf(pointsPeriod5);
		this.pointsPeriod6 = String.valueOf(pointsPeriod6);
		this.pointsPeriod7 = String.valueOf(pointsPeriod7);
		this.pointsPeriod8 = String.valueOf(pointsPeriod8);
		this.daysOff = String.valueOf(daysOff);
		this.team = team;
	}

	public String getLocation() {
		return location;
	}
	public String getResult() {
		return result;
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
	public String getPointsPeriod1() {
		return pointsPeriod1;
	}
	public String getPointsPeriod2() {
		return pointsPeriod2;
	}
	public String getPointsPeriod3() {
		return pointsPeriod3;
	}
	public String getPointsPeriod4() {
		return pointsPeriod4;
	}
	public String getPointsPeriod5() {
		return pointsPeriod5;
	}
	public String getPointsPeriod6() {
		return pointsPeriod6;
	}
	public String getPointsPeriod7() {
		return pointsPeriod7;
	}
	public String getPointsPeriod8() {
		return pointsPeriod8;
	}
	public String getDaysOff() {
		return daysOff;
	}
	public PubTeam getTeam() {
		return team;
	}
}