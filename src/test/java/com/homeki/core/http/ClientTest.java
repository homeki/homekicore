package com.homeki.core.http;

import com.homeki.core.TestUtil;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ClientTest {
	public static class JsonClient {
		public String id;
	}
	
	@Test
	public void testRegister() throws Exception {
		JsonClient jclient = new JsonClient();
		assertEquals(TestUtil.sendPost("/clients", jclient).statusCode, 400);
		
		jclient.id = "someid";
		assertEquals(TestUtil.sendPost("/clients", jclient).statusCode, 200);
	}
	
	@Test
	public void testUnregister() throws Exception {
		assertEquals(TestUtil.sendDelete("/clients/someid").statusCode, 200);
	}
}
