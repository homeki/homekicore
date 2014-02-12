package com.homeki.core.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

public class L {
	private static final String LOG_NAME = "org.homeki.core";
	
	public static void i(String msg) {
		Logger.getLogger(LOG_NAME).info(msg);
	}
	
	public static void w(String msg) {
		Logger.getLogger(LOG_NAME).warning(msg);
	}
	
	public static void e(String msg) {
		Logger.getLogger(LOG_NAME).severe(msg);
	}
	
	public static void e(String msg, Throwable e) {
		msg += "\n";
		msg += stackTraceToString(e);
		e(msg);
	}

	private static String stackTraceToString(Throwable e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}
