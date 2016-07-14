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

import com.rossotti.basketball.app.business.StandingsBusiness;
import com.rossotti.basketball.dao.model.AppStandings;
import com.rossotti.basketball.dao.model.Standing;
import com.rossotti.basketball.dao.pub.PubStanding;
import com.rossotti.basketball.dao.pub.PubStandings;

@Service
@Path("/rank")
public class RankResource {
	@Autowired
	private StandingsBusiness standingsBusiness;

	@POST
	@Path("/{asOfDate}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response rankStandings(@Context UriInfo uriInfo, 
									@PathParam("asOfDate") String asOfDateString) {

			AppStandings appStandings = standingsBusiness.rankStandings(asOfDateString);

			if (appStandings.isAppCompleted()) {
				List<PubStanding> listPubStandings = new ArrayList<PubStanding>();
				for (Standing standing : appStandings.getStandings()) {
					PubStanding pubStanding = standing.toPubStanding(uriInfo);
					listPubStandings.add(pubStanding);
				}
				PubStandings pubStandings = new PubStandings(uriInfo.getAbsolutePath(), listPubStandings);
				return Response.ok(pubStandings)
						.link(uriInfo.getAbsolutePath(), "standings")
						.build();
			}
			else if (appStandings.isAppClientError()) {
				return Response.status(400).build();
			}
			else if (appStandings.isAppServerError()) {
				return Response.status(500).build();
			}
			else {
				return Response.status(500).build();
			}
	}
}