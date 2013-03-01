package com.homeki.core.http.json;

import com.homeki.core.conditions.MinuteCondition;


public class JsonMinuteCondition extends JsonCondition {
	public String weekday;
	public String day;
	public Integer hour;
	public Integer minute;
	
	public JsonMinuteCondition(MinuteCondition cond) {
		super(cond);
		this.weekday = cond.getWeekday();
		this.day = cond.getDay();
		this.hour = cond.getHour();
		this.minute = cond.getMinute();
	}
}
