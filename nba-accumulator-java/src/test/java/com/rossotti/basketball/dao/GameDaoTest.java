package com.rossotti.basketball.dao;

import java.util.Arrays;
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
import com.rossotti.basketball.model.BoxScore;
import com.rossotti.basketball.model.BoxScore.Location;
import com.rossotti.basketball.model.Game;
import com.rossotti.basketball.model.Game.SeasonType;
import com.rossotti.basketball.model.Game.Status;
import com.rossotti.basketball.model.Team;

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
		Assert.assertTrue(findGame.isFound());
		Assert.assertEquals(new LocalDateTime("2015-10-27T20:00"), findGame.getGameDate());
		Assert.assertEquals("chicago-zephyrs", findGame.getBoxScores().get(0).getTeam().getTeamKey());
	}

	@Test
	public void findGameById_NotFound() {
		Game findGame = gameDAO.findById(0L);
		Assert.assertTrue(findGame.isNotFound());
	}

	@Test
	public void findGamesById_Found() {
		List<Long> ids = Arrays.asList(2L, 4L);
		List<Game> games = gameDAO.findById(ids);
		Assert.assertEquals(2, games.size());
		Assert.assertEquals(new LocalDateTime("2015-10-27T21:00"), games.get(0).getGameDate());
		Assert.assertEquals("st-louis-bombers", games.get(0).getBoxScores().get(0).getTeam().getTeamKey());
	}

	@Test
	public void findGamesById_NotFound() {
		List<Long> ids = Arrays.asList(10L);
		List<Game> games = gameDAO.findById(ids);
		Assert.assertEquals(0, games.size());
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

	@Test
	public void findCountGamesByDateScheduled() {
		int games = gameDAO.findCountGamesByDateScheduled(new LocalDate("2015-10-27"));
		Assert.assertEquals(1, games);
	}

	//'2015-10-10 21:00', 'chicago-zephyrs', 'harlem-globetrotters'

	@Test
	public void createGame_Created() {
		Game game = createMockGame(new LocalDateTime("2015-10-10T21:00"), 1L, "chicago-zephyrs", 2L, "harlem-globetrotters");
		Game createGame = gameDAO.createGame(game);
		Long gameId = gameDAO.findIdByDateTeam(new LocalDate("2015-10-10"), "chicago-zephyrs");
		Game findGame = gameDAO.findById(gameId);
		Assert.assertTrue(createGame.isCreated());
		Assert.assertEquals(2, findGame.getBoxScores().size());
		Assert.assertEquals(Location.Home, findGame.getBoxScores().get(0).getLocation());
		Assert.assertEquals("Harlem Globetrotters", findGame.getBoxScores().get(1).getTeam().getFullName());
	}

	@Test(expected=DuplicateEntityException.class)
	public void createGame_Duplicate() {
		Game game = createMockGame(new LocalDateTime("2015-10-27T20:00"), 1L, "chicago-zephyrs", 2L, "harlem-globetrotters");
		gameDAO.createGame(game);
	}

	@Test(expected=PropertyValueException.class)
	public void createGame_Exception_MissingRequiredData() {
		Game game = createMockGame(new LocalDateTime("2015-10-11T21:00"), 1L, "chicago-zephyrs", 2L, "harlem-globetrotters");
		game.getBoxScores().get(0).setLocation(null);
		gameDAO.createGame(game);
	}

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

	private Game createMockGame(LocalDateTime gameDate, Long teamIdHome, String teamKeyHome, Long teamIdAway, String teamKeyAway) {
		Game game = new Game();
		game.setGameDate(gameDate);
		game.setSeasonType(SeasonType.Regular);
		game.setStatus(Status.Scheduled);
		game.addBoxScore(createMockBoxScore(game, teamIdHome, teamKeyHome, Location.Home));
		game.addBoxScore(createMockBoxScore(game, teamIdAway, teamKeyAway, Location.Away));
		return game;
	}

	private BoxScore createMockBoxScore(Game game, Long teamId, String teamKey, Location location) {
		BoxScore boxScore = new BoxScore();
		boxScore.setGame(game);
		boxScore.setTeam(createMockTeam(teamId, teamKey));
		boxScore.setLocation(location);
		return boxScore;
	}
	
	private Team createMockTeam(Long teamId, String teamKey) {
		Team team = new Team();
		team.setId(teamId);
		team.setTeamKey(teamKey);
		return team;
	}

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
