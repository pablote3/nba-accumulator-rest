package com.rossotti.basketball.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.UniqueConstraint;
import javax.ws.rs.core.UriInfo;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import com.rossotti.basketball.pub.PubPlayer;
import com.rossotti.basketball.util.DateTimeUtil;

@Entity
@Table (name="player", uniqueConstraints=@UniqueConstraint(columnNames={"lastName", "firstName", "birthdate"}))
public class Player {

	public Player() {
		setStatusCode(StatusCode.Found);
	}

	public Player(StatusCode statusCode) {
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

	@OneToMany(mappedBy="player", fetch = FetchType.LAZY)
	private List<RosterPlayer> rosterPlayers = new ArrayList<RosterPlayer>();
	public List<RosterPlayer> getRosterPlayers()  {
		return rosterPlayers;
	}
	public void setRosterPlayers(List<RosterPlayer> rosterPlayers)  {
		this.rosterPlayers = rosterPlayers;
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

	@Column(name="lastName", length=20, nullable=false)
	private String lastName;
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name="firstName", length=20, nullable=false)
	private String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name="birthdate", nullable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate birthdate;
	public LocalDate getBirthdate()  {
		return birthdate;
	}
	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}

	@Column(name="displayName", length=40, nullable=false)
	private String displayName;
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Column(name="height", nullable=true)
	private Short height;
	public Short getHeight() {
		return height;
	}
	public void setHeight(Short height) {
		this.height = height;
	}

	@Column(name="weight", nullable=true)
	private Short weight;
	public Short getWeight() {
		return weight;
	}
	public void setWeight(Short weight) {
		this.weight = weight;
	}

	@Column(name="birthplace", length=40, nullable=true)
	private String birthplace;
	public String getBirthplace() {
		return birthplace;
	}
	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}

	public String toString() {
		return new StringBuffer()
			.append("\n" + "  id: " + this.id + "\n")
			.append("  lastName: " + this.lastName + "\n")
			.append("  firstName: " + this.firstName + "\n")
			.append("  birthdate: " + this.birthdate + "\n")
			.append("  statusCode: " + this.statusCode)
			.toString();
	}

	public PubPlayer toPubPlayer(UriInfo uriInfo) {
		URI self;
		self = uriInfo.getBaseUriBuilder().path("players").
											path(this.getLastName()).
											path(this.getFirstName()).
											path(DateTimeUtil.getStringDate(this.getBirthdate())).build();
		return new PubPlayer( self,
							this.lastName,
							this.firstName,
							this.birthdate,
							this.displayName,
							this.height,
							this.weight,
							this.birthplace);
	}
}