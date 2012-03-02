package com.homeki.core.test.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TimerTriggerTest {
	public abstract class StoppableThread extends Thread {
		private boolean shutdown;

		@Override
		public void run() {
			while (!shutdown)
				iteration();
		}
		
		public void shutdown() {
			shutdown = true;
		}
		
		protected abstract void iteration();
	}
	
	public class JsonTriggerTimer {
		public String name;
		public Integer newValue;
		public Integer time;
		public Integer repeatType;
		public Integer days;
		
		public String toString() {
			return name;
		}
	}
	
	@Test
	public void testAdd() throws Exception {
		JsonTriggerTimer tmr = new JsonTriggerTimer();
		tmr.name = "name with spaces";
		tmr.newValue = 55;
		tmr.time = 12122;
		tmr.repeatType = 1;
		tmr.days = 4;
		assertEquals(200, TestUtil.sendPost("/trigger/timer/add", tmr).statusCode);
		
		tmr.name = "another name with spaces";
		tmr.newValue = 200;
		tmr.time = 99;
		tmr.repeatType = 2;
		tmr.days = 3;
		assertEquals(200, TestUtil.sendPost("/trigger/timer/add", tmr).statusCode);
	}
	
	@Test
	public void testTwoThreadsGet() throws Exception {
		StoppableThread t1 = new StoppableThread() {
			@Override
			public void iteration() {
				try {
					TestUtil.sendAndParseAsJson("/trigger/timer/get?triggerid=2", JsonTriggerTimer.class);
					Thread.yield();
				} catch (Exception e) {
					fail("Exception parsing JSON.");
				}
			}
		};
		
		StoppableThread t2 = new StoppableThread() {
			@Override
			public void iteration() {
				try {
					TestUtil.sendAndParseAsJson("/trigger/timer/get?triggerid=1", JsonTriggerTimer.class);
					Thread.yield();
				} catch (Exception e) {
					fail("Exception parsing JSON.");
				}
			}
		};

		t1.start();
		t2.start();
		
		Thread.sleep(5000);
		
		t1.shutdown();
		t2.shutdown();
		t1.join();
		t2.join();
	}
}
