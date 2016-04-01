package com.rossotti.basketball.dao.mapper;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.app.resource.ScoreResource;
import com.rossotti.basketball.client.dto.OfficialDTO;
import com.rossotti.basketball.dao.OfficialDAO;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.GameOfficial;
import com.rossotti.basketball.dao.model.Official;
import com.rossotti.basketball.dao.model.StatusCode;

@Service
public class GameMapperBean {
	@Autowired
	private OfficialDAO officialDAO;
	
	private final Logger logger = LoggerFactory.getLogger(GameMapperBean.class);

//	public BoxScore getBoxScoreStats(BoxScore boxScore, BoxScore stats) {
//		boxScore.setMinutes(stats.getMinutes());
//		boxScore.setPoints(stats.getPoints());
//		boxScore.setAssists(stats.getAssists());
//		boxScore.setTurnovers(stats.getTurnovers());
//		boxScore.setSteals(stats.getSteals());
//		boxScore.setBlocks(stats.getBlocks());
//		boxScore.setFieldGoalAttempts(stats.getFieldGoalAttempts());
//		boxScore.setFieldGoalMade(stats.getFieldGoalMade());
//		boxScore.setFieldGoalPercent(stats.getFieldGoalPercent());
//		boxScore.setThreePointAttempts(stats.getThreePointAttempts());
//		boxScore.setThreePointMade(stats.getThreePointMade());
//		boxScore.setThreePointPercent(stats.getThreePointPercent());
//		boxScore.setFreeThrowAttempts(stats.getFreeThrowAttempts());
//		boxScore.setFreeThrowMade(stats.getFreeThrowMade());
//		boxScore.setFreeThrowPercent(stats.getFreeThrowPercent());
//		boxScore.setReboundsOffense(stats.getReboundsOffense());
//		boxScore.setReboundsDefense(stats.getReboundsDefense());
//		boxScore.setPersonalFouls(stats.getPersonalFouls());
//		return boxScore;
//	}

	public List<GameOfficial> getGameOfficials(OfficialDTO[] officials, LocalDate gameDate) {
		List<GameOfficial> gameOfficials = new ArrayList<GameOfficial>();
		GameOfficial gameOfficial;
		Official official;
		for (int i = 0; i < officials.length; i++) {
			String lastName = officials[i].getLast_name();
			String firstName = officials[i].getFirst_name();
			official = officialDAO.findOfficial(lastName, firstName, gameDate);
			if (official.isNotFound()) {
				logger.info("Official not found " + firstName + " " + lastName);
				throw new NoSuchEntityException(Official.class);
			}
			else {
				gameOfficial = new GameOfficial();
				gameOfficial.setOfficial(official);
				gameOfficials.add(gameOfficial);
			}
		}
		return gameOfficials;
	}

//	public List<BoxScorePlayer> getBoxScorePlayers(BoxScorePlayerDTO[] boxScorePlayerDTOs, String gameDate) {
//		List<BoxScorePlayer> boxScorePlayers = new ArrayList<BoxScorePlayer>();
//		BoxScorePlayerDTO boxScorePlayerDTO;
//		BoxScorePlayer boxScorePlayer;
//		RosterPlayer rosterPlayer;
//
//		for (int i = 0; i < boxScorePlayerDTOs.length; i++) {
//			boxScorePlayerDTO = boxScorePlayerDTOs[i];
//			String lastName = boxScorePlayerDTO.getLast_name();
//			String firstName = boxScorePlayerDTO.getFirst_name();
//			String teamKey = Team.findByAbbr(boxScorePlayerDTO.getTeam_abbreviation()).getKey();
//			rosterPlayer = RosterPlayer.findByDatePlayerNameTeam(gameDate, lastName, firstName, teamKey);
//			if (rosterPlayer == null) {
//				System.out.println("Player not found " + firstName + " " + lastName + " on " + teamKey);
//				return null;
//			}
//			else {
//				boxScorePlayer = new BoxScorePlayer();
//				boxScorePlayer.setRosterPlayer(rosterPlayer);
//				boxScorePlayer.setPosition(BoxScorePlayer.Position.valueOf(boxScorePlayerDTO.getPosition()));
//				boxScorePlayer.setMinutes(boxScorePlayerDTO.getMinutes());
//				boxScorePlayer.setStarter(boxScorePlayerDTO.getIs_starter());
//				boxScorePlayer.setPoints(boxScorePlayerDTO.getPoints());
//				boxScorePlayer.setAssists(boxScorePlayerDTO.getAssists());
//				boxScorePlayer.setTurnovers(boxScorePlayerDTO.getTurnovers());
//				boxScorePlayer.setSteals(boxScorePlayerDTO.getSteals());
//				boxScorePlayer.setBlocks(boxScorePlayerDTO.getBlocks());
//				boxScorePlayer.setFieldGoalAttempts(boxScorePlayerDTO.getField_goals_attempted());
//				boxScorePlayer.setFieldGoalMade(boxScorePlayerDTO.getField_goals_made());
//				boxScorePlayer.setFieldGoalPercent(boxScorePlayerDTO.getField_goal_percentage());
//				boxScorePlayer.setThreePointAttempts(boxScorePlayerDTO.getThree_point_field_goals_attempted());
//				boxScorePlayer.setThreePointMade(boxScorePlayerDTO.getThree_point_field_goals_made());
//				boxScorePlayer.setThreePointPercent(boxScorePlayerDTO.getThree_point_percentage());
//				boxScorePlayer.setFreeThrowAttempts(boxScorePlayerDTO.getFree_throws_attempted());
//				boxScorePlayer.setFreeThrowMade(boxScorePlayerDTO.getFree_throws_made());
//				boxScorePlayer.setFreeThrowPercent(boxScorePlayerDTO.getFree_throw_percentage());
//				boxScorePlayer.setReboundsOffense(boxScorePlayerDTO.getOffensive_rebounds());
//				boxScorePlayer.setReboundsDefense(boxScorePlayerDTO.getDefensive_rebounds());
//				boxScorePlayer.setPersonalFouls(boxScorePlayerDTO.getPersonal_fouls());
//				boxScorePlayers.add(boxScorePlayer);
//			}
//		}
//		return boxScorePlayers;
//	}

//	public List<RosterPlayer> getRosterPlayers(Roster xmlStatsRoster) {
//		RosterPlayerDTO[] rosterPlayerDTOs = xmlStatsRoster.players;
//		Team team = Team.findByTeamKey(xmlStatsRoster.team.getKey());
//		List<RosterPlayer> rosterPlayers = new ArrayList<RosterPlayer>();
//		Player player;
//		RosterPlayer rosterPlayer;
//		for (int i = 0; i < rosterPlayerDTOs.length; i++) {
//			player = new Player();
//			player.setLastName(rosterPlayerDTOs[i].getLast_name());
//			player.setFirstName(rosterPlayerDTOs[i].getFirst_name());
//			player.setDisplayName(rosterPlayerDTOs[i].getDisplay_name());
//			player.setHeight(rosterPlayerDTOs[i].getHeight_in());
//			player.setWeight(rosterPlayerDTOs[i].getWeight_lb());
//			player.setBirthDate(DateTimeUtil.getLocalDateFromDateTime(rosterPlayerDTOs[i].getBirthdate()));
//			player.setBirthPlace(rosterPlayerDTOs[i].getBirthplace());
//			rosterPlayer = new RosterPlayer();
//			rosterPlayer.setPlayer(player);
//			rosterPlayer.setTeam(team);
//			rosterPlayer.setNumber(rosterPlayerDTOs[i].getUniform_number());
//			rosterPlayer.setPosition(Position.valueOf(rosterPlayerDTOs[i].getPosition()));
//			rosterPlayers.add(rosterPlayer);
//		}
//		return rosterPlayers;
//	}
}