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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.ws.rs.core.UriInfo;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import com.rossotti.basketball.dao.pub.PubRosterPlayer;
import com.rossotti.basketball.dao.pub.PubRosterPlayer_ByPlayer;
import com.rossotti.basketball.dao.pub.PubRosterPlayer_ByTeam;
import com.rossotti.basketball.util.DateTimeUtil;

@Entity
@Table (name="rosterPlayer")
public class RosterPlayer {
	public RosterPlayer() {
		setStatusCode(StatusCode.Found);
	}
	public RosterPlayer(StatusCode statusCode) {
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

	@ManyToOne(cascade=CascadeType.MERGE)
	@JoinColumn(name="teamId", referencedColumnName="id", nullable=false)
	private Team team;
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}

	@ManyToOne(cascade=CascadeType.MERGE)
	@JoinColumn(name="playerId", referencedColumnName="id", nullable=false)
	private Player player;
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}

	@OneToMany(mappedBy="rosterPlayer", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private List<BoxScorePlayer> boxScorePlayers = new ArrayList<BoxScorePlayer>();
	public List<BoxScorePlayer> getBoxScorePlayers()  {
		return boxScorePlayers;
	}
	public void setBoxScorePlayers(List<BoxScorePlayer> boxScorePlayers)  {
		this.boxScorePlayers = boxScorePlayers;
	}
	public void addBoxScorePlayer(BoxScorePlayer boxScorePlayer)  {
		this.getBoxScorePlayers().add(boxScorePlayer);
	}
	public void removeBoxScorePlayer(BoxScorePlayer boxScorePlayer)  {
		this.getBoxScorePlayers().remove(boxScorePlayer);
	}

	@Column(name="fromDate", nullable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate fromDate;
	public LocalDate getFromDate()  {
		return fromDate;
	}
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	@Column(name="toDate", nullable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate toDate;
	public LocalDate getToDate()  {
		return toDate;
	}
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	@Column(name="number", length=3, nullable=false)
	private String number;
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="position", length=5, nullable=false)
	private Position position;
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}

	public String toString() {
		return new StringBuffer()
			.append("\r" + "  id: " + this.id + "\n")
			.append("  lastName: " + player.getLastName() + "\n")
			.append("  firstName: " + player.getFirstName() + "\n")
			.append("  birthDate: " + player.getBirthdate() + "\n")
			.append("  teamKey: " + team.getTeamKey() + "\n")
			.append("  fromDate: " + this.getFromDate() + "\n")
			.append("  toDate: " + this.getToDate() + "\n")
			.append("  statusCode: " + this.statusCode)
			.toString();
	}

	public PubRosterPlayer toPubRosterPlayer(UriInfo uriInfo) {
		URI self = uriInfo.getBaseUriBuilder().path("rosterPlayers").
												path(this.getPlayer().getLastName()).
												path(this.getPlayer().getFirstName()).
												path(DateTimeUtil.getStringDate(this.getPlayer().getBirthdate())).
												path(DateTimeUtil.getStringDate(this.getFromDate())).build();
		return new PubRosterPlayer(self,
							this.fromDate,
							this.toDate,
							this.position,
							this.number,
							this.getPlayer().toPubPlayer(uriInfo),
							this.getTeam().toPubTeam(uriInfo));
	}

	public PubRosterPlayer_ByPlayer toPubRosterPlayer_ByPlayer(UriInfo uriInfo) {
		URI self = uriInfo.getBaseUriBuilder().path("rosterPlayers").
												path(this.getPlayer().getLastName()).
												path(this.getPlayer().getFirstName()).
												path(DateTimeUtil.getStringDate(this.getPlayer().getBirthdate())).
												path(DateTimeUtil.getStringDate(this.getFromDate())).build();
		return new PubRosterPlayer_ByPlayer(self,
							this.fromDate,
							this.toDate,
							this.position,
							this.number,
							this.getTeam().toPubTeam(uriInfo));
	}

	public PubRosterPlayer_ByTeam toPubRosterPlayer_ByTeam(UriInfo uriInfo) {
		URI self = uriInfo.getBaseUriBuilder().path("rosterPlayers").
												path(this.getPlayer().getLastName()).
												path(this.getPlayer().getFirstName()).
												path(this.getTeam().getTeamKey()).
												path(DateTimeUtil.getStringDate(this.getFromDate())).build();
		return new PubRosterPlayer_ByTeam(self,
							this.fromDate,
							this.toDate,
							this.position,
							this.number,
							this.getPlayer().toPubPlayer(uriInfo));
	}
}