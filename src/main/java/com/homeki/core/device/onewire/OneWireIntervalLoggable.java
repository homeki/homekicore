package com.homeki.core.device.onewire;

import java.io.FileNotFoundException;

public interface OneWireIntervalLoggable {
	String getInternalId();
	void updateValue() throws FileNotFoundException;
}
