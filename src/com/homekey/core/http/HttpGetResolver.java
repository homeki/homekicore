package com.homekey.core.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.StringTokenizer;

public class HttpGetResolver {
	
	public static boolean resolve(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		if (!st.hasMoreTokens())
			return false;
		if (eat(st, "get")) {
			return resolveGet(st,api, out);
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
		if (token.equals("echo")) {
			return resolveGetEcho(st,api, out);
		} else if (token.equals("time")) {
			return resolveGetTime(st,api, out);
		} else if (token.equals("devices")) {
			return resolveGetDevices(st,api, out);
		}
		return false;
	}
	
	private static boolean resolveGetDevices(StringTokenizer st, HttpApi api, DataOutputStream out) {
		
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
