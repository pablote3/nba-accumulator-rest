package com.rossotti.basketball.dao;

import java.util.List;

import org.hibernate.PropertyValueException;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rossotti.basketball.dao.exceptions.DuplicateEntityException;
import com.rossotti.basketball.dao.exceptions.NoSuchEntityException;
import com.rossotti.basketball.models.Team;
import com.rossotti.basketball.models.Team.Conference;
import com.rossotti.basketball.models.Team.Division;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"config/applicationContextTest.xml"})
public class TeamDaoTest {

	@Autowired
	private TeamDAO teamDAO;

	//'harlem-globetrotters', '2009-07-01', '2010-06-30', 'Harlem Globetrotters'
	
	@Test
	public void findTeamByKey_MatchFromDate() {
		Team team = teamDAO.findTeam("harlem-globetrotters", new LocalDate("2009-07-01"), new LocalDate("2009-07-01"));
		Assert.assertEquals("Harlem Globetrotters", team.getFullName());
	}

	@Test
	public void findTeamByKey_MatchToDate() {
		Team team = teamDAO.findTeam("harlem-globetrotters", new LocalDate("2010-06-30"), new LocalDate("2010-06-30"));
		Assert.assertEquals("Harlem Globetrotters", team.getFullName());
	}

	@Test
	public void findTeamByKey_MatchDateRange() {
		Team team = teamDAO.findTeam("harlem-globetrotters", new LocalDate("2009-07-01"), new LocalDate("2010-06-30"));
		Assert.assertEquals("Harlem Globetrotters", team.getFullName());
	}

	@Test(expected=NoSuchEntityException.class)
	public void findTeamByKey_NoSuchEntityException_Key() {
		teamDAO.findTeam("harlem-hoopers", new LocalDate("2009-07-01"), new LocalDate("2010-06-30"));
	}

	@Test(expected=NoSuchEntityException.class)
	public void findTeamByKey_NoSuchEntityException_BeforeAsOfDate() {
		teamDAO.findTeam("harlem-globetrotters", new LocalDate("2009-06-30"), new LocalDate("2009-06-30"));
	}

	@Test(expected=NoSuchEntityException.class)
	public void findTeamByKey_NoSuchEntityException_AfterAsOfDate() {
		teamDAO.findTeam("harlem-globetrotters", new LocalDate("2010-07-01"), new LocalDate("2010-07-01"));
	}

	//'st-louis-bombers', '2009-07-01', '2010-06-30', 'St. Louis Bombers'
	//'st-louis-bombers', '2010-07-01', '2011-06-30', 'St. Louis Bombers'

	@Test
	public void findTeamsByKey() {
		List<Team> teams = teamDAO.findTeams("st-louis-bombers");
		Assert.assertEquals(2, teams.size());
	}

	@Test(expected=NoSuchEntityException.class)
	public void findTeamsByKey_NoSuchEntityException() {
		teamDAO.findTeams("st-louis-bombbers");
	}
	
	@Test
	public void findTeamsByDateRange() {
		List<Team> teams = teamDAO.findTeams(new LocalDate("2009-10-31"), new LocalDate("2010-06-30"));
		Assert.assertEquals(2, teams.size());
	}

	@Test(expected=NoSuchEntityException.class)
	public void findTeamsByDateRange_NoSuchEntityException() {
		teamDAO.findTeams(new LocalDate("1909-10-31"), new LocalDate("1910-06-30"));
	}

	//'baltimore-bullets', '2005-07-01', '2006-06-30', 'Baltimore Bullets'

	@Test
	public void createTeam() {
		teamDAO.createTeam(createMockTeam("seattle-supersonics", new LocalDate("2012-07-01"), new LocalDate("9999-12-31"), "Seattle Supersonics"));
		Team team = teamDAO.findTeam("seattle-supersonics", new LocalDate("2012-07-01"), new LocalDate("2012-07-01"));
		Assert.assertEquals("Seattle Supersonics", team.getFullName());
	}

	@Test
	public void createTeam_NonOverlappingDates() {
		Team createTeam = createMockTeam("baltimore-bullets", new LocalDate("2006-07-01"), new LocalDate("9999-12-31"), "Baltimore Bullets2");
		teamDAO.createTeam(createTeam);
		Team team = teamDAO.findTeam("baltimore-bullets", new LocalDate("2006-07-01"), new LocalDate("9999-12-31"));
		Assert.assertEquals("Baltimore Bullets2", team.getFullName());
	}

	@Test(expected=DuplicateEntityException.class)
	public void createTeam_OverlappingDates() {
		teamDAO.createTeam(createMockTeam("baltimore-bullets", new LocalDate("2005-07-01"), new LocalDate("2005-07-01"), "Baltimore Bullets"));
	}

	@Test(expected=PropertyValueException.class)
	public void createTeam_MissingRequiredData() {
		Team team = new Team();
		team.setTeamKey("missing-required-data-key");
		teamDAO.createTeam(team);
	}

	//'st-louis-bombers', '2009-07-01', '2010-06-30', 'St. Louis Bombers'

	@Test
	public void updateTeam() {
		teamDAO.updateTeam(updateMockTeam("st-louis-bombers", new LocalDate("2009-07-01"), new LocalDate("2010-06-30"), "St. Louis Bombiers"));
		Team team = teamDAO.findTeam("st-louis-bombers", new LocalDate("2010-05-30"), new LocalDate("2010-05-30"));
		Assert.assertEquals("St. Louis Bombiers", team.getFullName());
	}

	@Test(expected=NoSuchEntityException.class)
	public void updateTeam_NoSuchEntityException_Key() {
		teamDAO.updateTeam(updateMockTeam("st-louis-bombs", new LocalDate("2009-07-01"), new LocalDate("2010-07-01"), "St. Louis Bombiers"));
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void updateTeam_MissingRequiredData() {
		Team team = updateMockTeam("st-louis-bombers", new LocalDate("2009-07-01"), new LocalDate("2010-06-30"), null);
		teamDAO.updateTeam(team);
	}

	//'rochester-royals', '2008-07-01', '2009-06-30', 'Rochester Royals'

	@Test(expected=NoSuchEntityException.class)
	public void deleteTeam() {
		teamDAO.deleteTeam("rochester-royals", new LocalDate("2009-06-30"), new LocalDate("2009-06-30"));
		teamDAO.findTeam("rochester-royals", new LocalDate("2009-06-30"), new LocalDate("2009-06-30"));
	}

	@Test(expected=NoSuchEntityException.class)
	public void deleteTeam_NoSuchEntityException_Key() {
		teamDAO.deleteTeam("rochester-royales", new LocalDate("2009-06-30"), new LocalDate("2009-06-30"));
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
