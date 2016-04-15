package com.rossotti.basketball.app.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.client.dto.StandingDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.BoxScore;
import com.rossotti.basketball.dao.model.BoxScore.Result;
import com.rossotti.basketball.dao.repository.GameRepository;
import com.rossotti.basketball.dao.repository.StandingRepository;
import com.rossotti.basketball.dao.repository.TeamRepository;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.Standing;
import com.rossotti.basketball.dao.model.StandingRecord;
import com.rossotti.basketball.dao.model.Team;
import com.rossotti.basketball.util.DateTimeUtil;

@Service
public class StandingsService {
	@Autowired
	private StandingRepository standingRepo;

	@Autowired
	private TeamRepository teamRepo;

	@Autowired
	private GameRepository gameRepo;

	private final Logger logger = LoggerFactory.getLogger(StandingsService.class);

	public List<Standing> getStandings(StandingsDTO standingsDTO) {
		LocalDate asOfDate = DateTimeUtil.getLocalDate(standingsDTO.standings_date);
		List<Standing> standings = new ArrayList<Standing>();
		for (int i = 0; i < standingsDTO.standing.length; i++) {
			StandingDTO standingDTO = standingsDTO.standing[i];
			Team team = teamRepo.findTeam(standingDTO.getTeam_id(), asOfDate);
			if (team.isNotFound()) {
				logger.info("Team not found " + standingDTO.getTeam_id());
				throw new NoSuchEntityException(Team.class);
			}
			Standing standing = new Standing();
			standing.setTeam(team);
			standing.setStandingDate(asOfDate);
			standing.setRank(standingDTO.getRank());
			standing.setOrdinalRank(standingDTO.getOrdinal_rank());
			standing.setGamesWon(standingDTO.getWon());
			standing.setGamesLost(standingDTO.getLost());
			standing.setStreak(standingDTO.getStreak());
			standing.setStreakType(standingDTO.getStreak_type());
			standing.setStreakTotal(standingDTO.getStreak_total());
			standing.setGamesBack(standingDTO.getGames_back());
			standing.setPointsFor(standingDTO.getPoints_for());
			standing.setPointsAgainst(standingDTO.getPoints_against());
			standing.setHomeWins(standingDTO.getHome_won());
			standing.setHomeLosses(standingDTO.getHome_lost());
			standing.setAwayWins(standingDTO.getAway_won());
			standing.setAwayLosses(standingDTO.getAway_lost());
			standing.setConferenceWins(standingDTO.getConference_won());
			standing.setConferenceLosses(standingDTO.getConference_lost());
			standing.setLastFive(standingDTO.getLast_five());
			standing.setLastTen(standingDTO.getLast_ten());
			standing.setGamesPlayed(standingDTO.getGames_played());
			standing.setPointsScoredPerGame(standingDTO.getPoints_scored_per_game());
			standing.setPointsAllowedPerGame(standingDTO.getPoints_allowed_per_game());
			standing.setWinPercentage(standingDTO.getWin_percentage());
			standing.setPointDifferential(standingDTO.getPoint_differential());
			standing.setPointDifferentialPerGame(standingDTO.getPoint_differential_per_game());
			standings.add(standing);
		}
		return standings;
	}

	public Standing findStanding(String teamKey, LocalDate asOfDate) {
		return standingRepo.findStanding(teamKey, asOfDate);
	}

	public List<Standing> findStandings(LocalDate asOfDate) {
		return standingRepo.findStandings(asOfDate);
	}

	public Standing createStanding(Standing standing) {
		return standingRepo.createStanding(standing);
	}

	public List<Standing> createTeamStandings(List<Standing> standings) {
		for (int i = 0; i < standings.size(); i++) {
			standings.set(i, standingRepo.createStanding(standings.get(i)));
		}
		return standings;
	}

	public Standing updateStanding(Standing standing) {
		return standingRepo.updateStanding(standing);
	}

	public List<Standing> deleteStandings(LocalDate asOfDate) {
		List<Standing> standings = standingRepo.findStandings(asOfDate);
		if (!standings.isEmpty() && standings.size() > 0) {
			logger.info("Deleting standings for " + DateTimeUtil.getStringDate(asOfDate));
			for (int i = 0; i < standings.size(); i++) {
				String team = standings.get(i).getTeam().getTeamKey();
				standings.set(i, standingRepo.deleteStanding(team, asOfDate));
			}
		}
		return standings;
	}

	public Map<String, StandingRecord> buildStandingsMap(List<Standing> standings, LocalDate asOfDate) {
		Map<String, StandingRecord> standingsMap = new HashMap<String, StandingRecord>();
		//create map with team games won/played
		for (int i = 0; i < standings.size(); i++) {
			StandingRecord standingRecord = new StandingRecord((int)standings.get(i).getGamesWon(), (int)standings.get(i).getGamesPlayed(), 0, 0);
			standingsMap.put(standings.get(i).getTeam().getTeamKey(), standingRecord);
		}

		//update map summing opponent games won/played
		for (int i = 0; i < standings.size(); i++) {
			String teamKey = standings.get(i).getTeam().getTeamKey();
			Integer opptGamesWon = 0;
			Integer opptGamesPlayed = 0;
			List<Game> completeGames = gameRepo.findByDateTeamSeason(asOfDate, teamKey);
			for (int j = 0; j < completeGames.size(); j++) {
				Game completedGame = completeGames.get(j);
				int opptBoxScoreId = completedGame.getBoxScores().get(0).getTeam().getTeamKey().equals(teamKey) ? 1 : 0;
				String opptTeamKey = completedGame.getBoxScores().get(opptBoxScoreId).getTeam().getTeamKey();
				opptGamesWon = opptGamesWon + standingsMap.get(opptTeamKey).getGamesWon();
				opptGamesPlayed = opptGamesPlayed + standingsMap.get(opptTeamKey).getGamesPlayed();

				String completedGameDate = DateTimeUtil.getStringDate(completedGame.getGameDateTime());
				logger.debug('\n' + ("  StandingsMap " + teamKey + " " + completedGameDate + " " + opptTeamKey + 
									" Games Won/Played: " + standingsMap.get(opptTeamKey).getGamesWon() + " - " + standingsMap.get(opptTeamKey).getGamesPlayed()));
			}
			standingsMap.get(teamKey).setOpptGamesWon(opptGamesWon);
			standingsMap.get(teamKey).setOpptGamesPlayed(opptGamesPlayed);
		}
		return standingsMap;
	}

	public Map<String, StandingRecord> buildHeadToHeadMap(String teamKey, LocalDate asOfDate, Map<String, StandingRecord> standingsMap) {
		Map<String, StandingRecord> headToHeadMap = new HashMap<String, StandingRecord>();
		List<Game> completeGames = gameRepo.findByDateTeamSeason(asOfDate, teamKey);

		for (int i = 0; i < completeGames.size(); i++) {
			int opptBoxScoreId = completeGames.get(i).getBoxScores().get(0).getTeam().getTeamKey().equals(teamKey) ? 1 : 0;
			BoxScore opptBoxScore = completeGames.get(i).getBoxScores().get(opptBoxScoreId);
			String opptTeamKey = opptBoxScore.getTeam().getTeamKey();
			Integer opptHeadToHeadResult = opptBoxScore.getResult() != null && opptBoxScore.getResult().equals(Result.Win) ? 1 : 0;
			if (headToHeadMap.get(opptTeamKey) == null) {
				headToHeadMap.put(opptTeamKey, new StandingRecord(opptHeadToHeadResult, 1, standingsMap.get(teamKey).getGamesWon(), standingsMap.get(teamKey).getGamesPlayed()));
			}
			else {
				headToHeadMap.get(opptTeamKey).setGamesWon(headToHeadMap.get(opptTeamKey).getGamesWon() + opptHeadToHeadResult);
				headToHeadMap.get(opptTeamKey).setGamesPlayed(headToHeadMap.get(opptTeamKey).getGamesPlayed() + 1);
				headToHeadMap.get(opptTeamKey).setOpptGamesWon(headToHeadMap.get(opptTeamKey).getOpptGamesWon() + standingsMap.get(teamKey).getGamesWon());
				headToHeadMap.get(opptTeamKey).setOpptGamesPlayed(headToHeadMap.get(opptTeamKey).getOpptGamesPlayed() + standingsMap.get(teamKey).getGamesPlayed());
			}
		}
		return headToHeadMap;
	}

	public StandingRecord calculateStrengthOfSchedule(String teamKey, LocalDate asOfDate, Map<String, StandingRecord> standingsMap, Map<String, StandingRecord> headToHeadMap) {
		BoxScore opptBoxScore;
		Integer opptGamesWon = 0;
		Integer opptGamesPlayed = 0;
		Integer opptOpptGamesWon = 0;
		Integer opptOpptGamesPlayed = 0;
		List<Game> completeGames = gameRepo.findByDateTeamSeason(asOfDate, teamKey);

		for (int i = 0; i < completeGames.size(); i++) {
			int opptBoxScoreId = completeGames.get(i).getBoxScores().get(0).getTeam().getTeamKey().equals(teamKey) ? 1 : 0;
			opptBoxScore = completeGames.get(i).getBoxScores().get(opptBoxScoreId);
			String opptTeamKey = opptBoxScore.getTeam().getTeamKey();

			opptGamesWon = opptGamesWon + standingsMap.get(opptTeamKey).getGamesWon() - headToHeadMap.get(opptTeamKey).getGamesWon();
			opptGamesPlayed = opptGamesPlayed + standingsMap.get(opptTeamKey).getGamesPlayed() - headToHeadMap.get(opptTeamKey).getGamesPlayed();
			opptOpptGamesWon = opptOpptGamesWon + standingsMap.get(opptTeamKey).getOpptGamesWon() - headToHeadMap.get(opptTeamKey).getOpptGamesWon();
			opptOpptGamesPlayed = opptOpptGamesPlayed + standingsMap.get(opptTeamKey).getOpptGamesPlayed() - headToHeadMap.get(opptTeamKey).getOpptGamesPlayed();

			logger.debug('\n' + "SubTeamStanding " + opptTeamKey);
			logger.debug('\n' + "  Opponent Games Won/Played: " + opptGamesWon + " - " + opptGamesPlayed + " = " + 
									standingsMap.get(opptTeamKey).getGamesWon() + " - " + standingsMap.get(opptTeamKey).getGamesPlayed() + " minus " + 
									headToHeadMap.get(opptTeamKey).getGamesWon() + " - " + headToHeadMap.get(opptTeamKey).getGamesPlayed());
			logger.debug('\n' + "  OpptOppt Games Won/Played: " + opptOpptGamesWon + " - " + opptOpptGamesPlayed + " = " + 
									standingsMap.get(opptTeamKey).getOpptGamesWon() + " - " + standingsMap.get(opptTeamKey).getOpptGamesPlayed() + " minus " + 
									headToHeadMap.get(opptTeamKey).getOpptGamesWon() + " - " + headToHeadMap.get(opptTeamKey).getOpptGamesPlayed());

			if (opptGamesWon > opptGamesPlayed) { 
				//head to head wins exceed opponent wins, should only occur until wins start to occur
				//observed occurrence when loading standings before entire day's games were loaded
				logger.info('\n' + "Crazy opptGamesWon more than opptGamesPlayed!");
				opptGamesWon = opptGamesPlayed;
			}
		}

		BigDecimal opptRecord = opptGamesPlayed == 0 ? new BigDecimal(0) : new BigDecimal(opptGamesWon).divide(new BigDecimal(opptGamesPlayed), 4, RoundingMode.HALF_UP);
		BigDecimal opptOpptRecord = opptOpptGamesWon == 0 ? new BigDecimal(0) : new BigDecimal(opptOpptGamesWon).divide(new BigDecimal(opptOpptGamesPlayed), 4, RoundingMode.HALF_UP);
		logger.debug('\n' + "  Opponent Games Won/Played = " + opptGamesWon + "-" + opptGamesPlayed);
		logger.debug('\n' + "  OpptOppt Games Won/Played = " + opptOpptGamesWon + "-" + opptOpptGamesPlayed);
		logger.debug('\n' + "  Opponent Record = " + opptRecord);
		logger.debug('\n' + "  OpptOppt Record = " + opptOpptRecord);
		logger.info('\n' + "  Strenghth Of Schedule " + teamKey + " " + opptRecord.multiply(new BigDecimal(2)).add(opptOpptRecord).divide(new BigDecimal(3), 4, RoundingMode.HALF_UP));
		
		return new StandingRecord(opptGamesWon, opptGamesPlayed, opptOpptGamesWon, opptOpptGamesPlayed);
	}
}