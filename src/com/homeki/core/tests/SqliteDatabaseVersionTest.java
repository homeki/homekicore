package com.homeki.core.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.homeki.core.storage.sqlite.SqliteDatabaseVersion;

public class SqliteDatabaseVersionTest {
	public class TestVersion extends SqliteDatabaseVersion {
		protected TestVersion(String version, String databasePath) {
			super(version, databasePath);
		}

		@Override
		public void run() {
			// ignore
		}
	}
	
	@Test
	public void testCompare() {
		TestVersion v1 = new TestVersion("0.0.5", "");
		TestVersion v2 = new TestVersion("0.0.5", "");
		TestVersion v3 = new TestVersion("0.0.6", "");
		TestVersion v4 = new TestVersion("0.0.4", "");
		TestVersion v5 = new TestVersion("1.0.4", "");
		TestVersion v6 = new TestVersion("0.4.4", "");
		
		assertTrue(v1.compareTo(v2) == 0);
		assertTrue(v1.compareTo(v3) < 0);
		assertTrue(v1.compareTo(v4) > 0);
		assertTrue(v5.compareTo(v1) > 0);
		assertTrue(v5.compareTo(v1) > 0);
		assertTrue(v6.compareTo(v3) > 0);
		assertTrue(v6.compareTo(v5) < 0);
	}
	
	@Test
	public void testToString() {
		TestVersion v1 = new TestVersion("1.0.4", "");
		TestVersion v2 = new TestVersion("0.0.6", "");
		
		assertEquals("1.0.4", v1.toString());
		assertEquals("0.0.6", v2.toString());
	}
}
