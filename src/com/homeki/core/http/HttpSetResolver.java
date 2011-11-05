package com.homeki.core.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

public class HttpSetResolver {
	public enum Actions {
		BAD_ACTION
	}
	public static boolean resolve(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		if (!st.hasMoreTokens())
			return false;
		String httpMethod = st.nextToken();
		if (httpMethod.equals("GET")) {
			if (HttpGetResolver.eat(st, "set")) {
				return resolveSet(st, api, out);
			}
		}
		else if (httpMethod.equals("POST")) {
			
		}
		return false;
	}
	
	private static boolean resolveSet(StringTokenizer st, HttpApi api, DataOutputStream out) throws IOException {
		if (!st.hasMoreTokens())
			return false;		
		return false;
	}
}
