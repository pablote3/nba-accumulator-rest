package com.rossotti.basketball.app.business;

import java.util.List;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.app.exception.PropertyException;
import com.rossotti.basketball.app.resource.ClientSource;
import com.rossotti.basketball.app.service.PlayerService;
import com.rossotti.basketball.app.service.PropertyService;
import com.rossotti.basketball.app.service.RosterPlayerService;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.service.FileClientService;
import com.rossotti.basketball.client.service.RestClientService;
import com.rossotti.basketball.dao.model.AppRoster;
import com.rossotti.basketball.dao.model.AppStatus;
import com.rossotti.basketball.dao.model.Player;
import com.rossotti.basketball.dao.model.RosterPlayer;
import com.rossotti.basketball.util.DateTimeUtil;
import com.rossotti.basketball.util.FormatUtil;

@Service
public class RosterPlayerBusiness {
	@Autowired
	private RestClientService restClientService;

	@Autowired
	private FileClientService fileClientService;

	@Autowired
	private RosterPlayerService rosterPlayerService;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private PropertyService propertyService;

	private final Logger logger = LoggerFactory.getLogger(RosterPlayerBusiness.class);

	public AppRoster loadRoster(String asOfDateString, String teamKey) {
		AppRoster appRoster = new AppRoster();
		RosterDTO rosterDTO = null;
		ClientSource clientSource = propertyService.getProperty_ClientSource("accumulator.source.roster");
		if (clientSource == ClientSource.File) {
			rosterDTO = fileClientService.retrieveRoster(teamKey);
		}
		else if (clientSource == ClientSource.Api) {
			rosterDTO = restClientService.retrieveRoster(teamKey);
		}
		else {
			throw new PropertyException("Unknown");
		}

		if (rosterDTO.isFound()) {
			LocalDate fromDate = DateTimeUtil.getLocalDate(asOfDateString);
			LocalDate toDate = DateTimeUtil.getLocalDateSeasonMax(fromDate);
			//activate new roster players
			logger.info("Activate new roster players");
			List<RosterPlayer> activeRosterPlayers = rosterPlayerService.getRosterPlayers(rosterDTO.players, fromDate, teamKey);
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
					logger.info(generateLogMessage("Player on current team roster", activeRosterPlayer));
				}
			}

			//deactivate terminated roster players
			logger.info("Deactivate terminated roster players");
			List<RosterPlayer> priorRosterPlayers = rosterPlayerService.findRosterPlayers(fromDate, teamKey);
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
						logger.info(generateLogMessage("Player on current team roster", priorRosterPlayer));
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
			appRoster.setRosterPlayers(rosterPlayerService.getRosterPlayers(rosterDTO.players, fromDate, teamKey));
		}
		else if (rosterDTO.isNotFound()) {
			logger.info('\n' + "" + " unable to find game");
			appRoster.setAppStatus(AppStatus.ClientError);
		}
		else if (rosterDTO.isClientException()) {
			logger.info('\n' + "" + " client exception");
			appRoster.setAppStatus(AppStatus.ClientError);
		}
		return appRoster;
	}

	private String generateLogMessage(String messageType, RosterPlayer rosterPlayer) {
		StringBuilder sb;
		sb = new StringBuilder();
		sb.append(FormatUtil.padString(messageType, 40));
		sb.append(" name = " + FormatUtil.padString(rosterPlayer.getPlayer().getFirstName() + " " + rosterPlayer.getPlayer().getLastName(), 35));
		sb.append(" dob = " + DateTimeUtil.getStringDate(rosterPlayer.getPlayer().getBirthdate()));
		sb.append(" team = " + rosterPlayer.getTeam().getAbbr());
		sb.append(" fromDate = " + DateTimeUtil.getStringDate(rosterPlayer.getFromDate()));
		sb.append(" toDate = " + DateTimeUtil.getStringDate(rosterPlayer.getToDate()));
		return sb.toString();
	}
}
