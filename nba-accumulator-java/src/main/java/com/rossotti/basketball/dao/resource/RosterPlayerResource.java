package com.rossotti.basketball.dao.resource;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.dao.RosterPlayerDAO;
import com.rossotti.basketball.dao.exception.DuplicateEntityException;
import com.rossotti.basketball.model.RosterPlayer;
import com.rossotti.basketball.pub.PubPlayer;
import com.rossotti.basketball.pub.PubRosterPlayer;
import com.rossotti.basketball.pub.PubRosterPlayer_ByPlayer;
import com.rossotti.basketball.pub.PubRosterPlayer_ByTeam;
import com.rossotti.basketball.pub.PubRosterPlayers_ByPlayer;
import com.rossotti.basketball.pub.PubRosterPlayers_ByTeam;
import com.rossotti.basketball.pub.PubTeam;
import com.rossotti.basketball.util.DateTimeUtil;

@Service
@Path("/rosterPlayers")
public class RosterPlayerResource {

	@Autowired
	private RosterPlayerDAO rosterPlayerDAO;

	@GET
	@Path("/player/{lastName}/{firstName}/{birthdate}/{asOfDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findRosterPlayerByPlayer(@Context UriInfo uriInfo, 
											@PathParam("lastName") String lastName,
											@PathParam("firstName") String firstName,
											@PathParam("birthdate") String birthdateString,
											@PathParam("asOfDate") String asOfDateString) {
		try {
			LocalDate birthdate = DateTimeUtil.getLocalDate(birthdateString);
			LocalDate asOfDate = DateTimeUtil.getLocalDate(asOfDateString);
			RosterPlayer rosterPlayer = rosterPlayerDAO.findRosterPlayer(lastName, firstName, birthdate, asOfDate);
			if (rosterPlayer.isFound()) {
				PubRosterPlayer pubRosterPlayer = rosterPlayer.toPubRosterPlayer(uriInfo);
				return Response.ok(pubRosterPlayer)
					.link(uriInfo.getAbsolutePath(), "rosterPlayer")
					.build();
			}
			else if (rosterPlayer.isNotFound()) {
				return Response.status(404).build();
			}
			else {
				return Response.status(500).build();
			}
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("dates must be yyyy-MM-dd format", e);
		}
	}
	
	@GET
	@Path("/team/{lastName}/{firstName}/{teamKey}/{asOfDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findRosterPlayerByTeam(@Context UriInfo uriInfo, 
											@PathParam("lastName") String lastName,
											@PathParam("firstName") String firstName,
											@PathParam("teamKey") String teamKey,
											@PathParam("asOfDate") String asOfDateString) {
		try {
			LocalDate asOfDate = DateTimeUtil.getLocalDate(asOfDateString);
			RosterPlayer rosterPlayer = rosterPlayerDAO.findRosterPlayer(lastName, firstName, teamKey, asOfDate);
			if (rosterPlayer.isFound()) {
				PubRosterPlayer pubRosterPlayer = rosterPlayer.toPubRosterPlayer(uriInfo);
				return Response.ok(pubRosterPlayer)
					.link(uriInfo.getAbsolutePath(), "rosterPlayer")
					.build();
			}
			else if (rosterPlayer.isNotFound()) {
				return Response.status(404).build();
			}
			else {
				return Response.status(500).build();
			}
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("dates must be yyyy-MM-dd format", e);
		}
	}

	@GET
	@Path("/player/{lastName}/{firstName}/{birthdate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findRosterPlayersByPlayer(@Context UriInfo uriInfo, 
											@PathParam("lastName") String lastName, 
											@PathParam("firstName") String firstName,
											@PathParam("birthdate") String birthdateString) {
		LocalDate birthdate = DateTimeUtil.getLocalDate(birthdateString);
		List<RosterPlayer> listRosterPlayers = rosterPlayerDAO.findRosterPlayers(lastName, firstName, birthdate);
		PubPlayer pubPlayer = listRosterPlayers.get(0).getPlayer().toPubPlayer(uriInfo);
		if (listRosterPlayers.size() > 0) {
			List<PubRosterPlayer_ByPlayer> listPubRosterPlayers = new ArrayList<PubRosterPlayer_ByPlayer>();
			for (RosterPlayer rosterPlayer : listRosterPlayers) {
				PubRosterPlayer_ByPlayer pubRosterPlayer = rosterPlayer.toPubRosterPlayer_ByPlayer(uriInfo);
				listPubRosterPlayers.add(pubRosterPlayer);
			}
			PubRosterPlayers_ByPlayer pubRosterPlayers = new PubRosterPlayers_ByPlayer(uriInfo.getAbsolutePath(), pubPlayer, listPubRosterPlayers);
			return Response.ok(pubRosterPlayers)
					.link(uriInfo.getAbsolutePath(), "rosterPlayer")
					.build();
		}
		else {
			return Response.status(404).build();
		}
	}

	@GET
	@Path("/team/{teamKey}/{asOfDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findRosterPlayersByTeamKey(@Context UriInfo uriInfo, 
											@PathParam("teamKey") String teamKey,
											@PathParam("asOfDate") String asOfDateString) {
		LocalDate asOfDate = DateTimeUtil.getLocalDate(asOfDateString);
		List<RosterPlayer> listRosterPlayers = rosterPlayerDAO.findRosterPlayers(teamKey, asOfDate);
		PubTeam pubTeam = listRosterPlayers.get(0).getTeam().toPubTeam(uriInfo);
		if (listRosterPlayers.size() > 0) {
			List<PubRosterPlayer_ByTeam> listPubRosterPlayers = new ArrayList<PubRosterPlayer_ByTeam>();
			for (RosterPlayer rosterPlayer : listRosterPlayers) {
				PubRosterPlayer_ByTeam pubRosterPlayer = rosterPlayer.toPubRosterPlayer_ByTeam(uriInfo);
				listPubRosterPlayers.add(pubRosterPlayer);
			}
			PubRosterPlayers_ByTeam pubRosterTeams = new PubRosterPlayers_ByTeam(uriInfo.getAbsolutePath(), pubTeam, listPubRosterPlayers);
			return Response.ok(pubRosterTeams)
					.link(uriInfo.getAbsolutePath(), "rosterPlayer")
					.build();
		}
		else {
			return Response.status(404).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createRosterPlayer(@Context UriInfo uriInfo, RosterPlayer createRosterPlayer) {
		try {
			RosterPlayer rosterPlayer = rosterPlayerDAO.createRosterPlayer(createRosterPlayer);
			if (rosterPlayer.isCreated()) {
				return Response.created(uriInfo.getAbsolutePath()).build();
			}
			else {
				return Response.status(500).build();
			}
		} catch (DuplicateEntityException e) {
			throw new BadRequestException("player " + createRosterPlayer.getPlayer().getFirstName() + " " + createRosterPlayer.getPlayer().getLastName() + " already exists", e);
		} catch (PropertyValueException e) {
			throw new BadRequestException("missing required field(s)", e);
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateRosterPlayer(RosterPlayer updateRosterPlayer) {
		try {
			RosterPlayer rosterPlayer = rosterPlayerDAO.updateRosterPlayer(updateRosterPlayer);
			if (rosterPlayer.isUpdated()) {
				return Response.noContent().build();
			}
			else if (rosterPlayer.isNotFound()) {
				return Response.status(404).build();
			}
			else {
				return Response.status(500).build();
			}
		} catch (PropertyValueException e) {
			throw new BadRequestException("missing required field(s)", e);
		}
	}

//	cannot delete or update parent row - foreign key constraints fail
//	@DELETE
//	@Path("/{lastName}/{firstName}/{birthdate}/{asOfDate}")
//	public Response deleteRosterPlayer(@Context UriInfo uriInfo, 
//									@PathParam("lastName") String lastName, 
//									@PathParam("firstName") String firstName, 
//									@PathParam("birthdate") String birthdateString, 
//									@PathParam("asOfdate") String asOfDateString) {
//		try {
//			LocalDate birthdate = DateTimeUtil.getLocalDate(birthdateString);
//			LocalDate asOfDate = DateTimeUtil.getLocalDate(asOfDateString);
//			RosterPlayer rosterPlayer = rosterPlayerDAO.deleteRosterPlayer(lastName, firstName, birthdate, asOfDate);
//			if (rosterPlayer.isDeleted()) {
//				return Response.noContent().build();
//			}
//			else if (rosterPlayer.isNotFound()){
//				return Response.status(404).build();
//			}
//			else {
//				return Response.status(500).build();
//			}
//		} catch (IllegalArgumentException e) {
//			throw new BadRequestException("dates must be yyyy-MM-dd format", e);
//		}
//	}
}
