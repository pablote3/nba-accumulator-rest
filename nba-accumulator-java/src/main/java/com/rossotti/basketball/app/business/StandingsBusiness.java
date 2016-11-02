package com.rossotti.basketball.app.business;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import com.rossotti.basketball.client.dto.ClientSource;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.app.exception.PropertyException;
import com.rossotti.basketball.app.service.PropertyService;
import com.rossotti.basketball.app.service.StandingsService;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.client.service.FileStatsService;
import com.rossotti.basketball.client.service.RestStatsService;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.AppStandings;
import com.rossotti.basketball.dao.model.AppStatus;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.Standing;
import com.rossotti.basketball.dao.model.StandingRecord;
import com.rossotti.basketball.dao.model.Team;
import com.rossotti.basketball.util.DateTimeUtil;

@Service
public class StandingsBusiness {
	private final PropertyService propertyService;

	private final RestStatsService restStatsService;

	private final FileStatsService fileStatsService;

	private final StandingsService standingsService;

	private final Logger logger = LoggerFactory.getLogger(StandingsBusiness.class);

	@Autowired
	public StandingsBusiness(FileStatsService fileStatsService, RestStatsService restStatsService, PropertyService propertyService, StandingsService standingsService) {
		this.fileStatsService = fileStatsService;
		this.restStatsService = restStatsService;
		this.propertyService = propertyService;
		this.standingsService = standingsService;
	}

	public AppStandings rankStandings(String asOfDateString) {
		AppStandings appStandings = new AppStandings();
		try {
			StandingsDTO standingsDTO;
			ClientSource clientSource = propertyService.getProperty_ClientSource("accumulator.source.standings");
			LocalDate asOfDate = DateTimeUtil.getLocalDate(asOfDateString);
			String nakedAsOfDate = DateTimeUtil.getStringDateNaked(asOfDate);
			if (clientSource == ClientSource.File) {
				standingsDTO = fileStatsService.retrieveStandings(nakedAsOfDate, asOfDate);
			}
			else if (clientSource == ClientSource.Api) {
				standingsDTO = restStatsService.retrieveStandings(nakedAsOfDate, asOfDate);
			}
			else {
				throw new PropertyException("Unknown");
			}

			if (standingsDTO.isFound()) {
				if (standingsDTO.standing.length > 0) {
					logger.debug("Rank standings");

					//clear existing standings
					standingsService.deleteStandings(asOfDate);

					List<Standing> standings = standingsService.getStandings(standingsDTO);
					Map<String, StandingRecord> standingsMap = standingsService.buildStandingsMap(standings, asOfDate);

					for (int i = 0; i < standings.size(); i++) {
						Standing standing = standings.get(i);
						String teamKey = standing.getTeam().getTeamKey();
						Map<String, StandingRecord> headToHeadMap = standingsService.buildHeadToHeadMap(teamKey, asOfDate, standingsMap);
						StandingRecord standingRecord = standingsService.calculateStrengthOfSchedule(teamKey, asOfDate, standingsMap, headToHeadMap);
						standing.setOpptGamesWon(standingRecord.getGamesWon());
						standing.setOpptGamesPlayed(standingRecord.getGamesPlayed());
						standing.setOpptOpptGamesWon(standingRecord.getOpptGamesWon());
						standing.setOpptOpptGamesPlayed(standingRecord.getOpptGamesPlayed());
						Standing createdStanding = standingsService.createStanding(standing);
						if (createdStanding.isCreated()) {
							BigDecimal opponentRecord = standingRecord.getGamesPlayed() == 0 ? new BigDecimal(0) : new BigDecimal(standingRecord.getGamesWon()).divide(new BigDecimal(standingRecord.getGamesPlayed()), 4, RoundingMode.HALF_UP);
							BigDecimal opponentOpponentRecord = standingRecord.getOpptGamesPlayed() == 0 ? new BigDecimal(0) : new BigDecimal(standingRecord.getOpptGamesWon()).divide(new BigDecimal(standingRecord.getOpptGamesPlayed()), 4, RoundingMode.HALF_UP);
							logger.debug("    Opponent Games Won/Played = " + standingRecord.getGamesWon() + "-" + standingRecord.getGamesPlayed());
							logger.debug("    OpptOppt Games Won/Played = " + standingRecord.getOpptGamesWon() + "-" + standingRecord.getOpptGamesPlayed());
							logger.debug("    Opponent Record = " + opponentRecord);
							logger.debug("    OpptOppt Record = " + opponentOpponentRecord);
							logger.info("  Strenghth Of Schedule  " + standing.getTeam().getAbbr() + ": " + opponentRecord.multiply(new BigDecimal(2)).add(opponentOpponentRecord).divide(new BigDecimal(3), 4, RoundingMode.HALF_UP));
						}
						else {
							logger.info("Unable to create standing");
							throw new Exception("Unknown");
						}
					}

					logger.info("StandingsCount: " + standings.size() + " Completed: route to outputChannel");
					appStandings.setStandings(standingsService.findStandings(asOfDate));
					appStandings.setAppStatus(AppStatus.Completed);
				}
				else {
					logger.info("Client exception - standings found with empty list");
					appStandings.setAppStatus(AppStatus.ClientError);
				}
			}
			else if (standingsDTO.isNotFound()) {
				logger.info("Unable to find standings");
				appStandings.setAppStatus(AppStatus.ClientError);
			}
			else {
				logger.info("Client error retrieving standings");
				appStandings.setAppStatus(AppStatus.ClientError);
			}
		}
		catch (NoSuchEntityException nse) {
			if (nse.getEntityClass().equals(Team.class)) {
				logger.info("Team not found");
			}
			appStandings.setAppStatus(AppStatus.ClientError);
		}
		catch (PropertyException pe) {
			logger.info("Property exception = " + pe);
			appStandings.setAppStatus(AppStatus.ServerError);
		}
		catch (Exception e) {
			logger.info("Unexpected exception = " + e);
			appStandings.setAppStatus(AppStatus.ServerError);
		}
		return appStandings;
	}

	public AppStandings rankStandings(List<Game> games) {
		if (games.size() > 0) {
			return rankStandings(DateTimeUtil.getStringDate(games.get(0).getGameDateTime()));
		}
		else {
			return null;
		}
	}
}