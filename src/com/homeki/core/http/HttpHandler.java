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

public abstract class HttpHandler implements HttpRequestHandler {
	protected static final Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	
	protected HttpRequest request;
	protected HttpResponse response;
	protected HttpContext context;
	private List<NameValuePair> queryString;
	
	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
		this.request = request;
		this.response = response;
		this.context = context;

		String path = request.getRequestLine().getUri();
		String method = request.getRequestLine().getMethod();
		
		File p = new File(path);
		
		L.i("Got " + method + " request for path " + p.getAbsolutePath() + ".");
		
		try {
			URI uri = new URI(p.getAbsolutePath());
			this.queryString = URLEncodedUtils.parse(uri, "UTF-8");
		} catch (Exception ex) {
			L.e("Exception parsing query string.", ex);
		}
		
		StringTokenizer st = new StringTokenizer(p.getAbsolutePath(), "/?");
		
		try {
			handle(method, st);
		} catch (JsonSyntaxException ex) {
			try {
				sendString(405, "Couldn't parse JSON, make sure it is well formed.");
			} catch (Exception ignore) {}
		} catch (Exception ex) {
			L.e("Unknown exception occured while processing HTTP request.", ex);
			try {
				sendString(405, "Something went wrong while processing the HTTP request.");
			} catch (Exception ignore) {}
		}
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
			L.e("Could not parse '" + key + "' as an integer.");
			sendString(405, "Could not parse '" + key + "' as an integer.");
		} catch (MissingKeyException ex) {
			id = -1;
			L.e(ex.getMessage());
			sendString(405, ex.getMessage());
		}
		
		return id;
	}
	
	protected Date getDateParameter(String key) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dft = new SimpleDateFormat("yyyy-MM-dd hh:ss:mm");
		Date d;
		
		try {
			try {
				d = dft.parse(getParameter(key));
			} catch (ParseException ex) {
				d = df.parse(getParameter(key));
			}
		} catch (ParseException ex) {
			L.e("Could not parse '" + key + "' as a date.");
			sendString(405, "Could not parse '" + key + "' as a date.");
			d = null;
		} catch (MissingKeyException ex) {
			L.e(ex.getMessage());
			sendString(405, ex.getMessage());
			d = null;
		}
		
		return d;
	}
	
	protected String getStringParameter(String key) {
		String value = "";
		
		try {
			value = getParameter(key);
		} catch (MissingKeyException ex) {
			L.e(ex.getMessage());
			sendString(405, ex.getMessage());
		}
		
		return value;
	}
	
	private String getParameter(String key) throws MissingKeyException {
		for (NameValuePair pair : queryString) {
			if (pair.getName().toLowerCase().equals(key.toLowerCase())) {
				return pair.getValue();
			}
		}
		
		throw new MissingKeyException(String.format("Missing parameter '%s'.", key));
	}
	
	protected String getPost() {
		String s = "";
		
		if (!request.getRequestLine().getMethod().toUpperCase().equals("POST")) {
			L.e("Expected POST HTTP, but received something else.");
			sendString(405, "HTTP method not POST.");
			return "";
		}
		
		if (!(request instanceof HttpEntityEnclosingRequest)) {
			L.e("Missing POST HTTP data.");
			sendString(405, "POST data not provided.");
			return "";
		}
		
		HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
		
		try {
			s = EntityUtils.toString(entity);
		} catch (Exception ex) {
			L.e("Could not parse POST data.", ex);
			sendString(405, "Could not parse POST data.");
		}
		
		return s;
	}
	
	protected abstract void handle(String method, StringTokenizer path);
}
