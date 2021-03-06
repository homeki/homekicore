package com.homeki.core.http.rest;

import com.homeki.core.device.Device;
import com.homeki.core.device.HistoryPoint;
import com.homeki.core.device.Settable;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.DateParam;
import com.homeki.core.json.JsonChannel;
import com.homeki.core.json.JsonChannelValue;
import com.homeki.core.json.JsonHistoryPoint;
import com.homeki.core.json.JsonVoid;
import com.homeki.core.storage.Hibernate;
import org.hibernate.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DeviceChannelResource {
	@GET
	public Response list(@PathParam("deviceId") int deviceId) {
		Device dev = (Device)Hibernate.currentSession().get(Device.class, deviceId);

		if (dev == null)
			throw new ApiException("No device with specified ID.");

		return Response.ok(JsonChannel.convertList(dev.getChannels())).build();
	}

	@POST
	@Path("/{channel}")
	public Response set(@PathParam("deviceId") int deviceId, @PathParam("channel") int channel, JsonChannelValue value) {
		Device dev = (Device)Hibernate.currentSession().get(Device.class, deviceId);

		if (dev == null)
			throw new ApiException("Could not load device from device ID.");
		if (!(dev instanceof Settable))
			throw new ApiException("Device with specified ID is not settable.");

		((Settable)dev).set(channel, value.value);

		return Response.ok(new JsonVoid("Device channel value successfully changed.")).build();
	}

	@GET
	@Path("/{channel}")
	public Response listValues(@PathParam("deviceId") int deviceId, @PathParam("channel") int channel, @QueryParam("from") DateParam from, @QueryParam("to") DateParam to) {
		Session ses = Hibernate.currentSession();

		Device dev = (Device)ses.get(Device.class, deviceId);

		if (dev == null)
			throw new ApiException("No device with specified ID.");

		@SuppressWarnings("unchecked")
		List<HistoryPoint> l = ses.createFilter(dev.getHistoryPoints(), "where channel = ? and registered between ? and ? order by registered desc")
														 .setInteger(0, channel)
														 .setTimestamp(1, from.value())
														 .setTimestamp(2, to.value())
														 .list();

		return Response.ok(JsonHistoryPoint.convertList(l)).build();
	}
}
