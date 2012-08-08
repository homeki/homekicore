package com.homeki.core.main;

import com.homeki.core.logging.L;
import com.homeki.core.storage.DatabaseManager;

public class MainClear {
	public static void main(String[] args) throws Exception {
		DatabaseManager mgr = new DatabaseManager();
		L.i("MainClear: Dropping all database tables...");
		mgr.dropAll();
		L.i("MainClear: All database tables dropped.");
		
		Configuration.MOCK_ENABLED = false;
		L.i("MainClear: Disabled mock devices to prevent conflicts.");
		
		new ThreadMaster().launch();
	}
}
