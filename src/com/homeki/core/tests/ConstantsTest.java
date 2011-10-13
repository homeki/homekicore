package com.homeki.core.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.homeki.core.Logs;

public class ConstantsTest {
	@Test
	public void test() {
		assertEquals(Logs.CORE,"core");
		assertEquals(Logs.CORE_MOCK,"core.mock");
		new Logs();
	}
}
