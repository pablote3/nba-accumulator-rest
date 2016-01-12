package com.rossotti.basketball.dao;

import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rossotti.basketball.model.Team;
import com.rossotti.basketball.model.Team.Conference;
import com.rossotti.basketball.model.Team.Division;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"config/applicationContextTest.xml"})
public class TeamDaoTest {

	@Autowired
	private TeamDAO teamDAO;

	//'harlem-globetrotters', '2009-07-01', '2010-06-30', 'Harlem Globetrotters'
	
	@Test
	public void findTeamByKey_Found_FromDate() {
		Team findTeam = teamDAO.findTeam("harlem-globetrotters", new LocalDate("2009-07-01"));
		Assert.assertEquals("Harlem Globetrotters", findTeam.getFullName());
		Assert.assertTrue(findTeam.isFound());
	}

	@Test
	public void findTeamByKey_Found_ToDate() {
		Team findTeam = teamDAO.findTeam("harlem-globetrotters", new LocalDate("2010-06-30"));
		Assert.assertEquals("Harlem Globetrotters", findTeam.getFullName());
		Assert.assertTrue(findTeam.isFound());
	}

	@Test
	public void findTeamByKey_NotFound_TeamKey() {
		Team findTeam = teamDAO.findTeam("harlem-hoopers", new LocalDate("2009-07-01"));
		Assert.assertTrue(findTeam.isNotFound());
	}

	@Test
	public void findTeamByKey_NotFound_BeforeAsOfDate() {
		Team findTeam = teamDAO.findTeam("harlem-globetrotters", new LocalDate("2009-06-30"));
		Assert.assertTrue(findTeam.isNotFound());
	}

	@Test
	public void findTeamByKey_NotFound_AfterAsOfDate() {
		Team findTeam = teamDAO.findTeam("harlem-globetrotters", new LocalDate("2010-07-01"));
		Assert.assertTrue(findTeam.isNotFound());
	}

	//'st-louis-bombers', '2009-07-01', '2010-06-30', 'St. Louis Bombers'
	//'st-louis-bombers', '2010-07-01', '2011-06-30', 'St. Louis Bombers'

	@Test
	public void findTeamsByKey_Found() {
		List<Team> teams = teamDAO.findTeams("st-louis-bombers");
		Assert.assertEquals(2, teams.size());
	}

	@Test
	public void findTeamsByKey_NotFound() {
		List<Team> teams = teamDAO.findTeams("st-louis-bombbers");
		Assert.assertEquals(0, teams.size());
	}
	
	@Test
	public void findTeamsByDateRange_Found() {
		List<Team> teams = teamDAO.findTeams(new LocalDate("2009-10-31"));
		Assert.assertEquals(3, teams.size());
	}

	@Test
	public void findTeamsByDateRange_NotFound() {
		List<Team> teams = teamDAO.findTeams(new LocalDate("1909-10-31"));
		Assert.assertEquals(0, teams.size());
	}

	@Test
	public void createTeam() {
		Team createTeam = teamDAO.createTeam(createMockTeam());
		Team findTeam = teamDAO.findTeam("seattle-supersonics", new LocalDate("2012-07-01"));
		Assert.assertTrue(createTeam.isCreated());
		Assert.assertEquals("Seattle Supersonics", findTeam.getFullName());
	}

	@Test
	public void updateTeam() {
		Team updateTeam = teamDAO.updateTeam(updateMockTeam());
		Team findTeam = teamDAO.findTeam("st-louis-bombers", new LocalDate("2009-07-01"));
		Assert.assertTrue(updateTeam.isUpdated());
		Assert.assertEquals("St. Louis Bombiers", findTeam.getFullName());
	}

	@Test
	public void deleteTeam() {
		Team deleteTeam = teamDAO.deleteTeam(deleteMockTeam());
		Team findTeam = teamDAO.findTeam("rochester-royals", new LocalDate("2008-07-01"));
		Assert.assertTrue(deleteTeam.isDeleted());
		Assert.assertTrue(findTeam.isNotFound());
	}

	private Team createMockTeam() {
		Team team = new Team();
		team.setTeamKey("seattle-supersonics");
		team.setFromDate(new LocalDate("2012-07-01"));
		team.setToDate(new LocalDate("9999-12-31"));
		team.setAbbr("SEA");
		team.setFirstName("Seattle");
		team.setLastName("Supersonics");
		team.setConference(Conference.West);
		team.setDivision(Division.Pacific);
		team.setSiteName("Key Arena");
		team.setCity("Seattle");
		team.setState("WA");
		team.setFullName("Seattle Supersonics");
		return team;
	}

	private Team updateMockTeam() {
		Team team = new Team();
		team.setId(3L);
		team.setTeamKey("st-louis-bombers");
		team.setAbbr("SLB");
		team.setFromDate(new LocalDate("2009-07-01"));
		team.setToDate(new LocalDate("2010-06-30"));
		team.setFirstName("St. Louis");
		team.setLastName("Bombiers");
		team.setConference(Conference.East);
		team.setDivision(Division.Southwest);
		team.setSiteName("St. Louis Arena");
		team.setCity("St. Louis");
		team.setState("MO");
		team.setFullName("St. Louis Bombiers");
		return team;
	}

	private Team deleteMockTeam() {
		Team team = new Team();
		team.setId(7L);
		team.setTeamKey("rochester-royals");
		team.setAbbr("ROC");
		team.setFromDate(new LocalDate("2008-07-01"));
		team.setToDate(new LocalDate("2009-06-30"));
		team.setFirstName("Rochester");
		team.setLastName("Royals");
		team.setConference(Conference.East);
		team.setDivision(Division.Atlantic);
		team.setSiteName("Edgerton Park Arena");
		team.setCity("Rochester");
		team.setState("NY");
		team.setFullName("Rochester Royals");
		return team;
	}
}
