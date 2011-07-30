package com.homekey.core.tests;

import org.junit.After;
import org.junit.Before;

import com.homekey.core.device.tellstick.TellStickSwitch;
import com.homekey.core.storage.Database;

public class TellStickSwitchTest {
	private static final String INTERNAL_ID = "mock1";
	private static final String NAME = "My Device #5";
	
	// TODO: Linus: implement more testing
	
	private TellStickSwitch device;
	private Database db;
	
	@Before
	public void setUp() throws Exception {
		db = TestUtil.getEmptyTestDatabase();
		device = new TellStickSwitch(INTERNAL_ID, db);
		device.setName(NAME);
	}
	
	@After
	public void tearDown() throws Exception {
		db.close();
	}
}
