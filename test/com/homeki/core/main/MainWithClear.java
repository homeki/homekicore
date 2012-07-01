package com.homeki.core.main;

import com.homeki.core.storage.DatabaseManager;

public class MainWithClear {
	public static void main(String[] args) throws Exception {
		DatabaseManager mgr = new DatabaseManager();
		L.i("MainWithClear: Dropping all database tables...");
		mgr.dropAll();
		L.i("MainWithClear: All database tables dropped.");
		
		Configuration.MOCK_ENABLED = false;
		L.i("MainWithClear: Disabled mock devices to prevent conflicts.");
		
		new ThreadMaster().launch();
	}
}
