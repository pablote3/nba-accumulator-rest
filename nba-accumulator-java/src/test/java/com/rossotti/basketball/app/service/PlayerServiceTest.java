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

	@Test
	public void findLatestByPlayerNameBirthdateSeason_notFound() {
		when(playerRepo.findPlayer(anyString(), anyString(), (LocalDate) anyObject()))
			.thenReturn(createMockPlayer("Simmons", "Richard", StatusCodeDAO.NotFound));
		Player player = playerService.findByPlayerNameBirthdate("Simmons", "Richard", new LocalDate(1995, 11, 26));
		Assert.assertTrue(player.isNotFound());
	}

	@Test
	public void findLatestByPlayerNameBirthdateSeason_found() {
		when(playerRepo.findPlayer(anyString(), anyString(), (LocalDate) anyObject()))
			.thenReturn(createMockPlayer("Adams", "Samuel", StatusCodeDAO.Found));
		Player player = playerService.findByPlayerNameBirthdate("Adams", "Samuel", new LocalDate(1995, 11, 26));
		Assert.assertEquals("Samuel", player.getFirstName());
		Assert.assertTrue(player.isFound());
	}

	@Test(expected=DuplicateEntityException.class)
	public void createPlayer_alreadyExists() {
		when(playerRepo.createPlayer((Player) anyObject()))
			.thenThrow(new DuplicateEntityException(Player.class));
		Player player = playerService.createPlayer(createMockPlayer("Smith", "Emmitt", StatusCodeDAO.Found));
		Assert.assertTrue(player.isNotFound());
	}

	@Test
	public void createPlayer_created() {
		when(playerRepo.createPlayer((Player) anyObject()))
			.thenReturn(createMockPlayer("Payton", "Walter", StatusCodeDAO.Created));
		Player player = playerService.createPlayer(createMockPlayer("Payton", "Walter", StatusCodeDAO.Created));
		Assert.assertEquals("Walter", player.getFirstName());
		Assert.assertTrue(player.isCreated());
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