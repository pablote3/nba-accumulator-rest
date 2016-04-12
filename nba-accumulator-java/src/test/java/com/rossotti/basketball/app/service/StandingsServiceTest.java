package com.rossotti.basketball.app.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.rossotti.basketball.client.dto.StandingDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.dao.exception.DuplicateEntityException;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.Standing;
import com.rossotti.basketball.dao.model.StatusCode;
import com.rossotti.basketball.dao.model.Team;
import com.rossotti.basketball.dao.repository.GameRepository;
import com.rossotti.basketball.dao.repository.StandingRepository;
import com.rossotti.basketball.dao.repository.TeamRepository;

@RunWith(MockitoJUnitRunner.class)
public class StandingsServiceTest {
	@Mock
	private StandingRepository standingRepo;

	@Mock
	private GameRepository gameRepo;

	@Mock
	private TeamRepository teamRepo;

	@InjectMocks
	private StandingsService standingsService;

	@Before
	public void setUp() {
		when(standingRepo.findStanding(anyString(), (LocalDate) anyObject()))
			.thenReturn(createMockStanding("denver-nuggets", StatusCode.Found))
			.thenReturn(createMockStanding("denver-mcnuggets", StatusCode.NotFound));
		when(standingRepo.findStandings((LocalDate) anyObject()))
			.thenReturn(createMockStandings())
			.thenReturn(new ArrayList<Standing>());
		when(standingRepo.createStanding((Standing) anyObject()))
			.thenReturn(createMockStanding("seattle-supersonics", StatusCode.Created))
			.thenThrow(new DuplicateEntityException());
		when(standingRepo.updateStanding((Standing) anyObject()))
			.thenReturn(createMockStanding("toronto-raptors", StatusCode.Updated))
			.thenReturn(createMockStanding("seattle-supersonics", StatusCode.NotFound));
		when(standingRepo.deleteStanding(anyString(), (LocalDate) anyObject()))
			.thenReturn(createMockStanding("toronto-raptors", StatusCode.Deleted))
			.thenReturn(createMockStanding("seattle-supersonics", StatusCode.NotFound));
		when(teamRepo.findTeam(anyString(), (LocalDate) anyObject()))
			.thenReturn(createMockTeam("denver-nuggets", StatusCode.Found))
			.thenReturn(createMockTeam("miami-heat", StatusCode.Found))
			.thenReturn(createMockTeam("denver-mcnuggets", StatusCode.NotFound));
	}

	@Test(expected=NoSuchEntityException.class)
	public void getStandings() {
		List<Standing> standings;
		//standings found
		standings = standingsService.getStandings(createMockStandingsDTO());
		Assert.assertEquals(2, standings.size());
		Assert.assertEquals("denver-nuggets", standings.get(0).getTeam().getTeamKey());
		Assert.assertTrue(standings.get(0).isFound());
		Assert.assertEquals("miami-heat", standings.get(1).getTeam().getTeamKey());
		Assert.assertTrue(standings.get(1).isFound());

		//standings team not found
		standings = standingsService.getStandings(createMockStandingsDTO());
	}

	@Test
	public void findStanding() {
		Standing standing;
		//standing found
		standing = standingsService.findStanding("denver-nuggets", new LocalDate(2015, 11, 26));
		Assert.assertEquals("denver-nuggets", standing.getTeam().getTeamKey());
		Assert.assertTrue(standing.isFound());

		//standing not found
		standing = standingsService.findStanding("denver-mcnuggets", new LocalDate(2015, 11, 26));
		Assert.assertTrue(standing.isNotFound());
	}

	@Test
	public void findStandings() {
		List<Standing> standings;
		//standings found
		standings = standingsService.findStandings(new LocalDate(2015, 11, 26));
		Assert.assertEquals(2, standings.size());
		Assert.assertEquals("utah-jazz", standings.get(1).getTeam().getTeamKey());
		Assert.assertTrue(standings.get(1).isFound());

		//standings not found
		standings = standingsService.findStandings(new LocalDate(2015, 8, 26));
		Assert.assertEquals(new ArrayList<Standing>(), standings);
	}

	@Test(expected=DuplicateEntityException.class)
	public void createStanding() {
		Standing standing;
		//standing created
		standing = standingsService.createStanding(createMockStanding("seattle-supersonics", StatusCode.NotFound));
		Assert.assertEquals("seattle-supersonics", standing.getTeam().getTeamKey());
		Assert.assertTrue(standing.isCreated());

		//standing already exists
		standing = standingsService.createStanding(createMockStanding("houston-rockets", StatusCode.NotFound));
	}

	@Test
	public void updateStanding() {
		Standing standing;
		//standing updated
		standing = standingsService.updateStanding(createMockStanding("toronto-raptors", StatusCode.Found));
		Assert.assertEquals("toronto-raptors", standing.getTeam().getTeamKey());
		Assert.assertTrue(standing.isUpdated());

		//no standing found
		standing = standingsService.updateStanding(createMockStanding("seattle-supersonics", StatusCode.NotFound));
		Assert.assertEquals("seattle-supersonics", standing.getTeam().getTeamKey());
		Assert.assertTrue(standing.isNotFound());
	}

	@Test
	public void deleteStanding() {
		Standing standing;
		//standing deleted
		standing = standingsService.deleteStanding("toronto-raptors", new LocalDate(2015, 11, 26));
		Assert.assertEquals("toronto-raptors", standing.getTeam().getTeamKey());
		Assert.assertTrue(standing.isDeleted());

		//no standing found
		standing = standingsService.deleteStanding("seattle-supersonics", new LocalDate(2015, 11, 26));
		Assert.assertEquals("seattle-supersonics", standing.getTeam().getTeamKey());
		Assert.assertTrue(standing.isNotFound());
	}

	private StandingsDTO createMockStandingsDTO() {
		StandingsDTO standings = new StandingsDTO();
		standings.standings_date = new DateTime(2015, 3, 26, 12, 0, 0, 0);
		StandingDTO[] standing = new StandingDTO[2];
		standing[0] = createMockStandingDTO("denver-nuggets");
		standing[1] = createMockStandingDTO("miami-heat");
		standings.standing = standing;
		return standings;
	}

	private StandingDTO createMockStandingDTO(String teamKey) {
		StandingDTO standing = new StandingDTO();
		standing.setTeam_id(teamKey);
		standing.setRank((short)1);
		standing.setOrdinal_rank("1st");
		standing.setWon((short)70);
		standing.setLost((short)12);
		standing.setStreak("W3");
		standing.setStreak_type("win");
		standing.setStreak_total((short)3);
		standing.setGames_back((float)0);
		standing.setPoints_for((short)5350);
		standing.setPoints_against((short)5041);
		standing.setHome_won((short)40);
		standing.setHome_lost((short)2);
		standing.setAway_won((short)30);
		standing.setAway_lost((short)10);
		standing.setConference_won((short)42);
		standing.setConference_lost((short)3);
		standing.setLast_five("4-1");
		standing.setLast_ten("9-1");
		standing.setGames_played((short)82);
		standing.setPoints_scored_per_game((float)103.4);
		standing.setPoints_allowed_per_game((float)99.2);
		standing.setWin_percentage((float).8537);
		standing.setPoint_differential((short)359);
		standing.setPoint_differential_per_game((float)3.8);
		return standing;
	}

	private List<Standing> createMockStandings() {
		List<Standing> standings = Arrays.asList(
			createMockStanding("sacramento-kings", StatusCode.Found),
			createMockStanding("utah-jazz", StatusCode.Found)
		);
		return standings;
	}

	private Standing createMockStanding(String teamKey, StatusCode statusCode) {
		Standing standing = new Standing();
		Team team = new Team();
		team.setTeamKey(teamKey);
		standing.setTeam(team);
		standing.setStatusCode(statusCode);
		return standing;
	}

	private Team createMockTeam(String teamKey, StatusCode statusCode) {
		Team team = new Team();
		team.setTeamKey(teamKey);
		team.setStatusCode(statusCode);
		return team;
	}
}