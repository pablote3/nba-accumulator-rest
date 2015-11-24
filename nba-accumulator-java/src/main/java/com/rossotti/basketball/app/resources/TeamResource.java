package com.rossotti.basketball.app.resources;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.hibernate.PropertyValueException;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.dao.TeamDAO;
import com.rossotti.basketball.dao.exceptions.DuplicateEntityException;
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
		Team team = teamDAO.findTeamByKeyAsOfDate(key, new LocalDate());
		PubTeam pubTeam = team.toPubTeam(uriInfo, key, null);
		return Response.ok(pubTeam)
			.link(uriInfo.getAbsolutePath(), "team")
			.build();
	}

	@GET
	@Path("/{key}/{asOfDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findTeamByKey(@Context UriInfo uriInfo, @PathParam("key") String key, @PathParam("asOfDate") String asOfDateString) {
		try {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			LocalDate asOfDate = formatter.parseLocalDate(asOfDateString);
			Team team = teamDAO.findTeamByKeyAsOfDate(key, asOfDate);
			PubTeam pubTeam = team.toPubTeam(uriInfo, key, asOfDateString);
			return Response.ok(pubTeam)
				.link(uriInfo.getAbsolutePath(), "team")
				.build();
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("asOfDate must be yyyy-MM-dd format", e);
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createTeam(@Context UriInfo uriInfo, Team team) {
		try {
			teamDAO.createTeam(team);
			PubTeam pubTeam = team.toPubTeam(uriInfo, team.getKey(), null);
			return Response.ok(pubTeam)
				.link(uriInfo.getAbsolutePath(), "team")
				.build();
		} catch (DuplicateEntityException e) {
			throw new BadRequestException("team " + team.getKey() + " already exists", e);
		} catch (PropertyValueException e) {
			throw new BadRequestException("missing required field(s)", e);
		}
	}

//	@PUT
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response updateTeam(Team team) {
//		teamDAO.updateTeam(team);
//		String result = "Team updated : " + team.toString();
//		return Response.ok(result).build();
//	}
}
