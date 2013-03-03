package com.homeki.core.actions;

import com.google.gson.Gson;
import com.homeki.core.device.Device;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.json.JsonChangeChannelValueAction;
import com.homeki.core.main.Util;
import com.homeki.core.storage.Hibernate;

public class ActionParser {
	private static final Gson gson = Util.constructGson();
	
	public static Action createAction(String type, String json) {
		if (type.equals("changechannelvalue"))
			return createChangeChannelValue(json);
		else
			throw new ApiException("No such action type.");
	}
	
	public static void updateAction(Action action, String json) {
		if (action instanceof ChangeChannelValueAction)
			updateChangeChannelValue(json, (ChangeChannelValueAction)action);
	}
	
	private static void updateChangeChannelValue(String json, ChangeChannelValueAction action) {
		JsonChangeChannelValueAction jact = gson.fromJson(json, JsonChangeChannelValueAction.class);
		
		if (jact.deviceId != null) {
			Device dev = (Device)Hibernate.currentSession().get(Device.class, jact.deviceId);
			
			if (dev == null)
				throw new ApiException("Could not load new device from device ID.");
			
			action.setDevice(dev);
		}
		if (jact.channel != null) {
			action.setChannel(jact.channel);
		}
		if (jact.value != null) {
			action.setValue(jact.value);
		}
	}
	
	private static Action createChangeChannelValue(String json) {
		JsonChangeChannelValueAction jact = gson.fromJson(json, JsonChangeChannelValueAction.class);
		
		if (jact.value == null)
			throw new ApiException("Missing number.");
		if (jact.deviceId == null)
			throw new ApiException("Missing deviceId.");
		if (jact.channel == null)
			throw new ApiException("Missing channel.");
		
		Device dev = (Device)Hibernate.currentSession().get(Device.class, jact.deviceId);
		
		if (dev == null)
			throw new ApiException("Could not load device from device ID.");
		
		// TODO: add more validation here (does device have channel, etc)
			
		return new ChangeChannelValueAction(dev, jact.channel, jact.value);
	}
}
