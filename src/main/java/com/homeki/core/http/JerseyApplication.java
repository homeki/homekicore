package com.homeki.core.http;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.homeki.core.http.filters.CacheControlResponseFilter;
import com.homeki.core.http.filters.CrossOriginResourceSharingResponseFilter;
import com.homeki.core.http.filters.LogRequestFilter;
import com.homeki.core.http.rest.ServerResource;
import com.homeki.core.logging.L;
import com.homeki.core.main.Configuration;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class JerseyApplication extends ResourceConfig {
	public JerseyApplication() {
		if (Configuration.ENABLE_CORS_HEADERS) {
			L.i("CORS headers enabled.");
			register(CrossOriginResourceSharingResponseFilter.class);
		}

		register(CacheControlResponseFilter.class);
		register(LogRequestFilter.class);
		register(JacksonContextResolver.class);
		register(JacksonJsonProvider.class);
		register(ExceptionMapper.class);
		register(SseFeature.class);
		packages(true, ServerResource.class.getPackage().getName());
	}
}
