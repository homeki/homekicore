package com.homeki.core.events;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.homeki.core.conditions.ChannelChangedCondition;
import com.homeki.core.conditions.Condition;
import com.homeki.core.device.Device;
import com.homeki.core.device.mock.MockSwitch;

public class ChannelChangedEventTest {
	private int channel = 1;
	private int deviceId = 1;
	private int value = 1;
	private Device device = new MockSwitch(1);
	
	@Test
	public void testEqual() throws Exception {
		ChannelChangedCondition ccc =  new ChannelChangedCondition(device, channel, value, Condition.EQ);
		
		Assert.assertTrue(ccc.check(new ChannelChangedEvent(deviceId, channel, value)));
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId + 1, channel, value)));
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId, channel + 1, value)));
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId, channel, value + 1)));
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId, channel, value - 1)));
	}
	
	@Test
	public void testGreaterThan() throws Exception {
		ChannelChangedCondition ccc =  new ChannelChangedCondition(device, channel, value, Condition.GT);
		
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId, channel, value)));
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId, channel, value - 1)));
		Assert.assertTrue(ccc.check(new ChannelChangedEvent(deviceId, channel, value + 1)));
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId + 1, channel, value + 1)));
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId, channel + 1, value + 1)));
	}
	
	@Test
	public void testLessThan() throws Exception {
		ChannelChangedCondition ccc =  new ChannelChangedCondition(device, channel, value, Condition.LT);
		
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId, channel, value)));
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId, channel, value + 1)));
		Assert.assertTrue(ccc.check(new ChannelChangedEvent(deviceId, channel, value - 1)));
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId + 1, channel, value - 1)));
		Assert.assertFalse(ccc.check(new ChannelChangedEvent(deviceId, channel+1, value - 1)));
	}
}
