package com.homeki.core.http.handlers;

import java.util.List;

import com.homeki.core.device.Device;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonDevice;
import com.homeki.core.http.json.JsonTrigger;
import com.homeki.core.triggers.Trigger;

public class TriggerHandler extends HttpHandler {
	public enum Actions {
		LIST, ADD, BAD_ACTION
	}
	
	@Override
	protected void handle(Container c) throws Exception {
		c.path.nextToken(); // dismiss "trigger"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(c.path.nextToken().toUpperCase());
		} catch (Exception ignore) {}
		
		switch (action) {
		case LIST:
			resolveList(c);
			break;
		case ADD:
			resolveAdd(c);
			break;
		default:
			throw new ApiException("No such URL/action: '" + action + "'.");
		}
	}
	
	private void resolveList(Container c) {
		@SuppressWarnings("unchecked")
		List<Trigger> list = c.session.createCriteria(Trigger.class).list();
		set200Response(c, gson.toJson(JsonTrigger.convertList(list)));
	}
	
	private void resolveAdd(Container c) {
		String post = getPost(c);
		
		JsonTrigger jtrigger = gson.fromJson(post, JsonTrigger.class);
		
		if (jtrigger.name == null || jtrigger.name.length() == 0)
			throw new ApiException("Trigger name cannot be empty");
		
		Trigger trigger = new Trigger();
		trigger.setName(jtrigger.name);
		c.session.save(trigger);
	}
}
