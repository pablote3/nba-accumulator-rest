package com.rossotti.basketball.dao;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rossotti.basketball.dao.TeamDAO;
import com.rossotti.basketball.models.Team;
import com.rossotti.basketball.models.Team.Division;
import com.rossotti.basketball.models.Team.Conference;

public class JavaAppTeam {
	
	@Autowired
	private static TeamDAO teamDAO;
	
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("WEB-INF/applicationContext.xml");

		TeamDAO teamDAO = (TeamDAO)context.getBean(TeamDAO.class);

		teamDAO.createTeam(createMockTeam());
		context.close();
	}

	private static Team createMockTeam() {
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
