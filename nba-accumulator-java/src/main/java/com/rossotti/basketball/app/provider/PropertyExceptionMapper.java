package com.rossotti.basketball.app.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.rossotti.basketball.dao.exception.PropertyException;

@Provider
public class PropertyExceptionMapper implements ExceptionMapper<PropertyException>{

	@Override
	public Response toResponse(PropertyException exception) {
		String msg = exception.getMessage();
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).build();
	}

}
