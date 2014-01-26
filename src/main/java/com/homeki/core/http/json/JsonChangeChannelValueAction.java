package com.homeki.core.http.json;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.homeki.core.actions.ChangeChannelValueAction;

@JsonTypeName("changechannelvalue")
public class JsonChangeChannelValueAction extends JsonAction {
	public Integer deviceId;
	public Integer channel;
	public Number value;

	public JsonChangeChannelValueAction() {

	}
	
	public JsonChangeChannelValueAction(ChangeChannelValueAction action) {
		super(action);
		this.deviceId = action.getDeviceId();
		this.channel = action.getChannel();
		this.value = action.getValue();
	}
}
