package com.rossotti.basketball.dao;

import java.util.List;

import org.hibernate.PropertyValueException;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rossotti.basketball.dao.exception.DuplicateEntityException;
import com.rossotti.basketball.model.Game;
import com.rossotti.basketball.model.Game.Status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"config/applicationContextTest.xml"})
public class GameDaoTest {

	@Autowired
	private GameDAO gameDAO;

	//1, '2015-10-27 20:00:00.0', 'Completed'
	//1, 1, 1, 'Home', 'Win' : 'chicago-zephyrs'
	//1, 2, 2, 'Away', 'Loss' : 'harlem-globetrotters'
	
	//2, '2015-10-27 21:00:00.0', 'Scheduled'
	//2, 3, 3, 'Home', 'Win' : 'st-louis-bombers'
	//2, 5, 4, 'Away', 'Loss' : 'salinas-cowboys'
	
	//3, '2015-10-27 20:30:00.0', 'Scheduled'
	//3, 6, 5, 'Home', 'Win' : 'baltimore-bullets'
	//3, 7, 6, 'Away', 'Loss' : 'cleveland-rebels'
	
	//4, '2015-10-28 20:00:00.0', 'Scheduled'
	//4, 7, 1, 'Home', 'Win' : 'chicago-zephyrs'
	//4, 8, 3, 'Away', 'Loss' : 'st-louis-bombers'

	@Test
	public void findGameById_Found() {
		Game findGame = gameDAO.findById(1L);
		Assert.assertEquals(Status.Completed, findGame.getStatus());
		Assert.assertEquals(2, findGame.getBoxScores().size());
		Assert.assertTrue(findGame.isFound());
	}

	@Test
	public void findGameById_NotFound() {
		Game findGame = gameDAO.findById(0L);
		Assert.assertTrue(findGame.isNotFound());
	}

	@Test
	public void findIdByDateTeam_Found() {
		Long findId = gameDAO.findIdByDateTeam(new LocalDate("2015-10-27"), "chicago-zephyrs");
		Assert.assertEquals(1L, findId.longValue());
	}
	
	@Test
	public void findIdByDateTeam_NotFound_GameDate() {
		Long findId = gameDAO.findIdByDateTeam(new LocalDate("2015-10-26"), "chicago-zephyrs");
		Assert.assertEquals(0L, findId.longValue());
	}

	@Test
	public void findIdByDateTeam_NotFound_Team() {
		Long findId = gameDAO.findIdByDateTeam(new LocalDate("2015-10-27"), "chicago-zephyres");
		Assert.assertEquals(0L, findId.longValue());
	}

	@Test
	public void findIdsByDateRangeSize_Size0() {
		List<Long> findIds = gameDAO.findIdsByDateRangeSize(new LocalDate("2015-10-28"), 0);
		Assert.assertEquals(3, findIds.size());
		Assert.assertTrue(findIds.contains(4L));
		Assert.assertTrue(findIds.contains(5L));
		Assert.assertTrue(findIds.contains(6L));
	}

	@Test
	public void findIdsByDateRangeSize_Size1() {
		List<Long> findIds = gameDAO.findIdsByDateRangeSize(new LocalDate("2015-10-27"), 1);
		Assert.assertEquals(1, findIds.size());
		Assert.assertTrue(findIds.contains(1L));
	}

	@Test
	public void findIdsByDateRangeSize_Size2() {
		List<Long> findIds = gameDAO.findIdsByDateRangeSize(new LocalDate("2015-10-27"), 2);
		Assert.assertEquals(2, findIds.size());
		Assert.assertTrue(findIds.contains(1L));
		Assert.assertTrue(findIds.contains(3L));
	}
	
	@Test
	public void findIdsByDateRangeSize_NotFound() {
		List<Long> findIds = gameDAO.findIdsByDateRangeSize(new LocalDate("2015-11-01"), 2);
		Assert.assertEquals(0, findIds.size());
	}

	@Test
	public void findIdsByDateScheduled_Found() {
		List<Long> findIds = gameDAO.findIdsByDateScheduled(new LocalDate("2015-10-27"));
		Assert.assertEquals(1, findIds.size());
		Assert.assertTrue(findIds.contains(3L));
	}

	@Test
	public void findIdsByDateScheduled_NotFound() {
		List<Long> findIds = gameDAO.findIdsByDateScheduled(new LocalDate("2015-10-26"));
		Assert.assertEquals(0, findIds.size());
	}

	@Test
	public void findPreviousGameDateTimeByDateTeam_Found() {
		LocalDateTime dateTime = gameDAO.findPreviousGameDateTimeByDateTeam(new LocalDate("2015-10-30"), "st-louis-bombers");
		Assert.assertEquals(new LocalDateTime("2015-10-27T21:00:00.0"), dateTime);
	}

	@Test
	public void findPreviousGameDateTimeByDateTeam_NotFound() {
		LocalDateTime dateTime = gameDAO.findPreviousGameDateTimeByDateTeam(new LocalDate("2015-10-27"), "st-louis-bombers");
		Assert.assertEquals(null, dateTime);
	}

	@Test
	public void findByDateTeamSeason_Found() {
		List<Long> findIds = gameDAO.findByDateTeamSeason(new LocalDate("2015-10-29"), "st-louis-bombers");
		Assert.assertEquals(2, findIds.size());
		Assert.assertTrue(findIds.contains(2L));
		Assert.assertTrue(findIds.contains(4L));
	}

	@Test
	public void findByDateTeamSeason_NotFound() {
		List<Long> findIds = gameDAO.findByDateTeamSeason(new LocalDate("2015-09-29"), "st-louis-bombers");
		Assert.assertEquals(0, findIds.size());
	}

//	@Test
//	public void findGame_NotFound_GameDate() {
//		Game findGame = gameDAO.findGame(new LocalDate("2015-10-26"), "atlanta-hawks");
//		Assert.assertTrue(findGame.isNotFound());
//	}
//
//	@Test
//	public void findGameByName_NotFound_TeamKey() {
//		Game findGame = gameDAO.findGame(new LocalDate("2015-10-27"), "atlanta-hawkers");
//		Assert.assertTrue(findGame.isNotFound());
//	}

	//'Thad', 'Puzdrakiewicz', '1966-06-02', 'Thad Puzdrakiewicz'
	//'Thad', 'Puzdrakiewicz', '2000-03-13', 'Thad Puzdrakiewicz'

//	@Test
//	public void findGamesByName_Found() {
//		List<Game> findGames = gameDAO.findGames("Puzdrakiewicz","Luke");
//		Assert.assertEquals(1, findGames.size());
//	}
//
//	@Test
//	public void findGamesByName_NotFound() {
//		List<Game> findGames = gameDAO.findGames("Puzdrakiewicz", "Thady");
//		Assert.assertEquals(0, findGames.size());
//	}

	//'Michelle', 'Puzdrakiewicz', '1969-09-08', 'Michelle Puzdrakiewicz'

//	@Test
//	public void createGame_Created_UniqueName() {
//		Game createGame = gameDAO.createGame(createMockGame("Puzdrakiewicz", "Fred", new LocalDate("1968-11-08"), "Fred Puzdrakiewicz"));
//		Game findGame = gameDAO.findGame("Puzdrakiewicz", "Fred", new LocalDate("1968-11-08"));
//		Assert.assertTrue(createGame.isCreated());
//		Assert.assertEquals("Fred Puzdrakiewicz", findGame.getDisplayName());
//	}
//
//	@Test
//	public void createGame_Created_UniqueBirthdate() {
//		Game createGame = gameDAO.createGame(createMockGame("Puzdrakiewicz", "Michelle", new LocalDate("1969-09-09"), "Michelle Puzdrakiewicz2"));
//		Game findGame = gameDAO.findGame("Puzdrakiewicz", "Michelle", new LocalDate("1969-09-09"));
//		Assert.assertTrue(createGame.isCreated());
//		Assert.assertEquals("Michelle Puzdrakiewicz2", findGame.getDisplayName());
//	}
//
//	@Test(expected=DuplicateEntityException.class)
//	public void createGame_Duplicate_IdenticalBirthdate() {
//		gameDAO.createGame(createMockGame("Puzdrakiewicz", "Michelle", new LocalDate("1969-09-08"), "Michelle Puzdrakiewicz"));
//	}
//
//	@Test(expected=PropertyValueException.class)
//	public void createGame_Exception_MissingRequiredData() {
//		Game createGame = new Game();
//		createGame.setLastName("missing-required-data");
//		createGame.setFirstName("missing-required-data");
//		gameDAO.createGame(createGame);
//	}

	//'Thad', 'Puzdrakiewicz', '1966-06-10', 'Thad Puzdrakiewicz'

//	@Test
//	public void updateGame_Updated() {
//		Game updateGame = gameDAO.updateGame(updateMockGame("Puzdrakiewicz", "Thad", new LocalDate("1966-06-02"), "Thad Puzdrakiewicz"));
//		Game findGame = gameDAO.findGame("Puzdrakiewicz", "Thad", new LocalDate("1966-06-02"));
//		Assert.assertTrue(updateGame.isUpdated());
//		Assert.assertEquals((short)215, findGame.getWeight().shortValue());
//	}
//
//	@Test
//	public void updateGame_NotFound() {
//		Game updateGame = gameDAO.updateGame(updateMockGame("Puzdrakiewicz", "Thad", new LocalDate("2009-06-21"), "Thad Puzdrakiewicz"));
//		Assert.assertTrue(updateGame.isNotFound());
//	}
//
//	@Test(expected=DataIntegrityViolationException.class)
//	public void updateGame_Exception_MissingRequiredData() {
//		Game updateGame = updateMockGame("Puzdrakiewicz", "Thad", new LocalDate("1966-06-02"), null);
//		gameDAO.updateGame(updateGame);
//	}

	//'Junior', 'Puzdrakiewicz', '1966-06-10', 'Junior Puzdrakiewicz'

//	@Test
//	public void deleteGame_Deleted() {
//		Game deleteGame = gameDAO.deleteGame("Puzdrakiewicz", "Junior", new LocalDate("1966-06-10"));
//		Game findGame = gameDAO.findGame("Puzdrakiewicz", "Junior", new LocalDate("1966-06-10"));
//		Assert.assertTrue(deleteGame.isDeleted());
//		Assert.assertTrue(findGame.isNotFound());
//	}
//
//	@Test
//	public void deleteGame_NotFound() {
//		Game deleteGame = gameDAO.deleteGame("Puzdrakiewicz", "Juni", new LocalDate("1966-06-10"));
//		Assert.assertTrue(deleteGame.isNotFound());
//	}

//	private Game createMockGame(String lastName, String firstName, LocalDate birthdate, String displayName) {
//		Game game = new Game();
//		game.setLastName(lastName);
//		game.setFirstName(firstName);
//		game.setBirthdate(birthdate);
//		game.setDisplayName(displayName);
//		game.setHeight((short)79);
//		game.setWeight((short)195);
//		game.setBirthplace("Monroe, Louisiana, USA");
//		return game;
//	}
//	
//	private Game updateMockGame(String lastName, String firstName, LocalDate birthdate, String displayName) {
//		Game game = new Game();
//		game.setLastName(lastName);
//		game.setFirstName(firstName);
//		game.setBirthdate(birthdate);
//		game.setDisplayName(displayName);
//		game.setHeight((short)79);
//		game.setWeight((short)215);
//		game.setBirthplace("Monroe, Louisiana, USA");
//		return game;
//	}
}
