package com.rossotti.basketball.app.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import com.rossotti.basketball.pub.PubTeams;

@Service
@Path("/teams")
public class TeamResource {

	@Autowired
	private TeamDAO teamDAO;

	@GET
	@Path("/{key}/{fromDate}/{toDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findTeamByKeyDate(@Context UriInfo uriInfo, 
									@PathParam("key") String key, 
									@PathParam("fromDate") String fromDateString, 
									@PathParam("toDate") String toDateString) {
		try {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			LocalDate fromDate = formatter.parseLocalDate(fromDateString);
			LocalDate toDate = formatter.parseLocalDate(toDateString);
			Team team = teamDAO.findTeam(key, fromDate, toDate);
			if (team.isFound()) {
				PubTeam pubTeam = team.toPubTeam(uriInfo);
				return Response.ok(pubTeam)
					.link(uriInfo.getAbsolutePath(), "team")
					.build();
			}
			else if (team.isNotFound()) {
				return Response.status(404).build();
			}
			else {
				return Response.status(500).build();
			}
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("asOfDate must be yyyy-MM-dd format", e);
		}
	}
	
	@GET
	@Path("/{key}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findTeamsByKey(@Context UriInfo uriInfo, 
									@PathParam("key") String key) {
		List<Team> listTeams = teamDAO.findTeams(key);
		if (listTeams.size() > 0) {
			List<PubTeam> listPubTeams = new ArrayList<PubTeam>();
			for (Team team : listTeams) {
				PubTeam pubTeam = team.toPubTeam(uriInfo);
				listPubTeams.add(pubTeam);
			}
			PubTeams pubTeams = new PubTeams(uriInfo.getAbsolutePath(), listPubTeams);
			return Response.ok(pubTeams)
				.link(uriInfo.getAbsolutePath(), "team")
				.build();
		}
		else {
			return Response.status(404).build();
		}
	}

	@GET
	@Path("/{fromDate}/{toDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findTeamsByDate(@Context UriInfo uriInfo, 
									@PathParam("fromDate") String fromDateString, 
									@PathParam("toDate") String toDateString) {
		try {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			LocalDate fromDate = formatter.parseLocalDate(fromDateString);
			LocalDate toDate = formatter.parseLocalDate(toDateString);
			List<Team> listTeams = teamDAO.findTeams(fromDate, toDate);
			if (listTeams.size() > 0) {
				List<PubTeam> listPubTeams = new ArrayList<PubTeam>();
				for (Team team : listTeams) {
					PubTeam pubTeam = team.toPubTeam(uriInfo);
					listPubTeams.add(pubTeam);
				}
				PubTeams pubTeams = new PubTeams(uriInfo.getAbsolutePath(), listPubTeams);
				return Response.ok(pubTeams)
					.link(uriInfo.getAbsolutePath(), "team")
					.build();
			}
			else {
				return Response.status(404).build();
			}
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("asOfDate must be yyyy-MM-dd format", e);
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createTeam(@Context UriInfo uriInfo, Team createTeam) {
		try {
			Team team = teamDAO.createTeam(createTeam);
			if (team.isDeleted()) {
				return Response.created(uriInfo.getAbsolutePath()).build();
			}
			else {
				return Response.status(500).build();
			}
		} catch (DuplicateEntityException e) {
			throw new BadRequestException("team " + createTeam.getTeamKey() + " already exists", e);
		} catch (PropertyValueException e) {
			throw new BadRequestException("missing required field(s)", e);
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateTeam(Team updateTeam) {
		try {
			Team team = teamDAO.updateTeam(updateTeam);
			if (team.isUpdated()) {
				return Response.noContent().build();
			}
			else if (team.isNotFound()) {
				return Response.status(404).build();
			}
			else {
				return Response.status(500).build();
			}
		} catch (PropertyValueException e) {
			throw new BadRequestException("missing required field(s)", e);
		}
	}
	
	@DELETE
	@Path("/{key}/{fromDate}/{toDate}")
	public Response deleteTeam(@Context UriInfo uriInfo, 
								@PathParam("key") String key, 
								@PathParam("fromDate") String fromDateString, 
								@PathParam("toDate") String toDateString) {
		try {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			LocalDate fromDate = formatter.parseLocalDate(fromDateString);
			LocalDate toDate = formatter.parseLocalDate(toDateString);
			Team team = teamDAO.deleteTeam(key, fromDate, toDate);
			if (team.isDeleted()) {
				return Response.noContent().build();
			}
			else if (team.isNotFound()){
				return Response.status(404).build();
			}
			else {
				return Response.status(500).build();
			}
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("asOfDate must be yyyy-MM-dd format", e);
		}
	}
}
