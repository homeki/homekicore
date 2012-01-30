package com.homeki.core.http;

import java.util.StringTokenizer;

import org.hibernate.Session;

import com.homeki.core.http.json.JsonTimerTrigger;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.entities.HTimerTrigger;

public class HttpTimerTriggerHandler extends HttpHandler {
	public enum Actions {
		ADD, GET, BAD_ACTION
	}
	
	public HttpTimerTriggerHandler(HttpApi api) {
		super(api);
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
		case GET:
			resolveGet();
			break;
		default:
			sendString(404, "No such action, " + action + ".");
			break;
		}
	}
	
	private void resolveAdd() {
		String post = getPost();
		JsonTimerTrigger triggerTimer = gson.fromJson(post, JsonTimerTrigger.class);
		
		Session session = Hibernate.openSession();
		
		HTimerTrigger trigger = new HTimerTrigger();
		trigger.setName(triggerTimer.name);
		trigger.setValue(triggerTimer.newValue);
		trigger.setDay(triggerTimer.days);
		trigger.setRepeat(triggerTimer.repeatType);
		trigger.setTime(triggerTimer.time);
		session.save(trigger);
		
		Hibernate.closeSession(session);
	}
	
	private void resolveGet() {
		int id = getIntParameter("triggerid");
		
		if (id == -1)
			return;
		
		Session session = Hibernate.openSession();
		
		HTimerTrigger trigger = (HTimerTrigger)session.get(HTimerTrigger.class, id);
		
		if (trigger == null) {
			sendString(405, "No timer trigger with specified ID.");
			return;
		}
		
		JsonTimerTrigger restrigger = new JsonTimerTrigger();
		restrigger.id = id;
		restrigger.name = trigger.getName();
		restrigger.newValue = trigger.getValue();
		restrigger.days = trigger.getDay();
		restrigger.repeatType = trigger.getRepeat();
		restrigger.time = trigger.getTime();
		
		sendString(200, gson.toJson(restrigger));
		
		Hibernate.closeSession(session);
	}
}
