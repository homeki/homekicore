package com.homeki.core.http;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.homeki.core.TestUtil;

public class ClientTest {
	public class JsonClient {
		public String ipAddress;
	}
	
	@Test
	public void testRegister() throws Exception {
		JsonClient jclient = new JsonClient();
		assertEquals(TestUtil.sendPost("/client/register", jclient).statusCode, 400);
		
		jclient.ipAddress = "somethingnotanipaddress";
		assertEquals(TestUtil.sendPost("/client/register", jclient).statusCode, 400);
		
		jclient.ipAddress = "192.168.0.450";
		assertEquals(TestUtil.sendPost("/client/register", jclient).statusCode, 400);
		
		jclient.ipAddress = "192.168.0.30";
		assertEquals(TestUtil.sendPost("/client/register", jclient).statusCode, 200);
	}
}
