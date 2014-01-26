package com.homeki.core.web.rest;

import com.homeki.core.web.ApiException;
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

@Path("/trigger")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TriggerResource {
	@GET
	@Path("/list")
	public Response list() {
		List<Trigger> list = Hibernate.currentSession().createCriteria(Trigger.class).list();
		return Response.ok(JsonTrigger.convertList(list)).build();
	}

	@POST
	@Path("/add")
	public Response add(JsonTrigger jtrigger) {
		if (Util.isNullOrEmpty(jtrigger.name))
			throw new ApiException("Trigger name cannot be empty.");

		Trigger trigger = new Trigger();
		trigger.setName(jtrigger.name);
		Hibernate.currentSession().save(trigger);

		JsonTrigger newid = new JsonTrigger();
		newid.id = trigger.getId();

		return Response.ok(newid).build();
	}

	@POST
	@Path("/{triggerId}/set")
	public Response set(@PathParam("triggerId") int triggerId, JsonTrigger jtrigger) {
		Trigger trigger = (Trigger)Hibernate.currentSession().get(Trigger.class, triggerId);

		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");

		if (Util.isNullOrEmpty(jtrigger.name))
			throw new ApiException("New trigger name cannot be empty.");

		if (jtrigger.name != null)
			trigger.setName(jtrigger.name);

		return Response.ok(new JsonVoid("Trigger updated successfully.")).build();
	}

	@GET
	@Path("/{triggerId}/delete")
	public Response delete(@PathParam("triggerId") int triggerId) {
		Session ses = Hibernate.currentSession();
		Trigger trigger = (Trigger)ses.get(Trigger.class, triggerId);

		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");

		ses.delete(trigger);

		return Response.ok(new JsonVoid("Trigger deleted successfully.")).build();
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
