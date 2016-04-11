package com.rossotti.basketball.app.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.rossotti.basketball.client.dto.BoxScorePlayerDTO;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.BoxScorePlayer;
import com.rossotti.basketball.dao.model.Player;
import com.rossotti.basketball.dao.model.RosterPlayer;
import com.rossotti.basketball.dao.model.StatusCode;
import com.rossotti.basketball.dao.repository.RosterPlayerRepository;

@RunWith(MockitoJUnitRunner.class)
public class RosterPlayerServiceTest {
	@Mock
	private RosterPlayerRepository rosterPlayerRepo;

	@InjectMocks
	private RosterPlayerService rosterPlayerService;

	@Before
	public void setUp() {
		when(rosterPlayerRepo.findRosterPlayer(anyString(), anyString(), anyString(), (LocalDate) anyObject()))
			.thenReturn(createMockRosterPlayer("Adams", "Samuel", StatusCode.Found))
			.thenReturn(createMockRosterPlayer("Coors", "Adolph", StatusCode.Found))
			.thenReturn(createMockRosterPlayer("", "", StatusCode.NotFound));
	}

	@Test(expected=NoSuchEntityException.class)
	public void getBoxScorePlayers() {
		List<BoxScorePlayer> boxScorePlayers;
		//roster player found
		boxScorePlayers = rosterPlayerService.getBoxScorePlayers(createMockBoxScorePlayerDTOs(), new LocalDate(2015, 11, 26), "sacramento-hornets");
		Assert.assertEquals(2, boxScorePlayers.size());
		Assert.assertEquals("Coors", boxScorePlayers.get(1).getRosterPlayer().getPlayer().getLastName());
		Assert.assertEquals("Adolph", boxScorePlayers.get(1).getRosterPlayer().getPlayer().getFirstName());

		//roster player not found
		boxScorePlayers = rosterPlayerService.getBoxScorePlayers(createMockBoxScorePlayerDTOs(), new LocalDate(2015, 8, 26), "sacramento-hornets");
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

	private RosterPlayer createMockRosterPlayer(String lastName, String firstName, StatusCode statusCode) {
		RosterPlayer rosterPlayer = new RosterPlayer();
		Player player = new Player();
		rosterPlayer.setPlayer(player);
		rosterPlayer.setStatusCode(statusCode);
		player.setLastName(lastName);
		player.setFirstName(firstName);
		return rosterPlayer;
	}
}