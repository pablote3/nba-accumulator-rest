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

import com.rossotti.basketball.dao.OfficialDAO;
import com.rossotti.basketball.dao.exceptions.DuplicateEntityException;
import com.rossotti.basketball.dao.exceptions.NoSuchEntityException;
import com.rossotti.basketball.models.Official;
import com.rossotti.basketball.pub.PubOfficial;
import com.rossotti.basketball.pub.PubOfficials;

@Service
@Path("/officials")
public class OfficialResource {

	@Autowired
	private OfficialDAO officialDAO;

	@GET
	@Path("/name/{lastName}/{firstName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findOfficialsByLastNameFirstName(@Context UriInfo uriInfo, 
									@PathParam("lastName") String lastName, 
									@PathParam("firstName") String firstName) {
		try {
			List<PubOfficial> listOfficials = new ArrayList<PubOfficial>();
			for (Official official : officialDAO.findOfficials(lastName, firstName)) {
				PubOfficial pubOfficial = official.toPubOfficial(uriInfo);
				listOfficials.add(pubOfficial);
			}
			PubOfficials pubOfficials = new PubOfficials(uriInfo.getAbsolutePath(), listOfficials);
			return Response.ok(pubOfficials)
				.link(uriInfo.getAbsolutePath(), "official")
				.build();
		} catch (NoSuchEntityException e) {
			return Response.status(404).build();
		}
	}

	@GET
	@Path("/date/{fromDate}/{toDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findOfficialsByDate(@Context UriInfo uriInfo, 
									@PathParam("fromDate") String fromDateString, 
									@PathParam("toDate") String toDateString) {
		try {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			LocalDate fromDate = formatter.parseLocalDate(fromDateString);
			LocalDate toDate = formatter.parseLocalDate(toDateString);
			List<PubOfficial> listOfficials = new ArrayList<PubOfficial>();
			for (Official official : officialDAO.findOfficials(fromDate, toDate)) {
				PubOfficial pubOfficial = official.toPubOfficial(uriInfo);
				listOfficials.add(pubOfficial);
			}
			
			PubOfficials pubOfficials = new PubOfficials(uriInfo.getAbsolutePath(), listOfficials);
			return Response.ok(pubOfficials)
				.link(uriInfo.getAbsolutePath(), "official")
				.build();
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("asOfDate must be yyyy-MM-dd format", e);
		} catch (NoSuchEntityException e) {
			return Response.status(404).build();
		}
	}
	
	@GET
	@Path("/{lastName}/{firstName}/{fromDate}/{toDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findOfficialByKeyDate(@Context UriInfo uriInfo, 
									@PathParam("lastName") String lastName,
									@PathParam("firstName") String firstName,
									@PathParam("fromDate") String fromDateString, 
									@PathParam("toDate") String toDateString) {
		try {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			LocalDate fromDate = formatter.parseLocalDate(fromDateString);
			LocalDate toDate = formatter.parseLocalDate(toDateString);
			Official official = officialDAO.findOfficial(lastName, firstName, fromDate, toDate);
			PubOfficial pubOfficial = official.toPubOfficial(uriInfo);
			return Response.ok(pubOfficial)
				.link(uriInfo.getAbsolutePath(), "official")
				.build();
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("asOfDate must be yyyy-MM-dd format", e);
		} catch (NoSuchEntityException e) {
			return Response.status(404).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createOfficial(@Context UriInfo uriInfo, Official official) {
		try {
			officialDAO.createOfficial(official);
			return Response.created(uriInfo.getAbsolutePath()).build();
		} catch (DuplicateEntityException e) {
			throw new BadRequestException("official " + official.getLastName() + " " + official.getFirstName() + " already exists", e);
		} catch (PropertyValueException e) {
			throw new BadRequestException("missing required field(s)", e);
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateOfficial(Official official) {
		try {
			officialDAO.updateOfficial(official);
			return Response.noContent().build();
		} catch (PropertyValueException e) {
			throw new BadRequestException("missing required field(s)", e);
		}
	}
	
	@DELETE
	@Path("/{lastName}/{firstName}/{fromDate}/{toDate}")
	public Response deleteOfficial(@Context UriInfo uriInfo, 
								@PathParam("lastName") String lastName,
								@PathParam("firstName") String firstName,
								@PathParam("fromDate") String fromDateString, 
								@PathParam("toDate") String toDateString) {
		try {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			LocalDate fromDate = formatter.parseLocalDate(fromDateString);
			LocalDate toDate = formatter.parseLocalDate(toDateString);
			officialDAO.deleteOfficial(lastName, firstName, fromDate, toDate);
			return Response.noContent().build();
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("asOfDate must be yyyy-MM-dd format", e);
		} catch (NoSuchEntityException e) {
			return Response.status(404).build();
		}
	}
}
