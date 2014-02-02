package com.homeki.core.http.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

public class CrossOriginResourceSharingResponseFilter implements ContainerResponseFilter {
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
		responseContext.getHeaders().add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		responseContext.getHeaders().add("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
	}
}
