package com.rossotti.basketball;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.stub;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.rossotti.basketball.app.resources.TeamResource;
import com.rossotti.basketball.dao.TeamDAO;
import com.rossotti.basketball.models.Team;
import com.rossotti.basketball.models.Team.Conference;
import com.rossotti.basketball.models.Team.Division;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTest {

	@Mock
	private TeamDAO teamDao;

	@InjectMocks
	private TeamResource teamResource = new TeamResource();

	@Before
	public void initializeMokito() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test() {
		Team team = getMockTeam();
		stub(teamDao.findTeam(anyString(), (LocalDate) anyObject())).toReturn(team);
		//need service to mock team into
		//see Mockito tutorial at Tools/Testing
	}
	
	private Team getMockTeam() {
		Team team = new Team();
		team.setTeamKey("seattle-supersonics");
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
