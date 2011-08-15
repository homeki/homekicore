package com.homekey.core.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.homekey.core.device.Device;
import com.homekey.core.device.mock.MockDevice;
import com.homekey.core.device.mock.MockDimmerDevice;
import com.homekey.core.http.HttpApi;
import com.homekey.core.http.HttpRequestResolverThread;
import com.homekey.core.http.HttpSetResolver;
import com.homekey.core.main.Monitor;
import com.homekey.core.storage.Database;

public class HttpResolverTest {
	DataOutputStream os;
	Monitor monitor;
	HttpApi api;

	@Before
	public void setUp() throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		os = new DataOutputStream(bos);
		monitor = new Monitor();
		api = new HttpApi(monitor);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBadStatusRequest() {
		try {
			HttpRequestResolverThread.handleRequest(
					"GET get/status?id=1 HTTP/1.1", os, api);
			fail("Did not generate exception.");
		} catch (NoSuchElementException e) {
			// Correct, since the id 1 does not exist.
		} catch (IOException e) {
			fail("Something went wrong.");
		}
	}

	@Test
	public void testStatusRequest() {
		Database db = TestUtil.getEmptyTestDatabase();
		Device dev = new MockDimmerDevice("hejsan", db);
		monitor.addDevice(dev);
		try {
			String req = String.format("GET get/status?id=%d HTTP/1.1",
					dev.getId());
			HttpRequestResolverThread.handleRequest(req, os, api);
		} catch (NoSuchElementException e) {
			fail("Generated exception.");
		} catch (IOException e) {
			fail("Something went wrong.");
		}
	}
	

	@Test
	public void testRequestTime() {
		try {
			String req = String.format("GET get/time HTTP/1.1");
			System.out.println("Requesting: " + req);
			HttpRequestResolverThread.handleRequest(req, os, api);
		} catch (NoSuchElementException e) {
			fail("Generated exception.");
		} catch (IOException e) {
			fail("Something went wrong.");
		}
	}
	
	@Test
	public void testRequestDevices() {
		try {
			String req = String.format("GET get/time HTTP/1.1");
			System.out.println("Requesting: " + req);
			HttpRequestResolverThread.handleRequest(req, os, api);
		} catch (NoSuchElementException e) {
			fail("Generated exception.");
		} catch (IOException e) {
			fail("Something went wrong.");
		}
	}
}
