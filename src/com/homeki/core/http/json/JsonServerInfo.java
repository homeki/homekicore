package com.homeki.core.http.json;

import java.lang.management.ManagementFactory;
import java.util.Date;

import com.homeki.core.main.Util;


public class JsonServerInfo {
	Long uptimeMs;
	Long timeMs;
	String time;
	String version;
	
	public JsonServerInfo() {
		time = Util.getDateTimeFormat().format(new Date());
		version = Util.getVersion();
		timeMs = System.currentTimeMillis();
	    uptimeMs = ManagementFactory.getRuntimeMXBean().getUptime();
	}
}
