package com.rossotti.basketball.dao.mapper;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.client.dto.BoxScorePlayerDTO;
import com.rossotti.basketball.client.dto.OfficialDTO;
import com.rossotti.basketball.dao.OfficialDAO;
import com.rossotti.basketball.dao.RosterPlayerDAO;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.BoxScorePlayer;
import com.rossotti.basketball.dao.model.GameOfficial;
import com.rossotti.basketball.dao.model.Official;
import com.rossotti.basketball.dao.model.RosterPlayer;

@Service
public class GameMapperBean {
	@Autowired
	private OfficialDAO officialDAO;
	
	@Autowired
	private RosterPlayerDAO rosterPlayerDAO;
	
	private final Logger logger = LoggerFactory.getLogger(GameMapperBean.class);

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

	public List<BoxScorePlayer> getBoxScorePlayers(BoxScorePlayerDTO[] boxScorePlayerDTOs, LocalDate gameDate, String teamKey) {
		List<BoxScorePlayer> boxScorePlayers = new ArrayList<BoxScorePlayer>();
		BoxScorePlayerDTO boxScorePlayerDTO;
		BoxScorePlayer boxScorePlayer;
		RosterPlayer rosterPlayer;

		for (int i = 0; i < boxScorePlayerDTOs.length; i++) {
			boxScorePlayerDTO = boxScorePlayerDTOs[i];
			String lastName = boxScorePlayerDTO.getLast_name();
			String firstName = boxScorePlayerDTO.getFirst_name();
			rosterPlayer = rosterPlayerDAO.findRosterPlayer(lastName, firstName, teamKey, gameDate);
			if (rosterPlayer.isNotFound()) {
				logger.info("Roster Player not found " + firstName + " " + lastName + " Team: " + teamKey + " GameDate: " + gameDate);
				throw new NoSuchEntityException(RosterPlayer.class);
			}
			else {
				boxScorePlayer = new BoxScorePlayer();
				boxScorePlayer.setRosterPlayer(rosterPlayer);
				boxScorePlayer.setPosition(BoxScorePlayer.Position.valueOf(boxScorePlayerDTO.getPosition()));
				boxScorePlayer.setMinutes(boxScorePlayerDTO.getMinutes());
				boxScorePlayer.setStarter(boxScorePlayerDTO.getIs_starter());
				boxScorePlayer.setPoints(boxScorePlayerDTO.getPoints());
				boxScorePlayer.setAssists(boxScorePlayerDTO.getAssists());
				boxScorePlayer.setTurnovers(boxScorePlayerDTO.getTurnovers());
				boxScorePlayer.setSteals(boxScorePlayerDTO.getSteals());
				boxScorePlayer.setBlocks(boxScorePlayerDTO.getBlocks());
				boxScorePlayer.setFieldGoalAttempts(boxScorePlayerDTO.getField_goals_attempted());
				boxScorePlayer.setFieldGoalMade(boxScorePlayerDTO.getField_goals_made());
				boxScorePlayer.setFieldGoalPercent(boxScorePlayerDTO.getField_goal_percentage());
				boxScorePlayer.setThreePointAttempts(boxScorePlayerDTO.getThree_point_field_goals_attempted());
				boxScorePlayer.setThreePointMade(boxScorePlayerDTO.getThree_point_field_goals_made());
				boxScorePlayer.setThreePointPercent(boxScorePlayerDTO.getThree_point_percentage());
				boxScorePlayer.setFreeThrowAttempts(boxScorePlayerDTO.getFree_throws_attempted());
				boxScorePlayer.setFreeThrowMade(boxScorePlayerDTO.getFree_throws_made());
				boxScorePlayer.setFreeThrowPercent(boxScorePlayerDTO.getFree_throw_percentage());
				boxScorePlayer.setReboundsOffense(boxScorePlayerDTO.getOffensive_rebounds());
				boxScorePlayer.setReboundsDefense(boxScorePlayerDTO.getDefensive_rebounds());
				boxScorePlayer.setPersonalFouls(boxScorePlayerDTO.getPersonal_fouls());
				boxScorePlayers.add(boxScorePlayer);
			}
		}
		return boxScorePlayers;
	}

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