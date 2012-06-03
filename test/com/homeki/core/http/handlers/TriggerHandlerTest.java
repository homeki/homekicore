package com.homeki.core.http.handlers;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

import com.homeki.core.TestUtil;

public class TriggerHandlerTest {
	public class JsonTrigger {
		public String name;
	}
	
	@Test
	public void testAdd() throws Exception {
		JsonTrigger set = new JsonTrigger();
		assertEquals(405, TestUtil.sendPost("/trigger/add", set).statusCode);
		set.name = "";
		assertEquals(405, TestUtil.sendPost("/trigger/add", set).statusCode);
		set.name = "MyTrigger";
		assertEquals(200, TestUtil.sendPost("/trigger/add", set).statusCode);
	}
}
