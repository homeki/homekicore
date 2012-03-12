package com.homeki.core.http.handlers;

import java.util.Date;
import java.util.List;

import com.homeki.core.device.Device;
import com.homeki.core.device.HistoryPoint;
import com.homeki.core.device.abilities.Settable;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonHistoryPoint;

public class DeviceStateHandler extends HttpHandler {
	public enum Actions {
		SET, LIST, BAD_ACTION
	}
	
	@Override
	protected void handle(Container c) {
		c.path.nextToken(); // dismiss "device"
		c.path.nextToken(); // dismiss "state"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(c.path.nextToken().toUpperCase());
		} catch (Exception ignore) {}
		
		switch (action) {
		case SET:
			resolveSet(c);
			break;
		case LIST:
			resolveList(c);
			break;
		default:
			throw new ApiException("No such URL/action: '" + action + "'.");
		}
	}
	
	private void resolveSet(Container c) {
		int id = getIntParameter(c, "deviceid");
		int channel = getOptionalIntParameter(c, "channel");
		int value = getIntParameter(c, "value");

		Device dev = (Device)c.ses.get(Device.class, id);
		
		if (dev == null)
			throw new ApiException("Could not load device from device ID.");
		
		if (channel == -1)
			channel = 0;
		
		if (!(dev instanceof Settable))
			throw new ApiException("Device with specified ID is not a settable.");

		((Settable)dev).set(channel, value);
		
		set200Response(c, "Device state successfully changed.");
	}
	
	private void resolveList(Container c) {
		int id = getIntParameter(c, "deviceid");
		int channel = getOptionalIntParameter(c, "channel");
		Date from = getDateParameter(c, "from");
		Date to = getDateParameter(c, "to");
		
		Device dev = (Device)c.ses.get(Device.class, id);
		
		if (dev == null)
			throw new ApiException("Could not load device from device ID.");
		
		if (channel == -1)
			channel = 0;
		
		@SuppressWarnings("unchecked")
		List<HistoryPoint> l = c.ses.createFilter(dev.getHistoryPoints(), "where channel = ? and registered between ? and ? order by registered desc")
				.setInteger(0, channel)
				.setTimestamp(1, from)
				.setTimestamp(2, to)
				.list();
		
		set200Response(c, gson.toJson(JsonHistoryPoint.convertList(l)));
	}
}
