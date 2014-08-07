package com.homeki.core.http;

import com.homeki.core.TestUtil;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class ServerTest {
	public static class JsonServerInfo {
		public Long uptimeMs;
		public Long timeMs;
		public String time;
		public String version;
		public String name;
		public String hostname;
		public Double locationLongitude;
		public Double locationLatitude;
		public String smtpHost;
		public Integer smtpPort;
		public Boolean smtpAuth;
		public Boolean smtpTls;
		public String smtpUser;
		public String smtpPassword;
	}
	
	@Test
	public void testSet() throws Exception {
		JsonServerInfo jinfo = new JsonServerInfo();
		assertEquals(TestUtil.sendPost("/server", jinfo).statusCode, 200);
		
		jinfo.name = "MyServer";
		jinfo.hostname = "http://this.is.my.server.com:12345/";
		jinfo.locationLatitude = 15.05;
		jinfo.locationLongitude = 12.03;
		jinfo.smtpAuth = true;
		jinfo.smtpHost = "some.host.com";
		jinfo.smtpPassword = "somepass";
		jinfo.smtpPort = 25;
		jinfo.smtpTls = true;
		jinfo.smtpUser = "some@user.com";
		assertEquals(TestUtil.sendPost("/server", jinfo).statusCode, 200);
	}
	
	@Test(dependsOnMethods="testSet")
	public void testGet() throws Exception {
		JsonServerInfo jinfo = TestUtil.sendGetAndParseAsJson("/server", JsonServerInfo.class);
		TestUtil.getDateTimeFormat().parse(jinfo.time);
		assertTrue(jinfo.timeMs > 0);
		assertTrue(jinfo.uptimeMs > 0);
		assertTrue(jinfo.version.length() > 0);
		assertEquals(jinfo.name, "MyServer");
		assertEquals(jinfo.hostname, "http://this.is.my.server.com:12345/");
		assertEquals(jinfo.locationLatitude, 15.05, 0.01);
		assertEquals(jinfo.locationLongitude, 12.03, 0.01);
		assertEquals((boolean)jinfo.smtpAuth, true);
		assertEquals(jinfo.smtpHost, "some.host.com");
		assertEquals(jinfo.smtpPassword, "somepass");
		assertEquals((int)jinfo.smtpPort, 25);
		assertEquals((boolean)jinfo.smtpTls, true);
		assertEquals(jinfo.smtpUser, "some@user.com");
	}
}
