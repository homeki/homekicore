package com.homeki.core.logging;

import java.util.Calendar;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import com.homeki.core.main.Util;

public class CustomFormatter extends Formatter {
	@Override
	public String format(LogRecord log) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(log.getMillis());
		return "[" + Util.getDateTimeFormat().format(c.getTime()) + " | " + log.getLevel().getName() + " | " + Thread.currentThread().getName() + "] " + log.getMessage() + " (" + log.getLoggerName() + ")\n";
	}
}
