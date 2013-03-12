package com.homeki.core.http;

import org.restlet.Component;
import org.restlet.data.Protocol;

import com.homeki.core.logging.L;
import com.homeki.core.main.Module;

public class RestApiModule implements Module {
	private Component jsonRestletComponent;
	
	@Override
	public void construct() {
		try {
			jsonRestletComponent = new Component();
			jsonRestletComponent.getServers().add(Protocol.HTTP, 5000);
			jsonRestletComponent.getDefaultHost().attach(new JsonRestletApplication());
			jsonRestletComponent.start();
		} catch (Exception e) {
			L.e("Unknown exception starting JSON restlet HTTP server.", e);
			System.exit(-1);
		}
	}

	@Override
	public void destruct() {
		if (jsonRestletComponent != null) {
			try {
				jsonRestletComponent.stop();
			} catch (Exception e) {
				L.e("Failed to stop JSON restlet HTTP server.", e);
			}
		}
	}
}
