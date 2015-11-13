package com.rossotti.basketball.app.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.dao.TeamDAO;
import com.rossotti.basketball.models.Team;
import com.rossotti.basketball.pub.PubTeam;

@Service
@Path("/teams")
public class TeamResource {

	@Autowired
	private TeamDAO teamDAO;
	
	@GET
	@Path("/{key}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findTeamByKey(@Context UriInfo uriInfo, @PathParam("key") String key) {
		Team team = teamDAO.findTeamByKey(key, new LocalDate());
		PubTeam pubTeam = team.toPubTeam(uriInfo);
		return Response.ok(pubTeam)
			.link(uriInfo.getAbsolutePath(), "team")
			.build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createTeam(Team team) {
		teamDAO.createTeam(team);
		String result = "Team created : " + team.toString();
		return Response.ok(result).build();
	}
	
}
