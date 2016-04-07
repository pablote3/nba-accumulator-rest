package com.rossotti.basketball.app.resource;

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

import com.rossotti.basketball.app.service.GameServiceBean;
import com.rossotti.basketball.app.service.OfficialServiceBean;
import com.rossotti.basketball.app.service.RosterPlayerServiceBean;
import com.rossotti.basketball.client.FileClientBean;
import com.rossotti.basketball.client.RestClientBean;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.BoxScore;
import com.rossotti.basketball.dao.model.BoxScore.Result;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.GameStatus;
import com.rossotti.basketball.dao.model.Official;
import com.rossotti.basketball.dao.model.RosterPlayer;
import com.rossotti.basketball.dao.pub.PubGame;
import com.rossotti.basketball.util.DateTimeUtil;

@Service
@Path("/score/games")
public class ScoreResource {
	@Autowired
	private RestClientBean restClientBean;

	@Autowired
	private FileClientBean fileClientBean;

	@Autowired
	private GameServiceBean gameServiceBean;

	@Autowired
	private OfficialServiceBean officialServiceBean;

	@Autowired
	private RosterPlayerServiceBean rosterPlayerServiceBean;

	@Autowired
	private PropertyBean propertyBean;

	private final Logger logger = LoggerFactory.getLogger(ScoreResource.class);

	@POST
	@Path("/{gameDate}/{teamKey}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response scoreGame(@Context UriInfo uriInfo, 
								@PathParam("gameDate") String gameDateString, 
								@PathParam("teamKey") String teamKey, 
								Game game) {

		try {
			BoxScore awayBoxScore = game.getBoxScoreAway();
			BoxScore homeBoxScore = game.getBoxScoreHome();
			String awayTeamKey = awayBoxScore.getTeam().getTeamKey();
			String homeTeamKey = homeBoxScore.getTeam().getTeamKey();
			LocalDateTime gameDateTime = game.getGameDateTime();
			LocalDate gameDate = DateTimeUtil.getLocalDate(gameDateTime);

			String event = DateTimeUtil.getStringDateNaked(gameDateTime) + "-" + awayTeamKey + "-at-" + homeTeamKey;

			if (game.getStatus().equals(GameStatus.Scheduled)) {
				logger.info('\n' + "Scheduled game ready to be scored: " + event);

				GameDTO gameDTO = null;
				ClientSource clientSource = propertyBean.getProperty_ClientSource("accumulator.source.boxScore");
				if (clientSource == ClientSource.File) {
					gameDTO = fileClientBean.retrieveBoxScore(event);
				}
				else if (clientSource == ClientSource.Api) {
					gameDTO = restClientBean.retrieveBoxScore(event);
				}

				if (gameDTO.httpStatus == 200) {
					try {
						game.setStatus(GameStatus.Completed);
						awayBoxScore.updateTotals(gameDTO.away_totals);
						homeBoxScore.updateTotals(gameDTO.home_totals);
						awayBoxScore.updatePeriodScores(gameDTO.away_period_scores);
						homeBoxScore.updatePeriodScores(gameDTO.home_period_scores);
						awayBoxScore.setBoxScorePlayers(rosterPlayerServiceBean.getBoxScorePlayers(gameDTO.away_stats, gameDate, awayTeamKey));
						homeBoxScore.setBoxScorePlayers(rosterPlayerServiceBean.getBoxScorePlayers(gameDTO.home_stats, gameDate, homeTeamKey));
						game.setGameOfficials(officialServiceBean.getGameOfficials(gameDTO.officials, gameDate));

						if (gameDTO.away_totals.getPoints() > gameDTO.home_totals.getPoints()) {
							awayBoxScore.setResult(Result.Win);
							homeBoxScore.setResult(Result.Loss);
						}
						else {
							awayBoxScore.setResult(Result.Loss);
							homeBoxScore.setResult(Result.Win);
						}

						awayBoxScore.setDaysOff((short)DateTimeUtil.getDaysBetweenTwoDateTimes(gameServiceBean.findPreviousGameDateTime(gameDate, awayTeamKey), gameDateTime));
						homeBoxScore.setDaysOff((short)DateTimeUtil.getDaysBetweenTwoDateTimes(gameServiceBean.findPreviousGameDateTime(gameDate, homeTeamKey), gameDateTime));

						Game updatedGame = gameServiceBean.updateGame(game);
						if (updatedGame.isUpdated()) {
							logger.info("Game Scored " + awayTeamKey +  " " + awayBoxScore.getPoints() + " " + homeTeamKey +  " " + homeBoxScore.getPoints());
						}
						else {
							logger.info("Unable to update game - " + updatedGame.getStatus());
						}
					} catch (NoSuchEntityException nse) {
						if (nse.getEntityClass().equals(Official.class)) {
							logger.info("Official not found");
						}
						else if (nse.getEntityClass().equals(RosterPlayer.class)) {
							logger.info("Rebuild active roster");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				PubGame pubGame = game.toPubGame(uriInfo, teamKey);
				
				return Response.ok(pubGame)
					.link(uriInfo.getAbsolutePath(), "game")
					.build();
			}
			else {
				logger.info('\n' + "" + game.getStatus() + " game not eligible to be scored: " + event.toString());
				return Response.notModified()
						.link(uriInfo.getAbsolutePath(), "game")
						.build();
			}
		}
		catch (Exception e) {
			System.out.println("exception = " + e);
			return null;
		}
	}
}