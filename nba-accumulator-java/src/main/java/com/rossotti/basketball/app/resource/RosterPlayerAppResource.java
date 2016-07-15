package com.rossotti.basketball.app.resource;

import java.util.ArrayList;
import java.util.List;

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
import com.rossotti.basketball.dao.model.AppRoster;
import com.rossotti.basketball.dao.model.RosterPlayer;
import com.rossotti.basketball.dao.pub.PubRosterPlayer_ByTeam;
import com.rossotti.basketball.dao.pub.PubRosterPlayers_ByTeam;
import com.rossotti.basketball.dao.pub.PubTeam;

@Service
@Path("/roster")
public class RosterPlayerAppResource {
	@Autowired
	private RosterPlayerBusiness rosterPlayerBusiness;

	@POST
	@Path("/{asOfDate}/{teamKey}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loadRoster(@Context UriInfo uriInfo, 
								@PathParam("asOfDate") String asOfDateString, 
								@PathParam("teamKey") String teamKey) {

		AppRoster appRoster = rosterPlayerBusiness.loadRoster(asOfDateString, teamKey);

		if (appRoster.isAppCompleted()) {
			PubTeam pubTeam = appRoster.getRosterPlayers().get(0).getTeam().toPubTeam(uriInfo);
			List<PubRosterPlayer_ByTeam> listPubRosterPlayers = new ArrayList<PubRosterPlayer_ByTeam>();
			for (RosterPlayer rosterPlayer : appRoster.getRosterPlayers()) {
				PubRosterPlayer_ByTeam pubRosterPlayer = rosterPlayer.toPubRosterPlayer_ByTeam(uriInfo);
				listPubRosterPlayers.add(pubRosterPlayer);
			}
			PubRosterPlayers_ByTeam pubRosterPlayers = new PubRosterPlayers_ByTeam(uriInfo.getAbsolutePath(), pubTeam, listPubRosterPlayers);
			return Response.ok(pubRosterPlayers)
					.link(uriInfo.getAbsolutePath(), "rosterPlayer")
					.build();
		}
		else if (appRoster.isAppClientError()) {
			return Response.status(400).build();
		}
		else if (appRoster.isAppServerError()) {
			return Response.status(500).build();
		}
		else {
			return Response.status(500).build();
		}
	}
}