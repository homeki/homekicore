package com.homekey.core.log;

import java.util.List;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class SuperLogger {
	public static final int LEVEL_DEBUG = 10;
	public static final int LEVEL_INFO = 20;
	public static final int LEVEL_WARN = 30;
	public static final int LEVEL_ERROR = 40;
	
	private int minLevel;
	
	List<StreamHolder> outs;
	private String label;
	
	static HashMap<String, SuperLogger> all = new HashMap<String, SuperLogger>();
	
	private String levelToString(int level){
		if(level >= LEVEL_ERROR )
			return "ERROR";
		if(level >= LEVEL_WARN )
			return "WARN";
		if(level >= LEVEL_INFO )
			return "INFO";
		if(level >= LEVEL_ERROR )
			return "DEBUG";
		return "NO PRIOROTY";
		
	}
	
	public static SuperLogger getLogger(String label) {
		if (!all.containsKey(label))
			all.put(label, new SuperLogger(label));
		return all.get(label);
	}
	
	public SuperLogger(String label) {
		this.minLevel = LEVEL_DEBUG;
		this.outs = new ArrayList<StreamHolder>();
		this.label = label;
	}
	
	public void addOutput(PrintStream out, int minLevel, boolean showTime, boolean showDate) {
		outs.add(new StreamHolder(out, minLevel, showTime, showDate));
	}
	
	public void addOutput(PrintStream out) {
		outs.add(new StreamHolder(out, LEVEL_DEBUG, true, false));
	}
	
	public void log(String message, int level) {
		log(message, level, false);
	}
	
	private void log(String message, int level, boolean removeDescriptor) {
		if(minLevel > level) return;
		for (StreamHolder sh : outs) {
			if (sh.level <= level)
				addLog(message, sh, level, removeDescriptor);
		}
	}
	
	private void addLog(String message, StreamHolder sh, int level, boolean removeDescriptor) {
		String full = "";
		full += "[" + label;
		Calendar c = Calendar.getInstance();
		if (sh.showDate || sh.showTime) {
			full = full + " | ";
			if (sh.showDate) {
				full += c.get(Calendar.YEAR) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.DAY_OF_MONTH);
			}
			if (sh.showTime) {
				full += c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
			}
			full = full + "] ";
		} else {
			full += "] ";
		}
		
		if (removeDescriptor) {
			full = "                                                                                      ".substring(0, full.length());
		}
		
		full += levelToString(level) + ": ";
		
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
	
	private void setMinimumLevel(int minLevel) {
		this.minLevel = minLevel;
	}
	
	private void log(String message) {
		log(message, LEVEL_DEBUG);
	}
}
