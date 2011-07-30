package com.homekey.core.log;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class L {
	public static final int LEVEL_DEBUG = 10;
	public static final int LEVEL_INFO = 20;
	public static final int LEVEL_WARN = 30;
	public static final int LEVEL_ERROR = 40;
	
	private static String std;
	private List<String> ignore;
	
	private int minLevel;
	
	List<StreamHolder> outs;
	private String label;
	
	static HashMap<String, L> all = new HashMap<String, L>();
	
	private String levelToString(int level) {
		if (level >= LEVEL_ERROR)
			return "ERROR";
		if (level >= LEVEL_WARN)
			return "WARN";
		if (level >= LEVEL_INFO)
			return "INFO";
		if (level >= LEVEL_DEBUG)
			return "DEBUG";
		return "NO PRIORITY";
	}
	
	public static L getLogger(String label) {
		if (!all.containsKey(label))
			all.put(label, new L(label));
		return all.get(label);
	}
	
	public static void setStandard(String standard) {
		std = standard;
		getLogger(standard);
		for (StreamHolder sh : getStd().outs) {
			if (sh.out.equals(System.out))
				return;
		}
		getLogger(standard).addOutput(System.out);
	}
	
	private static L getStd() {
		return getLogger(std);
	}
	
	public L(String label) {
		this.minLevel = LEVEL_DEBUG;
		this.outs = new ArrayList<StreamHolder>();
		this.label = label;
		this.ignore = new ArrayList<String>();
	}
	
	public void addOutput(PrintStream out, int minLevel, boolean showTime, boolean showDate) {
		outs.add(new StreamHolder(out, minLevel, showTime, showDate));
	}
	
	public void addOutput(PrintStream out) {
		for (StreamHolder sh : outs) {
			if (sh.out.equals(out))
				throw new RuntimeException("You already added this PrintStream?");
		}
		outs.add(new StreamHolder(out, LEVEL_DEBUG, true, false));
	}
	
	public void log(String message, int level) {
		log(message, level, false);
	}
	
	private void log(String message, int level, boolean removeDescriptor) {
		if (minLevel > level)
			return;
		for (StreamHolder sh : outs) {
			if (sh.level <= level)
				addLog(message, sh, level, removeDescriptor);
		}
	}
	
	private void addLog(String message, StreamHolder sh, int level, boolean removeDescriptor) {
		if (ignoreThis(message))
			return;
		String full = "";
		full += "[" + label;
		Calendar c = Calendar.getInstance();
		if (sh.showDate || sh.showTime) {
			full = full + " | ";
			if (sh.showDate) {
				full += c.get(Calendar.YEAR) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.DAY_OF_MONTH);
			}
			if (sh.showTime) {
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				full += sdf.format(c.getTime());;
			}
		} else {}
		full += " | " + Thread.currentThread().getName();
		full += "] ";
		full += levelToString(level) + ": ";
		if (removeDescriptor) {
			full = "                                                                                      ".substring(0, full.length());
		}
		
		full += message;
		sh.out.println(full);
	}
	
	class StreamHolder {
		public PrintStream out;
		public int level;
		private boolean showTime;
		private boolean showDate;
		
		public StreamHolder(PrintStream out, int level, boolean showTime, boolean showDate) {
			this.level = level;
			this.out = out;
			this.showTime = showTime;
			this.showDate = showDate;
		}
	}
	
	public void setMinimumLevel(int minLevel) {
		this.minLevel = minLevel;
	}
	
	public void log(String message) {
		log(message, LEVEL_DEBUG);
	}
	
	public static void d(String msg) {
		getStd().log(msg, LEVEL_DEBUG);
	}
	
	public static void w(String msg) {
		getStd().log(msg, LEVEL_WARN);
	}
	
	public static void i(String msg) {
		getStd().log(msg, LEVEL_INFO);
	}
	
	public static void e(String msg) {
		getStd().log(msg, LEVEL_ERROR);
	}
	
	public static void d(Object obj, String msg) {
		if (!getStd().ignoreThis(msg)) {
			getStd().log(msg, LEVEL_DEBUG, false);
			getStd().log(obj.toString(), LEVEL_DEBUG, true);
		}
	}
	
	private boolean ignoreThis(String msg) {
		for (String s : ignore) {
			if (msg.contains(s))
				return true;
		}
		return false;
	}
	
	public void addRemoveFilter(String string) {
		ignore.add(string);
	}
}
