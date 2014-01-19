package com.homeki.core.web;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

public class JerseyApplication extends ResourceConfig {
	public JerseyApplication() {
		register(JacksonContextResolver.class);
		register(JacksonJsonProvider.class);
		packages(true, "com.homeki.core.web.rest");
	}
}
