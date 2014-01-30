package com.homeki.core.http.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

public class CacheControlResponseFilter implements ContainerResponseFilter {
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		CacheControl cc = new CacheControl();
		cc.setMustRevalidate(true);
		cc.setNoCache(true);
		cc.setNoStore(true);

		MultivaluedMap<String, Object> headers = responseContext.getHeaders();
		headers.add("Cache-Control", cc.toString());
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
	}
}
