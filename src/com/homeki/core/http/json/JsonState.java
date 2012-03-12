package com.homeki.core.http.json;

import com.homeki.core.device.Device;
import com.homeki.core.device.mock.MockDimmer;
import com.homeki.core.device.tellstick.TellStickDimmer;

public class JsonState {
	public static JsonState create(Device d) {
		if (d instanceof TellStickDimmer)
			return new TellStickDimmerJsonState((TellStickDimmer)d);
		else if (d instanceof MockDimmer)
			return new MockDimmerJsonState((MockDimmer)d);
		else
			return new ObjectJsonState(d);
	}
}
