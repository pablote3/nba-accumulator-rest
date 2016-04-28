package com.rossotti.basketball.dao.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.hibernate.PropertyValueException;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.dao.exception.DuplicateEntityException;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.BoxScore;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.Official;
import com.rossotti.basketball.dao.model.Player;
import com.rossotti.basketball.dao.model.RosterPlayer;
import com.rossotti.basketball.dao.model.Team;
import com.rossotti.basketball.dao.pub.PubGame;
import com.rossotti.basketball.dao.pub.PubGames;
import com.rossotti.basketball.dao.repository.GameRepository;
import com.rossotti.basketball.dao.repository.OfficialRepository;
import com.rossotti.basketball.dao.repository.RosterPlayerRepository;
import com.rossotti.basketball.dao.repository.TeamRepository;
import com.rossotti.basketball.util.DateTimeUtil;

@Service
@Path("/games")
public class GameResource {

	@Autowired
	private GameRepository gameRepo;

	@Autowired
	private TeamRepository teamRepo;

	@Autowired
	private OfficialRepository officialRepo;

	@Autowired
	private RosterPlayerRepository rosterPlayerRepo;

	@GET
	@Path("/{gameDate}/{teamKey}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findGameByTeamGameDate(@Context UriInfo uriInfo, 
											@PathParam("gameDate") String gameDateString, 
											@PathParam("teamKey") String teamKey) {
		try {
			LocalDate gameDate = DateTimeUtil.getLocalDate(gameDateString);
			Game game = gameRepo.findByDateTeam(gameDate, teamKey);
			if (game.isFound()) {
				PubGame pubGame = game.toPubGame(uriInfo, teamKey);
				return Response.ok(pubGame)
					.link(uriInfo.getAbsolutePath(), "game")
					.build();
			}
			else if (game.isNotFound()) {
				throw new NoSuchEntityException(Game.class);
			}
			else {
				return Response.status(500).build();
			}
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("gameDate must be yyyy-MM-dd format", e);
		}
	}

	@GET
	@Path("/{gameDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findGamesByGameDate(@Context UriInfo uriInfo, 
										@PathParam("gameDate") String gameDateString) {
		try {
			LocalDate gameDate = DateTimeUtil.getLocalDate(gameDateString);
			List<Game> listGames = gameRepo.findByDate(gameDate);
			if (listGames.size() > 0) {
				List<PubGame> listPubGames = new ArrayList<PubGame>();
				for (Game game : listGames) {
					PubGame pubGame = game.toPubGame(uriInfo, game.getBoxScores().get(0).getTeam().getTeamKey());
					listPubGames.add(pubGame);
				}
				PubGames pubGames = new PubGames(uriInfo.getAbsolutePath(), listPubGames);
				return Response.ok(pubGames)
						.link(uriInfo.getAbsolutePath(), "game")
						.build();
			}
			else {
				return Response.status(404).build();
			}
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("asOfDate must be yyyy-MM-dd format", e);
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createGame(@Context UriInfo uriInfo, Game createGame) {
		try {
			Team homeTeam = teamRepo.findTeam(createGame.getBoxScoreHome().getTeam().getTeamKey(), DateTimeUtil.getLocalDate(createGame.getGameDateTime()));
			Team awayTeam = teamRepo.findTeam(createGame.getBoxScoreAway().getTeam().getTeamKey(), DateTimeUtil.getLocalDate(createGame.getGameDateTime()));
			if (homeTeam.isFound() && awayTeam.isFound()) {
				createGame.getBoxScoreHome().getTeam().setId(homeTeam.getId());
				createGame.getBoxScoreAway().getTeam().setId(awayTeam.getId());
				Game game = gameRepo.createGame(createGame);
				if (game.isCreated()) {
					return Response.created(uriInfo.getAbsolutePath()).build();
				}
				else if (game.isFound()) {
					throw new DuplicateEntityException(Game.class);
				}
				else {
					return Response.status(500).build();
				}
			}
			else if (homeTeam.isNotFound() || awayTeam.isNotFound()) {
				throw new NoSuchEntityException(Team.class);
			}
			else {
				return Response.status(500).build();
			}
		} catch (PropertyValueException e) {
			throw new BadRequestException("missing required field(s)", e);
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateGame(Game updateGame) {
		try {
			LocalDate gameDate = DateTimeUtil.getLocalDate(updateGame.getGameDateTime());
			Team homeTeam = teamRepo.findTeam(updateGame.getBoxScoreHome().getTeam().getTeamKey(), gameDate);
			Team awayTeam = teamRepo.findTeam(updateGame.getBoxScoreAway().getTeam().getTeamKey(), gameDate);
			if (homeTeam.isFound() && awayTeam.isFound()) {
				updateGame.getBoxScoreHome().getTeam().setId(homeTeam.getId());
				updateGame.getBoxScoreAway().getTeam().setId(awayTeam.getId());
				for (int i = 0; i < updateGame.getGameOfficials().size(); i++) {
					Official updateOfficial = updateGame.getGameOfficials().get(i).getOfficial();
					Official findOfficial = officialRepo.findOfficial(updateOfficial.getLastName(), updateOfficial.getFirstName(), gameDate);
					if (findOfficial.isFound()) {
						updateOfficial.setId(findOfficial.getId());
					}
					else if (findOfficial.isNotFound()) {
						throw new NoSuchEntityException(Official.class);
					}
					else {
						return Response.status(500).build();
					}
				}
				BoxScore homeBoxScore = updateGame.getBoxScoreHome();
				for (int i = 0; i < homeBoxScore.getBoxScorePlayers().size(); i++) {
					RosterPlayer updateRosterPlayer = homeBoxScore.getBoxScorePlayers().get(i).getRosterPlayer();
					Player updatePlayer = updateRosterPlayer.getPlayer();
					RosterPlayer findRosterPlayer = rosterPlayerRepo.findRosterPlayer(updatePlayer.getLastName(), updatePlayer.getFirstName(), updatePlayer.getBirthdate(), gameDate);
					if (findRosterPlayer.isFound()) {
						updatePlayer.setId(findRosterPlayer.getPlayer().getId());
						updateRosterPlayer.setId(findRosterPlayer.getId());
					}
					else if (findRosterPlayer.isNotFound()) {
						throw new NoSuchEntityException(RosterPlayer.class);
					}
					else {
						return Response.status(500).build();
					}
					LocalDateTime homeTeamPreviousGame = gameRepo.findPreviousGameDateTimeByDateTeam(gameDate, homeTeam.getTeamKey());
					if (homeTeamPreviousGame != null) {
						homeBoxScore.setDaysOff((short) DateTimeUtil.getDaysBetweenTwoDateTimes(homeTeamPreviousGame, updateGame.getGameDateTime()));
					}
					else {
						homeBoxScore.setDaysOff((short)0);
					}
				}
				BoxScore awayBoxScore = updateGame.getBoxScoreAway();
				for (int i = 0; i < awayBoxScore.getBoxScorePlayers().size(); i++) {
					RosterPlayer updateRosterPlayer = awayBoxScore.getBoxScorePlayers().get(i).getRosterPlayer();
					Player updatePlayer = updateRosterPlayer.getPlayer();
					RosterPlayer findRosterPlayer = rosterPlayerRepo.findRosterPlayer(updatePlayer.getLastName(), updatePlayer.getFirstName(), updatePlayer.getBirthdate(), gameDate);
					if (findRosterPlayer.isFound()) {
						updatePlayer.setId(findRosterPlayer.getPlayer().getId());
						updateRosterPlayer.setId(findRosterPlayer.getId());
					}
					else if (findRosterPlayer.isNotFound()) {
						throw new NoSuchEntityException(RosterPlayer.class);
					}
					else {
						return Response.status(500).build();
					}
					LocalDateTime awayTeamPreviousGame = gameRepo.findPreviousGameDateTimeByDateTeam(gameDate, awayTeam.getTeamKey());
					if (awayTeamPreviousGame != null) {
						awayBoxScore.setDaysOff((short) DateTimeUtil.getDaysBetweenTwoDateTimes(awayTeamPreviousGame, updateGame.getGameDateTime()));
					}
					else {
						awayBoxScore.setDaysOff((short)0);
					}
				}
				Game game = gameRepo.updateGame(updateGame);
				if (game.isUpdated()) {
					return Response.noContent().build();
				}
				else if (game.isNotFound()) {
					throw new NoSuchEntityException(Game.class);
				}
				else {
					return Response.status(500).build();
				}
			}
			else if (homeTeam.isNotFound() || awayTeam.isNotFound()) {
				throw new NoSuchEntityException(Team.class);
			}
			else {
				return Response.status(500).build();
			}
		} catch (PropertyValueException e) {
			throw new BadRequestException("missing required field(s)", e);
		}
	}

	@DELETE
	@Path("/{gameDate}/{teamKey}")
	public Response deleteGame(@Context UriInfo uriInfo, 
								@PathParam("gameDate") String gameDateString, 
								@PathParam("teamKey") String teamKey) {
		try {
			LocalDate gameDate = DateTimeUtil.getLocalDate(gameDateString);
			Game game = gameRepo.deleteGame(gameDate, teamKey);
			if (game.isDeleted()) {
				return Response.noContent().build();
			}
			else if (game.isNotFound()){
				throw new NoSuchEntityException(Game.class);
			}
			else {
				return Response.status(500).build();
			}
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("gameDate must be yyyy-MM-dd format", e);
		}
	}
}
