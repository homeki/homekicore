package com.homeki.core.http;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

public class HttpGetHandler implements HttpRequestHandler {
	@Override
	public void handle(HttpRequest req, HttpResponse res, HttpContext context) throws HttpException, IOException {
		
	}
}
