package com.homeki.core.device;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TimerTrigger extends Trigger {
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
}
