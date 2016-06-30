package com.rossotti.basketball.dao.model;

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

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rossotti.basketball.dao.pub.PubOfficial;
import com.rossotti.basketball.util.DateTimeUtil;

@Entity
@Table (name="official", uniqueConstraints=@UniqueConstraint(columnNames={"lastName", "firstName", "fromDate", "toDate"}))
public class Official {
	public Official() {
		setStatusCode(StatusCodeDAO.Found);
	}
	public Official(StatusCodeDAO statusCode) {
		setStatusCode(statusCode);
	}

	@Enumerated(EnumType.STRING)
	@Transient
	private StatusCodeDAO statusCode;
	public void setStatusCode(StatusCodeDAO statusCode) {
		this.statusCode = statusCode;
	}
	public Boolean isFound() {
		return statusCode == StatusCodeDAO.Found;
	}
	public Boolean isNotFound() {
		return statusCode == StatusCodeDAO.NotFound;
	}
	public Boolean isUpdated() {
		return statusCode == StatusCodeDAO.Updated;
	}
	public Boolean isCreated() {
		return statusCode == StatusCodeDAO.Created;
	}
	public Boolean isDeleted() {
		return statusCode == StatusCodeDAO.Deleted;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(mappedBy="official", fetch = FetchType.LAZY)
	private List<GameOfficial> gameOfficials = new ArrayList<GameOfficial>();
	public List<GameOfficial> getGameOfficials() {
		return gameOfficials;
	}
	@JsonManagedReference(value="gameOfficial-to-official")
	public void setGameOfficials(List<GameOfficial> gameOfficials) {
		this.gameOfficials = gameOfficials;
	}
	public void addGameOfficial(GameOfficial gameOfficial) {
		this.getGameOfficials().add(gameOfficial);
	}
	public void removeGameOfficial(GameOfficial gameOfficial) {
		this.getGameOfficials().remove(gameOfficial);
	}

	@Column(name="lastName", length=25, nullable=false)
	private String lastName;
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name="firstName", length=25, nullable=false)
	private String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name="fromDate", nullable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate fromDate;
	public LocalDate getFromDate()  {
		return fromDate;
	}
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	@Column(name="toDate", nullable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate toDate;
	public LocalDate getToDate()  {
		return toDate;
	}
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	@Column(name="number", length=3, nullable=false)
	private String number;
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

	public String toString() {
		return new StringBuffer()
			.append("\r" + "  id: " + this.id + "\n")
			.append("  lastName: " + this.lastName + "\n")
			.append("  firstName: " + this.firstName + "\n")
			.append("  fromDate: " + this.fromDate + "\n")
			.append("  toDate: " + this.toDate + "\n")
			.append("  statusCode: " + this.statusCode)
			.toString();
	}

	public PubOfficial toPubOfficial(UriInfo uriInfo) {
		URI self;
		self = uriInfo.getBaseUriBuilder().path("officials").
											path(this.getLastName()).
											path(this.getFirstName()).
											path(DateTimeUtil.getStringDate(this.getFromDate())).build();
		return new PubOfficial( self,
							this.lastName,
							this.firstName,
							this.fromDate,
							this.number);
	}
}