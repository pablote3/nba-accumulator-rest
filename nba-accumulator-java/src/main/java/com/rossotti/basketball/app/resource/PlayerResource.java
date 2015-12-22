package com.rossotti.basketball.app.resource;

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
import com.rossotti.basketball.dao.exception.DuplicateEntityException;
import com.rossotti.basketball.model.Player;
import com.rossotti.basketball.pub.PubPlayer;
import com.rossotti.basketball.pub.PubPlayers;

@Service
@Path("/players")
public class PlayerResource {

	@Autowired
	private PlayerDAO playerDAO;

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

	@GET
	@Path("/{lastName}/{firstName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findPlayersByName(@Context UriInfo uriInfo, 
									@PathParam("lastName") String lastName, 
									@PathParam("firstName") String firstName) {
		List<Player> listPlayers = playerDAO.findPlayers(lastName, firstName);
		if (listPlayers.size() > 0) {
			List<PubPlayer> listPubPlayers = new ArrayList<PubPlayer>();
			for (Player player : listPlayers) {
				PubPlayer pubPlayer = player.toPubPlayer(uriInfo);
				listPubPlayers.add(pubPlayer);
			}
			PubPlayers pubPlayers = new PubPlayers(uriInfo.getAbsolutePath(), listPubPlayers);
			return Response.ok(pubPlayers)
					.link(uriInfo.getAbsolutePath(), "player")
					.build();
		}
		else {
			return Response.status(404).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createPlayer(@Context UriInfo uriInfo, Player createPlayer) {
		try {
			Player player = playerDAO.createPlayer(createPlayer);
			if (player.isCreated()) {
				return Response.created(uriInfo.getAbsolutePath()).build();
			}
			else {
				return Response.status(500).build();
			}
		} catch (DuplicateEntityException e) {
			throw new BadRequestException("player " + createPlayer.getFirstName() + " " + createPlayer.getLastName() + " already exists", e);
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