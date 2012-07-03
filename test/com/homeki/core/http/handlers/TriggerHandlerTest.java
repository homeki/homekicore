package com.homeki.core.http.handlers;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

import com.homeki.core.TestUtil;

public class TriggerHandlerTest {
	private int id;
	
	public class JsonTrigger {
		public Integer id;
		public String name;
	}
	
	@Test
	public void testAdd() throws Exception {
		JsonTrigger jtrigger = new JsonTrigger();
		assertEquals(405, TestUtil.sendPost("/trigger/add", jtrigger).statusCode);
		jtrigger.name = "";
		assertEquals(405, TestUtil.sendPost("/trigger/add", jtrigger).statusCode);
		jtrigger.name = "MyTrigger";
		TestUtil.sendPostAndParseAsJson("/trigger/add", jtrigger, JsonTrigger.class);
		assertTrue(jtrigger.id > 0);
	}
	
	// TODO: add delete
}
