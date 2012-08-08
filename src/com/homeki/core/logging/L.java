package com.homeki.core.logging;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class L {
	public static void init() {
		LogManager.getLogManager().reset();
		Logger log = Logger.getLogger("");
		log.addHandler(new CustomConsoleHandler());
	}
	
	public static void i(String msg) {
		Logger.getLogger("").info(msg);
	}
	
	public static void w(String msg) {
		Logger.getLogger("").warning(msg);
	}
	
	public static void e(String msg) {
		Logger.getLogger("").severe(msg);
	}
	
	public static void e(String msg, Throwable e) {
		msg += "\n               Exception: " + e.getMessage();
		e(msg);
	}
}
