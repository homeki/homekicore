package com.homeki.core.main;

import com.homeki.core.logging.L;

public class Configuration {
	public static boolean MOCK_ENABLED = false;
	public static int TIMER_THREAD_INTERVAL = 10*1000;
	public static int ONEWIRE_DETECTOR_INTERVAL = 2*60*1000;
	public static int ONEWIRE_COLLECTOR_INTERVAL = 15*60*1000;
	public static int REPORTER_INTERVAL = 30*60*1000;
	public static boolean REPORTER_ENABLED = true;
	public static int SUNSET_SUNRISE_OFFSET_MINUTES = 90;
	public static String REPORTER_URL = "http://report.homeki.com";
	public static String ONEWIRE_PATH = "/mnt/1wire";
	public static String WEBROOT_PATH = "/opt/homeki/www";
	
	public static void transformForDev() {
		L.i("Development version detected, adjusting configuration.");
		Configuration.REPORTER_ENABLED = false;
		Configuration.REPORTER_URL = "http://localhost:8888/report";
		Configuration.WEBROOT_PATH = "/home/dev/workspace/homeki/homekiweb";
	}
	
	public static void transformForTest() {
		L.i("Test version detected, adjusting configuration.");
		Configuration.REPORTER_ENABLED = false;
		Configuration.MOCK_ENABLED = false;
	}
}
