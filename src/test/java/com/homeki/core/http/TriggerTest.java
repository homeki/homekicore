package com.homeki.core.http;

import com.homeki.core.TestUtil;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TriggerTest {
	private int triggerId;
	
	public static class JsonTrigger {
		public Integer triggerId;
		public String name;
	}
	
	@Test
	public void testAdd() throws Exception {
		JsonTrigger jtrigger = new JsonTrigger();
		assertEquals(TestUtil.sendPost("/triggers", jtrigger).statusCode, 400);
		jtrigger.name = "";
		assertEquals(TestUtil.sendPost("/triggers", jtrigger).statusCode, 400);
		jtrigger.name = "MyTrigger";
		jtrigger = TestUtil.sendPostAndParseAsJson("/triggers", jtrigger, JsonTrigger.class);
		assertTrue(jtrigger.triggerId > 0);
		triggerId = jtrigger.triggerId;
	}
	
	@Test(dependsOnMethods="testAdd")
	public void testList() {
		JsonTrigger[] jtriggers = TestUtil.sendGetAndParseAsJson("/triggers", JsonTrigger[].class);
		
		Set<Integer> existingIds = new HashSet<Integer>();
		
		for (JsonTrigger jd : jtriggers)
			existingIds.add(jd.triggerId);
		
		assertTrue(existingIds.contains(triggerId));
	}
	
	@Test(dependsOnMethods="testList")
	public void testSet() {
		JsonTrigger jtrigger = new JsonTrigger();
		assertEquals(TestUtil.sendPost("/triggers/9999", jtrigger).statusCode, 400);
		assertEquals(TestUtil.sendPost("/triggers/" + triggerId, jtrigger).statusCode, 400);
		jtrigger.name = "MyNewTrigger";
		assertEquals(TestUtil.sendPost("/triggers/" + triggerId, jtrigger).statusCode, 200);
		
		JsonTrigger[] jtriggers = TestUtil.sendGetAndParseAsJson("/triggers", JsonTrigger[].class);
		
		boolean found = false;
		for (JsonTrigger jt : jtriggers) {
			if (jt.triggerId == triggerId) {
				found = true;
				assertEquals(jt.name, "MyNewTrigger");
			}
		}
		assertTrue(found);
	}
	
	@Test(dependsOnMethods="testSet")
	public void testDelete() {
		assertEquals(TestUtil.sendDelete("/triggers/" + triggerId).statusCode, 200);
	}
}
