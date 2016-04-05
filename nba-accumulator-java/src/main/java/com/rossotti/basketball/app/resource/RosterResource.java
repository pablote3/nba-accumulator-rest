package com.rossotti.basketball.app.resource;

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
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.client.FileClientBean;
import com.rossotti.basketball.client.RestClientBean;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.RosterPlayerDTO;
import com.rossotti.basketball.dao.exception.DuplicateEntityException;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.BoxScore;
import com.rossotti.basketball.dao.model.BoxScore.Result;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.GameStatus;
import com.rossotti.basketball.dao.model.Official;
import com.rossotti.basketball.dao.model.Player;
import com.rossotti.basketball.dao.model.RosterPlayer;
import com.rossotti.basketball.dao.pub.PubGame;
import com.rossotti.basketball.dao.pub.PubRosterPlayers_ByTeam;
import com.rossotti.basketball.dao.service.GameServiceBean;
import com.rossotti.basketball.dao.service.OfficialServiceBean;
import com.rossotti.basketball.dao.service.PlayerServiceBean;
import com.rossotti.basketball.dao.service.RosterPlayerServiceBean;
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
	private PropertyBean propertyBean;

	private final Logger logger = LoggerFactory.getLogger(RosterResource.class);

	@POST
	@Path("/{gameDate}/{teamKey}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateRoster(@Context UriInfo uriInfo, 
								@PathParam("gameDate") String gameDateString, 
								@PathParam("teamKey") String teamKey, 
								List<RosterPlayer> rosterPlayers) {

		try {
			LocalDate fromDate = DateTimeUtil.getLocalDate(gameDateString);
			LocalDate toDate = DateTimeUtil.getLocalDateSeasonMax(fromDate);
			String event = teamKey;

			logger.info('\n' + "Team roster ready to be updated: " + event);

			RosterDTO rosterDTO = null;
			ClientSource clientSource = propertyBean.getProperty_ClientSource("accumulator.source.roster");
			if (clientSource == ClientSource.File) {
				rosterDTO = fileClientBean.retrieveRoster(event);
			}
			else if (clientSource == ClientSource.Api) {
				rosterDTO = restClientBean.retrieveRoster(event);
			}

			if (rosterDTO.httpStatus == 200) {
				List<RosterPlayer> activeRosterPlayers = rosterPlayerServiceBean.getRosterPlayers(rosterDTO.players, fromDate, teamKey);
				RosterPlayer finderRosterPlayer;
				Player finderPlayer;
				StringBuilder sb;
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
							logger.info(generateLogMessage("Player on another team -  " + finderRosterPlayer.getTeam().getAbbr() + " - Terminate", finderRosterPlayer));

							activeRosterPlayer.setPlayer(finderPlayer);
							activeRosterPlayer.setFromDate(fromDate);
							activeRosterPlayer.setToDate(toDate);
							rosterPlayerServiceBean.createRosterPlayer(activeRosterPlayer);
							logger.info(generateLogMessage("Player on another team -  " + activeRosterPlayer.getTeam().getAbbr() + " - Add", activeRosterPlayer));
						}
					}
					else {
						//player is on current team roster
						logger.info(generateLogMessage("Player on current team roster -  ", activeRosterPlayer));
					}
				}
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
		sb.append(" fromDate = " + DateTimeUtil.getStringDate(rosterPlayer.getFromDate()));
		sb.append(" toDate = " + DateTimeUtil.getStringDate(rosterPlayer.getToDate()));
		return sb.toString();
	}
}