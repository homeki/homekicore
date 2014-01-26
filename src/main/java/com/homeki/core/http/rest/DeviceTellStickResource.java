package com.homeki.core.http.rest;

import com.homeki.core.device.Device;
import com.homeki.core.device.tellstick.TellStickDevice;
import com.homeki.core.device.tellstick.TellStickDimmer;
import com.homeki.core.device.tellstick.TellStickLearnable;
import com.homeki.core.device.tellstick.TellStickSwitch;
import com.homeki.core.http.ApiException;
import com.homeki.core.json.JsonDevice;
import com.homeki.core.json.JsonTellStickDevice;
import com.homeki.core.json.JsonVoid;
import com.homeki.core.main.Setting;
import com.homeki.core.main.Util;
import com.homeki.core.storage.Hibernate;
import org.hibernate.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DeviceTellStickResource {
	private static final int UNIT = 3;

	@POST
	@Path("/add")
	public Response add(JsonTellStickDevice jdev) {
		Session ses = Hibernate.currentSession();

		if (Util.isNullOrEmpty(jdev.type))
			throw new ApiException("Missing required field 'type' in JSON.");
		if (Util.isNullOrEmpty(jdev.name))
			throw new ApiException("Missing required field 'name' in JSON.");

		int house;
		int unit;

		if (jdev.house == null)
			house = Setting.getInt(ses, Setting.NEXT_HOUSE_KEY);
		else
			house = jdev.house;

		if (jdev.unit == null)
			unit = UNIT;
		else
			unit = jdev.unit;

		Device dev;

		if (jdev.type.equals("switch"))
			dev = new TellStickSwitch(false, house, unit);
		else if (jdev.type.equals("dimmer"))
			dev = new TellStickDimmer(255, house, unit);
		else
			throw new ApiException("Did not recognize type '" + jdev.type + "' as a valid TellStick type.");

		dev.setName(jdev.name);

		if (jdev.description != null)
			dev.setDescription(jdev.description);

		if (jdev.house == null)
			Setting.putInt(ses, Setting.NEXT_HOUSE_KEY, house+1);

		ses.save(dev);

		JsonDevice newid = new JsonDevice();
		newid.id = dev.getId();

		return Response.ok(newid).build();
	}

	@GET
	@Path("/learn")
	public Response handle(@PathParam("deviceId") int deviceId) {
		TellStickDevice dev = (TellStickDevice)Hibernate.currentSession().get(TellStickDevice.class, deviceId);

		if (dev == null)
			throw new ApiException("No TellStick device with specified ID found.");
		if (!(dev instanceof TellStickLearnable))
			throw new ApiException("Specified TellStick device does not support learn.");

		((TellStickLearnable)dev).learn();

		return Response.ok(new JsonVoid("Learn command sent successfully.")).build();
	}
}
