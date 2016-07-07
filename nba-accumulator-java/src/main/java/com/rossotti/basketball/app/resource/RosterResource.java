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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.app.business.RosterPlayerBusiness;
import com.rossotti.basketball.dao.model.AppStatus;

@Service
@Path("/roster")
public class RosterResource {
	@Autowired
	private RosterPlayerBusiness rosterPlayerBusiness;

	@POST
	@Path("/{asOfDate}/{teamKey}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loadRoster(@Context UriInfo uriInfo, 
								@PathParam("asOfDate") String asOfDateString, 
								@PathParam("teamKey") String teamKey) {

		try {
			AppStatus appStatus = rosterPlayerBusiness.loadRoster(asOfDateString, teamKey);

			return Response.ok().build();
//				PubTeam pubTeam = activeRosterPlayers.get(0).getTeam().toPubTeam(uriInfo);
//				List<PubRosterPlayer_ByTeam> listPubRosterPlayers = new ArrayList<PubRosterPlayer_ByTeam>();
//				for (RosterPlayer rosterPlayer : activeRosterPlayers) {
//					PubRosterPlayer_ByTeam pubRosterPlayer = rosterPlayer.toPubRosterPlayer_ByTeam(uriInfo);
//					listPubRosterPlayers.add(pubRosterPlayer);
//				}
//				PubRosterPlayers_ByTeam pubRosterPlayers = new PubRosterPlayers_ByTeam(uriInfo.getAbsolutePath(), pubTeam, listPubRosterPlayers);
//				return Response.ok(pubRosterPlayers)
//						.link(uriInfo.getAbsolutePath(), "rosterPlayer")
//						.build();
//			}
//			else if (rosterDTO.isNotFound()) {
//				logger.info('\n' + "" + " unable to find roster");
//				return Response.status(404).build();
//			}
//			else {
//				logger.info('\n' + "" + " client error retrieving roster");
//				return Response.status(500).build();
//			}
		}
		catch (Exception e) {
			return null;
		}
	}
}