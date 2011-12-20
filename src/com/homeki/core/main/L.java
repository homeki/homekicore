package com.homeki.core.main;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class L {
	public final static int INFO = 1;
	public final static int WARN = 2;
	public final static int ERROR = 3;
	
	private static int min = INFO;
	
	public static void setMinimumLevel(int level) {
		min = level;
	}
	
	public static void i(String msg) {
		log(msg, INFO, System.out);
	}
	
	public static void w(String msg) {
		log(msg, WARN, System.out);
	}
	
	public static void e(String msg) {
		log(msg, ERROR, System.err);
	}
	
	public static void e(String msg, Exception ex) {
		msg += "\n               Exception: " + ex.getMessage();
		log(msg, ERROR, System.err);
	}
	
	private static void log(String msg, int level, PrintStream ps) {
		if (level < min)
			return;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ps.println("[" + sdf.format(new Date()) + " | " + Thread.currentThread().getName() + "] " + msg);
	}
}
