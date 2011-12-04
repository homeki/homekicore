package com.homeki.core.device.tellstick;

import java.util.List;

import com.homeki.core.device.Detector;
import com.homeki.core.device.Module;
import com.homeki.core.threads.ControlledThread;
import com.homeki.core.threads.TellStickCommandDispatcherThread;

public class TellStickModule implements Module {
	private ControlledThread thread;

	@Override
	public void construct(List<Detector> detectors) {
		TellStickNative.open();
		detectors.add(new TellStickDetector());
		thread = new TellStickCommandDispatcherThread(1000);
		thread.start();
	}

	@Override
	public void destruct() {
		thread.shutdown();
		TellStickNative.close();
	}
}
