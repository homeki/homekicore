package com.homekey.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.homekey.core.storage.IDeviceTable;
import com.homekey.core.storage.ITableFactory;


public class SqliteDeviceTableTest {
	private IDeviceTable deviceTable;
	
	private static final String INTERNAL_ID_1 = "int1";
	private static final String INTERNAL_ID_2 = "int2";
	private static final String INTERNAL_ID_3 = "changed";
	private static final String TYPE_1 = "DeviceType";
	private static final String TYPE_2 = "AnotherDeviceType";
	private static final String NAME_1 = "a device name #1";
	private static final String NAME_2 = "a device name #2";
	
	@Before
	public void setUp() throws Exception {
		ITableFactory factory = TestUtil.getEmptySqliteTestTableFactory();
		deviceTable = factory.getDeviceTable();
	}
	
	@Test
	public void testCreateRowAndGetId() {
		deviceTable.createRow(INTERNAL_ID_1, TYPE_1);
		int id = deviceTable.createRow(INTERNAL_ID_2, TYPE_2);
		assertEquals(2, id);
	}
	
	@Test
	public void testGetAdded() {
		addSomeRows();
		
		Date d = deviceTable.getAdded(1);
		
		assert(d != null);
		assert(Calendar.getInstance().get(Calendar.YEAR) >= 2011);
	}
	
	@Test
	public void testGetSetInternalId() {
		addSomeRows();
		
		String internalId1 = deviceTable.getInternalId(1);
		String internalId2 = deviceTable.getInternalId(2);
		
		assertEquals(INTERNAL_ID_1, internalId1);
		assertEquals(INTERNAL_ID_2, internalId2);
		
		deviceTable.setInternalId(2, INTERNAL_ID_3);
		internalId1 = deviceTable.getInternalId(1);
		internalId2 = deviceTable.getInternalId(2);
		
		assertEquals(INTERNAL_ID_1, internalId1);
		assertEquals(INTERNAL_ID_3, internalId2);
	}
	
	@Test
	public void testGetSetName() {
		addSomeRows();
		
		deviceTable.setName(1, NAME_1);
		deviceTable.setName(2, NAME_2);
		
		String name1 = deviceTable.getName(1);
		String name2 = deviceTable.getName(2);
		
		assertEquals(NAME_1, name1);
		assertEquals(NAME_2, name2);
		
		deviceTable.setName(1, "");
		name1 = deviceTable.getName(1);
		name2 = deviceTable.getName(2);
		
		assertEquals("", name1);
		assertEquals(NAME_2, name2);
	}
	
	@Test
	public void testGetSetActive() {
		addSomeRows();
		
		assert(deviceTable.isActive(1));
		assert(deviceTable.isActive(2));
		
		deviceTable.setActive(2, false);
		
		assert(deviceTable.isActive(1));
		assert(!deviceTable.isActive(2));
	}
	
	@Test
	public void testGetSetType() {
		addSomeRows();
		
		deviceTable.setType(1, TYPE_1);
		deviceTable.setType(2, TYPE_2);
		
		String type1 = deviceTable.getType(1);
		String type2 = deviceTable.getType(2);
		
		assertEquals(TYPE_1, type1);
		assertEquals(TYPE_2, type2);
		
		deviceTable.setType(1, "");
		type1 = deviceTable.getType(1);
		type2 = deviceTable.getType(2);
		
		assertEquals("", type1);
		assertEquals(TYPE_2, type2);
	}
	
	@Test
	public void testRowExists() {
		addSomeRows();
		
		assert(deviceTable.rowExists(INTERNAL_ID_1));
		assertFalse(deviceTable.rowExists(INTERNAL_ID_3));
	}
	
	private void addSomeRows() {
		deviceTable.createRow(INTERNAL_ID_1, TYPE_1);
		deviceTable.createRow(INTERNAL_ID_2, TYPE_2);
	}
}
