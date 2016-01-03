package com.rossotti.basketball.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

@Entity
@Table (name="game")
public class Game {
	public Game() {
		setStatusCode(StatusCode.Found);
	}
	public Game(StatusCode statusCode) {
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
	@Column(name="id")
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(mappedBy="game", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private List<BoxScore> boxScores = new ArrayList<BoxScore>();
	public List<BoxScore> getBoxScores() {
		return boxScores;
	}
	public void setBoxScores(List<BoxScore> boxScores) {
		this.boxScores = boxScores;
	}
	public void addBoxScore(BoxScore boxScore) {
		this.getBoxScores().add(boxScore);
	}
	public void removeBoxScore(BoxScore boxScore) {
		this.getBoxScores().remove(boxScore);
	}

	@OneToMany(mappedBy="game", fetch = FetchType.LAZY)
	private List<GameOfficial> gameOfficials = new ArrayList<GameOfficial>();
	public List<GameOfficial> getGameOfficials() {
		return gameOfficials;
	}
	public void setGameOfficials(List<GameOfficial> gameOfficials) {
		this.gameOfficials = gameOfficials;
	}
	public void addGameOfficial(GameOfficial gameOfficial) {
		this.getGameOfficials().add(gameOfficial);
	}
	public void removeGameOfficial(GameOfficial gameOfficial) {
		this.getGameOfficials().remove(gameOfficial);
	}

	@Column(name="gameDate", nullable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime gameDate;
	public LocalDateTime getGameDate() {
		return gameDate;
	}
	public void setGameDate(LocalDateTime gameDate) {
		this.gameDate = gameDate;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="status", length=9, nullable=false)
	private Status status;
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public enum Status {
		Scheduled,
		Finished,
		Completed,
		Postponed,
		Suspended,
		Cancelled
	}

	@Enumerated(EnumType.STRING)
	@Column(name="seasonType", length=7, nullable=false)
	private SeasonType seasonType;
	public SeasonType getSeasonType() {
		return seasonType;
	}
	public void setSeasonType(SeasonType seasonType) {
		this.seasonType = seasonType;
	}
	public enum SeasonType {
		Pre,
		Regular,
		Post
	}

	public String toString() {
		return new StringBuffer()
			.append("\r" + "  id: " + this.id + "\n")
			.append("  gameDate: " + this.gameDate + "\n")
			.append("  status: " + this.status + "\n")
			.append("  seasonType: " + this.seasonType + "\n")
			.append("  statusCode: " + this.statusCode)
			.toString();
	}
}