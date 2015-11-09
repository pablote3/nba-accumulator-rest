package com.rossotti.basketball.app.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.dao.TeamDAO;
import com.rossotti.basketball.models.Team;

@Service
@Path("/hello")
public class TeamResource {

	@Autowired
	private TeamDAO teamDAO;
	
	@GET
	@Path("/html/{name}/{age}")
	@Produces(MediaType.TEXT_PLAIN)
	public String produceText_PathParam(@PathParam("name") String name, @PathParam("age") int age) {
		return "Howdy " + name + ", welcome to " + age;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createTeam(Team team) {
		teamDAO.create(team);
		String result = "Team created : " + team.toString();
		return Response.ok(result).build();
	}
	
}
