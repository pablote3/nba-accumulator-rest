package com.rossotti.basketball.dao;

import java.util.List;

import org.hibernate.PropertyValueException;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Test
	public void findByTeamKey_MatchFromDate() {
		Team team = teamDAO.findTeamByKeyAsOfDate("harlem-globetrotters", new LocalDate("2009-07-01"));
		Assert.assertEquals("Harlem Globetrotters", team.getFullName());
	}
	
	@Test
	public void findByTeamKey_MatchToDate() {
		Team team = teamDAO.findTeamByKeyAsOfDate("harlem-globetrotters", new LocalDate("2010-06-30"));
		Assert.assertEquals("Harlem Globetrotters", team.getFullName());
	}
	
	@Test(expected=NoSuchEntityException.class)
	public void findByTeamKey_NoSuchEntityException_Key() {
		teamDAO.findTeamByKeyAsOfDate("harlem-hoopers", new LocalDate("2012-07-01"));
	}
	
	@Test(expected=NoSuchEntityException.class)
	public void findByTeamKey_NoSuchEntityException_BeforeAsOfDate() {
		teamDAO.findTeamByKeyAsOfDate("harlem-globetrotters", new LocalDate("2009-06-30"));
	}
	
	@Test(expected=NoSuchEntityException.class)
	public void findByTeamKey_NoSuchEntityException_AfterAsOfDate() {
		teamDAO.findTeamByKeyAsOfDate("harlem-globetrotters", new LocalDate("2010-07-01"));
	}
	
	@Test
	public void findByDateRange() {
		List<Team> teams = teamDAO.findTeamsByDateRange(new LocalDate("2009-10-31"), new LocalDate("2010-06-30"));
		Assert.assertEquals(1, teams.size());
	}
	
	@Test
	public void createTeam() {
		teamDAO.createTeam(createMockTeam("seattle-supersonics"));
		Team team = teamDAO.findTeamByKeyAsOfDate("seattle-supersonics", new LocalDate("2012-07-01"));
		Assert.assertEquals("Seattle Supersonics", team.getFullName());
	}
	
	@Test(expected=PropertyValueException.class)
	public void createTeam_MissingRequiredData() {
		Team team = new Team();
		team.setKey("missing-required-data-key");
		teamDAO.createTeam(team);
	}
	
	@Test(expected=DuplicateEntityException.class)
	public void createTeam_DuplicateEntityException() {
		teamDAO.createTeam(createMockTeam("duplicate-entity-exception-key"));
		teamDAO.createTeam(createMockTeam("duplicate-entity-exception-key"));
	}
	
	private Team createMockTeam(String key) {
		Team team = new Team();
		team.setKey(key);
		team.setAbbr("SEA");
		team.setFromDate(new LocalDate("2012-07-01"));
		team.setToDate(new LocalDate("9999-12-31"));
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
}
