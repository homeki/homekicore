package com.homeki.core.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.homeki.core.storage.ISettingsTable;
import com.homeki.core.storage.ITableFactory;

public class SqliteSettingsTableTest {
	private ISettingsTable settingsTable;

	@Before
	public void setUp() throws Exception {
		ITableFactory factory = TestUtil.getEmptySqliteTestTableFactory();
		settingsTable = factory.getSettingsTable();
		settingsTable.ensureTable();
	}
	
	@Test
	public void testString() {
		settingsTable.setString("key1", "value1");
		settingsTable.setString("key2", "value2");
		assertEquals("value2", settingsTable.getString("key2"));
		assertEquals("value1", settingsTable.getString("key1"));
		settingsTable.setString("key1", "value1-changed");
		assertEquals("value2", settingsTable.getString("key2"));
		assertEquals("value1-changed", settingsTable.getString("key1"));
	}
	
	@Test
	public void testInt() {
		settingsTable.setInt("key1", 10);
		settingsTable.setInt("key2", -11);
		assertEquals(10, settingsTable.getInt("key1"));
		assertEquals(-11, settingsTable.getInt("key2"));
		settingsTable.setInt("key1", 12);
		assertEquals(12, settingsTable.getInt("key1"));
		assertEquals(-11, settingsTable.getInt("key2"));
	}
}
