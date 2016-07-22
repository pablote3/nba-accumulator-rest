package com.rossotti.basketball.app.business;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.app.exception.PropertyException;
import com.rossotti.basketball.app.resource.ClientSource;
import com.rossotti.basketball.app.service.PropertyService;
import com.rossotti.basketball.app.service.StandingsService;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.client.service.FileClientService;
import com.rossotti.basketball.client.service.RestStatsService;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.AppStandings;
import com.rossotti.basketball.dao.model.AppStatus;
import com.rossotti.basketball.dao.model.Standing;
import com.rossotti.basketball.dao.model.StandingRecord;
import com.rossotti.basketball.dao.model.Team;
import com.rossotti.basketball.util.DateTimeUtil;

@Service
public class StandingsBusiness {
	@Autowired
	private PropertyService propertyService;

	@Autowired
	private RestStatsService restStatsService;

	@Autowired
	private FileClientService fileClientService;

	@Autowired
	private StandingsService standingsService;

	private final Logger logger = LoggerFactory.getLogger(StandingsBusiness.class);

	public AppStandings rankStandings(String asOfDateString) {
		AppStandings appStandings = new AppStandings();
		try {
			StandingsDTO standingsDTO = null;
			ClientSource clientSource = propertyService.getProperty_ClientSource("accumulator.source.standings");
			LocalDate asOfDate = DateTimeUtil.getLocalDate(asOfDateString);
			String nakedAsOfDate = DateTimeUtil.getStringDateNaked(asOfDate);
			if (clientSource == ClientSource.File) {
				standingsDTO = fileClientService.retrieveStandings(nakedAsOfDate);
			}
			else if (clientSource == ClientSource.Api) {
				standingsDTO = (StandingsDTO)restStatsService.retrieveStandings(nakedAsOfDate);
			}
			else {
				throw new PropertyException("Unknown");
			}

			if (standingsDTO.isFound()) {
				if (standingsDTO.standing.length > 0) {
					logger.info("Rank standings");

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
//							logger.error("    Opponent Games Won/Played = " + standingRecord.getGamesWon() + "-" + standingRecord.getGamesPlayed());
//							logger.error("    OpptOppt Games Won/Played = " + standingRecord.getOpptGamesWon() + "-" + standingRecord.getOpptGamesPlayed());
//							logger.error("    Opponent Record = " + opponentRecord);
//							logger.error("    OpptOppt Record = " + opponentOpponentRecord);
							logger.info("  Strenghth Of Schedule  " + teamKey + " " + opponentRecord.multiply(new BigDecimal(2)).add(opponentOpponentRecord).divide(new BigDecimal(3), 4, RoundingMode.HALF_UP));
						}
						else {
							logger.info("Unable to create standing");
							throw new Exception("Unknown");
						}
					}
					
					logger.info("Standings Ranked " + asOfDateString);
					appStandings.setStandings(standingsService.findStandings(asOfDate));
					appStandings.setAppStatus(AppStatus.Completed);
				}
				else {
					logger.info('\n' + "" + " client exception - standings found with empty list");
					appStandings.setAppStatus(AppStatus.ClientError);
				}
			}
			else if (standingsDTO.isNotFound()) {
				logger.info('\n' + "" + " unable to find standings");
				appStandings.setAppStatus(AppStatus.ClientError);
			}
			else {
				logger.info('\n' + "" + " client error retrieving standings");
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
			logger.info("property exception = " + pe);
			appStandings.setAppStatus(AppStatus.ServerError);
		}
		catch (Exception e) {
			logger.info("unexpected exception = " + e);
			appStandings.setAppStatus(AppStatus.ServerError);
		}
		return appStandings;
	}
}