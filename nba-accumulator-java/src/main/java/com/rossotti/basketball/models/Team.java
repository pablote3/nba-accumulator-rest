package com.rossotti.basketball.models;

import java.net.URI;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import com.rossotti.basketball.pub.PubTeam;

@Entity
@Table (name="team", uniqueConstraints=@UniqueConstraint(columnNames={"team_key"}))
public class Team {
	public Team() {}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="team_key", length=35, nullable=false)
	@JsonProperty("team_id")
	private String key;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	@Column(name="first_name", length=15, nullable=false)
	@JsonProperty("first_name")
	private String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(name="last_name", length=20, nullable=false)
	@JsonProperty("last_name")
	private String lastName;
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Column(name="full_name", length=35, nullable=false)
	@JsonProperty("full_name")
	private String fullName;
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	@Column(name="from_date", nullable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	@JsonProperty("from_date")
	private LocalDate fromDate;
	public LocalDate getFromDate()  {
		return fromDate;
	}
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}
	
	@Column(name="to_date", nullable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	@JsonProperty("to_date")
	private LocalDate toDate;
	public LocalDate getToDate()  {
		return toDate;
	}
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}
	
	@Column(name="abbr", length=5, nullable=false)
	@JsonProperty("abbreviation")
	private String abbr;
	public String getAbbr() {
		return abbr;
	}
	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name="conference", length=4, nullable=false)
	private Conference conference;
	public Conference getConference() {
		return conference;
	}
	public void setConference(Conference conference) {
		this.conference = conference;
	}
	
	public enum Conference {
		East,
		West;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name="division", length=9, nullable=false)
	private Division division;
	public Division getDivision() {
		return division;
	}
	public void setDivision(Division division) {
		this.division = division;
	}
	
	public enum Division {
		Atlantic,
		Central,
		Southeast,
		Southwest,
		Northwest,
		Pacific;
	}
	
	@Column(name="site_name", length=30, nullable=false)
	@JsonProperty("site_name")
	private String siteName;
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	@Column(name="city", length=15, nullable=false)
	private String city;
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	@Column(name="state", length=2, nullable=false)
	private String state;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	public String toString() {
		return new StringBuffer()
			.append("\n" + "  id: " + this.id)
			.append("  key: " + this.key)
			.append("  fromDate: " + this.fromDate + "\n")
			.append("  toDate: " + this.toDate + "\n")
			.toString();
	}

	public PubTeam toPubTeam(UriInfo uriInfo) {
		URI self;
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		self = uriInfo.getBaseUriBuilder().path("teams").
											path(this.getKey()).
											path(this.getFromDate().toString(fmt)).
											path(this.getToDate().toString(fmt)).build();
		return new PubTeam( self,
							this.id,
							this.key,
							this.firstName,
							this.lastName,
							this.fullName,
							this.fromDate,
							this.toDate,
							this.abbr,
							this.conference,
							this.division,
							this.city,
							this.state,
							this.siteName);
	}
}