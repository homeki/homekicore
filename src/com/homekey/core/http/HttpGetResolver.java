package com.homekey.core.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

public class HttpGetResolver {
	
	public enum Actions {
		ECHO, TIME, ON, OFF, DIM, DEVICES, STATUS, BAD_ACTION,NO_ACTION
	}
	
	public static void resolve(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		if (!st.hasMoreTokens()) {
			HttpMacro.send404("Missing tokens.", out);
		} else {
			String token = st.nextToken();
			if (token.equals("get") || token.equals("set")) {
				resolveGet(st, api, out);
			} else {
				HttpMacro.send404("Missing tokens.", out);
			}
		}
	}
	
	public static boolean eat(StringTokenizer st, String mustBe) {
		if (st.hasMoreTokens() && st.nextToken().equals(mustBe))
			return true;
		return false;
	}
	
	// TODO: fix static boolean issue
	private static void resolveGet(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		Actions action = Actions.NO_ACTION;
		String token = "";
		if (st.hasMoreTokens()) {
			token = st.nextToken();
			try {
				action = Actions.valueOf(token.toUpperCase());
			} catch (IllegalArgumentException e) {
				action = Actions.BAD_ACTION;
			}
		}
		
		switch (action) {
		case ECHO:
			resolveGetEcho(st, api, out);
			break;
		case TIME:
			resolveGetTime(st, api, out);
			break;
		case STATUS:
			resolveGetStatus(st, api, out);
			break;
		case DEVICES:
			resolveGetDevices(st, api, out);
			break;
		case ON:
			resolveGetOn(st, api, out, true);
			break;
		case OFF:
			resolveGetOn(st, api, out, false);
			break;
		case BAD_ACTION:
			HttpMacro.send404("Get does not allow command '" + token + "'.", out);
			break;
		case NO_ACTION:
			HttpMacro.send404("No action specified.'.", out);
			break;
		}
	}
	
	private static void resolveGetStatus(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		HashMap<String, String> args = getArguments(st);
		Integer id = HttpArguments.demandInteger("id", args, out);
		HttpMacro.sendResponse(200, api.getStatus(id), out);
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
	
	private static void resolveGetOn(StringTokenizer st, HttpApi api, DataOutputStream out, boolean on) throws IOException {
		HashMap<String, String> args = getArguments(st);
		Integer id = HttpArguments.demandInteger("id", args, out);
		if (id == null) {
			HttpMacro.send404("id is not a string.", out);
		} else {
			try {
				if (on) {
					api.switchOn(id);
				} else {
					api.switchOff(id);
				}
			} catch (ClassCastException e) {
				HttpMacro.send404("The device with id=" + id + " is not switchable.", out);
			}
			HttpMacro.sendResponse(200, "Device " + id + " is now " + (on ? "on" : "off") + ".", out);
		}
	}
	
	private static void resolveGetDevices(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		HttpMacro.sendResponse(200, api.getDevices(), out);
	}
	
	private static void resolveGetTime(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		HttpMacro.sendResponse(200, "Time is: " + (new Date()).toString(), out);
	}
	
	private static void resolveGetEcho(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		if (!st.hasMoreTokens())
			HttpMacro.sendResponse(204, "No content to echo.", out);
		else {
			String token = st.nextToken();
			HttpMacro.sendResponse(200, token, out);
		}
	}
}
