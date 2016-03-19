package com.rossotti.basketball.dao.pub;

import java.net.URI;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rossotti.basketball.util.DateTimeUtil;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PubStanding {
	private final URI self;
	private final String standingDate;
	private final Short rank;
	private final String ordinalRank;
	private final Short gamesWon;
	private final Short gamesLost;
	private final String streak;
	private final String streakType;
	private final Short streakTotal;
	private final Float gamesBack;
	private final Short pointsFor;
	private final Short pointsAgainst;
	private final Short homeWins;
	private final Short homeLosses;
	private final Short awayWins;
	private final Short awayLosses;
	private final Short conferenceWins;
	private final Short conferenceLosses;
	private final String lastFive;
	private final String lastTen;
	private final Short gamesPlayed;
	private final Float pointsScoredPerGame;
	private final Float pointsAllowedPerGame;
	private final Float winPercentage;
	private final Short pointDifferential;
	private final Float pointDifferentialPerGame;
	private final Integer opptGamesWon;
	private final Integer opptGamesPlayed;
	private final Integer opptOpptGamesWon;
	private final Integer opptOpptGamesPlayed;

	@JsonCreator
	public PubStanding(	@JsonProperty("self") URI self,
						@JsonProperty("standingDate") LocalDate standingDate,
						@JsonProperty("rank") Short rank,
						@JsonProperty("ordinalRank") String ordinalRank,
						@JsonProperty("gamesWon") Short gamesWon,
						@JsonProperty("gamesLost") Short gamesLost,
						@JsonProperty("streak") String streak,
						@JsonProperty("streakType") String streakType,
						@JsonProperty("streakTotal") Short streakTotal,
						@JsonProperty("gamesBack") Float gamesBack,
						@JsonProperty("pointsFor") Short pointsFor,
						@JsonProperty("pointsAgainst") Short pointsAgainst,
						@JsonProperty("homeWins") Short homeWins,
						@JsonProperty("homeLosses") Short homeLosses,
						@JsonProperty("awayWins") Short awayWins,
						@JsonProperty("awayLosses") Short awayLosses,
						@JsonProperty("conferenceWins") Short conferenceWins,
						@JsonProperty("conferenceLosses") Short conferenceLosses,
						@JsonProperty("lastFive") String lastFive,
						@JsonProperty("lastTen") String lastTen,
						@JsonProperty("gamesPlayed") Short gamesPlayed,
						@JsonProperty("pointsScoredPerGame") Float pointsScoredPerGame,
						@JsonProperty("pointsAllowedPerGame") Float pointsAllowedPerGame,
						@JsonProperty("winPercentage") Float winPercentage,
						@JsonProperty("pointDifferential") Short pointDifferential,
						@JsonProperty("pointDifferentialPerGame") Float pointDifferentialPerGame,
						@JsonProperty("opptGamesWon") Integer opptGamesWon,
						@JsonProperty("opptGamesPlayed") Integer opptGamesPlayed,
						@JsonProperty("opptOpptGamesWon") Integer opptOpptGamesWon,
						@JsonProperty("opptOpptGamesPlayed") Integer opptOpptGamesPlayed) {
		this.self = self;
		this.standingDate = DateTimeUtil.getStringDate(standingDate);
		this.rank = rank;
		this.ordinalRank = ordinalRank;
		this.gamesWon = gamesWon;
		this.gamesLost = gamesLost;
		this.streak = streak;
		this.streakType = streakType;
		this.streakTotal = streakTotal;
		this.gamesBack = gamesBack;
		this.pointsFor = pointsFor;
		this.pointsAgainst = pointsAgainst;
		this.homeWins = homeWins;
		this.homeLosses = homeLosses;
		this.awayWins = awayWins;
		this.awayLosses = awayLosses;
		this.conferenceWins = conferenceWins;
		this.conferenceLosses = conferenceLosses;
		this.lastFive = lastFive;
		this.lastTen = lastTen;
		this.gamesPlayed = gamesPlayed;
		this.pointsScoredPerGame = pointsScoredPerGame;
		this.pointsAllowedPerGame = pointsAllowedPerGame;
		this.winPercentage = winPercentage;
		this.pointDifferential = pointDifferential;
		this.pointDifferentialPerGame = pointDifferentialPerGame;
		this.opptGamesWon = opptGamesWon;
		this.opptGamesPlayed = opptGamesPlayed;
		this.opptOpptGamesWon = opptOpptGamesWon;
		this.opptOpptGamesPlayed = opptOpptGamesPlayed;
	}

	public URI getSelf() {
		return self;
	}
	public String getStandingDate() {
		return standingDate;
	}
	public Short getRank() {
		return rank;
	}
	public String getOrdinalRank() {
		return ordinalRank;
	}
	public Short getGamesWon() {
		return gamesWon;
	}
	public Short getGamesLost() {
		return gamesLost;
	}
	public String getStreak() {
		return streak;
	}
	public String getStreakType() {
		return streakType;
	}
	public Short getStreakTotal() {
		return streakTotal;
	}
	public Float getGamesBack() {
		return gamesBack;
	}
	public Short getPointsFor() {
		return pointsFor;
	}
	public Short getPointsAgainst() {
		return pointsAgainst;
	}
	public Short getHomeWins() {
		return homeWins;
	}
	public Short getHomeLosses() {
		return homeLosses;
	}
	public Short getAwayWins() {
		return awayWins;
	}
	public Short getAwayLosses() {
		return awayLosses;
	}
	public Short getConferenceWins() {
		return conferenceWins;
	}
	public Short getConferenceLosses() {
		return conferenceLosses;
	}
	public String getLastFive() {
		return lastFive;
	}
	public String getLastTen() {
		return lastTen;
	}
	public Short getGamesPlayed() {
		return gamesPlayed;
	}
	public Float getPointsScoredPerGame() {
		return pointsScoredPerGame;
	}
	public Float getPointsAllowedPerGame() {
		return pointsAllowedPerGame;
	}
	public Float getWinPercentage() {
		return winPercentage;
	}
	public Short getPointDifferential() {
		return pointDifferential;
	}
	public Float getPointDifferentialPerGame() {
		return pointDifferentialPerGame;
	}
	public Integer getOpptGamesWon() {
		return opptGamesWon;
	}
	public Integer getOpptGamesPlayed() {
		return opptGamesPlayed;
	}
	public Integer getOpptOpptGamesWon() {
		return opptOpptGamesWon;
	}
	public Integer getOpptOpptGamesPlayed() {
		return opptOpptGamesPlayed;
	}
}
