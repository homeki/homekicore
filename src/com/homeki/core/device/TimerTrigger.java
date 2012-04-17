package com.homeki.core.device;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TimerTrigger extends Trigger {
	@Column
	private Integer secondsFromMidnight;
	
	@Column
	private Integer repeatType;
	
	@Column
	private Integer days;
	
	public Integer getSecondsFromMidnight() {
		return secondsFromMidnight;
	}
	
	public void setSecondsFromMidnight(Integer seconds) {
		this.secondsFromMidnight = seconds;
	}
	
	public Integer getRepeatType() {
		return repeatType;
	}
	
	public void setRepeatType(Integer repeatType) {
		this.repeatType = repeatType;
	}
	
	public Integer getDays() {
		return days;
	}
	
	public void setDays(Integer day) {
		this.days = day;
	}

	@Override
	public String getMeta() {
		return String.format("%02d:%02d", secondsFromMidnight / 3600, secondsFromMidnight % 3600 / 60);
	}

	@Override
	public String getType() {
		return "timer";
	}
}
