package com.homeki.core.web.rest;

import com.homeki.core.conditions.*;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.json.*;
import com.homeki.core.http.json.conditions.JsonCondition;
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
	@Path("/add")
	public Response add(@PathParam("triggerId") int triggerId, JsonCondition jcond) {
		Session ses = Hibernate.currentSession();
		Trigger trigger = (Trigger)ses.get(Trigger.class, triggerId);

		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");

		Condition condition = ConditionParser.createCondition(jcond);

		trigger.addCondition(condition);
		ses.save(condition);

		JsonCondition newid = new JsonCondition();
		newid.id = condition.getId();

		return Response.ok(newid).build();
	}

	@GET
	@Path("/list")
	public Response list(@PathParam("triggerId") int triggerId) {
		Trigger trigger = (Trigger)Hibernate.currentSession().get(Trigger.class, triggerId);

		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");

		return Response.ok(JsonCondition.convertList(trigger.getConditions())).build();
	}

	@GET
	@Path("/{conditionId}/delete")
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
	@Path("/{conditionId}/get")
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
	@Path("/{conditionId}/set")
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

		return Response.ok(new JsonVoid("Condition updated successfully.")).build();
	}
}
