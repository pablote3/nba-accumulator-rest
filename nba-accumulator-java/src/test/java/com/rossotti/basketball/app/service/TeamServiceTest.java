package com.rossotti.basketball.app.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.StatusCodeDAO;
import com.rossotti.basketball.dao.model.Team;
import com.rossotti.basketball.dao.repository.TeamRepository;

@SuppressWarnings("CanBeFinal")
@RunWith(MockitoJUnitRunner.class)
public class TeamServiceTest {
	@Mock
	private TeamRepository teamRepo;

	@InjectMocks
	private TeamService teamService;

	@Test(expected=NoSuchEntityException.class)
	public void findTeam_notFound_gameDate() {
		when(teamRepo.findTeam(anyString(), (LocalDate) anyObject()))
				.thenReturn(createMockTeam("new-orleans-hornets", "Hornets", StatusCodeDAO.NotFound));
		Team team = teamService.findTeam("new-orleans-hornets", new LocalDate(2010, 8, 26));
		Assert.assertTrue(team.isNotFound());
	}

	@Test(expected=NoSuchEntityException.class)
	public void findTeam_notFound_teamKey() {
		when(teamRepo.findTeam(anyString(), (LocalDate) anyObject()))
				.thenReturn(createMockTeam("denver-mcnuggets", "Nuggets", StatusCodeDAO.NotFound));
		Team team = teamService.findTeam("denver-mcnuggets", new LocalDate(2015, 8, 26));
		Assert.assertTrue(team.isNotFound());
	}

	@Test
	public void findTeam_found() {
		when(teamRepo.findTeam(anyString(), (LocalDate) anyObject()))
				.thenReturn(createMockTeam("denver-nuggets", "Nuggets", StatusCodeDAO.Found));
		Team team = teamService.findTeam("denver-nuggets", new LocalDate(2015, 11, 26));
		Assert.assertEquals("denver-nuggets", team.getTeamKey());
		Assert.assertTrue(team.isFound());
	}

	@Test(expected=NoSuchEntityException.class)
	public void findTeamByLastName_notFound_gameDate() {
		when(teamRepo.findTeamByLastName(anyString(), (LocalDate) anyObject()))
				.thenReturn(createMockTeam("new-orleans-hornets", "Hornets", StatusCodeDAO.NotFound));
		Team team = teamService.findTeamByLastName("Hornets", new LocalDate(2010, 8, 26));
		Assert.assertTrue(team.isNotFound());
	}

	@Test(expected=NoSuchEntityException.class)
	public void findTeamByLastName_notFound_lastName() {
		when(teamRepo.findTeamByLastName(anyString(), (LocalDate) anyObject()))
				.thenReturn(createMockTeam("denver-mcnuggets", "Nuggers", StatusCodeDAO.NotFound));
		Team team = teamService.findTeamByLastName("Nuggers", new LocalDate(2015, 8, 26));
		Assert.assertTrue(team.isNotFound());
	}

	@Test
	public void findTeamByLastName_found() {
		when(teamRepo.findTeamByLastName(anyString(), (LocalDate) anyObject()))
				.thenReturn(createMockTeam("denver-nuggets", "Nuggets", StatusCodeDAO.Found));
		Team team = teamService.findTeamByLastName("Nuggets", new LocalDate(2015, 11, 26));
		Assert.assertEquals("Nuggets", team.getLastName());
		Assert.assertTrue(team.isFound());
	}

	private Team createMockTeam(String teamKey, String lastName, StatusCodeDAO statusCode) {
		Team team = new Team();
		team.setTeamKey(teamKey);
		team.setLastName(lastName);
		team.setFromDate(new LocalDate(1995, 11, 26));
		team.setToDate(new LocalDate(2014, 11, 26));
		team.setStatusCode(statusCode);
		return team;
	}
}