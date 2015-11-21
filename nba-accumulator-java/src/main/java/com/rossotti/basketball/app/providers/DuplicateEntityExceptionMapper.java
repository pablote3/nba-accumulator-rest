package com.rossotti.basketball.app.providers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.rossotti.basketball.dao.exceptions.DuplicateEntityException;

@Provider
public class DuplicateEntityExceptionMapper implements ExceptionMapper<DuplicateEntityException>{

	@Override
	public Response toResponse(DuplicateEntityException exception) {
		String msg = exception.getMessage();
		return Response.status(Response.Status.FORBIDDEN).entity(msg).build();
	}

}
