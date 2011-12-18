package com.homeki.core.tests;

import java.io.File;

import com.homeki.core.main.Util;

public class TestUtil {
	private final static String TEST_DATABASE_PATH = "/tmp/homekitest.db";
	private final static String TEXT_DIR_PATH = "test/texts/";
	
	public static Object getEmptySqliteTestTableFactory() {
		/*removeDbIfExists(TEST_DATABASE_PATH);
		ITableFactory dbf = new SqliteTableFactory(TEST_DATABASE_PATH);
		dbf.ensureTables();
		return dbf;*/
		return null;
	}
	
	public static String getStringFromTextFile(String fileName) {
		return Util.fromFile(TEXT_DIR_PATH + "/" + fileName);
	}
	
	private static void removeDbIfExists(String path) {
		File f = new File(path);
		if (f.exists())
			f.delete();
	}
}
