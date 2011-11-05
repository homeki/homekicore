package com.homeki.core.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.homeki.core.log.L;

public class HttpGetResolver {
	
	public enum Actions {
		ECHO, TIME, ON, OFF, DIM, DEVICES, STATUS, HISTORY, BAD_ACTION, NO_ACTION
	}
	
	public static void resolve(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		if (!st.hasMoreTokens()) {
			HttpMacro.send404("No token?", out);
		} else {
			String token = st.nextToken();
			if (token.equals("get") || token.equals("set")) {
				resolveGet(st, api, out);
			} else {
				HttpMacro.send404("Missing tokens. Expected get or set action.", out);
			}
		}
	}
	
	public static boolean eat(StringTokenizer st, String mustBe) {
		if (st.hasMoreTokens() && st.nextToken().equals(mustBe))
			return true;
		return false;
	}
	
	private static void resolveGet(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		Actions action = Actions.NO_ACTION;
		String token = "";
		if (st.hasMoreTokens()) {
			token = st.nextToken();
			L.d(HttpGetResolver.class, "Got token '" + token + "'");
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
		case DIM:
			resolveGetDim(st, api, out);
			break;
		case DEVICES:
			resolveGetDevices(st, api, out);
			break;
		case HISTORY:
			resolveGetHistory(st, api, out);
			break;
		case ON:
			resolveGetOn(st, api, out, true);
			break;
		case OFF:
			resolveGetOn(st, api, out, false);
			break;
		case BAD_ACTION:
			HttpMacro.send405("Command 'get' does not contain resource '" + token + "'.", out);
			break;
		case NO_ACTION:
			HttpMacro.send404("No action specified.", out);
			break;
		default:
			HttpMacro.send404("Your request is wicked in some way.", out);
			break;
		}
	}
	
	private static void resolveGetStatus(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		HashMap<String, String> args = getArguments(st);
		Integer id = HttpArguments.demandInteger("id", args, out);
		
		HttpMacro.sendResponse(200, api.getStatus(id), out);
	}
	
	private static void resolveGetDim(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		HashMap<String, String> args = getArguments(st);
		Integer id = HttpArguments.demandInteger("id", args, out);
		Integer level = HttpArguments.demandInteger("level", args, out);
		
		if (id != null && level != null) {
			try {
				api.dim(id, level);
			} catch (ClassCastException e) {
				HttpMacro.send404("The device with id " + id + " is not dimmable.", out);
				return;
			}
			HttpMacro.sendResponse(200, "Device " + id + " has level " + level + ".", out);
		}
	}
	
	private static HashMap<String, String> getArguments(StringTokenizer st) {
		HashMap<String, String> args = new HashMap<String, String>();
		while (st.hasMoreTokens()) {
			String[] arg = st.nextToken().split("=");
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
				HttpMacro.send404("The device with id " + id + " is not switchable.", out);
				return;
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
	
	private static void resolveGetHistory(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		HashMap<String, String> args = getArguments(st);
		
		Integer id = HttpArguments.demandInteger("id", args, out);
		Date from = HttpArguments.demandDate("from", args, out);
		Date to = HttpArguments.demandDate("to", args, out);
		
		if (id != null && from != null && to != null) {
			HttpMacro.sendResponse(200, api.getHistory(id, from, to), out);
		}
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
