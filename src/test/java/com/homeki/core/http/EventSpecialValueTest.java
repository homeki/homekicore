package com.homeki.core.http;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.homeki.core.TestUtil;

public class EventSpecialValueTest {
	public class JsonSpecialValue {
		public String source;
		public Integer value;
	}
	
	@Test
	public void testEvent() {
		JsonSpecialValue value = new JsonSpecialValue();
		
		value.source = "XBMC";
		value.value = 12;
		
		assertEquals(TestUtil.sendPost("/event/specialvalue", value).statusCode, 200);
	}
}
