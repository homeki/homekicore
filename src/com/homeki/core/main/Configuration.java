package com.homeki.core.main;

public class Configuration {
	public final static boolean MOCK_ENABLED = false;
	public final static int TIMER_THREAD_INTERVAL = 10*1000;
	public final static int ONEWIRE_DETECTOR_INTERVAL = 30*1000;
	public final static int ONEWIRE_COLLECTOR_INTERVAL = 10*1000;
	public final static String ONEWIRE_PATH = "/mnt/1wire";
	public final static int HTTP_THREAD_POOL_SIZE = 10;
}
