package com.rossotti.basketball.pub;

import java.net.URI;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PubOfficial {

	private final URI self;
	private final Long id;
	private final String lastName;
	private final String firstName;
	private final String fromDate;
	private final String toDate;
	private final String number;

	@JsonCreator
	public PubOfficial(	@JsonProperty("self") URI self,
					@JsonProperty("id") Long id,
					@JsonProperty("lastName") String lastName,
					@JsonProperty("firstName") String firstName,
					@JsonProperty("fromDate") LocalDate fromDate,
					@JsonProperty("toDate") LocalDate toDate,
					@JsonProperty("number") String number) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy");
		this.self = self;
		this.id = id;

		this.firstName = firstName;
		this.lastName = lastName;
		this.fromDate = fromDate.toString(fmt);
		this.toDate = toDate.toString(fmt);
		this.number = number;
	}

	public URI getSelf() {
		return self;
	}
	
	public Long getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getFromDate() {
		return fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public String getNumber() {
		return number;
	}
}
