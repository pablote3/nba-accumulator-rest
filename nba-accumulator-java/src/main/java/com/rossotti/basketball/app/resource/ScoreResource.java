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

import org.springframework.stereotype.Service;

import com.rossotti.basketball.model.Game;
import com.rossotti.basketball.pub.PubGame;

@Service
@Path("/score/games")
public class ScoreResource {

	@POST
	@Path("/{gameDate}/{teamKey}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response scoreGame(@Context UriInfo uriInfo, 
								@PathParam("gameDate") String gameDateString, 
								@PathParam("teamKey") String teamKey, 
								Game game) {

//			if (game.getStatus().equals(Game.Status.Scheduled)) {
//				
//			}
//			else {
//				
//			}
//			
			PubGame pubGame = game.toPubGame(uriInfo, teamKey);
			
			return Response.ok(pubGame)
				.link(uriInfo.getAbsolutePath(), "game")
				.build();

	}
}