package com.homeki.core.actions;

import com.google.gson.Gson;
import com.homeki.core.device.Device;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.json.JsonChangeChannelValueAction;
import com.homeki.core.http.json.JsonSendMailAction;
import com.homeki.core.http.json.JsonTriggerActionGroupAction;
import com.homeki.core.main.Util;
import com.homeki.core.storage.Hibernate;

public class ActionParser {
	private static final Gson gson = Util.constructGson();
	
	public static Action createAction(String type, String json) {
		if (type.equals("changechannelvalue"))
			return createChangeChannelValue(json);
		else if (type.equals("triggeractiongroup"))
			return createTriggerActionGroup(json);
		else if (type.equals("sendmail"))
			return createSendMail(json);
		else
			throw new ApiException("No such action type.");
	}
	
	public static void updateAction(Action action, String json) {
		if (action instanceof ChangeChannelValueAction)
			updateChangeChannelValue(json, (ChangeChannelValueAction)action);
		else if (action instanceof TriggerActionGroupAction)
			updateTriggerActionGroup(json, (TriggerActionGroupAction)action);
		else if (action instanceof SendMailAction)
			updateSendMail(json, (SendMailAction)action);
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
	
	private static void updateTriggerActionGroup(String json, TriggerActionGroupAction action) {
		JsonTriggerActionGroupAction jact = gson.fromJson(json, JsonTriggerActionGroupAction.class);
		
		if (jact.actionGroupId != null) {
			ActionGroup actionGroup = (ActionGroup)Hibernate.currentSession().get(ActionGroup.class, jact.actionGroupId);
			
			if (actionGroup == null || !actionGroup.isExplicit())
				throw new ApiException("Could not load new action group from action group ID.");
			
			action.setActionGroup(actionGroup);
		}
	}
	
	private static void updateSendMail(String json, SendMailAction action) {
		JsonSendMailAction jact = gson.fromJson(json, JsonSendMailAction.class);
		
		if (jact.subject != null)
			action.setSubject(jact.subject);
		if (jact.recipients != null)
			action.setRecipients(jact.recipients);
		if (jact.text != null)
			action.setText(jact.text);
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
	
	private static Action createTriggerActionGroup(String json) {
		JsonTriggerActionGroupAction jact = gson.fromJson(json, JsonTriggerActionGroupAction.class);
		
		if (jact.actionGroupId == null)
			throw new ApiException("Missing action group ID.");
		
		ActionGroup actionGroup = (ActionGroup)Hibernate.currentSession().get(ActionGroup.class, jact.actionGroupId);
		
		if (actionGroup == null || !actionGroup.isExplicit())
			throw new ApiException("Could not load action group from action group ID.");
			
		return new TriggerActionGroupAction(actionGroup);
	}
	
	private static Action createSendMail(String json) {
		JsonSendMailAction jact = gson.fromJson(json, JsonSendMailAction.class);
		
		if (jact.recipients == null)
			throw new ApiException("Missing to (recipients).");
		if (jact.subject == null)
			throw new ApiException("Missing subject.");
		if (jact.text == null)
			throw new ApiException("Missing text.");
		
		return new SendMailAction(jact.subject, jact.recipients, jact.text);
	}
}
