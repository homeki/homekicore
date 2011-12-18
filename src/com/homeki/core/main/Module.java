package com.homeki.core.main;

import java.util.List;

import com.homeki.core.device.Detector;

public interface Module {
	void construct(List<Detector> detectors);
	void destruct();
}
