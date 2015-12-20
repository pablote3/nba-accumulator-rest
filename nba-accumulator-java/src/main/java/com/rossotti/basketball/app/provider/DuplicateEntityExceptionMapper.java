package com.rossotti.basketball.app.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.rossotti.basketball.dao.exception.DuplicateEntityException;

@Provider
public class DuplicateEntityExceptionMapper implements ExceptionMapper<DuplicateEntityException>{

	@Override
	public Response toResponse(DuplicateEntityException exception) {
		String msg = exception.getMessage();
		return Response.status(Response.Status.FORBIDDEN).entity(msg).build();
	}

}
