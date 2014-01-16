package com.homeki.core.web;

import org.glassfish.jersey.server.ResourceConfig;

public class JerseyApplication extends ResourceConfig {
	public JerseyApplication() {
		packages(true, "com.homeki.core.web.rest");
	}
}
