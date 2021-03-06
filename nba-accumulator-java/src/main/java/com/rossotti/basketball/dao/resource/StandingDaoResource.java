package com.rossotti.basketball.dao.resource;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.dao.exception.DuplicateEntityException;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.Standing;
import com.rossotti.basketball.dao.model.Team;
import com.rossotti.basketball.dao.pub.PubStanding;
import com.rossotti.basketball.dao.pub.PubStandings;
import com.rossotti.basketball.dao.repository.StandingRepository;
import com.rossotti.basketball.dao.repository.TeamRepository;
import com.rossotti.basketball.util.DateTimeUtil;

@Service
@Path("/standings")
public class StandingDaoResource {

	@Autowired
	private StandingRepository standingRepo;

	@Autowired
	private TeamRepository teamRepo;

	@GET
	@Path("/{teamKey}/{asOfDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findStanding(@Context UriInfo uriInfo, 
								@PathParam("teamKey") String teamKey,
								@PathParam("asOfDate") String asOfDateString) {
		try {
			LocalDate asOfDate = DateTimeUtil.getLocalDate(asOfDateString);
			Standing standing = standingRepo.findStanding(teamKey, asOfDate);
			if (standing.isFound()) {
				PubStanding pubStanding = standing.toPubStanding(uriInfo);
				return Response.ok(pubStanding)
					.link(uriInfo.getAbsolutePath(), "standing")
					.build();
			}
			else if (standing.isNotFound()) {
				throw new NoSuchEntityException(Standing.class);
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
		try {
			LocalDate asOfDate = DateTimeUtil.getLocalDate(asOfDateString);
			List<Standing> listStandings = standingRepo.findStandings(asOfDate);
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
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("asOfDate must be yyyy-MM-dd format", e);
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createStanding(@Context UriInfo uriInfo, Standing createStanding) {
		try {
			Team team = teamRepo.findTeam(createStanding.getTeam().getTeamKey(), createStanding.getStandingDate());
			if (team.isFound()) {
				createStanding.getTeam().setId(team.getId());
				Standing standing = standingRepo.createStanding(createStanding);
				if (standing.isCreated()) {
					return Response.created(uriInfo.getAbsolutePath()).build();
				}
				else if (standing.isFound()) {
					throw new DuplicateEntityException(Standing.class);
				}
				else {
					return Response.status(500).build();
				}
			}
			else if (team.isNotFound()) {
				throw new NoSuchEntityException(Team.class);
			}
			else {
				return Response.status(500).build();
			}
		} catch (PropertyValueException e) {
			throw new BadRequestException("missing required field(s)", e);
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateStanding(Standing updateStanding) {
		try {
			Standing standing = standingRepo.updateStanding(updateStanding);
			if (standing.isUpdated()) {
				return Response.noContent().build();
			}
			else if (standing.isNotFound()) {
				throw new NoSuchEntityException(Standing.class);
			}
			else {
				return Response.status(500).build();
			}
		} catch (PropertyValueException e) {
			throw new BadRequestException("missing required field(s)", e);
		}
	}

	@DELETE
	@Path("/{teamKey}/{asOfDate}")
	public Response deleteStanding(@Context UriInfo uriInfo, 
								@PathParam("teamKey") String teamKey, 
								@PathParam("asOfDate") String asOfDateString) {
		try {
			LocalDate asOfDate = DateTimeUtil.getLocalDate(asOfDateString);
			Standing standing = standingRepo.deleteStanding(teamKey, asOfDate);
			if (standing.isDeleted()) {
				return Response.noContent().build();
			}
			else if (standing.isNotFound()){
				throw new NoSuchEntityException(Standing.class);
			}
			else {
				return Response.status(500).build();
			}
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("asOfDate must be yyyy-MM-dd format", e);
		}
	}
}
