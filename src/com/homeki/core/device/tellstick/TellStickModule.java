package com.homeki.core.device.tellstick;

import java.util.List;

import com.homeki.core.device.Detector;
import com.homeki.core.main.Module;

public class TellStickModule implements Module {
	@Override
	public void construct(List<Detector> detectors) {
		TellStickNative.open();
		detectors.add(new TellStickDetector());
	}

	@Override
	public void destruct() {
		TellStickNative.close();
	}
}
