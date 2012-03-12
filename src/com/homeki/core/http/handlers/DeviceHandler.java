package com.homeki.core.http.handlers;

import java.util.List;

import com.homeki.core.device.Device;
import com.homeki.core.device.HistoryPoint;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonDevice;

public class DeviceHandler extends HttpHandler {
	public enum Actions {
		LIST, SET, DELETE, MERGE, BAD_ACTION
	}
	
	@Override
	protected void handle(Container c) {
		c.path.nextToken(); // dismiss "device"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(c.path.nextToken().toUpperCase());
		} catch (Exception ignore) {}
		
		switch (action) {
		case LIST:
			resolveList(c);
			break;
		case SET:
			resolveSet(c);
			break;
		case DELETE:
			resolveDelete(c);
			break;
		case MERGE:
			resolveMerge(c);
			break;
		default:
			throw new ApiException("No such URL/action: '" + action + "'.");
		}
	}
	
	private void resolveSet(Container c) {
		int id = getIntParameter(c, "deviceid");
		String post = getPost(c);
		
		JsonDevice jdev = gson.fromJson(post, JsonDevice.class);
		
		Device dev = (Device)c.ses.get(Device.class, id);
		
		if (dev == null)
			throw new ApiException("No device with specified ID.");
		
		if (jdev.name != null)
			dev.setName(jdev.name);
		if (jdev.description != null)
			dev.setDescription(jdev.description);
		
		c.ses.save(dev);
		
		set200Response(c, "Device updated successfully.");
	}
	
	private void resolveList(Container c) {
		@SuppressWarnings("unchecked")
		List<Device> list = c.ses.createCriteria(Device.class).list();
		set200Response(c, gson.toJson(JsonDevice.convertList(list)));
	}
	
	private void resolveMerge(Container c) {
		int targetDeviceId = getIntParameter(c, "targetdeviceid");
		int sourceDeviceId = getIntParameter(c, "sourcedeviceid");
		
		Device targetDev = (Device)c.ses.get(Device.class, targetDeviceId);
		Device sourceDev = (Device)c.ses.get(Device.class, sourceDeviceId);
		
		if (targetDev == null)
			throw new ApiException("No target device with specified ID.");
		if (sourceDev == null)
			throw new ApiException("No source device with specified ID.");
		
		if (!targetDev.getType().equals(sourceDev.getType()))
			throw new ApiException("Source device type ('" + sourceDev.getType() + "') has to be the same as target device type ('" + targetDev.getType() + "'.");
		
		// we do this first, cause if this fails we don't want to move the history points
		sourceDev.preDelete();
		
		// nope, addAll on the list does not work
		for (HistoryPoint p : sourceDev.getHistoryPoints()) {
			p.setDevice(targetDev);
			targetDev.getHistoryPoints().add(p);
		}
		sourceDev.getHistoryPoints().clear();
		
		c.ses.delete(sourceDev);
		
		set200Response(c, "Source device successfully merged into target device.");
	}
	
	private void resolveDelete(Container c) {
		int id = getIntParameter(c, "deviceid");
		
		Device dev = (Device)c.ses.get(Device.class, id);
		
		if (dev == null)
			throw new ApiException("No device with specified ID.");
		
		dev.preDelete();
		c.ses.delete(dev);
		
		set200Response(c, "Device successfully deleted.");
	}
}
