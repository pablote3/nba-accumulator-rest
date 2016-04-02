package com.rossotti.basketball.dao.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.client.dto.BoxScorePlayerDTO;
import com.rossotti.basketball.dao.RosterPlayerDAO;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.BoxScorePlayer;
import com.rossotti.basketball.dao.model.RosterPlayer;

@Service
public class RosterPlayerServiceBean {
	@Autowired
	private RosterPlayerDAO rosterPlayerDAO;

	private final Logger logger = LoggerFactory.getLogger(RosterPlayerServiceBean.class);

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
}