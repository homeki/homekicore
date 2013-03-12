package com.homeki.core.report;

import com.homeki.core.logging.L;
import com.homeki.core.main.Configuration;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.Module;

public class ReportModule implements Module {
	private ControlledThread reportThread;
	
	@Override
	public void construct() {
		if (Configuration.REPORTER_ENABLED) {
			try {
				reportThread = new ReportThread();
				reportThread.start();
			} catch (Exception e) {
				L.e("Could not start report thread.", e);
			}
		}
		else {
			L.w("Reporting disabled in configuration.");
		}
	}

	@Override
	public void destruct() {
		if (reportThread != null)
			reportThread.shutdown();
	}
}
