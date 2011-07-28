package com.homekey.core.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.StringTokenizer;

public class HttpGetResolver {
	
	public enum Actions {
		ECHO, TIME, DEVICES,BAD_ACTION
	}
	
	public static boolean resolve(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		if (!st.hasMoreTokens())
			return false;
		if (eat(st, "get")) {
			return resolveGet(st, api, out);
		}
		return false;
	}
	
	public static boolean eat(StringTokenizer st, String mustBe) {
		if (st.hasMoreTokens() && st.nextToken().equals(mustBe))
			return true;
		return false;
	}
	
	private static boolean resolveGet(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		if (!st.hasMoreTokens())
			return false;
		
		String token = st.nextToken();
		Actions action;
		try {
			action = Actions.valueOf(token.toUpperCase());
		} catch (IllegalArgumentException e) {
			action = Actions.BAD_ACTION;
		}
		
		switch (action) {
		case ECHO:
			return resolveGetEcho(st, api, out);
		case TIME:
			return resolveGetTime(st, api, out);
		case DEVICES:
			return resolveGetDevices(st, api, out);
		case BAD_ACTION:
			HttpMacro.send404("Get does not allow command '" + token +"'.", out);
			return true;
		default:
			return false;
		}
	}
	
	private static boolean resolveGetDevices(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		HttpMacro.sendResponse(200, api.getDevices(), out);
		return true;
	}
	
	private static boolean resolveGetTime(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		HttpMacro.sendResponse(200, "Time is: " + (new Date()).toString(), out);
		return true;
	}
	
	private static boolean resolveGetEcho(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		if (!st.hasMoreTokens())
			HttpMacro.sendResponse(204, "No content to echo.", out);
		else {
			String token = st.nextToken();
			HttpMacro.sendResponse(200, token, out);
		}
		return true;
	}
}
