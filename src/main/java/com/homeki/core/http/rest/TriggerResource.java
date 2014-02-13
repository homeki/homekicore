package com.homeki.core.http.rest;

import com.homeki.core.http.ApiException;
import com.homeki.core.json.JsonTrigger;
import com.homeki.core.json.JsonVoid;
import com.homeki.core.main.Util;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.triggers.Trigger;
import org.hibernate.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/triggers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TriggerResource {
	@GET
	public Response list() {
		List<Trigger> list = Hibernate.currentSession().createCriteria(Trigger.class).list();
		return Response.ok(JsonTrigger.convertList(list)).build();
	}

	@POST
	public Response add(JsonTrigger jtrigger) {
		if (Util.nullOrEmpty(jtrigger.name))
			throw new ApiException("Trigger name cannot be empty.");

		Trigger trigger = new Trigger();
		trigger.setName(jtrigger.name);
		if (jtrigger.description != null)
			trigger.setDescription(jtrigger.description);
		else
			trigger.setDescription("");

		Hibernate.currentSession().save(trigger);

		return Response.ok(new JsonTrigger(trigger)).build();
	}

	@POST
	@Path("/{triggerId}")
	public Response set(@PathParam("triggerId") int triggerId, JsonTrigger jtrigger) {
		Trigger trigger = (Trigger)Hibernate.currentSession().get(Trigger.class, triggerId);

		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");

		if (Util.nullOrEmpty(jtrigger.name))
			throw new ApiException("New trigger name cannot be empty.");

		if (jtrigger.name != null)
			trigger.setName(jtrigger.name);
		if (jtrigger.description != null)
			trigger.setDescription(jtrigger.description);

		return Response.ok(new JsonTrigger(trigger)).build();
	}

	@DELETE
	@Path("/{triggerId}")
	public Response delete(@PathParam("triggerId") int triggerId) {
		Session ses = Hibernate.currentSession();
		Trigger trigger = (Trigger)ses.get(Trigger.class, triggerId);

		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");

		ses.delete(trigger);

		return Response.ok(new JsonVoid("Trigger successfully deleted.")).build();
	}

	@Path("/{triggerId}/action")
	public Class<TriggerActionResource> continueInAction() {
		return TriggerActionResource.class;
	}

	@Path("/{triggerId}/condition")
	public Class<TriggerConditionResource> continueInCondition() {
		return TriggerConditionResource.class;
	}
}
