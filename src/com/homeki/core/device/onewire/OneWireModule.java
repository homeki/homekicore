package com.homeki.core.device.onewire;

import java.util.List;

import com.homeki.core.device.Detector;
import com.homeki.core.device.Module;
import com.homeki.core.main.ConfigurationFile;

public class OneWireModule implements Module {
	private String owRootPath;
	
	public OneWireModule(ConfigurationFile file) {
		this.owRootPath = file.getString("modules.onewire.path");
	}
	
	@Override
	public void construct(List<Detector> detectors) {
		detectors.add(new OneWireDetector(owRootPath));
	}
	
	@Override
	public void destruct() {
		
	}
}
