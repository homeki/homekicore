package com.homeki.core.test.api;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimerTriggerTest {
	public class JsonTriggerTimer {
		public String name;
		public Integer newValue;
		public Integer time;
		public Integer repeatType;
		public Integer days;
	}
	
	@Test
	public void testAdd() throws Exception {
		JsonTriggerTimer tmr = new JsonTriggerTimer();
		tmr.name = "name with spaces";
		tmr.newValue = 55;
		tmr.time = 12122;
		tmr.repeatType = 1;
		tmr.days = 4;
		assertEquals(200, TestUtil.sendPost("/trigger/timer/add", tmr).statusCode);
		
		tmr.name = "another name with spaces";
		tmr.newValue = 200;
		tmr.time = 99;
		tmr.repeatType = 2;
		tmr.days = 3;
		assertEquals(200, TestUtil.sendPost("/trigger/timer/add", tmr).statusCode);
	}
	
	@Test
	public void testGet() {
		assertTrue(true);
	}
}
