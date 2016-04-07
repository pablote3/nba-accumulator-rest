package com.rossotti.basketball.app.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.client.dto.StandingDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.dao.StandingDAO;
import com.rossotti.basketball.dao.TeamDAO;
import com.rossotti.basketball.dao.model.Standing;
import com.rossotti.basketball.util.DateTimeUtil;

@Service
public class StandingsServiceBean {
	@Autowired
	private StandingDAO standingDAO;

	@Autowired
	private TeamDAO teamDAO;

	public List<Standing> getStandings(StandingsDTO standingsDTO) {
		LocalDate asOfDate = DateTimeUtil.getLocalDate(standingsDTO.standings_date);
		List<Standing> standings = new ArrayList<Standing>();
		StandingDTO standingDTO;
		Standing standing;
		for (int i = 0; i < standingsDTO.standing.length; i++) {
			standingDTO = standingsDTO.standing[i];
			standing = new Standing();
			standing.setTeam(teamDAO.findTeam(standingDTO.getTeam_id(), asOfDate));
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

	public List<Standing> findStandings(LocalDate asOfDate) {
		return standingDAO.findStandings(asOfDate);
	}

	public Standing createStanding(Standing standing) {
		return standingDAO.createStanding(standing);
	}

	public Standing deleteStanding(String teamKey, LocalDate asOfDate) {
		return standingDAO.deleteStanding(teamKey, asOfDate);
	}
}