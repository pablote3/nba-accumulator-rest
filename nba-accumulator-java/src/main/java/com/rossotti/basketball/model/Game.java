package com.rossotti.basketball.model;

import java.net.URI;
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
import javax.ws.rs.core.UriInfo;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import com.rossotti.basketball.pub.PubBoxScore;
import com.rossotti.basketball.pub.PubGame;
import com.rossotti.basketball.util.DateTimeUtil;

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

	@Column(name="gameDateTime", nullable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime gameDateTime;
	public LocalDateTime getGameDateTime() {
		return gameDateTime;
	}
	public void setGameDateTime(LocalDateTime gameDateTime) {
		this.gameDateTime = gameDateTime;
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
			.append("  gameDateTime: " + this.gameDateTime + "\n")
			.append("  status: " + this.status + "\n")
			.append("  seasonType: " + this.seasonType + "\n")
			.append("  statusCode: " + this.statusCode)
			.toString();
	}

	public PubGame toPubGame(UriInfo uriInfo, String teamKey) {
		URI self = uriInfo.getBaseUriBuilder().path("games").
											path(DateTimeUtil.getStringDate(this.getGameDateTime())).
											path(teamKey).build();

		List<PubBoxScore> listPubBoxScore = new ArrayList<PubBoxScore>();
		if (this.getBoxScores().size() > 0) {
			for (BoxScore boxScore : this.getBoxScores()) {
				PubBoxScore pubBoxScore = boxScore.toPubBoxScore(uriInfo);
				listPubBoxScore.add(pubBoxScore);
			}
		}

		return new PubGame(self,
						this.gameDateTime,
						this.status,
						this.seasonType,
						listPubBoxScore);
	}
}