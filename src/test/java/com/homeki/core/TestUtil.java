package com.homeki.core;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.homeki.core.json.devices.JsonMockDevice;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.logging.LogManager;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class TestUtil {
	private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String HOST = "http://localhost:5000/api";
	
	private static JacksonJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private static Client client = ClientBuilder.newClient().register(jacksonJsonProvider);
	
	static {
		LogManager.getLogManager().reset();
		jacksonJsonProvider.locateMapper(Object.class, MediaType.APPLICATION_JSON_TYPE).setDateFormat(new SimpleDateFormat(DATETIME_FORMAT));
	}
	
	public enum MockDeviceType {
		SWITCH,
		DIMMER,
		THERMOMETER
	}

	public class Response {
		public int statusCode;
		private javax.ws.rs.core.Response nativeResponse;
	}

	public static class JsonVoid {
		public String message;
	}
	
	public static SimpleDateFormat getDateTimeFormat() {
		return new SimpleDateFormat(DATETIME_FORMAT);
	}
	
	public static Response sendPost(String url, Object obj) {
		Response r = null;
		
		try {
			javax.ws.rs.core.Response response = client
																						 .target(HOST + url)
																						 .request(MediaType.APPLICATION_JSON)
																						 .post(Entity.json(obj));
			
			r = new TestUtil().new Response();
			r.statusCode = response.getStatus();
			r.nativeResponse = response;
		}
		catch (Exception e) {
			fail("Failed sending POST request, message: " + e.getMessage());
		}
		
		return r;
	}

	public static Response sendDelete(String url) {
		Response r = null;

		try {
			javax.ws.rs.core.Response response = client
																						 .target(HOST + url)
																						 .request(MediaType.APPLICATION_JSON)
																						 .delete();

			r = new TestUtil().new Response();
			r.statusCode = response.getStatus();
			r.nativeResponse = response;
		}
		catch (Exception e) {
			fail("Failed sending DELETE request, message: " + e.getMessage());
		}

		return r;
	}

	public static Response sendGet(String url) {
		Response r = null;

		try {
			javax.ws.rs.core.Response response = client
																						 .target(HOST + url)
																						 .request(MediaType.APPLICATION_JSON)
																						 .get();

			r = new TestUtil().new Response();
			r.statusCode = response.getStatus();
			r.nativeResponse = response;
		}
		catch (Exception e) {
			fail("Failed sending GET request, message: " + e.getMessage());
		}

		return r;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T sendGetAndParseAsJson(String url, Class<?> t) {
		Response r = sendGet(url);
		throwIfError(r);
		return (T)r.nativeResponse.readEntity(t);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T sendPostAndParseAsJson(String url, Object obj, Class<?> t) {
		Response r = sendPost(url, obj);
		throwIfError(r);
		return (T)r.nativeResponse.readEntity(t);
	}

	private static void throwIfError(Response r) {
		if (r.statusCode == 200) return;
		JsonVoid jv = r.nativeResponse.readEntity(JsonVoid.class);
		throw new RuntimeException(jv.message);
	}
	
	public static int addMockDevice(MockDeviceType type) {
		JsonMockDevice dev = new JsonMockDevice();

		dev.vendor = "mock";
		dev.type = type.toString().toLowerCase();
		dev.name = UUID.randomUUID().toString().replace("-", "").substring(0, 7);
		
		dev = sendPostAndParseAsJson("/devices", dev, JsonMockDevice.class);
		return dev.deviceId;
	}
	
	public static void deleteDevice(int id) {
		assertEquals(sendDelete("/devices/" + id).statusCode, 200);
	}
}
