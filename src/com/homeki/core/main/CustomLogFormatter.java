package com.homeki.core.main;

import java.util.Calendar;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomLogFormatter extends Formatter {
	@Override
	public String format(LogRecord log) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(log.getMillis());
		return "[" + Util.getDateTimeFormat().format(c.getTime()) + " | " + log.getLevel().getName() + " | " + Thread.currentThread().getName() + "] " + log.getMessage() + "\n";
	}
}
