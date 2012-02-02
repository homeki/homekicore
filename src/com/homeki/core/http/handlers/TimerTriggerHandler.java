package com.homeki.core.http.handlers;

import java.util.StringTokenizer;

import org.hibernate.Session;

import com.homeki.core.device.TimerTrigger;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonTimerTrigger;
import com.homeki.core.storage.Hibernate;

public class TimerTriggerHandler extends HttpHandler {
	public enum Actions {
		ADD, GET, BAD_ACTION, SET
	}
	
	@Override
	protected void handle(String method, StringTokenizer path) {
		path.nextToken(); // dismiss "trigger"
		path.nextToken(); // dismiss "timer"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(path.nextToken().toUpperCase());
		} catch (Exception ex) {}
		
		switch (action) {
		case ADD:
			resolveAdd();
			break;
		case SET:
			resolveSet();
			break;
		case GET:
			resolveGet();
			break;
		default:
			sendString(404, "No such action, " + action + ".");
			break;
		}
	}

	private void resolveSet() {
		int id = getIntParameter("triggerid");
		
		if (id == -1)
			return;
		
		String post = getPost();
		JsonTimerTrigger triggerTimer = gson.fromJson(post, JsonTimerTrigger.class);
		
		Session session = Hibernate.openSession();
		
		TimerTrigger trigger = (TimerTrigger) session.get(TimerTrigger.class, id);
		trigger.setName(triggerTimer.name);
		trigger.setValue(triggerTimer.newValue);
		trigger.setDays(triggerTimer.days);
		trigger.setRepeatType(triggerTimer.repeatType);
		trigger.setSecondsFromMidnight(triggerTimer.time);
		session.save(trigger);
		
		Hibernate.closeSession(session);
	}

	private void resolveAdd() {
		String post = getPost();
		JsonTimerTrigger triggerTimer = gson.fromJson(post, JsonTimerTrigger.class);
		
		Session session = Hibernate.openSession();
		
		TimerTrigger trigger = new TimerTrigger();
		trigger.setName(triggerTimer.name);
		trigger.setValue(triggerTimer.newValue);
		trigger.setDays(triggerTimer.days);
		trigger.setRepeatType(triggerTimer.repeatType);
		trigger.setSecondsFromMidnight(triggerTimer.time);
		session.save(trigger);
		
		Hibernate.closeSession(session);
	}
	
	private void resolveGet() {
		int id = getIntParameter("triggerid");
		
		if (id == -1)
			return;
		
		Session session = Hibernate.openSession();
		
		TimerTrigger trigger = (TimerTrigger) session.get(TimerTrigger.class, id);
		
		if (trigger == null) {
			sendString(405, "No timer trigger with specified ID.");
			return;
		}
		
		JsonTimerTrigger restrigger = new JsonTimerTrigger();
		restrigger.id = id;
		restrigger.name = trigger.getName();
		restrigger.newValue = trigger.getValue();
		restrigger.days = trigger.getDays();
		restrigger.repeatType = trigger.getRepeatType();
		restrigger.time = trigger.getSecondsFromMidnight();
		
		sendString(200, gson.toJson(restrigger));
		
		Hibernate.closeSession(session);
	}
}
