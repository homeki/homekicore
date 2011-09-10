package com.homekey.core.tests;

import static org.junit.Assert.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.junit.Test;

import com.homekey.core.http.HttpArguments;

public class HttpArgumentsTest {
	@Test
	public void testConstructor() {
		new HttpArguments();
	}

	@Test
	public void testValidInt() {
		OutputStream out;
		try {
			out = new FileOutputStream(new File("/tmp/asdasdasd"));

			DataOutputStream s = new DataOutputStream(out);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("myInt", "100");
			try {
				Integer val = HttpArguments.demandInteger("myInt", map, s);
				assertEquals(val, new Integer(100));
			} catch (IOException e) {
				fail();
			}
		} catch (FileNotFoundException e1) {
			fail("Could not create temporary file.");
		}
	}
	
	@Test
	public void testInvalidInt() {
		OutputStream out;
		try {
			out = new FileOutputStream(new File("/tmp/asdasdasd"));

			DataOutputStream s = new DataOutputStream(out);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("myInt", "not an integer!");
			try {
				Integer val = HttpArguments.demandInteger("myInt", map, s);
				if(val != null){
					fail("Parsed non-int as int.");
				}
			} catch (IOException e) {
				fail();
			}
		} catch (FileNotFoundException e1) {
			fail("Could not create temporary file.");
		}
	}
	
	@Test
	public void testArgumentNotPresent() {
		OutputStream out;
		try {
			out = new FileOutputStream(new File("/tmp/asdasdasd"));

			DataOutputStream s = new DataOutputStream(out);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("myInt", "100");
			try {
				Integer val = HttpArguments.demandInteger("somethingElse", map, s);
				if(val != null){
					fail("Non-existing argument parsed.");
				}
			} catch (IOException e) {
				fail();
			}
		} catch (FileNotFoundException e1) {
			fail("Could not create temporary file.");
		}
	}
}
