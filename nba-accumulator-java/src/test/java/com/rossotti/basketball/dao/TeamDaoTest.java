package com.rossotti.basketball.dao;

import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rossotti.basketball.models.Team;
import com.rossotti.basketball.models.Team.Conference;
import com.rossotti.basketball.models.Team.Division;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"config/applicationContextTest.xml"})
public class TeamDaoTest {

	@Autowired
	private TeamDAO teamDAO;

	@Test
	public void findTeamByTeamKey() {
		Team team = teamDAO.findTeamByKey("harlem-globetrotters", new LocalDate("2012-07-01"));
		Assert.assertEquals("Harlem Globetrotters", team.getFullName());
	}
	
	@Test
	public void findTeamsByDateRange() {
		List<Team> teams = teamDAO.findTeamsByDateRange(new LocalDate("2011-12-31"), new LocalDate("2012-07-01"));
		Assert.assertEquals(1, teams.size());
	}
	
	@Test
	public void createTeam() {
		teamDAO.createTeam(createMockTeam());

		Team createTeam = teamDAO.findTeamByKey("seattle-supersonics", new LocalDate("2012-07-01"));
		Assert.assertEquals("Seattle Supersonics", createTeam.getFullName());
		Assert.assertEquals("SEA", createTeam.getAbbr());
	}
	
	private Team createMockTeam() {
		Team team = new Team();
		team.setKey("seattle-supersonics");
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
