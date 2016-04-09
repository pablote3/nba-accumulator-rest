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
import com.rossotti.basketball.dao.model.Official;
import com.rossotti.basketball.dao.pub.PubOfficial;
import com.rossotti.basketball.dao.pub.PubOfficials;
import com.rossotti.basketball.dao.repository.OfficialDAO;
import com.rossotti.basketball.util.DateTimeUtil;

@Service
@Path("/officials")
public class OfficialResource {

	@Autowired
	private OfficialDAO officialDAO;

	@GET
	@Path("/{lastName}/{firstName}/{asOfDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findOfficialByNameDate(@Context UriInfo uriInfo, 
										@PathParam("lastName") String lastName,
										@PathParam("firstName") String firstName,
										@PathParam("asOfDate") String asOfDateString) {
		try {
			LocalDate asOfDate = DateTimeUtil.getLocalDate(asOfDateString);
			Official official = officialDAO.findOfficial(lastName, firstName, asOfDate);
			if (official.isFound()) {
				PubOfficial pubOfficial = official.toPubOfficial(uriInfo);
				return Response.ok(pubOfficial)
					.link(uriInfo.getAbsolutePath(), "official")
					.build();
			}
			else if (official.isNotFound()) {
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
	@Path("/{lastName}/{firstName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findOfficialsByName(@Context UriInfo uriInfo, 
										@PathParam("lastName") String lastName, 
										@PathParam("firstName") String firstName) {
		List<Official> listOfficials = officialDAO.findOfficials(lastName, firstName);
		if (listOfficials.size() > 0) {
			List<PubOfficial> listPubOfficials = new ArrayList<PubOfficial>();
			for (Official official : officialDAO.findOfficials(lastName, firstName)) {
				PubOfficial pubOfficial = official.toPubOfficial(uriInfo);
				listPubOfficials.add(pubOfficial);
			}
			PubOfficials pubOfficials = new PubOfficials(uriInfo.getAbsolutePath(), listPubOfficials);
			return Response.ok(pubOfficials)
				.link(uriInfo.getAbsolutePath(), "official")
				.build();
		}
		else {
			return Response.status(404).build();
		}
	}

	@GET
	@Path("/{asOfDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findOfficialsByDate(@Context UriInfo uriInfo, 
										@PathParam("asOfDate") String asOfDateString) {
		try {
			LocalDate asOfDate = DateTimeUtil.getLocalDate(asOfDateString);
			List<Official> listOfficials = officialDAO.findOfficials(asOfDate);
			if (listOfficials.size() > 0) {
				List<PubOfficial> listPubOfficials = new ArrayList<PubOfficial>();
				for (Official official : listOfficials) {
					PubOfficial pubOfficial = official.toPubOfficial(uriInfo);
					listPubOfficials.add(pubOfficial);
				}
				PubOfficials pubOfficials = new PubOfficials(uriInfo.getAbsolutePath(), listPubOfficials);
				return Response.ok(pubOfficials)
					.link(uriInfo.getAbsolutePath(), "official")
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
	public Response createOfficial(@Context UriInfo uriInfo, Official createOfficial) {
		try {
			Official official = officialDAO.createOfficial(createOfficial);
			if (official.isCreated()) {
				return Response.created(uriInfo.getAbsolutePath()).build();
			}
			else {
				return Response.status(500).build();
			}
		} catch (DuplicateEntityException e) {
			throw new BadRequestException("official " + createOfficial.getFirstName() + " " + createOfficial.getLastName() + " already exists", e);
		} catch (PropertyValueException e) {
			throw new BadRequestException("missing required field(s)", e);
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateOfficial(Official updateOfficial) {
		try {
			Official official = officialDAO.updateOfficial(updateOfficial);
			if (official.isUpdated()) {
				return Response.noContent().build();
			}
			else if (official.isNotFound()) {
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
	@Path("/{lastName}/{firstName}/{asOfDate}")
	public Response deleteOfficial(@Context UriInfo uriInfo, 
								@PathParam("lastName") String lastName,
								@PathParam("firstName") String firstName,
								@PathParam("asOfDate") String asOfDateString) {
		try {
			LocalDate asOfDate = DateTimeUtil.getLocalDate(asOfDateString);
			Official official = officialDAO.deleteOfficial(lastName, firstName, asOfDate);
			if (official.isDeleted()) {
				return Response.noContent().build();
			}
			else if (official.isNotFound()){
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
