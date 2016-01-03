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
import com.rossotti.basketball.model.BoxScorePlayer;
import com.rossotti.basketball.model.BoxScorePlayer.Position;
import com.rossotti.basketball.model.Game;
import com.rossotti.basketball.model.Game.SeasonType;
import com.rossotti.basketball.model.Game.Status;
import com.rossotti.basketball.model.GameOfficial;
import com.rossotti.basketball.model.Official;
import com.rossotti.basketball.model.Player;
import com.rossotti.basketball.model.RosterPlayer;
import com.rossotti.basketball.model.Team;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"config/applicationContextTest.xml"})
public class GameDaoTest {

	@Autowired
	private GameDAO gameDAO;

	@Test
	public void findByDateTeam_Found() {
		Game findGame = gameDAO.findByDateTeam(new LocalDate("2015-10-27"), "chicago-zephyrs");
		Assert.assertTrue(findGame.isFound());
		Assert.assertEquals("QuestionableCall", findGame.getGameOfficials().get(2).getOfficial().getLastName());
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
		findGame.addGameOfficial(getMockGameOfficial(4L, 1L, "LateCall", "Joe"));
//		findGame.addGameOfficial(getMockGameOfficial(5L, 3L, "MissedCall", "Mike"));
//		findGame.addGameOfficial(getMockGameOfficial(6L, 5L, "TerribleCall", "Limo"));
		updateMockBoxScoreHome(findGame);
		updateMockBoxScoreAway(findGame);
		Game updateGame = gameDAO.updateGame(findGame);
		Assert.assertTrue(updateGame.isUpdated());
		Game game = gameDAO.findByDateTeam(new LocalDate("2015-10-27"), "salinas-cowboys");
		BoxScore homeBoxScore = game.getBoxScores().get(game.getBoxScores().get(0).getLocation().equals(Location.Home) ? 1 : 0);
		BoxScorePlayer homeBoxScorePlayer0 = homeBoxScore.getBoxScorePlayers().get(0);
		Assert.assertTrue(homeBoxScorePlayer0.getMinutes().equals((short)20));
		BoxScorePlayer homeBoxScorePlayer1 = homeBoxScore.getBoxScorePlayers().get(1);
		Assert.assertTrue(homeBoxScorePlayer1.getThreePointPercent().equals((float)0.0));
		BoxScore awayBoxScore = game.getBoxScores().get(game.getBoxScores().get(0).getLocation().equals(Location.Away) ? 1 : 0);
		BoxScorePlayer awayBoxScorePlayer = awayBoxScore.getBoxScorePlayers().get(0);
		Assert.assertTrue(awayBoxScorePlayer.getBlocks().equals((short)2));
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
		boxScore.setTeam(getMockTeam(teamId, teamKey));
		boxScore.setLocation(location);
		return boxScore;
	}

	private void updateMockBoxScoreHome(Game game) {
		int homeBoxScoreId = game.getBoxScores().get(0).getLocation().equals(Location.Home) ? 1 : 0;
		BoxScore homeBoxScore = game.getBoxScores().get(homeBoxScoreId);
		homeBoxScore.addBoxScorePlayer(createMockBoxScorePlayerHome_0());
		homeBoxScore.addBoxScorePlayer(createMockBoxScorePlayerHome_1());
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
		awayBoxScore.addBoxScorePlayer(createMockBoxScorePlayerAway());
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

	private GameOfficial getMockGameOfficial(Long gameOfficialId, Long officialId, String lastName, String firstName) {
		GameOfficial gameOfficial = new GameOfficial();
		gameOfficial.setId(gameOfficialId);
		gameOfficial.setOfficial(getMockOfficial(officialId, lastName, firstName));
		return gameOfficial;
	}

	private Official getMockOfficial(Long officialId, String lastName, String firstName) {
		Official official = new Official();
		official.setId(officialId);
		official.setLastName(lastName);
		official.setFirstName(firstName);
		official.setFromDate(new LocalDate("2012-02-16"));
		official.setToDate(new LocalDate("9999-12-31"));
		official.setNumber("66");
		return official;
	}

	private Team getMockTeam(Long teamId, String teamKey) {
		Team team = new Team();
		team.setId(teamId);
		team.setTeamKey(teamKey);
		return team;
	}

	private Player getMockPlayer(String lastName, String firstName, LocalDate birthdate) {
		Player player = new Player();
		player.setLastName(lastName);
		player.setFirstName(firstName);
		player.setBirthdate(birthdate);
		return player;
	}

	private RosterPlayer getMockRosterPlayer(Long rosterPlayerId, String lastName, String firstName, LocalDate birthdate, LocalDate fromDate, LocalDate toDate) {
		RosterPlayer rosterPlayer = new RosterPlayer();
		rosterPlayer.setId(rosterPlayerId);
		rosterPlayer.setPlayer(getMockPlayer(lastName, firstName, birthdate));
		rosterPlayer.setFromDate(fromDate);
		rosterPlayer.setToDate(toDate);
		rosterPlayer.setPosition(RosterPlayer.Position.C);
		rosterPlayer.setNumber("99");
		return rosterPlayer;
	}

	private BoxScorePlayer createMockBoxScorePlayerHome_0() {
		BoxScorePlayer homeBoxScorePlayer = new BoxScorePlayer();
		homeBoxScorePlayer.setRosterPlayer(getMockRosterPlayer(2L, "Puzdrakiewicz", "Luke", new LocalDate("2002-02-20"), new LocalDate("2009-11-30"), new LocalDate("9999-12-31")));
		homeBoxScorePlayer.setPosition(Position.F);
		homeBoxScorePlayer.setStarter(true);
		homeBoxScorePlayer.setMinutes((short)20);
		homeBoxScorePlayer.setPoints((short)15);
		homeBoxScorePlayer.setAssists((short)5);
		homeBoxScorePlayer.setTurnovers((short)2);
		homeBoxScorePlayer.setSteals((short)3);
		homeBoxScorePlayer.setBlocks((short)2);
		homeBoxScorePlayer.setFieldGoalAttempts((short)14);
		homeBoxScorePlayer.setFieldGoalMade((short)7);
		homeBoxScorePlayer.setFieldGoalPercent((float).500);
		homeBoxScorePlayer.setThreePointAttempts((short)6);
		homeBoxScorePlayer.setThreePointMade((short)2);
		homeBoxScorePlayer.setThreePointPercent((float).333);
		homeBoxScorePlayer.setFreeThrowAttempts((short)4);
		homeBoxScorePlayer.setFreeThrowMade((short)4);
		homeBoxScorePlayer.setFreeThrowPercent((float)1.0);
		homeBoxScorePlayer.setReboundsOffense((short)3);
		homeBoxScorePlayer.setReboundsDefense((short)2);
		homeBoxScorePlayer.setPersonalFouls((short)3);
		return homeBoxScorePlayer;
	}

	private BoxScorePlayer createMockBoxScorePlayerHome_1() {
		BoxScorePlayer homeBoxScorePlayer = new BoxScorePlayer();
		homeBoxScorePlayer.setRosterPlayer(getMockRosterPlayer(3L, "Puzdrakiewicz", "Thad", new LocalDate("1966-06-02"), new LocalDate("2009-11-04"), new LocalDate("2009-10-30")));
		homeBoxScorePlayer.setPosition(Position.C);
		homeBoxScorePlayer.setStarter(false);
		homeBoxScorePlayer.setMinutes((short)5);
		homeBoxScorePlayer.setPoints((short)5);
		homeBoxScorePlayer.setAssists((short)1);
		homeBoxScorePlayer.setTurnovers((short)2);
		homeBoxScorePlayer.setSteals((short)0);
		homeBoxScorePlayer.setBlocks((short)1);
		homeBoxScorePlayer.setFieldGoalAttempts((short)5);
		homeBoxScorePlayer.setFieldGoalMade((short)5);
		homeBoxScorePlayer.setFieldGoalPercent((float)1.0);
		homeBoxScorePlayer.setThreePointAttempts((short)0);
		homeBoxScorePlayer.setThreePointMade((short)0);
		homeBoxScorePlayer.setThreePointPercent((float)0.0);
		homeBoxScorePlayer.setFreeThrowAttempts((short)4);
		homeBoxScorePlayer.setFreeThrowMade((short)0);
		homeBoxScorePlayer.setFreeThrowPercent((float)0.0);
		homeBoxScorePlayer.setReboundsOffense((short)2);
		homeBoxScorePlayer.setReboundsDefense((short)0);
		homeBoxScorePlayer.setPersonalFouls((short)5);
		return homeBoxScorePlayer;
	}

	private BoxScorePlayer createMockBoxScorePlayerAway() {
		BoxScorePlayer awayBoxScorePlayer = new BoxScorePlayer();
		awayBoxScorePlayer.setRosterPlayer(getMockRosterPlayer(4L, "Puzdrakiewicz", "Junior", new LocalDate("1966-06-10"), new LocalDate("2009-10-30"), new LocalDate("9999-12-31")));
		awayBoxScorePlayer.setPosition(Position.SG);
		awayBoxScorePlayer.setStarter(false);
		awayBoxScorePlayer.setMinutes((short)2);
		awayBoxScorePlayer.setPoints((short)5);
		awayBoxScorePlayer.setAssists((short)3);
		awayBoxScorePlayer.setTurnovers((short)0);
		awayBoxScorePlayer.setSteals((short)1);
		awayBoxScorePlayer.setBlocks((short)2);
		awayBoxScorePlayer.setFieldGoalAttempts((short)4);
		awayBoxScorePlayer.setFieldGoalMade((short)2);
		awayBoxScorePlayer.setFieldGoalPercent((float).500);
		awayBoxScorePlayer.setThreePointAttempts((short)1);
		awayBoxScorePlayer.setThreePointMade((short)0);
		awayBoxScorePlayer.setThreePointPercent((float).0);
		awayBoxScorePlayer.setFreeThrowAttempts((short)2);
		awayBoxScorePlayer.setFreeThrowMade((short)2);
		awayBoxScorePlayer.setFreeThrowPercent((float)1.0);
		awayBoxScorePlayer.setReboundsOffense((short)2);
		awayBoxScorePlayer.setReboundsDefense((short)0);
		awayBoxScorePlayer.setPersonalFouls((short)1);
		return awayBoxScorePlayer;
	}
}
