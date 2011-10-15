package com.homeki.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.homeki.core.log.L;

public class LTest {
	private L testlog;
	private ByteArrayOutputStream bos;

	@Before
	public void setUp() throws Exception {
		L.reset();
		testlog = L.getLogger("testlog");
		bos = new ByteArrayOutputStream();
		testlog.addOutput(bos);
	}

	@Test
	public void testGetLogger() {
		assertEquals(testlog, L.getLogger("testlog"));
	}

	@Test
	public void testAddExistingPrintStream() {
		try {
			testlog.addOutput(bos);
			fail("Should cast exception here!");
		} catch (Exception e) {
		}
	}

	@Test
	public void testSetStandard() {
		L.setStandard("testlog", false);
		L.i("my msg!");
		String msg = bos.toString();
		// The message
		assertTrue(msg.contains("my msg!"));
		// The thread name
		assertTrue(msg.contains("main"));
		// Message type info:
		assertTrue(msg.contains("INFO:"));
		try {
			bos.flush();
		} catch (IOException e) {
			fail("Could not flush, good thing this is not your toilet!");
		}
		L.e("my msg2!");
		msg = bos.toString();
		// The message
		assertTrue(msg.contains("my msg2!"));
		// The thread name
		assertTrue(msg.contains("main"));
		// Message type info:
		assertTrue(msg.contains("ERROR:"));
		L.w("my msg3!");
		msg = bos.toString();
		// The message
		assertTrue(msg.contains("my msg3!"));
		// The thread name
		assertTrue(msg.contains("main"));
		// Message type info:
		assertTrue(msg.contains("WARN:"));
		L.d("my msg4!");
		msg = bos.toString();
		// The message
		assertTrue(msg.contains("my msg4!"));
		// The thread name
		assertTrue(msg.contains("main"));
		// Message type info:
		assertTrue(msg.contains("DEBUG:"));
	}

	@Test
	public void testSetMinimumLevel() {
		L.setStandard("testlog", false);

		testlog.setMinimumLevel(L.LEVEL_WARN);
		L.i("my msg!");
		bos.reset();
		String msg = bos.toString();
		// The message
		assertTrue(!msg.contains("my msg!"));
		// The thread name
		assertTrue(!msg.contains("main"));
		// Message type info:
		assertTrue(!msg.contains("INFO:"));
		try {
			bos.flush();
		} catch (IOException e) {
			fail("Could not flush, good thing this is not your toilet!");
		}
		bos.reset();
		L.e("my msg2!");
		msg = bos.toString();
		// The message
		assertTrue(msg.contains("my msg2!"));
		// The thread name
		assertTrue(msg.contains("main"));
		// Message type info:
		assertTrue(msg.contains("ERROR:"));
		bos.reset();
		L.w("my msg3!");
		msg = bos.toString();
		// The message
		assertTrue(msg.contains("my msg3!"));
		// The thread name
		assertTrue(msg.contains("main"));
		// Message type info:
		assertTrue(msg.contains("WARN:"));
		bos.reset();
		L.d("my msg4!");
		msg = bos.toString();
		// The message
		assertTrue(!msg.contains("my msg4!"));
		// The thread name
		assertTrue(!msg.contains("main"));
		// Message type info:
		assertTrue(!msg.contains("DEBUG:"));
		testlog.setMinimumLevel(0);
		L.getLogger("testlog").setMinimumLevel(0);
		bos.reset();
		L.d("my msg5!");
		msg = bos.toString();
		// The message
		assertTrue(msg.contains("my msg5!"));
		// The thread name
		assertTrue(msg.contains("main"));
		// Message type info:
		assertTrue(msg.contains("DEBUG:"));
		bos.reset();
		L.i("my msg6!");
		msg = bos.toString();
		// The message
		assertTrue(msg.contains("my msg6!"));
		// The thread name
		assertTrue(msg.contains("main"));
		// Message type info:
		assertTrue(msg.contains("INFO:"));
		testlog.setMinimumLevel(L.LEVEL_ERROR);
		bos.reset();
		L.w("my msg7!");
		msg = bos.toString();
		// The message
		assertTrue(!msg.contains("my msg7!"));
		// The thread name
		assertTrue(!msg.contains("main"));
		// Message type info:
		assertTrue(!msg.contains("WARN:"));
	}

	@Test
	public void testAddRemoveFilter() {
		L.setStandard("testlog", false);
		L.getLogger("testlog").addRemoveFilter("my msg");
		L.i("my msg!");
		bos.reset();
		String msg = bos.toString();
		// The message
		assertTrue(!msg.contains("my msg!"));
		// The thread name
		assertTrue(!msg.contains("main"));
		// Message type info:
		assertTrue(!msg.contains("INFO:"));
		bos.reset();
		L.i("do not filter this!");
		msg = bos.toString();
		// The message
		assertTrue(msg.contains("do not filter this!"));
		// The thread name
		assertTrue(msg.contains("main"));
		// Message type info:
		assertTrue(msg.contains("INFO:"));
	}

}