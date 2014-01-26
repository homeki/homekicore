package com.homeki.core.web.rest;

import com.homeki.core.device.Device;
import com.homeki.core.device.HistoryPoint;
import com.homeki.core.web.ApiException;
import com.homeki.core.json.JsonDevice;
import com.homeki.core.json.JsonVoid;
import com.homeki.core.main.Util;
import com.homeki.core.storage.Hibernate;
import org.hibernate.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/device")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DeviceResource {
	@GET
	@Path("/list")
	public Response list() {
		List<Device> list = Hibernate.currentSession().createCriteria(Device.class).list();
		return Response.ok(JsonDevice.convertList(list)).build();
	}

	@GET
	@Path("/{deviceId}/get")
	public Response get(@PathParam("deviceId") int deviceId) {
		Device dev = (Device)Hibernate.currentSession().get(Device.class, deviceId);

		if (dev == null)
			throw new ApiException("No device with specified ID.");

		return Response.ok(new JsonDevice(dev)).build();
	}

	@POST
	@Path("/{deviceId}/set")
	public Response set(@PathParam("deviceId") int deviceId, JsonDevice jdev) {
		Session ses = Hibernate.currentSession();

		if (Util.isNotNullAndEmpty(jdev.name))
			throw new ApiException("Attribute 'name' exists, but is empty. An empty name is not allowed for a device.");

		Device dev = (Device)ses.get(Device.class, deviceId);

		if (dev == null)
			throw new ApiException("No device with specified ID.");

		if (jdev.name != null)
			dev.setName(jdev.name);
		if (jdev.description != null)
			dev.setDescription(jdev.description);

		ses.save(dev);

		return Response.ok(new JsonVoid("Device updated successfully.")).build();
	}

	@GET
	@Path("/{deviceId}/delete")
	public Response delete(@PathParam("deviceId") int deviceId) {
		Session ses = Hibernate.currentSession();
		Device dev = (Device)ses.get(Device.class, deviceId);

		if (dev == null)
			throw new ApiException("No device with specified ID.");

		dev.preDelete();
		ses.delete(dev);

		return Response.ok(new JsonVoid("Device successfully deleted.")).build();
	}

	@GET
	@Path("/{deviceId}/merge")
	public Response merge(@PathParam("deviceId") int deviceId, @QueryParam("mergewith") int mergeWithDeviceId) {
		Session ses = Hibernate.currentSession();

		Device dev = (Device)ses.get(Device.class, deviceId);
		Device mergeWithDev = (Device)ses.get(Device.class, mergeWithDeviceId);

		if (dev == null)
			throw new ApiException("No device with specified ID.");
		if (mergeWithDev == null)
			throw new ApiException("No merge-with device with specified ID.");

		if (!dev.getType().equals(mergeWithDev.getType()))
			throw new ApiException("Device type ('" + dev.getType() + "') has to be the same as merge-with device type ('" + mergeWithDev.getType() + "'.");

		// we do this first, cause if this fails we don't want to move the history points
		mergeWithDev.preDelete();

		// nope, addAll on the list does not work
		for (HistoryPoint p : mergeWithDev.getHistoryPoints()) {
			p.setDevice(dev);
			dev.getHistoryPoints().add(p);
		}
		mergeWithDev.getHistoryPoints().clear();

		ses.flush();
		ses.delete(mergeWithDev);

		return Response.ok(new JsonVoid("Merge-with device successfully merged into device.")).build();
	}

	@Path("/mock")
	public Class<DeviceMockResource> continueInMock() {
		return DeviceMockResource.class;
	}

	@Path("tellstick")
	public Class<DeviceTellStickResource> continueInTellStick() {
		return DeviceTellStickResource.class;
	}

	@Path("{deviceId}/tellstick")
	public Class<DeviceTellStickResource> continueInTellStickWithId() {
		return DeviceTellStickResource.class;
	}

	@Path("{deviceId}/channel")
	public Class<DeviceChannelResource> continueInChannel() {
		return DeviceChannelResource.class;
	}
}
