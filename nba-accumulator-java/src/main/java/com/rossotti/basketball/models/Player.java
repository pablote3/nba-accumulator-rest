package com.rossotti.basketball.models;

import java.net.URI;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.ws.rs.core.UriInfo;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rossotti.basketball.pub.PubPlayer;

@Entity
@Table (name="player", uniqueConstraints=@UniqueConstraint(columnNames={"lastName", "firstName", "birthDate"}))
public class Player {
	public Player() {}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="lastName", length=20, nullable=false)
	@JsonProperty("last_name")
	private String lastName;
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name="firstName", length=20, nullable=false)
	@JsonProperty("first_name")
	private String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name="birthDate", nullable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	@JsonProperty("birthdate")
	private LocalDate birthDate;
	public LocalDate getBirthDate()  {
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	@Column(name="displayName", length=40, nullable=false)
	@JsonProperty("display_name")
	private String displayName;
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Column(name="height", nullable=true)
	@JsonProperty("height_in")
	private Short height;
	public Short getHeight() {
		return height;
	}
	public void setHeight(Short height) {
		this.height = height;
	}

	@Column(name="weight", nullable=true)
	@JsonProperty("weight_lb")
	private Short weight;
	public Short getWeight() {
		return weight;
	}
	public void setWeight(Short weight) {
		this.weight = weight;
	}

	@Column(name="birthPlace", length=40, nullable=true)
	@JsonProperty("birthplace")
	private String birthPlace;
	public String getBirthPlace() {
		return birthPlace;
	}
	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public String toString() {
		return new StringBuffer()
			.append("\n" + "  id: " + this.id)
			.append("  lastName: " + this.lastName + "\n")
			.append("  firstName: " + this.firstName + "\n")
			.append("  birthDate: " + this.birthDate + "\n")
			.toString();
	}

	public PubPlayer toPubPlayer(UriInfo uriInfo) {
		URI self;
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		self = uriInfo.getBaseUriBuilder().path("players").
											path(this.getLastName()).
											path(this.getFirstName()).
											path(this.getBirthDate().toString(fmt)).build();
		return new PubPlayer( self,
							this.id,
							this.lastName,
							this.firstName,
							this.birthDate,
							this.displayName,
							this.height,
							this.weight,
							this.birthPlace);
	}
}