package com.homekey.core.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.homekey.core.storage.ColumnType;
import com.homekey.core.storage.Database;
import com.homekey.core.storage.DatabaseTable;

public class SqliteDatabaseTest {
	private Database db;
	
	@Before
	public void setUp() throws Exception {
		db = TestUtil.getEmptyTestDatabase();
	}
	
	@After
	public void tearDown() throws Exception {
		db.close();
	}
	
	private void addSomeRows() {
		db.addRow("table1", new String[] { "name", "integernumber", "realnumber" }, new Object[] { "jonas", 1986, 1986.05 });
		db.addRow("table1", new String[] { "name", "integernumber", "realnumber" }, new Object[] { "marcus", 1986, 1986.04 });
		db.addRow("table1", new String[] { "name", "integernumber", "realnumber" }, new Object[] { "linus", 1986, 1986.03 });
	}
	
	private void createTestTable() {
		DatabaseTable table = new DatabaseTable(3);
		table.setColumn(0, "name", ColumnType.String);
		table.setColumn(1, "integernumber", ColumnType.Integer);
		table.setColumn(2, "realnumber", ColumnType.Float);
		db.createTable("table1", table);
	}
	
	@Test
	public void testCreateTableAndTableExists() {
		createTestTable();
		assertTrue(db.tableExists("table1"));
	}
	
	@Test
	public void testAddRowsAndGetTopFieldOrderBy() {
		createTestTable();
		addSomeRows();
		int id = (Integer)db.getTopFieldOrderByDescending("table1", "id", "id");
		assertEquals("The row should exist with ID 1 since it is the first row.", id, 3);
	}
	
	@Test
	public void testGetRow() {
		createTestTable();
		addSomeRows();
		Object[] objs = db.getRow("table1", new String[] { "name", "integernumber", "realnumber" }, "id", 2);
		assertEquals("marcus", objs[0]);
		assertEquals(1986, objs[1]);
		assertEquals(1986.04, objs[2]);
	}
	
	@Test
	public void testGetSingleField() {
		createTestTable();
		addSomeRows();
		int value = (Integer)db.getField("table1", "integernumber", "id", 2);
		assertEquals(1986, value);
	}
	
	@Test
	public void testUpdateRow() {
		createTestTable();
		addSomeRows();
		db.updateRow("table1", new String[] { "name", "integernumber", "realnumber" }, new Object[] { "niklas", 1987, 1987.04 }, "id", 2);
		Object[] objs = db.getRow("table1", new String[] { "name", "integernumber", "realnumber" }, "id", 2);
		assertEquals("niklas", objs[0]);
		assertEquals(1987, objs[1]);
		assertEquals(1987.04, objs[2]);
	}
	
	@Test
	public void testSystemTablesCreated() {
		assertTrue(db.tableExists("devices"));
	}
}
