package com.homekey.core.tests;

import java.io.File;

import com.homekey.core.main.Util;
import com.homekey.core.storage.Database;
import com.homekey.core.storage.sqlite.SqliteDatabase;

public class TestUtil {
	private final static String TEST_DATABASE_PATH = "/tmp/homekeytest.db";
	private final static String TEXT_DIR_PATH = "test/texts/";
	
	public static Database getEmptyTestDatabase() {
		removeDbIfExists(TEST_DATABASE_PATH);
		return new SqliteDatabase(TEST_DATABASE_PATH);	
	}
	
	public static String getStringFromTextFile(String fileName) {
		return Util.fromFile(TEXT_DIR_PATH + "/" + fileName);
	}
	
	private static void removeDbIfExists(String path) {
		File f = new File(path);
		if (f.exists()) {
			f.delete();
		}
	}
}
