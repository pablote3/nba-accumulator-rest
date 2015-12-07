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
import com.rossotti.basketball.dao.exceptions.NoSuchEntityException;
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
		try {
			List<PubPlayer> listPlayers = new ArrayList<PubPlayer>();
			for (Player player : playerDAO.findPlayers(lastName, firstName)) {
				PubPlayer pubPlayer = player.toPubPlayer(uriInfo);
				listPlayers.add(pubPlayer);
			}
			PubPlayers pubPlayers = new PubPlayers(uriInfo.getAbsolutePath(), listPlayers);
			return Response.ok(pubPlayers)
				.link(uriInfo.getAbsolutePath(), "player")
				.build();
		} catch (NoSuchEntityException e) {
			return Response.status(404).build();
		}
	}

	@GET
	@Path("/{lastName}/{firstName}/{birthDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findPlayerByKeyDate(@Context UriInfo uriInfo, 
										@PathParam("lastName") String lastName, 
										@PathParam("firstName") String firstName,
										@PathParam("birthDate") String birthDateString) {
		try {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			LocalDate birthDate = formatter.parseLocalDate(birthDateString);
			Player player = playerDAO.findPlayer(lastName, firstName, birthDate);
			PubPlayer pubPlayer = player.toPubPlayer(uriInfo);
			return Response.ok(pubPlayer)
				.link(uriInfo.getAbsolutePath(), "player")
				.build();
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("asOfDate must be yyyy-MM-dd format", e);
		} catch (NoSuchEntityException e) {
			return Response.status(404).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createPlayer(@Context UriInfo uriInfo, Player player) {
		try {
			playerDAO.createPlayer(player);
			return Response.created(uriInfo.getAbsolutePath()).build();
		} catch (DuplicateEntityException e) {
			throw new BadRequestException("player " + player.getFirstName() + " " + player.getLastName() + " already exists", e);
		} catch (PropertyValueException e) {
			throw new BadRequestException("missing required field(s)", e);
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updatePlayer(Player player) {
		try {
			playerDAO.updatePlayer(player);
			return Response.noContent().build();
		} catch (PropertyValueException e) {
			throw new BadRequestException("missing required field(s)", e);
		}
	}
	
	@DELETE
	@Path("/{lastName}/{firstName}/{birthDate}")
	public Response deletePlayer(@Context UriInfo uriInfo, 
								@PathParam("lastName") String lastName, 
								@PathParam("firstName") String firstName, 
								@PathParam("birthDate") String birthDateString) {
		try {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			LocalDate birthDate = formatter.parseLocalDate(birthDateString);
			playerDAO.deletePlayer(lastName, firstName, birthDate);
			return Response.noContent().build();
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("asOfDate must be yyyy-MM-dd format", e);
		} catch (NoSuchEntityException e) {
			return Response.status(404).build();
		}
	}
}
