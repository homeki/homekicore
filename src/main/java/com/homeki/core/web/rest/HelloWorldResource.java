package com.homeki.core.web.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/helloworld")
public class HelloWorldResource {
	@GET
	public Response get() {
		return Response.ok("Jonas, damn you rock!").build();
	}
}
