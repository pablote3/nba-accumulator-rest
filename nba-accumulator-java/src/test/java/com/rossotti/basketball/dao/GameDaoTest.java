package com.rossotti.basketball.dao;

import java.util.List;

import org.hibernate.PropertyValueException;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
	public void findByDateTeam_Found() {
		Game findGame = gameDAO.findByDateTeam(new LocalDate("2015-10-27"), "chicago-zephyrs");
		Assert.assertTrue(findGame.isFound());
		Assert.assertEquals(new LocalDateTime("2015-10-27T20:00"), findGame.getGameDate());
		Assert.assertEquals("harlem-globetrotters", findGame.getBoxScores().get(1).getTeam().getTeamKey());
		Assert.assertTrue(findGame.getBoxScores().get(1).getPoints().equals((short)98));
		Assert.assertEquals(2, findGame.getBoxScores().get(1).getBoxScorePlayers().size());
		Assert.assertTrue(findGame.getBoxScores().get(1).getBoxScorePlayers().get(1).getPoints().equals((short)5));
		Assert.assertEquals("21", findGame.getBoxScores().get(1).getBoxScorePlayers().get(0).getRosterPlayer().getNumber());
		Assert.assertEquals("Luke", findGame.getBoxScores().get(1).getBoxScorePlayers().get(0).getRosterPlayer().getPlayer().getFirstName());
	}

	@Test
	public void findByDateTeam_NotFound_GameDate() {
		Game findGame = gameDAO.findByDateTeam(new LocalDate("2015-10-26"), "chicago-zephyrs");
		Assert.assertTrue(findGame.isNotFound());
	}

	@Test
	public void findByDateTeam_NotFound_Team() {
		Game findGame = gameDAO.findByDateTeam(new LocalDate("2015-10-27"), "chicago-zephyres");
		Assert.assertTrue(findGame.isNotFound());
	}

	@Test
	public void findByDateRangeSize_Size0() {
		List<Game> findGames = gameDAO.findByDateRangeSize(new LocalDate("2015-10-28"), 0);
		Assert.assertEquals(3, findGames.size());
		Assert.assertEquals(new LocalDateTime("2015-10-28T20:00"), findGames.get(0).getGameDate());
		Assert.assertEquals(new LocalDateTime("2015-10-29T20:00"), findGames.get(1).getGameDate());
		Assert.assertEquals(new LocalDateTime("2015-10-30T10:00"), findGames.get(2).getGameDate());
	}

	@Test
	public void findByDateRangeSize_Size1() {
		List<Game> findGames = gameDAO.findByDateRangeSize(new LocalDate("2015-10-27"), 1);
		Assert.assertEquals(1, findGames.size());
		Assert.assertEquals(new LocalDateTime("2015-10-27T20:00"), findGames.get(0).getGameDate());
	}

	@Test
	public void findByDateRangeSize_Size2() {
		List<Game> findGames = gameDAO.findByDateRangeSize(new LocalDate("2015-10-27"), 2);
		Assert.assertEquals(2, findGames.size());
		Assert.assertEquals(new LocalDateTime("2015-10-27T20:00"), findGames.get(0).getGameDate());
		Assert.assertEquals(new LocalDateTime("2015-10-27T20:30"), findGames.get(1).getGameDate());
	}
	
	@Test
	public void findByDateRangeSize_NotFound() {
		List<Game> findGames = gameDAO.findByDateRangeSize(new LocalDate("2015-11-01"), 2);
		Assert.assertEquals(0, findGames.size());
	}

	@Test
	public void findByDateScheduled_Found() {
		List<Game> findGames = gameDAO.findByDateScheduled(new LocalDate("2015-10-28"));
		Assert.assertEquals(1, findGames.size());
		Assert.assertEquals(new LocalDateTime("2015-10-28T20:00"), findGames.get(0).getGameDate());
	}

	@Test
	public void findByDateScheduled_NotFound() {
		List<Game> findGames = gameDAO.findByDateScheduled(new LocalDate("2015-10-26"));
		Assert.assertEquals(0, findGames.size());
	}

	@Test
	public void findPreviousByDateTeam_Found() {
		LocalDateTime dateTime = gameDAO.findPreviousGameDateTimeByDateTeam(new LocalDate("2015-10-30"), "chicago-zephyrs");
		Assert.assertEquals(new LocalDateTime("2015-10-27T20:00:00.0"), dateTime);
	}

	@Test
	public void findPreviousByDateTeam_NotFound() {
		LocalDateTime dateTime = gameDAO.findPreviousGameDateTimeByDateTeam(new LocalDate("2015-10-27"), "st-louis-bombers");
		Assert.assertEquals(null, dateTime);
	}

	@Test
	public void findByDateTeamSeason_Found() {
		List<Game> findGames = gameDAO.findByDateTeamSeason(new LocalDate("2015-10-28"), "chicago-zephyrs");
		Assert.assertEquals(2, findGames.size());
		Assert.assertEquals(new LocalDateTime("2015-10-27T20:00"), findGames.get(0).getGameDate());
		Assert.assertEquals(new LocalDateTime("2015-10-28T20:00"), findGames.get(1).getGameDate());
	}

	@Test
	public void findByDateTeamSeason_NotFound() {
		List<Game> findGames = gameDAO.findByDateTeamSeason(new LocalDate("2015-09-29"), "st-louis-bombers");
		Assert.assertEquals(0, findGames.size());
	}

	@Test
	public void findCountGamesByDateScheduled() {
		int games = gameDAO.findCountGamesByDateScheduled(new LocalDate("2015-10-28"));
		Assert.assertEquals(1, games);
	}

	@Test
	public void createGame_Created() {
		Game game = createMockGame(new LocalDateTime("2015-10-10T21:00"), 1L, "chicago-zephyrs", 2L, "harlem-globetrotters");
		Game createGame = gameDAO.createGame(game);
		Game findGame = gameDAO.findByDateTeam(new LocalDate("2015-10-10"), "chicago-zephyrs");
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

	@Test
	public void updateGame_Updated() {
		Game findGame = gameDAO.findByDateTeam(new LocalDate("2015-10-27"), "salinas-cowboys");
		updateMockBoxScoreHome(findGame);
		updateMockBoxScoreAway(findGame);
		Game updateGame = gameDAO.updateGame(findGame);
		Assert.assertTrue(updateGame.isUpdated());
		Game game = gameDAO.findByDateTeam(new LocalDate("2015-10-27"), "salinas-cowboys");
		BoxScore homeBoxScore = game.getBoxScores().get(game.getBoxScores().get(0).getLocation().equals(Location.Home) ? 1 : 0);
		BoxScore awayBoxScore = game.getBoxScores().get(game.getBoxScores().get(0).getLocation().equals(Location.Away) ? 1 : 0);
		Assert.assertTrue(homeBoxScore.getFreeThrowMade().equals((short)10));
		Assert.assertTrue(awayBoxScore.getFreeThrowMade().equals((short)18));
	}

	@Test
	public void updateGame_NotFound() {
		Game updateGame = gameDAO.updateGame(createMockGame(new LocalDateTime("2015-10-10T21:00"), 1L, "chicago-zephyriers", 2L, "harlem-globetrottered"));
		Assert.assertTrue(updateGame.isNotFound());
	}

	@Test
	public void deleteGame_Deleted() {
		Game deleteGame = gameDAO.deleteGame(new LocalDate("2015-10-15"), "baltimore-bullets");
		Game findGame = gameDAO.findByDateTeam(new LocalDate("2015-10-15"), "baltimore-bullets");
		Assert.assertTrue(deleteGame.isDeleted());
		Assert.assertTrue(findGame.isNotFound());
	}

	@Test
	public void deleteGame_NotFound() {
		Game deleteGame = gameDAO.deleteGame(new LocalDate("2015-10-15"), "baltimore-bulletiers");
		Assert.assertTrue(deleteGame.isNotFound());
	}

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
	
	private void updateMockBoxScoreHome(Game game) {
		int homeBoxScoreId = game.getBoxScores().get(0).getLocation().equals(Location.Home) ? 1 : 0;
		BoxScore homeBoxScore = game.getBoxScores().get(homeBoxScoreId);
		homeBoxScore.setMinutes((short)240);
		homeBoxScore.setPoints((short)98);
		homeBoxScore.setAssists((short)14);
		homeBoxScore.setTurnovers((short)5);
		homeBoxScore.setSteals((short)7);
		homeBoxScore.setBlocks((short)5);
		homeBoxScore.setFieldGoalAttempts((short)44);
		homeBoxScore.setFieldGoalMade((short)22);
		homeBoxScore.setFieldGoalPercent((float).500);
		homeBoxScore.setThreePointAttempts((short)10);
		homeBoxScore.setThreePointMade((short)6);
		homeBoxScore.setThreePointPercent((float).6);
		homeBoxScore.setFreeThrowAttempts((short)20);
		homeBoxScore.setFreeThrowMade((short)10);
		homeBoxScore.setFreeThrowPercent((float).500);
		homeBoxScore.setReboundsOffense((short)25);
		homeBoxScore.setReboundsDefense((short)5);
		homeBoxScore.setPersonalFouls((short)18);
	}
	
	private void updateMockBoxScoreAway(Game game) {
		int awayBoxScoreId = game.getBoxScores().get(0).getLocation().equals(Location.Away) ? 1 : 0;
		BoxScore awayBoxScore = game.getBoxScores().get(awayBoxScoreId);
		awayBoxScore.setMinutes((short)240);
		awayBoxScore.setPoints((short)98);
		awayBoxScore.setAssists((short)14);
		awayBoxScore.setTurnovers((short)5);
		awayBoxScore.setSteals((short)7);
		awayBoxScore.setBlocks((short)5);
		awayBoxScore.setFieldGoalAttempts((short)44);
		awayBoxScore.setFieldGoalMade((short)22);
		awayBoxScore.setFieldGoalPercent((float).500);
		awayBoxScore.setThreePointAttempts((short)10);
		awayBoxScore.setThreePointMade((short)6);
		awayBoxScore.setThreePointPercent((float).6);
		awayBoxScore.setFreeThrowAttempts((short)20);
		awayBoxScore.setFreeThrowMade((short)18);
		awayBoxScore.setFreeThrowPercent((float).500);
		awayBoxScore.setReboundsOffense((short)25);
		awayBoxScore.setReboundsDefense((short)5);
		awayBoxScore.setPersonalFouls((short)18);
	}
	
	private Team createMockTeam(Long teamId, String teamKey) {
		Team team = new Team();
		team.setId(teamId);
		team.setTeamKey(teamKey);
		return team;
	}
	
	private Team createMockRosterPlayer(Long teamId, String teamKey) {
		Team team = new Team();
		team.setId(teamId);
		team.setTeamKey(teamKey);
		return team;
	}
}
