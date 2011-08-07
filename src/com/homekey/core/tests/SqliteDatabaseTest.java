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
	private static final String TABLE_NAME = "table1";
	private static final String[] COLUMNS = new String[] { "name", "integernumber", "realnumber" };
	private static final Object[] JONAS_ROW = new Object[] { "jonas", 1986, 1986.05 };
	private static final Object[] MARCUS_ROW = new Object[] { "marcus", 1986, 1986.04 };
	private static final Object[] LINUS_ROW = new Object[] { "linus", 1986, 1986.03 };
	private static final String ID_FIELD = "id";
	
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
		db.addRow(TABLE_NAME, COLUMNS, JONAS_ROW);
		db.addRow(TABLE_NAME, COLUMNS, MARCUS_ROW);
		db.addRow(TABLE_NAME, COLUMNS, LINUS_ROW);
	}
	
	private void createTestTable() {
		DatabaseTable table = new DatabaseTable(3);
		table.setColumn(0, COLUMNS[0], ColumnType.STRING);
		table.setColumn(1, COLUMNS[1], ColumnType.INTEGER);
		table.setColumn(2, COLUMNS[2], ColumnType.DOUBLE);
		db.createTable(TABLE_NAME, table);
	}
	
	@Test
	public void testCreateTableAndTableExists() {
		createTestTable();
		assertTrue(db.tableExists(TABLE_NAME));
	}
	
	@Test
	public void testAddRowsAndGetTopFieldOrderBy() {
		createTestTable();
		addSomeRows();
		int id = (Integer)db.getTopFieldOrderByDescending(TABLE_NAME, ID_FIELD, ID_FIELD);
		assertEquals("The row should exist with ID 1 since it is the first row.", id, 3);
	}
	
	@Test
	public void testGetRow() {
		createTestTable();
		addSomeRows();
		Object[] objs = db.getRow(TABLE_NAME, COLUMNS, ID_FIELD, 2);
		assertEquals(MARCUS_ROW[0], objs[0]);
		assertEquals(MARCUS_ROW[1], objs[1]);
		assertEquals(MARCUS_ROW[2], objs[2]);
	}
	
	@Test
	public void testGetSingleField() {
		createTestTable();
		addSomeRows();
		int value = (Integer)db.getField(TABLE_NAME, COLUMNS[1], ID_FIELD, 2);
		assertEquals(MARCUS_ROW[1], value);
		Double fvalue = (Double)db.getField(TABLE_NAME, COLUMNS[2], ID_FIELD, 2);
		assertEquals(MARCUS_ROW[2], fvalue);
	}
	
	@Test
	public void testUpdateRow() {
		final Object[] NIKLAS_ROW = new Object[] { "niklas", 1987, 1987.04 };
		
		createTestTable();
		addSomeRows();
		db.updateRow(TABLE_NAME, COLUMNS, NIKLAS_ROW, ID_FIELD, 2);
		Object[] objs = db.getRow(TABLE_NAME, COLUMNS, ID_FIELD, 2);
		assertEquals(NIKLAS_ROW[0], objs[0]);
		assertEquals(NIKLAS_ROW[1], objs[1]);
		assertEquals(NIKLAS_ROW[2], objs[2]);
	}
	
	@Test
	public void testSystemTablesCreated() {
		assertTrue(db.tableExists("devices"));
	}
}
