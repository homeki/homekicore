package com.homekey.core.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.homekey.core.threads.mock.ControlledMockThread;

public class ControlledThreadTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		long ms = -System.currentTimeMillis();
		ControlledMockThread t = new ControlledMockThread();
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			fail();
		}
		ms += System.currentTimeMillis();
		// The thread should run during one second
		ms = Math.abs(ms - 1005);
		assertTrue(
				String.format(
						"Controlled Thread had too large timing error (%d ms). 50 ms allowed.",
						ms), ms < 50);
	}

}
