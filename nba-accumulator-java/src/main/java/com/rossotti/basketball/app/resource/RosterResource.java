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
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.BoxScore;
import com.rossotti.basketball.dao.model.BoxScore.Result;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.GameStatus;
import com.rossotti.basketball.dao.model.Official;
import com.rossotti.basketball.dao.model.Player;
import com.rossotti.basketball.dao.model.RosterPlayer;
import com.rossotti.basketball.dao.pub.PubGame;
import com.rossotti.basketball.dao.service.GameServiceBean;
import com.rossotti.basketball.dao.service.OfficialServiceBean;
import com.rossotti.basketball.dao.service.PlayerServiceBean;
import com.rossotti.basketball.dao.service.RosterPlayerServiceBean;
import com.rossotti.basketball.util.DateTimeUtil;

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
								
							}
						}
					}

//					game.setGameOfficials(officialServiceBean.getGameOfficials(rosterDTO.officials, gameDate));
//
//					if (rosterDTO.away_totals.getPoints() > rosterDTO.home_totals.getPoints()) {
//						awayBoxScore.setResult(Result.Win);
//						homeBoxScore.setResult(Result.Loss);
//					}
//					else {
//						awayBoxScore.setResult(Result.Loss);
//						homeBoxScore.setResult(Result.Win);
//					}
//
//					awayBoxScore.setDaysOff((short)DateTimeUtil.getDaysBetweenTwoDateTimes(gameServiceBean.findPreviousGameDateTime(gameDate, awayTeamKey), gameDateTime));
//					homeBoxScore.setDaysOff((short)DateTimeUtil.getDaysBetweenTwoDateTimes(gameServiceBean.findPreviousGameDateTime(gameDate, homeTeamKey), gameDateTime));
//
//					Game updatedGame = gameServiceBean.updateGame(game);
//					if (updatedGame.isUpdated()) {
//						logger.info("Game Scored " + awayTeamKey +  " " + awayBoxScore.getPoints() + " " + homeTeamKey +  " " + homeBoxScore.getPoints());
//					}
//					else {
//						logger.info("Unable to update game - " + updatedGame.getStatus());
//					}
//				} catch (NoSuchEntityException nse) {
//					if (nse.getEntityClass().equals(Official.class)) {
//						logger.info("Official not found");
//					}
//					else if (nse.getEntityClass().equals(RosterPlayer.class)) {
//						logger.info("Rebuild active roster");
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
				}
			}
				
//			PubGame pubGame = game.toPubGame(uriInfo, teamKey);
//				
//			return Response.ok(pubGame)
//				.link(uriInfo.getAbsolutePath(), "game")
//				.build();
		}
		catch (Exception e) {
			System.out.println("exception = " + e);
			return null;
		}
	}
}