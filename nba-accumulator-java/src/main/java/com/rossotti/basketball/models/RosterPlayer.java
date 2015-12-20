package com.rossotti.basketball.models;

import java.net.URI;

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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.ws.rs.core.UriInfo;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rossotti.basketball.pub.PubRosterPlayer;
import com.rossotti.basketball.pub.PubRosterPlayer_ByPlayer;

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

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="teamId")
	private Team team;
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="playerId")
	private Player player;
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}

	@Column(name="fromDate", nullable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	@JsonProperty("from_date")
	private LocalDate fromDate;
	public LocalDate getFromDate()  {
		return fromDate;
	}
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	@Column(name="toDate", nullable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	@JsonProperty("to_date")
	private LocalDate toDate;
	public LocalDate getToDate()  {
		return toDate;
	}
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	@Column(name="number", length=3, nullable=false)
	@JsonProperty("number")
	private String number;
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="position", length=5, nullable=false)
	@JsonProperty("position")
	private Position position;
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public enum Position {
		PG,
		SG,
		SF,
		PF,
		C,
		G,
		F
	}

	public String toString() {
		return new StringBuffer()
			.append("\n" + "  id: " + this.id + "\n")
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
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		URI self = uriInfo.getBaseUriBuilder().path("rosterPlayers").
												path(this.getPlayer().getLastName()).
												path(this.getPlayer().getFirstName()).
												path(this.getPlayer().getBirthdate().toString(fmt)).
												path(this.getFromDate().toString(fmt)).build();
		return new PubRosterPlayer( self,
							this.fromDate,
							this.toDate,
							this.position,
							this.number,
							this.getPlayer().toPubPlayer(uriInfo),
							this.getTeam().toPubTeam(uriInfo));
	}

	public PubRosterPlayer_ByPlayer toPubRosterPlayer_ByPlayer(UriInfo uriInfo) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		URI self = uriInfo.getBaseUriBuilder().path("rosterPlayers").
												path(this.getPlayer().getLastName()).
												path(this.getPlayer().getFirstName()).
												path(this.getPlayer().getBirthdate().toString(fmt)).
												path(this.getFromDate().toString(fmt)).build();
		return new PubRosterPlayer_ByPlayer( self,
							this.fromDate,
							this.toDate,
							this.position,
							this.number,
							this.getTeam().toPubTeam(uriInfo));
	}
}