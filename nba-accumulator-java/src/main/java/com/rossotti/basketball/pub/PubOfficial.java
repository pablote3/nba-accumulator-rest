package com.rossotti.basketball.pub;

import java.net.URI;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rossotti.basketball.util.DateTimeUtil;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PubOfficial {
	private final URI self;
	private final String lastName;
	private final String firstName;
	private final String fromDate;
	private final String number;

	@JsonCreator
	public PubOfficial(	@JsonProperty("self") URI self,
					@JsonProperty("lastName") String lastName,
					@JsonProperty("firstName") String firstName,
					@JsonProperty("fromDate") LocalDate fromDate,
					@JsonProperty("number") String number) {
		this.self = self;
		this.firstName = firstName;
		this.lastName = lastName;
		this.fromDate = DateTimeUtil.getStringDate(fromDate);
		this.number = number;
	}

	public URI getSelf() {
		return self;
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
	public String getNumber() {
		return number;
	}
}
