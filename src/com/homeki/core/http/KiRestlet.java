package com.homeki.core.http;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;

import org.hibernate.Session;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.engine.header.Header;
import org.restlet.util.Series;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.homeki.core.logging.L;
import com.homeki.core.main.Util;
import com.homeki.core.storage.Hibernate;

public abstract class KiRestlet extends Restlet {
	protected static final Gson gson = new GsonBuilder()
		.setPrettyPrinting()
		.setDateFormat(Util.getDateTimeFormat().toPattern())
		.create();
	
	@Override
	public void handle(Request request, Response response) {
		Container c = new Container();
		Session session = null;
		
		c.req = request;
		c.res = response;
		
		try {
			L.i("Got " + request.getMethod().getName() + " request for path " + request.getOriginalRef().getPath() + ".");
			addCustomHttpHeaders(c);
			session = Hibernate.openSession();
			c.ses = session;
			handle(c);
		}
		catch (JsonSyntaxException e) {
			set400Response(c, "Could not parse JSON, make sure it is well formed.");
		}
		catch (ApiException e) {
			set400Response(c, e.getMessage());
		}
		catch (Throwable e) {
			L.e("Unknown exception occured while processing HTTP request.", e);
			set400Response(c, "Unhandled exception occured while processing HTTP request. The exception message was: " + e.getMessage());
		}
		finally {
			if (session != null && session.isOpen()) {
				try {
					Hibernate.closeSession(session);
				}
				catch (Exception ex) {
					L.e("Error committing transaction.", ex);
					set400Response(c, "Error committing transaction, message: " + ex.getMessage());
				}
			}
		}
	}
	
	protected void set200Response(Container c, String content) {
		c.res.setStatus(Status.SUCCESS_OK);
		c.res.setEntity(content, MediaType.TEXT_PLAIN);
	}
	
	protected void set400Response(Container c, String content) {
		c.res.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
		c.res.setEntity(content, MediaType.TEXT_PLAIN);
	}
	
	protected int getInt(Container c, String key) {
		try {
			return Integer.parseInt(getString(c, key));
		} catch (NumberFormatException e) {
			throw new ApiException("Could not parse URL parameter {" + key + "} as integer.");
		}
	}
	
	protected String getString(Container c, String key) {
		Object value = c.req.getAttributes().get(key);
		
		if (value == null)
			throw new ApiException("Missing URL parameter {" +  key  + "}.");
		
		return value.toString();
	}
	
	protected String getStringParam(Container c, String key) {
		String value = c.req.getResourceRef().getQueryAsForm().getFirstValue(key);
		
		if (value == null)
			throw new ApiException("Missing querystring parameter {" + key + "}.");
		
		return value;
	}
	
	protected int getIntParam(Container c, String key) {
		try {
			return Integer.parseInt(getStringParam(c, key));
		} catch (NumberFormatException e) {
			throw new ApiException("Could not parse querystring parameter {" + key + "} as integer.");
		}
	}
	
	protected Date getDateParam(Container c, String key) {
		try {
			try {
				return Util.getDateTimeFormat().parse(getStringParam(c, key));
			} catch (ParseException ex) {
				return Util.getDateFormat().parse(getStringParam(c, key));
			}
		} catch (ParseException e) {
			throw new ApiException("Could not parse querystring parameter {" + key + "} as a date.");
		}
	}
	
	protected <T> T getJsonObject(Container c, Type t) {
		String s = "";
		
		if (!c.req.getMethod().equals(Method.POST))
			throw new ApiException("Expected POST HTTP, but received " + c.req.getMethod().getName() + ".");
		
		s = c.req.getEntityAsText();
		
		if (s.length() == 0)
			throw new ApiException("Expected JSON in POST HTTP, but received nothing.");
		
		return gson.fromJson(s, t);
	}
	
	protected abstract void handle(Container c);
	
	private void addCustomHttpHeaders(Container c) {
		@SuppressWarnings("unchecked")
		Series<Header> responseHeaders = (Series<Header>)c.res.getAttributes().get("org.restlet.http.headers");
		if (responseHeaders == null) {
		    responseHeaders = new Series<Header>(Header.class);
		    c.res.getAttributes().put("org.restlet.http.headers", responseHeaders);
		}
		responseHeaders.add(new Header("Access-Control-Allow-Origin", "*"));
	}
}
