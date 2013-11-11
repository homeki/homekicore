package com.homeki.core.http.restlets.event;

import com.homeki.core.events.EventQueue;
import com.homeki.core.events.SpecialValueChangedEvent;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonSpecialValue;
import com.homeki.core.main.Util;

public class EventSpecialValueRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		JsonSpecialValue jvalue = getJsonObject(c, JsonSpecialValue.class);
		
		if (Util.isNullOrEmpty(jvalue.source))
			throw new ApiException("Source cannot be empty.");
		if (jvalue.value == null)
			throw new ApiException("Value cannot be empty.");
		
		EventQueue.INSTANCE.add(SpecialValueChangedEvent.createCustomEvent(jvalue.source, jvalue.value));
		
		set200Response(c, msg("Special value changed event triggered successfully."));
	}
}
