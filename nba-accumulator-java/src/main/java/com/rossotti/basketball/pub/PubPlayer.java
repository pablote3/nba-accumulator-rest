package com.rossotti.basketball.pub;

import java.net.URI;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PubPlayer {

	private final URI self;
	private final String lastName;
	private final String firstName;
	private final String birthdate;
	private final String displayName;
	private final String height;
	private final String weight;
	private final String birthplace;

	@JsonCreator
	public PubPlayer(@JsonProperty("self") URI self,
					@JsonProperty("lastName") String lastName,
					@JsonProperty("firstName") String firstName,
					@JsonProperty("birthdate") LocalDate birthdate,
					@JsonProperty("displayName") String displayName,
					@JsonProperty("height") Short height,
					@JsonProperty("weight") Short weight,
					@JsonProperty("birthplace") String birthplace) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy");
		this.self = self;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthdate = birthdate.toString(fmt);
		this.displayName = displayName;
		this.height = String.valueOf(height);
		this.weight = String.valueOf(weight);
		this.birthplace = birthplace;
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
	public String getBirthdate() {
		return birthdate;
	}
	public String getDisplayName() {
		return displayName;
	}
	public String getHeight() {
		return height;
	}
	public String getWeight() {
		return weight;
	}
	public String getBirthplace() {
		return birthplace;
	}
}
