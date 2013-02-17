package com.homeki.core.main;

public class Configuration {
	public static boolean MOCK_ENABLED = false;
	public final static int TIMER_THREAD_INTERVAL = 10*1000;
	public final static int ONEWIRE_DETECTOR_INTERVAL = 2*60*1000;
	public final static int ONEWIRE_COLLECTOR_INTERVAL = 15*60*1000;
	public final static int REPORTER_INTERVAL = 24*60*1000;
	//public final static String REPORTER_URL = "http://localhost:8888/report";
	public final static boolean REPORTER_ENABLED = true;
	public final static String REPORTER_URL = "http://report.homeki.com";
	public final static String ONEWIRE_PATH = "/mnt/1wire";
	public final static String WEBROOT_PATH = "/opt/homeki/www";
	//public final static String WEBROOT_PATH = "/home/dev/workspace/homeki/homekiweb";
}
