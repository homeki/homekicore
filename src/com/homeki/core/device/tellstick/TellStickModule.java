package com.homeki.core.device.tellstick;

import java.util.List;

import com.homeki.core.device.Detector;
import com.homeki.core.main.L;
import com.homeki.core.main.Module;
import com.homeki.core.main.Monitor;
import com.homeki.core.threads.ControlledThread;

public class TellStickModule implements Module {
	private ControlledThread listenerThread;
	private Monitor monitor;
	
	public TellStickModule(Monitor monitor) {
		this.monitor = monitor;
	}
	
	
	@Override
	public void construct(List<Detector> detectors) {
		TellStickNative.open();
		L.i("Denna ska komma först!");
		detectors.add(new TellStickDetector());
		listenerThread = new TellStickListener(monitor);
		listenerThread.start();
	}

	@Override
	public void destruct() {
		TellStickNative.close();
	}
}
