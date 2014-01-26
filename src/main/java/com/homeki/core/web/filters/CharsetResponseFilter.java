package com.homeki.core.web.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

public class CharsetResponseFilter implements ContainerResponseFilter {
	@Override
	public void filter(ContainerRequestContext request, ContainerResponseContext response) {
		String contentType = response.getMediaType().toString();
		if (!contentType.contains("charset")) {
			contentType = contentType + ";charset=utf-8";
			response.getHeaders().putSingle("Content-Type", contentType);
		}
	}
}