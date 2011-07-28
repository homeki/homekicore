package com.homekey.core.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

import com.homekey.core.http.HttpGetResolver.Actions;

public class HttpSetResolver {
	public enum Actions {
		BAD_ACTION
	}
	public static boolean resolve(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		if (!st.hasMoreTokens())
			return false;
		if (st.nextToken().equals("GET")) {
			if (HttpGetResolver.eat(st, "set")) {
				return resolveSet(st, api, out);
			}
		}
		return false;
	}
	
	private static boolean resolveSet(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		if (!st.hasMoreTokens())
			return false;
		String token = st.nextToken();
		Actions action;
		try {
			action = Actions.valueOf(token.toUpperCase());
		} catch (IllegalArgumentException e) {
			action = Actions.BAD_ACTION;
		}
		
		return false;
	}
}
