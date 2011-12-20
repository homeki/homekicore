package com.homeki.core.device.mock;

import java.util.List;

import com.homeki.core.device.Detector;
import com.homeki.core.main.Module;

public class MockModule implements Module {
	@Override
	public void construct(List<Detector> detectors) {
		detectors.add(new MockDetector());
	}
	
	@Override
	public void destruct() {
		
	}
}
