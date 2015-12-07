package com.rossotti.basketball.dao;

import java.util.List;

import org.hibernate.PropertyValueException;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rossotti.basketball.dao.exceptions.DuplicateEntityException;
import com.rossotti.basketball.dao.exceptions.NoSuchEntityException;
import com.rossotti.basketball.models.Player;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"config/applicationContextTest.xml"})
public class PlayerDaoTest {

	@Autowired
	private PlayerDAO playerDAO;

	//'Luke', 'Puzdrakiewicz', '2002-02-20', 'Luke Puzdrakiewicz'

	@Test
	public void findPlayerByName_MatchBirthdate() {
		Player player = playerDAO.findPlayer("Puzdrakiewicz", "Luke", new LocalDate("2002-02-20"));
		Assert.assertEquals("Luke Puzdrakiewicz", player.getDisplayName());
	}

	@Test(expected=NoSuchEntityException.class)
	public void findPlayerByName_NoSuchEntityException_LastName() {
		playerDAO.findPlayer("Puzdrakiewic", "Luke", new LocalDate("2002-02-20"));
	}

	@Test(expected=NoSuchEntityException.class)
	public void findPlayerByName_NoSuchEntityException_FirstName() {
		playerDAO.findPlayer("Puzdrakiewicz", "Luk", new LocalDate("2002-02-20"));
	}

	@Test(expected=NoSuchEntityException.class)
	public void findPlayerByName_NoSuchEntityException_Birthdate() {
		playerDAO.findPlayer("Puzdrakiewicz", "Luke", new LocalDate("2002-02-21"));
	}

	//'Thad', 'Puzdrakiewicz', '1966-06-02', 'Thad Puzdrakiewicz'
	//'Thad', 'Puzdrakiewicz', '2000-03-13', 'Thad Puzdrakiewicz'

	@Test
	public void findPlayersByName() {
		List<Player> players = playerDAO.findPlayers("Puzdrakiewicz","Thad");
		Assert.assertEquals(2, players.size());
	}

	@Test(expected=NoSuchEntityException.class)
	public void findPlayersByName_NoSuchEntityException() {
		playerDAO.findPlayers("Puzdrakiewicz", "Thady");
	}

	//'Michelle', 'Puzdrakiewicz', '1969-09-08', 'Michelle Puzdrakiewicz'

	@Test
	public void createPlayer() {
		playerDAO.createPlayer(createMockPlayer("Puzdrakiewicz", "Fred", new LocalDate("1968-11-08"), "Fred Puzdrakiewicz"));
		Player player = playerDAO.findPlayer("Puzdrakiewicz", "Fred", new LocalDate("1968-11-08"));
		Assert.assertEquals("Fred Puzdrakiewicz", player.getDisplayName());
	}

	@Test
	public void createPlayer_UniqueBirthdate() {
		playerDAO.createPlayer(createMockPlayer("Puzdrakiewicz", "Michelle", new LocalDate("1969-09-09"), "Michelle Puzdrakiewicz2"));
		Player player = playerDAO.findPlayer("Puzdrakiewicz", "Michelle", new LocalDate("1969-09-09"));
		Assert.assertEquals("Michelle Puzdrakiewicz2", player.getDisplayName());
	}

	@Test(expected=DuplicateEntityException.class)
	public void createPlayer_IdenticalBirthdate() {
		playerDAO.createPlayer(createMockPlayer("Puzdrakiewicz", "Michelle", new LocalDate("1969-09-08"), "Michelle Puzdrakiewicz"));
	}

	@Test(expected=PropertyValueException.class)
	public void createPlayer_MissingRequiredData() {
		Player player = new Player();
		player.setLastName("missing-required-data");
		player.setFirstName("missing-required-data");
		playerDAO.createPlayer(player);
	}

	//'Thad', 'Puzdrakiewicz', '1966-06-10', 'Thad Puzdrakiewicz'

	@Test
	public void updatePlayer() {
		playerDAO.updatePlayer(updateMockPlayer("Puzdrakiewicz", "Thad", new LocalDate("1966-06-02"), "Thad Puzdrakiewicz"));
		Player player = playerDAO.findPlayer("Puzdrakiewicz", "Thad", new LocalDate("1966-06-02"));
		Assert.assertEquals((short)215, player.getWeight().shortValue());
	}

	@Test(expected=NoSuchEntityException.class)
	public void updatePlayer_NoSuchEntityException_Key() {
		playerDAO.updatePlayer(updateMockPlayer("Puzdrakiewicz", "Thad", new LocalDate("2009-06-21"), "Thad Puzdrakiewicz"));
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void updatePlayer_MissingRequiredData() {
		Player player = updateMockPlayer("Puzdrakiewicz", "Thad", new LocalDate("1966-06-02"), null);
		playerDAO.updatePlayer(player);
	}

	//'Junior', 'Puzdrakiewicz', '1966-06-10', 'Junior Puzdrakiewicz'

	@Test(expected=NoSuchEntityException.class)
	public void deletePlayer() {
		playerDAO.deletePlayer("Junior", "Puzdrakiewicz", new LocalDate("1966-06-10"));
		playerDAO.findPlayer("Junior", "Puzdrakiewicz", new LocalDate("1966-06-10"));
	}

	@Test(expected=NoSuchEntityException.class)
	public void deletePlayer_NoSuchEntityException_Key() {
		playerDAO.deletePlayer("Juni", "Puzdrakiewicz", new LocalDate("1966-06-10"));
	}

	private Player createMockPlayer(String lastName, String firstName, LocalDate birthdate, String displayName) {
		Player player = new Player();
		player.setLastName(lastName);
		player.setFirstName(firstName);
		player.setBirthdate(birthdate);
		player.setDisplayName(displayName);
		player.setHeight((short)79);
		player.setWeight((short)195);
		player.setBirthplace("Monroe, Louisiana, USA");
		return player;
	}
	
	private Player updateMockPlayer(String lastName, String firstName, LocalDate birthdate, String displayName) {
		Player player = new Player();
		player.setLastName(lastName);
		player.setFirstName(firstName);
		player.setBirthdate(birthdate);
		player.setDisplayName(displayName);
		player.setHeight((short)79);
		player.setWeight((short)215);
		player.setBirthplace("Monroe, Louisiana, USA");
		return player;
	}
}
