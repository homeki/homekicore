package com.homeki.core.device;

import java.util.List;

public interface Module {
	void construct(List<Detector> detectors);
	void destruct();
}
