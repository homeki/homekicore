package com.homeki.core.device.zwave;

import com.homeki.core.main.Module;

public class ZWaveModule implements Module {
	@Override
	public void construct() {
		ZWaveApi.INSTANCE.open();
	}

	@Override
	public void destruct() {
		ZWaveApi.INSTANCE.close();
	}
}
