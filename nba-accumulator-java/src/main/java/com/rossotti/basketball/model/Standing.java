package com.rossotti.basketball.model;

import java.net.URI;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.ws.rs.core.UriInfo;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import com.rossotti.basketball.pub.PubStanding;
import com.rossotti.basketball.util.DateTimeUtil;

@Entity
@Table (name="standing")
public class Standing {
	public Standing() {
		setStatusCode(StatusCode.Found);
	}
	public Standing(StatusCode statusCode) {
		setStatusCode(statusCode);
	}

	@Enumerated(EnumType.STRING)
	@Transient
	private StatusCode statusCode;
	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}
	public Boolean isFound() {
		return statusCode == StatusCode.Found;
	}
	public Boolean isNotFound() {
		return statusCode == StatusCode.NotFound;
	}
	public Boolean isUpdated() {
		return statusCode == StatusCode.Updated;
	}
	public Boolean isCreated() {
		return statusCode == StatusCode.Created;
	}
	public Boolean isDeleted() {
		return statusCode == StatusCode.Deleted;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="teamId", referencedColumnName="id", nullable=false)
	private Team team;
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}

	@Column(name="standingDate", nullable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate standingDate;
	public LocalDate getStandingDate() {
		return standingDate;
	}
	public void setStandingDate(LocalDate standingDate) {
		this.standingDate = standingDate;
	}

	@Column(name="rank", nullable=false)
	private Short rank;
	public Short getRank() {
		return rank;
	}
	public void setRank(Short rank) {
		this.rank = rank;
	}

	@Column(name="ordinalRank", length=4, nullable=false)
	private String ordinalRank;
	public String getOrdinalRank() {
		return ordinalRank;
	}
	public void setOrdinalRank(String ordinalRank) {
		this.ordinalRank = ordinalRank;
	}

	@Column(name="gamesWon", nullable=false)
	private Short gamesWon;
	public Short getGamesWon() {
		return gamesWon;
	}
	public void setGamesWon(Short gamesWon) {
		this.gamesWon = gamesWon;
	}

	@Column(name="gamesLost", nullable=false)
	private Short gamesLost;
	public Short getGamesLost() {
		return gamesLost;
	}
	public void setGamesLost(Short gamesLost) {
		this.gamesLost = gamesLost;
	}

	@Column(name="streak", length=4, nullable=false)
	private String streak;
	public String getStreak() {
		return streak;
	}
	public void setStreak(String streak) {
		this.streak = streak;
	}

	@Column(name="streakType", length=4, nullable=false)
	private String streakType;
	public String getStreakType() {
		return streakType;
	}
	public void setStreakType(String streakType) {
		this.streakType = streakType;
	}

	@Column(name="streakTotal", nullable=false)
	private Short streakTotal;
	public Short getStreakTotal() {
		return streakTotal;
	}
	public void setStreakTotal(Short streakTotal) {
		this.streakTotal = streakTotal;
	}

	@Column(name="gamesBack", nullable=false)
	private Float gamesBack;
	public Float getGamesBack() {
		return gamesBack;
	}
	public void setGamesBack(Float gamesBack) {
		this.gamesBack = gamesBack;
	}

	@Column(name="pointsFor", nullable=false)
	private Short pointsFor;
	public Short getPointsFor() {
		return pointsFor;
	}
	public void setPointsFor(Short pointsFor) {
		this.pointsFor = pointsFor;
	}
	
	@Column(name="pointsAgainst", nullable=false)
	private Short pointsAgainst;
	public Short getPointsAgainst() {
		return pointsAgainst;
	}
	public void setPointsAgainst(Short pointsAgainst) {
		this.pointsAgainst = pointsAgainst;
	}

	@Column(name="homeWins", nullable=false)
	private Short homeWins;
	public Short getHomeWins() {
		return homeWins;
	}
	public void setHomeWins(Short homeWins) {
		this.homeWins = homeWins;
	}

	@Column(name="homeLosses", nullable=false)
	private Short homeLosses;
	public Short getHomeLosses() {
		return homeLosses;
	}
	public void setHomeLosses(Short homeLosses) {
		this.homeLosses = homeLosses;
	}

	@Column(name="awayWins", nullable=false)
	private Short awayWins;
	public Short getAwayWins() {
		return awayWins;
	}
	public void setAwayWins(Short awayWins) {
		this.awayWins = awayWins;
	}

	@Column(name="awayLosses", nullable=false)
	private Short awayLosses;
	public Short getAwayLosses() {
		return awayLosses;
	}
	public void setAwayLosses(Short awayLosses) {
		this.awayLosses = awayLosses;
	}

	@Column(name="conferenceWins", nullable=false)
	private Short conferenceWins;
	public Short getConferenceWins() {
		return conferenceWins;
	}
	public void setConferenceWins(Short conferenceWins) {
		this.conferenceWins = conferenceWins;
	}

	@Column(name="conferenceLosses", nullable=false)
	private Short conferenceLosses;
	public Short getConferenceLosses() {
		return conferenceLosses;
	}
	public void setConferenceLosses(Short conferenceLosses) {
		this.conferenceLosses = conferenceLosses;
	}

	@Column(name="lastFive", length=4, nullable=false)
	private String lastFive;
	public String getLastFive() {
		return lastFive;
	}
	public void setLastFive(String lastFive) {
		this.lastFive = lastFive;
	}

	@Column(name="lastTen", length=4, nullable=false)
	private String lastTen;
	public String getLastTen() {
		return lastTen;
	}
	public void setLastTen(String lastTen) {
		this.lastTen = lastTen;
	}

	@Column(name="gamesPlayed", nullable=false)
	private Short gamesPlayed;
	public Short getGamesPlayed() {
		return gamesPlayed;
	}
	public void setGamesPlayed(Short gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}

	@Column(name="pointsScoredPerGame", nullable=false)
	private Float pointsScoredPerGame;
	public Float getPointsScoredPerGame() {
		return pointsScoredPerGame;
	}
	public void setPointsScoredPerGame(Float pointsScoredPerGame) {
		this.pointsScoredPerGame = pointsScoredPerGame;
	}

	@Column(name="pointsAllowedPerGame", nullable=false)
	private Float pointsAllowedPerGame;
	public Float getPointsAllowedPerGame() {
		return pointsAllowedPerGame;
	}
	public void setPointsAllowedPerGame(Float pointsAllowedPerGame) {
		this.pointsAllowedPerGame = pointsAllowedPerGame;
	}

	@Column(name="winPercentage", nullable=false)
	private Float winPercentage;
	public Float getWinPercentage() {
		return winPercentage;
	}
	public void setWinPercentage(Float winPercentage) {
		this.winPercentage = winPercentage;
	}

	@Column(name="pointDifferential", nullable=false)
	private Short pointDifferential;
	public Short getPointDifferential() {
		return pointDifferential;
	}
	public void setPointDifferential(Short pointDifferential) {
		this.pointDifferential = pointDifferential;
	}

	@Column(name="pointDifferentialPerGame", nullable=false)
	private Float pointDifferentialPerGame;
	public Float getPointDifferentialPerGame() {
		return pointDifferentialPerGame;
	}
	public void setPointDifferentialPerGame(Float pointDifferentialPerGame) {
		this.pointDifferentialPerGame = pointDifferentialPerGame;
	}

	@Column(name="opptGamesWon", nullable=true)
	private Integer opptGamesWon;
	public Integer getOpptGamesWon() {
		return opptGamesWon;
	}
	public void setOpptGamesWon(Integer opptGamesWon) {
		this.opptGamesWon = opptGamesWon;
	}

	@Column(name="opptGamesPlayed", nullable=true)
	private Integer opptGamesPlayed;
	public Integer getOpptGamesPlayed() {
		return opptGamesPlayed;
	}
	public void setOpptGamesPlayed(Integer opptGamesPlayed) {
		this.opptGamesPlayed = opptGamesPlayed;
	}

	@Column(name="opptOpptGamesWon", nullable=true)
	private Integer opptOpptGamesWon;
	public Integer getOpptOpptGamesWon() {
		return opptOpptGamesWon;
	}
	public void setOpptOpptGamesWon(Integer opptOpptGamesWon) {
		this.opptOpptGamesWon = opptOpptGamesWon;
	}

	@Column(name="opptOpptGamesPlayed", nullable=true)
	private Integer opptOpptGamesPlayed;
	public Integer getOpptOpptGamesPlayed() {
		return opptOpptGamesPlayed;
	}	
	public void setOpptOpptGamesPlayed(Integer opptOpptGamesPlayed) {
		this.opptOpptGamesPlayed = opptOpptGamesPlayed;
	}

	public String toString() {
		return new StringBuffer()
			.append("\r" + "  id: " + this.id + "\n")
			.append("  standingDate: " + this.standingDate + "\n")
			.append("  teamKey: " + team.getTeamKey() + "\n")
			.append("  gamesWon: " + this.gamesWon + "\n")
			.append("  gamesLost: " + this.gamesLost + "\n")
			.append("  statusCode: " + this.statusCode)
			.toString();
	}

	public PubStanding toPubStanding(UriInfo uriInfo) {
		URI self;
		self = uriInfo.getBaseUriBuilder().path("standings").
											path(DateTimeUtil.getStringDate(this.getStandingDate())).
											path(this.getTeam().getTeamKey()).build();
		return new PubStanding( self,
								this.standingDate,
								this.rank,
								this.ordinalRank,
								this.gamesWon,
								this.gamesLost,
								this.streak,
								this.streakType,
								this.streakTotal,
								this.gamesBack,
								this.pointsFor,
								this.pointsAgainst,
								this.homeWins,
								this.homeLosses,
								this.awayWins,
								this.awayLosses,
								this.conferenceWins,
								this.conferenceLosses,
								this.lastFive,
								this.lastTen,
								this.gamesPlayed,
								this.pointsScoredPerGame,
								this.pointsAllowedPerGame,
								this.winPercentage,
								this.pointDifferential,
								this.pointDifferentialPerGame,
								this.opptGamesWon,
								this.opptGamesPlayed,
								this.opptOpptGamesWon,
								this.opptOpptGamesPlayed);
	}
}