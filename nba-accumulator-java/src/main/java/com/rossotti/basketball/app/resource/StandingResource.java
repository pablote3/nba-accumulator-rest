package com.rossotti.basketball.app.resource;

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

import com.rossotti.basketball.dao.StandingDAO;
import com.rossotti.basketball.dao.exception.DuplicateEntityException;
import com.rossotti.basketball.model.Standing;
import com.rossotti.basketball.pub.PubStanding;
import com.rossotti.basketball.pub.PubStandings;
import com.rossotti.basketball.util.DateTimeUtil;

@Service
@Path("/standings")
public class StandingResource {

	@Autowired
	private StandingDAO standingDAO;

	@GET
	@Path("/{teamKey}/{asOfDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findStanding(@Context UriInfo uriInfo, 
								@PathParam("teamKey") String teamKey,
								@PathParam("asOfDate") String asOfDateString) {
		try {
			LocalDate asOfDate = DateTimeUtil.getLocalDate(asOfDateString);
			Standing standing = standingDAO.findStanding(teamKey, asOfDate);
			if (standing.isFound()) {
				PubStanding pubStanding = standing.toPubStanding(uriInfo);
				return Response.ok(pubStanding)
					.link(uriInfo.getAbsolutePath(), "standing")
					.build();
			}
			else if (standing.isNotFound()) {
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
	@Path("/{asOfDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findStandings(@Context UriInfo uriInfo, 
								@PathParam("asOfDate") String asOfDateString) {
		LocalDate asOfDate = DateTimeUtil.getLocalDate(asOfDateString);
		List<Standing> listStandings = standingDAO.findStandings(asOfDate);
		if (listStandings.size() > 0) {
			List<PubStanding> listPubStandings = new ArrayList<PubStanding>();
			for (Standing standing : listStandings) {
				PubStanding pubStanding = standing.toPubStanding(uriInfo);
				listPubStandings.add(pubStanding);
			}
			PubStandings pubStandings = new PubStandings(uriInfo.getAbsolutePath(), listPubStandings);
			return Response.ok(pubStandings)
					.link(uriInfo.getAbsolutePath(), "standing")
					.build();
		}
		else {
			return Response.status(404).build();
		}
	}

//	@POST
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response createRosterPlayer(@Context UriInfo uriInfo, RosterPlayer createRosterPlayer) {
//		try {
//			RosterPlayer rosterPlayer = standingDAO.createRosterPlayer(createRosterPlayer);
//			if (rosterPlayer.isCreated()) {
//				return Response.created(uriInfo.getAbsolutePath()).build();
//			}
//			else {
//				return Response.status(500).build();
//			}
//		} catch (DuplicateEntityException e) {
//			throw new BadRequestException("player " + createRosterPlayer.getPlayer().getFirstName() + " " + createRosterPlayer.getPlayer().getLastName() + " already exists", e);
//		} catch (PropertyValueException e) {
//			throw new BadRequestException("missing required field(s)", e);
//		}
//	}
//
//	@PUT
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response updateRosterPlayer(RosterPlayer updateRosterPlayer) {
//		try {
//			RosterPlayer rosterPlayer = standingDAO.updateRosterPlayer(updateRosterPlayer);
//			if (rosterPlayer.isUpdated()) {
//				return Response.noContent().build();
//			}
//			else if (rosterPlayer.isNotFound()) {
//				return Response.status(404).build();
//			}
//			else {
//				return Response.status(500).build();
//			}
//		} catch (PropertyValueException e) {
//			throw new BadRequestException("missing required field(s)", e);
//		}
//	}

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
//			RosterPlayer rosterPlayer = standingDAO.deleteRosterPlayer(lastName, firstName, birthdate, asOfDate);
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
