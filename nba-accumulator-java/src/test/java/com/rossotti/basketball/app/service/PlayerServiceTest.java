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

import com.rossotti.basketball.dao.exception.DuplicateEntityException;
import com.rossotti.basketball.dao.model.Player;
import com.rossotti.basketball.dao.model.StatusCodeDAO;
import com.rossotti.basketball.dao.repository.PlayerRepository;

@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceTest {
	@Mock
	private PlayerRepository playerRepo;

	@InjectMocks
	private PlayerService playerService;

	@Before
	public void setUp() {
		when(playerRepo.findPlayer(anyString(), anyString(), (LocalDate) anyObject()))
			.thenReturn(createMockPlayer("Adams", "Samuel", StatusCodeDAO.Found))
			.thenReturn(createMockPlayer("Simmons", "Richard", StatusCodeDAO.NotFound));
		when(playerRepo.createPlayer((Player) anyObject()))
			.thenReturn(createMockPlayer("Payton", "Walter", StatusCodeDAO.Created))
			.thenThrow(new DuplicateEntityException(Player.class));
	}

	@Test
	public void findLatestByPlayerNameBirthdateSeason() {
		Player player;
		//player found
		player = playerService.findByPlayerNameBirthdate("Adams", "Samuel", new LocalDate(1995, 11, 26));
		Assert.assertEquals("Samuel", player.getFirstName());
		Assert.assertTrue(player.isFound());

		//no roster player found
		player = playerService.findByPlayerNameBirthdate("Simmons", "Richard", new LocalDate(1995, 11, 26));
		Assert.assertTrue(player.isNotFound());
	}

	@Test(expected=DuplicateEntityException.class)
	public void createPlayer() {
		Player player;
		//player created
		player = playerService.createPlayer(createMockPlayer("Payton", "Walter", StatusCodeDAO.Created));
		Assert.assertEquals("Walter", player.getFirstName());
		Assert.assertTrue(player.isCreated());

		//player already exists
		player = playerService.createPlayer(createMockPlayer("Smith", "Emmitt", StatusCodeDAO.Found));
	}

	private Player createMockPlayer(String lastName, String firstName, StatusCodeDAO statusCode) {
		Player player = new Player();
		player.setLastName(lastName);
		player.setFirstName(firstName);
		player.setBirthdate(new LocalDate(1995, 11, 26));
		player.setStatusCode(statusCode);
		return player;
	}
}