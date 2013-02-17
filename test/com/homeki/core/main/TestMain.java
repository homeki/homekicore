package com.homeki.core.main;

import com.homeki.core.logging.L;
import com.homeki.core.storage.DatabaseManager;

public class TestMain {
	public static void main(String[] args) throws Exception {
		DatabaseManager mgr = new DatabaseManager();
		L.i("TestMain: Dropping all database tables...");
		mgr.dropAll();
		L.i("TestMain: All database tables dropped.");
		
		Configuration.transformForTest();
		new Homeki().launch();
	}
}
