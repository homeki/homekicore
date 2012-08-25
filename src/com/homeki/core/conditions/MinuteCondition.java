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
	
	protected MinuteCondition() {}
	
	public static class Builder {
		public String weekday = "";
		public String day = "";
		public int hour;
		public int minute;
		
		public Builder weekday(String weekday) {
			this.weekday = weekday;
			return this;
		}
		
		public Builder day(String day) {
			this.day = day;
			return this;
		}
		
		public Builder hour(int hour) {
			this.hour = hour;
			return this;
		}
		
		public Builder minute(int minute) {
			this.minute = minute;
			return this;
		}
		
		public MinuteCondition build() {
			return new MinuteCondition(this);
		}
	}
	
	private MinuteCondition(Builder builder) {
		this.day = builder.day;
		this.weekday = builder.weekday;
		this.hour = builder.hour;
		this.minute = builder.minute;
	}
	
	public String getDay() {
		return day;
	}
	
	public String getWeekday() {
		return weekday;
	}
	
	public int getHour() {
		return hour;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public boolean check(Event e) {
		if (e instanceof MinuteChangedEvent) {
			MinuteChangedEvent mce = (MinuteChangedEvent)e;
			status = true;
			
			if (this.day.length() > 0) {
				String day = String.valueOf(mce.day);
				Pattern p = Pattern.compile( "(^|,)" + day + "(,|$)");
				status &= p.matcher(this.day).find();;
			}
			
			if (this.weekday.length() > 0) {
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
