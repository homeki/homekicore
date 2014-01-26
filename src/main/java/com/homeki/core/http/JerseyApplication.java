package com.homeki.core.http;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.homeki.core.http.filters.CharsetResponseFilter;
import com.homeki.core.http.filters.CrossOriginResourceSharingResponseFilter;
import com.homeki.core.http.filters.LogRequestFilter;
import com.homeki.core.http.rest.ServerResource;
import org.glassfish.jersey.server.ResourceConfig;

public class JerseyApplication extends ResourceConfig {
	public JerseyApplication() {
		register(CrossOriginResourceSharingResponseFilter.class);
		register(LogRequestFilter.class);
		register(CharsetResponseFilter.class);
		register(JacksonContextResolver.class);
		register(JacksonJsonProvider.class);
		register(ExceptionMapper.class);
		packages(true, ServerResource.class.getPackage().getName());
	}
}
