package com.rossotti.basketball.JUnits;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rossotti.basketball.dao.impl.TeamDAOImpl;
import com.rossotti.basketball.models.Team;
import com.rossotti.basketball.models.Team.Conference;
import com.rossotti.basketball.models.Team.Division;

public class TeamJUnit {

	@Test
	public void createTeam() {
		ApplicationContext context = new ClassPathXmlApplicationContext("WEB-INF/applicationContext.xml");
		TeamDAOImpl dao = context.getBean("teamDaoImpl", TeamDAOImpl.class);
		dao.create(createMockTeam());

//		Team createTeam = Team.findByTeamKey("seattle-supersonics", ProcessingType.online);
//		assertThat(createTeam.getFullName()).isEqualTo("Seattle Supersonics");
//		assertThat(createTeam.getAbbr()).isEqualTo("SEA");
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
