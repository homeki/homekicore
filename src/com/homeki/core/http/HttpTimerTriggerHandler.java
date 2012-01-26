package com.homeki.core.http;

import java.util.StringTokenizer;

import org.hibernate.Session;

import com.homeki.core.http.json.JsonTriggerTimer;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.entities.HTimerCondition;
import com.homeki.core.storage.entities.HTrigger;

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
		
		HTimerCondition condition = new HTimerCondition();
		condition.setDay(triggerTimer.days);
		condition.setRepeat(triggerTimer.repeatType);
		condition.setTime(triggerTimer.time);
		session.save(condition);
		
		HTrigger trigger = new HTrigger();
		trigger.setName(triggerTimer.name);
		trigger.setValue(triggerTimer.newValue);
		trigger.setCondition(condition);
		session.save(trigger);
		
		session.close();
	}
	
	private void resolveGet() {
		int id = getIntParameter("triggerid");
		
		if (id == -1)
			return;
		
		Session session = Hibernate.openSession();
		
		HTrigger trigger = (HTrigger)session.createQuery("from HTrigger as trig where trig.id = ?").setInteger(0, id)
				.uniqueResult();
		
		JsonTriggerTimer restrigger = new JsonTriggerTimer();
		restrigger.id = id;
		restrigger.name = trigger.getName();
		restrigger.newValue = trigger.getValue();
		
		try {
			HTimerCondition condition = (HTimerCondition)trigger.getCondition();
			restrigger.days = condition.getDay();
			restrigger.repeatType = condition.getRepeat();
			restrigger.time = condition.getTime();
			
			sendString(200, gson.toJson(restrigger));
		} catch (ClassCastException ex) {
			sendString(405, "Tried to fetch other trigger as timer trigger.");
		}
	}
}
