package com.rossotti.basketball.app.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.client.dto.BoxScorePlayerDTO;
import com.rossotti.basketball.client.dto.RosterPlayerDTO;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.BoxScorePlayer;
import com.rossotti.basketball.dao.model.Player;
import com.rossotti.basketball.dao.model.RosterPlayer;
import com.rossotti.basketball.dao.model.Position;
import com.rossotti.basketball.dao.model.Team;
import com.rossotti.basketball.dao.repository.RosterPlayerRepository;
import com.rossotti.basketball.dao.repository.TeamRepository;
import com.rossotti.basketball.util.DateTimeUtil;

@Service
public class RosterPlayerService {
	@Autowired
	private RosterPlayerRepository rosterPlayerRepo;

	@Autowired
	private TeamRepository teamRepo;

	private final Logger logger = LoggerFactory.getLogger(RosterPlayerService.class);

	public List<BoxScorePlayer> getBoxScorePlayers(BoxScorePlayerDTO[] boxScorePlayerDTOs, LocalDate gameDate, String teamKey) {
		List<BoxScorePlayer> boxScorePlayers = new ArrayList<BoxScorePlayer>();
		BoxScorePlayer boxScorePlayer;
		RosterPlayer rosterPlayer;

		for (int i = 0; i < boxScorePlayerDTOs.length; i++) {
			String lastName = boxScorePlayerDTOs[i].getLast_name();
			String firstName = boxScorePlayerDTOs[i].getFirst_name();
			rosterPlayer = rosterPlayerRepo.findRosterPlayer(lastName, firstName, teamKey, gameDate);
			if (rosterPlayer.isNotFound()) {
				logger.info("Roster Player not found " + firstName + " " + lastName + " Team: " + teamKey + " GameDate: " + gameDate);
				throw new NoSuchEntityException(RosterPlayer.class);
			}
			else {
				boxScorePlayer = new BoxScorePlayer();
				boxScorePlayer.setRosterPlayer(rosterPlayer);
				boxScorePlayer.setPosition(Position.valueOf(boxScorePlayerDTOs[i].getPosition()));
				boxScorePlayer.setMinutes(boxScorePlayerDTOs[i].getMinutes());
				boxScorePlayer.setStarter(boxScorePlayerDTOs[i].getIs_starter());
				boxScorePlayer.setPoints(boxScorePlayerDTOs[i].getPoints());
				boxScorePlayer.setAssists(boxScorePlayerDTOs[i].getAssists());
				boxScorePlayer.setTurnovers(boxScorePlayerDTOs[i].getTurnovers());
				boxScorePlayer.setSteals(boxScorePlayerDTOs[i].getSteals());
				boxScorePlayer.setBlocks(boxScorePlayerDTOs[i].getBlocks());
				boxScorePlayer.setFieldGoalAttempts(boxScorePlayerDTOs[i].getField_goals_attempted());
				boxScorePlayer.setFieldGoalMade(boxScorePlayerDTOs[i].getField_goals_made());
				boxScorePlayer.setFieldGoalPercent(boxScorePlayerDTOs[i].getField_goal_percentage());
				boxScorePlayer.setThreePointAttempts(boxScorePlayerDTOs[i].getThree_point_field_goals_attempted());
				boxScorePlayer.setThreePointMade(boxScorePlayerDTOs[i].getThree_point_field_goals_made());
				boxScorePlayer.setThreePointPercent(boxScorePlayerDTOs[i].getThree_point_percentage());
				boxScorePlayer.setFreeThrowAttempts(boxScorePlayerDTOs[i].getFree_throws_attempted());
				boxScorePlayer.setFreeThrowMade(boxScorePlayerDTOs[i].getFree_throws_made());
				boxScorePlayer.setFreeThrowPercent(boxScorePlayerDTOs[i].getFree_throw_percentage());
				boxScorePlayer.setReboundsOffense(boxScorePlayerDTOs[i].getOffensive_rebounds());
				boxScorePlayer.setReboundsDefense(boxScorePlayerDTOs[i].getDefensive_rebounds());
				boxScorePlayer.setPersonalFouls(boxScorePlayerDTOs[i].getPersonal_fouls());
				boxScorePlayers.add(boxScorePlayer);
			}
		}
		return boxScorePlayers;
	}

	public List<RosterPlayer> getRosterPlayers(RosterPlayerDTO[] rosterPlayerDTOs, LocalDate gameDate, String teamKey) {
		List<RosterPlayer> rosterPlayers = new ArrayList<RosterPlayer>();
		RosterPlayer rosterPlayer;
		Player player;
		Team team;
		team = teamRepo.findTeam(teamKey, gameDate);
		if (team.isNotFound()) {
			logger.info("Team not found " + teamKey);
			throw new NoSuchEntityException(Team.class);
		}
		else {
			for (int i = 0; i < rosterPlayerDTOs.length; i++) {
				player = new Player();
				player.setLastName(rosterPlayerDTOs[i].getLast_name());
				player.setFirstName(rosterPlayerDTOs[i].getFirst_name());
				player.setDisplayName(rosterPlayerDTOs[i].getDisplay_name());
				player.setHeight(rosterPlayerDTOs[i].getHeight_in());
				player.setWeight(rosterPlayerDTOs[i].getWeight_lb());
				player.setBirthdate(DateTimeUtil.getLocalDate(rosterPlayerDTOs[i].getBirthdate()));
				player.setBirthplace(rosterPlayerDTOs[i].getBirthplace());
				rosterPlayer = new RosterPlayer();
				rosterPlayer.setPlayer(player);
				rosterPlayer.setTeam(team);
				rosterPlayer.setNumber(rosterPlayerDTOs[i].getUniform_number());
				rosterPlayer.setPosition(Position.valueOf(rosterPlayerDTOs[i].getPosition()));
				rosterPlayers.add(rosterPlayer);
			}
		}
		return rosterPlayers;
	}

	public RosterPlayer findByDatePlayerNameTeam(LocalDate asOfDate, String lastName, String firstName, String teamKey) {
		return rosterPlayerRepo.findRosterPlayer(lastName, firstName, teamKey, asOfDate);
	}

	public RosterPlayer findLatestByPlayerNameBirthdateSeason(LocalDate asOfDate, String lastName, String firstName, LocalDate birthdate) {
		return rosterPlayerRepo.findRosterPlayer(lastName, firstName, birthdate, asOfDate);
	}

	public RosterPlayer createRosterPlayer(RosterPlayer rosterPlayer) {
		return rosterPlayerRepo.createRosterPlayer(rosterPlayer);
	}

	public RosterPlayer updateRosterPlayer(RosterPlayer rosterPlayer) {
		return rosterPlayerRepo.updateRosterPlayer(rosterPlayer);
	}

	public List<RosterPlayer> findRosterPlayers(LocalDate asOfDate, String teamKey) {
		return rosterPlayerRepo.findRosterPlayers(teamKey, asOfDate);
	}
}