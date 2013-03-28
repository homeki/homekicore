package com.homeki.core.http.json;

import java.lang.management.ManagementFactory;
import java.util.Date;

import com.homeki.core.main.Util;


public class JsonServerInfo {
	public Long uptimeMs;
	public Long timeMs;
	public String time;
	public String version;
	public String name;
	public Double locationLongitude;
	public Double locationLatitude;
	
	public JsonServerInfo(String name, Double longitude, Double latitude) {
		this.name = name;
		this.time = Util.getDateTimeFormat().format(new Date());
		this.version = Util.getVersion();
		this.timeMs = System.currentTimeMillis();
		this.uptimeMs = ManagementFactory.getRuntimeMXBean().getUptime();
		this.locationLongitude = longitude;
		this.locationLatitude = latitude;
	}
}
