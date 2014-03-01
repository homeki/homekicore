package com.homeki.core.http.rest;

import com.homeki.core.conditions.*;
import com.homeki.core.http.ApiException;
import com.homeki.core.json.*;
import com.homeki.core.json.conditions.JsonCondition;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.triggers.Trigger;
import org.hibernate.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TriggerConditionResource {
	@POST
	public Response add(@PathParam("triggerId") int triggerId, JsonCondition jcond) {
		Session ses = Hibernate.currentSession();
		Trigger trigger = (Trigger)ses.get(Trigger.class, triggerId);

		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");

		Condition cond = ConditionParser.createCondition(jcond);

		trigger.addCondition(cond);
		ses.save(cond);

		return Response.ok(JsonCondition.create(cond)).build();
	}

	@GET
	public Response list(@PathParam("triggerId") int triggerId) {
		Trigger trigger = (Trigger)Hibernate.currentSession().get(Trigger.class, triggerId);

		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");

		return Response.ok(JsonCondition.convertList(trigger.getConditions())).build();
	}

	@DELETE
	@Path("/{conditionId}")
	public Response delete(@PathParam("triggerId") int triggerId, @PathParam("conditionId") int conditionId) {
		Session ses = Hibernate.currentSession();
		Trigger trigger = (Trigger)ses.get(Trigger.class, triggerId);

		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");

		Condition cond = (Condition)ses.get(Condition.class, conditionId);

		if (cond == null)
			throw new ApiException("No condition with the specified ID found.");

		trigger.deleteCondition(cond);

		return Response.ok(new JsonVoid("Condition successfully deleted.")).build();
	}

	@GET
	@Path("/{conditionId}")
	public Response get(@PathParam("triggerId") int triggerId, @PathParam("conditionId") int conditionId) {
		Session ses = Hibernate.currentSession();
		Trigger trigger = (Trigger)ses.get(Trigger.class, triggerId);

		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");

		Condition cond = (Condition)ses.get(Condition.class, conditionId);

		if (cond == null)
			throw new ApiException("No condition with the specified ID found.");

		return Response.ok(JsonCondition.create(cond)).build();
	}

	@POST
	@Path("/{conditionId}")
	public Response set(@PathParam("triggerId") int triggerId, @PathParam("conditionId") int conditionId, JsonCondition jcond) {
		Session ses = Hibernate.currentSession();
		Trigger trigger = (Trigger)ses.get(Trigger.class, triggerId);

		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");

		Condition cond = (Condition)ses.get(Condition.class, conditionId);

		if (cond == null)
			throw new ApiException("No condition with the specified ID found.");

		ConditionParser.updateCondition(cond, jcond);
		ses.save(cond);

		return Response.ok(JsonCondition.create(cond)).build();
	}
}
