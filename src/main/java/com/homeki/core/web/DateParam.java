package com.homeki.core.web;

import com.homeki.core.http.ApiException;
import com.homeki.core.main.Util;

import java.text.ParseException;
import java.util.Date;

public class DateParam {
	private final Date date;

	public DateParam(String value) {
		this.date = extractDate(value);
	}

	public Date value() {
		return date;
	}

	private Date extractDate(String value) {
		try {
			try {
				return Util.getDateTimeFormat().parse(value);
			} catch (ParseException ex) {
				return Util.getDateFormat().parse(value);
			}
		} catch (ParseException e) {
			throw new ApiException("Could not parse " + value + " as a date.");
		}
	}
}
