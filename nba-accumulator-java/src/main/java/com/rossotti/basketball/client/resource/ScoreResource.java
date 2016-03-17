package com.rossotti.basketball.client.resource;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.client.ClientBean;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.model.Game;
import com.rossotti.basketball.model.GameStatus;
import com.rossotti.basketball.pub.PubGame;
import com.rossotti.basketball.util.DateTimeUtil;

@Service
@Path("/score/games")
public class ScoreResource {
	@Autowired
	private ClientBean clientBean;
	
	private final Logger logger = LoggerFactory.getLogger(ScoreResource.class);
	private static ObjectMapper mapper = JsonProvider.buildObjectMapper();

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
			event.append("https://erikberg.com/nba/boxscore/");
			event.append(DateTimeUtil.getStringDateNaked(game.getGameDateTime()) + "-");
			event.append(game.getBoxScores().get(0).getTeam().getTeamKey() + "-at-");
			event.append(game.getBoxScores().get(1).getTeam().getTeamKey() + ".json");
	
			if (game.getStatus().equals(GameStatus.Scheduled)) {
				logger.info('\n' + "Scheduled game ready to be scored: " + event);

				GameDTO gameDTO = this.retrieveBoxScore(event.toString());
//				Response response = clientBean.getClient().target(event.toString()).request().get();

//				Properties properties = ResourceLoader.getInstance().getProperties();
//				String boxScoreSource = properties.getProperty("accumulator.source.boxScore");
//				System.out.println(boxScoreSource);
				
//				if (StringUtils.isNotBlank(boxScoreSource) && boxScoreSource.equalsIgnoreCase("file")) {
//					String fileBoxScore = ResourceLoader.getInstance().getProperties().getProperty("xmlstats.fileBoxScore");
//					if (StringUtils.isNotBlank(fileBoxScore) && fileBoxScore.length() > 0) {
//						logger.info("about to load file");
//					} else {
//						throw new PropertyException("xmlstats.fileBoxScore");
//					}
//				} else {
//					throw new PropertyException("accumulator.source.boxScore");
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
	
	private GameDTO retrieveBoxScore(String event) {
		GameDTO game = null;

		Response response = clientBean.getClient().target(event).request().get();

		if (response.getStatus() != 200) {
			game = new GameDTO();
			response.readEntity(String.class);
		} else {
			try {
				game = mapper.readValue(response.readEntity(String.class), GameDTO.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		game.httpStatus = response.getStatus();
		response.close();
		return game;
	}
}