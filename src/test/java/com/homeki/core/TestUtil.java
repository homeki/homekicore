package com.homeki.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.homeki.core.json.JsonDevice;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.logging.LogManager;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class TestUtil {
	private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String HOST = "http://localhost:5001";
	
	private static final Gson gson = new GsonBuilder()
		.setPrettyPrinting()
		.setDateFormat(DATETIME_FORMAT)
		.create();
	
	protected static final Client client = new Client(Protocol.HTTP);
	
	static {
		LogManager.getLogManager().reset();
	}
	
	public enum MockDeviceType {
		SWITCH,
		DIMMER,
		THERMOMETER
	}
	
	public class Response {
		public int statusCode;
		public String content;
	}
	
	public static SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat(DATE_FORMAT);
	}
	
	public static SimpleDateFormat getDateTimeFormat() {
		return new SimpleDateFormat(DATETIME_FORMAT);
	}
	
	public static Response sendPost(String url, Object obj) {
		Request request = new Request(Method.POST, HOST + url);
		String postString = gson.toJson(obj);
		Response r = null;
		
		try {
			request.setEntity(postString, MediaType.APPLICATION_JSON);
			org.restlet.Response response = client.handle(request);
			
			r = new TestUtil().new Response();
			r.statusCode = response.getStatus().getCode();
			r.content = response.getEntityAsText();		
		}
		catch (Exception e) {
			fail("Failed sending POST request, message: " + e.getMessage());
		}
		
		return r;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T sendGetAndParseAsJson(String url, Type t) {
		Response r  = sendGet(url);
		
		assertEquals(r.statusCode, 200, r.content);
		
		String json = r.content;
		
		if (json == null || json.length() == 0)
			fail("Expected JSON in response, got none.");
		
		return (T)gson.fromJson(json, t);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T sendPostAndParseAsJson(String url, Object obj, Type t) {
		Response r  = sendPost(url, obj);
		
		assertEquals(r.statusCode, 200, r.content);
		
		String json = r.content;
		
		if (json == null || json.length() == 0)
			fail("Expected JSON in response, got none.");
		
		return (T)gson.fromJson(json, t);
	}
	
	public static Response sendGet(String url) {
		Request request = new Request(Method.GET, HOST + url);
		Response r = null;
		
		try {
			org.restlet.Response response = client.handle(request);
			
			r = new TestUtil().new Response();
			r.statusCode = response.getStatus().getCode();
			r.content = response.getEntityAsText();		
		}
		catch (Exception e) {
			fail("Failed sending GET request, message: " + e.getMessage());
		}
		
		return r;
	}
	
	public static int addMockDevice(MockDeviceType type) {
		JsonDevice dev = new JsonDevice();
		
		dev.type = type.toString().toLowerCase();
		
		dev = sendPostAndParseAsJson("/device/mock/add", dev, JsonDevice.class);
		return dev.id;
	}
	
	public static void deleteDevice(int id) {
		assertEquals(sendGet("/device/" + id + "/delete").statusCode, 200);
	}
}
