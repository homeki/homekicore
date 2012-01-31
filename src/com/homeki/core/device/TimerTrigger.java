package com.homeki.core.device;

public class TimerTrigger extends Trigger {
	private Integer time;
	private Integer repeat;
	private Integer day;
	
	public Integer getTime() {
		return time;
	}
	
	public void setTime(Integer time) {
		this.time = time;
	}
	
	public Integer getRepeat() {
		return repeat;
	}
	
	public void setRepeat(Integer repeat) {
		this.repeat = repeat;
	}
	
	public Integer getDay() {
		return day;
	}
	
	public void setDay(Integer day) {
		this.day = day;
	}
}
