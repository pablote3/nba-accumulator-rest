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
import com.rossotti.basketball.dao.exceptions.NoSuchEntityException;
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
	public Response findTeamByKey(@Context UriInfo uriInfo, 
									@PathParam("key") String key) {
		try {
			Team team = teamDAO.findTeam(key, new LocalDate(), new LocalDate());
			PubTeam pubTeam = team.toPubTeam(uriInfo, key, null, null);
			return Response.ok(pubTeam)
				.link(uriInfo.getAbsolutePath(), "team")
				.build();
		} catch (NoSuchEntityException e) {
			return Response.status(404).build();
		}
	}

	@GET
	@Path("/{key}/{fromDate}/{toDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findTeamByKey(@Context UriInfo uriInfo, 
									@PathParam("key") String key, 
									@PathParam("fromDate") String fromDateString, 
									@PathParam("toDate") String toDateString) {
		try {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			LocalDate fromDate = formatter.parseLocalDate(fromDateString);
			LocalDate toDate = formatter.parseLocalDate(toDateString);
			Team team = teamDAO.findTeam(key, fromDate, toDate);
			PubTeam pubTeam = team.toPubTeam(uriInfo, key, fromDateString, toDateString);
			return Response.ok(pubTeam)
				.link(uriInfo.getAbsolutePath(), "team")
				.build();
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("asOfDate must be yyyy-MM-dd format", e);
		} catch (NoSuchEntityException e) {
			return Response.status(404).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createTeam(@Context UriInfo uriInfo, Team team) {
		try {
			teamDAO.createTeam(team);
			return Response.created(uriInfo.getAbsolutePath()).build();
		} catch (DuplicateEntityException e) {
			throw new BadRequestException("team " + team.getKey() + " already exists", e);
		} catch (PropertyValueException e) {
			throw new BadRequestException("missing required field(s)", e);
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateTeam(Team team) {
		try {
			teamDAO.updateTeam(team);
			return Response.noContent().build();
		} catch (PropertyValueException e) {
			throw new BadRequestException("missing required field(s)", e);
		}
	}
}
