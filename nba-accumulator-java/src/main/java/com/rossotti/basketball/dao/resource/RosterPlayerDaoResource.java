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
import com.rossotti.basketball.dao.model.Player;
import com.rossotti.basketball.dao.model.RosterPlayer;
import com.rossotti.basketball.dao.model.Team;
import com.rossotti.basketball.dao.pub.PubPlayer;
import com.rossotti.basketball.dao.pub.PubRosterPlayer;
import com.rossotti.basketball.dao.pub.PubRosterPlayer_ByPlayer;
import com.rossotti.basketball.dao.pub.PubRosterPlayer_ByTeam;
import com.rossotti.basketball.dao.pub.PubRosterPlayers_ByPlayer;
import com.rossotti.basketball.dao.pub.PubRosterPlayers_ByTeam;
import com.rossotti.basketball.dao.pub.PubTeam;
import com.rossotti.basketball.dao.repository.PlayerRepository;
import com.rossotti.basketball.dao.repository.RosterPlayerRepository;
import com.rossotti.basketball.dao.repository.TeamRepository;
import com.rossotti.basketball.util.DateTimeUtil;

@Service
@Path("/rosterPlayers")
public class RosterPlayerDaoResource {

	@Autowired
	private RosterPlayerRepository rosterPlayerRepo;
	
	@Autowired
	private PlayerRepository playerRepo;

	@Autowired
	private TeamRepository teamRepo;

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
			RosterPlayer rosterPlayer = rosterPlayerRepo.findRosterPlayer(lastName, firstName, birthdate, asOfDate);
			if (rosterPlayer.isFound()) {
				PubRosterPlayer pubRosterPlayer = rosterPlayer.toPubRosterPlayer(uriInfo);
				return Response.ok(pubRosterPlayer)
					.link(uriInfo.getAbsolutePath(), "rosterPlayer")
					.build();
			}
			else if (rosterPlayer.isNotFound()) {
				throw new NoSuchEntityException(RosterPlayer.class);
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
			RosterPlayer rosterPlayer = rosterPlayerRepo.findRosterPlayer(lastName, firstName, teamKey, asOfDate);
			if (rosterPlayer.isFound()) {
				PubRosterPlayer pubRosterPlayer = rosterPlayer.toPubRosterPlayer(uriInfo);
				return Response.ok(pubRosterPlayer)
					.link(uriInfo.getAbsolutePath(), "rosterPlayer")
					.build();
			}
			else if (rosterPlayer.isNotFound()) {
				throw new NoSuchEntityException(RosterPlayer.class);
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
		try {
			LocalDate birthdate = DateTimeUtil.getLocalDate(birthdateString);
			List<RosterPlayer> listRosterPlayers = rosterPlayerRepo.findRosterPlayers(lastName, firstName, birthdate);
			if (listRosterPlayers.size() > 0) {
				PubPlayer pubPlayer = listRosterPlayers.get(0).getPlayer().toPubPlayer(uriInfo);
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
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("dates must be yyyy-MM-dd format", e);
		}
	}

	@GET
	@Path("/team/{teamKey}/{asOfDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findRosterPlayersByTeamKey(@Context UriInfo uriInfo, 
											@PathParam("teamKey") String teamKey,
											@PathParam("asOfDate") String asOfDateString) {
		try {
			LocalDate asOfDate = DateTimeUtil.getLocalDate(asOfDateString);
			List<RosterPlayer> listRosterPlayers = rosterPlayerRepo.findRosterPlayers(teamKey, asOfDate);
			if (listRosterPlayers.size() > 0) {
				PubTeam pubTeam = listRosterPlayers.get(0).getTeam().toPubTeam(uriInfo);
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
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("dates must be yyyy-MM-dd format", e);
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createRosterPlayer(@Context UriInfo uriInfo, RosterPlayer createRosterPlayer) {
		try {
			Player player = playerRepo.findPlayer(createRosterPlayer.getPlayer().getLastName(), createRosterPlayer.getPlayer().getFirstName(), createRosterPlayer.getPlayer().getBirthdate());
			if (player.isFound()) {
				createRosterPlayer.getPlayer().setId(player.getId());
				Team team = teamRepo.findTeam(createRosterPlayer.getTeam().getTeamKey(), createRosterPlayer.getFromDate());
				if (team.isFound()) {
					createRosterPlayer.getTeam().setId(team.getId());
					RosterPlayer rosterPlayer = rosterPlayerRepo.createRosterPlayer(createRosterPlayer);
					if (rosterPlayer.isCreated()) {
						return Response.created(uriInfo.getAbsolutePath()).build();
					}
					else if (rosterPlayer.isFound()){
						throw new DuplicateEntityException(RosterPlayer.class);
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
			}
			else if (player.isNotFound()) {
				throw new NoSuchEntityException(Player.class);
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
	public Response updateRosterPlayer(RosterPlayer updateRosterPlayer) {
		try {
			RosterPlayer rosterPlayer = rosterPlayerRepo.updateRosterPlayer(updateRosterPlayer);
			if (rosterPlayer.isUpdated()) {
				return Response.noContent().build();
			}
			else if (rosterPlayer.isNotFound()) {
				throw new NoSuchEntityException(RosterPlayer.class);
			}
			else {
				return Response.status(500).build();
			}
		} catch (PropertyValueException e) {
			throw new BadRequestException("missing required field(s)", e);
		}
	}

	@DELETE
	@Path("/{lastName}/{firstName}/{birthdate}/{asOfDate}")
	public Response deleteRosterPlayer(@Context UriInfo uriInfo, 
									@PathParam("lastName") String lastName, 
									@PathParam("firstName") String firstName, 
									@PathParam("birthdate") String birthdateString, 
									@PathParam("asOfDate") String asOfDateString) {
		try {
			LocalDate birthdate = DateTimeUtil.getLocalDate(birthdateString);
			LocalDate asOfDate = DateTimeUtil.getLocalDate(asOfDateString);
			RosterPlayer rosterPlayer = rosterPlayerRepo.deleteRosterPlayer(lastName, firstName, birthdate, asOfDate);
			if (rosterPlayer.isDeleted()) {
				return Response.noContent().build();
			}
			else if (rosterPlayer.isNotFound()){
				throw new NoSuchEntityException(RosterPlayer.class);
			}
			else {
				return Response.status(500).build();
			}
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("dates must be yyyy-MM-dd format", e);
		}
	}
}
