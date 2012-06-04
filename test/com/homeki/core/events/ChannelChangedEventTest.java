package com.homeki.core.events;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.homeki.core.conditions.ChannelChangedCondition;
import com.homeki.core.conditions.Condition;

public class ChannelChangedEventTest {
	int channel = 1;
	int deviceId = 1;
	int value = 1;
	
	@Test
	public void testEqual() throws Exception {
		ChannelChangedCondition ccc =  new ChannelChangedCondition(deviceId, channel, value, Condition.EQ);
		
		Assert.assertTrue(ccc.check(new ChannelChangedEvent(deviceId, channel, value)));
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId + 1, channel, value)));
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId, channel + 1, value)));
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId, channel, value + 1)));
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId, channel, value - 1)));
	}
	
	@Test
	public void testGreaterThan() throws Exception {
		ChannelChangedCondition ccc =  new ChannelChangedCondition(deviceId, channel, value, Condition.GT);
		
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId, channel, value)));
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId, channel, value - 1)));
		Assert.assertTrue(ccc.check(new ChannelChangedEvent(deviceId, channel, value + 1)));
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId + 1, channel, value + 1)));
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId, channel + 1, value + 1)));
	}
	
	@Test
	public void testLessThan() throws Exception {
		ChannelChangedCondition ccc =  new ChannelChangedCondition(deviceId, channel, value, Condition.LT);
		
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId, channel, value)));
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId, channel, value + 1)));
		Assert.assertTrue(ccc.check(new ChannelChangedEvent(deviceId, channel, value - 1)));
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId + 1, channel, value - 1)));
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId, channel+1, value - 1)));
	}
}
