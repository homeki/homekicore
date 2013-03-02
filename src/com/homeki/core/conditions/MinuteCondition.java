package com.homeki.core.conditions;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.homeki.core.events.Event;
import com.homeki.core.events.MinuteChangedEvent;

@Entity
public class MinuteCondition extends Condition {
	@Column
	public String weekday;
	
	@Column
	public String day;
	
	@Column
	public int hour;
	
	@Column
	public int minute;
	
	public MinuteCondition() {
		
	}
	
	public MinuteCondition(String day, String weekday, int hour, int minute) {
		this.day = day;
		this.weekday = weekday;
		this.hour = hour;
		this.minute = minute;
	}
	
	public String getDay() {
		return day;
	}
	
	public void setDay(String day) {
		this.day = day;
	}
	
	public String getWeekday() {
		return weekday;
	}
	
	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}
	
	public int getHour() {
		return hour;
	}
	
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public void setMinute(int minute) {
		this.minute = minute;
	}
	
	public boolean check(Event e) {
		if (e instanceof MinuteChangedEvent) {
			MinuteChangedEvent mce = (MinuteChangedEvent)e;
			status = true;
			
			if (!this.day.equals("*")) {
				String day = String.valueOf(mce.day);
				Pattern p = Pattern.compile( "(^|,)" + day + "(,|$)");
				status &= p.matcher(this.day).find();
			}
			
			if (!this.weekday.equals("*")) {
				String weekday = String.valueOf(mce.weekday);
				status &= this.weekday.contains(weekday);
			}
			
			status &= hour == mce.hour && minute == mce.minute;
		}
		
		return status;
	}
	
	@Override
	public String getType() {
		return "minute";
	}
}
