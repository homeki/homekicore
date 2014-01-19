package com.homeki.core.web.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("device")
public class DeviceResource {
	public Response list() {
		return null;
	}

	@GET
	public Response get() {
		return Response.ok("hejsan").build();
	}

	public Response set() {
		return null;
	}

	public Response delete() {
		return null;
	}

	public Response merge() {
		return null;
	}
}
