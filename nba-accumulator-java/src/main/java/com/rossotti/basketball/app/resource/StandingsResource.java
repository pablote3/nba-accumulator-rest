package com.rossotti.basketball.app.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.app.service.GameServiceBean;
import com.rossotti.basketball.app.service.StandingsServiceBean;
import com.rossotti.basketball.client.FileClientBean;
import com.rossotti.basketball.client.RestClientBean;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.RosterPlayer;
import com.rossotti.basketball.dao.model.Standing;
import com.rossotti.basketball.dao.model.StandingRecord;
import com.rossotti.basketball.dao.pub.PubStanding;
import com.rossotti.basketball.dao.pub.PubStandings;
import com.rossotti.basketball.util.DateTimeUtil;

@Service
@Path("/score/roster")
public class StandingsResource {
	@Autowired
	private RestClientBean restClientBean;

	@Autowired
	private FileClientBean fileClientBean;

	@Autowired
	private StandingsServiceBean standingsServiceBean;

	@Autowired
	private GameServiceBean gameServiceBean;

	@Autowired
	private PropertyBean propertyBean;

	private final Logger logger = LoggerFactory.getLogger(StandingsResource.class);

	@POST
	@Path("/{asOfDate}/{teamKey}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loadStandings(@Context UriInfo uriInfo, 
								@PathParam("asOfDate") String asOfDateString, 
								List<RosterPlayer> rosterPlayers) {

		try {
			LocalDate asOfDate = DateTimeUtil.getLocalDate(asOfDateString);
			String event = asOfDateString;
			List<Standing> activeStandings = null;

			logger.info('\n' + "Standings ready to be loaded: " + event);

			StandingsDTO standingsDTO = null;
			ClientSource clientSource = propertyBean.getProperty_ClientSource("accumulator.source.standings");
			if (clientSource == ClientSource.File) {
				standingsDTO = fileClientBean.retrieveRoster(event);
			}
			else if (clientSource == ClientSource.Api) {
				standingsDTO = restClientBean.retrieveRoster(event);
			}

			if (standingsDTO.httpStatus == 200) {
				//delete standings for asOfDate if they exist
				List<Standing> oldStandings = standingsServiceBean.findStandings(asOfDate);
				if (!oldStandings.isEmpty() && oldStandings.size() > 0) {
					logger.info("Deleting standings for " + asOfDateString);
					for (int i = 0; i < oldStandings.size(); i++) {
						standingsServiceBean.deleteStanding(oldStandings.get(i).getTeam().getTeamKey(), asOfDate);
					}
				}

				activeStandings = standingsServiceBean.getStandings(standingsDTO);

				//build standings map
				Map<String, StandingRecord> standingsMap = new HashMap<String, StandingRecord>();
				StandingRecord standingRecord;
				for (int i = 0; i < activeStandings.size(); i++) {
					standingRecord = new StandingRecord((int)activeStandings.get(i).getGamesWon(), (int)activeStandings.get(i).getGamesPlayed(), 0, 0);
					standingsMap.put(activeStandings.get(i).getTeam().getTeamKey(), standingRecord);
				}

				//create team standing
				Standing createTeamStanding;
				List<Game> completeGames;
				Game completedGame;
				String opptTeamKey;
				Integer opptGamesWon;
				Integer opptGamesPlayed;
				for (int i = 0; i < activeStandings.size(); i++) {
					createTeamStanding = activeStandings.get(i);
					String teamKey = createTeamStanding.getTeam().getTeamKey();
					opptGamesWon = 0;
					opptGamesPlayed = 0;
					completeGames = gameServiceBean.findByDateTeamSeason(asOfDate, teamKey);
					for (int k = 0; k < completeGames.size(); k++) {
						completedGame = completeGames.get(k);
						int opptBoxScoreId = completedGame.getBoxScores().get(0).getTeam().equals(createTeamStanding.getTeam()) ? 1 : 0;
						opptTeamKey = completedGame.getBoxScores().get(opptBoxScoreId).getTeam().getTeamKey();
						opptGamesWon = opptGamesWon + standingsMap.get(opptTeamKey).getGamesWon();
						opptGamesPlayed = opptGamesPlayed + standingsMap.get(opptTeamKey).getGamesPlayed();

						String completedGameDate = DateTimeUtil.getStringDate(completedGame.getGameDateTime());
						logger.debug('\n' + ("  StandingsMap " + teamKey + " " + completedGameDate + " " + opptTeamKey + 
											" Games Won/Played: " + standingsMap.get(opptTeamKey).getGamesWon() + " - " + standingsMap.get(opptTeamKey).getGamesPlayed()));
					}
					standingsMap.get(teamKey).setOpptGamesWon(opptGamesWon);
					standingsMap.get(teamKey).setOpptGamesPlayed(opptGamesPlayed);
					standingsServiceBean.createStanding(createTeamStanding);
				}

				//update team standing
				Standing updateTeamStanding;
				for (int i = 0; i < activeStandings.size(); i++) {
					updateTeamStanding = activeStandings.get(i);
					String standingTeam = updateTeamStanding.getTeam().getTeamKey();
					updateTeamStanding = standingsServiceBean.findStanding(standingTeam, asOfDate);
					standingsServiceBean.calculateStrengthOfSchedule(updateTeamStanding, standingsMap);
				}
			}

			List<PubStanding> listPubStandings = new ArrayList<PubStanding>();
			for (Standing standing : activeStandings) {
				PubStanding pubStanding = standing.toPubStanding(uriInfo);
				listPubStandings.add(pubStanding);
			}
			PubStandings pubStandings = new PubStandings(uriInfo.getAbsolutePath(), listPubStandings);
			return Response.ok(pubStandings)
					.link(uriInfo.getAbsolutePath(), "standings")
					.build();
		}
		catch (Exception e) {
			System.out.println("exception = " + e);
			return null;
		}
	}
}