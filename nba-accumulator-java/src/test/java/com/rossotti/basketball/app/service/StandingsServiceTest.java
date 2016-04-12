package com.rossotti.basketball.app.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

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
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.Player;
import com.rossotti.basketball.dao.model.RosterPlayer;
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
//		when(rosterPlayerRepo.findRosterPlayer(anyString(), anyString(), anyString(), (LocalDate) anyObject()))
//			.thenReturn(createMockRosterPlayer("Adams", "Samuel", StatusCode.Found))
//			.thenReturn(createMockRosterPlayer("Coors", "Adolph", StatusCode.Found))
//			.thenReturn(createMockRosterPlayer("", "", StatusCode.NotFound));
//		when(rosterPlayerRepo.findRosterPlayer(anyString(), anyString(), (LocalDate) anyObject(), (LocalDate) anyObject()))
//			.thenReturn(createMockRosterPlayer("Simmons", "Gene", StatusCode.Found))
//			.thenReturn(createMockRosterPlayer("Simmons", "Richard", StatusCode.NotFound));
//		when(rosterPlayerRepo.findRosterPlayers(anyString(), (LocalDate) anyObject()))
//			.thenReturn(createMockRosterPlayers())
//			.thenReturn(new ArrayList<RosterPlayer>());
//		when(rosterPlayerRepo.createRosterPlayer((RosterPlayer) anyObject()))
//			.thenReturn(createMockRosterPlayer("Payton", "Walter", StatusCode.Created))
//			.thenThrow(new DuplicateEntityException());
//		when(rosterPlayerRepo.updateRosterPlayer((RosterPlayer) anyObject()))
//			.thenReturn(createMockRosterPlayer("Schaub", "Buddy", StatusCode.Updated))
//			.thenReturn(createMockRosterPlayer("Lima", "Roger", StatusCode.NotFound));
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

//	@Test(expected=NoSuchEntityException.class)
//	public void getRosterPlayers() {
//		List<RosterPlayer> rosterPlayers;
//		//roster players found
//		rosterPlayers = rosterPlayerService.getRosterPlayers(createMockRosterPlayerDTOs(), new LocalDate(2015, 11, 26), "sacramento-hornets");
//		Assert.assertEquals(2, rosterPlayers.size());
//		Assert.assertEquals("Clayton", rosterPlayers.get(1).getPlayer().getLastName());
//		Assert.assertEquals("Mark", rosterPlayers.get(1).getPlayer().getFirstName());
//
//		//roster player not found
//		rosterPlayers = rosterPlayerService.getRosterPlayers(createMockRosterPlayerDTOs(), new LocalDate(2015, 8, 26), "sacramento-hornets");
//	}
//
//	@Test
//	public void findRosterPlayers() {
//		List<RosterPlayer> rosterPlayers;
//		//roster players found
//		rosterPlayers = rosterPlayerService.findRosterPlayers(new LocalDate(2015, 11, 26), "sacramento-hornets");
//		Assert.assertEquals(2, rosterPlayers.size());
//		Assert.assertEquals("Simpson", rosterPlayers.get(1).getPlayer().getLastName());
//		Assert.assertEquals("Lisa", rosterPlayers.get(1).getPlayer().getFirstName());
//
//		//roster player not found
//		rosterPlayers = rosterPlayerService.findRosterPlayers(new LocalDate(2015, 8, 26), "sacramento-hornets");
//		Assert.assertEquals(new ArrayList<RosterPlayer>(), rosterPlayers);
//	}
//
//	@Test
//	public void findByDatePlayerNameTeam() {
//		RosterPlayer rosterPlayer;
//		//roster players found
//		rosterPlayer = rosterPlayerService.findByDatePlayerNameTeam(new LocalDate(2015, 11, 26), "Moore", "Michael", "sacramento-hornets");
//		Assert.assertEquals("Adams", rosterPlayer.getPlayer().getLastName());
//		Assert.assertTrue(rosterPlayer.isFound());
//		rosterPlayer = rosterPlayerService.findByDatePlayerNameTeam(new LocalDate(2015, 11, 26), "Moore", "Nat", "sacramento-hornets");
//		Assert.assertEquals("Coors", rosterPlayer.getPlayer().getLastName());
//		Assert.assertTrue(rosterPlayer.isFound());
//
//		//no roster player found
//		rosterPlayer = rosterPlayerService.findByDatePlayerNameTeam(new LocalDate(2015, 11, 26), "Moore", "Even", "sacramento-hornets");
//		Assert.assertTrue(rosterPlayer.isNotFound());
//	}
//
//	@Test
//	public void findLatestByPlayerNameBirthdateSeason() {
//		RosterPlayer rosterPlayer;
//		//roster player found
//		rosterPlayer = rosterPlayerService.findLatestByPlayerNameBirthdateSeason(new LocalDate(2015, 11, 26), "Simmons", "Gene", new LocalDate(1995, 11, 26));
//		Assert.assertEquals("Gene", rosterPlayer.getPlayer().getFirstName());
//		Assert.assertTrue(rosterPlayer.isFound());
//
//		//no roster player found
//		rosterPlayer = rosterPlayerService.findLatestByPlayerNameBirthdateSeason(new LocalDate(2015, 11, 26), "Simmons", "Richard", new LocalDate(1995, 11, 26));
//		Assert.assertTrue(rosterPlayer.isNotFound());
//	}
//
//	@Test(expected=DuplicateEntityException.class)
//	public void createRosterPlayer() {
//		RosterPlayer rosterPlayer;
//		//roster player created
//		rosterPlayer = rosterPlayerService.createRosterPlayer(createMockRosterPlayer("Payton", "Walter", StatusCode.Created));
//		Assert.assertEquals("Walter", rosterPlayer.getPlayer().getFirstName());
//		Assert.assertTrue(rosterPlayer.isCreated());
//
//		//roster player already exists
//		rosterPlayer = rosterPlayerService.createRosterPlayer(createMockRosterPlayer("Smith", "Emmitt", StatusCode.Found));
//	}
//
//	@Test
//	public void updateRosterPlayer() {
//		RosterPlayer rosterPlayer;
//		//roster player updated
//		rosterPlayer = rosterPlayerService.updateRosterPlayer(createMockRosterPlayer("Schaub", "Buddy", StatusCode.Found));
//		Assert.assertEquals("Buddy", rosterPlayer.getPlayer().getFirstName());
//		Assert.assertTrue(rosterPlayer.isUpdated());
//
//		//no roster player found
//		rosterPlayer = rosterPlayerService.updateRosterPlayer(createMockRosterPlayer("Roger", "Lima", StatusCode.NotFound));
//		Assert.assertEquals("Roger", rosterPlayer.getPlayer().getFirstName());
//		Assert.assertTrue(rosterPlayer.isNotFound());
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

	private List<RosterPlayer> createMockRosterPlayers() {
		List<RosterPlayer> rosterPlayers = Arrays.asList(
			createMockRosterPlayer("Simpson", "Homer", StatusCode.Found),
			createMockRosterPlayer("Simpson", "Lisa", StatusCode.Found)
		);
		return rosterPlayers;
	}

	private RosterPlayer createMockRosterPlayer(String lastName, String firstName, StatusCode statusCode) {
		RosterPlayer rosterPlayer = new RosterPlayer();
		Player player = new Player();
		rosterPlayer.setPlayer(player);
		rosterPlayer.setStatusCode(statusCode);
		rosterPlayer.setFromDate(new LocalDate(2015, 11, 26));
		player.setLastName(lastName);
		player.setFirstName(firstName);
		player.setBirthdate(new LocalDate(1995, 11, 26));
		return rosterPlayer;
	}

	private Team createMockTeam(String teamKey, StatusCode statusCode) {
		Team team = new Team();
		team.setTeamKey(teamKey);
		team.setStatusCode(statusCode);
		return team;
	}
}