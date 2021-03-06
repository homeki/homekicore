package com.homeki.core.json;

import com.homeki.core.main.Util;

import java.lang.management.ManagementFactory;
import java.util.Date;


public class JsonServerInfo {
	public Long uptimeMs;
	public Long timeMs;
	public String time;
	public String version;
	public String name;
	public String hostname;
	public Double locationLatitude;
	public Double locationLongitude;
	public String smtpHost;
	public Integer smtpPort;
	public Boolean smtpAuth;
	public Boolean smtpTls;
	public String smtpUser;
	public String smtpPassword;

	public JsonServerInfo() {

	}
	
	public JsonServerInfo(String name, String hostname, Double latitude, Double longitude, String smtpHost, Integer smtpPort, Boolean smtpAuth, Boolean smtpTls, String smtpUser, String smtpPassword) {
		this.name = name;
		this.hostname = hostname;
		this.time = Util.getDateTimeFormat().format(new Date());
		this.version = Util.getVersion();
		this.timeMs = System.currentTimeMillis();
		this.uptimeMs = ManagementFactory.getRuntimeMXBean().getUptime();
		this.locationLatitude = latitude;
		this.locationLongitude = longitude;
		this.smtpHost = smtpHost;
		this.smtpPort = smtpPort;
		this.smtpAuth = smtpAuth;
		this.smtpTls = smtpTls;
		this.smtpUser = smtpUser;
		this.smtpPassword = smtpPassword;
	}
}
