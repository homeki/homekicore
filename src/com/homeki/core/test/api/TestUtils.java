package com.homeki.core.test.api;

import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TestUtils {
	private static final String HOST = "http://localhost:5000";
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss").create();;
	
	public static int sendPost(String url, Object obj) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(HOST + url);
		HttpResponse response = null;
		String postString = gson.toJson(obj);
		
		try {
			post.setEntity(new StringEntity(postString));
		} catch (UnsupportedEncodingException e) {
			fail("Failed setting entity on HttpPost: " + e.getMessage());
		}
		
		try {
			response = client.execute(post);
		} catch (Exception e) {
			fail("Failed when sending post to server: " + e.getMessage());
		}
		
		return response.getStatusLine().getStatusCode();
	}
}
