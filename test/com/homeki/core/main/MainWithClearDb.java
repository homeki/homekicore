package com.homeki.core.main;

import com.homeki.core.device.tellstick.TellStickNative;
import com.homeki.core.storage.DatabaseManager;

public class MainWithClearDb {
	public static void main(String[] args) throws Exception {
		DatabaseManager mgr = new DatabaseManager();
		L.i("Dropping all database tables...");
		mgr.dropAll();
		L.i("All database tables dropped.");
		
		Configuration.MOCK_ENABLED = true;
		L.i("Enabled mock devices.");
		
		new ThreadMaster().launch();
	}
}
