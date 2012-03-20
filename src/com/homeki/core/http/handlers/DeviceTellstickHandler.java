package com.homeki.core.http.handlers;

import java.util.List;

import com.homeki.core.device.Device;
import com.homeki.core.device.tellstick.TellStickDevice;
import com.homeki.core.device.tellstick.TellStickDimmer;
import com.homeki.core.device.tellstick.TellStickLearnable;
import com.homeki.core.device.tellstick.TellStickSwitch;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonDevice;
import com.homeki.core.http.json.JsonTellStickDevice;
import com.homeki.core.main.Setting;

public class DeviceTellstickHandler extends HttpHandler {
	private static final String NEXT_HOUSE_KEY = "TELLSTICK_NEXT_HOUSE_VALUE";
	private static final int UNIT = 3;
	
	public enum Actions {
		ADD, LIST, LEARN, BAD_ACTION
	}
	
	@Override
	protected void handle(Container c) {
		c.path.nextToken(); // dismiss "device"
		c.path.nextToken(); // dismiss "tellstick"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(c.path.nextToken().toUpperCase());
		} catch (Exception e) {}
		
		switch (action) {
		case ADD:
			resolveAdd(c);
			break;
		case LIST:
			resolveList(c);
			break;
		case LEARN:
			resolveLearn(c);
			break;
		default:
			throw new ApiException("No such URL/action: '" + action + "'.");
		}
	}
	
	private void resolveAdd(Container c) {
		String post = getPost(c);
		JsonTellStickDevice jsonDevice = gson.fromJson(post, JsonTellStickDevice.class);
		
		int house;
		int unit;
		
		if (jsonDevice.house == null)
			house = Setting.getInt(c.ses, NEXT_HOUSE_KEY);
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
			Setting.putInt(c.ses, NEXT_HOUSE_KEY, house+1);
		
		c.ses.save(dev);
		
		JsonDevice newid = new JsonDevice();
		newid.id = dev.getId();
		
		set200Response(c, gson.toJson(newid));
	}
	
	private void resolveList(Container c) {
		@SuppressWarnings("unchecked")
		List<Device> list = c.ses.createCriteria(TellStickDevice.class).list();
		set200Response(c, gson.toJson(JsonDevice.convertList(list)));
	}
	
	private void resolveLearn(Container c) {
		int id = getIntParameter(c, "deviceid");
		
		TellStickDevice dev = (TellStickDevice)c.ses.get(TellStickDevice.class, id);
		
		if (dev == null)
			throw new ApiException("No TellStick device with specified ID found.");
		
		if (!(dev instanceof TellStickLearnable))
			throw new ApiException("Specified TellStick device does not support learn.");
		
		((TellStickLearnable)dev).learn();
		
		set200Response(c, "Learn command sent successfully.");
	}
}
