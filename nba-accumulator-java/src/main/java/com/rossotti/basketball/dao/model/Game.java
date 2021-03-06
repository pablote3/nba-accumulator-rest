package com.rossotti.basketball.dao.model;

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

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rossotti.basketball.dao.model.BoxScore.Location;
import com.rossotti.basketball.dao.pub.PubBoxScore;
import com.rossotti.basketball.dao.pub.PubGame;
import com.rossotti.basketball.dao.pub.PubGameOfficial;
import com.rossotti.basketball.util.DateTimeUtil;

@Entity
@Table (name="game")
public class Game {
	public Game() {
		setStatusCode(StatusCodeDAO.Found);
	}
	public Game(StatusCodeDAO statusCode) {
		setStatusCode(statusCode);
	}

	@Enumerated(EnumType.STRING)
	@Transient
	private StatusCodeDAO statusCode;
	public void setStatusCode(StatusCodeDAO statusCode) {
		this.statusCode = statusCode;
	}
	public Boolean isFound() {
		return statusCode == StatusCodeDAO.Found;
	}
	public Boolean isNotFound() {
		return statusCode == StatusCodeDAO.NotFound;
	}
	public Boolean isUpdated() {
		return statusCode == StatusCodeDAO.Updated;
	}
	public Boolean isCreated() {
		return statusCode == StatusCodeDAO.Created;
	}
	public Boolean isDeleted() {
		return statusCode == StatusCodeDAO.Deleted;
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
	@JsonManagedReference(value="boxScore-to-game")
	public void setBoxScores(List<BoxScore> boxScores) {
		this.boxScores = boxScores;
	}
	public void addBoxScore(BoxScore boxScore) {
		this.getBoxScores().add(boxScore);
	}
	public void removeBoxScore(BoxScore boxScore) {
		this.getBoxScores().remove(boxScore);
	}
	public BoxScore getBoxScoreAway() {
		if (this.getBoxScores().get(0).getLocation() == Location.Away) {
			return this.getBoxScores().get(0);
		}
		else {
			return this.getBoxScores().get(1);
		}
	}
	public BoxScore getBoxScoreHome() {
		if (this.getBoxScores().get(0).getLocation() == Location.Home) {
			return this.getBoxScores().get(0);
		}
		else {
			return this.getBoxScores().get(1);
		}
	}

	@OneToMany(mappedBy="game", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private List<GameOfficial> gameOfficials = new ArrayList<GameOfficial>();
	public List<GameOfficial> getGameOfficials() {
		return gameOfficials;
	}
	@JsonManagedReference(value="gameOfficial-to-game")
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
	private GameStatus status;
	public GameStatus getStatus() {
		return status;
	}
	public void setStatus(GameStatus status) {
		this.status = status;
	}
	public Boolean isScheduled() {
		return status == GameStatus.Scheduled;
	}
	public Boolean isCompleted() {
		return status == GameStatus.Completed;
	}
	public Boolean isPostponed() {
		return status == GameStatus.Postponed;
	}
	public Boolean isSuspended() {
		return status == GameStatus.Suspended;
	}
	public Boolean isCancelled() {
		return status == GameStatus.Cancelled;
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
		URI scoreLink = null;

		URI selfLink = uriInfo.getBaseUriBuilder()
								.path("games")
								.path(DateTimeUtil.getStringDate(this.getGameDateTime()))
								.path(teamKey).build();
	
		List<PubBoxScore> listPubBoxScore = new ArrayList<PubBoxScore>();
		if (this.getBoxScores().size() > 0) {
			for (BoxScore boxScore : this.getBoxScores()) {
				PubBoxScore pubBoxScore = boxScore.toPubBoxScore(uriInfo);
				listPubBoxScore.add(pubBoxScore);
			}
		}

		if (this.isCompleted()) {
			List<PubGameOfficial> listPubGameOfficial = new ArrayList<PubGameOfficial>();
			if (this.getGameOfficials().size() > 0) {
				for (GameOfficial gameOfficial : this.getGameOfficials()) {
					PubGameOfficial pubGameOfficial = gameOfficial.toPubGameOfficial(uriInfo);
					listPubGameOfficial.add(pubGameOfficial);
				}
			}
			return new PubGame(selfLink,
					this.gameDateTime,
					this.status,
					this.seasonType,
					listPubGameOfficial,
					listPubBoxScore);
		}
		else {
			scoreLink = uriInfo.getBaseUriBuilder()
					.path("score")
					.path("games")
					.path(DateTimeUtil.getStringDate(this.getGameDateTime()))
					.path(teamKey).build();
		}
		return new PubGame(selfLink,
				scoreLink,
				this.gameDateTime,
				this.status,
				this.seasonType,
				listPubBoxScore);
	}
}