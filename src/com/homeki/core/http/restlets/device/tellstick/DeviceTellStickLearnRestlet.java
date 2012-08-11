package com.homeki.core.http.restlets.device.tellstick;

import com.homeki.core.device.tellstick.TellStickDevice;
import com.homeki.core.device.tellstick.TellStickLearnable;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.KiContainer;
import com.homeki.core.http.KiRestlet;

public class DeviceTellStickLearnRestlet extends KiRestlet {
	@Override
	protected void handle(KiContainer c) {
		int id = getInt(c, "deviceid");
		
		TellStickDevice dev = (TellStickDevice)c.ses.get(TellStickDevice.class, id);
		
		if (dev == null)
			throw new ApiException("No TellStick device with specified ID found.");
		
		if (!(dev instanceof TellStickLearnable))
			throw new ApiException("Specified TellStick device does not support learn.");
		
		((TellStickLearnable)dev).learn();
		
		set200Response(c, "Learn command sent successfully.");
	}
}
