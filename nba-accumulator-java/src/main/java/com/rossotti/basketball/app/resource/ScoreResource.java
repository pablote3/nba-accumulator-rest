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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.model.Game;
import com.rossotti.basketball.model.GameStatus;
import com.rossotti.basketball.pub.PubGame;

@Service
@Path("/score/games")
public class ScoreResource {
	private final Logger logger = LoggerFactory.getLogger(ScoreResource.class);

	@POST
	@Path("/{gameDate}/{teamKey}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response scoreGame(@Context UriInfo uriInfo, 
								@PathParam("gameDate") String gameDateString, 
								@PathParam("teamKey") String teamKey, 
								Game game) {

		StringBuilder event = new StringBuilder();
		event.append(gameDateString + "-");
		event.append(game.getBoxScores().get(0).getTeam().getTeamKey() + "-at-");
		event.append(game.getBoxScores().get(1).getTeam().getTeamKey());

		if (game.getStatus().equals(GameStatus.Scheduled)) {
			logger.info('\n' + "Scheduled game ready to be scored: " + event);
		
			PubGame pubGame = game.toPubGame(uriInfo, teamKey);
			
			return Response.ok(pubGame)
				.link(uriInfo.getAbsolutePath(), "game")
				.build();
		}
		else {
			logger.info('\n' + "" + game.getStatus() + " game not eligible to be scored: " + event);
			return Response.notModified()
					.link(uriInfo.getAbsolutePath(), "game")
					.build();
		}
	}
}