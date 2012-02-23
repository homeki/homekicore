package com.homeki.core.http.json;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;


public class JsonServerInfo {
	Long uptime;
	Long time;
	String version;
	
	public JsonServerInfo() {
		time = System.currentTimeMillis();
		Package p = getClass().getPackage();
		version = p.getImplementationVersion();
		if (version == null)
			version = "(DEV)";
	    RuntimeMXBean mx = ManagementFactory.getRuntimeMXBean();
	    uptime = mx.getUptime();
	}
}
