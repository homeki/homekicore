package com.homeki.core.http.rest;

import com.homeki.core.actions.ActionGroup;
import com.homeki.core.http.ApiException;
import com.homeki.core.json.JsonActionGroup;
import com.homeki.core.json.JsonVoid;
import com.homeki.core.logging.L;
import com.homeki.core.main.Util;
import com.homeki.core.storage.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/actiongroup")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ActionGroupResource {
	@GET
	@Path("/list")
	public Response list() {
		@SuppressWarnings("unchecked")
		List<ActionGroup> list = Hibernate.currentSession().createCriteria(ActionGroup.class)
															 .add(Restrictions.eq("explicit", true))
															 .list();
		return Response.ok(JsonActionGroup.convertList(list)).build();
	}

	@POST
	@Path("/{actionGroupId}/set")
	public Response set(@PathParam("actionGroupId") int actionGroupId, JsonActionGroup jactgr) {
		ActionGroup actgrp = (ActionGroup)Hibernate.currentSession().get(ActionGroup.class, actionGroupId);

		if (actgrp == null || !actgrp.isExplicit())
			throw new ApiException("No action group with the specified ID found.");

		if (Util.isNullOrEmpty(jactgr.name))
			throw new ApiException("New action group name cannot be empty.");

		if (jactgr.name != null)
			actgrp.setName(jactgr.name);

		// TODO: session.save() needed here?

		return Response.ok(new JsonVoid("Action group updated successfully.")).build();
	}

	@GET
	@Path("/{actionGroupId}/delete")
	public Response delete(@PathParam("actionGroupId") int actionGroupId) {
		Session ses = Hibernate.currentSession();

		ActionGroup actionGroup = (ActionGroup)ses.get(ActionGroup.class, actionGroupId);

		if (actionGroup == null || !actionGroup.isExplicit())
			throw new ApiException("No action group with the specified ID found.");

		// TODO: add checks for future connected actions which possibly trigger this actiongroup before deletion

		ses.delete(actionGroup);

		return Response.ok(new JsonVoid("Action group deleted successfully.")).build();
	}

	@POST
	@Path("/add")
	public Response add(JsonActionGroup jactgrp) {
		if (Util.isNullOrEmpty(jactgrp.name))
			throw new ApiException("Action group name cannot be empty.");

		ActionGroup actionGroup = new ActionGroup();
		actionGroup.setName(jactgrp.name);
		actionGroup.setExplicit(true);
		Hibernate.currentSession().save(actionGroup);

		JsonActionGroup newid = new JsonActionGroup();
		newid.id = actionGroup.getId();

		return Response.ok(newid).build();
	}

	@GET
	@Path("/{actionGroupId}/trigger")
	public Response trigger(@PathParam("actionGroupId") final int actionGroupId) {
		ActionGroup actgrp = (ActionGroup)Hibernate.currentSession().get(ActionGroup.class, actionGroupId);

		if (actgrp == null || !actgrp.isExplicit())
			throw new ApiException("No action group with the specified ID found.");

		new Thread() {
			public void run() {
				setName("ManualTriggerThread");
				L.i("Manually triggering action group in spawned thread...");
				Session ses = Hibernate.openSession();
				try {
					ActionGroup actgrp = (ActionGroup)ses.get(ActionGroup.class, actionGroupId);
					actgrp.execute(ses);
				} catch (Exception e) {
					L.e("Error occured during manual trigger of action group.", e);
				} finally {
					Hibernate.closeSession(ses);
				}
				L.i("Manual trigger of action group in spawned thread completed.");
			};
		}.start();

		return Response.ok("Action group executed successfully.").build();
	}

	@Path("/{actionGroupId}/action")
	public Class<ActionGroupActionResource> continueInAction() {
		return ActionGroupActionResource.class;
	}
}
