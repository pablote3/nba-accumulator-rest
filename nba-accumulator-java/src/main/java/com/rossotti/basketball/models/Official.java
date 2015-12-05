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
import com.rossotti.basketball.pub.PubOfficial;

@Entity
@Table (name="official", uniqueConstraints=@UniqueConstraint(columnNames={"lastName", "firstName", "fromDate", "toDate"}))
public class Official {
	public Official() {}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="lastName", length=25, nullable=false)
	@JsonProperty("last_name")
	private String lastName;
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name="firstName", length=25, nullable=false)
	@JsonProperty("first_name")
	private String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
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

	public String toString() {
		return new StringBuffer()
			.append("\n" + "  id: " + this.id)
			.append("  lastName: " + this.lastName + "\n")
			.append("  firstName: " + this.firstName + "\n")
			.append("  fromDate: " + this.fromDate + "\n")
			.append("  toDate: " + this.toDate + "\n")
			.toString();
	}

	public PubOfficial toPubOfficial(UriInfo uriInfo) {
		URI self;
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		self = uriInfo.getBaseUriBuilder().path("teams").
											path(this.getLastName()).
											path(this.getFirstName()).
											path(this.getFromDate().toString(fmt)).
											path(this.getToDate().toString(fmt)).build();
		return new PubOfficial( self,
							this.id,
							this.lastName,
							this.firstName,
							this.fromDate,
							this.toDate,
							this.number);
	}
}