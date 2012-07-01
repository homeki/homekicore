package com.homeki.core.http.handlers;

import com.homeki.core.device.Device;
import com.homeki.core.device.mock.MockDimmer;
import com.homeki.core.device.mock.MockSwitch;
import com.homeki.core.device.mock.MockThermometer;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonDevice;

public class DeviceMockHandler extends HttpHandler {
	public enum Actions {
		ADD, BAD_ACTION
	}
	
	@Override
	protected void handle(Container c) {
		c.path.nextToken(); // dismiss "device"
		c.path.nextToken(); // dismiss "mock"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(c.path.nextToken().toUpperCase());
		} catch (Exception e) {}
		
		switch (action) {
		case ADD:
			resolveAdd(c);
			break;
		default:
			throw new ApiException("No such URL/action.");
		}
	}
	
	private void resolveAdd(Container c) {
		String post = getPost(c);
		JsonDevice jsonDevice = gson.fromJson(post, JsonDevice.class);
		
		Device dev;

		if (jsonDevice.type.equals("switch"))
			dev = new MockSwitch(false);
		else if (jsonDevice.type.equals("dimmer"))
			dev = new MockDimmer(0);
		else if (jsonDevice.type.equals("thermometer"))
			dev = new MockThermometer(0.0);
		else
			throw new ApiException("Did not recognize type '" + jsonDevice.type + "' as a valid mock device type.");
		
		if (jsonDevice.name != null)
			dev.setName(jsonDevice.name);
		if (jsonDevice.description != null)
			dev.setDescription(jsonDevice.description);
		
		c.session.save(dev);
		
		JsonDevice newid = new JsonDevice();
		newid.id = dev.getId();
		
		set200Response(c, gson.toJson(newid));
	}
}
