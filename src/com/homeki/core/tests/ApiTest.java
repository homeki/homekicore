package com.homeki.core.tests;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.homeki.core.device.mock.MockDevice;
import com.homeki.core.device.mock.MockDimmer;
import com.homeki.core.device.mock.MockSwitch;
import com.homeki.core.http.HttpApi;
import com.homeki.core.main.Monitor;

public class ApiTest {
	private static final String GET_DEVICES_JSON = TestUtil.getStringFromTextFile("get_devices_json");
	private static final String JSON_STATUS_FALSE = "{\n  \"status\": false\n}";
	private static final String JSON_STATUS_TRUE = "{\n  \"status\": true\n}";
	private static final String JSON_STATUS_INT = "{\n  \"status\": %d\n}";
	
	private HttpApi api;
	private MockSwitch mock1;
	private MockDimmer mock2;
	
	@Before
	public void setUp() throws Exception {
		Monitor mon = new Monitor();
		api = new HttpApi(mon);
		
		mock1 = new MockSwitch("switch1");
		mock2 = new MockDimmer("dimmer1");
		
		setMockDate(mock1, mock2);
		
		mon.addDevice(mock1); // id 1 in db
		mon.addDevice(mock2); // id 2 in db
	}
	
	private void setMockDate(MockDevice mock1, MockDevice mock2) throws ParseException {
		Date d;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		d = sdf.parse("2011-07-30 18:50:41");
		mock1.setPretendAddedDate(d);
		mock2.setPretendAddedDate(d);
	}
	
	@Test
	public void testGetDevices() {
		assertEquals(GET_DEVICES_JSON, api.getDevices());
	}
	
	@Test
	public void testSwitchOn() {
		// Shut them down
		mock1.off();
		assertEquals(false, mock1.getValue());
		mock2.off();
		assertEquals(0, (int) mock2.getValue());
		
		// Turn them on via API
		api.switchOn(mock1.getId());
		assertEquals(true, mock1.getValue());
		assertEquals(0, (int) mock2.getValue());
		
		api.switchOn(mock2.getId());
		assertEquals(true, mock1.getValue());
		assertEquals(255, (int) mock2.getValue());
	}
	
	@Test
	public void testSwitchOff() {
		// Turn them on
		mock1.on();
		assertEquals(true, mock1.getValue());
		mock2.on();
		assertEquals(255, (int) mock2.getValue());
		
		// Turn them on via API
		api.switchOff(mock2.getId());
		assertEquals(true, mock1.getValue());

		assertEquals(0, (int) mock2.getValue());

		api.switchOff(mock1.getId());
		assertEquals(false, mock1.getValue());
		assertEquals(0, (int) mock2.getValue());
/*
		assertEquals(255, (int) mock2.getValue());
		
		api.switchOn(mock1.getId());
		assertEquals(true, mock1.getValue());
		assertEquals(255, (int) mock2.getValue());
*/
	}
	
	@Test
	public void testDim() {
		mock2.dim(0);
		for (int i = 0; i < 10000; i += 11) {
			
			api.dim(mock2.getId(), i % 256);
			assertEquals(i % 256, (int) mock2.getValue());
		}
	}
	
	@Test
	public void testGetData() {
		
	}
	
	@Test
	public void testGetStatus() {
		System.out.println();
		
		for (int i = 0; i < 10000; i += 101) {
			mock1.off();
			assertEquals(JSON_STATUS_FALSE, api.getStatus(mock1.getId()));
			mock1.on();
			assertEquals(JSON_STATUS_TRUE, api.getStatus(mock1.getId()));
			mock2.dim(i % 256);
			assertEquals(String.format(JSON_STATUS_INT, i % 256), api.getStatus(mock2.getId()));
		}
	}
	
}
