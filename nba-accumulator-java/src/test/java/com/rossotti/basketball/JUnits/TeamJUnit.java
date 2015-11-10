package com.rossotti.basketball.JUnits;

import org.joda.time.LocalDate;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rossotti.basketball.dao.TeamDAO;
import com.rossotti.basketball.models.Team;
import com.rossotti.basketball.models.Team.Conference;
import com.rossotti.basketball.models.Team.Division;

public class TeamJUnit {
	private static TeamDAO teamDAO;
	private static ClassPathXmlApplicationContext context;
	
	@BeforeClass
	public static void onceExecuteBeforeAll() {
		context = new ClassPathXmlApplicationContext("WEB-INF/applicationContext.xml");
		teamDAO = (TeamDAO)context.getBean(TeamDAO.class);
	}
	
	@AfterClass
	public static void onceExecuteAfterAll() {
		context.close();
	}
		
	@Test
	public void createTeam() {
		teamDAO.createTeam(createMockTeam());

		Team createTeam = teamDAO.findTeamByKey("seattle-supersonics", new LocalDate("2012-07-01"));
		Assert.assertEquals(createTeam.getFullName(), "Seattle Supersonics");
		Assert.assertEquals(createTeam.getAbbr(), "SEA");
//		Team.delete(createTeam.getId());
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
