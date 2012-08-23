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
	
	@Column
	public int timeOperator;
	
	protected MinuteCondition() {}
	
	public static class Builder {
		public String weekday = "";
		public String day = "";
		public int hour;
		public int minute;
		public int timeOperator = Condition.IGNORE;
		
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
		
		public Builder timeOperator(int timeOperator) {
			this.timeOperator = timeOperator;
			return this;
		}
		
		public MinuteCondition build() {
			return new MinuteCondition(this);
		}
		
	}
	
	private MinuteCondition(Builder builder) {
		day = builder.day;
		weekday = builder.weekday;
		hour = builder.hour;
		minute = builder.minute;
		timeOperator = builder.timeOperator;
	}
	
	public boolean check(Event e) {
		if (e instanceof MinuteChangedEvent) {
			MinuteChangedEvent mce = (MinuteChangedEvent) e;
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
			
			switch (this.timeOperator) {
			case EQ:
				status &= evalute(mce.hour, hour, EQ);
				status &= evalute(mce.minute, minute, EQ);
				break;
			case LT:
				if (!evalute(mce.hour, hour, LT)) {
					status &= evalute(mce.hour, hour, EQ);
					status &= evalute(mce.minute, minute, LT);
				}
				break;
			case GT:
				if (!evalute(mce.hour, hour, GT)) {
					status &= evalute(mce.hour, hour, EQ);
					status &= evalute(mce.minute, minute, GT);
				}
				break;
			case IGNORE:
				break;
			default:
				break;
			}
		}
		return status;
	}
	
	@Override
	public String toString() {
		return "At " + hour + ":" + minute + " (day: " + day + ", weekday: " + weekday + ")";
	}
	
	@Override
	public String getType() {
		return "minutechanged";
	}
}
