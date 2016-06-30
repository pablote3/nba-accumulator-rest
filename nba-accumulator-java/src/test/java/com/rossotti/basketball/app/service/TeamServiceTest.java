package com.rossotti.basketball.app.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.rossotti.basketball.dao.model.StatusCodeDAO;
import com.rossotti.basketball.dao.model.Team;
import com.rossotti.basketball.dao.repository.TeamRepository;

@RunWith(MockitoJUnitRunner.class)
public class TeamServiceTest {
	@Mock
	private TeamRepository teamRepo;

	@InjectMocks
	private TeamService teamService;

	@Before
	public void setUp() {
		when(teamRepo.findTeam(anyString(), (LocalDate) anyObject()))
			.thenReturn(createMockTeam("denver-nuggets", StatusCodeDAO.Found))
			.thenReturn(createMockTeam("new-orleans-hornets", StatusCodeDAO.NotFound))
			.thenReturn(createMockTeam("denver-mcnuggets", StatusCodeDAO.NotFound));
	}

	@Test
	public void findTeam() {
		Team team;
		//team found
		team = teamService.findTeam("denver-nuggets", new LocalDate(2015, 11, 26));
		Assert.assertEquals("denver-nuggets", team.getTeamKey());
		Assert.assertTrue(team.isFound());

		//team not found_gameDate
		team = teamService.findTeam("new-orleans-hornets", new LocalDate(2010, 8, 26));
		Assert.assertTrue(team.isNotFound());

		//team not found_teamKey
		team = teamService.findTeam("denver-mcnuggets", new LocalDate(2015, 8, 26));
		Assert.assertTrue(team.isNotFound());
	}
	
	private Team createMockTeam(String teamKey, StatusCodeDAO statusCode) {
		Team team = new Team();
		team.setTeamKey(teamKey);
		team.setFromDate(new LocalDate(1995, 11, 26));
		team.setToDate(new LocalDate(2014, 11, 26));
		team.setStatusCode(statusCode);
		return team;
	}

}