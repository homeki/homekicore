package com.homeki.core.events;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.homeki.core.conditions.Condition;
import com.homeki.core.conditions.MinuteCondition;
import com.homeki.core.conditions.MinuteCondition.Builder;
import com.homeki.core.events.MinuteChangedEvent;

public class MinuteChangedEventTest {
	int hour = 15;
	int minute = 32;
	
	@Test
	public void equalTest() throws Exception {
		String days = "1,15,23";
		String weekdays = "1,2,3";
		Builder builder = new MinuteCondition.Builder();
		builder.day(days).hour(hour).minute(minute).timeOperator(Condition.EQ);
		MinuteCondition mcc = builder.build();
		
		Assert.assertTrue(mcc.check(new MinuteChangedEvent(1, 15, hour, minute)));
		
		//Fail because of incorrect day
		Assert.assertFalse(mcc.check(new MinuteChangedEvent(1, 5, hour, minute)));
		
		//Fail because of incorrect hour
		Assert.assertFalse(mcc.check(new MinuteChangedEvent(1, 15, hour + 1, minute)));
		
		//Fail because of incorrect minute
		Assert.assertFalse(mcc.check(new MinuteChangedEvent(1, 15, hour, minute + 1)));
		
		builder = new MinuteCondition.Builder();
		builder.weekday(weekdays).hour(hour).minute(minute).timeOperator(Condition.EQ);
		mcc = builder.build();

		Assert.assertTrue(mcc.check(new MinuteChangedEvent(1, 15, hour, minute)));
		//Fail because of incorrect weekday
		Assert.assertFalse(mcc.check(new MinuteChangedEvent(5, 15, hour, minute)));
	}
	
	@Test
	public void gtTest() throws Exception {
		Builder builder = new MinuteCondition.Builder();
		builder.hour(hour).minute(minute).timeOperator(Condition.GT);
		MinuteCondition mcc = builder.build();
		
		Assert.assertTrue(mcc.check(new MinuteChangedEvent(1, 15, hour + 1, minute)));
		Assert.assertTrue(mcc.check(new MinuteChangedEvent(1, 15, hour + 1, 0)));
		Assert.assertTrue(mcc.check(new MinuteChangedEvent(1, 15, hour, minute + 1)));

		Assert.assertFalse(mcc.check(new MinuteChangedEvent(1, 5, hour, minute)));
		Assert.assertFalse(mcc.check(new MinuteChangedEvent(1, 15, hour - 1, minute)));
		Assert.assertFalse(mcc.check(new MinuteChangedEvent(1, 15, hour - 1, 59)));
		Assert.assertFalse(mcc.check(new MinuteChangedEvent(1, 15, hour, minute - 1)));
	}
	
	@Test
	public void ltTest() throws Exception {
		Builder builder = new MinuteCondition.Builder();
		builder.hour(hour).minute(minute).timeOperator(Condition.LT);
		MinuteCondition mcc = builder.build();
		
		Assert.assertTrue(mcc.check(new MinuteChangedEvent(1, 15, hour - 1, minute)));
		Assert.assertTrue(mcc.check(new MinuteChangedEvent(1, 15, hour - 1, minute + 1)));
		Assert.assertTrue(mcc.check(new MinuteChangedEvent(1, 15, hour, minute - 1)));

		Assert.assertFalse(mcc.check(new MinuteChangedEvent(1, 5, hour, minute)));
		Assert.assertFalse(mcc.check(new MinuteChangedEvent(1, 15, hour + 1, minute)));
		Assert.assertFalse(mcc.check(new MinuteChangedEvent(1, 15, hour, minute + 1)));
		Assert.assertFalse(mcc.check(new MinuteChangedEvent(1, 15, hour + 1, 0)));
	}
}
