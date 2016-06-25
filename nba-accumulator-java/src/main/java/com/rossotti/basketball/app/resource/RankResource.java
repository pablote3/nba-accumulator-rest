package com.rossotti.basketball.app.resource;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
	@Path("/{asOfDate}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response rankStandings(@Context UriInfo uriInfo, 
									@PathParam("asOfDate") String asOfDateString) {

		try {
			logger.info('\n' + "Standings ready to be ranked: " + asOfDateString);

			StandingsDTO standingsDTO = null;
			ClientSource clientSource = propertyService.getProperty_ClientSource("accumulator.source.standings");
			LocalDate asOfDate = DateTimeUtil.getLocalDate(asOfDateString);
			String nakedAsOfDate = DateTimeUtil.getStringDateNaked(asOfDate);

			if (clientSource == ClientSource.File) {
				standingsDTO = fileClientService.retrieveStandings(nakedAsOfDate);
			}
			else if (clientSource == ClientSource.Api) {
				standingsDTO = restClientService.retrieveStandings(nakedAsOfDate);
			}

			if (standingsDTO.isFound()) {
				logger.info("Rank standings");

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
					Standing createdStanding = standingsService.createStanding(standing);
					if (createdStanding.isCreated()) {
						BigDecimal opponentRecord = standingRecord.getGamesPlayed() == 0 ? new BigDecimal(0) : new BigDecimal(standingRecord.getGamesWon()).divide(new BigDecimal(standingRecord.getGamesPlayed()), 4, RoundingMode.HALF_UP);
						BigDecimal opponentOpponentRecord = standingRecord.getOpptGamesPlayed() == 0 ? new BigDecimal(0) : new BigDecimal(standingRecord.getOpptGamesWon()).divide(new BigDecimal(standingRecord.getOpptGamesPlayed()), 4, RoundingMode.HALF_UP);
//						logger.error("    Opponent Games Won/Played = " + standingRecord.getGamesWon() + "-" + standingRecord.getGamesPlayed());
//						logger.error("    OpptOppt Games Won/Played = " + standingRecord.getOpptGamesWon() + "-" + standingRecord.getOpptGamesPlayed());
//						logger.error("    Opponent Record = " + opponentRecord);
//						logger.error("    OpptOppt Record = " + opponentOpponentRecord);
						logger.info("  Strenghth Of Schedule  " + teamKey + " " + opponentRecord.multiply(new BigDecimal(2)).add(opponentOpponentRecord).divide(new BigDecimal(3), 4, RoundingMode.HALF_UP));
					}
					else {
						logger.info("Unable to create standing");
						return Response.status(404).build();
					}
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
				logger.info('\n' + "" + " unable to retrieve standings: HTTP status = " + standingsDTO.getStatusCode());
				return Response.status(404).build();
			}
		}
		catch (Exception e) {
			logger.info("unexpected exception = " + e);
			return null;
		}
	}
}