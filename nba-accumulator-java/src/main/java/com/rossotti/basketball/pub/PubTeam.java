package com.rossotti.basketball.pub;

import java.net.URI;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rossotti.basketball.models.Team.Conference;
import com.rossotti.basketball.models.Team.Division;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PubTeam {

	private final URI self;
	private final String key;
	private final String firstName;
	private final String lastName;
	private final String fullName;
	private final String abbr;
	private final String fromDate;
	private final String toDate;
	private final String conference;
	private final String division;
	private final String city;
	private final String state;
	private final String siteName;

	@JsonCreator
	public PubTeam(	@JsonProperty("self") URI self,
					@JsonProperty("key") String key,
					@JsonProperty("firstName") String firstName,
					@JsonProperty("lastName") String lastName,
					@JsonProperty("fullName") String fullName,
					@JsonProperty("fromDate") LocalDate fromDate,
					@JsonProperty("toDate") LocalDate toDate,
					@JsonProperty("abbr") String abbr,
					@JsonProperty("conference") Conference conference,
					@JsonProperty("division") Division division,
					@JsonProperty("city") String city,
					@JsonProperty("state") String state,
					@JsonProperty("siteName") String siteName) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy");
		this.self = self;
		this.key = key;
		this.firstName = firstName;
		this.lastName = lastName;
		this.fullName = fullName;
		this.abbr = abbr;
		this.fromDate = fromDate.toString(fmt);
		this.toDate = toDate.toString(fmt);
		this.conference = conference.name();
		this.division = division.name();
		this.city = city;
		this.state = state;
		this.siteName = siteName;
	}

	public URI getSelf() {
		return self;
	}
	public String getKey() {
		return key;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getFullName() {
		return fullName;
	}
	public String getAbbr() {
		return abbr;
	}
	public String getFromDate() {
		return fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public String getConference() {
		return conference;
	}
	public String getDivision() {
		return division;
	}
	public String getCity() {
		return city;
	}
	public String getState() {
		return state;
	}
	public String getSiteName() {
		return siteName;
	}
}
