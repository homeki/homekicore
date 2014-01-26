package com.homeki.core.http;

import com.homeki.core.TestUtil;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class EventSpecialValueTest {
	public static class JsonSpecialValue {
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
