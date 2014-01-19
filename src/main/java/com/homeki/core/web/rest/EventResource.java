package com.homeki.core.web.rest;

import com.homeki.core.events.EventQueue;
import com.homeki.core.events.SpecialValueChangedEvent;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.json.JsonSpecialValue;
import com.homeki.core.main.Util;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/event")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EventResource {
	@POST
	@Path("/specialvalue")
	public Response triggerSpecialValue(JsonSpecialValue jvalue) {
		if (Util.isNullOrEmpty(jvalue.source))
			throw new ApiException("Source cannot be empty.");
		if (jvalue.value == null)
			throw new ApiException("Value cannot be empty.");

		EventQueue.INSTANCE.add(SpecialValueChangedEvent.createCustomEvent(jvalue.source, jvalue.value));

		return Response.ok("Special value changed event triggered successfully.").build();
	}
}
