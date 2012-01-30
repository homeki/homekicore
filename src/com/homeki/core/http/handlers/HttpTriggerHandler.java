package com.homeki.core.http.handlers;

import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.Session;

import com.homeki.core.http.HttpApi;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonTimerTrigger;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.entities.HTrigger;

public class HttpTriggerHandler extends HttpHandler {
	public enum Actions {
		LIST, LINK, UNLINK, DELETE, BAD_ACTION
	}
	
	public HttpTriggerHandler(HttpApi api) {
		super(api);
	}
	
	@Override
	protected void handle(String method, StringTokenizer path) {
		path.nextToken(); // dismiss "trigger"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(path.nextToken().toUpperCase());
		} catch (Exception ex) {}
		
		switch (action) {
		case LIST:
			resolveList();
			break;
		default:
			sendString(404, "No such action, " + action + ".");
			break;
		}
	}
	
	private void resolveList() {
		Session session = Hibernate.openSession();
		
		@SuppressWarnings("unchecked")
		List<HTrigger> list = session.createQuery("from HTrigger as t").list();
		
		sendString(200, gson.toJson(JsonTimerTrigger.convertList(list)));
		
		Hibernate.closeSession(session);
	}
}
