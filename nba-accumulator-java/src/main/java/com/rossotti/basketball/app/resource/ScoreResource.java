package com.rossotti.basketball.app.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.app.business.GameBusiness;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.pub.PubGame;

@Service
@Path("/score")
public class ScoreResource {
	@Autowired
	private GameBusiness gameBusiness;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response scoreGame(@Context UriInfo uriInfo, Game game) {
		Game resultGame = gameBusiness.scoreGame(game);
		if (resultGame.isAppCompleted()) {
			PubGame pubGame = game.toPubGame(uriInfo, game.getBoxScoreHome().getTeam().getTeamKey());
			return Response.ok(pubGame)
				.link(uriInfo.getAbsolutePath(), "game")
				.build();
		}
		else if(resultGame.isAppClientError()) {
			return Response.status(400).build();
		}
		else if(resultGame.isAppServerError()) {
			return Response.status(500).build();
		}
		else {
			return Response.status(500).build();
		}
	}
}