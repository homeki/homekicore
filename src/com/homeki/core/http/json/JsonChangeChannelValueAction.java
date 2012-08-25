package com.homeki.core.http.json;

import com.homeki.core.actions.Action;


public class JsonChangeChannelValueAction extends JsonAction {
	public Integer deviceId;
	public Integer channel;
	public Number value;
	
	public JsonChangeChannelValueAction(Action action) {
		super(action);
	}
}
