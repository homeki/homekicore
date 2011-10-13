package com.homeki.core.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.homeki.core.threads.mock.ControlledMockThread;

public class ControlledThreadTest {
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
		while(!t.isAlive()){
			t.hashCode();
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
