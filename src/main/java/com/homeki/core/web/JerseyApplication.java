package com.homeki.core.web;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.homeki.core.web.filters.CharsetResponseFilter;
import com.homeki.core.web.filters.CrossOriginResourceSharingResponseFilter;
import com.homeki.core.web.filters.LogRequestFilter;
import org.glassfish.jersey.server.ResourceConfig;

public class JerseyApplication extends ResourceConfig {
	public JerseyApplication() {
		register(CrossOriginResourceSharingResponseFilter.class);
		register(LogRequestFilter.class);
		register(CharsetResponseFilter.class);
		register(JacksonContextResolver.class);
		register(JacksonJsonProvider.class);
		register(ExceptionMapper.class);
		packages(true, "com.homeki.core.web.rest");
	}
}
