package com.homekey.core.tests;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.homekey.core.storage.IHistoryTable;
import com.homekey.core.storage.ITableFactory;

public class SqliteHistoryTableTest {
	private IHistoryTable historyTable;
	
	private static final String TABLE_NAME = "D_TestTable_1";
	
	@Before
	public void setUp() throws Exception {
		ITableFactory factory = TestUtil.getEmptySqliteTestTableFactory();
		historyTable = factory.getHistoryTable(TABLE_NAME, Float.class);
		historyTable.ensureTable();
	}
	
	@After
	public void tearDown() throws Exception {
		
	}

	
	@Test
	public void testPutValue() {
		putValues();
	}
	
	@Test
	public void testGetValue() {
		putValues();
		float f = (Float)historyTable.getLatestValue();
		assertEquals(-13.5f, f, 0.0f);
	}
	
	private void putValues() {
		historyTable.putValue(getDate(2011, 11, 11, 17, 18, 19), 12.5f);
		historyTable.putValue(getDate(2011, 11, 11, 17, 18, 20), -13.5f);
	}
	
	private Date getDate(int year, int month, int day, int hour, int minute, int second) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, second);
		return c.getTime();
	}
}
