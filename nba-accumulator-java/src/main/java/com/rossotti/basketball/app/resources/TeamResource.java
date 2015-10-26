package com.rossotti.basketball.app.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.rossotti.basketball.models.Team;

@Path("/hello")
public class TeamResource {

	@GET
	@Path("/html/{name}/{age}")
	@Produces(MediaType.TEXT_PLAIN)
	public String produceText_PathParam(@PathParam("name") String name, @PathParam("age") int age) {
		return "Howdy " + name + ", welcome to " + age;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
    public Response createTeam(Team team) {

		String result = "Team created : " + team.toString();
//		Team team = team.createChirp(content);

		return Response.ok(result).build();
	}
	
}
