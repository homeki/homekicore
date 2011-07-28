package com.homekey.core.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class HttpArguments {
	
	public static Integer demandInteger(String key, HashMap<String, String> args, DataOutputStream out) throws IOException {
		if (args.containsKey(key)) {
			int val = 0;
			try {
				val = Integer.parseInt(args.get(key));
			} catch (Exception e) {
				HttpMacro.sendResponse(400, "The key '" + args.get(key) + "' is not an integer.", out);
				return null;
			}
			return val;
		} else {
			HttpMacro.sendResponse(400, "Did not find parameter '" + key +"'.", out);
		}
		return null;
	}
}
