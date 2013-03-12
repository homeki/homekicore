package com.homeki.core.generators;

import com.homeki.core.logging.L;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.Module;

public class GeneratorModule implements Module {
	private ControlledThread clockGeneratorThread;
	
	@Override
	public void construct() {
		try {
			clockGeneratorThread = new ClockGeneratorThread();
			clockGeneratorThread.start();
		} catch (Exception e) {
			L.e("Could not start ClockGeneratorThread.", e);
		}
	}

	@Override
	public void destruct() {
		if (clockGeneratorThread != null)
			clockGeneratorThread.shutdown();
	}
}
