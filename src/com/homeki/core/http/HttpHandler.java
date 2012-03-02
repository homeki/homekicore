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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.homeki.core.main.L;
import com.homeki.core.main.Util;

public abstract class HttpHandler implements HttpRequestHandler {
	protected static final Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	
	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
		List<NameValuePair> queryString = null;
		String path = request.getRequestLine().getUri();
		String method = request.getRequestLine().getMethod();
		
		File p = new File(path);
		
		L.i("Got " + method + " request for path " + p.getAbsolutePath() + ".");
		
		try {
			URI uri = new URI(p.getAbsolutePath());
			queryString = URLEncodedUtils.parse(uri, "UTF-8");
		} catch (Exception e) {
			L.e("Exception parsing query string.", e);
		}
		
		StringTokenizer st = new StringTokenizer(p.getAbsolutePath(), "/?");
		
		try {
			handle(request, response, queryString, method, st);
		} catch (JsonSyntaxException e) {
			try {
				sendString(response, 405, "Couldn't parse JSON, make sure it is well formed.");
			} catch (Exception ignore) {}
		} catch (Exception e) {
			L.e("Unknown exception occured while processing HTTP request.", e);
			try {
				sendString(response, 405, "Something went wrong while processing the HTTP request.");
			} catch (Exception ignore) {}
		}
	}
	
	protected void sendString(HttpResponse response, int statusCode, String content) {
		try {
			response.setStatusCode(statusCode);
			response.setEntity(new StringEntity(content));
		} catch (UnsupportedEncodingException e) {
			L.e("Unsupported encoding when adding StringEntity to response.");
		}
	}
	
	protected int getIntParameter(HttpResponse response, List<NameValuePair> queryString, String key) {
		int id;
		
		try {
			id = Integer.parseInt(getParameter(queryString, key));
		} catch (NumberFormatException e) {
			id = -1;
			L.e("Could not parse '" + key + "' as an integer.");
			sendString(response, 405, "Could not parse '" + key + "' as an integer.");
		} catch (MissingKeyException e) {
			id = -1;
			L.e(e.getMessage());
			sendString(response, 405, e.getMessage());
		}
		
		return id;
	}
	
	protected Date getDateParameter(HttpResponse response, List<NameValuePair> queryString, String key) {
		Date d;
		
		try {
			try {
				d = Util.getDateTimeFormat().parse(getParameter(queryString, key));
			} catch (ParseException ex) {
				d = Util.getDateFormat().parse(getParameter(queryString, key));
			}
		} catch (ParseException ex) {
			L.e("Could not parse '" + key + "' as a date.");
			sendString(response, 405, "Could not parse '" + key + "' as a date.");
			d = null;
		} catch (MissingKeyException e) {
			L.e(e.getMessage());
			sendString(response, 405, e.getMessage());
			d = null;
		}
		
		return d;
	}
	
	protected String getStringParameter(HttpResponse response, List<NameValuePair> queryString, String key) {
		String value = "";
		
		try {
			value = getParameter(queryString, key);
		} catch (MissingKeyException e) {
			L.e(e.getMessage());
			sendString(response, 405, e.getMessage());
		}
		
		return value;
	}
	
	private String getParameter(List<NameValuePair> queryString, String key) throws MissingKeyException {
		for (NameValuePair pair : queryString) {
			if (pair.getName().toLowerCase().equals(key.toLowerCase())) {
				return pair.getValue();
			}
		}
		
		throw new MissingKeyException(String.format("Missing parameter '%s'.", key));
	}
	
	protected String getPost(HttpRequest request, HttpResponse response) {
		String s = "";
		
		if (!request.getRequestLine().getMethod().toUpperCase().equals("POST")) {
			L.e("Expected POST HTTP, but received something else.");
			sendString(response, 405, "HTTP method not POST.");
			return "";
		}
		
		if (!(request instanceof HttpEntityEnclosingRequest)) {
			L.e("Missing POST HTTP data.");
			sendString(response, 405, "POST data not provided.");
			return "";
		}
		
		HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
		
		try {
			s = EntityUtils.toString(entity);
		} catch (Exception e) {
			L.e("Could not parse POST data.", e);
			sendString(response, 405, "Could not parse POST data.");
		}
		
		return s;
	}
	
	protected abstract void handle(HttpRequest request, HttpResponse response, List<NameValuePair> queryString, String method, StringTokenizer path);
}
