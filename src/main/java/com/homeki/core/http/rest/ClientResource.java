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

@Path("/clients")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClientResource {
	@GET
	public Response list() {
		List<String> clients = ClientStore.INSTANCE.listClients();
		return Response.ok(clients).build();
	}

	@POST
	public Response register(JsonClient jclient) {
		if (Util.nullOrEmpty(jclient.id))
			throw new ApiException("Client ID cannot be null or empty.");

		ClientStore.INSTANCE.addClient(jclient.id);

		return Response.ok(jclient).build();
	}

	@DELETE
	@Path("/{clientId}")
	public Response unregister(@PathParam("clientId") String clientId) {
		ClientStore.INSTANCE.removeClient(clientId);

		return Response.ok(new JsonVoid("Client with ID " + clientId + " removed if it existed.")).build();
	}
}
