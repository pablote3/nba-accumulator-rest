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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.dao.GameDAO;
import com.rossotti.basketball.dao.exception.DuplicateEntityException;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.pub.PubGame;
import com.rossotti.basketball.dao.pub.PubGames;

import com.rossotti.basketball.util.DateTimeUtil;

@Service
@Path("/games")
public class GameResource {

	@Autowired
	private GameDAO gameDAO;

	@GET
	@Path("/{gameDate}/{teamKey}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findGameByTeamGameDate(@Context UriInfo uriInfo, 
											@PathParam("gameDate") String gameDateString, 
											@PathParam("teamKey") String teamKey) {
		try {
			LocalDate gameDate = DateTimeUtil.getLocalDate(gameDateString);
			Game game = gameDAO.findByDateTeam(gameDate, teamKey);
			if (game.isFound()) {
				PubGame pubGame = game.toPubGame(uriInfo, teamKey);
				
				return Response.ok(pubGame)
					.link(uriInfo.getAbsolutePath(), "game")
					.build();
			}
			else if (game.isNotFound()) {
				return Response.status(404).build();
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
		LocalDate gameDate = DateTimeUtil.getLocalDate(gameDateString);
		List<Game> listGames = gameDAO.findByDate(gameDate);
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
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createGame(@Context UriInfo uriInfo, Game createGame) {
		try {
			Game game = gameDAO.createGame(createGame);
			if (game.isCreated()) {
				return Response.created(uriInfo.getAbsolutePath()).build();
			}
			else {
				return Response.status(500).build();
			}
		} catch (DuplicateEntityException e) {
			throw new BadRequestException("game " + createGame.getGameDateTime() + " " + createGame.getBoxScores().get(0).getTeam().getTeamKey() + " already exists", e);
		} catch (PropertyValueException e) {
			throw new BadRequestException("missing required field(s)", e);
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateGame(Game updateGame) {
		try {
			Game game = gameDAO.updateGame(updateGame);
			if (game.isUpdated()) {
				return Response.noContent().build();
			}
			else if (game.isNotFound()) {
				return Response.status(404).build();
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
			Game game = gameDAO.deleteGame(gameDate, teamKey);
			if (game.isDeleted()) {
				return Response.noContent().build();
			}
			else if (game.isNotFound()){
				return Response.status(404).build();
			}
			else {
				return Response.status(500).build();
			}
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("gameDate must be yyyy-MM-dd format", e);
		}
	}
}
