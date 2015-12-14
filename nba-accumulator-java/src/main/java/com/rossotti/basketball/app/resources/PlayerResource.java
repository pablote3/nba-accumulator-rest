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

import com.rossotti.basketball.dao.PlayerDAO;
import com.rossotti.basketball.dao.exceptions.DuplicateEntityException;
import com.rossotti.basketball.models.Player;
import com.rossotti.basketball.pub.PubPlayer;
import com.rossotti.basketball.pub.PubPlayers;

@Service
@Path("/players")
public class PlayerResource {

	@Autowired
	private PlayerDAO playerDAO;

	@GET
	@Path("/{lastName}/{firstName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findPlayersByName(@Context UriInfo uriInfo, 
									@PathParam("lastName") String lastName, 
									@PathParam("firstName") String firstName) {
		List<PubPlayer> listPlayers = new ArrayList<PubPlayer>();
		if (listPlayers.size() > 0) {
			for (Player player : playerDAO.findPlayers(lastName, firstName)) {
				PubPlayer pubPlayer = player.toPubPlayer(uriInfo);
				listPlayers.add(pubPlayer);
			}
			PubPlayers pubPlayers = new PubPlayers(uriInfo.getAbsolutePath(), listPlayers);
			return Response.ok(pubPlayers)
					.link(uriInfo.getAbsolutePath(), "player")
					.build();
		}
		else {
			return Response.status(404).build();
		}
	}

	@GET
	@Path("/{lastName}/{firstName}/{birthdate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findPlayerByNameBirthdate(@Context UriInfo uriInfo, 
										@PathParam("lastName") String lastName, 
										@PathParam("firstName") String firstName,
										@PathParam("birthdate") String birthdateString) {
		try {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			LocalDate birthdate = formatter.parseLocalDate(birthdateString);
			Player player = playerDAO.findPlayer(lastName, firstName, birthdate);
			if (player.isFound()) {
				PubPlayer pubPlayer = player.toPubPlayer(uriInfo);
				return Response.ok(pubPlayer)
						.link(uriInfo.getAbsolutePath(), "player")
						.build();
			}
			else if (player.isNotFound()) {
				return Response.status(404).build();
			}
			else {
				return Response.status(500).build();
			}
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("birthdate must be yyyy-MM-dd format", e);
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createPlayer(@Context UriInfo uriInfo, Player deletePlayer) {
		try {
			Player player = playerDAO.createPlayer(deletePlayer);
			if (player.isDeleted()) {
				return Response.created(uriInfo.getAbsolutePath()).build();
			}
			else {
				return Response.status(500).build();
			}
		} catch (DuplicateEntityException e) {
			throw new BadRequestException("player " + deletePlayer.getFirstName() + " " + deletePlayer.getLastName() + " already exists", e);
		} catch (PropertyValueException e) {
			throw new BadRequestException("missing required field(s)", e);
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updatePlayer(Player updatePlayer) {
		try {
			Player player = playerDAO.updatePlayer(updatePlayer);
			if (player.isUpdated()) {
				return Response.noContent().build();
			}
			else if (player.isNotFound()) {
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
	@Path("/{lastName}/{firstName}/{birthdate}")
	public Response deletePlayer(@Context UriInfo uriInfo, 
								@PathParam("lastName") String lastName, 
								@PathParam("firstName") String firstName, 
								@PathParam("birthdate") String birthdateString) {
		try {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			LocalDate birthdate = formatter.parseLocalDate(birthdateString);
			Player player = playerDAO.deletePlayer(lastName, firstName, birthdate);
			if (player.isDeleted()) {
				return Response.noContent().build();
			}
			else if (player.isNotFound()){
				return Response.status(404).build();
			}
			else {
				return Response.status(500).build();
			}
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("birthdate must be yyyy-MM-dd format", e);
		}
	}
}
