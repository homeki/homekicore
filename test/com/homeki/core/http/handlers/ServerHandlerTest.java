package com.homeki.core.http.handlers;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.homeki.core.TestUtil;


public class ServerHandlerTest {
	public class JsonServerInfo {
		public Long uptimeMs;
		public Long timeMs;
		public String time;
		public String version;
		public String name;
	}
	
	@Test
	public void testGet() throws Exception {
		JsonServerInfo jinfo = TestUtil.sendGetAndParseAsJson("/server/get", JsonServerInfo.class);
		assertEquals("Homeki", jinfo.name);
		TestUtil.getDateTimeFormat().parse(jinfo.time);
		assertTrue(jinfo.timeMs > 0);
		assertTrue(jinfo.uptimeMs > 0);
		assertTrue(jinfo.version.length() > 0);
	}
	
	@Test(dependsOnMethods="testGet")
	public void testSet() throws Exception {
		JsonServerInfo get = TestUtil.sendGetAndParseAsJson("/server/get", JsonServerInfo.class);
		assertEquals("Homeki", get.name);
		
		JsonServerInfo set = new JsonServerInfo();
		assertEquals(405, TestUtil.sendPost("/server/set", set).statusCode);
		set.name = "";
		assertEquals(405, TestUtil.sendPost("/server/set", set).statusCode);
		set.name = "MyServer";
		assertEquals(200, TestUtil.sendPost("/server/set", set).statusCode);
		
		get = TestUtil.sendGetAndParseAsJson("/server/get", JsonServerInfo.class);
		assertEquals("MyServer", get.name);
	}
}
