package com.homeki.core;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TestUtil {
	private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String HOST = "http://localhost:5000";
	
	private static Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat(DATETIME_FORMAT).create();
	
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
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(HOST + url);
		HttpResponse response = null;
		String postString = gson.toJson(obj);
		Response r = null;
		
		try {
			post.setEntity(new StringEntity(postString));
			response = client.execute(post);
			
			r = new TestUtil().new Response();
			r.statusCode = response.getStatusLine().getStatusCode();
			r.content = convertToString(response.getEntity());			
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
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(HOST + url);
		HttpResponse response = null;
		Response r = null;
		
		try {
			response = client.execute(get);
			
			r = new TestUtil().new Response();
			r.statusCode = response.getStatusLine().getStatusCode();
			r.content = convertToString(response.getEntity());
		} catch (Exception e) {
			fail("Failed sending GET request, message: " + e.getMessage());
		}
		
		return r;
	}
	
	private static String convertToString(HttpEntity he) throws IOException {
		String s;
		try {
			s = EntityUtils.toString(he);
		} catch (Exception e) {
			s = "";
		}
		return s;
	}
}
