package com.rossotti.basketball.app.resource;

import java.util.ArrayList;
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

import com.rossotti.basketball.app.service.PropertyService;
import com.rossotti.basketball.app.service.StandingsService;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.client.service.FileClientService;
import com.rossotti.basketball.client.service.RestClientService;
import com.rossotti.basketball.dao.model.RosterPlayer;
import com.rossotti.basketball.dao.model.Standing;
import com.rossotti.basketball.dao.model.StandingRecord;
import com.rossotti.basketball.dao.pub.PubStanding;
import com.rossotti.basketball.dao.pub.PubStandings;
import com.rossotti.basketball.util.DateTimeUtil;

@Service
@Path("/rank")
public class RankResource {
	@Autowired
	private RestClientService restClientService;

	@Autowired
	private FileClientService fileClientService;

	@Autowired
	private StandingsService standingsService;

	@Autowired
	private PropertyService propertyService;

	private final Logger logger = LoggerFactory.getLogger(RankResource.class);

	@POST
	@Path("/{asOfDate}/{teamKey}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loadStandings(@Context UriInfo uriInfo, 
								@PathParam("asOfDate") String asOfDateString, 
								List<RosterPlayer> rosterPlayers) {

		try {
			logger.info('\n' + "Standings ready to be loaded: " + asOfDateString);

			StandingsDTO standingsDTO = null;
			ClientSource clientSource = propertyService.getProperty_ClientSource("accumulator.source.standings");
			if (clientSource == ClientSource.File) {
				standingsDTO = fileClientService.retrieveStandings(asOfDateString);
			}
			else if (clientSource == ClientSource.Api) {
				standingsDTO = restClientService.retrieveStandings(asOfDateString);
			}

			if (standingsDTO.httpStatus == 200) {
				LocalDate asOfDate = DateTimeUtil.getLocalDate(asOfDateString);

				//clear existing standings
				standingsService.deleteStandings(asOfDate);

				List<Standing> standings = standingsService.getStandings(standingsDTO);
				Map<String, StandingRecord> standingsMap = standingsService.buildStandingsMap(standings, asOfDate);

				for (int i = 0; i < standings.size(); i++) {
					Standing standing = standings.get(i);
					String teamKey = standing.getTeam().getTeamKey();
					Map<String, StandingRecord> headToHeadMap = standingsService.buildHeadToHeadMap(teamKey, asOfDate, standingsMap);
					StandingRecord standingRecord = standingsService.calculateStrengthOfSchedule(teamKey, asOfDate, standingsMap, headToHeadMap);
					standing.setOpptGamesWon(standingRecord.getGamesWon());
					standing.setOpptGamesPlayed(standingRecord.getGamesPlayed());
					standing.setOpptOpptGamesWon(standingRecord.getOpptGamesWon());
					standing.setOpptOpptGamesPlayed(standingRecord.getOpptGamesPlayed());
					standingsService.updateStanding(standing);
				}

				List<PubStanding> listPubStandings = new ArrayList<PubStanding>();
				for (Standing standing : standings) {
					PubStanding pubStanding = standing.toPubStanding(uriInfo);
					listPubStandings.add(pubStanding);
				}
				PubStandings pubStandings = new PubStandings(uriInfo.getAbsolutePath(), listPubStandings);
				return Response.ok(pubStandings)
						.link(uriInfo.getAbsolutePath(), "standings")
						.build();
			}
			else {
				logger.info('\n' + "" + " unable to retrieve standings: HTTP status = " + standingsDTO.httpStatus);
				return Response.serverError()
						.link(uriInfo.getAbsolutePath(), "standings")
						.build();
			}
		}
		catch (Exception e) {
			logger.info("unexpected exception = " + e);
			return null;
		}
	}
}