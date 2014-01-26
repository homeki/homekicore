package com.homeki.core.web.rest;

import com.homeki.core.actions.Action;
import com.homeki.core.actions.ActionParser;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.json.actions.JsonAction;
import com.homeki.core.http.json.JsonVoid;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.triggers.Trigger;
import org.hibernate.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TriggerActionResource {
	@POST
	@Path("/add")
	public Response add(@PathParam("triggerId") int triggerId, JsonAction jact) {
		Session ses = Hibernate.currentSession();

		Trigger trigger = (Trigger)ses.get(Trigger.class, triggerId);

		if (trigger == null)
			throw new ApiException("No action group with the specified ID found.");

		Action action = ActionParser.createAction(jact);

		trigger.addAction(action);
		ses.save(action);

		JsonAction newid = new JsonAction();
		newid.id = action.getId();

		return Response.ok(newid).build();
	}

	@GET
	@Path("/list")
	public Response list(@PathParam("triggerId") int triggerId) {
		Trigger trigger = (Trigger)Hibernate.currentSession().get(Trigger.class, triggerId);

		if (trigger == null)
			throw new ApiException("No action group with the specified ID found.");

		return Response.ok(JsonAction.convertList(trigger.getActions())).build();
	}

	@GET
	@Path("/{actionId}/delete")
	public Response delete(@PathParam("triggerId") int triggerId, @PathParam("actionId") int actionId) {
		Session ses = Hibernate.currentSession();
		Trigger trigger = (Trigger)ses.get(Trigger.class, triggerId);

		if (trigger == null)
			throw new ApiException("No action group with the specified ID found.");

		Action act = (Action)ses.get(Action.class, actionId);

		if (act == null)
			throw new ApiException("No action with the specified ID found.");

		trigger.deleteAction(act);

		return Response.ok(new JsonVoid("Action successfully deleted.")).build();
	}

	@GET
	@Path("/{actionId}/get")
	public Response get(@PathParam("triggerId") int triggerId, @PathParam("actionId") int actionId) {
		Session ses = Hibernate.currentSession();
		Trigger trigger = (Trigger)ses.get(Trigger.class, triggerId);

		if (trigger == null)
			throw new ApiException("No action group with the specified ID found.");

		Action act = (Action)ses.get(Action.class, actionId);

		if (act == null)
			throw new ApiException("No action with the specified ID found.");

		return Response.ok(JsonAction.create(act)).build();
	}

	@POST
	@Path("/{actionId}/set")
	public Response set(@PathParam("triggerId") int triggerId, @PathParam("actionId") int actionId, JsonAction jact) {
		Session ses = Hibernate.currentSession();

		Trigger trigger = (Trigger)ses.get(Trigger.class, triggerId);

		if (trigger == null)
			throw new ApiException("No action group with the specified ID found.");

		Action act = (Action)ses.get(Action.class, actionId);

		if (act == null)
			throw new ApiException("No action with the specified ID found.");

		ActionParser.updateAction(act, jact);

		ses.save(act);

		return Response.ok(new JsonVoid("Action updated successfully.")).build();
	}
}
