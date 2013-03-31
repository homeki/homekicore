package com.homeki.core.http;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.homeki.core.TestUtil;

public class ClientTest {
	public class JsonClient {
		public String id;
	}
	
	@Test
	public void testRegister() throws Exception {
		JsonClient jclient = new JsonClient();
		assertEquals(TestUtil.sendPost("/client/register", jclient).statusCode, 400);
		
		jclient.id = "someid";
		assertEquals(TestUtil.sendPost("/client/register", jclient).statusCode, 200);
	}
	
	@Test
	public void testUnregister() throws Exception {
		JsonClient jclient = new JsonClient();
		assertEquals(TestUtil.sendPost("/client/unregister", jclient).statusCode, 400);
		
		jclient.id = "someid";
		assertEquals(TestUtil.sendPost("/client/unregister", jclient).statusCode, 200);
	}
}
