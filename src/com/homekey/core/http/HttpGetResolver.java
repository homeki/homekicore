package com.homekey.core.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.homekey.core.http.json.JsonStatus;

public class HttpGetResolver {
	
	public enum Actions {
		ECHO, TIME, ON, OFF, DIM, DEVICES, STATUS, BAD_ACTION
	}
	
	public static boolean resolve(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		if (!st.hasMoreTokens())
			return false;
		String token = st.nextToken();
		if (token.equals("get") || token.equals("set")) {
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
		case STATUS:
			return resolveGetStatus(st, api, out);
		case DEVICES:
			return resolveGetDevices(st, api, out);
		case ON:
			return resolveGetOn(st, api, out, true);
		case OFF:
			return resolveGetOn(st, api, out, false);
		case BAD_ACTION:
			HttpMacro.send404("Get does not allow command '" + token + "'.", out);
			return true;
		default:
			return false;
		}
	}
	
	private static boolean resolveGetStatus(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		HashMap<String, String> args = getArguments(st);
		Integer id = HttpArguments.demandInteger("id", args, out);
		HttpMacro.sendResponse(200, api.getStatus(id),out);
		return true;
	}
	
	private static HashMap<String, String> getArguments(StringTokenizer st) {
		HashMap<String, String> args = new HashMap<String, String>();
		while (st.hasMoreTokens()) {
			String[] arg = st.nextToken().split("=");
			System.out.println(arg[0] + "=" + arg[1]);
			args.put(arg[0], arg[1]);
		}
		return args;
	}
	
	private static boolean resolveGetOn(StringTokenizer st, HttpApi api, DataOutputStream out, boolean on) throws IOException {
		HashMap<String, String> args = getArguments(st);
		Integer id = HttpArguments.demandInteger("id", args, out);
		if (id == null)
			return true;
		if (on ? api.switchOn(id) : api.switchOff(id)) {
			HttpMacro.sendResponse(200, "Device " + id + " is now " + (on ? "on" : "off") + ".", out);
			return true;
		} else {
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
