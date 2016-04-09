package com.rossotti.basketball.app.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.app.service.PlayerServiceBean;
import com.rossotti.basketball.app.service.PropertyServiceBean;
import com.rossotti.basketball.app.service.RosterPlayerServiceBean;
import com.rossotti.basketball.client.FileClientBean;
import com.rossotti.basketball.client.RestClientBean;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.dao.model.Player;
import com.rossotti.basketball.dao.model.RosterPlayer;
import com.rossotti.basketball.dao.pub.PubRosterPlayer_ByTeam;
import com.rossotti.basketball.dao.pub.PubRosterPlayers_ByTeam;
import com.rossotti.basketball.dao.pub.PubTeam;
import com.rossotti.basketball.util.DateTimeUtil;
import com.rossotti.basketball.util.FormatUtil;

@Service
@Path("/score/roster")
public class RosterResource {
	@Autowired
	private RestClientBean restClientBean;

	@Autowired
	private FileClientBean fileClientBean;

	@Autowired
	private RosterPlayerServiceBean rosterPlayerServiceBean;

	@Autowired
	private PlayerServiceBean playerServiceBean;

	@Autowired
	private PropertyServiceBean propertyBean;

	private final Logger logger = LoggerFactory.getLogger(RosterResource.class);

	@POST
	@Path("/{gameDate}/{teamKey}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loadRoster(@Context UriInfo uriInfo, 
								@PathParam("asOfDate") String asOfDateString, 
								@PathParam("teamKey") String teamKey, 
								List<RosterPlayer> rosterPlayers) {

		try {
			LocalDate fromDate = DateTimeUtil.getLocalDate(asOfDateString);
			LocalDate toDate = DateTimeUtil.getLocalDateSeasonMax(fromDate);
			List<RosterPlayer> activeRosterPlayers = null;
			String event = teamKey;

			logger.info('\n' + "Team roster ready to be loaded: " + event);

			RosterDTO rosterDTO = null;
			ClientSource clientSource = propertyBean.getProperty_ClientSource("accumulator.source.roster");
			if (clientSource == ClientSource.File) {
				rosterDTO = fileClientBean.retrieveRoster(event);
			}
			else if (clientSource == ClientSource.Api) {
				rosterDTO = restClientBean.retrieveRoster(event);
			}

			if (rosterDTO.httpStatus == 200) {
				//activate new roster players
				logger.info("Activate new roster players");
				activeRosterPlayers = rosterPlayerServiceBean.getRosterPlayers(rosterDTO.players, fromDate, teamKey);
				RosterPlayer finderRosterPlayer;
				Player finderPlayer;
				for (int i = 0; i < activeRosterPlayers.size(); i++) {
					RosterPlayer activeRosterPlayer = activeRosterPlayers.get(i);
					Player activePlayer = activeRosterPlayer.getPlayer();
					finderRosterPlayer = rosterPlayerServiceBean.findByDatePlayerNameTeam(fromDate, activePlayer.getLastName(), activePlayer.getFirstName(), teamKey);
					if (finderRosterPlayer.isNotFound()) {
						//player is not on current team roster
						finderRosterPlayer = rosterPlayerServiceBean.findLatestByPlayerNameBirthdateSeason(fromDate, activePlayer.getLastName(), activePlayer.getFirstName(), activePlayer.getBirthdate());
						if (finderRosterPlayer.isNotFound()) {
							//player is not on any roster for current season
							finderPlayer = playerServiceBean.findByPlayerNameBirthdate(activePlayer.getLastName(), activePlayer.getFirstName(), activePlayer.getBirthdate());
							if (finderPlayer.isNotFound()) {
								//player does not exist
								Player createPlayer = playerServiceBean.createPlayer(activePlayer);
								activeRosterPlayer.setPlayer(createPlayer);
								activeRosterPlayer.setFromDate(fromDate);
								activeRosterPlayer.setToDate(toDate);
								rosterPlayerServiceBean.createRosterPlayer(activeRosterPlayer);
								logger.info(generateLogMessage("Player does not exist", activeRosterPlayer));
							}
							else {
								//player does exist, not on any roster
								activeRosterPlayer.setPlayer(finderPlayer);
								activeRosterPlayer.setFromDate(fromDate);
								activeRosterPlayer.setToDate(toDate);
								rosterPlayerServiceBean.createRosterPlayer(activeRosterPlayer);
								logger.info(generateLogMessage("Player does exist, not on any roster", activeRosterPlayer));
							}
						}
						else {
							//player is on another roster for current season
							finderRosterPlayer.setToDate(DateTimeUtil.getDateMinusOneDay(fromDate));
							rosterPlayerServiceBean.updateRosterPlayer(finderRosterPlayer);
							logger.info(generateLogMessage("Player on another team - Terminate", finderRosterPlayer));

							activeRosterPlayer.setFromDate(fromDate);
							activeRosterPlayer.setToDate(toDate);
							rosterPlayerServiceBean.createRosterPlayer(activeRosterPlayer);
							logger.info(generateLogMessage("Player on another team - Add", activeRosterPlayer));
						}
					}
					else {
						//player is on current team roster
						logger.info(generateLogMessage("Player on current team roster", activeRosterPlayer));
					}
				}
				//deactivate terminated roster players
				logger.info("Deactivate terminated roster players");
				
				List<RosterPlayer> priorRosterPlayers = rosterPlayerServiceBean.findRosterPlayers(fromDate, teamKey);
				boolean foundPlayerOnRoster;
				for (int i = 0; i < priorRosterPlayers.size(); i++) {
					RosterPlayer priorRosterPlayer = priorRosterPlayers.get(i);
					Player priorPlayer = priorRosterPlayer.getPlayer();
					foundPlayerOnRoster = false;
					for (int j = 0; j < activeRosterPlayers.size(); j++) {
						RosterPlayer activeRosterPlayer = activeRosterPlayers.get(j);
						Player activePlayer = activeRosterPlayer.getPlayer();
						if (priorPlayer.equals(activePlayer)) {
							//player is on current team roster
							logger.info(generateLogMessage("Player on current team roster", priorRosterPlayer));
							foundPlayerOnRoster = true;
							break;
						}
					}
					if (!foundPlayerOnRoster) {
						//player is not on current team roster
						priorRosterPlayer.setToDate(DateTimeUtil.getDateMinusOneDay(fromDate));
						rosterPlayerServiceBean.updateRosterPlayer(priorRosterPlayer);
						logger.info(generateLogMessage("Player is not on current team roster", priorRosterPlayer));
					}
				}
			}

			PubTeam pubTeam = activeRosterPlayers.get(0).getTeam().toPubTeam(uriInfo);
			List<PubRosterPlayer_ByTeam> listPubRosterPlayers = new ArrayList<PubRosterPlayer_ByTeam>();
			for (RosterPlayer rosterPlayer : activeRosterPlayers) {
				PubRosterPlayer_ByTeam pubRosterPlayer = rosterPlayer.toPubRosterPlayer_ByTeam(uriInfo);
				listPubRosterPlayers.add(pubRosterPlayer);
			}
			PubRosterPlayers_ByTeam pubRosterPlayers = new PubRosterPlayers_ByTeam(uriInfo.getAbsolutePath(), pubTeam, listPubRosterPlayers);
			return Response.ok(pubRosterPlayers)
					.link(uriInfo.getAbsolutePath(), "rosterPlayer")
					.build();
		}
		catch (Exception e) {
			System.out.println("exception = " + e);
			return null;
		}
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