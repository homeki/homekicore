package com.homeki.core.http;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.testng.annotations.Test;

import com.homeki.core.TestUtil;

public class TriggerTest {
	private int triggerId;
	
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
		triggerId = jtrigger.id;
	}
	
	@Test(dependsOnMethods="testAdd")
	public void testList() {
		JsonTrigger[] jtriggers = TestUtil.sendGetAndParseAsJson("/trigger/list", JsonTrigger[].class);
		
		Set<Integer> existingIds = new HashSet<Integer>();
		
		for (JsonTrigger jd : jtriggers)
			existingIds.add(jd.id);
		
		assertTrue(existingIds.contains(triggerId));
	}
	
	@Test(dependsOnMethods="testList")
	public void testDelete() {
		assertEquals(TestUtil.sendGet("/trigger/" + triggerId + "/delete").statusCode, 200);
	}
}
