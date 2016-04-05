package com.homeki.core.main;

import com.homeki.core.logging.L;
import com.homeki.core.storage.DatabaseManager;

public class Configuration {
	public static boolean ENABLE_CORS_HEADERS = false;
	public static boolean MOCK_ENABLED = false;
	public static int ONEWIRE_DETECTOR_INTERVAL = 2 * 60 * 1000;
	public static int ONEWIRE_COLLECTOR_INTERVAL = 1 * 60 * 1000;
	public static int REPORTER_INTERVAL = 30 * 60 * 1000;
	public static boolean REPORTER_ENABLED = true;
	public static int SUNSET_SUNRISE_OFFSET_MINUTES = 90;
	public static String REPORTER_URL = "http://report.homeki.com";
	public static String ONEWIRE_PATH = "/mnt/1wire";
	public static String WEBROOT_PATH = "/opt/homeki/www";

	public static void transformForDev() {
		L.i("Development version detected, adjusting configuration.");
		Configuration.MOCK_ENABLED = true;
		Configuration.REPORTER_ENABLED = false;
		Configuration.REPORTER_URL = "http://10.0.2.2:8080/report";
		Configuration.ENABLE_CORS_HEADERS = true;
	}

	public static void transformForTest() {
		L.i("Test version detected, adjusting configuration.");

		DatabaseManager mgr = new DatabaseManager();
		L.i("Dropping all database tables...");
		try {
			mgr.dropAll();
			L.i("All database tables dropped.");
		} catch (Exception e) {
			L.e("Failed to drop database tables.", e);
		}

		Configuration.REPORTER_ENABLED = false;
		Configuration.ENABLE_CORS_HEADERS = true;
	}
}
