package com.homeki.core.http.restlets.device.mock;

import com.homeki.core.device.Device;
import com.homeki.core.device.mock.MockDimmer;
import com.homeki.core.device.mock.MockSwitch;
import com.homeki.core.device.mock.MockThermometer;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.KiContainer;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonDevice;

public class DeviceMockAddRestlet extends KiRestlet {
	@Override
	protected void handle(KiContainer c) {
		JsonDevice jsonDevice = getJsonObject(c, JsonDevice.class);
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
		
		c.ses.save(dev);
		
		JsonDevice newid = new JsonDevice();
		newid.id = dev.getId();
		
		set200Response(c, gson.toJson(newid));
	}
}
