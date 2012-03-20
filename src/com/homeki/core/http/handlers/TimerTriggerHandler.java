package com.homeki.core.http.handlers;

import com.homeki.core.device.TimerTrigger;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonTimerTrigger;

public class TimerTriggerHandler extends HttpHandler {
	public enum Actions {
		ADD, GET, SET, BAD_ACTION
	}
	
	@Override
	protected void handle(Container c) {
		c.path.nextToken(); // dismiss "trigger"
		c.path.nextToken(); // dismiss "timer"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(c.path.nextToken().toUpperCase());
		} catch (Exception e) {}
		
		switch (action) {
		case ADD:
			resolveAdd(c);
			break;
		case SET:
			resolveSet(c);
			break;
		case GET:
			resolveGet(c);
			break;
		default:
			throw new ApiException("No such URL/action: '" + action + "'.");
		}
	}

	private void resolveSet(Container c) {
		int id = getIntParameter(c, "triggerid");
		
		String post = getPost(c);
		JsonTimerTrigger triggerTimer = gson.fromJson(post, JsonTimerTrigger.class);
		
		TimerTrigger trigger = (TimerTrigger)c.session.get(TimerTrigger.class, id);
		trigger.setName(triggerTimer.name);
		trigger.setNewValue(triggerTimer.newValue);
		trigger.setDays(triggerTimer.days);
		trigger.setRepeatType(triggerTimer.repeatType);
		trigger.setSecondsFromMidnight(triggerTimer.time);
		c.session.save(trigger);
		
		set200Response(c, "Trigger updated successfully.");
	}

	private void resolveAdd(Container c) {
		String post = getPost(c);
		JsonTimerTrigger triggerTimer = gson.fromJson(post, JsonTimerTrigger.class);
		
		TimerTrigger trigger = new TimerTrigger();
		trigger.setName(triggerTimer.name);
		trigger.setNewValue(triggerTimer.newValue);
		trigger.setDays(triggerTimer.days);
		trigger.setRepeatType(triggerTimer.repeatType);
		trigger.setSecondsFromMidnight(triggerTimer.time);
		c.session.save(trigger);
		
		JsonTimerTrigger newid = new JsonTimerTrigger();
		newid.id = trigger.getId();
		
		set200Response(c, gson.toJson(newid));
	}
	
	private void resolveGet(Container c) {
		int id = getIntParameter(c, "triggerid");
		
		TimerTrigger trigger = (TimerTrigger)c.session.get(TimerTrigger.class, id);
		
		if (trigger == null)
			throw new ApiException("No timer trigger with specified ID.");
		
		JsonTimerTrigger restrigger = new JsonTimerTrigger();
		restrigger.id = id;
		restrigger.name = trigger.getName();
		restrigger.newValue = trigger.getNewValue();
		restrigger.days = trigger.getDays();
		restrigger.repeatType = trigger.getRepeatType();
		restrigger.time = trigger.getSecondsFromMidnight();
		
		set200Response(c, gson.toJson(restrigger));
	}
}
