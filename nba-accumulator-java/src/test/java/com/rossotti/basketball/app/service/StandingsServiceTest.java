package com.rossotti.basketball.app.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
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
import com.rossotti.basketball.dao.model.BoxScore;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.Standing;
import com.rossotti.basketball.dao.model.StandingRecord;
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
			.thenReturn(createMockStanding("denver-nuggets", (short)4, (short)7, 18, 35, StatusCode.Found))
			.thenReturn(createMockStanding("denver-mcnuggets", null, null, null, null, StatusCode.NotFound));
		when(standingRepo.findStandings((LocalDate) anyObject()))
			.thenReturn(createMockStandings())
			.thenReturn(new ArrayList<Standing>());
		when(standingRepo.createStanding((Standing) anyObject()))
			.thenReturn(createMockStanding("sacramento-kings",(short)1, (short)4, null, null, StatusCode.Created))
			.thenReturn(createMockStanding("utah-jazz",(short)3, (short)4, null, null, StatusCode.Created))
			.thenThrow(new DuplicateEntityException());
		when(standingRepo.updateStanding((Standing) anyObject()))
			.thenReturn(createMockStanding("toronto-raptors", (short)10, (short)15, 25, 35, StatusCode.Updated))
			.thenReturn(createMockStanding("seattle-supersonics", null, null, null, null, StatusCode.NotFound));
		when(standingRepo.deleteStanding(anyString(), (LocalDate) anyObject()))
			.thenReturn(createMockStanding("toronto-raptors", (short)4, (short)5, 18, 27, StatusCode.Deleted))
			.thenReturn(createMockStanding("seattle-supersonics", null, null, null, null, StatusCode.NotFound));
		when(teamRepo.findTeam(anyString(), (LocalDate) anyObject()))
			.thenReturn(createMockTeam("denver-nuggets", StatusCode.Found))
			.thenReturn(createMockTeam("miami-heat", StatusCode.Found))
			.thenReturn(createMockTeam("denver-mcnuggets", StatusCode.NotFound));
		when(gameRepo.findByDateTeamSeason((LocalDate) anyObject(), anyString()))
			.thenReturn(createMockGames_Kings())
			.thenReturn(createMockGames_Jazz())
			.thenReturn(new ArrayList<Game>());
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
		Assert.assertEquals(5, standings.size());
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
		standing = standingsService.createStanding(createMockStanding("sacramento-kings", (short)5, (short)15, null, null, StatusCode.NotFound));
		Assert.assertEquals("sacramento-kings", standing.getTeam().getTeamKey());
		Assert.assertTrue(standing.isCreated());

		standing = standingsService.createStanding(createMockStanding("utah-jazz", (short)5, (short)15, null, null, StatusCode.NotFound));
		Assert.assertEquals("utah-jazz", standing.getTeam().getTeamKey());
		Assert.assertTrue(standing.isCreated());

		//standing already exists
		standing = standingsService.createStanding(createMockStanding("houston-rockets", (short)7, (short)10, null, null, StatusCode.Found));
	}

	@Test
	public void updateStanding() {
		Standing standing;
		//standing updated
		standing = standingsService.updateStanding(createMockStanding("toronto-raptors", (short)18, (short)22, 42, 68, StatusCode.Found));
		Assert.assertEquals("toronto-raptors", standing.getTeam().getTeamKey());
		Assert.assertTrue(standing.isUpdated());

		//no standing found
		standing = standingsService.updateStanding(createMockStanding("seattle-supersonics", null, null, null, null, StatusCode.NotFound));
		Assert.assertEquals("seattle-supersonics", standing.getTeam().getTeamKey());
		Assert.assertTrue(standing.isNotFound());
	}

	@Test
	public void deleteStandings() {
		List<Standing> standings;
		//standing deleted
		standings = standingsService.deleteStandings(new LocalDate(2015, 11, 26));
		Assert.assertTrue(standings.get(0).isDeleted());

		//no standing found
		Assert.assertTrue(standings.get(1).isNotFound());
	}

	@Test
	public void buildStandingsMap() {
		Map<String, StandingRecord> standingsMap;
		//standing map with entries
		standingsMap = standingsService.buildStandingsMap(createMockStandings(), new LocalDate(2015, 12, 05));
		Assert.assertEquals(5, standingsMap.size());
		Assert.assertEquals(3, standingsMap.get("utah-jazz").getGamesWon().intValue());
		Assert.assertEquals(12, standingsMap.get("utah-jazz").getOpptGamesPlayed().intValue());

		//no standing map entries
		standingsMap = standingsService.buildStandingsMap(new ArrayList<Standing>(), new LocalDate(2015, 12, 05));
		Assert.assertEquals(new HashMap<String, StandingRecord>(), standingsMap);
	}

	@Test
	public void createTeamStandings() {
		List<Standing> standings;
		//create standings
		standings = Arrays.asList(
			createMockStanding("sacramento-kings", (short)1, (short)4, 6, 10, StatusCode.NotFound),
			createMockStanding("utah-jazz", (short)3, (short)4, 4, 11, StatusCode.NotFound)
		);
		standings = standingsService.createTeamStandings(standings);
		Assert.assertEquals(2, standings.size());
		Assert.assertEquals(3, standings.get(1).getGamesWon().intValue());

		//no standings
		standings = standingsService.createTeamStandings(new ArrayList<Standing>());
		Assert.assertEquals(new ArrayList<Standing>(), standings);
	}

//	@Test
//	public void calculateStrengthOfSchedule() {
//		standingsService.calculateStrengthOfSchedule(createMockStanding("toronto-raptors", StatusCode.Found), standingsMap);
//	}

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
			createMockStanding("sacramento-kings", (short)1, (short)4, 6, 10, StatusCode.Found),
			createMockStanding("utah-jazz", (short)3, (short)4, 4, 11, StatusCode.Found),
			createMockStanding("detroit-pistons", (short)1, (short)3, 2, 3, StatusCode.Found),
			createMockStanding("phoenix-suns", (short)1, (short)2, 2, 4, StatusCode.Found),
			createMockStanding("miami-heat", (short)2, (short)3, 4, 6, StatusCode.Found)
		);
		return standings;
	}

	private Standing createMockStanding(String teamKey, Short gamesWon, Short gamesPlayed, Integer opptGamesWon, Integer opptGamesPlayed, StatusCode statusCode) {
		Standing standing = new Standing();
		Team team = new Team();
		team.setTeamKey(teamKey);
		standing.setTeam(team);
		standing.setGamesWon(gamesWon);
		standing.setGamesPlayed(gamesPlayed);
		standing.setOpptGamesWon(opptGamesWon);
		standing.setOpptGamesPlayed(opptGamesPlayed);
		standing.setStatusCode(statusCode);
		return standing;
	}

	private Map<String, StandingRecord> createMockStandingsMap() {
		Map<String, StandingRecord> standingsMap = new HashMap<String, StandingRecord>();
		standingsMap.put("sacramento-kings", new StandingRecord(1, 4, 0, 0));
		standingsMap.put("detroit-pistons", new StandingRecord(1, 3, 0, 0));
		standingsMap.put("miami-heat", new StandingRecord(2, 3, 0, 0));
		standingsMap.put("utah-jazz", new StandingRecord(3, 4, 0, 0));
		standingsMap.put("phoenix-suns", new StandingRecord(1, 2, 0, 0));
		return standingsMap;
	}

	private Team createMockTeam(String teamKey, StatusCode statusCode) {
		Team team = new Team();
		team.setTeamKey(teamKey);
		team.setStatusCode(statusCode);
		return team;
	}

	private List<Game> createMockGames_Kings() {
		List<Game> games = Arrays.asList(
				createMockGame(new LocalDateTime("2015-12-02T10:00"), "detroit-pistons", "sacramento-kings"),
				createMockGame(new LocalDateTime("2015-12-03T10:00"), "sacramento-kings", "miami-heat"),
				createMockGame(new LocalDateTime("2015-12-04T10:00"), "phoenix-suns", "sacramento-kings"),
				createMockGame(new LocalDateTime("2015-12-05T10:00"), "utah-jazz", "sacramento-kings")
			);
			return games;
	}

	private List<Game> createMockGames_Jazz() {
		List<Game> games = Arrays.asList(
				createMockGame(new LocalDateTime("2015-12-02T10:00"), "phoenix-suns", "utah-jazz"),
				createMockGame(new LocalDateTime("2015-12-03T10:00"), "detroit-pistons", "utah-jazz"),
				createMockGame(new LocalDateTime("2015-12-04T10:00"), "utah-jazz", "miami-heat"),
				createMockGame(new LocalDateTime("2015-12-05T10:00"), "utah-jazz", "sacramento-kings")
			);
			return games;
	}

	private Game createMockGame(LocalDateTime gameDateTime, String homeTeamKey, String awayTeamKey) {
		Game game = new Game();
		game.setGameDateTime(gameDateTime);
		BoxScore boxScoreHome = new BoxScore();
		Team teamHome = new Team();
		teamHome.setTeamKey(homeTeamKey);
		boxScoreHome.setTeam(teamHome);
		game.addBoxScore(boxScoreHome);
		BoxScore boxScoreAway = new BoxScore();
		Team teamAway = new Team();
		teamAway.setTeamKey(awayTeamKey);
		boxScoreAway.setTeam(teamAway);
		game.addBoxScore(boxScoreAway);
		return game;
	}
}