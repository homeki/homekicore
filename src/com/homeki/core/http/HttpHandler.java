package com.homeki.core.http;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import com.homeki.core.log.L;

public abstract class HttpHandler implements HttpRequestHandler {
	protected HttpApi api;
	protected HttpRequest request;
	protected HttpResponse response;
	protected HttpContext context;
	private List<NameValuePair> queryString;
	
	public HttpHandler(HttpApi api) {
		this.api = api;
	}
	
	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
		this.request = request;
		this.response = response;
		this.context = context;

		String path = request.getRequestLine().getUri();
		String method = request.getRequestLine().getMethod();
		
		File p = new File(path);
		
		L.d("Got " + method + " request for path " + p.getAbsolutePath() + ".");
		
		try {
			URI uri = new URI(p.getAbsolutePath());
			this.queryString = URLEncodedUtils.parse(uri, "UTF-8");
		} catch (Exception ex) {
			L.e("Exception parsing query string.", ex);
		}
		
		StringTokenizer st = new StringTokenizer(p.getAbsolutePath(), "/?");
		st.nextToken(); // dismiss the "set" or "get"
		handle(method, st);
	}
	
	protected void sendString(int statusCode, String content) {
		try {
			response.setStatusCode(statusCode);
			response.setEntity(new StringEntity(content));
		} catch (UnsupportedEncodingException ex) {
			L.e("Unsupported encoding when adding StringEntity to response.");
		}
	}
	
	protected int getIntParameter(String key) {
		int id;
		
		try {
			id = Integer.parseInt(getParameter(key));
		} catch (NumberFormatException ex) {
			id = -1;
			sendString(405, "Could not parse '" + key + "' as an integer.");
		}
		
		return id;
	}
	
	protected Date getDateParameter(String key) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d;
		
		try {
			d = df.parse(getParameter(key));
		} catch (ParseException ex) {
			sendString(405, "Could not parse '" + key + "' as a date.");
			d = null;
		}
		
		return d;
	}
	
	protected String getParameter(String key) {
		for (NameValuePair pair : queryString) {
			if (pair.getName().toLowerCase().equals(key.toLowerCase())) {
				return pair.getValue();
			}
		}
		
		return "";
	}
	
	protected abstract void handle(String method, StringTokenizer path);
}
