package com.rossotti.basketball.business;

import java.util.List;

import org.hibernate.PropertyValueException;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.rossotti.basketball.dao.exception.DuplicateEntityException;
import com.rossotti.basketball.model.Team;
import com.rossotti.basketball.model.Team.Conference;
import com.rossotti.basketball.model.Team.Division;

public class TeamBusinessTest {

	@Autowired
	private TeamBusiness teamBusiness;

	//'harlem-globetrotters', '2009-07-01', '2010-06-30', 'Harlem Globetrotters'
	
	@Test
	public void findTeamByKey_Found_FromDate() {
		Team findTeam = teamBusiness.findTeam("harlem-globetrotters", new LocalDate("2009-07-01"));
		Assert.assertEquals("Harlem Globetrotters", findTeam.getFullName());
		Assert.assertTrue(findTeam.isFound());
	}

	@Test
	public void findTeamByKey_Found_ToDate() {
		Team findTeam = teamBusiness.findTeam("harlem-globetrotters", new LocalDate("2010-06-30"));
		Assert.assertEquals("Harlem Globetrotters", findTeam.getFullName());
		Assert.assertTrue(findTeam.isFound());
	}

	@Test
	public void findTeamByKey_NotFound_TeamKey() {
		Team findTeam = teamBusiness.findTeam("harlem-hoopers", new LocalDate("2009-07-01"));
		Assert.assertTrue(findTeam.isNotFound());
	}

	@Test
	public void findTeamByKey_NotFound_BeforeAsOfDate() {
		Team findTeam = teamBusiness.findTeam("harlem-globetrotters", new LocalDate("2009-06-30"));
		Assert.assertTrue(findTeam.isNotFound());
	}

	@Test
	public void findTeamByKey_NotFound_AfterAsOfDate() {
		Team findTeam = teamBusiness.findTeam("harlem-globetrotters", new LocalDate("2010-07-01"));
		Assert.assertTrue(findTeam.isNotFound());
	}

	//'st-louis-bombers', '2009-07-01', '2010-06-30', 'St. Louis Bombers'
	//'st-louis-bombers', '2010-07-01', '2011-06-30', 'St. Louis Bombers'

	@Test
	public void findTeamsByKey_Found() {
		List<Team> teams = teamBusiness.findTeams("st-louis-bombers");
		Assert.assertEquals(2, teams.size());
	}

	@Test
	public void findTeamsByKey_NotFound() {
		List<Team> teams = teamBusiness.findTeams("st-louis-bombbers");
		Assert.assertEquals(0, teams.size());
	}
	
	@Test
	public void findTeamsByDateRange_Found() {
		List<Team> teams = teamBusiness.findTeams(new LocalDate("2009-10-31"));
		Assert.assertEquals(3, teams.size());
	}

	@Test
	public void findTeamsByDateRange_NotFound() {
		List<Team> teams = teamBusiness.findTeams(new LocalDate("1909-10-31"));
		Assert.assertEquals(0, teams.size());
	}

	//'baltimore-bullets', '2005-07-01', '2006-06-30', 'Baltimore Bullets'

	@Test
	public void createTeam_Created_New() {
		Team createTeam = teamBusiness.createTeam(createMockTeam("seattle-supersonics", new LocalDate("2012-07-01"), new LocalDate("9999-12-31"), "Seattle Supersonics"));
		Team findTeam = teamBusiness.findTeam("seattle-supersonics", new LocalDate("2012-07-01"));
		Assert.assertTrue(createTeam.isCreated());
		Assert.assertEquals("Seattle Supersonics", findTeam.getFullName());
	}

	@Test
	public void createTeam_Created_Existing() {
		Team createTeam = teamBusiness.createTeam(createMockTeam("baltimore-bullets", new LocalDate("2006-07-01"), new LocalDate("2006-07-02"), "Baltimore Bullets2"));
		Team findTeam = teamBusiness.findTeam("baltimore-bullets", new LocalDate("2006-07-01"));
		Assert.assertTrue(createTeam.isCreated());
		Assert.assertEquals("Baltimore Bullets2", findTeam.getFullName());
	}

	@Test(expected=DuplicateEntityException.class)
	public void createTeam_OverlappingDates() {
		teamBusiness.createTeam(createMockTeam("baltimore-bullets", new LocalDate("2005-07-01"), new LocalDate("2005-07-01"), "Baltimore Bullets"));
	}

	@Test(expected=PropertyValueException.class)
	public void createTeam_MissingRequiredData() {
		Team team = new Team();
		team.setTeamKey("missing-required-data-key");
		teamBusiness.createTeam(team);
	}

	//'st-louis-bombers', '2009-07-01', '2010-06-30', 'St. Louis Bombers'

	@Test
	public void updateTeam() {
		Team updateTeam = teamBusiness.updateTeam(updateMockTeam("st-louis-bombers", new LocalDate("2009-07-01"), new LocalDate("2010-06-30"), "St. Louis Bombiers"));
		Team findTeam = teamBusiness.findTeam("st-louis-bombers", new LocalDate("2010-05-30"));
		Assert.assertTrue(updateTeam.isUpdated());
		Assert.assertEquals("St. Louis Bombiers", findTeam.getFullName());
	}

	@Test
	public void updateTeam_NotFound() {
		Team updateTeam = teamBusiness.updateTeam(updateMockTeam("st-louis-bombs", new LocalDate("2009-07-01"), new LocalDate("2010-07-01"), "St. Louis Bombiers"));
		Assert.assertTrue(updateTeam.isNotFound());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void updateTeam_MissingRequiredData() {
		Team team = updateMockTeam("st-louis-bombers", new LocalDate("2009-07-01"), new LocalDate("2010-06-30"), null);
		teamBusiness.updateTeam(team);
	}

	//'rochester-royals', '2008-07-01', '2009-06-30', 'Rochester Royals'

	@Test
	public void deleteTeam_Deleted() {
		Team deleteTeam = teamBusiness.deleteTeam("rochester-royals", new LocalDate("2008-07-01"));
		Team findTeam = teamBusiness.findTeam("rochester-royals", new LocalDate("2008-07-01"));
		Assert.assertTrue(deleteTeam.isDeleted());
		Assert.assertTrue(findTeam.isNotFound());
	}

	@Test
	public void deleteTeam_NotFound() {
		Team deleteTeam = teamBusiness.deleteTeam("rochester-royales", new LocalDate("2009-07-01"));
		Assert.assertTrue(deleteTeam.isNotFound());
	}

	private Team createMockTeam(String key, LocalDate fromDate, LocalDate toDate, String fullName) {
		Team team = new Team();
		team.setTeamKey(key);
		team.setFromDate(fromDate);
		team.setToDate(toDate);
		team.setAbbr("SEA");
		team.setFirstName("Seattle");
		team.setLastName("Supersonics");
		team.setConference(Conference.West);
		team.setDivision(Division.Pacific);
		team.setSiteName("Key Arena");
		team.setCity("Seattle");
		team.setState("WA");
		team.setFullName(fullName);
		return team;
	}

	private Team updateMockTeam(String key, LocalDate fromDate, LocalDate toDate, String fullName) {
		Team team = new Team();
		team.setTeamKey(key);
		team.setAbbr("SLB");
		team.setFromDate(fromDate);
		team.setToDate(toDate);
		team.setFirstName("St. Louis");
		team.setLastName("Bombiers");
		team.setConference(Conference.East);
		team.setDivision(Division.Southwest);
		team.setSiteName("St. Louis Arena");
		team.setCity("St. Louis");
		team.setState("MO");
		team.setFullName(fullName);
		return team;
	}
}
