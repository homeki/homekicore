package com.homeki.core.http;

import com.homeki.core.json.JsonVoid;
import com.homeki.core.logging.L;

import javax.ws.rs.core.Response;

public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Throwable> {
	@Override
	public Response toResponse(Throwable exception) {
		try {
			throw exception;
		} catch (ApiException e) {
			return Response.status(400).entity(new JsonVoid(e.getMessage())).build();
		} catch (Throwable e) {
			L.e("Unknown exception occured while processing request.", e);
			return Response.status(400).entity("Unhandled exception occured while processing request: " + e.getMessage()).build();
		}
	}
}
