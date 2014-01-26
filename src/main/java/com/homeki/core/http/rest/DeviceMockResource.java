package com.homeki.core.http.rest;

import com.homeki.core.device.Device;
import com.homeki.core.device.mock.MockDimmer;
import com.homeki.core.device.mock.MockModule;
import com.homeki.core.device.mock.MockSwitch;
import com.homeki.core.device.mock.MockThermometer;
import com.homeki.core.http.ApiException;
import com.homeki.core.json.JsonDevice;
import com.homeki.core.storage.Hibernate;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DeviceMockResource {
	@POST
	@Path("/add")
	public Response add(JsonDevice jdev) {
		Device dev;

		if (jdev.type.equals("switch"))
			dev = new MockSwitch(false);
		else if (jdev.type.equals("dimmer"))
			dev = new MockDimmer(0);
		else if (jdev.type.equals("thermometer"))
			dev = new MockThermometer(0.0);
		else
			throw new ApiException("Did not recognize type '" + jdev.type + "' as a valid mock device type.");

		dev.setInternalId(jdev.type + MockModule.getNextCount());

		if (jdev.name != null)
			dev.setName(jdev.name);
		if (jdev.description != null)
			dev.setDescription(jdev.description);

		Hibernate.currentSession().save(dev);

		JsonDevice newid = new JsonDevice();
		newid.id = dev.getId();

		return Response.ok(newid).build();
	}
}
