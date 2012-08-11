package com.homeki.core.http;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

import com.homeki.core.TestUtil;

public class TriggerTest {
	public class JsonTrigger {
		public Integer id;
		public String name;
	}
	
	@Test
	public void testAdd() throws Exception {
		JsonTrigger jtrigger = new JsonTrigger();
		assertEquals(TestUtil.sendPost("/trigger/add", jtrigger).statusCode, 400);
		jtrigger.name = "";
		assertEquals(TestUtil.sendPost("/trigger/add", jtrigger).statusCode, 400);
		jtrigger.name = "MyTrigger";
		jtrigger = TestUtil.sendPostAndParseAsJson("/trigger/add", jtrigger, JsonTrigger.class);
		assertTrue(jtrigger.id > 0);
	}
	
	// TODO: add delete
}
