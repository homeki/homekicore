package com.homeki.core.web.rest;

import com.homeki.core.actions.Action;
import com.homeki.core.actions.ActionGroup;
import com.homeki.core.actions.ActionParser;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.json.JsonAction;
import com.homeki.core.http.json.JsonVoid;
import com.homeki.core.storage.Hibernate;
import org.hibernate.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ActionGroupActionResource {
	@POST
	@Path("/add")
	public Response add(@PathParam("actionGroupId") int actionGroupId, JsonAction jact) {
		Session ses = Hibernate.currentSession();

		ActionGroup actionGroup = (ActionGroup)ses.get(ActionGroup.class, actionGroupId);

		if (actionGroup == null || !actionGroup.isExplicit())
			throw new ApiException("No action group with the specified ID found.");

		Action action = ActionParser.createAction(jact);

		actionGroup.addAction(action);
		ses.save(action);

		JsonAction newid = new JsonAction();
		newid.id = action.getId();

		return Response.ok(newid).build();
	}

	@GET
	@Path("/list")
	public Response list(@PathParam("actionGroupId") int actionGroupId) {
		ActionGroup actionGroup = (ActionGroup)Hibernate.currentSession().get(ActionGroup.class, actionGroupId);

		if (actionGroup == null || !actionGroup.isExplicit())
			throw new ApiException("No action group with the specified ID found.");

		return Response.ok(JsonAction.convertList(actionGroup.getActions())).build();
	}

	@GET
	@Path("/{actionId}/delete")
	public Response delete(@PathParam("actionGroupId") int actionGroupId, @PathParam("actionId") int actionId) {
		Session ses = Hibernate.currentSession();
		ActionGroup actionGroup = (ActionGroup)ses.get(ActionGroup.class, actionGroupId);

		if (actionGroup == null || !actionGroup.isExplicit())
			throw new ApiException("No action group with the specified ID found.");

		Action act = (Action)ses.get(Action.class, actionId);

		if (act == null)
			throw new ApiException("No action with the specified ID found.");

		actionGroup.deleteAction(act);

		return Response.ok(new JsonVoid("Action successfully deleted.")).build();
	}

	@GET
	@Path("/{actionId}/get")
	public Response get(@PathParam("actionGroupId") int actionGroupId, @PathParam("actionId") int actionId) {
		Session ses = Hibernate.currentSession();
		ActionGroup actionGroup = (ActionGroup)ses.get(ActionGroup.class, actionGroupId);

		if (actionGroup == null || !actionGroup.isExplicit())
			throw new ApiException("No action group with the specified ID found.");

		Action act = (Action)ses.get(Action.class, actionId);

		if (act == null)
			throw new ApiException("No action with the specified ID found.");

		return Response.ok(JsonAction.create(act)).build();
	}

	@POST
	@Path("/{actionId}/set")
	public Response set(@PathParam("actionGroupId") int actionGroupId, @PathParam("actionId") int actionId, JsonAction jact) {
		Session ses = Hibernate.currentSession();

		ActionGroup actionGroup = (ActionGroup)ses.get(ActionGroup.class, actionGroupId);

		if (actionGroup == null)
			throw new ApiException("No action group with the specified ID found.");

		Action act = (Action)ses.get(Action.class, actionId);

		if (act == null)
			throw new ApiException("No action with the specified ID found.");

		ActionParser.updateAction(act, jact);

		ses.save(act);

		return Response.ok(new JsonVoid("Action updated successfully.")).build();
	}
}
