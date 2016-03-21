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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.app.exception.PropertyException;
import com.rossotti.basketball.client.RestClientBean;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.GameStatus;
import com.rossotti.basketball.dao.pub.PubGame;
import com.rossotti.basketball.util.DateTimeUtil;

@Service
@Path("/score/games")

//@Configuration
//@PropertySource("service.properties")
public class ScoreResource {
	@Autowired
	private RestClientBean restClientBean;
	
	@Autowired
	private PropertyBean propertyBean;
//	private Environment env;
	
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
			StringBuilder event = new StringBuilder();
			event.append(DateTimeUtil.getStringDateNaked(game.getGameDateTime()) + "-");
			event.append(game.getBoxScores().get(0).getTeam().getTeamKey() + "-at-");
			event.append(game.getBoxScores().get(1).getTeam().getTeamKey());
	
			if (game.getStatus().equals(GameStatus.Scheduled)) {
				logger.info('\n' + "Scheduled game ready to be scored: " + event);

				GameDTO gameDTO = null;
//				GameDTO gameDTO = restClientBean.retrieveBoxScore(event.toString());
				
				ClientSource boxScoreSource = ClientSource.valueOf(propertyBean.getProperty_String("accumulator.source.boxScore"));
				System.out.println(boxScoreSource);
//				if (boxScoreSource == ClientSource.File) {
//					String boxScoreFile = PropertyBean.getPropertyString("xmlstats.fileBoxScore");
//					logger.info("about to load file");
//					
//				}
				
				System.out.println(gameDTO.httpStatus);
				
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
		catch (Exception e) {
			System.out.println("exception = " + e);
			return null;
		}
	}
}