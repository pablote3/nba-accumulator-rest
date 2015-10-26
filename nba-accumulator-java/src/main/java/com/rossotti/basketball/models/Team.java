package com.rossotti.basketball.models;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

//@Entity
//public class Team extends Model {

public class Team {
	public Team() {
	}
	
//	@Id 
//	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
//	@Version
	private DateTime lastUpdate;
	public DateTime getLastUpdate()  {
		return lastUpdate;
	}
	public void setLastUpdate(DateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

//	@Required
//	@Column(name="team_key", length=35, nullable=false)
	@JsonProperty("team_id")
	private String key;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
//	@Required
//	@Column(name="first_name", length=15, nullable=false)
	@JsonProperty("first_name")
	private String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
//	@Required
//	@Column(name="last_name", length=20, nullable=false)
	@JsonProperty("last_name")
	private String lastName;
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
//	@Required
//	@Column(name="full_name", length=35, nullable=false)
	@JsonProperty("full_name")
	private String fullName;
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
//	@Required
//	@Column(name="from_date", nullable=false)
//	@Temporal(TemporalType.DATE)
	@JsonProperty("from_date")
	
	private LocalDate fromDate;
	public LocalDate getFromDate()  {
		return fromDate;
	}
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}
	
//	@Required
//	@Column(name="to_date", nullable=false)
//	@Temporal(TemporalType.DATE)
	@JsonProperty("to_date")
	
	private LocalDate toDate;
	public LocalDate getToDate()  {
		return toDate;
	}
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}
	
//	@Required
//	@Column(name="abbr", length=5, nullable=false)
	@JsonProperty("abbreviation")
	private String abbr;
	public String getAbbr() {
		return abbr;
	}
	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}
	
//	@Required
//	@Enumerated(EnumType.STRING)
//	@Column(name="conference", length=4, nullable=false)
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
	
//  @Required
//	@Enumerated(EnumType.STRING)
//	@Column(name="division", length=9, nullable=false)
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
	
//	@Column(name="site_name", length=30, nullable=false)
	@JsonProperty("site_name")
	private String siteName;
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
    
//	@Column(name="city", length=15, nullable=false)
	private String city;
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
    
//	@Column(name="state", length=2, nullable=false)
	private String state;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
//	public static Team findById(Long id) {
//		Team team = Ebean.find(Team.class, id);
//		return team;
//	}
	
//	public static Team findByKey(String key, String value, ProcessingType processingType) {
//		Query<Team> query = null;
//		if (processingType.equals(ProcessingType.batch))
//			query = ebeanServer.find(Team.class);
//		else if (processingType.equals(ProcessingType.online))
//			query = Ebean.find(Team.class);
//		
//		query.where().eq(key, value);
//		query.where().eq("active", true);
//		Team team = query.findUnique();
//		return team;
//	}
	  
//	public static List<Team> findAll() {
//		Query<Team> query = Ebean.find(Team.class);
//		List<Team> teams = query.findList();
//	    return teams;
//	}	
	
//	public static Team findByAbbr(String abbr, ProcessingType processingType) {
//		Team team;
//		Query<Team> query = null; 
//	  	if (processingType.equals(ProcessingType.batch)) 
//	  		query = ebeanServer.find(Team.class);
//  		else if (processingType.equals(ProcessingType.online))
//  			query = Ebean.find(Team.class);	
//
//		query.where().eq("abbr", abbr);
//		query.where().eq("active", true);
//		team = query.findUnique();
//		return team;
//	}
	
	
//	public static Team findByTeamKey(String teamKey, ProcessingType processingType) {
//		Team team;
//		Query<Team> query = null; 
//	  	if (processingType.equals(ProcessingType.batch)) 
//	  		query = ebeanServer.find(Team.class);
//  		else if (processingType.equals(ProcessingType.online))
//  			query = Ebean.find(Team.class);	
//
//		query.where().eq("team_key", teamKey);
//		team = query.findUnique();
//		return team;
//	}
	
//	public static List<Team> findByActive(boolean active) {
//		Query<Team> query = Ebean.find(Team.class);
//		query.where().eq("active", active);
//		List<Team> teams = query.findList();
//	    return teams;
//	}
	
//	public static List<Team> findFilter(String filter) {
//		Query<Team> query = Ebean.find(Team.class);
//		query.where().ilike("fullName", "%" + filter + "%");
//		List<Team> teams = query.findList();
//		return teams;
//	}
	  
//	public static void create(Team team, ProcessingType processingType) {
//		if (processingType.equals(ProcessingType.batch))
//			ebeanServer.save(team);
//		else if (processingType.equals(ProcessingType.online))
//			Ebean.save(team);
//	}
	  
//	public static void delete(Long id) {
//		Team team = Team.findById(id);
//	  	team.delete();
//	}
	
//    public static Page<Team> page(int page, int pageSize, String sortBy, String order, String filter) {
//    	Query<Team> query = Ebean.find(Team.class);
//    	query.where().ilike("fullName", "%" + filter + "%");
//    	query.where().orderBy(sortBy + " " + order);
//    	Page<Team> p = query.findPagingList(pageSize).getPage(page);
//    	return p;
//    }
	  
	public String toString() {
		return new StringBuffer()
			.append("\n" + "  id: " + this.id)
			.append("  key: " + this.key)
			.append("  fullName: " + this.fullName)
			.append("  abbr: " + this.abbr)
			.append("  fromDate: " + this.fromDate)
			.append("  conference: " + this.conference)
			.append("  division: " + this.division + "\n")
			.toString();
	}
}