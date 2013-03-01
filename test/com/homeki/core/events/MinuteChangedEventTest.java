package com.homeki.core.events;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.homeki.core.conditions.MinuteCondition;

public class MinuteChangedEventTest {
	int hour = 15;
	int minute = 32;
	
	@Test
	public void equalTest() throws Exception {
		String days = "1,15,23";
		String weekdays = "1,2,3";
		MinuteCondition mcc = new MinuteCondition(days, weekdays, hour, minute);
		
		Assert.assertTrue(mcc.check(new MinuteChangedEvent(1, 15, hour, minute)));
		
		//Fail because of incorrect day
		Assert.assertFalse(mcc.check(new MinuteChangedEvent(1, 5, hour, minute)));
		
		//Fail because of incorrect hour
		Assert.assertFalse(mcc.check(new MinuteChangedEvent(1, 15, hour + 1, minute)));
		
		//Fail because of incorrect minute
		Assert.assertFalse(mcc.check(new MinuteChangedEvent(1, 15, hour, minute + 1)));
		
		mcc = new MinuteCondition("*", weekdays, hour, minute);

		Assert.assertTrue(mcc.check(new MinuteChangedEvent(1, 15, hour, minute)));
		//Fail because of incorrect weekday
		Assert.assertFalse(mcc.check(new MinuteChangedEvent(5, 15, hour, minute)));
	}
}
