package com.homekey.core.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.homekey.core.Logs;

public class ConstantsTest {

	@Test
	public void test() {
		assertEquals(Logs.HOMEKEY,"homekey");
		assertEquals(Logs.MOCK,"mock");
		new Logs();
	}

}
