package com.homeki.core.test.unit;

import junit.framework.Assert;

import org.junit.Test;

import com.homeki.core.conditions.Condition;
import com.homeki.core.triggers.Trigger;

public class TriggerTest {
	@Test
	public void triggerTest() throws Exception {
		Condition dc = new DummyCondition(true);
		DummyAction da =  new DummyAction();
		Trigger t = new Trigger(dc, da);
		Assert.assertTrue(t.check(null));
		Assert.assertFalse(da.getTriggered());
		t.execute(null);
		Assert.assertTrue(da.getTriggered());

		dc = new DummyCondition(false);
		t = new Trigger(dc, da);
		Assert.assertFalse(t.check(null));
	}
}
