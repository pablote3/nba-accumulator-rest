package com.rossotti.basketball.models;

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

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table (name="rosterPlayer")
public class RosterPlayer {
	public RosterPlayer() {}

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
			.append("\n" + "  id: " + this.id)
//			.append("  lastName: " + player.getLastName() + "\n")
//			.append("  firstName: " + player.getFirstName() + "\n")
//			.append("  birthDate: " + player.getBirthdate() + "\n")
//			.append("  teamKey: " + team.getTeamKey() + "\n")
			.append("  fromDate: " + this.getFromDate() + "\n")
			.append("  toDate: " + this.getToDate() + "\n")
			.toString();
	}

//	public PubPlayer toPubPlayer(UriInfo uriInfo) {
//		URI self;
//		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
//		self = uriInfo.getBaseUriBuilder().path("players").
//											path(player.getLastName()).
//											path(player.getFirstName()).
//											path(player.getBirthDate().toString(fmt)).build();
//		return new PubRosterPlayer( self,
//							this.id,
//							player.lastName,
//							player.firstName,
//							this.birthDate,
//							this.displayName,
//							this.height,
//							this.weight,
//							this.birthPlace);
//	}
}