package com.homeki.core.tests;

import org.junit.Before;

import com.homeki.core.storage.ISettingsTable;
import com.homeki.core.storage.ITableFactory;

public class SqliteSettingsTableTest {
	private ISettingsTable settingsTable;

	private int ID_1, ID_2;

	@Before
	public void setUp() throws Exception {
		ITableFactory factory = TestUtil.getEmptySqliteTestTableFactory();
		settingsTable = factory.getSettingsTable();
	}
}
