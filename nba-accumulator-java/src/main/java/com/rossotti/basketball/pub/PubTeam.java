package com.rossotti.basketball.pub;

import java.net.URI;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PubTeam {

	private final URI self;
	private final Long id;
	private final String key;
	private final String firstName;
	private final String lastName;
	private final String fullName;
	private final String fromDate;
	private final String toDate;

	@JsonCreator
	public PubTeam(	@JsonProperty("self") URI self,
					@JsonProperty("id") Long id,
					@JsonProperty("key") String key,
					@JsonProperty("firstName") String firstName,
					@JsonProperty("lastName") String lastName,
					@JsonProperty("fullName") String fullName,
					@JsonProperty("fromDate") LocalDate fromDate,
					@JsonProperty("toDate") LocalDate toDate) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy");
		this.self = self;
		this.id = id;
		this.key = key;
		this.firstName = firstName;
		this.lastName = lastName;
		this.fullName = fullName;
		this.fromDate = fromDate.toString(fmt);
		this.toDate = toDate.toString(fmt);
	}

	public URI getSelf() {
		return self;
	}
	
	public Long getId() {
		return id;
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
	
	public String getFromDate() {
		return fromDate;
	}
	
	public String getToDate() {
		return toDate;
	}
}
