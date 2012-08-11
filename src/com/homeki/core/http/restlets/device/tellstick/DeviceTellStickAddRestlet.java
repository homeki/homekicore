package com.homeki.core.http.restlets.device.tellstick;

import com.homeki.core.device.Device;
import com.homeki.core.device.tellstick.TellStickDimmer;
import com.homeki.core.device.tellstick.TellStickSwitch;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonDevice;
import com.homeki.core.http.json.JsonTellStickDevice;
import com.homeki.core.main.Setting;

public class DeviceTellStickAddRestlet extends KiRestlet {
	private static final int UNIT = 3;
	
	@Override
	protected void handle(Container c) {
		JsonTellStickDevice jsonDevice = getJsonObject(c, JsonTellStickDevice.class);
		
		if (jsonDevice.type == null)
			throw new ApiException("Missing required field 'type' in JSON.");
		
		int house;
		int unit;
		
		if (jsonDevice.house == null)
			house = Setting.getInt(c.ses, Setting.NEXT_HOUSE_KEY);
		else
			house = jsonDevice.house;
		
		if (jsonDevice.unit == null)
			unit = UNIT;
		else
			unit = jsonDevice.unit;
		
		Device dev;

		if (jsonDevice.type.equals("switch"))
			dev = new TellStickSwitch(false, house, unit);
		else if (jsonDevice.type.equals("dimmer"))
			dev = new TellStickDimmer(255, house, unit);
		else
			throw new ApiException("Did not recognize type '" + jsonDevice.type + "' as a valid TellStick type.");
		
		if (jsonDevice.name != null)
			dev.setName(jsonDevice.name);
		if (jsonDevice.description != null)
			dev.setDescription(jsonDevice.description);
		
		if (jsonDevice.house == null)
			Setting.putInt(c.ses, Setting.NEXT_HOUSE_KEY, house+1);
		
		c.ses.save(dev);
		
		JsonDevice newid = new JsonDevice();
		newid.id = dev.getId();
		
		set200Response(c, gson.toJson(newid));
	}
}
