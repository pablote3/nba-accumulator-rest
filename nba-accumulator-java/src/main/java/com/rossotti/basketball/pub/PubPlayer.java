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
	private final Long id;
	private final String lastName;
	private final String firstName;
	private final String birthDate;
	private final String displayName;
	private final String height;
	private final String weight;
	private final String birthPlace;

	@JsonCreator
	public PubPlayer(@JsonProperty("self") URI self,
					@JsonProperty("id") Long id,
					@JsonProperty("lastName") String lastName,
					@JsonProperty("firstName") String firstName,
					@JsonProperty("birthDate") LocalDate birthDate,
					@JsonProperty("displayName") String displayName,
					@JsonProperty("height") Short height,
					@JsonProperty("weight") Short weight,
					@JsonProperty("birthPlace") String birthPlace) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy");
		this.self = self;
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate.toString(fmt);
		this.displayName = displayName;
		this.height = String.valueOf(height);
		this.weight = String.valueOf(weight);
		this.birthPlace = birthPlace;
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
	public String getBirthDate() {
		return birthDate;
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
	public String getBirthPlace() {
		return birthPlace;
	}
}
