package com.homeki.core.actions;

import com.homeki.core.device.Device;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.json.actions.JsonAction;
import com.homeki.core.http.json.actions.JsonChangeChannelValueAction;
import com.homeki.core.http.json.actions.JsonSendMailAction;
import com.homeki.core.http.json.actions.JsonTriggerActionGroupAction;
import com.homeki.core.storage.Hibernate;

public class ActionParser {
	public static Action createAction(JsonAction jact) {
		if (jact instanceof JsonChangeChannelValueAction)
			return createChangeChannelValue((JsonChangeChannelValueAction) jact);
		else if (jact instanceof JsonTriggerActionGroupAction)
			return createTriggerActionGroup((JsonTriggerActionGroupAction)jact);
		else if (jact instanceof JsonSendMailAction)
			return createSendMail((JsonSendMailAction)jact);
		else
			throw new ApiException("No such action type.");
	}
	
	public static void updateAction(Action action, JsonAction jact) {
		if (action instanceof ChangeChannelValueAction)
			updateChangeChannelValue((JsonChangeChannelValueAction)jact, (ChangeChannelValueAction)action);
		else if (action instanceof TriggerActionGroupAction)
			updateTriggerActionGroup((JsonTriggerActionGroupAction)jact, (TriggerActionGroupAction)action);
		else if (action instanceof SendMailAction)
			updateSendMail((JsonSendMailAction)jact, (SendMailAction)action);
	}

	private static void updateChangeChannelValue(JsonChangeChannelValueAction jact, ChangeChannelValueAction action) {
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
	
	private static void updateTriggerActionGroup(JsonTriggerActionGroupAction jact, TriggerActionGroupAction action) {
		if (jact.actionGroupId != null) {
			ActionGroup actionGroup = (ActionGroup)Hibernate.currentSession().get(ActionGroup.class, jact.actionGroupId);
			
			if (actionGroup == null || !actionGroup.isExplicit())
				throw new ApiException("Could not load new action group from action group ID.");
			
			action.setActionGroup(actionGroup);
		}
	}
	
	private static void updateSendMail(JsonSendMailAction jact, SendMailAction action) {
		if (jact.subject != null)
			action.setSubject(jact.subject);
		if (jact.recipients != null)
			action.setRecipients(jact.recipients);
		if (jact.text != null)
			action.setText(jact.text);
	}
	
	private static Action createChangeChannelValue(JsonChangeChannelValueAction jact) {
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
	
	private static Action createTriggerActionGroup(JsonTriggerActionGroupAction jact) {
		if (jact.actionGroupId == null)
			throw new ApiException("Missing action group ID.");
		
		ActionGroup actionGroup = (ActionGroup)Hibernate.currentSession().get(ActionGroup.class, jact.actionGroupId);
		
		if (actionGroup == null || !actionGroup.isExplicit())
			throw new ApiException("Could not load action group from action group ID.");
			
		return new TriggerActionGroupAction(actionGroup);
	}
	
	private static Action createSendMail(JsonSendMailAction jact) {
		if (jact.recipients == null)
			throw new ApiException("Missing to (recipients).");
		if (jact.subject == null)
			throw new ApiException("Missing subject.");
		if (jact.text == null)
			throw new ApiException("Missing text.");
		
		return new SendMailAction(jact.subject, jact.recipients, jact.text);
	}
}
