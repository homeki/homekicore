package com.homeki.core.http;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EntityUtils;
import org.hibernate.Session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.homeki.core.main.L;
import com.homeki.core.main.Util;
import com.homeki.core.storage.Hibernate;

public abstract class HttpHandler implements HttpRequestHandler {
	protected static final Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat(Util.getDateTimeFormat().toPattern()).create();
	
	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
		Session session = null;
		Container c = new Container();
		
		c.res = response;
		c.req = request;
		
		try {
			List<NameValuePair> queryString = null;
			String path = request.getRequestLine().getUri();
			String method = request.getRequestLine().getMethod();
			
			File p = new File(path);
			
			L.i("Got " + method + " request for path " + p.getAbsolutePath() + ".");
			
			StringTokenizer st = new StringTokenizer(p.getAbsolutePath(), "/?");
			URI uri = new URI(p.getAbsolutePath());
			queryString = URLEncodedUtils.parse(uri, "UTF-8");
			
			session = Hibernate.openSession();
			
			c.session = session;
			c.path = st;
			c.qs = queryString;
			
			handle(c);
		} catch (JsonSyntaxException e) {
			set405Response(c, "Could not parse JSON, make sure it is well formed.");
		} catch (ApiException e) {
			set405Response(c, e.getMessage());
		} catch (Exception e) {
			L.e("Unknown exception occured while processing HTTP request.", e);
			set405Response(c, "Unhandled exception occured while processing HTTP request. The exception message was: " + e.getMessage());
		} finally {
			if (session != null && session.isOpen())
				Hibernate.closeSession(session);
		}
	}
	
	private void setStringResponse(Container c, int statusCode, String content) {
		try {
			c.res.setStatusCode(statusCode);
			c.res.setEntity(new StringEntity(content));
		} catch (UnsupportedEncodingException e) {
			L.e("Unsupported encoding when adding StringEntity to response.");
			throw new ApiException("Unsupported encoding when adding StringEntity to response.");
		}
	}
	
	protected void set200Response(Container c, String content) {
		setStringResponse(c, 200, content);
	}
	
	protected void set405Response(Container c, String content) {
		setStringResponse(c, 405, content);
	}
	
	protected int getIntParameter(Container c, String key) {
		try {
			return Integer.parseInt(getStringParameter(c, key));
		} catch (NumberFormatException e) {
			throw new ApiException("Could not parse '" + key + "' as integer.");
		}
	}
	
	protected int getOptionalIntParameter(Container c, String key) {
		try {
			return Integer.parseInt(getStringParameter(c, key));
		} catch (Exception e) {
			return -1;
		}
	}
	
	protected Date getDateParameter(Container c, String key) {
		try {
			try {
				return Util.getDateTimeFormat().parse(getStringParameter(c, key));
			} catch (ParseException ex) {
				return Util.getDateFormat().parse(getStringParameter(c, key));
			}
		} catch (ParseException e) {
			throw new ApiException("Could not parse '" + key + "' as a date.");
		}
	}
	
	private String getStringParameter(Container c, String key) {
		for (NameValuePair pair : c.qs) {
			if (pair.getName().toLowerCase().equals(key.toLowerCase()))
				return pair.getValue();
		}
		
		throw new ApiException("Missing parameter '" +  key  + "'.");
	}
	
	protected String getPost(Container c) {
		String s = "";
		
		if (!c.req.getRequestLine().getMethod().toUpperCase().equals("POST"))
			throw new ApiException("Expected POST HTTP, but received something else.");
		
		if (!(c.req instanceof HttpEntityEnclosingRequest))
			throw new ApiException("POST data not provided.");
		
		HttpEntity entity = ((HttpEntityEnclosingRequest)c.req).getEntity();
		
		try {
			s = EntityUtils.toString(entity);
		} catch (Exception e) {
			throw new ApiException("Could not parse POST data.");
		}
		
		return s;
	}
	
	protected abstract void handle(Container c) throws Exception;
}
