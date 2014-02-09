package com.homeki.core.http.rest;

import com.homeki.core.device.Device;
import com.homeki.core.device.HistoryPoint;
import com.homeki.core.device.mock.MockDimmer;
import com.homeki.core.device.mock.MockModule;
import com.homeki.core.device.mock.MockSwitch;
import com.homeki.core.device.mock.MockThermometer;
import com.homeki.core.device.tellstick.TellStickDevice;
import com.homeki.core.device.tellstick.TellStickDimmer;
import com.homeki.core.device.tellstick.TellStickLearnable;
import com.homeki.core.device.tellstick.TellStickSwitch;
import com.homeki.core.http.ApiException;
import com.homeki.core.json.devices.JsonDevice;
import com.homeki.core.json.devices.JsonMockDevice;
import com.homeki.core.json.devices.JsonTellStickDevice;
import com.homeki.core.json.JsonVoid;
import com.homeki.core.main.Setting;
import com.homeki.core.main.Util;
import com.homeki.core.storage.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/devices")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DeviceResource {
	private static final int TELLSTICK_UNIT = 3;

	@POST
	public Response add(JsonDevice jdev) {
		if (Util.isNullOrEmpty(jdev.type))
			throw new ApiException("Missing required field 'type' in JSON.");
		if (Util.isNullOrEmpty(jdev.name))
			throw new ApiException("Missing required field 'name' in JSON.");

		if (jdev instanceof JsonTellStickDevice)
			return addTellStickDevice((JsonTellStickDevice)jdev);
		else if (jdev instanceof JsonMockDevice)
			return addMockDevice((JsonMockDevice)jdev);
		else
			throw new ApiException("No such device vendor.");
	}

	private Response addMockDevice(JsonMockDevice jdev) {
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

		return Response.ok(dev.toJson()).build();
	}

	private Response addTellStickDevice(JsonTellStickDevice jdev) {
		Session ses = Hibernate.currentSession();

		int house;
		int unit;

		if (jdev.house == null)
			house = Setting.getInt(ses, Setting.NEXT_HOUSE_KEY);
		else
			house = jdev.house;

		if (jdev.unit == null)
			unit = TELLSTICK_UNIT;
		else
			unit = jdev.unit;

		Device dev;

		if (jdev.type.equals("switch"))
			dev = new TellStickSwitch(false, house, unit);
		else if (jdev.type.equals("dimmer"))
			dev = new TellStickDimmer(255, house, unit);
		else
			throw new ApiException("Did not recognize type '" + jdev.type + "' as a valid TellStick type.");

		if (jdev.name != null)
			dev.setName(jdev.name);
		if (jdev.description != null)
			dev.setDescription(jdev.description);

		if (jdev.house == null)
			Setting.putInt(ses, Setting.NEXT_HOUSE_KEY, house+1);

		ses.save(dev);

		return Response.ok(dev.toJson()).build();
	}

	@GET
	@Path("/tellstick/learn")
	public Response learn(@PathParam("deviceId") int deviceId) {
		TellStickDevice dev = (TellStickDevice)Hibernate.currentSession().get(TellStickDevice.class, deviceId);

		if (dev == null)
			throw new ApiException("No TellStick device with specified ID found.");
		if (!(dev instanceof TellStickLearnable))
			throw new ApiException("Specified TellStick device does not support learn.");

		((TellStickLearnable)dev).learn();

		return Response.ok(new JsonVoid("Learn command sent successfully.")).build();
	}

	@GET
	public Response list() {
		List<Device> list = Hibernate.currentSession().createCriteria(Device.class).addOrder(Order.asc("name")).list();
		return Response.ok(JsonDevice.convertList(list)).build();
	}

	@GET
	@Path("/{deviceId}")
	public Response get(@PathParam("deviceId") int deviceId) {
		Device dev = (Device)Hibernate.currentSession().get(Device.class, deviceId);

		if (dev == null)
			throw new ApiException("No device with specified ID.");

		return Response.ok(dev.toJson()).build();
	}

	@POST
	@Path("/{deviceId}")
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

		return Response.ok(dev.toJson()).build();
	}

	@DELETE
	@Path("/{deviceId}")
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

	@Path("/{deviceId}/channels")
	public Class<DeviceChannelResource> continueInChannel() {
		return DeviceChannelResource.class;
	}
}
