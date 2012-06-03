package com.homeki.core.main;

import com.homeki.core.storage.DatabaseManager;

public class MainWithClearDb {
	public static void main(String[] args) throws Exception {
		DatabaseManager mgr = new DatabaseManager();

		L.i("Dropping all database tables...");
		mgr.dropAll();
		L.i("All database tables dropped.");
		
		new ThreadMaster().launch();
	}
}
