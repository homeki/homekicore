package com.homeki.core.http.rest;

import com.homeki.core.clientwatch.ClientStore;
import com.homeki.core.http.ApiException;
import com.homeki.core.json.JsonClient;
import com.homeki.core.json.JsonVoid;
import com.homeki.core.main.Util;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/client")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClientResource {
	@GET
	@Path("/list")
	public Response list() {
		List<String> clients = ClientStore.INSTANCE.listClients();
		return Response.ok(clients).build();
	}

	@POST
	@Path("/register")
	public Response register(JsonClient jclient) {
		if (Util.nullOrEmpty(jclient.id))
			throw new ApiException("Client ID cannot be null or empty.");

		ClientStore.INSTANCE.addClient(jclient.id);

		return Response.ok(new JsonVoid("Client registered successfully as " + jclient.id + ".")).build();
	}

	@POST
	@Path("/unregister")
	public Response unregister(JsonClient jclient) {
		if (Util.nullOrEmpty(jclient.id))
			throw new ApiException("Client ID cannot be null or empty.");

		ClientStore.INSTANCE.removeClient(jclient.id);

		return Response.ok(new JsonVoid("Client with ID " + jclient.id + " successfully removed.")).build();
	}
}
