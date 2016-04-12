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

import com.rossotti.basketball.client.dto.BoxScorePlayerDTO;
import com.rossotti.basketball.client.dto.RosterPlayerDTO;
import com.rossotti.basketball.dao.exception.DuplicateEntityException;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.BoxScorePlayer;
import com.rossotti.basketball.dao.model.Player;
import com.rossotti.basketball.dao.model.RosterPlayer;
import com.rossotti.basketball.dao.model.StatusCode;
import com.rossotti.basketball.dao.model.Team;
import com.rossotti.basketball.dao.repository.RosterPlayerRepository;
import com.rossotti.basketball.dao.repository.TeamRepository;

@RunWith(MockitoJUnitRunner.class)
public class RosterPlayerServiceTest {
	@Mock
	private RosterPlayerRepository rosterPlayerRepo;

	@Mock
	private TeamRepository teamRepo;

	@InjectMocks
	private RosterPlayerService rosterPlayerService;

	@Before
	public void setUp() {
		when(rosterPlayerRepo.findRosterPlayer(anyString(), anyString(), anyString(), (LocalDate) anyObject()))
			.thenReturn(createMockRosterPlayer("Adams", "Samuel", StatusCode.Found))
			.thenReturn(createMockRosterPlayer("Coors", "Adolph", StatusCode.Found))
			.thenReturn(createMockRosterPlayer("", "", StatusCode.NotFound));
		when(rosterPlayerRepo.findRosterPlayer(anyString(), anyString(), (LocalDate) anyObject(), (LocalDate) anyObject()))
			.thenReturn(createMockRosterPlayer("Simmons", "Gene", StatusCode.Found))
			.thenReturn(createMockRosterPlayer("Simmons", "Richard", StatusCode.NotFound));
		when(rosterPlayerRepo.findRosterPlayers(anyString(), (LocalDate) anyObject()))
			.thenReturn(createMockRosterPlayers())
			.thenReturn(new ArrayList<RosterPlayer>());
		when(rosterPlayerRepo.createRosterPlayer((RosterPlayer) anyObject()))
			.thenReturn(createMockRosterPlayer("Payton", "Walter", StatusCode.Created))
			.thenThrow(new DuplicateEntityException());
		when(rosterPlayerRepo.updateRosterPlayer((RosterPlayer) anyObject()))
			.thenReturn(createMockRosterPlayer("Schaub", "Buddy", StatusCode.Updated))
			.thenReturn(createMockRosterPlayer("Lima", "Roger", StatusCode.NotFound));
		when(teamRepo.findTeam(anyString(), (LocalDate) anyObject()))
			.thenReturn(createMockTeam("denver-nuggets", StatusCode.Found))
			.thenReturn(createMockTeam("denver-mcnuggets", StatusCode.NotFound));
	}

	@Test(expected=NoSuchEntityException.class)
	public void getBoxScorePlayers() {
		List<BoxScorePlayer> boxScorePlayers;
		//box score players found
		boxScorePlayers = rosterPlayerService.getBoxScorePlayers(createMockBoxScorePlayerDTOs(), new LocalDate(2015, 11, 26), "sacramento-hornets");
		Assert.assertEquals(2, boxScorePlayers.size());
		Assert.assertEquals("Coors", boxScorePlayers.get(1).getRosterPlayer().getPlayer().getLastName());
		Assert.assertEquals("Adolph", boxScorePlayers.get(1).getRosterPlayer().getPlayer().getFirstName());

		//box score player not found
		boxScorePlayers = rosterPlayerService.getBoxScorePlayers(createMockBoxScorePlayerDTOs(), new LocalDate(2015, 8, 26), "sacramento-hornets");
	}

	@Test(expected=NoSuchEntityException.class)
	public void getRosterPlayers() {
		List<RosterPlayer> rosterPlayers;
		//roster players found
		rosterPlayers = rosterPlayerService.getRosterPlayers(createMockRosterPlayerDTOs(), new LocalDate(2015, 11, 26), "sacramento-hornets");
		Assert.assertEquals(2, rosterPlayers.size());
		Assert.assertEquals("Clayton", rosterPlayers.get(1).getPlayer().getLastName());
		Assert.assertEquals("Mark", rosterPlayers.get(1).getPlayer().getFirstName());

		//roster player team not found
		rosterPlayers = rosterPlayerService.getRosterPlayers(createMockRosterPlayerDTOs(), new LocalDate(2015, 8, 26), "sacramento-hornets");
	}

	@Test
	public void findRosterPlayers() {
		List<RosterPlayer> rosterPlayers;
		//roster players found
		rosterPlayers = rosterPlayerService.findRosterPlayers(new LocalDate(2015, 11, 26), "sacramento-hornets");
		Assert.assertEquals(2, rosterPlayers.size());
		Assert.assertEquals("Simpson", rosterPlayers.get(1).getPlayer().getLastName());
		Assert.assertEquals("Lisa", rosterPlayers.get(1).getPlayer().getFirstName());

		//roster player not found
		rosterPlayers = rosterPlayerService.findRosterPlayers(new LocalDate(2015, 8, 26), "sacramento-hornets");
		Assert.assertEquals(new ArrayList<RosterPlayer>(), rosterPlayers);
	}

	@Test
	public void findByDatePlayerNameTeam() {
		RosterPlayer rosterPlayer;
		//roster players found
		rosterPlayer = rosterPlayerService.findByDatePlayerNameTeam(new LocalDate(2015, 11, 26), "Moore", "Michael", "sacramento-hornets");
		Assert.assertEquals("Adams", rosterPlayer.getPlayer().getLastName());
		Assert.assertTrue(rosterPlayer.isFound());
		rosterPlayer = rosterPlayerService.findByDatePlayerNameTeam(new LocalDate(2015, 11, 26), "Moore", "Nat", "sacramento-hornets");
		Assert.assertEquals("Coors", rosterPlayer.getPlayer().getLastName());
		Assert.assertTrue(rosterPlayer.isFound());

		//no roster player found
		rosterPlayer = rosterPlayerService.findByDatePlayerNameTeam(new LocalDate(2015, 11, 26), "Moore", "Even", "sacramento-hornets");
		Assert.assertTrue(rosterPlayer.isNotFound());
	}

	@Test
	public void findLatestByPlayerNameBirthdateSeason() {
		RosterPlayer rosterPlayer;
		//roster player found
		rosterPlayer = rosterPlayerService.findLatestByPlayerNameBirthdateSeason(new LocalDate(2015, 11, 26), "Simmons", "Gene", new LocalDate(1995, 11, 26));
		Assert.assertEquals("Gene", rosterPlayer.getPlayer().getFirstName());
		Assert.assertTrue(rosterPlayer.isFound());

		//no roster player found
		rosterPlayer = rosterPlayerService.findLatestByPlayerNameBirthdateSeason(new LocalDate(2015, 11, 26), "Simmons", "Richard", new LocalDate(1995, 11, 26));
		Assert.assertTrue(rosterPlayer.isNotFound());
	}

	@Test(expected=DuplicateEntityException.class)
	public void createRosterPlayer() {
		RosterPlayer rosterPlayer;
		//roster player created
		rosterPlayer = rosterPlayerService.createRosterPlayer(createMockRosterPlayer("Payton", "Walter", StatusCode.Created));
		Assert.assertEquals("Walter", rosterPlayer.getPlayer().getFirstName());
		Assert.assertTrue(rosterPlayer.isCreated());

		//roster player already exists
		rosterPlayer = rosterPlayerService.createRosterPlayer(createMockRosterPlayer("Smith", "Emmitt", StatusCode.Found));
	}

	@Test
	public void updateRosterPlayer() {
		RosterPlayer rosterPlayer;
		//roster player updated
		rosterPlayer = rosterPlayerService.updateRosterPlayer(createMockRosterPlayer("Schaub", "Buddy", StatusCode.Found));
		Assert.assertEquals("Buddy", rosterPlayer.getPlayer().getFirstName());
		Assert.assertTrue(rosterPlayer.isUpdated());

		//no roster player found
		rosterPlayer = rosterPlayerService.updateRosterPlayer(createMockRosterPlayer("Roger", "Lima", StatusCode.NotFound));
		Assert.assertEquals("Roger", rosterPlayer.getPlayer().getFirstName());
		Assert.assertTrue(rosterPlayer.isNotFound());
	}

	private BoxScorePlayerDTO[] createMockBoxScorePlayerDTOs() {
		BoxScorePlayerDTO[] boxScorePlayers = new BoxScorePlayerDTO[2];
		boxScorePlayers[0] = createMockBoxScorePlayerDTO("Adams", "Samuel");
		boxScorePlayers[1] = createMockBoxScorePlayerDTO("Coors", "Adolph");
		return boxScorePlayers;
	}

	private BoxScorePlayerDTO createMockBoxScorePlayerDTO(String lastName, String firstName) {
		BoxScorePlayerDTO boxScorePlayer = new BoxScorePlayerDTO();
		boxScorePlayer.setLast_name(lastName);
		boxScorePlayer.setFirst_name(firstName);
		boxScorePlayer.setPosition("C");
		boxScorePlayer.setMinutes((short)25);
		boxScorePlayer.setIs_starter(true);
		boxScorePlayer.setPoints((short)12);
		boxScorePlayer.setAssists((short)3);
		boxScorePlayer.setTurnovers((short)0);
		boxScorePlayer.setSteals((short)2);
		boxScorePlayer.setBlocks((short)15);
		boxScorePlayer.setField_goals_attempted((short)8);
		boxScorePlayer.setField_goals_made((short)4);
		boxScorePlayer.setField_goal_percentage((float).5);
		boxScorePlayer.setThree_point_field_goals_attempted((short)3);
		boxScorePlayer.setThree_point_field_goals_made((short)1);
		boxScorePlayer.setThree_point_percentage((float).333);
		boxScorePlayer.setFree_throws_attempted((short)10);
		boxScorePlayer.setFree_throws_made((short)1);
		boxScorePlayer.setFree_throw_percentage((float).1);
		boxScorePlayer.setOffensive_rebounds((short)0);
		boxScorePlayer.setDefensive_rebounds((short)10);
		boxScorePlayer.setPersonal_fouls((short)4);
		return boxScorePlayer;
	}

	private RosterPlayerDTO[] createMockRosterPlayerDTOs() {
		RosterPlayerDTO[] rosterPlayers = new RosterPlayerDTO[2];
		rosterPlayers[0] = createMockRosterPlayerDTO("Marino", "Dan");
		rosterPlayers[1] = createMockRosterPlayerDTO("Clayton", "Mark");
		return rosterPlayers;
	}

	private RosterPlayerDTO createMockRosterPlayerDTO(String lastName, String firstName) {
		RosterPlayerDTO rosterPlayer = new RosterPlayerDTO();
		rosterPlayer.setLast_name(lastName);
		rosterPlayer.setFirst_name(firstName);
		rosterPlayer.setDisplay_name(firstName + " " + lastName);
		rosterPlayer.setHeight_in((short)82);
		rosterPlayer.setWeight_lb((short)200);
		rosterPlayer.setBirthdate(new DateTime(2005, 3, 26, 12, 0, 0, 0));
		rosterPlayer.setBirthplace("Kalamazoo, KS");
		rosterPlayer.setUniform_number("25");
		rosterPlayer.setPosition("G");
		return rosterPlayer;
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