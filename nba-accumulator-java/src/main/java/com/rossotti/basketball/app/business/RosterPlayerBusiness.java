package com.rossotti.basketball.app.business;

import java.util.List;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.app.exception.PropertyException;
import com.rossotti.basketball.app.service.PlayerService;
import com.rossotti.basketball.app.service.PropertyService;
import com.rossotti.basketball.app.service.RosterPlayerService;
import com.rossotti.basketball.client.dto.ClientSource;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.service.FileStatsService;
import com.rossotti.basketball.client.service.RestStatsService;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.AppGame;
import com.rossotti.basketball.dao.model.AppRoster;
import com.rossotti.basketball.dao.model.AppStatus;
import com.rossotti.basketball.dao.model.Player;
import com.rossotti.basketball.dao.model.RosterPlayer;
import com.rossotti.basketball.dao.model.Team;
import com.rossotti.basketball.util.DateTimeUtil;
import com.rossotti.basketball.util.FormatUtil;

@Service
public class RosterPlayerBusiness {
	private final RestStatsService restStatsService;

	private final FileStatsService fileStatsService;

	private final RosterPlayerService rosterPlayerService;

	private final PlayerService playerService;

	private final PropertyService propertyService;

	private final Logger logger = LoggerFactory.getLogger(RosterPlayerBusiness.class);

	@Autowired
	public RosterPlayerBusiness(RosterPlayerService rosterPlayerService, PropertyService propertyService, FileStatsService fileStatsService, RestStatsService restStatsService, PlayerService playerService) {
		this.rosterPlayerService = rosterPlayerService;
		this.propertyService = propertyService;
		this.fileStatsService = fileStatsService;
		this.restStatsService = restStatsService;
		this.playerService = playerService;
	}

	public AppRoster loadRoster(String asOfDateString, String teamKey) {
		AppRoster appRoster = new AppRoster();
		try {
			RosterDTO rosterDTO;
			LocalDate fromDate = DateTimeUtil.getLocalDate(asOfDateString);
			LocalDate toDate = DateTimeUtil.getLocalDateSeasonMax(fromDate);
			ClientSource clientSource = propertyService.getProperty_ClientSource("accumulator.source.roster");
			if (clientSource == ClientSource.File) {
				rosterDTO = fileStatsService.retrieveRoster(teamKey, fromDate);
			}
			else if (clientSource == ClientSource.Api) {
				rosterDTO = restStatsService.retrieveRoster(teamKey, fromDate);
			}
			else {
				throw new PropertyException("Unknown");
			}

			if (rosterDTO.isFound()) {
				if (rosterDTO.players.length > 0) {
					//activate new roster players
					logger.info("Activate new roster players");
					List<RosterPlayer> activeRosterPlayers = rosterPlayerService.getRosterPlayers(rosterDTO.players, fromDate, teamKey);
					if (activeRosterPlayers.size() > 0) {
						for (int i = 0; i < activeRosterPlayers.size(); i++) {
							RosterPlayer activeRosterPlayer = activeRosterPlayers.get(i);
							Player activePlayer = activeRosterPlayer.getPlayer();
							RosterPlayer finderRosterPlayer = rosterPlayerService.findByDatePlayerNameTeam(fromDate, activePlayer.getLastName(), activePlayer.getFirstName(), teamKey);
							if (finderRosterPlayer.isNotFound()) {
								//player is not on current team roster
								finderRosterPlayer = rosterPlayerService.findLatestByPlayerNameBirthdateSeason(fromDate, activePlayer.getLastName(), activePlayer.getFirstName(), activePlayer.getBirthdate());
								if (finderRosterPlayer.isNotFound()) {
									//player is not on any roster for current season
									Player finderPlayer = playerService.findByPlayerNameBirthdate(activePlayer.getLastName(), activePlayer.getFirstName(), activePlayer.getBirthdate());
									if (finderPlayer.isNotFound()) {
										//player does not exist
										Player createPlayer = playerService.createPlayer(activePlayer);
										activeRosterPlayer.setPlayer(createPlayer);
										activeRosterPlayer.setFromDate(fromDate);
										activeRosterPlayer.setToDate(toDate);
										logger.info(generateLogMessage("Player does not exist", activeRosterPlayer));
										rosterPlayerService.createRosterPlayer(activeRosterPlayer);
									}
									else {
										//player does exist, not on any roster
										activeRosterPlayer.setPlayer(finderPlayer);
										activeRosterPlayer.setFromDate(fromDate);
										activeRosterPlayer.setToDate(toDate);
										logger.info(generateLogMessage("Player does exist, not on any roster", activeRosterPlayer));
										rosterPlayerService.createRosterPlayer(activeRosterPlayer);
									}
								}
								else {
									//player is on another roster for current season
									finderRosterPlayer.setToDate(DateTimeUtil.getDateMinusOneDay(fromDate));
									logger.info(generateLogMessage("Player on another team - Terminate", finderRosterPlayer));
									rosterPlayerService.updateRosterPlayer(finderRosterPlayer);
									activeRosterPlayer.setFromDate(fromDate);
									activeRosterPlayer.setToDate(toDate);
									activeRosterPlayer.getPlayer().setId(finderRosterPlayer.getPlayer().getId());
									logger.info(generateLogMessage("Player on another team - Add", activeRosterPlayer));
									rosterPlayerService.createRosterPlayer(activeRosterPlayer);
								}
							}
							else {
								//player is on current team roster
								activeRosterPlayer.setFromDate(finderRosterPlayer.getFromDate());
								activeRosterPlayer.setToDate(finderRosterPlayer.getToDate());
								logger.debug(generateLogMessage("Player on current team roster", activeRosterPlayer));
							}
						}

						//deactivate terminated roster players
						logger.info("Deactivate terminated roster players");
						List<RosterPlayer> priorRosterPlayers = rosterPlayerService.findRosterPlayers(fromDate, teamKey);
						if (priorRosterPlayers.size() > 0) {
							boolean foundPlayerOnRoster;
							for (int i = 0; i < priorRosterPlayers.size(); i++) {
								RosterPlayer priorRosterPlayer = priorRosterPlayers.get(i);
								Player priorPlayer = priorRosterPlayer.getPlayer();
								foundPlayerOnRoster = false;
								for (int j = 0; j < activeRosterPlayers.size(); j++) {
									RosterPlayer activeRosterPlayer = activeRosterPlayers.get(j);
									Player activePlayer = activeRosterPlayer.getPlayer();
									if (priorPlayer.getLastName().equals(activePlayer.getLastName()) &&
											priorPlayer.getFirstName().equals(activePlayer.getFirstName()) &&
											priorPlayer.getBirthdate().equals(activePlayer.getBirthdate())) {
										//player is on current team roster
										logger.debug(generateLogMessage("Player on current team roster", priorRosterPlayer));
										foundPlayerOnRoster = true;
										break;
									}
								}
								if (!foundPlayerOnRoster) {
									//player is not on current team roster
									priorRosterPlayer.setToDate(DateTimeUtil.getDateMinusOneDay(fromDate));
									logger.info(generateLogMessage("Player is not on current team roster", priorRosterPlayer));
									rosterPlayerService.updateRosterPlayer(priorRosterPlayer);
								}
							}
							appRoster.setRosterPlayers(rosterPlayerService.findRosterPlayers(fromDate, teamKey));
							appRoster.setAppStatus(AppStatus.Completed);
						}
						else {
							logger.info("Unable to find roster players on deactivation");
							appRoster.setAppStatus(AppStatus.ServerError);
						}
					}
					else {
						logger.info("Unable to get roster players on activation");
						appRoster.setAppStatus(AppStatus.ServerError);
					}
				}
				else {
					logger.info("Client exception - roster found with empty player list");
					appRoster.setAppStatus(AppStatus.ClientError);
				}
			}
			else if (rosterDTO.isNotFound()) {
				logger.info("Unable to find roster");
				appRoster.setAppStatus(AppStatus.ClientError);
			}
			else if (rosterDTO.isClientException()) {
				logger.info("Client exception");
				appRoster.setAppStatus(AppStatus.ClientError);
			}
		}
		catch (NoSuchEntityException nse) {
			if (nse.getEntityClass().equals(Team.class)) {
				logger.info("Team not found");
			}
			appRoster.setAppStatus(AppStatus.ClientError);
		}
		catch (PropertyException pe) {
			logger.info("Property exception = " + pe);
			appRoster.setAppStatus(AppStatus.ServerError);
		}
		catch (Exception e) {
			logger.info("Unexpected exception = " + e);
			appRoster.setAppStatus(AppStatus.ServerError);
		}
		return appRoster;
	}

	public AppGame loadRoster(AppGame appGame) {
		logger.info("Load Roster for team = " + appGame.getRosterLastTeam() + " gameDate = " + DateTimeUtil.getStringDate(appGame.getGame().getGameDateTime()));
		AppRoster roster = loadRoster(DateTimeUtil.getStringDate(appGame.getGame().getGameDateTime()), appGame.getRosterLastTeam());
		if (roster.isAppClientError()) {
			appGame.setAppStatus(AppStatus.ClientError);
		}
		else if (roster.isAppServerError()) {
			appGame.setAppStatus(AppStatus.ServerError);
		}
		else if (roster.isAppCompleted()) {
			appGame.setAppStatus(AppStatus.RosterComplete);
		}
		return appGame;
	}

	private String generateLogMessage(String messageType, RosterPlayer rosterPlayer) {
		return FormatUtil.padString(messageType, 40) +
				" fromDate = " + DateTimeUtil.getStringDate(rosterPlayer.getFromDate()) +
				" toDate = " + DateTimeUtil.getStringDate(rosterPlayer.getToDate()) +
				" dob = " + DateTimeUtil.getStringDate(rosterPlayer.getPlayer().getBirthdate()) +
				" name = " + FormatUtil.padString(rosterPlayer.getPlayer().getFirstName() + " " + rosterPlayer.getPlayer().getLastName(), 35);
	}
}