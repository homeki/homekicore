package com.homekey.core.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.StringTokenizer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.homekey.core.http.HttpApi;
import com.homekey.core.http.HttpRequestResolverThread;
import com.homekey.core.http.HttpSetResolver;
import com.homekey.core.main.Monitor;

public class HttpResolverTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBadStatusRequest() {
		ByteArrayOutputStream bos =new ByteArrayOutputStream();
		DataOutputStream os = new DataOutputStream(bos);
		Monitor monitor = new Monitor();
		HttpApi api = new HttpApi(monitor);
		try {
			HttpRequestResolverThread.handleRequest("GET get/status?id=1 HTTP/1.1",os,api);
			fail();
		} catch (IOException e) {
			
		}
	}
}
