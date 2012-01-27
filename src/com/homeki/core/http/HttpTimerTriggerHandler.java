package com.homeki.core.http;

import java.util.StringTokenizer;

import org.hibernate.Session;

import com.homeki.core.http.json.JsonTriggerTimer;
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
		JsonTriggerTimer triggerTimer = gson.fromJson(post, JsonTriggerTimer.class);
		
		Session session = Hibernate.openSession();
		
		HTimerTrigger trigger = new HTimerTrigger();
		trigger.setName(triggerTimer.name);
		trigger.setValue(triggerTimer.newValue);
		trigger.setDay(triggerTimer.days);
		trigger.setRepeat(triggerTimer.repeatType);
		trigger.setTime(triggerTimer.time);
		session.save(trigger);
		
		session.close();
	}
	
	private void resolveGet() {
		int id = getIntParameter("triggerid");
		
		if (id == -1)
			return;
		
		Session session = Hibernate.openSession();
		
		HTimerTrigger trigger = (HTimerTrigger)session.get(HTimerTrigger.class, id);
		
		JsonTriggerTimer restrigger = new JsonTriggerTimer();
		restrigger.id = id;
		restrigger.name = trigger.getName();
		restrigger.newValue = trigger.getValue();
		restrigger.days = trigger.getDay();
		restrigger.repeatType = trigger.getRepeat();
		restrigger.time = trigger.getTime();
		
		sendString(200, gson.toJson(restrigger));
		
		session.close();
	}
}
